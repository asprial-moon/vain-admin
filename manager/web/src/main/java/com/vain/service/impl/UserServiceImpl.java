package com.vain.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vain.base.service.AbstractBaseService;
import com.vain.common.ErrorCodeException;
import com.vain.entity.Menu;
import com.vain.entity.User;
import com.vain.entity.UserMenu;
import com.vain.entity.UserRole;
import com.vain.enums.AccountStatus;
import com.vain.enums.DeletedStatus;
import com.vain.enums.StatusCode;
import com.vain.log.OperationLog;
import com.vain.log.constant.LogConstants;
import com.vain.mapper.UserMapper;
import com.vain.mapper.UserMenuMapper;
import com.vain.mapper.UserRoleMapper;
import com.vain.service.IUserService;
import com.vain.util.MD5Utils;
import com.vain.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author vain
 * @description: 用户信息service
 * @date 2017/8/31 12:01
 */
@Service
@Slf4j
public class UserServiceImpl extends AbstractBaseService implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserMenuMapper userMenuMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public User login(User entity) throws ErrorCodeException {
        //通过邮箱登录
        if (StringUtils.isEmail(entity.getUserName())) {
            entity.setEmail(entity.getUserName());
            entity.setUserName(null);
        } else if (StringUtils.isNumeric(entity.getUserName())) {
            //通过手机号码登录
            entity.setPhone(entity.getUserName());
            entity.setUserName(null);
        }
        User dbUser = userMapper.get(entity);
        if (null == dbUser) {
            // 登录失败，账号不存在
            log.error("login failure，this account does not exist");
            throwErrorCodeException(StatusCode.ACCOUNT_NOT_EXIST);
        } else {
            if (AccountStatus.STATUS_LOCK.getStatus() == dbUser.getState()) {
                // 登录失败，账户被禁用 后期可根据state来扩展
                log.error("login failure，this account is locked");
                throwErrorCodeException(StatusCode.ACCOUNT_LOCK);
            }
            if (dbUser.getPassword().equals(MD5Utils.getMD5Str(entity.getPassword() + dbUser.getSalt()))) {
                //清除敏感字段
                dbUser.clearSecretField();
                return dbUser;
            }
            // 登录失败，账号或密码不正确
            log.error("login failure，this account or password is error");
            throwErrorCodeException(StatusCode.ACCOUNT_PASSWORD_ERROR);
        }
        return null;
    }

    /**
     * 重置密码
     *
     * @param entity
     * @return
     */
    @Override
    public int resetPwd(User entity) {
        entity.setSalt(UUID.randomUUID().toString());
        entity.setPassword(MD5Utils.getMD5Str(entity.getNewpasswd() + entity.getSalt()));
        return userMapper.resetPwd(entity);
    }

    /**
     * 锁定 / 解锁 账号
     *
     * @param entity
     * @return
     */
    @Override
    @OperationLog(operationType = LogConstants.OperationLogType.OPERATION_DISABLE)
    public int lock(User entity) {
        return userMapper.lock(entity);
    }

    /**
     * 分配用户权限
     * 先删除原来的权限列表 在添加新的权限列表
     *
     * @param entity
     */
    @Override
    public int assignUserMenu(User entity) {
        UserMenu userMenu = new UserMenu();
        List<UserMenu> userMenus = new ArrayList<>();
        userMenu.setUserId(entity.getId());
        userMenuMapper.delete(userMenu);
        List<Menu> menus = entity.getMenus();
        for (Menu menu : menus) {
            assignPermission(menu, entity, userMenus);
        }
        if (userMenus.size() > 0) {
            return userMenuMapper.assignUserMenu(userMenus);
        }
        return 0;
    }

    /**
     * 分配用户菜单权限递归的方法
     *
     * @param menu，传入遍历的单个菜单数据
     * @param user，用户id
     */
    private void assignPermission(Menu menu, User user, List<UserMenu> userMenus) {
        if (menu.getHasPermission() != null && menu.getHasPermission()) {
            /*
             * 添加，修改为同一方法，修改进来，默认选中 根据menuId和roleId重新添加
             */
            UserMenu userMenu = new UserMenu();
            userMenu.setMenuId(menu.getId());
            userMenu.setUserId(user.getId());
            userMenus.add(userMenu);
            /*
             * 再次进行递归 第3级判断
             */
            if (menu.getChildren() != null && menu.getChildren().size() > 0) {
                List<Menu> menuChilds = menu.getChildren();
                for (Menu menuChild : menuChilds) {
                    assignPermission(menuChild, user, userMenus);
                }
            }
        }
    }

    @Override
    public PageInfo<User> getPagedList(User entity) throws ErrorCodeException {
        entity.initPageParam();
        PageHelper.startPage(entity.getCurrentPage(), entity.getPageSize());
        List<User> list = userMapper.getList(entity);
        return new PageInfo<>(list);
    }

    @Override
    public List<User> getList(User entity) throws ErrorCodeException {
        return null;
    }

    @Override
    public User get(User entity) throws ErrorCodeException {
        return userMapper.get(entity);
    }

    /**
     * 添加账号
     *
     * @param entity 参数实体
     * @throws ErrorCodeException
     */
    @Override
    public int add(User entity) throws ErrorCodeException {
        entity.setSalt(UUID.randomUUID().toString());
        entity.setPassword(MD5Utils.getMD5Str(entity.getPassword() + entity.getSalt()));
        entity.setState(AccountStatus.STATUS_UNLOCK.getStatus());
        entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        entity.setDeleted(DeletedStatus.STATUS_NOT_DELETED.getStatus());
        return userMapper.insert(entity);
    }

    /**
     * 修改账号信息
     *
     * @param entity 参数实体
     * @return
     * @throws ErrorCodeException
     */
    @Override
    public int modify(User entity) throws ErrorCodeException {
        //修改用户角色
        if (!CollectionUtils.isEmpty(entity.getRoles())) {
            List<UserRole> userRoles = new ArrayList<>();
            entity.getRoles().forEach(role -> userRoles.add(new UserRole(entity.getId(), role.getId())));
            userRoleMapper.deleteUserAllRole(entity.getId());
            userRoleMapper.insertUserRole(userRoles);
        }
        return userMapper.update(entity);
    }

    /**
     * 删除账号
     *
     * @param entity 参数实体
     * @throws ErrorCodeException
     */
    @Override
    public int delete(User entity) throws ErrorCodeException {
        entity.setDeleted(DeletedStatus.STATUS_DELETED.getStatus());
        return userMapper.delete(entity);
    }

}