package com.store.data.to.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class UserLoginRequest {

    // 用户名
    @NotBlank(message = "用户名不能为空！", groups = BaseInfo.class)
    private String loginName;

    // 密码
    @NotBlank(message = "密码不能为空！", groups = BaseInfo.class)
    private String upwd;

    // 验证码 redis key
    @NotBlank(message = "验证码不能为空！", groups = BaseInfo.class)
    private String key;

    // 验证码
    @NotBlank(message = "验证码不能为空！", groups = BaseInfo.class)
    private String code;

    public interface BaseInfo {}
}