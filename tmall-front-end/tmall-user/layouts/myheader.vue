<template>
  <div>
    <!--这里放nav-->
    <nav class="top">
      <div class="navTopDiv">
        <a href="/">
          <span class="glyphicon glyphicon-home redColor"></span>天猫首页
        </a>
        <span>喵，欢迎来天猫</span>
        <router-link :to="userInfo == null ? '/login' : '/accountSet'" ref="name">{{userInfo == null ? "请登录" : userInfo.name}}</router-link>
        <a v-show="userInfo" @click="quit" href="javascript:void(0)">退出</a>
        <router-link v-show="!userInfo" to="/regist">免费注册</router-link>
        <span class="pull-right">
            <a :href="userInfo == null ? '/login' : '/order/' + userInfo.id">我的订单</a>
            <a :href="userInfo == null ? '/login' : '/orderItem/' + userInfo.id">
                <span class="glyphicon glyphicon-shopping-cart redColor"></span>购物车<strong>{{userInfo == null ? 0 : userInfo.productNumber == null ? 0 : userInfo.productNumber }}</strong>件
            </a>
        </span>
      </div>
    </nav>
  </div>
</template>

<script>
import header from "../api/header";
import storage from "../assets/js/storage";

export default {
  data() {
    return {
      userInfo: null, // 用户信息
    }
  },
  name: "myheader",
  mounted() {
    this.getUserInfo()
  },
  created() {
  },
  methods: {
    // 获取用户信息
    getUserInfo() {
      if (storage.getItem("token")){
        // 从后端获取
        header.getUserInfo()
          .then(response => {
            this.userInfo = response.data
            // 存入localStorage中
            storage.setItem("userInfo", this.userInfo)
            storage.setItem("toDataLoad", true, -1)
            // 更新token的ttl
            storage.updateTtl("token")
          })
      }
    },

    // 用户退出
    quit() {
      storage.removeItem("token")
      storage.removeItem("userInfo")
      this.$router.push("/login")
    }

  }
}
</script>

<style scoped>

</style>
