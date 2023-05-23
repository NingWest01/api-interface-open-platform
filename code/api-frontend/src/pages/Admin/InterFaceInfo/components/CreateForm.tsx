import {ProColumns, ProTable,} from '@ant-design/pro-components';
import React from 'react';
import {Modal} from "antd";

export type Props = {
  // 将列全部传递过来
  columns: ProColumns<API.InterfaceInfoVo>[],
  onCancel: () => void;
  // @ts-ignore
  onSubmit: (values: API.InterFaceInfo) => Promise<void>;
  open: boolean;
};

const CreateFrom: React.FC<Props> = (props) => {
  const {open, onCancel, onSubmit, columns} = props
  return (
    <Modal footer={null} destroyOnClose={true} open={open} onCancel={() => onCancel?.()}>
      <ProTable
        type="form"
        columns={columns}
        onSubmit={async (value) => {
          onSubmit?.(value)
        }}
      />
    </Modal>
  )
};

export default CreateFrom;
