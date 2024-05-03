package com.revature.enums;

public enum Roles {

    MANAGER("Manager", 1),
    EMPLOYEE("Employee", 2);

    private final String description;
    private final int level;

    Roles(String description, int level) {
        this.description = description;
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "Roles{" +
                "description='" + description + '\'' +
                ", level=" + level +
                '}';
    }

    /**
     *
     * @param role the Role to format
     * @return the role enum description of the passed role
     * @throws IllegalArgumentException if the role is not a valid role
     */
    public static String formatRole(String role) throws IllegalArgumentException {
        return switch (role) {
            case "manager" -> MANAGER.getDescription();
            case "employee" -> EMPLOYEE.getDescription();
            default -> throw new IllegalArgumentException("invalid role");
        };
    }

}
