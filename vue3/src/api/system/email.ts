import { request } from '@/utils/request/index';

// 保存邮箱配置信息
export function saveOrUpdate(data) {
  return request.post({
    url: '/center/system/email/saveOrUpdate',
    method: 'post',
    data,
  });
}

// 发送邮件
export function sendEmail(data) {
  return request.post({
    url: '/center/system/email/sendEmail',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}

// 查询邮件配置信息
export function emailCfg() {
  return request.get({
    url: '/center/system/email/emailCfg',
    method: 'get',
  });
}
