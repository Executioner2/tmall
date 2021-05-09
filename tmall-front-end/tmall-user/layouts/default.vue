<template>
  <div>
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
export default {
  components: {
    myheader, myfooter
  },
  watch: {
    $route() {
      window.scrollTo(0, 0)
    }
  }
}
</script>
