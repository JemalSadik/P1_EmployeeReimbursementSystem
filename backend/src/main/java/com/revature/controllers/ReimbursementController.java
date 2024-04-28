package com.revature.controllers;

import com.revature.exceptions.InvalidStatusException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.Reimbursement;
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
    public ResponseEntity<List<Reimbursement>> getAllPendingReimbursements() {
        List<Reimbursement> pendingReimb = reimbService.getAllPendingReimbursement();
        // TODO: catch invalid user role error and return a 403 status code
        return ResponseEntity.ok(pendingReimb);
    }

    // get all reimbursements by user ID (user)
    @GetMapping("/all/{userId}")
    public ResponseEntity<Object> getAllReimbursementsByUserId(@PathVariable int userId) {
        try {
            List<Reimbursement> userReimbursements = reimbService.getAllReimbursementsByUserId(userId);
            return ResponseEntity.ok(userReimbursements);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body("No user found for ID: " + userId);
        }
    }

    // get all pending reimbursements for user ID (user)
    @GetMapping("/pending/{userId}")
    public ResponseEntity<Object> getAllPendingReimbursementsByUserId(@PathVariable int userId) {
        try {
            List<Reimbursement> pendingUserReimbursements = reimbService.getAllPendingReimbursementsByUserId(userId);
            return ResponseEntity.ok(pendingUserReimbursements);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body("No user found for ID: " + userId);
        }
    }

    // Create a new reimbursement for a user ID (user)
    @PostMapping("/{userId}")
    public ResponseEntity<Object> createReimbursement(@RequestBody Reimbursement reimbursement, @PathVariable int userId) {
        try {
            Reimbursement reimb = reimbService.createReimbursement(reimbursement, userId);
            return ResponseEntity.ok(reimb);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body("No user found for ID: " + userId);
        }
    }

    // update a reimbursement status by reimbursement ID (manager)
    @PatchMapping("/status/{reimbId}")
    public ResponseEntity<Object> updateReimbursementStatus(@RequestBody String status, @PathVariable int reimbId) {
        try {
            Reimbursement reimb = reimbService.updateReimbursementStatus(reimbId, status);
            // null means status of reimbursement was not "Pending"
            if (reimb == null) {
                return ResponseEntity.status(409).body("Status of a reimbursement must be PENDING in order to update a reimbursement status");
            }
            return ResponseEntity.ok(reimb);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body("No reimbursement found for ID: " + reimbId);
        } catch (InvalidStatusException e) {
            return ResponseEntity.status(409).body(status.toUpperCase() + " is not a valid status for a reimbursement");
        }
        // TODO: catch invalid user role error and return a 403 status code
    }

    /**
     * update a pending reimbursement description by reimbursement ID (user)
     * @param description the updated reimbursement description
     * @param reimbId the reimbursement ID for the reimbursement to update
     * @return ResponseEntity with the updated reimbursement or appropriate error message
     */
    @PatchMapping("/description/{reimbId}")
    public ResponseEntity<Object> updatePendingReimbursementDescription(@RequestBody String description, @PathVariable int reimbId) {
        try {
            Reimbursement reimb = reimbService.updatePendingReimbursementDescription(reimbId, description);
            // null means reimbursement status was not pending
            if (reimb == null) {
                return ResponseEntity.status(409).body("Reimbursement status must be PENDING to update the description");
            }
            return ResponseEntity.ok(reimb);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body("No reimbursement found for ID: " + reimbId);
        }
    }

}
