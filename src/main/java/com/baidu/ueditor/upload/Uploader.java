package com.baidu.ueditor.upload;

import com.baidu.ueditor.define.State;
import com.baidu.ueditor.qcloud.QCloudParam;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class Uploader {
	private HttpServletRequest request = null;
	private Map<String, Object> conf = null;
	private QCloudParam qCloudParam;

	public Uploader(HttpServletRequest request, Map<String, Object> conf,QCloudParam qCloudParam) {
		this.request = request;
		this.conf = conf;
		this.qCloudParam=qCloudParam;
	}

	public final State doExec() {
		String filedName = (String) this.conf.get("fieldName");
		State state = null;

		if ("true".equals(this.conf.get("isBase64"))) {
			state = Base64Uploader.save(this.request.getParameter(filedName),
					this.conf,qCloudParam);
		} else {
			state = BinaryUploader.save(this.request, this.conf,qCloudParam);
		}

		return state;
	}
}
