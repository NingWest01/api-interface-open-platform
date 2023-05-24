package com.ning.api.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ning.api.common.BaseResponse;
import com.ning.api.common.ErrorCode;
import com.ning.api.constant.CommonConstant;
import com.ning.api.exception.BusinessException;
import com.ning.api.mapper.InterfaceinfoMapper;
import com.ning.api.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.ning.api.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.ning.api.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.ning.api.model.entity.InterfaceInfo;
import com.ning.api.model.entity.User;
import com.ning.api.service.InterfaceinfoService;
import com.ning.api.service.UserService;
import com.ning.api.utils.BeanCopyUtil;
import com.ning.sdk.client.MyClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * @author W1323
 * @description 针对表【interfaceinfo(接口信息表)】的数据库操作Service实现
 * @createDate 2023-05-02 19:21:18
 */
@Service
public class InterfaceinfoServiceImpl extends ServiceImpl<InterfaceinfoMapper, InterfaceInfo>
        implements InterfaceinfoService {

    @Resource
    private UserService userService;

    @Resource
    private InterfaceinfoMapper interfaceinfoMapper;

    @Resource
    private MyClient apiClient;

    @Override
    public void add(InterfaceInfoAddRequest addRequest, HttpServletRequest request) {

        User loginUser = userService.getLoginUser(request);

        // 获取信息
        InterfaceInfo interfaceInfo = BeanCopyUtil.copyBean(addRequest, InterfaceInfo.class);

        // 使用用户的账户
        interfaceInfo.setUserName(loginUser.getUserAccount());
        interfaceInfo.setUserId(loginUser.getId());

        if (!save(interfaceInfo)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

    }

    @Override
    public Boolean upt(InterfaceInfoUpdateRequest dto) {
        InterfaceInfo interfaceInfo = interfaceinfoMapper.selectById(dto.getId());

        if (Objects.isNull(interfaceInfo)) {
            throw new BusinessException(ErrorCode.NO_HAVE_ERROR);
        }

        interfaceInfo.setName(dto.getName());
        interfaceInfo.setDescription(dto.getDescription());
        interfaceInfo.setUrl(dto.getUrl());
        interfaceInfo.setMethod(dto.getMethod());
        interfaceInfo.setRequestHeader(dto.getRequestHeader());
        interfaceInfo.setResponseHeader(dto.getResponseHeader());
        interfaceInfo.setStatus(dto.getStatus());
        interfaceInfo.setUpdateTime(new Date());

        return updateById(interfaceInfo);
    }


    @Override
    public Page<InterfaceInfo> selectPage(InterfaceInfoQueryRequest dto) {
        Page<InterfaceInfo> interfaceInfoPage = new Page<>(dto.getCurrent(), dto.getPageSize());
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(dto.getKeyword())) {
            wrapper.lambda()
                    .like(InterfaceInfo::getName, dto.getKeyword())
                    .or()
                    .like(InterfaceInfo::getDescription, dto.getKeyword())
                    .or()
                    .like(InterfaceInfo::getMethod, dto.getKeyword())
                    .or()
                    .like(InterfaceInfo::getUserName, dto.getKeyword());
        }
        // 状态信息
        if (dto.getStatus() != null && dto.getStatus() >= 0 && dto.getStatus() <= 1) {
            wrapper.lambda().eq(InterfaceInfo::getStatus, dto.getStatus());
        }

        // 根据创建时间倒序
        wrapper.lambda().orderByDesc(InterfaceInfo::getCreateTime);

        return interfaceinfoMapper.selectPage(interfaceInfoPage, wrapper);
    }

    @Override
    public void onlineInterfaceInfo(Long id) {
        InterfaceInfo interfaceInfo = this.getById(id);

        if (Objects.isNull(interfaceInfo)) {
            throw new BusinessException(ErrorCode.NO_HAVE_ERROR);
        }
        // 求地址
        String url = interfaceInfo.getUrl();
        if (StringUtils.isBlank(url)) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR);
        }
        // 引入第三方SDK 进行请求访问 注意：这里因为测试问题，没有真正调用  url地址 todo
        com.ning.sdk.model.User user = new com.ning.sdk.model.User();
        user.setName("ning test");

        String result = apiClient.postJsonName(user);
        if (StringUtils.isBlank(result)) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR);
        }
        BaseResponse bean = JSONUtil.toBean(result, BaseResponse.class);

        if (Objects.isNull(bean)) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR);
        }
        if (CommonConstant.ERROR_CODE == bean.getCode()) {
            throw new BusinessException(ErrorCode.NO_RETURN_ERROR);
        }

        // 接口状态（0 关闭 1 开启）  这里是 上线接口
        interfaceInfo.setStatus(1);

        if (!this.updateById(interfaceInfo)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

    }

    @Override
    public void offlineInterfaceInfo(Long id) {
        InterfaceInfo interfaceInfo = this.getById(id);

        if (Objects.isNull(interfaceInfo)) {
            throw new BusinessException(ErrorCode.NO_HAVE_ERROR);
        }
        // 求地址
        String url = interfaceInfo.getUrl();
        if (StringUtils.isBlank(url)) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR);
        }
        // 接口状态（0 关闭 1 开启）  这里是 下线接口
        interfaceInfo.setStatus(0);

        if (!this.updateById(interfaceInfo)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }
}




