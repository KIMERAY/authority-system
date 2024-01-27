package com.manong.dao;

import com.manong.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 查询角色数量
     *
     * @param id
     * @return
     */
    @Select("select count(1) from sys_user_role where role_Id = #{roleId}")
    int getRoleCountByRoleId(Long id);

    /**
     * 删除角色权限关系
     *
     * @param id
     */
    @Delete("delete from sys_role_permission where role_id = #{id}")
    void deleteRolePermission(Long id);

    /**
     * 保存角色权限关系
     *
     * @param roleId
     * @param permissionIds
     * @return
     */
    int saveRolePermission(Long roleId, List<Long> permissionIds);

    /**
     * 根据登录用户ID查询该用户拥有的角色列表
     *
     * @param userId
     * @return
     */
    @Select("select role_id from `sys_user_role` where user_id = #{userId}")
    List<Long> findRoleIdByUserId(Long userId);
}
