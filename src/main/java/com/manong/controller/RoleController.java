package com.manong.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manong.entity.Role;
import com.manong.service.RoleService;
import com.manong.utils.Result;
import com.manong.vo.query.RoleQueryVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Resource
    private RoleService roleService;

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
    @DeleteMapping("/delete/{ijd}")
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
}

