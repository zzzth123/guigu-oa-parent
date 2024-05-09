package com.atguigu.auth.service;


import com.atguigu.model.system.SysMenu;
import com.atguigu.vo.system.AssginMenuVo;
import com.atguigu.vo.system.RouterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2024-04-14
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findNodes();

    //删除菜单
    void removeMenuById(Long id);

    List<SysMenu> finMenuByRoleId(Long roleId);

    //为角色分配菜单
    void doAssign(AssginMenuVo assginMenuVo);

    //4 根据用户id获取用户可以操作的菜单
    List<RouterVo> findUserMenuListByUserId(Long userId);
    //5 根据用户id动态的查询用户可以操作的按钮
    List<String> findUserPermsByUserId(Long userId);
}
