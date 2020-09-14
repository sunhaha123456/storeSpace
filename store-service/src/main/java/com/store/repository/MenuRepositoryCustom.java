package com.store.repository;

import com.store.common.data.base.PageList;
import com.store.common.repository.BaseRepository;
import com.store.data.entity.TbMenu;

import java.util.List;

public interface MenuRepositoryCustom extends BaseRepository {

    int queryMaxDirLevelByRoleGroupId(Long roleGroupId) throws Exception;

    /**
     * 功能：根据角色查询该角色下的指定目录深度级别的目录列表
     * @param roleId != null
     * @param level
     * @return
     */
    List<TbMenu> queryMenuListByRoleIdAndLevel(Long roleId, Integer level);

    /**
     * 功能：根据角色查询该角色下的所有目录列表
     * @param roleId != null
     * @return
     */
    List<TbMenu> queryMenuListByRoleId(Long roleId);

    PageList<TbMenu> listByDirLevel(Integer pageNo, Integer pageSize, Integer dirLevel) throws Exception;

    List<TbMenu> queryMenuListByParentIdAndLevel(Long parentId, Integer level);
}