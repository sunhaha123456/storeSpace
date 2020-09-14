//package com.store.data.enums;
//
///**
// * 描述：流程实例状态 enum
// * @author sunpeng
// */
//public enum FlowInstanceStateEnum {
//
//    // tb_flow_instance 表 state 字段，如下：
//    // 流程实例状态:
//    // 0已启动 1已完成 2用户撤回 3管理员冻结 4驳回
//
//    HAVE_STARTED(0, "已启动"),
//    HAVE_FINISH(1, "已完成"),
//    USER_WITHDRAW(2, "用户撤回"),
//    ADMIN_FROZEN(3, "管理员冻结"),
//    USER_REJECT(4, "驳回"), ;
//
//    private Integer index;
//    private String name;
//
//    FlowInstanceStateEnum(Integer index, String name) {
//        this.index = index;
//        this.name = name;
//    }
//
//    public static String getName(Integer index) {
//        for (FlowInstanceStateEnum c : FlowInstanceStateEnum.values()) {
//            if (c.getIndex().equals(index)) {
//                return c.name;
//            }
//        }
//        return null;
//    }
//
//    public static Integer getIndex(String name) {
//        for (FlowInstanceStateEnum c : FlowInstanceStateEnum.values()) {
//            if (c.getName().equals(name)) {
//                return c.index;
//            }
//        }
//        return null;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public Integer getIndex() {
//        return index;
//    }
//}