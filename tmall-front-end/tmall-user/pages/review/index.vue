<template>
  <div v-if="productInfo != null">
    <simple-search/>

    <!--这里放评价页面上-->
    <div id="theEvaluationUp" class="theEvaluationUp" >
      <div id="productItem">
        <div class="leftProductImage">
          <img id="productImage" class="productImage" :src="productInfo.params.imageUrl"/>
        </div>
        <div class="rightProductInfo">
          <div id="productTitle" class="productTitle">
            {{productInfo.name}}
          </div>
          <table class="tableInfo">
            <tr>
              <td>价格:</td>
              <td><span id="price" class="price formatMoney">{{moneyFormat(productInfo.promotePrice)}}</span>元</td>
            </tr>
            <tr>
              <td>配送:</td>
              <td>快递：0.00</td>
            </tr>
            <tr>
              <td>月销量:</td>
              <td><span id="daySalesVolume" class="daySalesVolume">{{productInfo.monthlySales}}</span>件</td>
            </tr>
          </table>
          <div class="hintDiv">
            <table>
              <tr>
                <td class="hintImage"><img src="~assets/img/site/reviewLight.png" height="38" width="23"/></td>
                <td class="hintText">现在查看的是 您所购买商品的信息 于<span id="createDate">{{formatDate(createDate)}}</span>下单购买了此商品</td>
              </tr>
            </table>
          </div>
        </div>
      </div>
    </div>

    <!--这里放评价页面下-->
    <div id="theEvaluationDown" class="theEvaluationDown">
      <div class="theCumulativeAssessment">
        <div class="contentDiv">
          <span>累计评价</span>
          <span id="reviews">{{productInfo.params.reviewNumber}}</span>
        </div>
        <div class="backgroundDiv"></div>
      </div>
      <!-- 评价 -->
      <div v-if="orderItem.isReview == 0" id="evaluateDiv${orderItem.product.id}" class="evaluateDiv">
        <div class="evaluateDivTop">
          <div class="evaluateHint">
            其他买家，需要你的建议哦！
          </div>
          <table>
            <tr>
              <td>评价商品</td>
              <td><textarea id="reviewsText" name="content" v-model="reviewObj.content"></textarea></td>
            </tr>
          </table>
        </div>
        <button class="submitBtn" @click="submitReview">提交评价</button>
      </div>

      <!-- 显示商品评价 -->
      <div v-else>
        <div class="reviewsItem" v-for="(item, index) in reviews" :key="index">
          <span class="reviewsDate">{{item.createTime}}</span>
          <span class="context">{{item.content}}</span>
          <span class="niMing pull-right">（匿名）</span>
          <span class="userName pull-right">{{item.anonymity}}</span>
        </div>
      </div>

    </div>

  </div>
</template>

<script>
import SimpleSearch from "../../layouts/simpleSearch";
import review from "../../api/review";
import moneyFormat from "../../assets/js/moneyFormat";
import productInfo from "../../api/productInfo";
export default {
  components: {SimpleSearch},
  data() {
    return {
      createDate: null, // 订单创建日期
      orderItemId: null, // 订单项id
      orderItem: {}, // 订单项
      productInfo: null, // 商品
      reviewObj: {}, // 评价对象
      reviews: [], // 商品评价
      current: 1, // 评价起始页
      limit: 15, // 每页数据量
      total: null, // 总页数
    }
  },
  created() {
    this.createDate = this.$route.query.createDate
    this.orderItemId = this.$route.query.orderItemId
    this.getInfo()
    this.formatDate(this.createDate)

  },
  methods: {
    // 根据订单项id查询订单项项和商品
    getInfo() {
      review.getOrderItemAndProductInfo(this.orderItemId)
        .then(response => {
          this.orderItem = response.data.orderItem
          this.productInfo = response.data.productInfo
          if (this.orderItem.isReview == 1) { // 已评价
            // 显示商品评价
            this.listReview(1)
          }
        })
    },

    // 显示商品评价
    listReview(val) {
      this.current = val
      productInfo.listReviewByProductId(this.current, this.limit, this.productInfo.id)
        .then(response => {
          this.reviews = response.data.records
          this.total = response.data.total
        })
    },

    // 提交评价
    submitReview() {
      this.reviewObj.productId = this.productInfo.id
      this.reviewObj.orderItemId = this.orderItemId
      review.submitReview(this.reviewObj)
        .then(response => {
          ++this.productInfo.params.reviewNumber
          this.orderItem.isReview = 1
          // 显示商品评价
          this.listReview(1)
        })
    },

    // 格式化货币
    moneyFormat(data) {
      return moneyFormat.format(data)
    },

    // 格式化日期
    formatDate(date) {
      let gs = ["年", "月", "日"]
      date = date.toString()
      date = date.substring(0, date.indexOf(" "))
      let temp = date.split("-")
      date = ""
      for (let i = 0; i < temp.length; i++) {
        date += temp[i] + gs[i]
      }
      return date
    }
  },

}
</script>

<style scoped>
.productReviewLink{
  position: relative;
  color: #777777;
  text-decoration-line: none;
}

.productReviewLink:hover{
  color: #777777;
  text-decoration-line: none;
}

#nextProductReviewPage{
  float: right;
  margin-right: 210px;
  top: -200px;
  font-size: 30px;
  display: none;
}
</style>
