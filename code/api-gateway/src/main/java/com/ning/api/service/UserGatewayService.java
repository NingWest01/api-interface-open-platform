package com.ning.api.service;

/**
 * @author NingWest
 * @date 2023/05/29 07:39
 */
public interface UserGatewayService {
    /**
     * 根据 ak 查询用户的
     * @param ak ak
     */
    Object akSKGetUser(String ak);
}
