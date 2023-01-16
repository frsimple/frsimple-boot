import request from '@/utils/request';

// 保存短信配置信息
export function saveOrUpdate(data) {
  return request({
    url: '/center/system/sms/saveOrUpdate',
    method: 'post',
    data,
  });
}

// 查询短信配置信息
export function smsConfig(type) {
  return request({
    url: `/center/system/sms/${type}`,
    method: 'get',
  });
}
