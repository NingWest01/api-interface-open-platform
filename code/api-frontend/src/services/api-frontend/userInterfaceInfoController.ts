// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addInterfaceInfo POST /api/userInterfaceInfo/add */
export async function addInterfaceInfoUsingPOST1(
  body: API.UserInterfaceInfoDto,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponselong>('/api/userInterfaceInfo/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** analysisInfo GET /api/userInterfaceInfo/analysis */
export async function analysisInfoUsingGET(options?: { [key: string]: any }) {
  return request<API.BaseResponseListAnalysisVo>('/api/userInterfaceInfo/analysis', {
    method: 'GET',
    ...(options || {}),
  });
}

/** deleteInterfaceInfo DELETE /api/userInterfaceInfo/delete/${param0} */
export async function deleteInterfaceInfoUsingDELETE(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteInterfaceInfoUsingDELETEParams,
  options?: { [key: string]: any },
) {
  const { id: param0, ...queryParams } = params;
  return request<API.BaseResponseboolean>(`/api/userInterfaceInfo/delete/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** getUserInterfaceInfoVOById GET /api/userInterfaceInfo/getOne */
export async function getUserInterfaceInfoVOByIdUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserInterfaceInfoVOByIdUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseUserInterfaceInfo>('/api/userInterfaceInfo/getOne', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listInterfaceInfoVOByPage GET /api/userInterfaceInfo/list/page/vo */
export async function listInterfaceInfoVOByPageUsingGET1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listInterfaceInfoVOByPageUsingGET1Params,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageVo>('/api/userInterfaceInfo/list/page/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** minusOne GET /api/userInterfaceInfo/minusOne */
export async function minusOneUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.minusOneUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsestring>('/api/userInterfaceInfo/minusOne', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** uptInterfaceInfo PUT /api/userInterfaceInfo/upt */
export async function uptInterfaceInfoUsingPUT(
  body: API.UserInterfaceInfoDto,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseboolean>('/api/userInterfaceInfo/upt', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
