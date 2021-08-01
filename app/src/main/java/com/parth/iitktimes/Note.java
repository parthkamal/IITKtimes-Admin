package com.parth.iitktimes;

public class Note {
    private String downloadUrl,semester,branch,uniqueKey;
    // we have used downloadUrl and unique key to access the document for future purpose

    //empty constructor
    public Note() {
    }

    public Note(String downloadUrl, String semester, String branch, String uniqueKey) {
        this.downloadUrl = downloadUrl;
        this.semester = semester;
        this.branch = branch;
        this.uniqueKey = uniqueKey;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
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

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }
}
