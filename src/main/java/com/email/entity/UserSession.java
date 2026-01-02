package com.email.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "usersession_id_seq")
    @SequenceGenerator(name = "usersession_id_seq",allocationSize = 1, initialValue = 1)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Person user;
    private String jwtToken;
    private Boolean isActive = true;
    private LocalDateTime loginTime = LocalDateTime.now();
    private LocalDateTime logoutTime;

    public UserSession(String jwtToken,Person user) {
        this.user=user;
        this.jwtToken = jwtToken;
        this.isActive = true;
    }

    public UserSession() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public LocalDateTime getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(LocalDateTime logoutTime) {
        this.logoutTime = logoutTime;
    }
}
