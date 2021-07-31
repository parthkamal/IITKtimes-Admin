package com.parth.iitktimes;

import java.io.Serializable;

public class Creator implements Serializable {
    private String name,design,email,phone,downloadUrl,uniquekey;

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public Creator(String name, String design, String email, String phone, String downloadUrl, String uniquekey) {
        this.name = name;
        this.design = design;
        this.email = email;
        this.phone = phone;
        this.downloadUrl = downloadUrl;
        this.uniquekey = uniquekey;
    }

    //empty constructor
    public Creator() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesign() {
        return design;
    }

    public void setDesign(String design) {
        this.design = design;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
