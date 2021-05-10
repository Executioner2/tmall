<template>
  <div>
    <!--这里放nav-->
    <nav class="top">
      <div class="navTopDiv">
        <router-link to="/">
          <span class="glyphicon glyphicon-home redColor"></span>天猫首页
        </router-link>
        <span>喵，欢迎来天猫</span>
        <router-link :to="userInfo == null ? '/login' : '/account_set'">{{userInfo == null ? "请登录" : userInfo.name}}</router-link>
        <a v-if="userInfo" @click="quit" href="javascript:void(0)">退出</a>
        <router-link v-if="!userInfo" to="/regist">免费注册</router-link>
        <span class="pull-right">
            <a :href="userInfo == null ? '/login' : '/order/' + userInfo.id">我的订单</a>
            <a :href="userInfo == null ? '#' : '/orderItem/' + userInfo.id">
                <span class="glyphicon glyphicon-shopping-cart redColor"></span>购物车<strong>{{userInfo == null ? 0 : userInfo.productNumber == null ? 0 : userInfo.productNumber }}</strong>件
            </a>
        </span>
      </div>
    </nav>
  </div>
</template>

<script>
import header from "../api/header";
import cookie from "js-cookie"
import storage from "../assets/js/storage";


export default {
  data() {
    return {
      userInfo: null, // 用户信息
    }
  },
  name: "myheader",
  mounted() {
    // if (storage.getItem("token")){
    //   if (storage.getItem("userInfo")) {
    //     // 如果可以从local Storage中取出userInfo那么就刷新token和userInfo的ttl
    //     this.userInfo = storage.getItem("userInfo")
    //     storage.updateTtl("token", 30*60*1000)
    //     storage.updateTtl("userInfo", 30*60*1000)
    //   } else {
    //     this.getUserInfo()
    //   }
    // }
    this.getUserInfo()

  },
  created() {

  },
  methods: {
    // 获取用户信息
    getUserInfo() {
      if (storage.getItem("token")){
        // 取出赋值
        this.userInfo = storage.getItem("userInfo")
        // this.userInfo = JSON.parse(cookie.get("userInfo"))
        if (this.userInfo) {
          // 如果有就更新生存时间并返回
          storage.updateTtl("token", 30*60*1000)
          storage.updateTtl("userInfo", 30*60*1000)
          return
        } else {
          // 如果没有就从后端获取
          header.getUserInfo()
            .then(response => {
              this.userInfo = response.data
              // // 存入localStorage中
              storage.setItem("userInfo", this.userInfo, 30*60*1000)
              // 存入cookie
              // cookie.set("userInfo", this.userInfo)
            })
        }
      }
    },

    // 用户退出
    quit() {
      storage.removeItem("token")
      storage.removeItem("userInfo")
      window.location.href = "/login"
    }

  }
}
</script>

<style scoped>

</style>
