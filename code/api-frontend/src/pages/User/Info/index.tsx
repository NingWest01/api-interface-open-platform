import React, {useEffect, useState} from 'react';
import {Card, Image, Divider, Button, message} from 'antd';
import {
  UserOutlined,
  TeamOutlined,
  VerifiedOutlined,
  FieldTimeOutlined,
  UnlockOutlined,
  InsuranceOutlined,
  CopyTwoTone
} from '@ant-design/icons';
import {getLoginUserUsingGET} from "@/services/api-frontend/userController";
import copy from "copy-to-clipboard";

const Info: React.FC = () => {

  const [loading, setLoading] = useState(true);

  const [userInfo, setUserInfo] = useState<API.LoginUserVO>();
  const getUserInfo = async () => {
    let {code, data, msg} = await getLoginUserUsingGET();
    if (code === 0) {
      // @ts-ignore
      setUserInfo(data)
      setLoading(false)
    } else {
      message.error("信息获取失败-" + msg)
      setLoading(false)
    }
  }

  useEffect(() => {
    getUserInfo()
  }, [])


  /**
   * 重置密钥
   */
  const reset = () => {
    console.log("重置密钥")
  }
  /**
   * 申请密钥
   */
  const applyFor = () => {
    console.log("申请密钥")
  }
  /**
   * 复制ak
   */
  const copyAk = (ak: string) => {
    copy(ak);
    message.success("accessKey 复制成功！")
  }
  /**
   * 复制sk
   */
  const copySk = (sk: string) => {
    copy(sk);
    message.success("secretKey 复制成功！")
  }


  return (
    <div style={{
      marginTop: '10px'
    }}>
      <Card title="个人信息" style={{width: '25%', boxShadow: '5px 5px 5px grey'}} loading={loading}>
        <div style={{
          textAlign: 'center'
        }}>
          <Image
            width={100}
            style={{
              borderRadius: '50%',
            }}
            src={userInfo?.userAvatar}
          />
        </div>
        <Divider/>
        <p><UserOutlined/>&nbsp;用户账号：{userInfo?.userAccount}</p>
        <Divider/>
        <p><TeamOutlined/>&nbsp;用户昵称：{userInfo?.userName}</p>
        <Divider/>
        <p><VerifiedOutlined/>&nbsp;用户角色：{userInfo?.userRole}</p>
        <Divider/>
        <p><FieldTimeOutlined/>&nbsp;注册时间：{userInfo?.createTime}</p>
      </Card>
      <Card title="密钥操作" style={{
        display: "block",
        marginTop: '-500px',
        float: 'right',
        width: '70%',
        boxShadow: '5px 5px 5px grey'
      }}>
        <p><InsuranceOutlined/>&nbsp;accessKey：{userInfo?.accessKey}</p>
        <CopyTwoTone onClick={() => copyAk(userInfo?.accessKey as string)} style={{fontSize: '16px'}}/>
        <Divider/>
        <div>
          <p><UnlockOutlined/>&nbsp;secretKey：{userInfo?.secretKey}</p>
          <CopyTwoTone onClick={() => copySk(userInfo?.secretKey as string)} style={{fontSize: '16px'}}/>
        </div>
        <Divider style={{marginBottom: '50px'}}/>
        <Button onClick={() => applyFor()} type="primary" size='middle' style={{boxShadow: '5px 5px 5px grey'}}>
          申请密钥
        </Button>
        <Button onClick={() => reset()} danger style={{marginLeft: '50px', boxShadow: '5px 5px 5px grey'}}
                type="primary" size='middle'>
          重置密钥
        </Button>
      </Card>
    </div>

  );
};

export default Info;
