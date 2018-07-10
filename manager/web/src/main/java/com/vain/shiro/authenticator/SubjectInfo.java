package com.vain.shiro.authenticator;

/**
 * Created by vain on 2017/9/19.
 * 用户的基本信息
 */
public interface SubjectInfo {
    /**
     * 获取身份证明
     *
     * @return
     */
    Object getIdentification();

    int getUserType();

    void setUserType(int userType);

    String getUserName();

    void setUserName(String username);

    Integer getUserId();

    void setUserId(Integer userId);

    String getNickName();

    void setNickName(String nickname);

}
