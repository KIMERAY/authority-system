package com.manong.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manong.dto.RolePermissionDTO;
import com.manong.entity.Role;
import com.manong.service.PermissionService;
import com.manong.service.RoleService;
import com.manong.service.UserService;
import com.manong.utils.Result;
import com.manong.vo.RolePermissionVo;
import com.manong.vo.query.RoleQueryVo;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;

    @GetMapping("/list")
    public Result list(RoleQueryVo roleQueryVo) {
//    创建分页对象
        IPage<Role> page = new Page<Role>
                (roleQueryVo.getPageNo(), roleQueryVo.getPageSize());
//    调用查询方法
        roleService.findRoleListByUserId(page, roleQueryVo);
//    返回数据
        return Result.ok(page);
    }

    /**
     * 添加角色
     *
     * @param role
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Role role) {

//        调用添加的方法
        if (roleService.save(role)) {
            return Result.ok().message("角色添加成功");
        }
        return Result.error().message("角色添加失败");
    }

    /**
     * 修改角色
     *
     * @param role
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody Role role) {

//        调用添加的方法
        if (roleService.updateById(role)) {
            return Result.ok().message("角色修改成功");
        }
        return Result.error().message("角色修改失败");
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {

//        调用删除的方法
        if (roleService.deleteRoleById(id)) {
            return Result.ok().message("角色删除成功");
        }
        return Result.error().message("角色删除失败");
    }

    /**
     * 检查用户角色是否被使用
     *
     * @param id
     * @return
     */
    @GetMapping("/check/{id}")
    public Result check(@PathVariable Long id) {
//        调用检查用户角色是否被使用的方法
        if (roleService.hasRoleCount(id)) {
            return Result.exist().message("该角色已分配给其他用户使用，无法删除");
        }
        return Result.ok();
    }

    /**
     * 分配权限-查询权限树数据
     *
     * @param userId
     * @param RoleId
     * @return
     */
    @GetMapping("/getAssignPermissionTree")
    public Result getAssignPermissionTree(Long userId, Long RoleId) {
//        调用查询权限树数据的方法
        RolePermissionVo permissionTree = permissionService.findPermissTree(userId, RoleId);
//        返回数据
        return Result.ok(permissionTree);
    }

    /**
     * 分配权限-保存角色权限数据
     *
     * @param rolePermissionDTO
     * @return
     */
    @PostMapping("/saveRoleAssign")
    public Result saveRoleAssign(@RequestBody RolePermissionDTO rolePermissionDTO) {
//        调用保存角色权限关系的方法

        if (roleService.saveRolePermission(rolePermissionDTO.getRoleId(), rolePermissionDTO.getList())) {
            return Result.ok().message("权限分配成功");
        }
        return Result.error().message("权限分配失败");
    }

    /**
     * 获取分配角色列表
     *
     * @param roleQueryVo
     * @return
     */
    @GetMapping("/getRoleListForAssign")
    public Result getRoleListForAssign(RoleQueryVo roleQueryVo) {
//        创建分页对象
        IPage<Role> page = new Page<Role>(roleQueryVo.getPageNo(), roleQueryVo.getPageSize());
//        调用查询角色列表的方法
        roleService.findRoleListByUserId(page, roleQueryVo);
//        返回数据
        return Result.ok(page);
    }

    /**
     * 根据用户ID查询该用户拥有的角色ID
     * @param userId
     * @return
     */
    @GetMapping("/getRoleByUserId/{userId}")
    public Result getRoleByUserId(@PathVariable Long userId){
//        调用根据用户ID查询该用户拥有的角色ID的方法
        List<Long> roleIds = roleService.findRoleIdByUserId(userId);
//        返回数据
        return Result.ok();
    }
}

