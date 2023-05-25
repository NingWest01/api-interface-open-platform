package com.ning.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ning.api.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.ning.api.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.ning.api.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.ning.api.model.dto.interfaceinfo.InterfaceInvokeRequest;
import com.ning.api.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author W1323
 * @description 针对表【interfaceinfo(接口信息表)】的数据库操作Service
 * @createDate 2023-05-02 19:21:18
 */
public interface InterfaceinfoService extends IService<InterfaceInfo> {

    /**
     * 添加
     */
    void add(InterfaceInfoAddRequest addRequest, HttpServletRequest request);

    /**
     * 修改
     */
    Boolean upt(InterfaceInfoUpdateRequest dto);


    /**
     * 分页查询
     */
    Page<InterfaceInfo> selectPage(InterfaceInfoQueryRequest dto);

    /**
     * 上线接口
     * @param id  id
     */
    void onlineInterfaceInfo(Long id);

    /**
     * 下线接口
     * @param id
     */
    void offlineInterfaceInfo(Long id);

    /**
     * 调用远程接口
     * @param request
     * @return
     */
    Object invoke(InterfaceInvokeRequest invokeRequest, HttpServletRequest request);
}
