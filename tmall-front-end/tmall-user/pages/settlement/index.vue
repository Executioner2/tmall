<template>
  <div>
    <!--这里放头部和收货地址-->
    <div class="headAndPlaceOfReceipt" id="headAndPlaceOfReceipt">
      <div class="title">
        <img class="simpleLogo pull-left" src="~assets/img/site/simpleLogo.png" height="27" width="190"/>
        <img class="buyflow pull-right" src="~assets/img/site/buyflow.png" height="72" width="597"/>
      </div>
      <div style="clear: both"></div>  <!--清除浮动避免上面的容器坍塌-->
      <table class="fillInInformation">
        <tr class="tableTitle">
          <td colspan="2" class="fillInTitle">输入收货地址</td>
        </tr>
        <tr class="tableBody">
          <td>
            <span>详细地址</span><span class="important">*</span>
          </td>
          <td>
            <textarea v-model="order.address" cols="60" rows="2" placeholder="建议您如实填写详细收货地址，例如接到名称，门牌号码，楼层和房间号等信息"></textarea>
          </td>
        </tr>
        <tr class="tableBody">
          <td>邮政编码</td>
          <td><input type="text" v-model="order.post" placeholder="如果您不清楚邮递区号，请填写000000"></td>
        </tr>
        <tr class="tableBody">
          <td><span>收货人姓名</span><span class="important">*</span></td>
          <td><input type="text" v-model="order.receiver" placeholder="长度不超过25个字符"></td>
        </tr>
        <tr class="tableBody">
          <td><span>手机号码</span><span class="important">*</span></td>
          <td><input type="text" v-model="order.mobile" placeholder="请输入11位手机号码"></td>
        </tr>
      </table>
    </div>

    <!--这里放确认订单信息-->
    <div class="confirmOrderInformation" id="confirmOrderInformation">
      <div class="confirmOrderHeadline">确认订单信息</div>
      <table  class="confirmOrderItem">
        <tr class="confirmOrderTitle">
          <td colspan="2px">
            <img class="tamllbuyPng" src="~assets/img/site/tmallbuy.png"/>
            <a href="#" class="storeName">店铺：天猫店铺</a>
            <a href="#"><div class="wangwangGif"></div></a>
          </td>
          <td>单价</td>
          <td>数量</td>
          <td>小计</td>
          <td>配送方式</td>
        </tr>
        <tr class="rowborder">
          <td colspan="2"></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
        </tr>
        <tr class="productItem" v-for="(item, index) in list" :key="index">
          <td>
            <img class="productImage" :src="item.params.productInfo.params.imageUrl"/>
          </td>
          <td>
            <div>
              <a href="#">{{item.params.productInfo.name}}</a>
              <div>
                <img src="~assets/img/site/creditcard.png" height="11" width="16"/>
                <img src="~assets/img/site/7day.png" height="16" width="16"/>
                <img src="~assets/img/site/promise.png" height="16" width="16"/>
              </div>
            </div>
          </td>
          <td class="price">
            ￥{{item.priceStr}}
          </td>
          <td class="productNumber">
            {{item.number}}
          </td>
          <td class="subtotal">
            ￥{{item.amountStr}}
          </td>
          <td :rowspan="list.length">
            <div v-if="index == 0">
              <input type="radio" checked>普通配送
              <select>
                <option value="0">快递 普通配送</option>
              </select>
            </div>
          </td>
        </tr>

      </table>
      <div class="confirmOrderFooter">
        <span>给卖家留言：</span>
        <span>店铺合计(含运费): ￥{{totalAmountStr}}</span>
        <a href="javascript:void(0)" v-if="!leaveMessageShow" @click="leaveMessageShow = true" class="leaveMessagePng"><img src="~assets/img/site/leaveMessage.png" height="26" width="255"/></a>
        <textarea maxlength="200" @keyup="leaveMsgKeyUp" v-model="order.userMessage" v-if="leaveMessageShow" class="leaveMessage" rows="4" cols="40"></textarea>
        <div class="explainSpan" v-if="leaveMessageShow" v-text="hint"></div>
      </div>
      <div class="submitOrderForm">
        <div><span>实付款：</span><span class="actualPayment">￥{{totalAmountStr}}</span></div>
        <button class="submitBtn" @click="submitOrder">提交订单</button>
      </div>
    </div>
  </div>
</template>

<script>
import storage from "../../assets/js/storage";
import moneyFormat from "../../assets/js/moneyFormat";
import settlement from "../../api/settlement";

export default {
  name: "index",
  data() {
    return {
      order: {}, // 订单对象
      list: [], // 订单项集合
      totalAmountStr: "0.00", // 总金额字符格式化
      totalAmount: 0, // 总金额
      leaveMessageShow: false, // 留言显示
      hint: "还可以输入200个字符", // 留言剩余可输入字符数量提示
    }
  },
  mounted() {
    this.init()
  },
  created() {


  },
  methods: {
    // 数据初始化
    init() {
      this.order.address = ""
      this.order.mobile = ""
      this.order.post = ""
      this.order.userMessage = ""
      this.order.receiver = ""
      let shopping = storage.getItem("shopping")
      this.list = shopping.orderItems
      this.totalAmountStr = shopping.totalAmountStr
      this.totalAmount = shopping.totalAmount
      // 格式化商品单价
      this.list.forEach(function (value) {
        value.priceStr = moneyFormat.format(value.params.productInfo.promotePrice)
      })
    },

    // 用户留言框输入
    leaveMsgKeyUp() {
      let msg = this.order.userMessage
      if (msg.length <= 200) {
        this.hint = "还可以输入"+ (200 - msg.length) +"个字符"
      }
    },

    // 下单
    submitOrder() {
      let idList = []
      this.list.forEach(function (value) {
        idList.push(value.id)
      })
      this.order.orderItemIdList = idList
      settlement.settlement(this.order)
        .then(response => {
          this.orderId = response.data
          // TODO 如果下单执行成功那么又向后端发送创建支付二维码请求
          this.$message.success("下单成功")

        })
    },

  }
}
</script>

<style scoped>

</style>
