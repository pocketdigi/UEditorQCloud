 UEditor腾讯万象优图版
----

百度的UEditor默认文件都是传到应用服务器上，会占用应用服务器资源。
这个版本整合了腾讯云万象优图，在编辑时会将图片传到万象优图上，并支持图片样式定义，另外还增加了webp格式的支持（仅是将webp传到万象优图，在不支持webp的设备中访问，配合图片样式，可以转成jpg等格式）。

对于视频以及其他文件上传，因为用得少，现在的版本没有支持。

### 使用方法
自行打包或者下载release里的包，不再需要controller.jsp(用UEditorQCloudServlet代替)

1. 配置servlet:

    * springboot
    
            @Bean
            public ServletRegistrationBean ueditorServletBean(){
                ServletRegistrationBean registrationBean = new ServletRegistrationBean(new UEditorQCloudServlet());
                registrationBean.addUrlMappings("/ueditor");
                Map<String, String> params = new HashMap<>();
                params.put("rootPath", "/data/ueditor");
                params.put("imageStyle", "imageMogr2/thumbnail/360x/format/jpg/interlace/0/quality/86");
                params.put("appId", String.valueOf(appId));
                params.put("secretId", secretId);
                params.put("secretKey", secretKey);
                params.put("bucket", bucket);
                params.put("bucketDomain", bucketDomain);
                params.put("region", region);
                registrationBean.setInitParameters(params);
                return registrationBean;
            }
        * configPath：保存config.json的目录（这里原先用来保存所有ueditor上传的文件的，但我们已经传到了万像优图）
        * imageStyle 万象优图的图片样式，会放到url后面
        * appId 腾讯云appid
        * secretId 腾讯云secretId
        * secretKey 腾讯云secretKey
        * bucket 腾讯云bucket
        * bucketDomain bucket访问域名
        * region bucket所在区域(华东为sh)
    * springmvc的配置参考springboot自行修改
    
2. UEditor初始化配置
     上面的配置将UEditorQCloudServlet映射到/ueditor上，所以在js里初始化UEditor时，要这样配serverUrl:
     
        serverUrl: '/ueditor',

3. 其他配置与原版相同，使用config.json


