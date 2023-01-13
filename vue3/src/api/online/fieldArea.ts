import { request } from '@/utils/request/index';

export function getList(params: any) {
  return request.get({
    url: '/center/online/fieldArea/list',
    method: 'get',
    params,
  });
}

export function getInfo(id: string) {
  return request.get({
    url: `/center/online/fieldArea/info/${id}`,
    method: 'get',
  });
}

export function doEdit(data: any) {
  return request.post({
    url: '/center/online/fieldArea/save',
    method: 'post',
    data,
  });
}

export function doDelete(data: any) {
  return request.post({
    url: '/center/online/fieldArea/delete',
    method: 'post',
    data,
  });
}
