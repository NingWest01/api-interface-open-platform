package com.ning.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ning.api.common.ErrorCode;
import com.ning.api.exception.BusinessException;
import com.ning.api.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.ning.api.model.dto.userInterfaceInfo.UserInterfaceInfoDto;
import com.ning.api.model.dto.userInterfaceInfo.UserInterfaceInfoRequest;
import com.ning.api.model.entity.InterfaceInfo;
import com.ning.api.model.entity.User;
import com.ning.api.model.entity.UserInterfaceInfo;
import com.ning.api.service.InterfaceinfoService;
import com.ning.api.service.UserInterfaceInfoService;
import com.ning.api.mapper.UserInterfaceInfoMapper;
import com.ning.api.service.UserService;
import com.ning.api.utils.BeanCopyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author W1323
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
 * @createDate 2023-05-26 07:32:15
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {

    @Resource
    private UserService userService;

    @Resource
    private InterfaceinfoService interfaceinfoService;

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;


    @Override
    public void minusOne(Long userInterFaceId, HttpServletRequest request) {
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectById(userInterFaceId);
        if (Objects.isNull(userInterfaceInfo)) {
            throw new BusinessException(ErrorCode.NO_HAVE_ERROR);
        }
        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        // 申请接口次数的人
        Long userId = userInterfaceInfo.getUserId();
        if (!Objects.equals(userId, loginUser.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        // 先判断是否有请求的次数
        Integer leftNum = userInterfaceInfo.getLeftNum();
        if (leftNum <= 0) {
            throw new BusinessException(ErrorCode.REQUEST_EXHAUSTION);
        }
        // 接口请求次数减一
        userInterfaceInfo.setLeftNum(leftNum - 1);

        if (!this.updateById(userInterfaceInfo)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public Page<UserInterfaceInfo> selectPage(UserInterfaceInfoRequest dto) {
        Page<UserInterfaceInfo> interfaceInfoPage = new Page<>(dto.getCurrent(), dto.getPageSize());
        QueryWrapper<UserInterfaceInfo> wrapper = new QueryWrapper<>();
        // 状态信息
        if (dto.getStatus() != null && dto.getStatus() >= 0 && dto.getStatus() <= 1) {
            wrapper.lambda().eq(UserInterfaceInfo::getStatus, dto.getStatus());
        }

        // 根据创建时间倒序
        wrapper.lambda().orderByDesc(UserInterfaceInfo::getCreateTime);

        return userInterfaceInfoMapper.selectPage(interfaceInfoPage, wrapper);
    }


    @Override
    public void add(UserInterfaceInfoDto dto, HttpServletRequest request) {
        // 获取登录用户信息
        User loginUser = userService.getLoginUser(request);
        if (Objects.isNull(loginUser)) {
            throw new BusinessException(ErrorCode.NO_HAVE_ERROR);
        }

        InterfaceInfo interfaceInfo = interfaceinfoService.getById(dto.getInterfaceinfoId());

        if (Objects.isNull(interfaceInfo)) {
            throw new BusinessException(ErrorCode.NO_HAVE_ERROR);
        }

        QueryWrapper<UserInterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserInterfaceInfo::getUserId, loginUser.getId()).eq(UserInterfaceInfo::getInterfaceinfoId, interfaceInfo.getId());
        UserInterfaceInfo userInterfaceInfoOld = userInterfaceInfoMapper.selectOne(wrapper);

        // 如果已存在不可再添加
        if (!Objects.isNull(userInterfaceInfoOld)) {
            throw new BusinessException(ErrorCode.OBJECT_IS_HAVE);
        }

        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        userInterfaceInfo.setUserId(loginUser.getId());
        userInterfaceInfo.setInterfaceinfoId(interfaceInfo.getId());
        userInterfaceInfo.setTotalNum(dto.getTotalNum());
        userInterfaceInfo.setLeftNum(dto.getTotalNum());

        if (!save(userInterfaceInfo)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

    }

    @Override
    public void upt(UserInterfaceInfoDto dto, HttpServletRequest request) {
        if (dto.getId() == null || dto.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR);
        }
        // 获取登录用户信息
        User loginUser = userService.getLoginUser(request);
        if (Objects.isNull(loginUser)) {
            throw new BusinessException(ErrorCode.NO_HAVE_ERROR);
        }

        InterfaceInfo interfaceInfo = interfaceinfoService.getById(dto.getInterfaceinfoId());

        if (Objects.isNull(interfaceInfo)) {
            throw new BusinessException(ErrorCode.NO_HAVE_ERROR);
        }

        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        userInterfaceInfo.setId(dto.getId());
        userInterfaceInfo.setUserId(loginUser.getId());
        userInterfaceInfo.setInterfaceinfoId(interfaceInfo.getId());
        userInterfaceInfo.setTotalNum(dto.getTotalNum());

        if (!updateById(userInterfaceInfo)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

    }
}




