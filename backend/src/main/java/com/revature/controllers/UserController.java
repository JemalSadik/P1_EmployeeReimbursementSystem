package com.revature.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.revature.models.dtos.IncomingUserDto;
import java.util.List;
import com.revature.models.User;
import com.revature.services.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
       this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).body("You must be logged in to update a user account");
        }
        String LoggedInRole = (String) session.getAttribute("role");

        if (!LoggedInRole.equalsIgnoreCase("Manager")) {
            return ResponseEntity.status(403).body("You must be a MANAGER to update  a user account");
        }

        try {
            return ResponseEntity.ok(userService.getAllUsers());
        }catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/{userid}")
    public ResponseEntity <?> getUserById(@PathVariable int userid, HttpSession session) {

        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).body("You must be logged in to update a user account");
        }
        String LoggedInRole = (String) session.getAttribute("role");

        if (!LoggedInRole.equalsIgnoreCase("Manager")) {
            return ResponseEntity.status(403).body("You must be a MANAGER to update  a user account");
        }

        try {
            return ResponseEntity.ok(userService.getUserById(userid));
        } catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity <Object> createUser(@RequestBody IncomingUserDto userDTO){

        try {
            return ResponseEntity.ok(userService.createUser(userDTO));
        }catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity <Object> updateUser(@RequestBody String role, @PathVariable int userId, HttpSession session){

        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).body("You must be logged in to update a user account");
        }
        String LoggedInRole = (String) session.getAttribute("role");

        if (!LoggedInRole.equalsIgnoreCase("Manager")) {
            return ResponseEntity.status(403).body("You must be a MANAGER to update  a user account");
        }
        //String role = "Manager";
        try {
            return ResponseEntity.ok(userService.updateUser(role, userId));
        }catch (Exception e) {
            return ResponseEntity.status(400).body("Update failed");
        }
    }

    @DeleteMapping("/{userid}")
    public ResponseEntity <Object> deleteUser(@PathVariable int userid, HttpSession session) {

        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).body("You must be logged in to update a user account");
        }
        String LoggedInRole = (String) session.getAttribute("role");

        if (!LoggedInRole.equalsIgnoreCase("Manager")) {
            return ResponseEntity.status(403).body("You must be a MANAGER to update  a user account");
        }

        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).body("You must be logged in to update a user account");
        }

        int LoggedUserid = (int) session.getAttribute("userId");

        if (LoggedUserid == userid)
            return ResponseEntity.status(401).body("You cannot delete your own account");

        try {
            return ResponseEntity.ok(userService.deleteUser(userid));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}

