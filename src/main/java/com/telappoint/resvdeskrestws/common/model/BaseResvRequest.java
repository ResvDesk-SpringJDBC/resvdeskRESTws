package com.telappoint.resvdeskrestws.common.model;

public class BaseResvRequest {
    private String deviceType;
    private String clientCode;
    private String langCode;

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getLangCode() {
        return langCode;
    }
}
