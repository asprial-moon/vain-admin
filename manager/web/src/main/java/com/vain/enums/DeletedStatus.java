package com.vain.enums;

public enum DeletedStatus {
    STATUS_DELETED(1, "删除"),
    STATUS_NOT_DELETED(0, "未删除");
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

    DeletedStatus(int status, String description) {
        this.status = status;
        this.description = description;
    }
}
