package com.movieticket.dto;

import com.movieticket.entity.User;
import com.movieticket.entity.User.UserRole;

public class userDTO {

    private Long id;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    private String name;

   
    private String email;

    
    private String phoneNumber;
    private UserRole role = UserRole.USER;

    private boolean active;
    private boolean verified;

    public userDTO(User user){
        this.id=user.getId();

        this.name=user.getName();
        this.email=user.getEmail();
        this.phoneNumber=user.getPhoneNumber();
        this.role=user.getRole();
        this.active=user.isActive();
        this.verified=user.isVerified();

    }
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
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public boolean isVerified() {
        return verified;
    }
    public void setVerified(boolean verified) {
        this.verified = verified;
    }

}
