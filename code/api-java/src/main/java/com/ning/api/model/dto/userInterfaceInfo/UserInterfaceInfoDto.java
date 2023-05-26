package com.ning.api.model.dto.userInterfaceInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户调用接口关系
 */
@Data
public class UserInterfaceInfoDto implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 接口id
     */
    @NotNull(message = "接口id，不能为空")
    @Min(value = 1, message = "接口id，输入错误")
    @ApiModelProperty("接口id")
    private Long interfaceinfoId;

    /**
     * 总调用次数
     */
    @NotNull(message = "总调用次数，不能为空")
    @Min(value = 1, message = "调用次数过小")
    @ApiModelProperty("总调用次数")
    private Integer totalNum;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}