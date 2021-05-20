<template>
  <div>
    <!--这里放搜索栏-->
    <search/>

    <!--这里放查询结果商品列表-->
    <div class="queryResultProductList" id="queryResultProductList">
      <div class="productItem" v-for="(item, index) in list" :key="index">
        <router-link :to="'/product/' + item.id"><img class="productImage" :src="item.params.imageUrl"/></router-link>
        <span class="price">￥{{item.promotePrice}}</span>
        <span><router-link :to="'/product/' + item.id">{{item.name}}</router-link></span>
        <span><a href="javascript:void(0)">天猫专卖</a></span>
        <div>
          <span>月成交 <span class="monthTurnover">{{item.monthlySales}} 笔</span></span>
          <span>评价<span class="evaluateNumber">  {{item.params.reviewNumber}}</span></span>
          <span><img src="~assets/img/site/wangwang.png" height="16" width="16"/></span>
        </div>
      </div>
    </div>

    <!-- 没有搜索到商品 -->
    <div v-if="list.length == 0" id="search_null" align="center" style="margin-top: 50px; margin-bottom: 50px; color: #757575">
        <h1>亲，没有查询到符合条件的商品呢！！</h1>
    </div>

    <!-- 分页 -->
    <div class="block" style="margin-top: 50px">
      <el-pagination
        :page-size="limit"
        :current-page="current"
        align="center"
        @current-change="searchProductInfo"
        layout="prev, pager, next"
        :total="total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
import Search from "../../layouts/search";
import searchApi from "../../api/search"

export default {
  components: {Search},
  data() {
    return {
      keyword: null, // 搜索的关键字
      list: [], // 商品集合
      current: 1, // 起始页
      limit: 20, // 每页大小
      total: null, // 总记录数
    }
  },
  created() {
    this.keyword = this.$route.params.keyword
    // 搜索商品
    this.searchProductInfo(1)
  },
  watch: {
    $route() {
      this.keyword = this.$route.params.keyword
      // 搜索商品
      this.searchProductInfo(1)
    },
    current(val, oldVal) {
      if (val != oldVal) {
        window.scrollTo(0, 0) // 如果页数进行了跳转，就回到页面首部
      }
    }
  },
  methods: {
    // 向后端发送搜索请求
    searchProductInfo(val) {
      this.current = val
      searchApi.searchProductInfo(this.current, this.limit, this.keyword)
        .then(response => {
          this.list = response.data.records
          this.total = response.data.total
        })
    }
  }

}
</script>

<style scoped>

</style>
