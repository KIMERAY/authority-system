package com.manong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.manong.entity.Permission;
import com.manong.dao.PermissionMapper;
import com.manong.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manong.utils.MenuTree;
import com.manong.vo.query.PermissionQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {


    /**
     * 根据用户ID查询权限列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<Permission> findPermissionListByUserId(Long userId) {
        return baseMapper.findPermissionListByUserId(userId);
    }

    /**
     * 查询菜单列表
     *
     * @param permissionQueryVo
     * @return
     */
    @Override
    public List<Permission> findPermissionList(PermissionQueryVo permissionQueryVo) {
//        创建条件构造器对象
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<Permission>();
//        设置排序自动
        queryWrapper.orderByAsc("order_num");
//        调用查询菜单列表的方法
        List<Permission> permissionList = baseMapper.selectList(queryWrapper);
//        生成菜单树
        List<Permission> menuTree = MenuTree.makeMenuTree(permissionList, 0L);

        return menuTree;
    }

    /**
     * 查询上级菜单列表
     *
     * @return
     */
    @Override
    public List<Permission> findParentPermissionList() {
//        创建条件构造器对象
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<Permission>();
//        只查询目录(0)和菜单(1)的数据
        queryWrapper.in("type", Arrays.asList(0, 1));
//        设置排序自动
        queryWrapper.orderByAsc("order_num");
//        调用查询菜单列表的方法
        List<Permission> permissionList = baseMapper.selectList(queryWrapper);
//        构造顶级菜单对象
        Permission permission = new Permission();
        permission.setId(0L);
        permission.setParentId(-1L);
        permission.setLabel("顶级菜单");
        permissionList.add(permission);
//        生成菜单数据
        List<Permission> menuTree = MenuTree.makeMenuTree(permissionList, -1L);
        return menuTree;
    }

    /**
     * 检查菜单是否有子菜单
     *
     * @param id
     * @return
     */
    @Override
    public boolean hasChildrenOfPermission(Long id) {
//        创建条件构造器对象
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<Permission>();
//        查询父级ID
        queryWrapper.eq("parent_id",id);
//        判断数量是否大于0，如果大于0则表示存在
        if (baseMapper.selectCount(queryWrapper)>0){
            return true;
        }
        return false;
    }
}
