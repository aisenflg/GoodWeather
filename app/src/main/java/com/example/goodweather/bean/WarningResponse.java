package com.example.goodweather.bean;

import java.io.Serializable;
import java.util.List;

public class WarningResponse implements Serializable {


    /**
     * code : 200
     * updateTime : 2021-11-05T13:45+08:00
     * fxLink : http://hfx.link/3c05
     * warning : [{"id":"10120010620211105092800469772751","sender":"东西湖区气象台","pubTime":"2021-11-05T09:28+08:00","title":"东西湖区气象台发布寒潮黄色预警[III级/较重]","startTime":"2021-11-05T09:27+08:00","endTime":"2021-11-06T09:27+08:00","status":"active","level":"黄色","type":"11B05","typeName":"寒潮","text":"东西湖区气象台2021年11月05日09时27分发布寒潮黄色预警信号：受强冷空气影响，预计6日夜间至8日，我区将出现寒潮天气，大部地区气温下降9～11℃，最低气温出现在8日早晨为2～4℃，并伴有偏北风4～6级，阵风7～9级，请注意防范！","related":""}]
     * refer : {"sources":["12379"],"license":["no commercial use"]}
     */

    private String code;
    private String updateTime;
    private String fxLink;
    private ReferBean refer;
    private List<WarningBean> warning;

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

    public ReferBean getRefer() {
        return refer;
    }

    public void setRefer(ReferBean refer) {
        this.refer = refer;
    }

    public List<WarningBean> getWarning() {
        return warning;
    }

    public void setWarning(List<WarningBean> warning) {
        this.warning = warning;
    }

    public static class ReferBean implements Serializable {
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

    public static class WarningBean implements Serializable {
        /**
         * id : 10120010620211105092800469772751
         * sender : 东西湖区气象台
         * pubTime : 2021-11-05T09:28+08:00
         * title : 东西湖区气象台发布寒潮黄色预警[III级/较重]
         * startTime : 2021-11-05T09:27+08:00
         * endTime : 2021-11-06T09:27+08:00
         * status : active
         * level : 黄色
         * type : 11B05
         * typeName : 寒潮
         * text : 东西湖区气象台2021年11月05日09时27分发布寒潮黄色预警信号：受强冷空气影响，预计6日夜间至8日，我区将出现寒潮天气，大部地区气温下降9～11℃，最低气温出现在8日早晨为2～4℃，并伴有偏北风4～6级，阵风7～9级，请注意防范！
         * related :
         */

        private String id;
        private String sender;
        private String pubTime;
        private String title;
        private String startTime;
        private String endTime;
        private String status;
        private String level;
        private String type;
        private String typeName;
        private String text;
        private String related;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getPubTime() {
            return pubTime;
        }

        public void setPubTime(String pubTime) {
            this.pubTime = pubTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getRelated() {
            return related;
        }

        public void setRelated(String related) {
            this.related = related;
        }
    }
}
