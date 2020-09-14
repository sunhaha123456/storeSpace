package com.store.repository.impl;

import com.store.common.data.base.PageList;
import com.store.common.data.base.PageUtil;
import com.store.common.repository.impl.BaseRepositoryImpl;
import com.store.common.util.StringUtil;
import com.store.data.entity.TbSysUser;
import com.store.repository.SysUserRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class SysUserRepositoryCustomImpl extends BaseRepositoryImpl implements SysUserRepositoryCustom {

    @Override
    public PageList<TbSysUser> list(String loginName, Integer pageNo, Integer pageSize) throws Exception {
        StringBuilder sql = new StringBuilder();
        List<Object> paramList = new ArrayList();
        sql.append(" SELECT u.id, u.login_name loginName, u.user_name userName, u.user_state userState, rg.role_name roleGroupName, u.role_group_id roleGroupId, u.create_date createDate ");
        sql.append(" FROM tb_sys_user u JOIN tb_role_group rg on u.role_group_id = rg.id ");
        sql.append(" WHERE u.user_state != 2 ");
        if (StringUtil.isNotEmpty(loginName)) {
            sql.append(" AND instr(u.login_name, ?) > 0 ");
            paramList.add(loginName);
        }
        return queryPage(sql.toString(), TbSysUser.class, new PageUtil(pageNo, pageSize), null, paramList.toArray());
    }
}