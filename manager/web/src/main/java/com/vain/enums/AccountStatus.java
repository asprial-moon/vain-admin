package com.vain.enums;

public enum AccountStatus {
    STATUS_LOCK(1, "锁定"),
    STATUS_UNLOCK(0, "未锁定");
    private int status;
    private String description;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    AccountStatus(int status, String description) {
        this.status = status;
        this.description = description;
    }
}
