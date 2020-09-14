package com.store.service.impl;

import com.store.common.data.base.PageList;
import com.store.common.exception.BusinessException;
import com.store.common.util.ValueHolder;
import com.store.data.base.PageParam;
import com.store.data.entity.TbMenu;
import com.store.data.entity.TbMenuRoleGroupReleation;
import com.store.data.entity.TbSysUser;
import com.store.repository.MenuRepository;
import com.store.repository.MenuRepositoryCustom;
import com.store.repository.MenuUserReleationRepository;
import com.store.repository.SysUserRepository;
import com.store.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MenuServiceImpl implements MenuService {

    @Inject
    private MenuRepositoryCustom menuRepositoryCustom;
    @Inject
    private MenuRepository menuRepository;
    @Inject
    private MenuUserReleationRepository menuUserReleationRepository;
    @Inject
    private SysUserRepository sysUserRepository;
    @Inject
    private ValueHolder valueHolder;

    @Override
    public List<TbMenu> getUserMenu() {
        TbSysUser user = sysUserRepository.findOne(valueHolder.getUserIdHolder());
        if (user != null && user.getUserState() == 0) {
            List<TbMenu> list_1 = menuRepositoryCustom.queryMenuListByRoleIdAndLevel(user.getRoleGroupId(), 1);
            if (list_1 != null && list_1.size() > 0) {
                List<TbMenu> list_2 = menuRepositoryCustom.queryMenuListByRoleIdAndLevel(user.getRoleGroupId(), 2);
                if (list_2 != null && list_2.size() > 0) {
                    for (TbMenu m1 : list_1) {
                        m1.setChildren(new ArrayList<>());
                        for (TbMenu m2 : list_2) {
                            if (m2.getParentId().equals(m1.getId())) {
                                m1.getChildren().add(m2);
                            }
                        }
                        if (m1.getChildren().size() > 0){
                            Collections.sort(m1.getChildren());
                        }
                    }
                }
            }
            return list_1;
        }
        return new ArrayList<>();
    }

    public List<TbMenu> getMenuTreeByRoleId(Long roleGroupId) {
        List<TbMenu> roleMenuList = menuRepositoryCustom.queryMenuListByRoleId(roleGroupId);
        List<Long> roleMenuIdList = roleMenuList.stream().map(TbMenu::getId).collect(Collectors.toList());
        List<TbMenu> list_1 = menuRepository.listByDirLevel(1);
        if (list_1 != null && list_1.size() > 0) {
            List<TbMenu> list_2 = menuRepository.listByDirLevel(2);
            if (list_2 != null && list_2.size() > 0) {
                for (TbMenu m1 : list_1) {
                    m1.setText(m1.getMenuName());
                    m1.setChildren(new ArrayList<>());
                    m1.setState("closed");
                    if (roleMenuIdList.contains(m1.getId())) {
                        m1.setChecked(true);
                    } else {
                        m1.setChecked(false);
                    }
                    for (TbMenu m2 : list_2) {
                        m2.setText(m2.getMenuName());
                        if (m2.getParentId().equals(m1.getId())) {
                            m1.getChildren().add(m2);
                            if (roleMenuIdList.contains(m2.getId())) {
                                m2.setChecked(true);
                            } else {
                                m1.setChecked(false);
                                m2.setChecked(false);
                            }
                        }
                    }
                    if (m1.getChildren().size() > 0){
                        Collections.sort(m1.getChildren());
                    }
                }
            }
        }
        return list_1;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveRoleMenus(Long roleGroupId, List<Long> menuIds) {
        menuUserReleationRepository.deleteByRoleGroupId(roleGroupId);
        List<TbMenuRoleGroupReleation> list = new ArrayList(menuIds.size());
        TbMenuRoleGroupReleation rele = null;
        Date now = new Date();
        for (Long menuId : menuIds) {
            rele = new TbMenuRoleGroupReleation(now, roleGroupId, menuId);
            list.add(rele);
        }
        menuUserReleationRepository.save(list);
    }

    @Override
    public PageList<TbMenu> searchRoot(PageParam param) throws Exception {
        return menuRepositoryCustom.listByDirLevel(param.getPage(), param.getRows(), 1);
    }

    @Override
    public List<TbMenu> getChild(Long rootId) {
        TbMenu menu = menuRepository.findOne(rootId);
        if (menu == null) {
            throw new BusinessException("查无此菜单！");
        }
        if (menu.getDirLevel() != 1) {
            throw new BusinessException("菜单信息异常！");
        }
        return menuRepositoryCustom.queryMenuListByParentIdAndLevel(rootId, 2);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(TbMenu param) {
        if (param.getId() == null) {
            long c = menuRepository.countByMenuName(param.getMenuName(), param.getDirLevel(), param.getParentId());
            if (0 < c) {
                throw new BusinessException("菜单名已已存在！");
            }
            param.setCreateDate(new Date());
            menuRepository.save(param);
        } else {
            TbMenu menu = menuRepository.findOne(param.getId());
            if (menu == null) {
                throw new BusinessException("菜单不存在！");
            }
            long c = menuRepository.countByMenuName(menu.getMenuName(), menu.getId(), menu.getDirLevel(), menu.getParentId());
            if (0 < c) {
                throw new BusinessException("菜单名已已存在！");
            }
            menu.setMenuName(param.getMenuName());
            menu.setUrl(param.getUrl());
            menu.setIconCls(param.getIconCls());
            menu.setMenuState(param.getMenuState());
            menu.setSort(param.getSort());
            menu.setLastModified(new Date());
            menuRepository.save(menu);
        }
    }

    @Override
    public TbMenu getDetail(Long id) {
        TbMenu menu = menuRepository.findOne(id);
        if (menu == null) {
            throw new BusinessException("菜单不存在！");
        }
        return menu;
    }
}