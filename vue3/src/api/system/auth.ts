import request from '@/utils/request';

const secret = '6ceb70f6cca98475ae91bb8aa9657b6d';

export function getPublicKey() {
  return request({
    url: '/center/system/auth/getPublicKey',
    method: 'get',
  });
}

export async function loginByUserName(data) {
  return request({
    url: '/center/system/auth/doLogin',
    method: 'post',
    data,
  });
}

export function loginByUserPhone(data) {
  return request({
    url: '/center/system/auth/oauth/token',
    method: 'post',
    ...data,
    headers: {
      isAuth: false,
      sp: secret,
      grant_type: 'sms_code',
    },
  });
}

export function sendSms(params) {
  return request({
    url: '/center/system/sms',
    method: 'get',
    params,
    headers: {
      isAuth: false,
    },
  });
}

export function getCurUserInfo() {
  return request({
    url: '/center/system/user/info',
    method: 'get',
  });
}

export function getCurUserMenu() {
  return request({
    url: '/center/system/user/menu',
    method: 'get',
  });
}

export function logout() {
  return request({
    url: '/center/system/auth/logout',
    method: 'get',
  });
}

export function tokenList(params) {
  return request({
    url: '/center/system/auth/list',
    method: 'get',
    params,
  });
}

export function userLogout(params) {
  return request({
    url: '/center/system/auth/userLogout',
    method: 'get',
    params,
  });
}
