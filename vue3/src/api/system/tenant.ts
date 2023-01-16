import request from '@/utils/request';

// 查询机构列表
export function tenantList(params) {
  return request({
    url: '/center/system/tenant/list',
    method: 'get',
    params,
  });
}

// 查询机构列表
export function tenantAllList(params) {
  return request({
    url: '/center/system/tenant/allList',
    method: 'get',
    params,
  });
}

// 新增机构
export function addTenant(data) {
  return request({
    url: '/center/system/tenant/addTenant',
    method: 'post',
    data,
  });
}

// 修改机构
export function editTenant(data) {
  return request({
    url: '/center/system/tenant/editTenant',
    method: 'post',
    data,
  });
}

// 删除机构
export function delTenant(id) {
  return request({
    url: `/center/system/tenant/delTenant/${id}`,
    method: 'post',
  });
}

// 根据name查询机构
export function getTenant(params) {
  return request({
    url: '/center/system/tenant/getTenant',
    method: 'get',
    params,
  });
}
