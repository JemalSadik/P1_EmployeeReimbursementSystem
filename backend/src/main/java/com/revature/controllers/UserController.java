package com.revature.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.revature.models.User;
import com.revature.services.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
       this.userService = userService;
    }

    // get all users (manager)
    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        // TODO: check if list is empty and return appropriate message

        return ResponseEntity.ok(users);
    }

    @PostMapping("")
    public ResponseEntity <User> createUser(@RequestBody User user){
        user.setRole("Employee");
        User savedUser = userService.createUser(user);
        // TODO: error handling

        return ResponseEntity.ok(savedUser);
    }

    @PatchMapping("/{userid}")
    public ResponseEntity <User> updateUser(@RequestBody String role, @PathVariable int userid){
        User savedUser = userService.updateUser(role, userid);
        // TODO: error handling
        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/{userid}")
    public ResponseEntity <User> deleteUser(@PathVariable int userid) {
        User deletedUser = userService.deleteUser(userid);
        // TODO: error handling
        return ResponseEntity.ok(deletedUser);
    }

}

