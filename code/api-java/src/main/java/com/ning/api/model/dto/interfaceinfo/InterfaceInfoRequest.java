package com.ning.api.model.dto.interfaceinfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查询请求
 */
@Data
public class InterfaceInfoRequest implements Serializable {

    /**
     * id
     */
    @NotNull(message = "id，必须存在")
    @ApiModelProperty("id")
    private Long id;
}