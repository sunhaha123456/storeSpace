package com.store.repository.impl;

import com.store.common.data.base.PageList;
import com.store.common.data.base.PageUtil;
import com.store.common.repository.impl.BaseRepositoryImpl;
import com.store.data.entity.TbMenu;
import com.store.repository.MenuRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Repository
public class MenuRepositoryCustomImpl extends BaseRepositoryImpl implements MenuRepositoryCustom {

    @Override
    public int queryMaxDirLevelByRoleGroupId(Long roleGroupId) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT MAX(m.dir_level) maxDirLevel ");
        sql.append(" FROM tb_role_group rg JOIN tb_menu_role_group_releation rgr ON rg.id = rgr.role_group_id ");
        sql.append(" AND rg.id = ? AND rg.role_state = 0 ");
        sql.append(" JOIN tb_menu m ON rgr.menu_id = m.id AND m.menu_state = 0 ");
        String res = queryOneObjAttr(sql.toString(), roleGroupId);
        if (res == null) {
            return 0;
        } else {
            return Integer.valueOf(res);
        }
    }

    @Override
    public List<TbMenu> queryMenuListByRoleIdAndLevel(Long roleId, Integer level) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT m.id, m.menu_name menuName, m.icon_cls iconCls, m.url, m.parent_id parentId, m.dir_level dirLevel ");
        sql.append(" FROM tb_role_group rg JOIN tb_menu_role_group_releation rgr ON rg.id = rgr.role_group_id ");
        sql.append(" AND rg.id = ? AND rg.role_state = 0 ");
        sql.append(" JOIN tb_menu m ON rgr.menu_id = m.id AND m.dir_level = ? AND m.menu_state = 0 order by m.sort desc ");
        return queryList(sql.toString(), TbMenu.class, new Object[]{roleId, level});
    }

    @Override
    public List<TbMenu> queryMenuListByRoleId(Long roleId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT m.id, m.menu_name menuName, m.icon_cls iconCls, m.url, m.parent_id parentId, m.dir_level dirLevel ");
        sql.append(" FROM tb_role_group rg JOIN tb_menu_role_group_releation rgr ON rg.id = rgr.role_group_id ");
        sql.append(" AND rg.id = ? AND rg.role_state = 0 ");
        sql.append(" JOIN tb_menu m ON rgr.menu_id = m.id AND m.menu_state = 0 order by m.sort desc ");
        return queryList(sql.toString(), TbMenu.class, new Object[]{roleId});
    }

    @Override
    public PageList<TbMenu> listByDirLevel(Integer pageNo, Integer pageSize, Integer dirLevel) throws Exception {
        StringBuilder sql = new StringBuilder();
        List<Object> paramList = new ArrayList();
        sql.append(" SELECT id, menu_name menuName, icon_cls iconCls, url, menu_state menuState, sort, create_date createDate ");
        sql.append(" FROM tb_menu WHERE dir_level = ? ");
        paramList.add(dirLevel);
        LinkedHashMap<String, String> sortMap = new LinkedHashMap();
        sortMap.put("sort", "desc");
        return queryPage(sql.toString(), TbMenu.class, new PageUtil(pageNo, pageSize), sortMap, paramList.toArray());
    }

    @Override
    public List<TbMenu> queryMenuListByParentIdAndLevel(Long parentId, Integer level) {
        StringBuilder sql = new StringBuilder();
        List<Object> param = new ArrayList();
        sql.append(" SELECT m.id, m.menu_name menuName, m.icon_cls iconCls, m.url, m.parent_id parentId, m.dir_level dirLevel, m.menu_state menuState, m.sort, m.create_date createDate ");
        sql.append(" FROM tb_menu m ");
        sql.append(" WHERE m.dir_level = ? AND m.parent_id = ? order by m.sort desc ");
        param.add(level);
        param.add(parentId);
        return queryList(sql.toString(), TbMenu.class, param.toArray());
    }
}