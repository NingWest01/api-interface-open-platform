package com.ning.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ning.api.common.ErrorCode;
import com.ning.api.exception.BusinessException;
import com.ning.api.mapper.InterfaceinfoMapper;
import com.ning.api.mapper.UserInterfaceInfoMapper;
import com.ning.api.model.entity.InterfaceInfo;
import com.ning.api.model.entity.UserInterfaceInfo;
import com.ning.api.service.UserInterfaceGatewayService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author NingWest
 * @date 2023/05/29 07:39
 */
@DubboService
public class UserInterfaceGatewayServiceImpl implements UserInterfaceGatewayService {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceinfoMapper interfaceinfoMapper;


    @Override
    public boolean minusOne(Long interFaceId, Long userId) {
        if (interFaceId == null || interFaceId <= 0) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR);
        }
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR);
        }

        QueryWrapper<UserInterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserInterfaceInfo::getUserId, userId).eq(UserInterfaceInfo::getInterfaceinfoId, interFaceId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(wrapper);

        // 查看是否存在此信息
        if (Objects.isNull(userInterfaceInfo)) {
            throw new BusinessException(ErrorCode.NO_HAVE_ERROR);
        }
        if (userInterfaceInfo.getLeftNum() <= 0) {
            throw new BusinessException(ErrorCode.REQUEST_EXHAUSTION);
        }
        userInterfaceInfo.setLeftNum(userInterfaceInfo.getLeftNum() - 1);

        int update = userInterfaceInfoMapper.updateById(userInterfaceInfo);

        return update == 1;
    }

    @Override
    public Object interfaceInfo(Long interFaceId, Long userId) {
        if (interFaceId == null || interFaceId <= 0) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR);
        }
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserInterfaceInfo::getInterfaceinfoId, interFaceId).eq(UserInterfaceInfo::getUserId, userId);
        return userInterfaceInfoMapper.selectOne(wrapper);
    }
}
