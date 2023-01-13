import { request } from '@/utils/request/index';

// 查询字典项
export function dicVals(code) {
  return request.get({
    url: `/center/system/dict/values/${code}`,
    method: 'get',
  });
}
