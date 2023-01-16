import request from '@/utils/request';

export function queryDs() {
  return request({
    url: '/center/system/code/queryDs',
    method: 'get',
  });
}

export function queryTableList(params) {
  return request({
    url: '/center/system/code/queryTableList',
    method: 'get',
    params,
  });
}

export function updateTableCfg(data) {
  return request({
    url: '/center/system/code/updateTableCfg',
    method: 'post',
    data,
  });
}

export function codeCreate(params) {
  return request({
    url: '/center/system/code/codeCreate',
    method: 'get',
    params,
    responseType: 'arraybuffer',
  });
}

export function addDataSource(data) {
  return request({
    url: '/center/system/code/addDataSource',
    method: 'post',
    data,
  });
}

export function delDataSource(data) {
  return request({
    url: '/center/system/code/delDataSource',
    method: 'post',
    data,
  });
}

export function checkDataSource(data) {
  return request({
    url: '/center/system/code/checkDataSource',
    method: 'post',
    data,
  });
}
