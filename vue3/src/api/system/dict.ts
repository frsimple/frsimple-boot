import request from '@/utils/request';

// 查询字典列表
export function listDict(params) {
  return request({
    url: '/center/system/dict/list',
    method: 'get',
    params,
  });
}

// 查询字典列表
export function listDict1(params) {
  return request({
    url: '/center/system/dict/list1',
    method: 'get',
    params,
  });
}

// 查询字典项
export function dictValues(params) {
  return request({
    url: '/center/system/dict/values',
    method: 'get',
    params,
  });
}

// 查询角色列表
export function roleList(params) {
  return request({
    url: '/center/system/role/list',
    method: 'get',
    params,
  });
}

// 新增字典
export function addDict(data) {
  return request({
    url: '/center/system/dict/addDict',
    method: 'post',
    data,
  });
}

// 修改字典
export function editDict(data) {
  return request({
    url: '/center/system/dict/editDict',
    method: 'post',
    data,
  });
}

// 删除字典
export function delDict(data) {
  return request({
    url: '/center/system/dict/delDict',
    method: 'post',
    data:data,
  });
}

// 刷新字典缓存
export function refDictCache() {
  return request({
    url: '/center/system/dict/refDictCache',
    method: 'get',
  });
}
