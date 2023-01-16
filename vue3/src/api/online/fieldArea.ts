import request from '@/utils/request';

export function getList(params: any) {
  return request({
    url: '/center/online/fieldArea/list',
    method: 'get',
    params,
  });
}

export function getInfo(id: string) {
  return request({
    url: `/center/online/fieldArea/info/${id}`,
    method: 'get',
  });
}

export function doEdit(data: any) {
  return request({
    url: '/center/online/fieldArea/save',
    method: 'post',
    data,
  });
}

export function doDelete(data: any) {
  return request({
    url: '/center/online/fieldArea/delete',
    method: 'post',
    data,
  });
}
