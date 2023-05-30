package com.ning.api.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ning.api.mapper.UserMapper;
import com.ning.api.model.entity.User;
import com.ning.api.service.UserGatewayService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author NingWest
 * @date 2023/05/29 07:39
 */
@DubboService
public class UserGatewayServiceImpl implements UserGatewayService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Object akSKGetUser(String ak) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getAccessKey, ak);
        return userMapper.selectOne(wrapper);
    }
}
