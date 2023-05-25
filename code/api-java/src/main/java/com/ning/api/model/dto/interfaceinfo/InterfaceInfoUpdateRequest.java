package com.ning.api.model.dto.interfaceinfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 更新请求
 */
@Data
public class InterfaceInfoUpdateRequest implements Serializable {

    /**
     * id
     */
    @NotNull(message = "id，必须存在")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 接口名称
     */
    @ApiModelProperty("接口名称")
    @NotBlank(message = "接口名称，不能为空")
    private String name;

    /**
     * 接口描述
     */
    @NotBlank(message = "接口描述，不能为空")
    @ApiModelProperty("接口名称")
    private String description;

    /**
     * 请求参数&类型
     */
    @ApiModelProperty("请求参数&类型")
    @NotBlank(message = "请求参数&类型，不能为空")
    private String requestParams;


    /**
     * 接口地址
     */
    @NotBlank(message = "接口地址，不能为空")
    @ApiModelProperty("接口地址")
    private String url;

    /**
     * 请求类型
     */
    @NotBlank(message = "请求类型，不能为空")
    @ApiModelProperty("请求类型")
    private String method;

    /**
     * 请求头
     */
    @NotBlank(message = "请求头，不能为空")
    @ApiModelProperty("请求头")
    private String requestHeader;

    /**
     * 响应头
     */
    @NotBlank(message = "响应头，不能为空")
    @ApiModelProperty("响应头")
    private String responseHeader;

    /**
     * 接口状态（0 关闭 1 开启）
     */
    @Min(value = 0, message = "接口状态 0 | 1")
    @Max(value = 1, message = "接口状态 0 | 1")
    @ApiModelProperty("接口状态（0 关闭 1 开启）")
    private Integer status;

    private static final long serialVersionUID = 1L;
}