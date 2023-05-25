package com.ning.api.model.dto.interfaceinfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 创建请求
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {

    /**
     * 接口名称
     */
    @ApiModelProperty("接口名称")
    @NotBlank(message = "接口名字不能为空")
    private String name;

    /**
     * 接口描述
     */
    @ApiModelProperty("接口描述")
    @NotBlank(message = "接口描述，不能为空")
    private String description;

    /**
     * 接口地址
     */
    @ApiModelProperty("接口地址")
    @NotBlank(message = "接口地址，不能为空")
    private String url;

    /**
     * 请求类型
     */
    @ApiModelProperty("请求类型")
    @NotBlank(message = "请求类型，不能为空")
    private String method;

    /**
     * 请求参数&类型
     */
    @ApiModelProperty("请求参数&类型")
    @NotBlank(message = "请求参数&类型，不能为空")
    private String requestParams;

    /**
     * 请求头
     */
    @ApiModelProperty("请求头")
    @NotBlank(message = "请求头，不能为空")
    private String requestHeader;

    /**
     * 响应头
     */
    @ApiModelProperty("响应头")
    @NotBlank(message = "响应头，不能为空")
    private String responseHeader;

    private static final long serialVersionUID = 1L;
}