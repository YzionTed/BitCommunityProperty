package com.bit.communityProperty.fragment.main.bean;

/**
 * Created by DELL60 on 2018/2/25.
 */

public class WeatherInfoBean {

    /**
     * aqi : 84
     * high : -3.0℃
     * low : -13.0℃
     * quality : 良
     * type : 晴
     */

    private String aqi;
    private String high;
    private String low;
    private String quality;
    private String type;

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
