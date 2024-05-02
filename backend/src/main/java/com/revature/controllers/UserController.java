package com.revature.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.revature.models.dtos.IncomingUserDto;
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
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        }catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity <?> createUser(@RequestBody IncomingUserDto userDTO){
        try {
            return ResponseEntity.ok(userService.createUser(userDTO));
        }catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PatchMapping("/{userid}")
    public ResponseEntity <?> updateUser(@RequestBody String role, @PathVariable int userid){
        try {
            return ResponseEntity.ok(userService.updateUser(role, userid));
        }catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/{userid}")
    public ResponseEntity <?> deleteUser(@PathVariable int userid) {
        try {
            return ResponseEntity.ok(userService.deleteUser(userid));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/{userid}")
    public ResponseEntity <?> getUserById(@PathVariable int userid) {
        try {
            return ResponseEntity.ok(userService.getUserById(userid));
        } catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

}

