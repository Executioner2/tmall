<template>
  <div>
    <div class="simpleSearchBox" id="simpleSearchBox">
      <a href="#">
        <img class="logo" id="simpelLogo" src="~/assets/img/site/simpleLogo.png"/>
      </a>
      <div class="pull-right">
        <div class="searchDiv">
          <input type="text" name="keyword" placeholder="平衡车 原汁机" v-model="keyword" @keydown="keydown">
          <button class="searchButton" @click="search">搜天猫</button>
        </div>
        <div class="searchBelow">
          <span><a href="#">冰箱</a><span>|</span></span>
          <span><a href="#">空调</a><span>|</span></span>
          <span><a href="#">女表</a><span>|</span></span>
          <span><a href="#">男表</a></span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "simpleSearch",
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
      this.$router.push("/search/" + this.keyword)
      this.keyword = null
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
