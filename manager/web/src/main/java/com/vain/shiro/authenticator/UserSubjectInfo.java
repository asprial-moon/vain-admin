package com.vain.shiro.authenticator;


import com.vain.constant.SystemConfigKeys;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录用户的信息
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSubjectInfo implements SubjectInfo, Serializable {

    /**
     * 登录名
     */
    private String userName;

    /**
     * 名字
     */
    private String nickName;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * @see com.vain.constant.SystemConfigKeys 管理员、普通用户
     */
    private int userType = SystemConfigKeys.USER;


    @Override
    public String getIdentification() {
        return "USER:" + this.userId;
    }

}
