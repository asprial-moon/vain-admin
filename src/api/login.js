import request from '@/utils/request'

export function login(username, password) {
  return request({
    url: '/user/login',
    method: 'post',
    data: {
      userName: username,
      password: password
    }
  })
}

export function getUserMenu(token) {
  return request({
    url: '/menu/getMyMenus',
    method: 'post',
    data: {}
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'get',
    data: {}
  })
}
