package com.vain.enums;

/**
 * @author vain
 * @Description
 * @date 2018/6/20 21:19
 */
public enum UploadFileType {
    PHOTO(1, "照片"),
    AUDIO(2, "音频"),
    VIDEO(3, "视频"),
    OTHER(4, "其他");
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

    UploadFileType(int type, String description) {
        this.type = type;
        this.description = description;
    }
}
