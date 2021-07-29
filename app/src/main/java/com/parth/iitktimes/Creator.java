package com.parth.iitktimes;

public class Creator {
    private String name,design,email,phone,downloadUrl;

//    overloading the constructor
    public Creator(String name, String design, String email, String phone, String downloadUrl) {
        this.name = name;
        this.design = design;
        this.email = email;
        this.phone = phone;
        this.downloadUrl = downloadUrl;
    }

    //empty constructor
    public Creator() {
    }

    public Creator(String name, String design, String email, String phone) {
        this.name = name;
        this.design = design;
        this.email = email;
        this.phone = phone;
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
