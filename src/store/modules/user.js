import { login, logout, getUserMenu } from '@/api/login'
import { getToken, setToken, removeToken, getName, setName, removeName, getAvatar, setAvatar, removeAvatar } from '@/utils/auth'

const user = {
  state: {
    token: getToken(),
    name: getName(),
    avatar: getAvatar(),
    roles: [],
    menus: []
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_MENUS: (state, menus) => {
      state.menus = menus
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
          setName(data.user.nickname)
          commit('SET_NAME', data.user.nickname)
          setAvatar(data.user.avatar)
          commit('SET_AVATAR', data.user.avatar)
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
          removeAvatar()
          removeName()
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
