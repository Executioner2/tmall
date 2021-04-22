<template>
<div>

  <!--这里放搜索栏-->
  <search/>

  <!--这里放排序和价格-->
  <div id="sortAndPrice">
    <img src="~/assets/img/classify/72.jpg" height="110" width="1010"/>
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
    <div class="productItem" price="799.2">
      <a href="#"><img class="productImage" src="~/assets/img/product/男士手拿包/7058.jpg" height="100"/></a>
      <span class="price">￥799.20</span>
      <a href="#">MAXFEEL休闲男士手包真皮手拿包大容量信封包手抓包夹包软韩版潮</a>
      <a href="#">天猫专卖</a>
      <div class="itemFooter">
        <span class="monthMake">月成交<span class="monthMakeCount">16笔</span></span>
        <span class="evaluate">评价<span class="evaluateCount">14</span></span>
        <span class="wangWang"><a href="#"><img src="~/assets/img/site/wangwang.png" height="16" width="16"/></a></span>
      </div>
    </div>
    <div class="productItem" price="511.2">
      <a href="#"><img class="productImage" src="~/assets/img/product/男士手拿包/7047.jpg" height="100"/></a>
      <span class="price">￥511.20</span>
      <a href="#">宾度 男士手包真皮大容量手拿包牛皮个性潮男包手抓包软皮信封包</a>
      <a href="#">天猫专卖</a>
      <div class="itemFooter">
        <span class="monthMake">月成交<span class="monthMakeCount">49笔</span></span>
        <span class="evaluate">评价<span class="evaluateCount">18</span></span>
        <span class="wangWang"><a href="#"><img src="~/assets/img/site/wangwang.png" height="16" width="16"/></a></span>
      </div>
    </div>
    <div class="productItem" price="488.2">
      <a href="#"><img class="productImage" src="~/assets/img/product/男士手拿包/7036.jpg" height="100"/></a>
      <span class="price">￥488.20</span>
      <a href="#">唯美诺新款男士手包男包真皮大容量小羊皮手拿包信封包软皮夹包潮</a>
      <a href="#">天猫专卖</a>
      <div class="itemFooter">
        <span class="monthMake">月成交<span class="monthMakeCount">34笔</span></span>
        <span class="evaluate">评价<span class="evaluateCount">16</span></span>
        <span class="wangWang"><a href="#"><img src="~/assets/img/site/wangwang.png" height="16" width="16"/></a></span>
      </div>
    </div>
    <div class="productItem" price="411.6">
      <a href="#"><img class="productImage" src="~/assets/img/product/男士手拿包/7025.jpg" height="100"/></a>
      <span class="price">￥411.60</span>
      <a href="#">英伦邦纹男士手包牛皮大容量真皮手拿包手抓包双拉链商务正品软皮</a>
      <a href="#">天猫专卖</a>
      <div class="itemFooter">
        <span class="monthMake">月成交<span class="monthMakeCount">25笔</span></span>
        <span class="evaluate">评价<span class="evaluateCount">19</span></span>
        <span class="wangWang"><a href="#"><img src="~/assets/img/site/wangwang.png" height="16" width="16"/></a></span>
      </div>
    </div>
    <div class="productItem" price="157.25">
      <a href="#"><img class="productImage" src="~/assets/img/product/男士手拿包/7047.jpg" height="100"/></a>
      <span class="price">￥157.25</span>
      <a href="#">劳迪莱斯男士手包休闲手拿包牛皮大容量钱包男包软面小包包手抓包</a>
      <a href="#">天猫专卖</a>
      <div class="itemFooter">
        <span class="monthMake">月成交<span class="monthMakeCount">17笔</span></span>
        <span class="evaluate">评价<span class="evaluateCount">16</span></span>
        <span class="wangWang"><a href="#"><img src="~/assets/img/site/wangwang.png" height="16" width="16"/></a></span>
      </div>
    </div>
    <div class="productItem" price="268.2">
      <a href="#"><img class="productImage" src="~/assets/img/product/男士手拿包/7003.jpg" height="100"/></a>
      <span class="price">￥268.20</span>
      <a href="#">帕朗尼男士手拿包真皮手包商务休闲头层牛皮软牛皮大容量休闲钱包</a>
      <a href="#">天猫专卖</a>
      <div class="itemFooter">
        <span class="monthMake">月成交<span class="monthMakeCount">5笔</span></span>
        <span class="evaluate">评价<span class="evaluateCount">19</span></span>
        <span class="wangWang"><a href="#"><img src="~/assets/img/site/wangwang.png" height="16" width="16"/></a></span>
      </div>
    </div>
    <div class="productItem" price="233.4">
      <a href="#"><img class="productImage" src="~/assets/img/product/男士手拿包/6992.jpg" height="100"/></a>
      <span class="price">￥233.40</span>
      <a href="#">编织手包手拿包男信封大容量手抓包真皮韩版潮商务休闲牛皮男包</a>
      <a href="#">天猫专卖</a>
      <div class="itemFooter">
        <span class="monthMake">月成交<span class="monthMakeCount">8笔</span></span>
        <span class="evaluate">评价<span class="evaluateCount">19</span></span>
        <span class="wangWang"><a href="#"><img src="~/assets/img/site/wangwang.png" height="16" width="16"/></a></span>
      </div>
    </div>
    <div class="productItem" price="952">
      <a href="#"><img class="productImage" src="~/assets/img/product/男士手拿包/6981.jpg" height="100"/></a>
      <span class="price">￥952.00</span>
      <a href="#">犟牛男士手包真皮手拿包头层牛皮商务大容量手抓包软皮夹包信封包</a>
      <a href="#">天猫专卖</a>
      <div class="itemFooter">
        <span class="monthMake">月成交<span class="monthMakeCount">20笔</span></span>
        <span class="evaluate">评价<span class="evaluateCount">16</span></span>
        <span class="wangWang"><a href="#"><img src="~/assets/img/site/wangwang.png" height="16" width="16"/></a></span>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import search from "../../layouts/search";
export default {
  components: {
    search
  },
  name: "_categoryId"
}
</script>

<style scoped>

</style>
