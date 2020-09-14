package com.store.service;

import com.store.data.entity.TbRoleGroup;

import java.util.List;

/**
 * 功能：roleGroup service
 * @author sunpeng
 * @date 2018
 */
public interface RoleGroupService {
    List<TbRoleGroup> list(Integer roleState) throws Exception;
    void add(String roleName, String roleRemark);
    void update(Long id, String roleName, String roleRemark);
    void opert(Long id, Integer state);
    TbRoleGroup getDetail(Long id);
}