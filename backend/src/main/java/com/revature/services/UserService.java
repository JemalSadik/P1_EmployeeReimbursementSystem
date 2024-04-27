package com.revature.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.enums.Roles;
import com.revature.models.User;
import com.revature.daos.UserDAO;

public class UserService {

    private UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<User> getAllUsers() {
        // TODO: get logged in user
        /*
        if the logged in user has a wrong Role, throw exception
        if user is managerRole return all users
        */
        return userDAO.findAll();
    }

    public User createUser(User user, String role) {
        String roleDesc = Roles.valueOf(role).getDescription();
        if (!roleDesc.isEmpty()) {
            user.setRole(roleDesc);
            // TODO
            return userDAO.save(user);
        }
        // TODO: Throw an exception
        return new User();
    }

    public User updateUser(String role, int userid) {
        Optional<User> checkUser = userDAO.findById(userid);
        if (checkUser.isPresent()) {
            String roleDesc = Roles.valueOf(role).getDescription();
            if (!roleDesc.isEmpty()) {
                User user = checkUser.get();
                user.setRole(roleDesc);
                // TODO
                return userDAO.save(user);
            }
        }
        // TODO
        return new User();
    }

    public User deleteUser (int userid) {
        Optional<User> checkUser = userDAO.findById(userid);
        if (checkUser.isPresent()) {
            userDAO.deleteById(userid);

            // TODO
            return (checkUser.get());
        }
        // TODO
        return new User();
    }
}