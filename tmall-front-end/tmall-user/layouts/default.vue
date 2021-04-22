<template>
  <div>
    <div id="main">
      <!-- header -->
      <myheader/>
      <div class="main-container">
        <el-scrollbar class='page-component__scroll'>
          <!-- 内容区域 -->
          <nuxt/>
        </el-scrollbar>
      </div>
      <!-- footer -->
      <myfooter/>
    </div>
  </div>
</template>

<script>
  import myfooter from "./myfooter";
  import myheader from "./myheader";
  import '~/assets/css/style.css'
export default {
  components: {
    myheader, myfooter
  }
}
</script>
