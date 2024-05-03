package com.revature.models.dtos;

public class OutgoingUserDTO {

    private int userId;

    private String username;

    private String firstname;

    private String lastname;

    private String role;

    public OutgoingUserDTO() {
    }

    public OutgoingUserDTO(int userId, String role, String username) {
        this.userId = userId;
        this.role = role;
        this.username = username;
    }

    public OutgoingUserDTO(String username, String firstname, String lastname, String role, int userId) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "OutgoingUserDTO{" +
                "username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

}
