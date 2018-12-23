import { login, logout, getUserMenu } from '@/api/login'
import { getToken, setToken, removeToken, getInformation, setInformation, removeInformation, setEnvironment, getEnvironment, removeEnvironment } from '@/utils/auth'

const user = {
  state: {
    token: getToken(),
    information: getInformation(),
    environment: getEnvironment(),
    roles: [],
    menus: []
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_MENUS: (state, menus) => {
      state.menus = menus
    },
    SET_ENVIRONMENT: (state, environment) => {
      state.environment = environment
    },
    SET_INFORMATION: (state, information) => {
      state.information = information
    }
  },

  actions: {
    // 登录 获取用户信息 用户名 角色 菜单key 头像
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      return new Promise((resolve, reject) => {
        // 调用api的接口
        login(username, userInfo.password).then(response => {
          const data = response.data
          setToken(data.Token)
          commit('SET_TOKEN', data.Token)
          commit('SET_ROLES', data.user.roles)
          setInformation(data.user)
          commit('SET_INFORMATION', data.user)
          setEnvironment(data.environment)
          commit('SET_ENVIRONMENT', data.environment)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取菜单
    GetUserMenu({ commit, state }) {
      return new Promise((resolve, reject) => {
        getUserMenu(state.token).then(response => {
          const data = response.data
          commit('SET_MENUS', data.menus)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_MENUS')
          removeToken()
          removeInformation()
          removeEnvironment()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    }
  }
}

export default user
