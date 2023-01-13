import { request } from '@/utils/request/index';

// 查询角色列表
export function logsList(params) {
  return request.get({
    url: '/center/system/logs/list',
    method: 'get',
    params,
  });
}
