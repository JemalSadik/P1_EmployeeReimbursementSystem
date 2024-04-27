package com.revature.controllers;

import com.revature.models.Reimbursement;
import com.revature.services.ReimbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

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
    public ResponseEntity<List<Reimbursement>> getAllReimbursements() {
        List<Reimbursement> reimbursements = reimbService.getAllReimbursements();

        // TODO: check if list is empty and return appropriate message

        return ResponseEntity.ok(reimbursements);
    }

    // get all pending reimbursements (for manager)
    public ResponseEntity<List<Reimbursement>> getAllPendingReimbursements() {
        List<Reimbursement> pendingReimb = reimbService.getAllPendingReimbursement();

        // TODO: check if list is empty and return appropriate message

        return ResponseEntity.ok(pendingReimb);
    }

    // get all reimbursements by user ID (user)
    @GetMapping("/all/{userId}")
    public ResponseEntity<List<Reimbursement>> getAllReimbursementsByUserId(@PathVariable int userId) {
        List<Reimbursement> userReimbursements = reimbService.getAllReimbursementsByUserId(userId);

        // TODO: check if list is empty or an exception is thrown

        return ResponseEntity.ok(userReimbursements);
    }

    // get all pending reimbursements for user ID (user)
    @GetMapping("/pending/{userId}")
    public ResponseEntity<List<Reimbursement>> getAllPendingReimbursementsByUserId(@PathVariable int userId) {
        List<Reimbursement> pendingUserReimbursements = reimbService.getAllPendingReimbursementsByUserId(userId);

        // TODO: check if list is empty or an exception is thrown

        return ResponseEntity.ok(pendingUserReimbursements);
    }

    // Create a new reimbursement for a user ID (user)
    @PostMapping("/{userId}")
    public ResponseEntity<Reimbursement> createReimbursement(@RequestBody Reimbursement reimbursement, @PathVariable int userId) {
        Reimbursement reimb = reimbService.createReimbursement(reimbursement, userId);

        // TODO: check if exception is thrown

        return ResponseEntity.ok(reimb);
    }

    // update a reimbursement status by reimbursement ID (manager)
    @PatchMapping("/status/{reimbId}")
    public ResponseEntity<Reimbursement> updateReimbursementStatus(@RequestBody String status, @PathVariable int reimbId) {
        status = status.toLowerCase().trim();
        Reimbursement reimb = reimbService.updateReimbursementStatus(reimbId, status);

        // TODO: Check if exception is thrown

        return ResponseEntity.ok(reimb);
    }

    // update a reimbursement description by reimbursement ID (user)
    @PatchMapping("/description/{reimbId}")
    public ResponseEntity<Reimbursement> updateReimbursementDescription(@RequestBody String description, @PathVariable int reimbId) {
        Reimbursement reimb = reimbService.updateReimbursementDescription(reimbId, description);

        // TODO: Check if exception is thrown

        return ResponseEntity.ok(reimb);
    }

}
