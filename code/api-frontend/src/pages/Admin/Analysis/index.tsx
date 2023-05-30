import {PageContainer,} from '@ant-design/pro-components';
import React, {useEffect, useState} from 'react';
import ReactECharts from 'echarts-for-react';
import {analysisInfoUsingGET} from "@/services/api-frontend/userInterfaceInfoController";
import {message} from "antd";

const Analysis: React.FC = () => {

  const [data, setData] = useState<API.AnalysisVo>();

  const getAnalysisInfo = async () => {
    let {code, data, msg} = await analysisInfoUsingGET();
    if (code === 0) {
      // @ts-ignore
      setData(data)
    } else {
      message.error("信息获取失败 - " + msg);
    }

  }

  useEffect(() => {
    getAnalysisInfo()
  }, [])

  // 图表分析
  const option = {
    title: {
      top: '10',
      text: '接口详情',
      left: 'center',
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '调用次数',
        type: 'pie',
        radius: '80%',
        data,
        emphasis: {
          itemStyle: {
            shadowBlur: 20,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };

  return (
    <PageContainer>
      <ReactECharts style={{height: 500}} option={option}/>
    </PageContainer>
  );
};

export default Analysis;
