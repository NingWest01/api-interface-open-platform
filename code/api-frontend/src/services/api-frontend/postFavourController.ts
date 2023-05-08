// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** doPostFavour POST /api/postfavour/ */
export async function doPostFavourUsingPOST(
  body: API.PostFavourAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseint>('/api/postfavour/', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listFavourPostByPage POST /api/postfavour/list/page */
export async function listFavourPostByPageUsingPOST(
  body: API.PostFavourQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePagePostVO>('/api/postfavour/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** listMyFavourPostByPage POST /api/postfavour/my/list/page */
export async function listMyFavourPostByPageUsingPOST(
  body: API.PostQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePagePostVO>('/api/postfavour/my/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
