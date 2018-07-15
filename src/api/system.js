import request from '@/utils/request'

export function getConfigPageList(currentPage, pageSize) {
  return request({
    url: '/systemConfig/getPagedList',
    method: 'post',
    data: {
      currentPage: currentPage,
      pageSize: pageSize
    }
  })
}

export function modifyConfig(params) {
  return request({
    url: '/systemConfig/modify',
    method: 'post',
    data: params
  })
}

export function getScheduleList(params) {
  return request({
    url: '/scheduleJob/getList',
    method: 'post',
    data: params
  })
}

export function modifySchedule(params) {
  return request({
    url: '/scheduleJob/modify',
    method: 'post',
    data: params
  })
}

export function deleteSchedule(params) {
  return request({
    url: '/scheduleJob/delete',
    method: 'post',
    data: params
  })
}

export function triggerSchedule(params) {
  return request({
    url: '/scheduleJob/triggerJob',
    method: 'post',
    data: params
  })
}

export function resumeSchedule(params) {
  return request({
    url: '/scheduleJob/resumeJob',
    method: 'post',
    data: params
  })
}

export function pauseSchedule(params) {
  return request({
    url: '/scheduleJob/pauseJob',
    method: 'post',
    data: params
  })
}

export function getLogList(params) {
  return request({
    url: '/log/getPagedList',
    method: 'post',
    data: params
  })
}

export function deleteLogs(params) {
  return request({
    url: '/log/delete',
    method: 'post',
    data: params
  })
}
