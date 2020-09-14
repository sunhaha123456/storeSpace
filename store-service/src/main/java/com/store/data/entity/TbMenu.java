package com.store.data.entity;

import com.store.common.data.base.BaseDataIdLong;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@ToString(callSuper = true)
@lombok.Data
@Entity
@Table(name = "tb_menu")
public class TbMenu extends BaseDataIdLong implements Comparable<TbMenu> {

    @Column(name = "menu_name", columnDefinition = "varchar(255) binary COMMENT '菜单名称'")
    private String menuName;

    // easyui中的菜单名称
    @Transient
    private String text;

    @Column(name = "icon_cls", columnDefinition = "varchar(255) binary COMMENT '菜单图标'")
    private String iconCls;

    @Column(name = "url", columnDefinition = "varchar(255) binary COMMENT '跳转链接'")
    private String url;

    // 0：无上级目录
    @Column(name = "parent_id", columnDefinition = "Int(20) COMMENT '上级目录id'")
    private Long parentId;

    // 1：第一级目录 2：第二级目录
    @Column(name = "dir_level", columnDefinition = "Int(1) COMMENT '目录深度'")
    private Integer dirLevel;

    // 由大到小排序
    @Column(name = "sort", columnDefinition = "Int(20) COMMENT '排序'")
    private Long sort;

    // 0：正常 1：冻结
    @Column(name = "menu_state", columnDefinition = "Int(1) default 0 COMMENT '菜单状态'")
    private Integer menuState;

    @Transient
    private Long roleGroupId;

    // 是否选中 true：选中，false：不选中
    @Transient
    private boolean checked;

    // 是否打开 open 打开 closed 关闭
    @Transient
    private String state;

    @Transient
    private List<TbMenu> children;

    @Override
    public int compareTo(TbMenu menu) {
        int sort_1 = menu.getSort() == null ? 0 : menu.getSort().intValue();
        int sort_2 = this.getSort() == null ? 0 : this.getSort().intValue();
        return sort_1 - sort_2;
    }
}