import { request } from '@/utils/request/index';

// 保存短信配置信息
export function saveOrUpdate(data) {
  return request.post({
    url: '/center/system/sms/saveOrUpdate',
    method: 'post',
    data,
  });
}

// 查询短信配置信息
export function smsConfig(type) {
  return request.get({
    url: `/center/system/sms/${type}`,
    method: 'get',
  });
}
