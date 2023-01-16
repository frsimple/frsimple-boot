import request from '@/utils/request';

export function getList(params: any) {
  return request({
    url: '/center/online/table/list',
    method: 'get',
    params,
  });
}

export function getInfo(id: string) {
  return request({
    url: `/center/online/table/info/${id}`,
    method: 'get',
  });
}

export function doEdit(data: any) {
  return request({
    url: '/center/online/table/save',
    method: 'post',
    data,
  });
}

export function doDelete(data: any) {
  return request({
    url: '/center/online/table/delete',
    method: 'post',
    data,
  });
}
