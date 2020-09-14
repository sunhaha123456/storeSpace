package com.store.repository;

import com.store.data.entity.TbMenuRoleGroupReleation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MenuUserReleationRepository extends CrudRepository<TbMenuRoleGroupReleation, Long> {

    //@Query(value = "select * from tb_sys_user where uname = :uname and upwd = :upwd and user_state = 0", nativeQuery = true)
    //TbSysUser findByLoginNameAndUpwd(@Param(value = "uname") String uname, @Param(value = "upwd") String upwd);

    @Modifying
    @Query(value = "delete from tb_menu_role_group_releation where role_group_id = :roleGroupId", nativeQuery = true)
    int deleteByRoleGroupId(@Param(value = "roleGroupId") Long roleGroupId);
}