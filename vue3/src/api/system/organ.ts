import { request } from '@/utils/request/index';

export function queryOrganTree(params) {
  return request.get({
    url: '/center/system/organ/queryOrganTree',
    method: 'get',
    params,
  });
}

export function getOrgan(id) {
  return request.get({
    url: `/center/system/organ/getOrgan/${id}`,
    method: 'get',
  });
}

export function addOrgan(data) {
  return request.post({
    url: '/center/system/organ/addOrgan',
    method: 'post',
    data,
  });
}

export function editOrgan(data) {
  return request.post({
    url: '/center/system/organ/editOrgan',
    method: 'post',
    data,
  });
}

export function delOrgan(id) {
  return request.post({
    url: `/center/system/organ/delOrgan/${id}`,
    method: 'post',
  });
}
