<template>
  <div v-show="toDataLoad">
    <!-- header -->
    <myheader v-if="$route.fullPath.indexOf('login') != 1"/>
    <!-- 内容区域 -->
    <router-view />
    <!-- footer -->
    <myfooter/>
  </div>
</template>

<script>
import myfooter from "./myfooter";
  import myheader from "./myheader";
  import '~/assets/css/style.css'
import storage from "../assets/js/storage";

export default {
  components: {
    myheader, myfooter
  },
  data() {
    return {
      toDataLoad: false, // 页面用户信息加载完毕，默认没有加载完毕
    }
  },
  watch: {
    $route() {
      window.scrollTo(0, 0)
      if (storage.getItem("token")) { // 如果token存在，用户登录了
        // 那么每次路由就会更新token的ttl
        storage.updateTtl("token")
        if (storage.getItem("userInfo")) {
          storage.updateTtl("userInfo") // 如果userInfo存在还会更新userInfo的ttl
        }
      }
    }
  },
  mounted() {
    const isLoginPage = this.$route.fullPath.indexOf('login') != 1
    const isToDataLoad = storage.getItem("toDataLoad")
    if (isToDataLoad || isLoginPage) {
      this.toDataLoad = true
    }

  }

}
</script>
