package com.vain.enums;

public enum AccountType {
    SUPER_ADMIN(1, "超级管理员"),
    ADMIN_GROUP(2, "管理组");
    private int type;
    private String description;

    AccountType(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
