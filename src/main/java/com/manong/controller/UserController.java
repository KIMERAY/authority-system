package com.manong.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manong.entity.User;
import com.manong.service.UserService;
import com.manong.utils.Result;
import com.manong.vo.query.UserQueryVo;
import org.apache.ibatis.annotations.Delete;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 查询所有用户信息（测试使用）
     *
     * @return
     */
    @GetMapping("/listAll")
    public Result listAll() {
        return Result.ok(userService.list());
    }


    /**
     * 查询用户列表
     *
     * @param userQueryVo
     * @return
     */
    @GetMapping("/list")
    public Result list(UserQueryVo userQueryVo) {
//        创建分页对象
        IPage<User> page = new Page<User>(userQueryVo.getPageNo(), userQueryVo.getPageSize());
//        调用分页查询方法
        userService.findUserListByPage(page, userQueryVo);
//        返回数据
        return Result.ok(page);
    }


    /**
     * 添加用户
     */
    @PostMapping("/add")

    public Result add(@RequestBody User user) {
//        调用根据用户名查询用户信息的方法
        User item = userService.findUserByUsername((user.getUsername()));
//        判断对象是否为空，不为空则表示用户存在
        if (item != null) {
            return Result.error().message("该用户名已被使用，请重新输入！");

        }
//        密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        调用保存用户信息的方法
        if (userService.save(user)) {
            return Result.ok().message("用户添加成功");
        }
        return Result.error().message("用户添加失败");
    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody User user) {
//查询用户
        User item = userService.findUserByUsername(user.getUsername());
//判断对象是否为空,且查询到的用户ID不等于当前编辑的用户ID，表示该名称被占用
        if (item != null && item.getId() != user.getId()) {
            return Result.error().message("该用户名已被占用，请重新输入！");
        }
//调用修改用户信息的方法
        if (userService.updateById(user)) {
            return Result.ok().message("用户修改成功");
        }
        return Result.error().message("用户修改失败");
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
//调用删除用户信息的方法
        if (userService.deleteById(id)) {
            return Result.ok().message("用户删除成功");
        }
        return Result.error().message("用户删除失败");
    }
}

