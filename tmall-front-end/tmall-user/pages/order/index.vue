<template>
  <div>
    <!--这里放简单搜索栏-->
    <simple-search/>

    <!--这里放我的订单标题部分-->
    <div class="myOrderFormTitle" id="myOrderFormTitle">
      <div class="myOrderFormTitleOption">
        <span class="allOrderForm"><a @click="listOrderInfo(-1)" href="javascript:void(0)">所有订单</a></span>
        <span class="obligation"><a @click="listOrderInfo(0)" href="javascript:void(0)">待付款</a></span>
        <span class="toSendTheGoods"><a @click="listOrderInfo(1)" href="javascript:void(0)">待发货</a></span>
        <span class="waitForReceiving"><a @click="listOrderInfo(2)" href="javascript:void(0)">待收货</a></span>
        <span class="remainToBeEvaluated"><a @click="listOrderInfo(3)" href="javascript:void(0)">待评价</a></span>
        <div class="redLine"></div>
      </div>
      <table class="productItemTitle">
        <tr>
          <td>宝贝</td>
          <td>单价</td>
          <td>数量</td>
          <td>实付款</td>
          <td>交易操作</td>
        </tr>
      </table>
    </div>

    <!--这里放产品列表部分-->
    <div id="myOrderItem" class="myOrderItem myAllOrder">
      <table class="myOrderProductItemTable" state="evaluate" v-for="(item, index) in list" :key="index"  >
        <tr class="myOrderPrductItemTitle">
          <td colspan="2">
              <span class="dateTime">{{item.createDate}}
              </span>订单号：<span class="orderNumber">{{item.outTradeNo}}</span>
          </td>
          <td colspan="2">
            <img class="orderItemTmallPng" src="~/assets/img/site/orderItemTmall.png"/>天猫商城
          </td>
          <td>
            <a href="javascript:void(0)"><div class="wangwangGif"></div></a>
          </td>
          <td>
            <a @click="remove(item.id)" href="javascript:void(0)" v-if="item.orderStatus >= 3"><span class="orderListItemDelete glyphicon glyphicon-trash"></span></a>
          </td>
        </tr>
        <tr class="myOrderProductItem" v-for="(oItem, index2) in item.params.orderItems" :key="index2">
          <td>
            <img class="productImage" :src="oItem.params.productInfo.params.imageUrl"/>
          </td>
          <td>
            <div class="productInformation">
              <router-link :to="'/product/' + oItem.params.productInfo.id">{{oItem.params.productInfo.name}}</router-link>
              <div class="cartProductLinkInnerDiv">
                <img src="~/assets/img/site/creditcard.png" height="11" width="16"/>
                <img src="~/assets/img/site/7day.png" height="16" width="16"/>
                <img src="~/assets/img/site/promise.png" height="16" width="16"/>
              </div>
            </div>
          </td>
          <td class="price">
            <div>￥{{moneyFormat(oItem.params.productInfo.orignalPrice)}}</div>
            <div>￥{{moneyFormat(oItem.params.productInfo.promotePrice)}}</div>
          </td>
          <td class="number" v-if="index2 == 0" :rowspan="item.params.orderItems.length">
            {{item.params.totalNumber}}
          </td>
          <td class="totalPrices" v-if="index2 == 0" :rowspan="item.params.orderItems.length">
            <div>￥{{moneyFormat(item.amount)}}</div>
            <div>(含运费：￥0.00)</div>
          </td>
          <td class="waitPay" v-if="index2 == 0 && item.orderStatus == 0" :rowspan="item.params.orderItems.length">
            <router-link :to="'/pay/' + item.id">
              <span>付款</span>
            </router-link>
          </td>
          <td class="waitDeliver" v-if="index2 == 0 && item.orderStatus == 1" :rowspan="item.params.orderItems.length">
            <span>待发货</span>
            <a href="javascript:void(0)" @click="deliver(item.id)">
              <span>催卖家发货</span>
            </a>
          </td>
          <td class="waitTake" v-if="index2 == 0 && item.orderStatus == 2" :rowspan="item.params.orderItems.length">
            <router-link :to="'/confirmReceipt/' + item.id">
              <span>确认收货</span>
            </router-link>
          </td>
          <td class="evaluate" v-if="item.orderStatus == 3">
            <router-link :to="{name: 'review', query:{'orderItemId': oItem.id, 'createDate': item.createDate}}" v-if="oItem.isReview == 0">
              <span>评价</span>
            </router-link>
            <span v-else>
              已评价
            </span>
          </td>
          <td v-if="index2 == 0 && item.orderStatus == 4" :rowspan="item.params.orderItems.length">
            <span>已完成</span>
          </td>
        </tr>
      </table>
    </div>

  </div>
</template>

<script>
import SimpleSearch from "../../layouts/simpleSearch";
import order from "../../api/order";
import moneyFormat from "../../assets/js/moneyFormat";
export default {
  components: {SimpleSearch},
  data() {
    return {
      list: [], // 订单信息集合
    }
  },
  created() {
    this.$nextTick(function () {
      this.listOrderInfo(-1)
    })
  },
  methods: {
    // 显示订单
    listOrderInfo(status) {
      order.listOrderInfo(status)
        .then(response => {
          this.list = response.data
        })
      this.redLinePosition(status)
    },

    // 货币格式化
    moneyFormat(data) {
      return moneyFormat.format(data)
    },

    // 发货
    deliver(orderId) {
      order.deliver(orderId)
        .then(response => {
          this.$message.success("卖家已光速发货")
          // 一秒后自动刷新页面
          setTimeout(() => {
            this.$router.go(0)
          }, 1000)
        })
        .catch(error => {
          this.$message.error("卖家拒绝发货并向您竖了个中指")
        })
    },

    // 删除订单
    remove(orderId) {
      this.$confirm("此操作将永久删除订单？是否继续", "提示", {
        confirmButtonText: "是",
        cancelButtonText: "否",
        type: "warning"
      }).then(() => {
        order.deleteOrderInfo(orderId)
          .then(() => {
            this.$router.go(0)
          })
      })
    },

    //红色下标的位置
    redLinePosition(status) {
      this.$nextTick(() => {
        $(".myOrderFormTitleOption a").css("color", "black");
        switch (status) {
          case 0:
            $(".obligation a").css("color", "#C40000");
            $(".redLine").css({"width": parseInt($(".obligation").css("width")) + 4, "left": 113});
            break;
          case 1:
            $(".toSendTheGoods a").css("color", "#C40000");
            $(".redLine").css({"width": parseInt($(".toSendTheGoods").css("width")) + 4, "left": 212});
            break;
          case 2:
            $(".waitForReceiving a").css("color", "#C40000");
            $(".redLine").css({"width": parseInt($(".waitForReceiving").css("width")) + 3, "left": 311});
            break;
          case 3:
            $(".remainToBeEvaluated a").css("color", "#C40000");
            $(".redLine").css({"width": parseInt($(".remainToBeEvaluated").css("width")) + 3, "left": 410});
            break;
          case -1:
            $(".allOrderForm a").css("color", "#C40000");
            $(".redLine").css({"width": parseInt($(".allOrderForm").css("width")), "left": 0});
            break;
        }
      })

    }
  }
}
</script>

<style scoped>

</style>
