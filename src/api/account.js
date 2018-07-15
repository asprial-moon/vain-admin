import request from '@/utils/request'

export function getUserList(params, currentPage, pageSize) {
  params.currentPage = currentPage
  params.pageSize = pageSize
  return request({
    url: '/user/getPagedList',
    method: 'post',
    data: params
  })
}
export function getRoleList() {
  return request({
    url: '/role/getList',
    method: 'post',
    data: {}
  })
}

export function getMenuList() {
  return request({
    url: '/menu/getMenuList',
    method: 'post',
    data: {}
  })
}

export function getMenusByUserId(params) {
  return request({
    url: '/menu/getMenusByUserId',
    method: 'post',
    data: params
  })
}

export function getRole(roleId) {
  return request({
    url: '/role/getById/' + roleId,
    method: 'get'
  })
}

export function addUser(params) {
  return request({
    url: '/user/add',
    method: 'post',
    data: params
  })
}

export function modifyUser(params) {
  return request({
    url: '/user/modify',
    method: 'post',
    data: params
  })
}

export function lockUser(params) {
  return request({
    url: '/user/lock',
    method: 'post',
    data: params
  })
}

export function deleteUser(params) {
  return request({
    url: '/user/delete',
    method: 'post',
    data: params
  })
}

export function resetPassword(params) {
  return request({
    url: '/user/resetPassword',
    method: 'post',
    data: params
  })
}

export function modifyPassword(params) {
  return request({
    url: '/user/modifyPassword',
    method: 'post',
    data: params
  })
}

export function assignUserMenu(params) {
  return request({
    url: '/user/assignUserMenu',
    method: 'post',
    data: params
  })
}

export function addRole(params) {
  return request({
    url: '/role/add',
    method: 'post',
    data: params
  })
}

export function deteleRole(params) {
  return request({
    url: '/role/delete',
    method: 'post',
    data: params
  })
}

export function modifyRole(params) {
  return request({
    url: '/role/modify',
    method: 'post',
    data: params
  })
}

export function assignRoleMenu(params) {
  return request({
    url: '/role/assignRoleMenu',
    method: 'post',
    data: params
  })
}

export function modifyMenu(params) {
  return request({
    url: '/menu/modify',
    method: 'post',
    data: params
  })
}
