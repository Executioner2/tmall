<template>
  <div>
    <!--这里是支付二维码-->
    <div class="payPage" id="payPage">
      <img src="~assets/img/site/simpleLogo.png"/>
      <div class="payMediate">
        <span style="color: #C40000">订单将在{{payTime}}后取消，请注意支付有效时间</span>
        <div>扫一扫付款（元）</div>
        <div class="paymentAmount">￥{{amountStr}}</div>
        <div>
          <div style="margin: 50px auto; height: 200px; width: 200px" class="qrcode" ref="qrCodeUrl"></div>
        </div>
        <button @click="pay">确认支付</button>
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
      payTime: "2:00:00", // 支付有效时间
    }
  },
  mounted() {
    this.orderId = this.$route.params.orderId
    this.getOrderInfo()
    this.createNative()
  },
  methods: {
    // 剩余支付时间
    leftPayTime() {
      // 拆分创建时间字符串
      let createDate = this.orderInfo.createDate
      let time = createDate.substring(createDate.indexOf(" "), createDate.length)
      let ars = time.split(":")
      // 转化为秒
      let createSecond = parseInt(ars[0])*60*60 + parseInt(ars[1])*60 + parseInt(ars[2])
      // 当前时间转化为秒
      let nowDate = new Date()
      let nowSecond = nowDate.getHours()*60*60 + nowDate.getMinutes()*60 + nowDate.getMilliseconds()
      // 剩余的支付时间（秒）
      let letSecond = 2*60*60 - (nowSecond - createSecond)

      let timer = setInterval(() => {
        --letSecond
        let temp = letSecond
        let formatTime
        let h = parseInt(temp / (60 * 60))
        temp = temp % (60 * 60)
        let m = parseInt(temp / 60)
        let s = temp % 60
        if (m < 10) m = "0" + m
        if (s < 10) s = "0" + s
        formatTime = h + ":" + m + ":" + s
        this.payTime = formatTime
      }, 1000)
      // 关闭计时器
      this.$once('hook:beforeDestroy',()=>{
        clearInterval(timer);
        timer = null;
      })
    },

    // 点击支付
    pay() {
      pay.pay(this.orderId)
        .then(response => {
          if (response.message === '已支付') {
            this.$router.push("/paySuccess/" + this.orderId)
          }
        })
    },

    // 根据订单id获取订单信息
    getOrderInfo() {
      pay.getOrderInfo(this.orderId, 0) // 0 代表未支付
        .then(response => {
          this.orderInfo = response.data
          this.amountStr = moneyFormat.format(this.orderInfo.amount)
          this.leftPayTime()
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
            if (response.message == "支付中") {
              return
            }
            // 跳转到支付成功页面
            this.$router.push("/paySuccess/" + this.orderId)
          })
      }, 3000)
      // 关闭计时器
      this.$once("hook:beforeDestroy", ()=> {
        clearInterval(timer)
        timer = null
      })
    },

  },

}
</script>

<style scoped>

</style>
