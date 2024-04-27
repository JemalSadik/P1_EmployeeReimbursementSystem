package com.revature.enums;

public enum Roles {

    MANAGER("Manager", 1),
    USER("User", 2);

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
}
