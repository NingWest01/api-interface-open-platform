import React, {useEffect, useState} from 'react';
import {useParams} from "react-router";
import {getInterfaceInfoVOByIdUsingGET} from "@/services/api-frontend/interFaceInfoController";
import {Card} from "antd";

const Index: React.FC = () => {

  let urlId = useParams()

  const [info, setInfo] = useState<API.InterfaceInfoVo>({})

  // 设置 骨架屏
  const [loading, setLoading] = useState(true);
  const getInfo = async () => {
    try {
      // @ts-ignore
      let result = await getInterfaceInfoVOByIdUsingGET({id: urlId?.id})
      if (result?.message === 'ok') {
        // @ts-ignore
        setInfo(result?.data)
        setLoading(false)
      }

      return true;
    } catch (error: any) {
      return false;
    }

  }

  useEffect(() => {
    getInfo()
  }, [])


  console.log(info)

  return (
    <>
      <Card style={{width: '100%', marginTop: 16, height: 400}} loading={loading}>
        <h2>接口名称：{info.name}</h2>
        <p>请求类型：<span style={{color: "red"}}>{info.method}</span></p>
        <p>请求头：{info.requestHeader}</p>
        <p>响应头：{info.responseHeader}</p>
        <p>描述：{info.description}</p>
        <p>请求地址：<span style={{backgroundColor: '#e0e0d0'}}>{info.url}</span></p>
        <p>发布人：{info.userName}</p>
        <p>发布时间：{info.createTime}</p>
        <p>修改时间：{info.updateTime}</p>
      </Card>
    </>
  );
};

export default Index;
