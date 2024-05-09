package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysRoleMapper;
import com.atguigu.auth.service.SysRoleService;
import com.atguigu.auth.service.SysUserRoleService;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUserRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {


    @Autowired
    private SysUserRoleService sysUserRoleService;


    @Override
    public Map<String, Object> findRoleDataByUserId(Long userId) {

        //1 查询所有角色，返回list集合
        List<SysRole> allRolesList = baseMapper.selectList(null);

        //2 根据userid查询角色用户关系表，查询userid对应所有角色id
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,userId);
        List<SysUserRole> existUserRoleList = sysUserRoleService.list(wrapper);

        //从查询出来的用户id对应角色list集合中，获取所有角色id
        // 遍历集合得到每个对象，用get方法得到每个角色对象的id，存到list集合中
        List<Long> existRoleIdList = existUserRoleList.stream().map(c -> c.getRoleId()).collect(Collectors.toList());

        //3 根据查询所有角色id，找到对应角色信息
        //根据角色id到所有角色的list集合进行比较
        List<SysRole> assginRoleList = new ArrayList<>();
        for (SysRole sysRole : allRolesList){
            if (existRoleIdList.contains(sysRole.getId())){
                assginRoleList.add(sysRole);
            }
        }

        //4 把得到的两部分数据封装到map集合进行返回
        Map<String,Object> roleMap = new HashMap<>();
        roleMap.put("assginRoleList",assginRoleList);
        roleMap.put("allRolesList",allRolesList);
        return roleMap;

    }


    //为用户分配角色
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        //把用户之前分配角色数据删除，在用户角色关系表里面，根据userid删除
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,assginRoleVo.getUserId());
        sysUserRoleService.remove(wrapper);

        //重新进行分配
        List<Long> roleIdList = assginRoleVo.getRoleIdList();
        for (Long roleId:roleIdList){
            if (StringUtils.isEmpty(roleId)){
                continue;
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleService.save(sysUserRole);
        }

    }
}
