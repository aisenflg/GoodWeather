package com.example.mvplibrary.bean;

import org.litepal.crud.LitePalSupport;

public class AppVersion extends LitePalSupport {

    /**
     * code : 0
     * message :
     * data : {"buildBuildVersion":"1","forceUpdateVersion":"","forceUpdateVersionNo":"","needForceUpdate":false,"downloadURL":"https://www.pgyer.com/app/installUpdate/6733c4d5a06ad56e1c2e307438c9b9be?sig=gEw0UA07Q3285TR1IFs2ABXhuEw9NiDXPNnH1gGk3cl3tUEFsX9lGkOMCVfpk9Md&amp;forceHttps=","buildHaveNewVersion":false,"buildVersionNo":"1","buildVersion":"1.0","buildUpdateDescription":"","appKey":"c974e9943f9d28dc642a626eec09f69a","buildKey":"6733c4d5a06ad56e1c2e307438c9b9be","buildName":"GoodWeather","buildIcon":"https://www.pgyer.com/image/view/app_icons/bd0152574a88a2eab0a20523dc123a64/120","buildFileKey":"6d2ecc4016fd88b0e6247bfa5b98a1c0.apk","buildFileSize":"39266480"}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean extends LitePalSupport {
        /**
         * buildBuildVersion : 1
         * forceUpdateVersion :
         * forceUpdateVersionNo :
         * needForceUpdate : false
         * downloadURL : https://www.pgyer.com/app/installUpdate/6733c4d5a06ad56e1c2e307438c9b9be?sig=gEw0UA07Q3285TR1IFs2ABXhuEw9NiDXPNnH1gGk3cl3tUEFsX9lGkOMCVfpk9Md&amp;forceHttps=
         * buildHaveNewVersion : false
         * buildVersionNo : 1
         * buildVersion : 1.0
         * buildUpdateDescription :
         * appKey : c974e9943f9d28dc642a626eec09f69a
         * buildKey : 6733c4d5a06ad56e1c2e307438c9b9be
         * buildName : GoodWeather
         * buildIcon : https://www.pgyer.com/image/view/app_icons/bd0152574a88a2eab0a20523dc123a64/120
         * buildFileKey : 6d2ecc4016fd88b0e6247bfa5b98a1c0.apk
         * buildFileSize : 39266480
         */

        private String buildBuildVersion;
        private String forceUpdateVersion;
        private String forceUpdateVersionNo;
        private boolean needForceUpdate;
        private String downloadURL;
        private boolean buildHaveNewVersion;
        private String buildVersionNo;
        private String buildVersion;
        private String buildUpdateDescription;
        private String appKey;
        private String buildKey;
        private String buildName;
        private String buildIcon;
        private String buildFileKey;
        private String buildFileSize;

        public String getBuildBuildVersion() {
            return buildBuildVersion;
        }

        public void setBuildBuildVersion(String buildBuildVersion) {
            this.buildBuildVersion = buildBuildVersion;
        }

        public String getForceUpdateVersion() {
            return forceUpdateVersion;
        }

        public void setForceUpdateVersion(String forceUpdateVersion) {
            this.forceUpdateVersion = forceUpdateVersion;
        }

        public String getForceUpdateVersionNo() {
            return forceUpdateVersionNo;
        }

        public void setForceUpdateVersionNo(String forceUpdateVersionNo) {
            this.forceUpdateVersionNo = forceUpdateVersionNo;
        }

        public boolean isNeedForceUpdate() {
            return needForceUpdate;
        }

        public void setNeedForceUpdate(boolean needForceUpdate) {
            this.needForceUpdate = needForceUpdate;
        }

        public String getDownloadURL() {
            return downloadURL;
        }

        public void setDownloadURL(String downloadURL) {
            this.downloadURL = downloadURL;
        }

        public boolean isBuildHaveNewVersion() {
            return buildHaveNewVersion;
        }

        public void setBuildHaveNewVersion(boolean buildHaveNewVersion) {
            this.buildHaveNewVersion = buildHaveNewVersion;
        }

        public String getBuildVersionNo() {
            return buildVersionNo;
        }

        public void setBuildVersionNo(String buildVersionNo) {
            this.buildVersionNo = buildVersionNo;
        }

        public String getBuildVersion() {
            return buildVersion;
        }

        public void setBuildVersion(String buildVersion) {
            this.buildVersion = buildVersion;
        }

        public String getBuildUpdateDescription() {
            return buildUpdateDescription;
        }

        public void setBuildUpdateDescription(String buildUpdateDescription) {
            this.buildUpdateDescription = buildUpdateDescription;
        }

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getBuildKey() {
            return buildKey;
        }

        public void setBuildKey(String buildKey) {
            this.buildKey = buildKey;
        }

        public String getBuildName() {
            return buildName;
        }

        public void setBuildName(String buildName) {
            this.buildName = buildName;
        }

        public String getBuildIcon() {
            return buildIcon;
        }

        public void setBuildIcon(String buildIcon) {
            this.buildIcon = buildIcon;
        }

        public String getBuildFileKey() {
            return buildFileKey;
        }

        public void setBuildFileKey(String buildFileKey) {
            this.buildFileKey = buildFileKey;
        }

        public String getBuildFileSize() {
            return buildFileSize;
        }

        public void setBuildFileSize(String buildFileSize) {
            this.buildFileSize = buildFileSize;
        }
    }
}
