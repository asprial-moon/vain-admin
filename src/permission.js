import router from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css' // Progress 进度条样式
import { Message } from 'element-ui'
import { getToken } from '@/utils/auth' // 验权

NProgress.configure({ showSpinner: false })

const whiteList = ['/login'] // 不重定向白名单

function hasPermission(menus, routeMenu) {
  if (!routeMenu) return true
  return menus.some(menu => routeMenu === menu)
}

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) { // 是否有token
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done() // 重定向到面板
    } else {
      if (store.getters.menus === undefined || store.getters.menus.length === 0) {
        store.dispatch('GetUserMenu').then(res => { // 拉取用户信息
          const menus = res.data.menus // 获取菜单集合
          store.dispatch('GenerateRoutes', { menus }).then(() => {
            router.addRoutes(store.getters.addRouters) // 动态添加路由表
            next({ ...to, replace: true }) // 添加完路由 设置为true 让标题栏不保留之前的历史纪录
          })
        }).catch(() => {
          store.dispatch('FedLogOut').then(() => {
            Message.error('验证失败,请重新登录')
            next({ path: '/login' })
          })
        })
      } else {
        // 无须动态路由  但是还是判断权限
        if (hasPermission(store.getters.menus, to.meta.menu)) {
          next()
        } else {
          next({ path: '/401', replace: true, query: { noGoBack: true }})
        }
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) { // 免权限路径 直接进
      next()
    } else {
      next('/login')
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})
