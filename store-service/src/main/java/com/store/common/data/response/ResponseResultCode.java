package com.store.common.data.response;

/**
 * 功能：返回状态码枚举类
 * @author sunpeng
 * @date 2017
 */
public enum ResponseResultCode {

	SUCCESS(200, "成功！"),

	SERVER_ERROR(500, "服务器异常！"),

	PARAM_ERROR(40001, "参数错误！"),
	LOGIC_ERROR(40002, "业务逻辑错误！"),

	LOGIN_ERROR(40004, "用户名或密码错误！"),
	CODE_ERROR(40005, "验证码错误！"),
	LOGIN_FIRST(40006, "登录信息已失效，请重新登录！"),
	USER_STATE_ERROR(40007, "用户状态不正常！"),
	OPERT_ERROR(40008, "操作失败！"),
	USER_OLD_PASSWORD_ERROR(40009, "用户密码错误！"),
	NO_DATA_RECORD_ERROR(40010, "数据记录不存在！");

	private Integer code;
	
	private String msg;
	
	ResponseResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}