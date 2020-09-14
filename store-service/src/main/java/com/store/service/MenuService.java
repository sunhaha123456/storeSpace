package com.store.service;

import com.store.common.data.base.PageList;
import com.store.data.base.PageParam;
import com.store.data.entity.TbMenu;

import java.util.List;

/**
 * 功能：菜单 service
 * @author sunpeng
 * @date 2018
 */
public interface MenuService {
    /**
     * 功能：只支持二级目录深度
     * @return
     */
    List<TbMenu> getUserMenu();
    PageList<TbMenu> searchRoot(PageParam param) throws Exception;
    List<TbMenu> getChild(Long rootId);
    void save(TbMenu param);
    TbMenu getDetail(Long id);
    /**
     * 功能：获取某角色下的选中菜单
     * 备注：返回全部非冻结菜单列表，其中关联到该角色上的菜单会标记为选中
     * @param roleGroupId
     * @return
     */
    List<TbMenu> getMenuTreeByRoleId(Long roleGroupId);
    void saveRoleMenus(Long roleGroupId, List<Long> menuIds);
}