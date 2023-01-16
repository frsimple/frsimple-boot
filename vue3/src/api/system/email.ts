import request from '@/utils/request';

// 保存邮箱配置信息
export function saveOrUpdate(data) {
  return request({
    url: '/center/system/email/saveOrUpdate',
    method: 'post',
    data,
  });
}

// 发送邮件
export function sendEmail(data) {
  return request({
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
  return request({
    url: '/center/system/email/emailCfg',
    method: 'get',
  });
}
