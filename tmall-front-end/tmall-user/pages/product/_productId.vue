<template>
  <div>

    <!--这里放简单搜索栏-->
    <simple-search/>

    <!--这里放titleImage-->
    <div id="titleImage" class="titleImage">
      <img :src="categoryInfo.imageUrl" height="110" width="1010"/>
    </div>

    <!--这里是把产品图片和基本信息装起来的div-->
    <div class="productImageAndProductBasicInformationDiv" id="productImageAndProductBasicInformationDiv">
      <!--这里放产品图片-->
      <div id="productImage">
        <img id="showImage" :src="showImage.url"/>
        <div class="productImageList">
          <img v-for="(item, index) in productImage.thumbnail" @mouseenter="showImage = item" :key="index" :src="item.url"/>
        </div>
      </div>

      <!--这里放基本信息-->
      <script>
        $(function () {
          $("#productNumber").keyup(procuctNumberKeyUp);
          $("#upKey").click(numberAdd);
          $("#downKey").click(numberSub);
        });

        function procuctNumberKeyUp(){
          var number = $("#productNumber").val();
          var inventory = $("#productBasicInformation .inventory").text();
          if(isNaN(parseInt(number)) || parseInt(number) <= 0){
            $("#productNumber").val(1);
          }
          if(isNaN(number)){
            $("#productNumber").val(parseInt(number));
          }
          if(parseInt(number) > parseInt(inventory)){
            $("#productNumber").val(inventory);
          }
        }

        function numberAdd(){
          var number = $("#productNumber").val();
          var inventory = $("#productBasicInformation .inventory").text();
          if(parseInt(number) < parseInt(inventory)){
            $("#productNumber").val(parseInt(number)+1);
          }
        }

        function numberSub(){
          var number = $("#productNumber").val();
          if(parseInt(number) > 1){
            $("#productNumber").val(parseInt(number)-1);
          }
        }
      </script>
      <div id="productBasicInformation" class="productBasicInformation">
        <div class="biTitle">
          {{productInfo.name}}
        </div>
        <div class="sketchDiv">
          <span>{{productInfo.subTitle}}</span>
        </div>
        <div class="juHuaSuanCountDown">
          <span>聚划算</span>
          <span>此商品即将参加聚划算，<span class="countDow">1天19小时</span>后开始</span>
        </div>
        <div class="priceDiv">
          <div>
            <img src="~/assets/img/site/gouwujuan.png"/>
            <span>全天猫实物商品通用</span>
          </div>
          <div>
            <span>价格</span>
            <span><del>￥{{productInfo.orignalPrice}}</del></span>
          </div>
          <div>
            <span>促销价</span>
            <span>￥</span>
            <span>{{productInfo.promotePrice}}</span>
          </div>
        </div>
        <div class="salesAndReviews">
          <span>销量 <span class="sales">{{productInfo.salesVolume}}</span></span>
          <span>累计评价 <span class="reviews">{{countReview}}</span></span>
        </div>
        <div class="numberDiv">
          <span>数量</span>
          <span><input type="text" value="1" name="number" id="productNumber"></span>
          <span class="arrow">
                <a href="#nowhere" id="upKey">
                    <span class="updown">
                        <img src="~/assets/img/site/increase.png" height="3" width="7"/>
                    </span>
                </a>
                <span class="updownMiddle"></span>
                <a href="#nowhere" id="downKey">
                    <span class="updown">
                        <img src="~/assets/img/site/decrease.png" height="3" width="7"/>
                    </span>
                </a>
            </span>

          <span>件 库存<span class="inventory">{{productInfo.stock}}</span>件</span>
        </div>
        <div class="commitment">
          <span>服务承诺</span>
          <a href="#">正品保证</a>
          <a href="#">极速退款</a>
          <a href="#">赠运费险</a>
          <a href="#">七天无理由退换</a>
        </div>
        <div class="buyAndJoinShopping">
          <button id="buy">立即购买</button>
          <button id="joinShopping"><span class="glyphicon glyphicon-shopping-cart"></span>加入购物车</button>
        </div>
      </div>
    </div>

    <!--这里放产品详情-->
    <div id="productParticulars" v-show="reviewShow == false" class="productParticulars">
      <div class="productDetailTopPart">
        <a href="#nowhere" @click="reviewShow = false" class="productDetailTopPartSelectedLink">商品详情</a>
        <a href="#nowhere" @click="selectReview"  class="productDetailTopReviewLink">累计评价 <span class="evaluateNumber">{{countReview}}</span></a>
      </div>
      <div class="productParameterDiv">
        <div class="titleDiv">产品参数：</div>
        <div class="productParamterList">
          <span v-for="(item, index) in propertyAndValueVoList" :key="index">{{item.propertyName}}: {{item.propertyValue}}</span>
        </div>
      </div>
      <div class="productDescribeImageDiv">
        <img v-for="(item, index) in productImage.details" :key="index" :src="item.url"/>
      </div>
    </div>

    <!--这里放积累评价-->
    <div id="productCumulativeAssessment" v-show="reviewShow == true" class="productCumulativeAssessment">
      <div class="productDetailTopPart">
        <a href="#nowhere" @click="reviewShow = false" class="productDetailTopPartSelectedLink">商品详情</a>
        <a href="#nowhere" @click="selectReview" class="productDetailTopReviewLink">累计评价 <span class="evaluateNumber">{{countReview}}</span></a>
      </div>
      <div class="cumulativeAssessmentDiv">
        <div class="productEvaluateItem" v-for="(item, index) in reviews" :key="index">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              {{item.content}}
            </div>
            <div class="productEvaluateItemDate">
              {{item.createTime}}
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            {{item.anonymity}}
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
      </div>
      <!-- 分页 -->
      <div class="block" style="margin-top: 50px">
        <el-pagination
          :page-size="limit"
          :current-page="current"
          align="center"
          @current-change="listReviewByProductId"
          layout="prev, pager, next"
          :total="total">
        </el-pagination>
      </div>
    </div>

  </div>
