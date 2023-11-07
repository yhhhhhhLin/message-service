package xyz.linyh.messageservice.model.dto;

import lombok.Data;

/**
 * 基础的请求分页类
 */
@Data
public class BasePageReq {

    /**
     * 页面大小
     */
    private Integer pageSize;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 判断页大小是否超过最大值
     */
    public Boolean isValid(){
        return pageSize != null && pageSize <= 50 && pageSize > 0;
    }
}
