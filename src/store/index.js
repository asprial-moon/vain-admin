import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import user from './modules/user'
import permission from './modules/permission'
import getters from './getters'
import tagsView from './modules/tagsView'

Vue.use(Vuex)
/* 定义请求接口 */
const store = new Vuex.Store({
  modules: {
    app,
    user,
    permission,
    tagsView
  },
  getters
})

export default store
