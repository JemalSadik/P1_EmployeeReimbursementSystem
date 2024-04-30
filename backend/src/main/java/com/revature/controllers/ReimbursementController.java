package com.revature.controllers;

import com.revature.models.Reimbursement;
import com.revature.models.dtos.IncomingReimbursementDTO;
import com.revature.services.ReimbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reimbursements")
@CrossOrigin
public class ReimbursementController {

    private ReimbursementService reimbService;

    @Autowired
    public ReimbursementController(ReimbursementService reimbService) {
        this.reimbService = reimbService;
    }

    // get all reimbursements (manager)
    @GetMapping
    public ResponseEntity<List<Reimbursement>> getAllReimbursements() {
        List<Reimbursement> reimbursements = reimbService.getAllReimbursements();
        // TODO: catch invalid user role error and return a 403 status code
        return ResponseEntity.ok(reimbursements);
    }

    // get all pending reimbursements (manager)
    @GetMapping("/pending")
    public ResponseEntity<List<Reimbursement>> getAllPendingReimbursements() {
        List<Reimbursement> pendingReimb = reimbService.getAllPendingReimbursement();
        // TODO: catch invalid user role error and return a 403 status code
        return ResponseEntity.ok(pendingReimb);
    }

    // get all reimbursements by user ID (user)
    @GetMapping("/users/{userId}")
    public ResponseEntity<Object> getAllReimbursementsByUserId(@PathVariable int userId) {
        try {
            List<Reimbursement> userReimbursements = reimbService.getAllReimbursementsByUserId(userId);
            return ResponseEntity.ok(userReimbursements);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // get all pending reimbursements for user ID (user)
    @GetMapping("/users/{userId}/pending")
    public ResponseEntity<Object> getAllPendingReimbursementsByUserId(@PathVariable int userId) {
        try {
            List<Reimbursement> pendingUserReimbursements = reimbService.getAllPendingReimbursementsByUserId(userId);
            return ResponseEntity.ok(pendingUserReimbursements);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // Create a new reimbursement for a user ID (user)
    @PostMapping("/users/{userId}")
    public ResponseEntity<Object> createReimbursement(@RequestBody IncomingReimbursementDTO reimbursementDTO, @PathVariable int userId) {
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
    public ResponseEntity<Object> updateReimbursementStatus(@RequestBody String status, @PathVariable int reimbId) {
        try {
            Reimbursement reimb = reimbService.updateReimbursementStatus(reimbId, status);
            return ResponseEntity.ok(reimb);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        // TODO: catch invalid user role error and return a 403 status code
    }

    /**
     * update a pending reimbursement description by reimbursement ID (user)
     * @param description the updated reimbursement description
     * @param reimbId the reimbursement ID for the reimbursement to update
     * @return ResponseEntity with the updated reimbursement or appropriate error message
     */
    @PatchMapping("/{reimbId}/description")
    public ResponseEntity<Object> updatePendingReimbursementDescription(@RequestBody String description, @PathVariable int reimbId) {
        try {
            Reimbursement reimb = reimbService.updatePendingReimbursementDescription(reimbId, description);
            return ResponseEntity.ok(reimb);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

}
