import {PageContainer} from '@ant-design/pro-components';
import {useModel} from '@umijs/max';
import {Card, Pagination, theme} from 'antd';
import type {PaginationProps} from 'antd';
import React, {useEffect, useState} from 'react';
import {listInterfaceInfoVOByPageUsingGET} from "@/services/api-frontend/interFaceInfoController";

/**
 * 每个单独的卡片，为了复用样式抽成了组件
 * @param param0
 * @returns
 */
const InfoCard: React.FC<{
  title: string;
  index: number;
  desc: string;
  id: number;
}> = ({title, id, index, desc}) => {
  const {useToken} = theme;

  const {token} = useToken();

  return (
    <div
      style={{
        backgroundColor: token.colorBgContainer,
        boxShadow: token.boxShadow,
        borderRadius: '8px',
        fontSize: '14px',
        color: token.colorTextSecondary,
        lineHeight: '22px',
        padding: '16px 19px',
        minWidth: '220px',
        flex: 1,
      }}
    >
      <div
        style={{
          display: 'flex',
          gap: '4px',
          alignItems: 'center',
        }}
      >
        <div
          style={{
            width: 48,
            height: 48,
            lineHeight: '22px',
            backgroundSize: '100%',
            textAlign: 'center',
            padding: '8px 16px 16px 12px',
            color: '#FFF',
            fontWeight: 'bold',
            backgroundImage:
              "url('https://gw.alipayobjects.com/zos/bmw-prod/daaf8d50-8e6d-4251-905d-676a24ddfa12.svg')",
          }}
        >
          {index}
        </div>
        <div
          style={{
            fontSize: '16px',
            color: token.colorText,
            paddingBottom: 8,
          }}
        >
          {title}
        </div>
      </div>
      <div
        style={{
          fontSize: '14px',
          color: token.colorTextSecondary,
          textAlign: 'justify',
          lineHeight: '22px',
          marginBottom: 8,
        }}
      >
        {desc}
      </div>
      {/*<a href={`interfaceInfo/detail/${id}`} target="_blank" rel="noreferrer">*/}
      <a href={`interfaceInfo/detail/${id}`}>
        了解更多 {'>'}
      </a>
    </div>
  );
};

const Index: React.FC = () => {
  const {token} = theme.useToken();
  const {initialState} = useModel('@@initialState');

  const [data, setData] = useState<API.InterfaceInfoVo[]>();
  const [total, setTotal] = useState<number>();

  const [loading, setLoading] = useState(true);
  const [current, setCurrent] = useState(1);
  // 获取数据
  const getData = async (current: number) => {
    setCurrent(current)
    try {
      let result = await listInterfaceInfoVOByPageUsingGET({
        current: current,
        pageSize: 4
      });
      // @ts-ignore
      setData(result?.data?.reconds)
      setTotal(result?.data?.total)
      setLoading(false)
      return true;
    } catch (error: any) {
      return false;
    }
  };

  useEffect(() => {
    getData(1)
  }, [])

  // 点击切换分页
  const onChangePage: PaginationProps['onChange'] = (page) => {
    setCurrent(current)
    getData(page)
  };


  return (
    <PageContainer>
      <Card
        style={{
          borderRadius: 8,
        }}
        bodyStyle={{
          backgroundImage:
            initialState?.settings?.navTheme === 'realDark'
              ? 'background-image: linear-gradient(75deg, #1A1B1F 0%, #191C1F 100%)'
              : 'background-image: linear-gradient(75deg, #FBFDFF 0%, #F5F7FF 100%)',
        }}
      >
        <div
          style={{
            backgroundPosition: '100% -30%',
            backgroundRepeat: 'no-repeat',
            backgroundSize: '274px auto',
            backgroundImage:
              "url('https://gw.alipayobjects.com/mdn/rms_a9745b/afts/img/A*BuFmQqsB2iAAAAAAAAAAAAAAARQnAQ')",
          }}
        >
          <div
            style={{
              fontSize: '20px',
              color: token.colorTextHeading,
            }}
          >
            欢迎使用 API 接口开放平台
          </div>
          <div
            style={{
              textIndent: '2em',
              fontSize: '14px',
              color: token.colorTextSecondary,
              lineHeight: '22px',
              marginTop: 16,
              marginBottom: 32,
              width: '100%',
            }}
          >
            <p>
              欢迎使用我们的API接口开放平台！我们致力于为开发者和企业提供一个强大、安全和灵活的接口服务。通过我们的平台，您可以访问各种功能丰富的API，以满足您的应用程序和业务需求。
            </p>
            <p>
              我们的API接口开放平台提供了广泛的功能和服务，涵盖了多个领域和行业。无论您是开发移动应用程序、构建网站、设计智能设备还是处理大数据，我们都能够为您提供合适的API接口。
            </p>
            <p>
              使用我们的API接口，您可以轻松地与我们的系统进行交互，获取实时数据、发送请求、执行操作和管理资源。我们的接口设计简洁易用，同时保证了安全性和稳定性。我们采用标准化的协议和认证机制，确保您的数据和用户信息的保密性和完整性。
            </p>
            <p>
              开始使用我们的API接口开放平台，并体验前所未有的开发便利和灵活性！如有任何疑问或需求，请随时联系我们的团队。感谢您选择我们的服务！
            </p>
          </div>
          <h2> 接口实例： </h2>
          <div
            style={{
              display: 'flex',
              flexWrap: 'wrap',
              gap: 16,
            }}
          >
            {
              data?.map(item => {
                return <InfoCard
                  loading={loading}
                  // 获取数组的下标
                  index={data?.map(a => a).indexOf(item) + 1}
                  key={item.id}
                  // @ts-ignore
                  title={item.name}
                  // @ts-ignore
                  id={item.id}
                  // @ts-ignore
                  desc={item.description}
                />
              })
            }
          </div>
        </div>

        <div style={{
          marginTop: '20px',
          float: 'right'
        }}>
          <Pagination
            showTotal={(total) => `总条数： ${total}`}
            current={current}
            defaultPageSize={5}
            total={total}
            onChange={onChangePage}
          />
        </div>

      </Card>

    </PageContainer>
  );
};

export default Index;
