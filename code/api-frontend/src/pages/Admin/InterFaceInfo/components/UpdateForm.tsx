import {ProColumns, ProFormInstance, ProTable,} from '@ant-design/pro-components';
import React, {useEffect, useRef} from 'react';
import {Modal} from "antd";

export type Props = {
  values: API.InterfaceInfoVo,
  // 将列全部传递过来
  columns: ProColumns<API.InterfaceInfoVo>[],
  onCancel: () => void;
  // @ts-ignore
  onSubmit: (values: API.InterFaceInfo) => Promise<void>;
  open: boolean;
};


const UpdateFrom: React.FC<Props> = (props) => {
  const {values, open, onCancel, onSubmit, columns} = props

  const formRef = useRef<ProFormInstance>()
  // 监听数据的变化重新赋值
  useEffect(() => {
    formRef.current?.setFieldsValue(values)
  }, [values])

  return (
    <Modal footer={null} open={open} onCancel={() => onCancel?.()}>
      <ProTable
        type="form"
        columns={columns}
        formRef={formRef}
        onSubmit={async (value) => {
          onSubmit?.(value)
        }}
      />
    </Modal>
  )
};

export default UpdateFrom;
