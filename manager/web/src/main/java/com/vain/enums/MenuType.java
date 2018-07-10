package com.vain.enums;

public enum MenuType {
    DIR(1, "目录"),
    MENU(2, "菜单"),
    OPERATE(3, "按钮");
    private int type;
    private String description;

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

    MenuType(int type, String description) {
        this.type = type;
        this.description = description;
    }
}
