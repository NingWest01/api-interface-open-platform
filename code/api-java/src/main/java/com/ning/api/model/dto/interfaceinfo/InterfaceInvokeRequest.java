package com.ning.api.model.dto.interfaceinfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查询请求
 */
@Data
public class InterfaceInvokeRequest implements Serializable {

    /**
     * id
     */
    @NotNull(message = "id，必须存在")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 接口地址
     */
    @NotBlank(message = "接口地址，不能为空")
    @ApiModelProperty("接口地址")
    private String url;

    /**
     * 请求参数&类型
     */
    @ApiModelProperty("请求参数&类型")
    @NotBlank(message = "请求参数&类型，不能为空")
    private String requestParams;
}