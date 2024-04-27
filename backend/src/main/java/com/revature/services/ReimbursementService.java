package com.revature.services;

import com.revature.daos.ReimbursementDAO;
import com.revature.daos.UserDAO;
import com.revature.enums.Status;
import com.revature.models.Reimbursement;
import com.revature.models.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Reimbursement> getAllReimbursements() {
        // TODO: get logged in user
        /*
        if user is userRole throw exception
        if user is managerRole return list of reimbursements
        */
        return reimbDAO.findAll();
    }

    public List<Reimbursement> getAllPendingReimbursement() {
        return reimbDAO.findByStatus(Status.PENDING.getDescription());
    }

    public List<Reimbursement> getAllReimbursementsByUserId(int userId) {
        Optional<User> user = userDAO.findById(userId);
        if (user.isPresent()) {
            return reimbDAO.findByUserUserId(userId);
        }
        // TODO: throw an exception here
        return Collections.emptyList();
    }

    public List<Reimbursement> getAllPendingReimbursementsByUserId(int userId) {
        Optional<User> user = userDAO.findById(userId);
        if (user.isPresent()) {
            return reimbDAO.findByUserUserIdAndStatus(userId, Status.PENDING.getDescription());
        }
        // TODO: throw an exception here
        return Collections.emptyList();
    }

    public Reimbursement createReimbursement(Reimbursement reimbursement, int userId) {
        Optional<User> user = userDAO.findById(userId);
        if (user.isPresent()) {
            reimbursement.setUser(user.get());
            return reimbDAO.save(reimbursement);
        }
        // TODO: Throw an exception
        return new Reimbursement();
    }

    public Reimbursement updateReimbursementStatus(int reimbId, String status) {
        Optional<Reimbursement> checkReimb = reimbDAO.findById(reimbId);
        if (checkReimb.isPresent()) {
            Reimbursement reimb = checkReimb.get();
            if (Objects.equals(reimb.getStatus(), "Pending")) {

                // TODO: Abstract the switch/case away
                switch (status) {
                    case "approved":
                        reimb.setStatus(Status.APPROVED.getDescription());
                        break;
                    case "denied":
                        reimb.setStatus(Status.DENIED.getDescription());
                        break;
                    case "pending":
                        break;
                    default:
                        // TODO: throw exception for invalid status
                }

                reimbDAO.save(reimb);
                return reimb;
            }
        }
        // TODO: throw exception for reimbursement not found
        return new Reimbursement();
    }

    public Reimbursement updateReimbursementDescription(int reimbId, String description) {
        Optional<Reimbursement> checkReimb = reimbDAO.findById(reimbId);
        if (checkReimb.isPresent()) {
            Reimbursement reimb = checkReimb.get();
            reimb.setDescription(description);
            reimbDAO.save(reimb);
            return reimb;
        }
        // TODO: throw an exception for reimbursement not found
        return new Reimbursement();
    }

}
