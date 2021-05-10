<template>
<div>

  <!--这里放搜索栏-->
  <search/>

  <!--这里放排序和价格-->
  <div id="sortAndPrice">
    <img :src="categoryInfo.imageUrl" height="110" width="1010"/>
    <div class="sortAndPriceDiv">
      <table class="sortTable">
        <tr class="grayColumn">
          <td :style="searchObj.sortField == 0 ? 'background-color: #F1EDEC;' : '' "><a :style="searchObj.sortField == 0 ? 'color: #C40000;' : '' " @click="sortBtn(0)">综合<span class="glyphicon glyphicon-arrow-down"></span></a></td>
          <td :style="searchObj.sortField == 1 ? 'background-color: #F1EDEC;' : '' "><a :style="searchObj.sortField == 1 ? 'color: #C40000;' : '' " @click="sortBtn(1)">人气<span class="glyphicon glyphicon-arrow-down"></span></a></td>
          <td :style="searchObj.sortField == 2 ? 'background-color: #F1EDEC;' : '' "><a :style="searchObj.sortField == 2 ? 'color: #C40000;' : '' "  @click="sortBtn(2)">新品<span class="glyphicon glyphicon-arrow-down"></span></a></td>
          <td :style="searchObj.sortField == 3 ? 'background-color: #F1EDEC;' : '' "><a :style="searchObj.sortField == 3 ? 'color: #C40000;' : '' "  @click="sortBtn(3)">销量<span class="glyphicon glyphicon-arrow-down"></span></a></td>
          <td :style="searchObj.sortField == 4 ? 'background-color: #F1EDEC;' : '' "><a :style="searchObj.sortField == 4 ? 'color: #C40000;' : '' "  @click="sortBtn(4)">价格<span class="glyphicon glyphicon-resize-vertical"></span></a></td>
        </tr>
      </table>
      <table class="priceRangeTable">
        <tr>
          <td><input class="price" id="priceLeft" v-model.number="searchObj.lowPrice" @blur="listProductInfo(1)" type="text" placeholder="请输入"></td>
          <td class="grayColumn priceMiddleColumn">-</td>
          <td><input class="price" id="priceRight" v-model.number="searchObj.highPrice" @blur="listProductInfo(1)" type="text" placeholder="请输入"></td>
        </tr>
      </table>
    </div>
  </div>

  <!--这里放产品列表-->
  <div class="productListInClassify" id="productListInClassify">
    <div class="productItem" v-for="(item, index) in list" :key="index">
      <div style="height: 200px; text-align: center">
        <a :href="'/product/' + item.id"><img class="productImage" :src="item.params.imageUrl" height="150"/></a>
      </div>
      <span class="price">￥{{item.promotePrice}}</span>
      <a :href="'/product/' + item.id">{{item.name}}</a>
      <a href="#">天猫专卖</a>
      <div class="itemFooter">
        <span class="monthMake">月成交<span class="monthMakeCount">{{item.monthlySales}}笔</span></span>
        <span class="evaluate">评价<span class="evaluateCount">{{item.params.reviewNumber == null ? 0 : item.params.reviewNumber}}</span></span>
        <span class="wangWang"><a href="#"><img src="~/assets/img/site/wangwang.png" height="16" width="16"/></a></span>
      </div>
    </div>
  </div>

  <!-- 分页 -->
  <div class="block">
    <el-pagination
      :page-size="limit"
      :current-page="current"
      align="center"
      @current-change="listProductInfo"
      layout="prev, pager, next"
      :total="total">
    </el-pagination>
  </div>

</div>
</template>

<script>
import search from "../../layouts/search";
import category from "../../api/category";

export default {
  components: {
    search
  },
  data() {
    return {
      list: [], // 产品集合
      current: 1, // 起始页
      limit: 20, // 每页大小
      total: null, // 总记录数
      searchObj: {} , // 查询对象
      categoryInfo: {}, // 分类对象
      categoryId: null, // 分类id
      lastSortType: null, // 上一次排序方式
    }
  },
  watch: {
    current(val, oldVal) {
      if (val != oldVal) {
        window.scrollTo(0, 0) // 如果页数进行了跳转，就回到页面首部
      }
    }
  },
  created() {
    this.categoryId = this.$route.params.categoryId
    this.searchObj.categoryId = this.categoryId
    this.searchObj.sortType = 1 // 默认降序
    this.searchObj.sortField = 0 // 默认综合排序
    this.getCategoryInfo()
    this.listProductInfo(1)
  },
  methods: {
    // 根据分类id查询分类
    getCategoryInfo() {
      category.getCategoryInfo(this.categoryId)
        .then(response => {
          this.categoryInfo = response.data
        })
    },

    // 根据分类id和查询条件显示商品
    listProductInfo(val) {
      this.current = val
      category.listProductInfo(this.current, this.limit, this.searchObj)
        .then(response => {
          this.list = response.data.records
          this.total = response.data.total
        })
    },

    // 点击排序
    sortBtn(val) {
      this.lastSortType = this.searchObj.sortField // 上一次的排序字段
      this.searchObj.sortField = val // 排序字段
      if (val == 4) { // 如果这次的排序按价格排序
        this.searchObj.sortType += 1
        this.searchObj.sortType = this.searchObj.sortType % 2 // 双向排序
      } else {
        this.searchObj.sortType = 1
      }

      this.listProductInfo(1)
    },

  }
}
</script>

<style scoped>

</style>
