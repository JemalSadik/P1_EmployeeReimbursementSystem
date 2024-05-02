package com.revature.models.dtos;

public class IncomingUserDto {
    private String firstname;

    private String lastname;

    private String Password;

    private String username;

    public IncomingUserDto() {
    }

    public IncomingUserDto(String firstname, String lastname, String password, String username) {
        this.firstname = firstname;
        this.lastname = lastname;
        Password = password;
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPassword() {
        return Password;
    }

    public String getUsername() {
        return username;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "IncomingUserDto{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", Password='" + Password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
