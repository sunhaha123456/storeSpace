package com.store.repository;

import com.store.common.data.base.PageList;
import com.store.common.repository.BaseRepository;
import com.store.data.entity.TbSysUser;

public interface SysUserRepositoryCustom extends BaseRepository {
    /**
     * 功能：用户条件分页查询
     * @param loginName
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    PageList<TbSysUser> list(String loginName, Integer pageNo, Integer pageSize) throws Exception;
}