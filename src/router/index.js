import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import Layout from '../views/layout/Layout'

// 固定加载的页面
export const constantRouterMap = [
  { path: '/login', component: () => import('@/views/login/login'), hidden: true },
  { path: '/404', component: () => import('@/views/404'), hidden: true },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: 'Dashboard',
    hidden: true,
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index')
      }
    ]
  }]

export default new Router({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

export const asyncRouterMap = [
  {
    path: '/system',
    component: Layout,
    redirect: 'noredirect',
    name: 'System',
    meta: { title: '系统管理', icon: 'setting', menu: 'system' },
    children: [
      {
        path: 'config',
        name: 'config',
        component: () => import('@/views/system/config'),
        meta: { title: '系统配置', icon: 'config', menu: 'system.config' }
      },
      {
        path: 'schedule',
        name: 'Schedule',
        component: () => import('@/views/system/schedule'),
        meta: { title: '系统任务', icon: 'schedule', menu: 'system.scheduleJob' }
      },
      {
        path: 'operationLog',
        name: 'OperationLog',
        component: () => import('@/views/system/operationLog'),
        meta: { title: '系统日志', icon: 'log', menu: 'system.log' }
      }
    ]
  },
  {
    path: '/account',
    component: Layout,
    redirect: 'noredirect',
    name: 'account',
    meta: { title: '账号管理', icon: 'account', menu: 'account' },
    children: [
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/account/user'),
        meta: { title: '用户管理', icon: '1', menu: 'account.user' }
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('@/views/account/role'),
        meta: { title: '角色管理', icon: '2', menu: 'account.role' }
      },
      {
        path: 'menu',
        name: 'Menu',
        component: () => import('@/views/account/menu'),
        meta: { title: '菜单管理', icon: '3', menu: 'account.menu' }
      },
      {
        path: 'modifyPassword',
        name: 'modifyPassword',
        component: () => import('@/views/account/modifyPassword'),
        meta: { title: '修改密码', icon: '', menu: 'account.user.modifyPassword' },
        hidden: true
      }
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]
