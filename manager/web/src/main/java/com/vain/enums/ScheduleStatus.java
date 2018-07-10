package com.vain.enums;

public enum ScheduleStatus {
    RUN(1, "运行"),
    NOT_RUN(0, "未运行"),
    CONCURRENT(1, "并发"),
    NOT_CONCURRENT(0, "禁止并发");
    private int state;
    private String description;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    ScheduleStatus(int state, String description) {
        this.state = state;
        this.description = description;
    }
}
