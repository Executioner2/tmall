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
          <td><a href="#">综合<span class="glyphicon glyphicon-arrow-down"></span></a></td>
          <td><a href="#">人气<span class="glyphicon glyphicon-arrow-down"></span></a></td>
          <td><a href="#">新品<span class="glyphicon glyphicon-arrow-down"></span></a></td>
          <td><a href="#">销量<span class="glyphicon glyphicon-arrow-down"></span></a></td>
          <td><a href="#">价格<span class="glyphicon glyphicon-resize-vertical"></span></a></td>
        </tr>
      </table>
      <table class="priceRangeTable">
        <tr>
          <td><input class="price" id="priceLeft" type="text" placeholder="请输入"></td>
          <td class="grayColumn priceMiddleColumn">-</td>
          <td><input class="price" id="priceRight" type="text" placeholder="请输入"></td>
        </tr>
      </table>
    </div>
  </div>

  <!--这里放产品列表-->
  <script>
    $(function () {
      $("#priceRight").blur(priceSectionFun);
      $("#priceLeft").blur(priceSectionFun);
      $("#priceLeft").keyup(delCharFun);
      $("#priceRight").keyup(delCharFun);
    });

    //删除除数字以外的字符
    function delCharFun(v) {
      var value = $(this).val();
      if(isNaN(parseFloat(value))){
        $(this).val(0);
        return ;
      }
      $(this).val(parseFloat(value));
    }

    function priceSectionFun() {
      var products = $(".productItem");
      var priceLeft = $("#priceLeft").val();
      var priceRight = $("#priceRight").val();
      if(priceLeft == ""){
        priceLeft = 0;
      }
      if(priceRight == "0" && priceLeft == "0"){
        products.each(function (){
          $(this).css("display","inline-block");
        })
        return ;
      }
      products.each(function (){
        var price = parseFloat($(this).attr("price"));
        if(price < parseFloat(priceLeft) || price > parseFloat(priceRight)){
          $(this).css("display","none");
        }else{
          $(this).css("display","inline-block");
        }
      })
    }
  </script>
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
        <span class="evaluate">评价<span class="evaluateCount">{{item.reviewNumber == null ? 0 : item.reviewNumber}}</span></span>
        <span class="wangWang"><a href="#"><img src="~/assets/img/site/wangwang.png" height="16" width="16"/></a></span>
      </div>
    </div>
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
    }
  },
  created() {
    this.categoryId = this.$route.params.categoryId
    this.searchObj.categoryId = this.categoryId
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
    }

  }
}
</script>

<style scoped>

</style>
