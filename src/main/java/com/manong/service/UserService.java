package com.manong.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.manong.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manong.vo.query.UserQueryVo;

import java.util.List;

public interface UserService extends IService<User> {


    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 分页查询用户列表
     * @param page
     * @param userQueryVo
     * @return
     */
    IPage<User> findUserListByPage(IPage<User> page, UserQueryVo userQueryVo);

    /**
     * 根据Id删除用户
     * @param userId
     * @return
     */
    boolean deleteById(Long userId);

    /**
     * 分配角色
     * @param userId
     * @param roleIds
     * @return
     */
    boolean saveUserRole(Long userId, List<Long> roleIds);
}
