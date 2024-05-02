package com.revature.controllers;

import com.revature.models.Reimbursement;
import com.revature.models.dtos.IncomingReimbursementDTO;
import com.revature.services.ReimbursementService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reimbursements")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ReimbursementController {

    private ReimbursementService reimbService;

    @Autowired
    public ReimbursementController(ReimbursementService reimbService) {
        this.reimbService = reimbService;
    }

    // get all reimbursements (manager)
    @GetMapping
    public ResponseEntity<?> getAllReimbursements(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).body("You must be logged in to see all reimbursements");
        }
        String role = (String) session.getAttribute("role");
        if (!role.equalsIgnoreCase("Manager")) {
            return ResponseEntity.status(403).body("You must be a MANAGER to see all reimbursements");
        }
        List<Reimbursement> reimbursements = reimbService.getAllReimbursements();
        return ResponseEntity.ok(reimbursements);
    }

    // get all pending reimbursements (manager)
    @GetMapping("/pending")
    public ResponseEntity<?> getAllPendingReimbursements(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).body("You must be logged in to see all pending reimbursements");
        }
        String role = (String) session.getAttribute("role");
        if (!role.equalsIgnoreCase("Manager")) {
            return ResponseEntity.status(403).body("You must be a MANAGER to see all pending reimbursements");
        }
        List<Reimbursement> pendingReimb = reimbService.getAllPendingReimbursement();
        return ResponseEntity.ok(pendingReimb);
    }

    // get all reimbursements by user ID (user)
    @GetMapping("/user")
    public ResponseEntity<?> getAllReimbursementsByUserId(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).body("You must be logged in to see all your reimbursements");
        }
        int userId = (int) session.getAttribute("userId");
        try {
            List<Reimbursement> userReimbursements = reimbService.getAllReimbursementsByUserId(userId);
            return ResponseEntity.ok(userReimbursements);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // get all pending reimbursements for user ID (user)
    @GetMapping("/user/pending")
    public ResponseEntity<?> getAllPendingReimbursementsByUserId(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).body("You must be logged in to see all your pending reimbursements");
        }
        int userId = (int) session.getAttribute("userId");
        try {
            List<Reimbursement> pendingUserReimbursements = reimbService.getAllPendingReimbursementsByUserId(userId);
            return ResponseEntity.ok(pendingUserReimbursements);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // Create a new reimbursement for a user ID (user)
    @PostMapping
    public ResponseEntity<Object> createReimbursement(@RequestBody IncomingReimbursementDTO reimbursementDTO, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).body("You must be logged in to create a reimbursement");
        }
        int userId = (int) session.getAttribute("userId");
        // Set user ID
        reimbursementDTO.setUserId(userId);
        // Set status to "Pending"
        reimbursementDTO.setStatus("Pending");
        try {
            Reimbursement reimb = reimbService.createReimbursement(reimbursementDTO);
            return ResponseEntity.status(201).body(reimb);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // update a reimbursement status by reimbursement ID (manager)
    @PatchMapping("/{reimbId}/status")
    public ResponseEntity<Object> updateReimbursementStatus(@RequestBody String status, @PathVariable int reimbId, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).body("You must be logged in to update a reimbursement's status");
        }
        String role = (String) session.getAttribute("role");
        if (!role.equalsIgnoreCase("Manager")) {
            return ResponseEntity.status(403).body("You must be a MANAGER to update a reimbursement's status");
        }
        try {
            Reimbursement reimb = reimbService.updateReimbursementStatus(reimbId, status);
            return ResponseEntity.ok(reimb);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * update a pending reimbursement description by reimbursement ID (user)
     * @param description the updated reimbursement description
     * @param reimbId the reimbursement ID for the reimbursement to update
     * @return ResponseEntity with the updated reimbursement or appropriate error message
     */
    @PatchMapping("/{reimbId}/description")
    public ResponseEntity<Object> updatePendingReimbursementDescription(@RequestBody String description, @PathVariable int reimbId, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).body("You must be logged in to update a reimbursement's description");
        }
        try {
            Reimbursement reimb = reimbService.updatePendingReimbursementDescription(reimbId, description);
            return ResponseEntity.ok(reimb);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

}
