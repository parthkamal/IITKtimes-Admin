package com.parth.iitktimes;

public class book {
    private String title,semester,branch,downloadUrl,uniqueKey;

    public book() {
    }

    public book(String title, String semester, String branch, String downloadUrl, String uniqueKey) {
        this.title = title;
        this.semester = semester;
        this.branch = branch;
        this.downloadUrl = downloadUrl;
        this.uniqueKey = uniqueKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }
}
