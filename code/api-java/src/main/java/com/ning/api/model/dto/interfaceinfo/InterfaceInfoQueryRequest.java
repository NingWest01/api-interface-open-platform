package com.ning.api.model.dto.interfaceinfo;

import com.ning.api.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 接口状态（0 关闭  1 开启）
     */
    private Integer status;
}