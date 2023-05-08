package com.ning.api.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author NingWest
 * @date 2023/05/03 18:57
 */
@Data
public class PageVo {

    @ApiModelProperty("当前页")
    private Long current;

    @ApiModelProperty("每页大小")
    private Long size;

    @ApiModelProperty("数据")
    private Object reconds;

    @ApiModelProperty("总数")
    private Long total;
}
