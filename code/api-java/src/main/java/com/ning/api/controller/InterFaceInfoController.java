package com.ning.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ning.api.annotation.AuthCheck;
import com.ning.api.common.BaseResponse;
import com.ning.api.common.ErrorCode;
import com.ning.api.common.ResultUtils;
import com.ning.api.constant.UserConstant;
import com.ning.api.exception.BusinessException;
import com.ning.api.exception.ThrowUtils;
import com.ning.api.model.dto.interfaceinfo.*;
import com.ning.api.model.entity.InterfaceInfo;
import com.ning.api.model.entity.User;
import com.ning.api.model.vo.InterfaceInfoVo;
import com.ning.api.model.vo.PageVo;
import com.ning.api.service.InterfaceinfoService;
import com.ning.api.service.UserService;
import com.ning.api.utils.BeanCopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 接口请求类型
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
//@Api(tags = "接口请求管理")
public class InterFaceInfoController {

    @Resource
    private InterfaceinfoService interfaceInfoService;


    /**
     * 调用SDK接口
     *
     * @return
     */
    @PostMapping("/invoke")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInvokeRequest invokeRequest, HttpServletRequest request) {
        Object info = interfaceInfoService.invoke(invokeRequest, request);
        // 查询接口信息
        return ResultUtils.success(info);
    }


    /**
     * 上线接口（仅管理员）
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineInterfaceInfo(@Validated @RequestBody InterfaceInfoRequest dto, HttpServletRequest request) {
        // 查询接口信息
        interfaceInfoService.onlineInterfaceInfo(dto.getId(), request);
        return ResultUtils.success();
    }

    /**
     * 下线接口（仅管理员）
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterfaceInfo(@Validated @RequestBody InterfaceInfoRequest dto) {
        // 查询接口信息
        interfaceInfoService.offlineInterfaceInfo(dto.getId());
        return ResultUtils.success();
    }


    // region 增删改查

    /**
     * 创建
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@Validated @RequestBody InterfaceInfoAddRequest addRequest, HttpServletRequest request) {

        interfaceInfoService.add(addRequest, request);

        return ResultUtils.success();
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteInterfaceInfo(@PathVariable String id) {
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@Validated @RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = interfaceInfoService.upt(interfaceInfoUpdateRequest);

        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     */
    @GetMapping("/get/vo")
    public BaseResponse<InterfaceInfoVo> getInterfaceInfoVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        InterfaceInfoVo interfaceInfoVo = BeanCopyUtil.copyBean(interfaceInfo, InterfaceInfoVo.class);
        return ResultUtils.success(interfaceInfoVo);
    }

    /**
     * 分页获取列表（封装类）
     */
    @GetMapping("/list/page/vo")
    public BaseResponse<PageVo> listInterfaceInfoVOByPage(InterfaceInfoQueryRequest dto) {
        // 限制爬虫
        ThrowUtils.throwIf(dto.getPageSize() > 20, ErrorCode.PARAMS_ERROR);

        Page<InterfaceInfo> pageInfo = interfaceInfoService.selectPage(dto);

        PageVo pageVo = new PageVo();
        pageVo.setCurrent(pageInfo.getCurrent());
        pageVo.setSize(pageInfo.getSize());
        pageVo.setReconds(pageInfo.getRecords());
        pageVo.setTotal(pageInfo.getTotal());

        return ResultUtils.success(pageVo);
    }

}
