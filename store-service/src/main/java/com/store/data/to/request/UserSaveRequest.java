package com.store.data.to.request;

import lombok.Data;

@Data
public class UserSaveRequest {

    private Long id;

    // 用户登录名
    private String loginName;

    // 用户名
    private String userName;

    // 密码
    private String upwd;

    private Long roleGroupId;
}