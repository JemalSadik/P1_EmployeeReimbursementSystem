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

    /**
     *
     * @param status the status to format
     * @return the status enum description of the passed status
     * @throws IllegalArgumentException if the status is not a valid status
     */
    public static String formatStatus(String status) throws IllegalArgumentException {
        return switch (status) {
            case "approved" -> APPROVED.getDescription();
            case "denied" -> DENIED.getDescription();
            case "pending" -> PENDING.getDescription();
            default -> throw new IllegalArgumentException();
        };
    }

    @Override
    public String toString() {
        return "Status{" +
                "description='" + description + '\'' +
                ", level=" + level +
                '}';
    }
}
