<template>
  <div>
    <simple-search/>

    <!--这里放确认收货页面上-->
    <div class="confirmReceiptUp" id="confirmReceiptUp">
      <img id="scheduleImage" src="~assets/img/site/comformPayFlow.png"/>
      <div class="dateTime">
        <span>{{orderInfo.createDate}}</span>
        <span>{{orderInfo.payDate}}</span>
        <span>{{orderInfo.deliveryDate}}</span>
      </div>
      <div class="hint1">我已收到货，同意支付宝付款</div>
      <table>
        <tr>
          <td>订单信息</td>
        </tr>
        <tr class="tableTitle">
          <td colspan="2">宝贝</td>
          <td>单价</td>
          <td>数量</td>
          <td>商品总价</td>
          <td>运费</td>
        </tr>
        <tr class="tableBody" v-for="(item, index) in orderItems" :key="index">
          <td><img class="productImage" :src="item.params.productInfo.params.imageUrl"/></td>
          <td><router-link :to="'/product/' + item.params.productInfo.id">{{item.params.productInfo.name}}</router-link></td>
          <td class="unitPrice">￥{{moneyFormat(item.params.productInfo.promotePrice)}}</td>
          <td class="number">{{item.number}}</td>
          <td class="totalPrices">￥{{amountStr(item)}}</td>
          <td>快递： 0.00</td>
        </tr>
      </table>
      <div class="actualPayment">
        <span>实付款：<span class="paymentAmount">￥{{moneyFormat(orderInfo.amount)}}</span></span>
        <div style="clear: both"></div>
      </div>
    </div>

    <!--这里放确认收货页面下-->
    <div class="confirmReceiptDown" id="confirmReceiptDown">
      <table class="gayTable">
        <tr>
          <td>订单编号:</td>
          <td>
            <span>{{orderInfo.outTradeNo}}</span>
            <img src="~assets/img/site/confirmOrderTmall.png" height="18" width="23"/>
          </td>
        </tr>
        <tr>
          <td>卖家昵称:</td>
          <td>
            <span>天猫商铺</span>
            <div class="wangwangGif">
            </div>
          </td>
        </tr>
        <tr>
          <td>收货信息:</td>
          <td>
            <span>{{orderInfo.address}}，</span>
            <span>{{orderInfo.receiver}}，</span>
            <span>{{orderInfo.mobile}}，</span>
            <span>{{orderInfo.post == "" ? "000000" : orderInfo.post}}</span>
          </td>
        </tr>
        <tr>
          <td>成交时间:</td>
          <td>{{orderInfo.createDate}}</td>
        </tr>
      </table>
      <div class="hintDiv">
        <div class="hint">请收到货后，再确认收货！否则您可能钱货两空！</div>
        <button @click="confirmReceipt">确认收货</button>
      </div>
    </div>

  </div>
</template>

<script>
import SimpleSearch from "../../layouts/simpleSearch";
import confirmReceipt from "../../api/confirmReceipt";
import moneyFormat from "../../assets/js/moneyFormat";

export default {
  components: {SimpleSearch},
  data() {
    return {
      orderInfo: {}, // 订单信息
      orderId: null, // 订单id
      orderItems: [], // 订单项
    }
  },
  created() {
    this.orderId = this.$route.params.orderId
    this.getOrderInfo()
  },
  methods: {
    // 获得订单信息
    getOrderInfo() {
      confirmReceipt.getOrderInfoDetails(this.orderId, 2) // 2代表等待买家确认收货
        .then(response => {
          this.orderInfo = response.data
          this.orderItems = this.orderInfo.params.orderItems
        })
    },

    // 货币格式化
    moneyFormat(data) {
      return moneyFormat.format(data)
    },

    // 订单项总金额
    amountStr(item) {
      let promotePrice = item.params.productInfo.promotePrice
      let amount = (parseFloat(promotePrice) * parseFloat(item.number)).toFixed(2)
      return this.moneyFormat(amount)
    },

    // 确认收货
    confirmReceipt() {
      confirmReceipt.confirmReceipt(this.orderId)
        .then(() => {
          this.$router.push("/tradeSuccessfully")
        })
    },

  },
}
</script>
