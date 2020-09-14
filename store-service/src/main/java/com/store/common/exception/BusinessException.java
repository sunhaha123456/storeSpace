package com.store.common.exception;

import com.store.common.data.response.ResponseResultCode;
import lombok.Data;

/**
 * 功能：
 *      业务异常类
 * @author sunpeng
 * @date 2017
 */
@Data
public class BusinessException extends RuntimeException {

    public int code;

    public String msg;

    public BusinessException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(String msg) {
        this.code = 500;
        this.msg = msg;
    }

    public BusinessException(ResponseResultCode res) {
        this.code = res.getCode();
        this.msg = res.getMsg();
    }
}