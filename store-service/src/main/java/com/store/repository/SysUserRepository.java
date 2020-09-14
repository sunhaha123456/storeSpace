package com.store.repository;

import com.store.data.entity.TbSysUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SysUserRepository extends CrudRepository<TbSysUser, Long> {

    @Query(value = "select * from tb_sys_user where login_name = :loginName and upwd = :upwd", nativeQuery = true)
    TbSysUser findByLoginNameAndUpwd(@Param(value = "loginName") String loginName, @Param(value = "upwd") String upwd);

    @Query(value = "select count(1) from tb_sys_user where login_name = :loginName", nativeQuery = true)
    long countByLoginName(@Param(value = "loginName") String loginName);

    @Modifying
    @Query(value = "update tb_sys_user set user_state = :userState where id = :id", nativeQuery = true)
    int updateStateById(@Param(value = "id") Long id, @Param(value = "userState") Integer userState);

    @Modifying
    @Query(value = "update tb_sys_user set role_group_id = :roleId, user_name = :userName where id = :id", nativeQuery = true)
    int updateRoleAndUserName(@Param(value = "id") Long id, @Param(value = "roleId") Long roleId, @Param(value = "userName") String userName);

    @Modifying
    @Query(value = "update tb_sys_user set upwd = :userNewPassword where id = :id", nativeQuery = true)
    int updatePasswod(@Param(value = "id") Long id, @Param(value = "userNewPassword") String userNewPassword);

    @Modifying
    @Query(value = "update tb_sys_user set upwd = :userNewPassword where id = :id and upwd = :userOldPassword", nativeQuery = true)
    int updatePasswodByOldPassword(@Param(value = "id") Long id, @Param(value = "userOldPassword") String userOldPassword, @Param(value = "userNewPassword") String userNewPassword);

    @Query(value = "select * from tb_sys_user where role_group_id = :roleGroupId", nativeQuery = true)
    List<TbSysUser> findByRoleGroupId(@Param(value = "roleGroupId") Long roleGroupId);

    @Query(value = "select * from tb_sys_user where login_name = :loginName and user_state != 2", nativeQuery = true)
    TbSysUser findByLoginName(@Param(value = "loginName") String loginName);
}