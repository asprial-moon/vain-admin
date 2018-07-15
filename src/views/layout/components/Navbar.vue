<template>
  <el-menu class="navbar" mode="horizontal">
      <hamburger class="hamburger-container" :toggleClick="toggleSideBar" :isActive="sidebar.opened"></hamburger>
      <breadcrumb class="breadcrumb-container"></breadcrumb>
      <div class="right-menu"> <span class="user-nickname">{{name}}</span >
      <el-dropdown class="avatar-container right-menu-item" trigger="click">
        <div class="avatar-wrapper">
         <img class="user-avatar" :src="avatar">
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/">
            <el-dropdown-item>
              主页
            </el-dropdown-item>
          </router-link>
          <router-link to="/account/modifyPassword">
            <el-dropdown-item>
              修改密码
            </el-dropdown-item>
          </router-link>
          <!-- <el-dropdown-item divided>
            <span @click="modifyPassword" style="display:block;">修改密码</span>
          </el-dropdown-item> -->
          <el-dropdown-item divided>
            <span @click="logout" style="display:block;">退出登录</span>
          </el-dropdown-item>

        </el-dropdown-menu>
      </el-dropdown>
      </div>
  </el-menu>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'

export default {
  components: {
    Breadcrumb,
    Hamburger,
    Screenfull
  },
  computed: {
    ...mapGetters(['sidebar', 'avatar', 'name'])
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('ToggleSideBar')
    },
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload()
      })
    },
    modifyPassword() {
      console.log(1)
      this.$store
        .dispatch('addVisitedViews', {
          path: '/account/modifyPassword',
          name: '修改密码',
          meta: { title: '修改密码' }
        })
        .then(() => {
          const tags = this.$refs.tag
          if (tags) {
            this.$nextTick(() => {
              this.$refs.scrollPane.moveToTarget(tags.$el)
            })
          }
        })
    }
  }
}
</script>


<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 50px;
  line-height: 50px;
  border-radius: 0px !important;
  .hamburger-container {
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 10px;
  }
  .breadcrumb-container {
    float: left;
  }
  .right-menu {
    float: right;
    height: 100%;
    text-align: center;
    &:focus {
      outline: none;
    }
    .user-nickname {
      letter-spacing: 2px;
      font-size: 13px;
      text-shadow: 0 0 1px #fff, 0 1px 2px rgba(0, 0, 0, 0.3);
      border-radius: 2px;
      text-align: center;
      height: 100%;
      display: inline-block;
    }
    .right-menu-item {
      display: inline-block;
      margin: 0 8px;
    }
    .screenfull {
      height: 20px;
    }
    .international {
      vertical-align: top;
    }
    .theme-switch {
      vertical-align: 15px;
    }
    .avatar-container {
      height: 50px;
      margin-right: 30px;
      .avatar-wrapper {
        cursor: pointer;
        margin-top: 5px;
        position: relative;
        .user-avatar {
          width: 40px;
          height: 40px;
          border-radius: 50px;
        }
        .el-icon-caret-bottom {
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
