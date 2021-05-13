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
