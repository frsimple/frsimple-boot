import request from '@/utils/request';

// 获取菜单根据固定条件
export function getMenuList(params) {
  return request({
    url: '/center/system/menu/menuList',
    method: 'get',
    params,
  });
}

// 查询菜单权限信息
export function getBtnList(params) {
  return request({
    url: '/center/system/menu/btnList',
    method: 'get',
    params,
  });
}

// 查询菜单树，所有数据
export function getTreeMenuAll() {
  return request({
    url: '/center/system/menu/treeAll',
    method: 'get',
  });
}

// 新增菜单
export function addMenuInfo(data) {
  return request({
    url: '/center/system/menu/addMenu',
    method: 'post',
    data,
  });
}

// 修改菜单
export function editMenuInfo(data) {
  return request({
    url: '/center/system/menu/editMenu',
    method: 'post',
    data,
  });
}

// 删除菜单信息
export function delMenuInfo(data) {
  return request({
    url: '/center/system/menu/delMenu',
    method: 'post',
    data,
  });
}

// 新增菜单按钮权限
export function addBtnMenuInfo(data) {
  return request({
    url: '/center/system/menu/addBtnMenu',
    method: 'post',
    data,
  });
}

// 修改菜单按钮权限
export function editBtnMenuInfo(data) {
  return request({
    url: '/center/system/menu/editBtnMenu',
    method: 'post',
    data,
  });
}

// 删除菜单按钮权限
export function delBtnMenuInfo(data) {
  return request({
    url: '/center/system/menu/delBtnMenu',
    method: 'post',
    data,
  });
}
