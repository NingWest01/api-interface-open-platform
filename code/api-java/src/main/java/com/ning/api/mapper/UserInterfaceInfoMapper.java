package com.ning.api.mapper;

import com.ning.api.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ning.api.model.vo.AnalysisVo;

import java.util.List;

/**
* @author W1323
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2023-05-26 07:32:15
* @Entity generator.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    /**
     * 数据图表分析
     * @return
     */
    List<UserInterfaceInfo> analysisInfo();
}




