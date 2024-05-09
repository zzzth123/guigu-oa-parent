package com.atguigu.auth;


import com.atguigu.auth.mapper.SysRoleMapper;
import com.atguigu.model.system.SysRole;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TestMpDemo1 {

    //注入
    @Autowired
    private SysRoleMapper mapper;

    //查询所有用户记录
    @Test
    public void getAll(){
        List<SysRole> list = mapper.selectList(null);
        System.out.println(list);
    }

    //添加用户
    @Test
    public void add(){
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("角色管理员1");
        sysRole.setRoleCode("role");
        sysRole.setDescription("角色管理员1");

        int rows = mapper.insert(sysRole);
        System.out.println(rows);
        System.out.println(sysRole.getId());
    }

    //修改操作
    @Test
    public void update(){
        //根据id进行查询
        SysRole role = mapper.selectById(10);
        // 设置要修改的值
        role.setRoleName("atguigu角色管理员");
        //调用方法实现修改
        int rows = mapper.updateById(role);
        System.out.println(rows);
    }

    //逻辑删除
    @Test
    public void delete(){
        int rows = mapper.deleteById(10);
    }

    //批量删除
    @Test
    public void testDeleteBatchIds(){
        int result = mapper.deleteBatchIds(Arrays.asList(1, 2));//逻辑删除id1和2
        System.out.println(result);
    }

    //条件查询
    @Test
    public void testQuery1(){
        //创建queryWrapper对象，调用方法封装条件
        LambdaQueryWrapper<SysRole> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleName,"总经理");

        //调用map方法实现查询操作
        List<SysRole> list = mapper.selectList(wrapper);
        System.out.println(list);
    }


}
