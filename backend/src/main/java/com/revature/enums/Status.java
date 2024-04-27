package com.revature.enums;

public enum Status {

    PENDING("Pending", 1),
    APPROVED("Approved", 2),
    DENIED("Denied", 3);

    private final String description;
    private final int level;

    Status(String description, int level) {
        this.description = description;
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }

    /*
    public String formatStatus(String description) {
        switch (description) {
            case "approved":
                return APPROVED.getDescription();
            case "denied":
                return DENIED.getDescription();
            case "pending":
                return PENDING.getDescription();
            default:
                // TODO: throw exception for invalid status
                return "";
        }
    }
    */

    @Override
    public String toString() {
        return "Status{" +
                "description='" + description + '\'' +
                ", level=" + level +
                '}';
    }
}
