package com.ning.api.model.entity;

import lombok.Data;

/**
 * @author NingWest
 * @date 2023/05/25 10:26
 */

@Data
public class Response {

    private Integer code;

    private String msg;

    private Object data;

}
