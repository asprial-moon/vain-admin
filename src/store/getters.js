const getters = {
  sidebar: state => state.app.sidebar,
  token: state => state.user.token,
  information: state => state.user.information,
  avatar: state => state.user.information.avatar,
  roles: state => state.user.roles,
  menus: state => state.user.menus,
  environment: state => state.user.environment,
  permission_routers: state => state.permission.routers,
  addRouters: state => state.permission.addRouters,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews
}
export default getters
