package com.example.goodweather.bean;

import java.util.List;

public class MinutePrecResponse {

    /**
     * code : 200
     * updateTime : 2022-01-07T17:05+08:00
     * fxLink : http://hfx.link/1
     * summary : 未来两小时无降水
     * minutely : [{"fxTime":"2022-01-07T17:05+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T17:10+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T17:15+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T17:20+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T17:25+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T17:30+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T17:35+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T17:40+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T17:45+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T17:50+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T17:55+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T18:00+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T18:05+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T18:10+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T18:15+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T18:20+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T18:25+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T18:30+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T18:35+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T18:40+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T18:45+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T18:50+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T18:55+08:00","precip":"0.0","type":"rain"},{"fxTime":"2022-01-07T19:00+08:00","precip":"0.0","type":"rain"}]
     * refer : {"sources":["QWeather"],"license":["no commercial use"]}
     */

    private String code;
    private String updateTime;
    private String fxLink;
    private String summary;
    private ReferBean refer;
    private List<MinutelyBean> minutely;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFxLink() {
        return fxLink;
    }

    public void setFxLink(String fxLink) {
        this.fxLink = fxLink;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ReferBean getRefer() {
        return refer;
    }

    public void setRefer(ReferBean refer) {
        this.refer = refer;
    }

    public List<MinutelyBean> getMinutely() {
        return minutely;
    }

    public void setMinutely(List<MinutelyBean> minutely) {
        this.minutely = minutely;
    }

    public static class ReferBean {
        private List<String> sources;
        private List<String> license;

        public List<String> getSources() {
            return sources;
        }

        public void setSources(List<String> sources) {
            this.sources = sources;
        }

        public List<String> getLicense() {
            return license;
        }

        public void setLicense(List<String> license) {
            this.license = license;
        }
    }

    public static class MinutelyBean {
        /**
         * fxTime : 2022-01-07T17:05+08:00
         * precip : 0.0
         * type : rain
         */

        private String fxTime;
        private String precip;
        private String type;

        public String getFxTime() {
            return fxTime;
        }

        public void setFxTime(String fxTime) {
            this.fxTime = fxTime;
        }

        public String getPrecip() {
            return precip;
        }

        public void setPrecip(String precip) {
            this.precip = precip;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
