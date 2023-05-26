package com.ning.api.model.dto.userInterfaceInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ning.api.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户调用接口关系
 */
@Data
public class UserInterfaceInfoRequest extends PageRequest implements Serializable {
    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 状态（0正常 1禁止 ）
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}