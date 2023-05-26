import React, {useEffect, useState} from 'react';
import {useParams} from "react-router";
import {
  getInterfaceInfoVOByIdUsingGET,
  invokeInterfaceInfoUsingPOST
} from "@/services/api-frontend/interFaceInfoController";
import {Button, Card, Divider, Form, Input, message} from "antd";
import copy from 'copy-to-clipboard';
import {isNil} from "lodash";

const Index: React.FC = () => {

  let urlId = useParams()

  const [info, setInfo] = useState<API.InterfaceInfoVo>({})

  // 设置 骨架屏
  const [loading, setLoading] = useState(true);
  const [resLoading, setResLoading] = useState(false);

  // 接受请求的信息
  const [dataInfo, setDataInfo] = useState<string>();


  const getInfo = async () => {
    if (urlId === undefined || urlId.id === undefined) {
      message.error("缺失必要参数");
      return;
    }
    try {
      // @ts-ignore
      let result = await getInterfaceInfoVOByIdUsingGET({id: urlId?.id})
      if (result?.msg === 'ok') {
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

  // 一键复制
  const copyCot = (cot: string) => {
    copy(cot);
    message.success('复制成功').then(r => {
      console.log(r)
    }); // 不需要提示 可注释
  }

  const onFinish = async (values: string) => {
    // 骨架平 出现
    setResLoading(true)
    // @ts-ignore
    let {params} = values
    // 校验字符串
    if (isNil(params)) {
      message.error('缺失请求参数').then(r => {
        console.log(r)
      });
      return
    }
    // 请求接口
    let result = await invokeInterfaceInfoUsingPOST({
      id: info.id,
      requestParams: params,
      url: info.url
    })
    let {code, data, msg} = result;
    if (code === 0) {
      message.success("发送请求成功")
      // @ts-ignore
      setDataInfo(data?.data)
    } else {
      message.error("发送请求失败")
      setDataInfo(msg)
    }
    // 骨架屏关闭 时间长一点 ！ 效果好看
    setTimeout(() => {
      // 请求成功，骨架平隐藏
      setResLoading(false)
    }, 200)
  };


  return (
    <>
      <Card style={{width: '100%', marginTop: 16, height: '100%'}} loading={loading}>
        <h2>接口名称：{info.name}</h2>
        <p>请求类型：<span style={{color: "red"}}>{info.method}</span></p>
        <p>请求头：{info.requestHeader}</p>
        <p>响应头：{info.responseHeader}</p>
        <p>描述：{info.description}</p>
        <p>请求参数：{info.requestParams}</p>
        <p>请求地址：<span style={{backgroundColor: '#e0e0d0'}}>{info.url}</span></p>
        <p>接口状态：{info.status === 1 ? '开放' : '关闭'}</p>
        <p>发布人：{info.userName}</p>
        <p>发布时间：{info.createTime}</p>
        <p>修改时间：{info.updateTime}</p>
        <Button onClick={() => copyCot(info.url as string)} type="primary">一键复制，请求地址</Button>
      </Card>
      <Card style={{width: '100%', marginTop: 16, height: '100%'}}>
        <h2>接口调用</h2>
        <Divider/>
        <Form
          layout='vertical'
          name="basic"
          style={{maxWidth: 600}}
          initialValues={{remember: true}}
          onFinish={onFinish}
          autoComplete="off"
        >
          <Form.Item
            label="请求参数"
            name="params"
          >
            <Input.TextArea placeholder='请求参数格式：{"name":"..."}'/>
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              发送请求
            </Button>
          </Form.Item>
        </Form>
      </Card>

      <Card style={{width: '100%', marginTop: 16, height: '100%'}} loading={resLoading}>
        <h2>结果显示：</h2>
        <p style={{color: 'red', fontSize: '30px'}}>{dataInfo}</p>
      </Card>

    </>
  );
};

export default Index;
