package com.store.data.entity;

import com.store.common.data.base.BaseDataIdLong;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@ToString(callSuper = true)
@lombok.Data
@Entity
@Table(name = "tb_menu_role_group_releation")
public class TbMenuRoleGroupReleation extends BaseDataIdLong implements Serializable {

    @Column(name = "role_group_id", columnDefinition = "Int(20) COMMENT '角色组id'")
    private Long roleGroupId;

    @Column(name = "menu_id", columnDefinition = "Int(20) COMMENT '菜单id'")
    private Long menuId;

    public TbMenuRoleGroupReleation() {
    }

    public TbMenuRoleGroupReleation(Date createDate, Long roleGroupId, Long menuId) {
        this.createDate = createDate;
        this.roleGroupId = roleGroupId;
        this.menuId = menuId;
    }
}