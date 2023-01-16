import request from '@/utils/request';

// 查询用户列表
export function userList(params) {
  return request({
    url: '/center/system/user/list',
    method: 'get',
    params,
  });
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/center/system/user/addUser',
    method: 'post',
    data,
  });
}

// 修改用户
export function editUser(data) {
  return request({
    url: '/center/system/user/editUser',
    method: 'post',
    data,
  });
}

// 删除用户
export function delUser(id) {
  return request({
    url: `/center/system/user/delUser/${id}`,
    method: 'post',
  });
}

// 根据name查询用户
export function getUser(params) {
  return request({
    url: '/center/system/user/list1',
    method: 'get',
    params,
  });
}

// 锁定用户
export function lockUser(id) {
  return request({
    url: `/center/system/user/lock/${id}`,
    method: 'get',
  });
}
// 解锁用户
export function unlockUser(id) {
  return request({
    url: `/center/system/user/unlock/${id}`,
    method: 'get',
  });
}

// 重置密码
export function resetPwd(id) {
  return request({
    url: `/center/system/user/resetPwd/${id}`,
    method: 'get',
  });
}
// 修改密码
export function updatePwd(data) {
  return request({
    url: '/center/system/user/updatePwd',
    method: 'post',
    data,
  });
}

// 检验密码是否正确
export function checkPwd(data) {
  return request({
    url: '/center/system/user/checkPwd',
    method: 'post',
    data,
  });
}

// 更换头像
export function updateAvatar(data) {
  return request({
    url: '/center/system/user/updateAvatar',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}

// 查询当前用户信息
export function queryUser() {
  return request({
    url: '/center/system/user/queryUser',
    method: 'get',
  });
}

// 修改当前用户的基本信息
export function updateUser(data) {
  return request({
    url: '/center/system/user/updateUser',
    method: 'post',
    data,
  });
}

// 发送短信验证码
export function sendMsg(params) {
  return request({
    url: '/center/system/user/sendMsg',
    method: 'get',
    params,
  });
}

// 修改用户绑定手机号
export function updatePhone(params) {
  return request({
    url: '/center/system/user/updatePhone',
    method: 'get',
    params,
  });
}

// 发送邮箱验证码
export function sendEmail(params) {
  return request({
    url: '/center/system/user/sendEmail',
    method: 'get',
    params,
  });
}

// 修改用户绑定邮箱
export function updateEmail(params) {
  return request({
    url: '/center/system/user/updateEmail',
    method: 'get',
    params,
  });
}
