package com.baidu.ueditor.upload;

import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.State;

import com.baidu.ueditor.qcloud.QCloudParam;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public class StorageManager {
	public static final int BUFFER_SIZE = 8192;

	public StorageManager() {
	}

	public static State saveBinaryFile(byte[] data, String path,QCloudParam qCloudParam) {
		File file = new File(path);

		State state = valid(file);

		if (!state.isSuccess()) {
			return state;
		}

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bos.write(data);
			bos.flush();
			bos.close();
		} catch (IOException ioe) {
			return new BaseState(false, AppInfo.IO_ERROR);
		}
		state = saveTmpFile2QCloud(file, path,qCloudParam);
		file.delete();
		return state;
	}

	public static State saveFileByInputStream(InputStream is, String path,
			long maxSize,QCloudParam qCloudParam) {
		State state = null;

		File tmpFile = getTmpFile();

		byte[] dataBuf = new byte[ 2048 ];
		BufferedInputStream bis = new BufferedInputStream(is, StorageManager.BUFFER_SIZE);

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);

			int count = 0;
			while ((count = bis.read(dataBuf)) != -1) {
				bos.write(dataBuf, 0, count);
			}
			bos.flush();
			bos.close();

			if (tmpFile.length() > maxSize) {
				tmpFile.delete();
				return new BaseState(false, AppInfo.MAX_SIZE);
			}

			state = saveTmpFile2QCloud(tmpFile, path,qCloudParam);

			if (!state.isSuccess()) {
				tmpFile.delete();
			}

			return state;
			
		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	public static State saveFileByInputStream(InputStream is, String path,QCloudParam qCloudParam) {
		State state = null;

		File tmpFile = getTmpFile();

		byte[] dataBuf = new byte[ 2048 ];
		BufferedInputStream bis = new BufferedInputStream(is, StorageManager.BUFFER_SIZE);

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);

			int count = 0;
			while ((count = bis.read(dataBuf)) != -1) {
				bos.write(dataBuf, 0, count);
			}
			bos.flush();
			bos.close();

			state = saveTmpFile2QCloud(tmpFile, path,qCloudParam);

			if (!state.isSuccess()) {
				tmpFile.delete();
			}

			return state;
		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static File getTmpFile() {
		File tmpDir = FileUtils.getTempDirectory();
		String tmpFileName = (Math.random() * 10000 + "").replace(".", "");
		return new File(tmpDir, tmpFileName);
	}

	private static State saveTmpFile(File tmpFile, String path) {
		State state = null;
		File targetFile = new File(path);

		if (targetFile.canWrite()) {
			return new BaseState(false, AppInfo.PERMISSION_DENIED);
		}
		try {
			FileUtils.moveFile(tmpFile, targetFile);
		} catch (IOException e) {
			return new BaseState(false, AppInfo.IO_ERROR);
		}

		state = new BaseState(true);
		state.putInfo( "size", targetFile.length() );
		state.putInfo( "title", targetFile.getName() );
		
		return state;
	}

	private static State valid(File file) {
		File parentPath = file.getParentFile();

		if ((!parentPath.exists()) && (!parentPath.mkdirs())) {
			return new BaseState(false, AppInfo.FAILED_CREATE_FILE);
		}

		if (!parentPath.canWrite()) {
			return new BaseState(false, AppInfo.PERMISSION_DENIED);
		}

		return new BaseState(true);
	}

	private static State saveTmpFile2QCloud(File tmpFile, String path,QCloudParam qCloudParam) {
		Credentials cred = new Credentials(qCloudParam.getAppid(), qCloudParam.getSecretId(), qCloudParam.getSecretKey());
		ClientConfig clientConfig = new ClientConfig();
		// 设置bucket所在的区域，比如华南园区：gz； 华北园区：tj；华东园区：sh ；
		clientConfig.setRegion("sh");
		COSClient cosClient = new COSClient(clientConfig, cred);
		UploadFileRequest uploadFileRequest=new UploadFileRequest(qCloudParam.getBucket(),path,tmpFile.getAbsolutePath());
		String result = cosClient.uploadFile(uploadFileRequest);
		JSONObject jsonObject = new JSONObject(result);
		State state=null;
		if(jsonObject.getInt("code")==0){
			JSONObject data = jsonObject.getJSONObject("data");
			String resourcePath = data.getString("resource_path");
			String prefix="/"+qCloudParam.getAppid()+"/"+qCloudParam.getBucket()+"/";
			String cosPath = resourcePath.substring(prefix.length());
			state = new BaseState(true);
			state.putInfo( "size", tmpFile.length() );
			state.putInfo( "title", tmpFile.getName() );
			String url=qCloudParam.getDomain()+cosPath;
			if(qCloudParam.getImageStyle()!=null) {
				url+="?"+qCloudParam.getImageStyle();
			}
			state.putInfo("url",url);
		}else {
			state = new BaseState(false, AppInfo.FAILED_CREATE_FILE);
		}
		cosClient.shutdown();
		return state;
	}
}
