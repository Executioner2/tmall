<template>
  <div>
    <div id="searchAndNav" class="searchAndNav">
      <div id="searchBoxDiv">
      <a href="#">
        <img class="logo" src="../static/img/site/logo.gif" height="130" width="290" id="logo"/>
      </a>
      <div class="searchRightDiv">
        <input type="text" placeholder="时尚男鞋 太阳镜" v-model="keyword" @keydown="keydown">
        <button class="searchButton" @click="search">搜索</button>
        <div class="searchBelow">
          <span><a href="#">平衡车</a><span>|</span></span>
          <span><a href="#">扫地机器人</a><span>|</span></span>
          <span><a href="#">原汁机</a><span>|</span></span>
          <span><a href="#">冰箱</a></span>
        </div>
      </div>
    </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "search",
  data() {
    return {
      keyword: "", // 商品搜索关键字
    }
  },
  methods: {
    // 搜索
    search() {
      this.keyword = this.keyword.trim()
      if (this.keyword === "" || this.keyword === null) {
        this.$message.warning("请输入商品查询关键字")
        return
      }
      window.location.href = '/search/' + this.keyword
    },

    // 搜索框按键按下
    keydown(key) {
      if (key.code === "Enter") {
        this.search()
      }
    }
  }
}
</script>

<style scoped>

</style>