</template>

<script>
import SimpleSearch from "../../layouts/simpleSearch";
import category from "../../api/category";
import productInfo from "../../api/productInfo";

export default {
  name: "_productId",
  components: {SimpleSearch},
  data() {
    return {
      categoryInfo: {}, // 分类对象
      productId: null, // 商品id
      productInfo: {}, // 商品信息
      countReview: 0, // 商品评价数
      propertyAndValueVoList: [], // 商品属性和属性值
      productImage: {thumbnail: [], details: []}, //商品图片
      showImage: {}, // 展示图
      reviewShow: false, // 评价框是否显示
      current: 1, // 商品评价起始页
      limit: 15, // 每页显示评价数量
      total: null, // 总页数
      reviews: [], // 评价集合
      firstReviewLoad: true, // 第一次加载评价
    }
  },
  created() {
    this.productId = this.$route.params.productId
    this.getProductInfoById()
  },
  methods:{
    // 根据分类id查询分类
    getCategoryInfo() {
      category.getCategoryInfo(this.productInfo.categoryId)
        .then(response => {
          this.categoryInfo = response.data
        })
    },

    // 根据商品id查询出商品信息
    getProductInfoById() {
      productInfo.getProductInfoById(this.productId)
        .then(response => {
          this.productInfo = response.data
          this.countReview = response.data.params.countReview
          this.productImage = response.data.params.productImage
          this.propertyAndValueVoList = response.data.params.propertyAndValueVoList
          this.showImage = this.productImage.thumbnail[0]
          this.getCategoryInfo() // 查询分类
        })
    },

    // 查询商品评价
    selectReview() {
      if (this.reviewShow) { // 如果当前已在评价div则不查询直接返回
        return
      }
      // 如果是第一次加载评价就向服务器发送加载请求，若不是则直接显示评价div
      if (this.firstReviewLoad) {
        // 查询商品评价
        this.listReviewByProductId(1)
        this.firstReviewLoad = false
      }
      this.reviewShow = true
    },

    // 商品评价查询
    listReviewByProductId(val) {
      this.current = val
      productInfo.listReviewByProductId(this.current, this.limit, this.productId)
        .then(response => {
          this.reviews = response.data.records
          this.total = response.data.total
        })
    }

  }
}
</script>

<style scoped>

</style>
