<template>
  <div>
    <!--这里放nav-->
    <nav class="top">
      <div class="navTopDiv">
        <a href="/">
          <span class="glyphicon glyphicon-home redColor"></span>天猫首页
        </a>
        <span>喵，欢迎来天猫</span>
        <a :href="userInfo == null ? '/login' : '#'">{{userInfo == null ? "请登录" : userInfo.name}}</a>
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

export default {
  data() {
    return {
      userInfo: null, // 用户信息
    }
  },
  name: "myheader",
  mounted() {
    console.log("token：" + localStorage.getItem("token"))
    if (localStorage.getItem("token") != null){
      this.getUserInfo()
    }
  },
  created() {

  },
  methods: {
    // 获取用户信息
    getUserInfo() {
      console.log("获取用户信息")
      header.getUserInfo()
        .then(response => {
          this.userInfo = response.data
        })
    }


  }
}
</script>

<style scoped>

</style>
