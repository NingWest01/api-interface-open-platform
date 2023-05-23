import {PlusOutlined} from '@ant-design/icons';
import type {ActionType, ProColumns, ProDescriptionsItemProps} from '@ant-design/pro-components';
import {PageContainer, ProDescriptions, ProTable,} from '@ant-design/pro-components';
import {FormattedMessage, useIntl} from '@umijs/max';
import {Button, Drawer, message} from 'antd';
import React, {useRef, useState} from 'react';
import UpdateForm from './components/UpdateForm';
import {
  addInterfaceInfoUsingPOST,
  deleteInterfaceInfoUsingPOST,
  listInterfaceInfoVOByPageUsingGET, offlineInterfaceInfoUsingPOST, onlineInterfaceInfoUsingPOST,
  updateInterfaceInfoUsingPOST
} from "@/services/api-frontend/interFaceInfoController";
import CreateForm from "@/pages/Admin/InterFaceInfo/components/CreateForm";

// 主题内容
const TableList: React.FC = () => {
  /**
   * @en-US Pop-up window of new window
   * @zh-CN 新建窗口的弹窗
   *  */
  const [createModalOpen, handleModalOpen] = useState<boolean>(false);
  /**
   * @en-US The pop-up window of the distribution update window
   * @zh-CN 分布更新窗口的弹窗
   * */
  const [updateModalOpen, handleUpdateModalOpen] = useState<boolean>(false);

  const [showDetail, setShowDetail] = useState<boolean>(false);

  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.RuleListItem>();
  const [setSelectedRows] = useState<API.RuleListItem[]>([]);


  /**
   *  Delete node
   * @zh-CN 删除节点
   *
   * @param selectedRows
   */
// 删除
  const handleRemove = async (one: API.InterfaceInfoVo) => {
    const hide = message.loading('正在删除');
    if (!one) return true;
    try {
      // @ts-ignore
      await deleteInterfaceInfoUsingPOST({
        // @ts-ignore
        id: one.id
      })
      hide();
      message.success('删除成功');
      // 删除完后刷新页面
      actionRef.current?.reload();
      return true;
    } catch (error) {
      hide();
      message.error('删除失败');
      return false;
    }
  };

  /**
   * @en-US Update node
   * @zh-CN 更新节点
   *
   * @param fields
   */
// 修改
  const handleUpdate = async (fields: API.InterfaceInfoVo) => {
    const hide = message.loading('修改中。');
    console.log(fields)
    try {
      // @ts-ignore
      await updateInterfaceInfoUsingPOST({...fields, id: currentRow?.id});
      hide();
      message.success('操作成功');
      return true;
    } catch (error: any) {
      hide();
      message.error('操作失败' + error.message);
      return false;
    }
  };

  /**
   * @en-US Add node
   * @zh-CN 添加节点
   * @param fields
   */

// 处理添加
  const handleAdd = async (fields: API.InterfaceInfoVo) => {
    const hide = message.loading('正在添加');
    try {
      await addInterfaceInfoUsingPOST({...fields});
      hide();
      message.success('添加成功');
      // 关闭模态框
      handleModalOpen(false)
      return true;
    } catch (error: any) {
      hide();
      message.error('添加失败' + error.message);
      return false;
    }
  };


// 上线接口
  const onlineInterFace = async (id: any) => {

    try {
      await onlineInterfaceInfoUsingPOST({id});
      message.success('上线成功');
      // 删除完后刷新页面
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      message.error('上线失败：' + error.message);
      return false;
    }
  };

  // 下线接口
  const offlineInterFace = async (id: any) => {
    try {
      await offlineInterfaceInfoUsingPOST({id});
      message.success('下线成功');
      // 删除完后刷新页面
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      message.error('下线失败：' + error.message);
      return false;
    }
  };


  /**
   * @en-US International configuration
   * @zh-CN 国际化配置
   * */
  const intl = useIntl();

  const columns: ProColumns<API.InterfaceInfoVo>[] = [
    {
      title: "id",
      dataIndex: 'id',
      valueType: 'index',
    },
    {
      title: "接口名",
      dataIndex: 'name',
      valueType: 'text',
      formItemProps: {
        rules: [{
          required: true,
          message: '接口名必须填写！'
        }]
      }
    },
    {
      title: "url",
      dataIndex: 'url',
      valueType: 'textarea',
    },
    {
      title: "请求类型",
      dataIndex: 'method',
      valueType: 'text',
    },
    {
      title: "创建人",
      dataIndex: 'userName',
      valueType: 'text'
    },
    {
      title: "响应头",
      dataIndex: 'responseHeader',
      valueType: 'textarea',
    },
    {
      title: "请求头",
      dataIndex: 'requestHeader',
      valueType: 'textarea',
    },
    {
      title: "接口描述",
      dataIndex: 'description',
      valueType: 'textarea',
    },
    {
      title: "接口状态",
      dataIndex: 'status',
      hideInForm: true,
      valueEnum: {
        0: {
          text: "关闭",
          status: 'Default',
        },
        1: {
          text: "开启",
          status: 'Processing',
        }
      },
    },
    {
      title: "创建时间",
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInForm: true
    },
    {
      title: "操作",
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => [
        <a
          key="update"
          onClick={() => {
            handleUpdateModalOpen(true);
            setCurrentRow(record);
          }}
        >
          修改
        </a>,
        record.status === 0 ? <a
          key="online"
          onClick={() => {
            onlineInterFace(record.id).then(r => {
              console.log(r)
            });
            setCurrentRow(record);
          }}
        >
          上线
        </a> : null,
        record.status === 1 ? <a
          key="offline"
          onClick={() => {
            offlineInterFace(record.id).then(r => {
              console.log(r)
            });
            setCurrentRow(record);
          }}
        >
          下线
        </a> : null,
        <a
          key="delete"
          onClick={() => {
            handleRemove(record).then(r => {
              console.log(r)
            })
          }}
        >
          删除
        </a>
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.RuleListItem, API.PageParams>
        headerTitle={intl.formatMessage({
          id: 'pages.searchTable.title',
          defaultMessage: 'Enquiry form',
        })}
        actionRef={actionRef}
        // key报错
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              handleModalOpen(true);
            }}
          >
            <PlusOutlined/> <FormattedMessage id="pages.searchTable.new" defaultMessage="New"/>
          </Button>,
        ]}
        // 更改自己获取数据的方法
        // @ts-ignore
        request={async (params) => {
          let res = await listInterfaceInfoVOByPageUsingGET({
            ...params
          })
          if (res.data) {
            return {
              data: res.data?.reconds || [],
              success: true,
              total: res.data.total,
            }
          }
        }}
        columns={columns}
        rowSelection={{
          onChange: (_, selectedRows) => {
            // @ts-ignore
            setSelectedRows(selectedRows);
          },
        }}
      />
      <CreateForm
        open={createModalOpen}
        columns={columns}
        // 关闭
        onCancel={() => {
          // 关闭模态框
          handleModalOpen(false)
        }}
        onSubmit={async (value) => {
          const success = await handleAdd(value);
          if (success) {
            handleUpdateModalOpen(false);
            setCurrentRow(undefined);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
      />
      <UpdateForm
        onSubmit={async (value) => {
          const success = await handleUpdate(value);
          if (success) {
            handleUpdateModalOpen(false);
            setCurrentRow(undefined);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
        onCancel={() => {
          handleUpdateModalOpen(false);
          if (!showDetail) {
            setCurrentRow(undefined);
          }
        }}
        columns={columns}
        open={updateModalOpen}
        values={currentRow || {}}
      />

      <Drawer
        width={600}
        open={showDetail}
        onClose={() => {
          setCurrentRow(undefined);
          setShowDetail(false);
        }}
        closable={false}
      >
        {currentRow?.name && (
          <ProDescriptions<API.RuleListItem>
            column={2}
            title={currentRow?.name}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.name,
            }}
            columns={columns as ProDescriptionsItemProps<API.RuleListItem>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};

export default TableList;
