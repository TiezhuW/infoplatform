<template>
  <div id="app">
    <el-row type="flex">
      <el-col :span="1">
        <el-link :underline="false" href="/">首页</el-link>
      </el-col>
      <el-col :span="1">
        <el-link href="/login" v-if="!isLogin">登录</el-link>
      </el-col>
      <el-col :span="1">
        <el-link href="/register" v-if="!isLogin">注册</el-link>
      </el-col>
      <el-col :span="19"/>
      <el-col :span="2">
        <i class="el-icon-user" v-if="isLogin">&nbsp;
          <el-dropdown>
            <span class="el-dropdown-link">{{currentUser.username}}</span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-link :underline="false" @click="userInfo(currentUser.username)">用户信息</el-link>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-link :underline="false" @click="collections(currentUser.id)">我的收藏</el-link>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-link :underline="false" @click="logout()">退出登录</el-link>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </i>
      </el-col>
    </el-row>
    <hr/>

    <router-view></router-view>
  </div>
</template>

<style>
</style>

<script>
export default {
  name: 'App',
  data() {
    return {
      isLogin: false,
      currentUser: null
    }
  },
  created() {
    if (localStorage.getItem('token') != null) {
      const _this = this
      axios.get('/user/current').then(function (resp) {
        if (resp.data.success == false) {
          _this.isLogin = false
          _this.$message({
            message: '登录状态已失效，请重新登录',
            type: 'warning',
            center: true
          })
          localStorage.removeItem('token')
        } else {
          _this.isLogin = true
          _this.currentUser = resp.data
        }
      })
    } else {
      this.isLogin = false
    }
  },
  methods: {
    collections(id) {
      this.$router.push({
        path: '/collection',
        query: {
          id: id
        }
      })
    },
    userInfo(username) {
      this.$router.push({
        path: '/userInfo',
        query: {
          username: username
        }
      })
    },
    logout() {
      localStorage.removeItem('token')
      window.location.href = '/'
    }
  }
};
</script>
