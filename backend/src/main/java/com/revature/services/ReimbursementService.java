package com.revature.services;

import com.revature.daos.ReimbursementDAO;
import com.revature.daos.UserDAO;
import com.revature.enums.Status;
import com.revature.exceptions.InvalidStatusException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReimbursementService {

    private ReimbursementDAO reimbDAO;
    private UserDAO userDAO;

    @Autowired
    public ReimbursementService(ReimbursementDAO reimbDAO, UserDAO userDAO) {
        this.reimbDAO = reimbDAO;
        this.userDAO = userDAO;
    }

    /**
     * Check if a user is authenticated.
     * @return true if the user is authenticated and false otherwise
     */
    private boolean isAuthenticated() {
        // get authentication token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // check if authentication token is not null, is not an instance of anonymous authentication token (sometimes used by spring security if no user is logged in) and the authentication token is authenticated
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    /**
     * Check if a user has the specific role passed
     * @param role the role to check if the user has
     * @return true if the user is authenticated and has the specified role, false otherwise
     */
    private boolean hasRole(String role) {
        // get authentication token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // check authentication token is not null and authentication token is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        // check authentication token has role specified
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }

    /**
     * Get a list of all reimbursements. Managers can get a list of all reimbursements.
     * @return a list of all reimbursements
     */
    public List<Reimbursement> getAllReimbursements() {
        // TODO: get logged in user
        /* TODO: check user role
        if user is userRole throw exception
        if user is managerRole return list of reimbursements
        */
        return reimbDAO.findAll();
    }

    /**
     * Get a list of all reimbursements. Managers can get a list of all reimbursements.
     * @return a list of all reimbursements
     */
    public List<Reimbursement> getAllPendingReimbursement() {
        // TODO: get logged in user
        /* TODO: check user role
        if user is userRole throw exception
        if user is managerRole return list of reimbursements
        */
        return reimbDAO.findByStatus(Status.PENDING.getDescription());
    }

    /**
     * Get a list of all reimbursements for the user at user ID. Users can get a list of all of their reimbursements.
     * @param userId the user ID for the user getting all of their reimbursements
     * @return a list of all reimbursements for the user at user ID
     * @throws ResourceNotFoundException if no user is found at user ID
     */
    public List<Reimbursement> getAllReimbursementsByUserId(int userId) throws ResourceNotFoundException {
        Optional<User> user = userDAO.findById(userId);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        return reimbDAO.findByUserUserId(userId);
    }

    /**
     * Get a list of pending reimbursements for the user at user ID. Users can get a list of all of their pending reimbursements.
     * @param userId the user ID for the user getting all of their reimbursements
     * @return a list of pending reimbursements for the user at user ID
     * @throws ResourceNotFoundException if no user is found at user ID
     */
    public List<Reimbursement> getAllPendingReimbursementsByUserId(int userId) throws ResourceNotFoundException {
        Optional<User> user = userDAO.findById(userId);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        return reimbDAO.findByUserUserIdAndStatus(userId, Status.PENDING.getDescription());
    }

    /**
     * Create a new reimbursement. Users can create a new reimbursement
     * @param reimbursement the reimbursement object to create a reimbursement
     * @param userId the user ID for the user creating the reimbursement
     * @return the created reimbursement
     * @throws ResourceNotFoundException if no user is found at user ID
     */
    public Reimbursement createReimbursement(Reimbursement reimbursement, int userId) throws ResourceNotFoundException {
        Optional<User> user = userDAO.findById(userId);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        reimbursement.setUser(user.get());
        return reimbDAO.save(reimbursement);
    }

    /**
     * Update pending reimbursement status. Managers can update a pending reimbursement status to "Approved" or "Denied"
     * @param reimbId the reimbursement ID for the reimbursement to update
     * @param status the updated reimbursement status
     * @return updated reimbursement or null if reimbursement status is not "Pending"
     * @throws ResourceNotFoundException if no reimbursement is found ot the given reimbursement ID
     * @throws InvalidStatusException if an invalid reimbursement status is given
     */
    public Reimbursement updateReimbursementStatus(int reimbId, String status) throws ResourceNotFoundException, InvalidStatusException {
        // TODO: get logged in user
        /* TODO: check user role
        if user is userRole throw exception
        if user is managerRole return list of reimbursements
        */
        Optional<Reimbursement> checkReimb = reimbDAO.findById(reimbId);
        // Check for reimbursement at reimbId
        if (checkReimb.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        // Get reimbursement
        Reimbursement reimb = checkReimb.get();
        // check reimbursement status
        if (!Objects.equals(reimb.getStatus(), Status.PENDING.getDescription())) {
            return null;
        }
        // normalize status to be lowercase and no additional whitespace before or after
        // format status to use Status enum values
        status = Status.formatStatus(status.toLowerCase().trim());
        // update reimbursement status
        reimb.setStatus(status);
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
     * @throws ResourceNotFoundException if no reimbursement is found ot the given reimbursement ID
     */
    public Reimbursement updatePendingReimbursementDescription(int reimbId, String description) throws ResourceNotFoundException {
        Optional<Reimbursement> checkReimb = reimbDAO.findById(reimbId);
        // Check for reimbursement at reimbId
        if (checkReimb.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        // Get reimbursement
        Reimbursement reimb = checkReimb.get();
        // Check reimbursement status is "Pending"
        if (!Objects.equals(reimb.getStatus(), Status.PENDING.getDescription())) {
            return null;
        }
        // update description
        reimb.setDescription(description);
        // save reimbursement
        reimbDAO.save(reimb);
        // return updated reimbursement
        return reimb;
    }

}
