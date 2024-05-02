package com.revature.services;

import java.util.List;
import java.util.Objects;

import com.revature.enums.Status;
import com.revature.models.dtos.IncomingUserDto;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.models.User;
import com.revature.daos.UserDAO;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    /**
     * Create a new User - All users can create a new User Account
     * @param userDTO object to create a new User
     * @return the created reimbursement
     * @throws IllegalArgumentException if no user is found at user ID
     */
    public User createUser(IncomingUserDto userDTO) {
       //Check the validity of all fields of the userDTO object
        if(userDTO.getFirstname().isBlank() || userDTO.getFirstname() == null){
            throw new IllegalArgumentException("Firstname cannot be empty");
        }

        if(userDTO.getLastname().isBlank() || userDTO.getLastname() == null){
            throw new IllegalArgumentException("Lastname cannot be empty");
        }

        if(userDTO.getUsername().isBlank() || userDTO.getUsername() == null){
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if(userDTO.getPassword().isBlank() || userDTO.getPassword() == null){
            throw new IllegalArgumentException("Password cannot be empty");
        }

        User user = new User(userDTO.getFirstname(), userDTO.getLastname(), userDTO.getUsername(), userDTO.getPassword());
        user.setRole("employee");
        return userDAO.save(user);
    }

    /**
     * Create a new User - Only managers can update a User
     * @param role and userId  - of the user to be updated.
     * @return the updated user
     * @throws IllegalArgumentException if no user is found at user ID
     */

    public User updateUser(String role, int userid) {
        if(role.isBlank() || role == null) {
            throw new IllegalArgumentException("Role cannot be empty");
        }
        // Role still needs to be validated - we do allow update, only if the role  it is "pending or 'manager',
        User user = userDAO.findById(userid).orElseThrow(() -> new IllegalArgumentException("No user found for ID: " + userid));
        user.setRole(role.trim());
        return userDAO.save(user);
    }

    public User deleteUser (int userid) {
        User user = userDAO.findById(userid).orElseThrow(() -> new IllegalArgumentException("No user found for ID: " + userid));
        userDAO.deleteById(userid);
        return (user);
    }

    public User getUserById(int userid) {
        User user = userDAO.findById(userid).orElseThrow(() -> new IllegalArgumentException("No user found for ID: " + userid));
        return (user);
    }

}