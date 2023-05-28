package com.ning.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ning.api.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.ning.api.model.dto.userInterfaceInfo.UserInterfaceInfoDto;
import com.ning.api.model.dto.userInterfaceInfo.UserInterfaceInfoRequest;
import com.ning.api.model.entity.InterfaceInfo;
import com.ning.api.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author W1323
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
 * @createDate 2023-05-26 07:32:15
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 减少调用次数
     *
     * @param userInterFaceId 服务的接口id
     * @param request         请求
     */
    void minusOne(Long userInterFaceId, HttpServletRequest request);

    /**
     * 增加调用次数
     *
     * @param dto     参数
     * @param request 请求
     */
    void add(UserInterfaceInfoDto dto, HttpServletRequest request);


    /**
     * 修改信息
     */
    void upt(UserInterfaceInfoDto dto, HttpServletRequest request);

    /**
     * 分页查询
     */
    Page<UserInterfaceInfo> selectPage(UserInterfaceInfoRequest dto);
}
