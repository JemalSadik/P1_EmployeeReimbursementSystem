package com.revature.services;

import com.revature.daos.ReimbursementDAO;
import com.revature.daos.UserDAO;
import com.revature.enums.Status;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import com.revature.models.dtos.IncomingReimbursementDTO;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReimbursementService {

    //private static final Logger LOGGER = LoggerFactory.getLogger(ReimbursementService.class);

    private ReimbursementDAO reimbDAO;
    private UserDAO userDAO;

    @Autowired
    public ReimbursementService(ReimbursementDAO reimbDAO, UserDAO userDAO) {
        this.reimbDAO = reimbDAO;
        this.userDAO = userDAO;
    }

    /**
     * Get a list of all reimbursements. Managers can get a list of all reimbursements.
     * @return a list of all reimbursements
     */
    public List<Reimbursement> getAllReimbursements() {
        // LOGGER.info("Retrieving all reimbursements");
        return reimbDAO.findAll();
    }

    /**
     * Get a list of all reimbursements. Managers can get a list of all reimbursements.
     * @return a list of all reimbursements
     */
    public List<Reimbursement> getAllPendingReimbursement() {
        // LOGGER.info("Retrieving all pending reimbursements");
        return reimbDAO.findByStatus(Status.PENDING.getDescription());
    }

    /**
     * Get a list of all reimbursements for the user at user ID. Users can get a list of all of their reimbursements.
     * @param userId the user ID for the user getting all of their reimbursements
     * @return a list of all reimbursements for the user at user ID
     * @throws IllegalArgumentException if no user is found at user ID
     */
    public List<Reimbursement> getAllReimbursementsByUserId(int userId) throws IllegalArgumentException {
        User user = userDAO.findById(userId).orElseThrow(() -> new IllegalArgumentException("No user found for ID: " + userId));
        // LOGGER.info("Retrieving all reimbursements for user ID: {}", userId);
        return reimbDAO.findByUserUserId(userId);
    }

    /**
     * Get a list of pending reimbursements for the user at user ID. Users can get a list of all of their pending reimbursements.
     * @param userId the user ID for the user getting all of their reimbursements
     * @return a list of pending reimbursements for the user at user ID
     * @throws IllegalArgumentException if no user is found at user ID
     */
    public List<Reimbursement> getAllPendingReimbursementsByUserId(int userId) throws IllegalArgumentException {
        User user = userDAO.findById(userId).orElseThrow(() -> new IllegalArgumentException("No user found for ID: " + userId));
        // LOGGER.info("Retrieving all pending reimbursements for user ID: {}", userId);
        return reimbDAO.findByUserUserIdAndStatus(userId, Status.PENDING.getDescription());
    }

    /**
     * Create a new reimbursement. Users can create a new reimbursement
     * @param reimbursementDTO the reimbursement object to create a reimbursement
     * @return the created reimbursement
     * @throws IllegalArgumentException if no user is found at user ID
     */
    public Reimbursement createReimbursement(IncomingReimbursementDTO reimbursementDTO) throws IllegalArgumentException {

        Reimbursement reimb = new Reimbursement(reimbursementDTO.getDescription(), reimbursementDTO.getAmount(), reimbursementDTO.getStatus(), null);

        User user = userDAO.findById(reimbursementDTO.getUserId()).orElseThrow(() -> new IllegalArgumentException("No user found for ID: " + reimbursementDTO.getUserId()));
        // LOGGER.info("Creating reimbursement");
        reimb.setUser(user);
        return reimbDAO.save(reimb);
    }

    /**
     * Update pending reimbursement status. Managers can update a pending reimbursement status to "Approved" or "Denied"
     * @param reimbId the reimbursement ID for the reimbursement to update
     * @param status the updated reimbursement status
     * @return updated reimbursement or null if reimbursement status is not "Pending"
     * @throws IllegalArgumentException if no reimbursement is found ot the given reimbursement ID or if an invalid reimbursement status is given
     */
    public Reimbursement updateReimbursementStatus(int reimbId, String status) throws IllegalArgumentException {
        Reimbursement reimb = reimbDAO.findById(reimbId).orElseThrow(() -> new IllegalArgumentException("No reimbursement found for ID: " + reimbId));
        // check reimbursement status
        if (!Objects.equals(reimb.getStatus(), Status.PENDING.getDescription())) {
            throw new IllegalArgumentException("Reimbursement status must be PENDING to update a reimbursement status");
        }
        // normalize status to be lowercase and no additional whitespace before or after
        // format status to use Status enum values
        status = Status.formatStatus(status.toLowerCase().trim());
        // update reimbursement status
        reimb.setStatus(status);
        // LOGGER.info("Updating reimbursement status for reimbursement ID: {}", reimbId);
        // save updated reimbursement
        reimbDAO.save(reimb);
        // return updated reimbursement
        return reimb;
    }

    /**
     * Update pending reimbursement description. Users can update the description for a pending reimbursement
     * @param reimbId the reimbursement ID for the reimbursement to update
     * @param description the updated reimbursement description
     * @return updated reimbursement or null if reimbursement status is not "Pending"
     * @throws IllegalArgumentException if no reimbursement is found ot the given reimbursement ID
     */
    public Reimbursement updatePendingReimbursementDescription(int reimbId, String description) throws IllegalArgumentException {
        Reimbursement reimb = reimbDAO.findById(reimbId).orElseThrow(() -> new IllegalArgumentException("No reimbursement found for ID: " + reimbId));
        // Check reimbursement status is "Pending"
        if (!Objects.equals(reimb.getStatus(), Status.PENDING.getDescription())) {
            throw new IllegalArgumentException("Reimbursement status must be PENDING to update the description");
        }
        // update description
        reimb.setDescription(description);
        // LOGGER.info("Updating reimbursement description for reimbursement ID: {}", reimbId);
        // save reimbursement
        reimbDAO.save(reimb);
        // return updated reimbursement
        return reimb;
    }

}
