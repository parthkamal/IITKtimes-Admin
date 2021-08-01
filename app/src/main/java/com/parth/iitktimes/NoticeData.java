package com.parth.iitktimes;

public class NoticeData {
    String NoticeTitle, NoticeDate, NoticeTime, downloadUrl, NoticeDescription, uniqueKey;

    public NoticeData() {
    }

    public NoticeData(String noticeTitle, String noticeDate, String noticeTime, String downloadUrl, String noticeDescription, String uniqueKey) {
        NoticeTitle = noticeTitle;
        NoticeDate = noticeDate;
        NoticeTime = noticeTime;
        this.downloadUrl = downloadUrl;
        NoticeDescription = noticeDescription;
        this.uniqueKey = uniqueKey;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getNoticeTitle() {
        return NoticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        NoticeTitle = noticeTitle;
    }

    public String getNoticeDate() {
        return NoticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        NoticeDate = noticeDate;
    }

    public String getNoticeTime() {
        return NoticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        NoticeTime = noticeTime;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getNoticeDescription() {
        return NoticeDescription;
    }

    public void setNoticeDescription(String noticeDescription) {
        NoticeDescription = noticeDescription;
    }
}