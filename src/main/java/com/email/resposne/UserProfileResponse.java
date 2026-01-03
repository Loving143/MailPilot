package com.email.resposne;

import com.email.entity.Person;

public class UserProfileResponse {
    private String name;
    private String email;
    private String mobNo;

    // Constructors
    public UserProfileResponse(Person person) {
        this.name = person.getName();
        this.email = person.getEmail();
        this.mobNo = person.getMobNo();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

}
