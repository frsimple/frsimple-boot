import request from '@/utils/request';

// 查询角色列表
export function roleTreeAll() {
  return request({
    url: '/center/system/menu/roleTreeAll',
    method: 'get',
  });
}

// 查询角色列表
export function roleTree(params) {
  return request({
    url: '/center/system/role/roleMenu',
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

// 查询角色列表全部
export function roleAllList() {
  return request({
    url: '/center/system/role/allList',
    method: 'get',
  });
}

// 新增角色
export function addRoleInfo(data) {
  return request({
    url: '/center/system/role/addRole',
    method: 'post',
    data,
  });
}

// 修改角色
export function editRoleInfo(data) {
  return request({
    url: '/center/system/role/editRole',
    method: 'post',
    data,
  });
}

// 删除角色
export function delRoleInfo(data) {
  return request({
    url: '/center/system/role/delRole',
    method: 'post',
    data,
  });
}

// 保存角色权限
export function saveRoleMenu(data) {
  return request({
    url: '/center/system/role/saveRoleMenu',
    method: 'post',
    data,
  });
}
