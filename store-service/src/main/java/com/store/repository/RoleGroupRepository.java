package com.store.repository;

import com.store.data.entity.TbRoleGroup;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleGroupRepository extends CrudRepository<TbRoleGroup, Long> {

    @Query(value = "select * from tb_role_group", nativeQuery = true)
    List<TbRoleGroup> listAll();

    @Query(value = "select * from tb_role_group where role_state = :roleState", nativeQuery = true)
    List<TbRoleGroup> listByState(@Param(value = "roleState") Integer roleState);

    @Query(value = "select count(1) from tb_role_group where role_name = :roleName", nativeQuery = true)
    long countByRoleName(@Param("roleName") String roleName);

    @Query(value = "select count(1) from tb_role_group where role_name = :roleName and id != :id", nativeQuery = true)
    long countByRoleName(@Param("roleName") String roleName, @Param("id") Long id);

    @Modifying
    @Query(value = "update tb_role_group set role_name = :roleName, role_remark = :roleRemark where id = :id", nativeQuery = true)
    int updateRole(@Param("id") Long id, @Param("roleName") String roleName, @Param("roleRemark") String roleRemark);

    @Modifying
    @Query(value = "update tb_role_group set role_state = :roleState where id = :id", nativeQuery = true)
    int updateStateById(@Param(value = "id") Long id, @Param(value = "roleState") Integer roleState);
}