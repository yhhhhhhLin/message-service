package xyz.linyh.messageservice.model;

import lombok.Data;
import xyz.linyh.messageservice.constant.ErrorCode;

/**
 * 统一返回类型
 * @param <T>
 */
@Data
public class BaseResponse<T> {

    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }

}
