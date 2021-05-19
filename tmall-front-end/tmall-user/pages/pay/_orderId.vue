<template>
  <div>
    <!--这里是支付二维码-->
    <div class="payPage" id="payPage">
      <img src="~assets/img/site/simpleLogo.png"/>
      <div class="payMediate">
        <div>   扫一扫付款（元）</div>
        <div class="paymentAmount">￥{{amountStr}}</div>
        <div>
          <div style="margin: 50px auto; height: 200px; width: 200px" class="qrcode" ref="qrCodeUrl"></div>
        </div>
        <button>确认支付</button>
      </div>
    </div>
  </div>
</template>

<script>
import pay from "../../api/pay";
import moneyFormat from "../../assets/js/moneyFormat";
import qrcode from "qrcodejs2";

export default {
  data() {
    return {
      orderId: null, // 订单id
      orderInfo: {}, // 订单信息
      amountStr: "", // 金额
      codeUrl: null, // 二维码地址
      timer: null, // 轮询计时器
    }
  },
  mounted() {
    this.orderId = this.$route.params.orderId
    this.getOrderInfo()
    this.createNative()
  },
  watch: {
  },
  methods: {
    // 根据订单id获取订单信息
    getOrderInfo() {
      pay.getOrderInfo(this.orderId)
        .then(response => {
          this.orderInfo = response.data
          this.amountStr = moneyFormat.format(this.orderInfo.amount)
        })
    },

    // 创建二维码
    createNative() {
      pay.createNative(this.orderId)
        .then(response => {
          this.codeUrl = response.data.codeUrl
          if (this.codeUrl == '' || this.codeUrl == null) {
            this.$message.error("支付错误")
          } else {
            new qrcode(this.$refs.qrCodeUrl, {
              text: this.codeUrl, // 需要转换为二维码的内容
            })
            // 开启轮询
            this.polling()
          }
        })
    },

    // 轮询
    polling() {
      let timer = setInterval(() => {
        pay.polling(this.orderId)
          .then(response => {
            if (response.data.message == "支付中") {
              return
            }
            // 跳转到支付成功页面
            clearInterval(timer)
            this.$router.push("/paySuccess/" + this.orderId)
            console.log("计时器已关闭")
          })
      }, 3000)
    },

  },

}
</script>

<style scoped>

</style>
