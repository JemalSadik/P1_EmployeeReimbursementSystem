package com.revature.controllers;

import com.revature.models.Reimbursement;
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
    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllReimbursements() {
        List<User> users = userService.getAllUsers();

        // TODO: check if list is empty and return appropriate message

        return ResponseEntity.ok(users);
    }

    @PostMapping("/{role}")
    public ResponseEntity <User> createUser(@RequestBody User user, @PathVariable String role){
        User savedUser = userService.createUser(user,role);
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
    public ResponseEntity <User> deleteUser(@PathVariable int uderid) {
        User deletedUser = userService.deleteUser(uderid);
        // TODO: error handling
        return ResponseEntity.ok(deletedUser);
    }

}

