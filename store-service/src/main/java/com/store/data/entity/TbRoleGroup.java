package com.store.data.entity;

import com.store.common.data.base.BaseDataIdLong;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@ToString(callSuper = true)
@lombok.Data
@Entity
@Table(name = "tb_role_group")
public class TbRoleGroup extends BaseDataIdLong implements Serializable {

    // 角色组名
    @Column(name = "role_name", columnDefinition = "varchar(255) binary COMMENT '角色名称'")
    private String roleName;

    // 角色组备注
    @Column(name = "role_remark", columnDefinition = "varchar(255) binary COMMENT '角色备注'")
    private String roleRemark;

    // 0：正常 1：冻结
    @Column(name = "role_state", columnDefinition = "Int(1) default 0 COMMENT '角色状态'")
    private Integer roleState;
}