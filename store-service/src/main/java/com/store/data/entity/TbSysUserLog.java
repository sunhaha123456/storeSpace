package com.store.data.entity;

import com.store.common.data.base.BaseDataIdLong;
import lombok.AllArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@lombok.Data
@AllArgsConstructor
@Entity
@Table(name = "tb_sys_user_log")
public class TbSysUserLog extends BaseDataIdLong implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "url", columnDefinition = "varchar(255) binary COMMENT 'url地址'")
    private String url;

    @Column(name = "ip", columnDefinition = "varchar(100) COMMENT 'ip地址'")
    private String ip;

    @Column(name = "args", columnDefinition = "text binary COMMENT '参数'")
    private String args;

    @Column(name = "ret", columnDefinition = "text binary COMMENT '返回'")
    private String ret;
}