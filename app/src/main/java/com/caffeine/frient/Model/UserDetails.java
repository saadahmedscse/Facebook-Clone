package com.caffeine.frient.Model;

public class UserDetails {

    String uid, name, email, pass, gender, dob, time, dp;

    public UserDetails() {}

    public UserDetails(String uid, String name, String email, String pass, String gender, String dob, String time, String dp) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.gender = gender;
        this.dob = dob;
        this.time = time;
        this.dp = dp;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public String getTime() {
        return time;
    }

    public String getDp() {
        return dp;
    }
}
