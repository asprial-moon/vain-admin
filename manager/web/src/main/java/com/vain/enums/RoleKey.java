package com.vain.enums;

public enum RoleKey {
    SUPER_ADMIN("SUPER_ADMINISTRATOR", "操作管理员");
    private String key;
    private String description;

    RoleKey(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
