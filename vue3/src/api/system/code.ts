import { request } from '@/utils/request/index';

export function queryDs() {
  return request.get({
    url: '/center/system/code/queryDs',
    method: 'get',
  });
}

export function queryTableList(params) {
  return request.get({
    url: '/center/system/code/queryTableList',
    method: 'get',
    params,
  });
}

export function updateTableCfg(data) {
  return request.post({
    url: '/center/system/code/updateTableCfg',
    method: 'post',
    data,
  });
}

export function codeCreate(params) {
  return request.get(
    {
      url: '/center/system/code/codeCreate',
      method: 'get',
      params,
      responseType: 'arraybuffer',
    },
    { isTransformResponse: false },
  );
}

export function addDataSource(data) {
  return request.post({
    url: '/center/system/code/addDataSource',
    method: 'post',
    data,
  });
}

export function delDataSource(data) {
  return request.post({
    url: '/center/system/code/delDataSource',
    method: 'post',
    data,
  });
}

export function checkDataSource(data) {
  return request.post({
    url: '/center/system/code/checkDataSource',
    method: 'post',
    data,
  });
}
