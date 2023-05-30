package com.ning.api.service;

/**
 * @author NingWest
 * @date 2023/05/29 07:39
 */
public interface UserInterfaceGatewayService {

    /**
     * 请求接口 次数 + 1
     *
     * @param interFaceId 接口id
     * @param userId      请求者 id
     */
    boolean minusOne(Long interFaceId, Long userId);

    /**
     * 请求信息
     *
     * @param interFaceId 接口id
     * @param userId      请求者 id
     */
    Object interfaceInfo(Long interFaceId, Long userId);
}
