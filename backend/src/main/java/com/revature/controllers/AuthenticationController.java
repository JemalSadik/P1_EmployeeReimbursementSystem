package com.revature.controllers;

import com.revature.models.User;
import com.revature.models.dtos.IncomingUserDto;
import com.revature.models.dtos.OutgoingUserDTO;
import com.revature.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping
@CrossOrigin
public class AuthenticationController {

    private UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody IncomingUserDto userDTO, HttpSession session) {
        Optional<User> optionalUser = userService.loginUser(userDTO);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).body("Login Failed, invalid username or password");
        }
        User user = optionalUser.get();

        session.setAttribute("userId", user.getUserId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole());

        return ResponseEntity.ok(new OutgoingUserDTO(user.getUsername(), user.getFirstname(), user.getLastname(), user.getRole()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).body("No user is logged in");
        }
        session.invalidate();
        return ResponseEntity.ok("You have been logged out!");
    }

}
