package com.baidu.ueditor.qcloud;


import com.baidu.ueditor.ActionEnter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fhp
 * @date 2017/12/2
 */
public class UEditorQCloudServlet extends HttpServlet {

    /**
     * 配置文件存储目录
     */
    private String rootPath;
    /**
     * 万象优图样式描述
     */
    private String imageStyle;

    Integer appId;
    String secretId;
    String secretKey;

    String bucket;
    String bucketDomain;

    String region;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        resp.setHeader("Content-Type","text/html");
        PrintWriter out = resp.getWriter();
        QCloudParam qCloudParam=new QCloudParam();
        qCloudParam.setAppid(appId);
        qCloudParam.setSecretId(secretId);
        qCloudParam.setSecretKey(secretKey);
        qCloudParam.setBucket(bucket);
        qCloudParam.setDomain(bucketDomain);
        qCloudParam.setRegion(region);
        qCloudParam.setImageStyle(imageStyle);
        out.write(new ActionEnter( req, rootPath,qCloudParam ).exec());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        rootPath=config.getInitParameter("rootPath");
        imageStyle=config.getInitParameter("imageStyle");
        appId=Integer.parseInt(config.getInitParameter("appId"));
        secretId=config.getInitParameter("secretId");
        secretKey=config.getInitParameter("secretKey");
        bucket=config.getInitParameter("bucket");
        bucketDomain=config.getInitParameter("bucketDomain");
        region=config.getInitParameter("region");
    }
}
