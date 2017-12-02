package com.baidu.ueditor.qcloud;

/**
 * 万象优图参数
 * @author fhp
 * @date 2017/12/2
 */
public class QCloudParam {

    /**
     * 腾讯云appId
     */
    Integer appid;
    /**
     * 腾讯云secretId
     */
    String secretId;
    /**
     * 腾讯云secretKey
     */
    String secretKey;
    /**
     * 腾讯云region
     */
    String region;
    /**
     * 腾讯云bucket
     */
    String bucket;
    /**
     * 万像优图访问域名
     */
    String domain;
    /**
     * 图片style
     */
    String imageStyle;

    public Integer getAppid() {
        return appid;
    }

    public void setAppid(Integer appid) {
        this.appid = appid;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getImageStyle() {
        return imageStyle;
    }

    public void setImageStyle(String imageStyle) {
        this.imageStyle = imageStyle;
    }
}
