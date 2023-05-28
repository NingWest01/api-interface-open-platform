package com.ning.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ning.api.annotation.AuthCheck;
import com.ning.api.common.BaseResponse;
import com.ning.api.common.ErrorCode;
import com.ning.api.common.ResultUtils;
import com.ning.api.constant.UserConstant;
import com.ning.api.exception.BusinessException;
import com.ning.api.exception.ThrowUtils;
import com.ning.api.model.dto.userInterfaceInfo.UserInterfaceInfoDto;
import com.ning.api.model.dto.userInterfaceInfo.UserInterfaceInfoRequest;
import com.ning.api.model.entity.InterfaceInfo;
import com.ning.api.model.entity.User;
import com.ning.api.model.entity.UserInterfaceInfo;
import com.ning.api.model.vo.PageVo;
import com.ning.api.service.InterfaceinfoService;
import com.ning.api.service.UserInterfaceInfoService;
import com.ning.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户调用接口
 */
@RestController
@RequestMapping("/userInterfaceInfo")
@Slf4j
//@Api(tags = "用户调用接口")
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private InterfaceinfoService interfaceinfoService;


    // region 增删改查


    /**
     * 创建
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@Validated @RequestBody UserInterfaceInfoDto dto, HttpServletRequest request) {

        userInterfaceInfoService.add(dto, request);

        return ResultUtils.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteInterfaceInfo(@PathVariable String id) {
        boolean b = userInterfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 减少调用次数
     */
    @GetMapping("/minusOne")
    public BaseResponse<String> minusOne(@RequestParam("userInterFaceId") Long userInterFaceId, HttpServletRequest request) {

        userInterfaceInfoService.minusOne(userInterFaceId, request);

        return ResultUtils.success();
    }

    /**
     * 修改
     */
    @PutMapping("/upt")
    public BaseResponse<Boolean> uptInterfaceInfo(@Validated @RequestBody UserInterfaceInfoDto dto, HttpServletRequest request) {

        userInterfaceInfoService.upt(dto, request);

        return ResultUtils.success();
    }

    @GetMapping("/getOne")
    public BaseResponse<UserInterfaceInfo> getUserInterfaceInfoVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(id);
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 如果状态为 1 直接返回空
        if (userInterfaceInfo.getStatus() == 1) {
            return ResultUtils.success();
        }
        if (userInterfaceInfo.getUserId() != null && userInterfaceInfo.getUserId() > 0) {
            User user = userService.getById(userInterfaceInfo.getUserId());
            userInterfaceInfo.setUsername(user.getUserName());
        }
        if (userInterfaceInfo.getInterfaceinfoId() != null && userInterfaceInfo.getInterfaceinfoId() > 0) {
            InterfaceInfo interfaceInfo = interfaceinfoService.getById(userInterfaceInfo.getInterfaceinfoId());
            userInterfaceInfo.setInterFaceName(interfaceInfo.getName());
        }
        return ResultUtils.success(userInterfaceInfo);
    }

    @GetMapping("/list/page/vo")
    public BaseResponse<PageVo> listInterfaceInfoVOByPage(UserInterfaceInfoRequest dto) {
        // 限制爬虫
        ThrowUtils.throwIf(dto.getPageSize() > 20, ErrorCode.PARAMS_ERROR);

        Page<UserInterfaceInfo> pageInfo = userInterfaceInfoService.selectPage(dto);

        PageVo pageVo = new PageVo();
        pageVo.setCurrent(pageInfo.getCurrent());
        pageVo.setSize(pageInfo.getSize());
        pageVo.setReconds(pageInfo.getRecords());
        pageVo.setTotal(pageInfo.getTotal());

        return ResultUtils.success(pageVo);
    }


}
