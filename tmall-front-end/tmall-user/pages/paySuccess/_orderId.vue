<template>
  <div>
    <simple-search/>

    <!--这里是支付成功信息-->
    <div class="paymentSuccessInformation" id="paymentSuccessInformation">
      <div class="title">
        <img src="~assets/img/site/paySuccess.png" height="33" width="36"/>
        &nbsp;&nbsp;您已成功付款
      </div>
      <div class="payInformationContent">
        <ul>
          <li>
            收货地址：
            <span>{{this.orderInfo.address}}</span>
          </li>
          <li>
            实付款：
            <span class="money">￥{{amountStr}}</span>
          </li>
          <li>
            预计<span>08月08日</span>送达
          </li>
        </ul>
        <div>您可以
          <router-link to="/order">查看已买到的宝贝</router-link>
          <router-link to="/order">查看交易详情</router-link>
        </div>
      </div>
      <div class="payedSeperateLine"></div>
      <div class="payFooter">
        <img src="~assets/img/site/warning.png" height="20" width="21"/>
        <span>
            <strong>安全提醒：</strong>
            下单后，<span class="redSpan">用QQ给您发送链接办理退款的都是骗子！</span>
            天猫不存在系统升级，订单异常等问题，谨防假冒客服电话诈骗！
        </span>
      </div>
    </div>

  </div>
</template>

<script>
import SimpleSearch from "../../layouts/simpleSearch";
import pay from "../../api/pay";
import moneyFormat from "../../assets/js/moneyFormat";

export default {
  components: {SimpleSearch},
  data() {
    return {
      orderInfo: {}, // 订单信息,
      orderId: null, // 订单id
      amountStr: "", // 金额
    }
  },
  mounted() {
    this.orderId = this.$route.params.orderId
    this.getOrderInfo()
  },
  methods: {
    // 根据订单id获取订单信息
    getOrderInfo() {
      pay.getOrderInfo(this.orderId, 1) // 1 代表支付成功，等待发货
        .then(response => {
          this.orderInfo = response.data
          console.log(this.orderInfo)
          this.amountStr = moneyFormat.format(this.orderInfo.amount)
        })
    },

  }

}
</script>

<style scoped>

</style>
