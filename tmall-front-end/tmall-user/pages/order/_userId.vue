<template>
  <div>
    <!--这里放简单搜索栏-->
    <simple-search/>

    <!--这里放我的订单标题部分-->
    <div class="myOrderFormTitle" id="myOrderFormTitle">
      <div class="myOrderFormTitleOption">
        <span class="allOrderForm"><a @click="redLinePosition('all')" href="#">所有订单</a></span>
        <span class="obligation"><a @click="redLinePosition('waitPay')" href="#">待付款</a></span>
        <span class="toSendTheGoods"><a @click="redLinePosition('waitDeliver')" href="#">待发货</a></span>
        <span class="waitForReceiving"><a @click="redLinePosition('waitTake')" href="#">待收货</a></span>
        <span class="remainToBeEvaluated"><a @click="redLinePosition('evaluate')" href="#">待评价</a></span>
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
      <table class="myOrderProductItemTable" state="evaluate" dateTime="2016-09-12 17:00:41" orderNumber="20160912170041674794">
        <tr class="myOrderPrductItemTitle">
          <td colspan="2">
                    <span class="dateTime">2016-09-12 17:00:41
                    </span>订单号：<span class="orderNumber">20160912170041674794</span>
          </td>
          <td colspan="2">
            <img class="orderItemTmallPng" src="~/assets/img/site/orderItemTmall.png"/>天猫商城
          </td>
          <td>
            <a href="#"><div class="wangwangGif"></div></a>
          </td>
          <td>
            <a href="#nowhere"><span class="orderListItemDelete glyphicon glyphicon-trash"></span></a>
          </td>
        </tr>
        <tr class="myOrderProductItem">
          <td>
            <img class="productImage" src="~/assets/img/orderForm/3796.jpg"/>
          </td>
          <td>
            <div class="productInformation">
              <a href="#">公众智能扫地机器人家用全自动电动清洁地毯擦拖地一体机吸尘器</a>
              <div class="cartProductLinkInnerDiv">
                <img src="~/assets/img/site/creditcard.png" height="11" width="16"/>
                <img src="~/assets/img/site/7day.png" height="16" width="16"/>
                <img src="~/assets/img/site/promise.png" height="16" width="16"/>
              </div>
            </div>
          </td>
          <td class="price">
            <div>￥2,499.00</div>
            <div>￥2,124.15</div>
          </td>
          <td class="number">
            1
          </td>
          <td class="totalPrices">
            <div>￥2,124.15</div>
            <div>(含运费：￥0.00)</div>
          </td>
          <td class="evaluate">
            <a href="">
              <span>评价</span>
            </a>
          </td>
        </tr>
      </table>
      <table class="myOrderProductItemTable" state="takeDeliveryOfGoods" dateTime="2016-09-12 17:00:33" orderNumber="201609121700333128784">
        <tr class="myOrderPrductItemTitle">
          <td colspan="2">
                    <span class="dateTime">2016-09-12 17:00:33
                    </span>订单号：<span class="orderNumber">201609121700333128784</span>
          </td>
          <td colspan="2">
            <img class="orderItemTmallPng" src="~/assets/img/site/orderItemTmall.png"/>天猫商城
          </td>
          <td>
            <a href="#"><div class="wangwangGif"></div></a>
          </td>
          <td>
            <a href="#nowhere"><span class="orderListItemDelete glyphicon glyphicon-trash"></span></a>
          </td>
        </tr>
        <tr class="myOrderProductItem">
          <td>
            <img class="productImage" src="~/assets/img/orderForm/6651.jpg"/>
          </td>
          <td>
            <div class="productInformation">
              <a href="#">ULIFE原创信封包男个性真皮手包男士手拿包休闲男包手抓包拉链潮</a>
              <div class="cartProductLinkInnerDiv">
                <img src="~/assets/img/site/creditcard.png" height="11" width="16"/>
                <img src="~/assets/img/site/7day.png" height="16" width="16"/>
                <img src="~/assets/img/site/promise.png" height="16" width="16"/>
              </div>
            </div>
          </td>
          <td class="price">
            <div>￥1,188.00</div>
            <div>￥1,128.60</div>
          </td>
          <td class="number">
            1
          </td>
          <td class="totalPrices">
            <div>￥1,128.60</div>
            <div>(含运费：￥0.00)</div>
          </td>
          <td class="waitTake">
            <a href="">
              <span>确认收货</span>
            </a>
          </td>
        </tr>
      </table>
      <table class="myOrderProductItemTable" state="shipments" dateTime="2016-09-12 17:00:16" orderNumber="201609121700167279660">
        <tr class="myOrderPrductItemTitle">
          <td colspan="2">
                    <span class="dateTime">2016-09-12 17:00:16
                    </span>订单号：<span class="orderNumber">201609121700167279660</span>
          </td>
          <td colspan="2">
            <img class="orderItemTmallPng" src="~/assets/img/site/orderItemTmall.png"/>天猫商城
          </td>
          <td>
            <a href="#"><div class="wangwangGif"></div></a>
          </td>
          <td>
            <a href="#nowhere"><span class="orderListItemDelete glyphicon glyphicon-trash"></span></a>
          </td>
        </tr>
        <tr class="myOrderProductItem">
          <td>
            <img class="productImage" src="~/assets/img/orderForm/8697.jpg"/>
          </td>
          <td>
            <div class="productInformation">
              <a href="#">2016夏季新款女装雪纺衫短裤两件套 韩版时尚名媛休闲套装女夏装</a>
              <div class="cartProductLinkInnerDiv">
                <img src="~/assets/img/site/creditcard.png" height="11" width="16"/>
                <img src="~/assets/img/site/7day.png" height="16" width="16"/>
                <img src="~/assets/img/site/promise.png" height="16" width="16"/>
              </div>
            </div>
          </td>
          <td class="price">
            <div>￥263.00</div>
            <div>￥236.70</div>
          </td>
          <td class="number">
            1
          </td>
          <td class="totalPrices">
            <div>￥236.70</div>
            <div>(含运费：￥0.00)</div>
          </td>
          <td class="waitDeliver">
            <div>待发货</div>
            <a ref="">
              催卖家发货
            </a>
          </td>
        </tr>
      </table>
      <table class="myOrderProductItemTable" state="pay" dateTime="2016-09-12 17:00:09" orderNumber="201609121700098025374">
        <tr class="myOrderPrductItemTitle">
          <td colspan="2">
                    <span class="dateTime">2016-09-12 17:00:09
                    </span>订单号：<span class="orderNumber">201609121700098025374</span>
          </td>
          <td colspan="2">
            <img class="orderItemTmallPng" src="~/assets/img/site/orderItemTmall.png"/>天猫商城
          </td>
          <td>
            <a href="#"><div class="wangwangGif"></div></a>
          </td>
          <td>
            <a href="#nowhere"><span class="orderListItemDelete glyphicon glyphicon-trash"></span></a>
          </td>
        </tr>
        <tr class="myOrderProductItem">
          <td>
            <img class="productImage" src="~/assets/img/orderForm/5266.jpg"/>
          </td>
          <td>
            <div class="productInformation">
              <a href="#">米纳专柜新款女式手表时尚简约陶瓷表女士手表女表正品防水石英表</a>
              <div class="cartProductLinkInnerDiv">
                <img src="~/assets/img/site/creditcard.png" height="11" width="16"/>
                <img src="~/assets/img/site/7day.png" height="16" width="16"/>
                <img src="~/assets/img/site/promise.png" height="16" width="16"/>
              </div>
            </div>
          </td>
          <td class="price">
            <div>￥1,880.00</div>
            <div>￥1,316.00</div>
          </td>
          <td class="number">
            1
          </td>
          <td class="totalPrices">
            <div>￥1,316.00</div>
            <div>(含运费：￥0.00)</div>
          </td>
          <td class="waitPay">
            <a ref="">付款</a>
          </td>
        </tr>
      </table>
    </div>

  </div>
</template>

<script>
import SimpleSearch from "../../layouts/simpleSearch";
export default {
  name: "_userId",
  components: {SimpleSearch},
  created() {
    this.$nextTick(function () {
      this.redLinePosition('all')
    })
  },
  methods: {
    //红色下标的位置
    redLinePosition(status) {
      $(".myOrderFormTitleOption a").css("color", "black");
      switch (status) {
        case "waitPay":
          $(".obligation a").css("color", "#C40000");
          $(".redLine").css({"width": parseInt($(".obligation").css("width")) + 4, "left": 113});
          break;
        case "waitDeliver":
          $(".toSendTheGoods a").css("color", "#C40000");
          $(".redLine").css({"width": parseInt($(".toSendTheGoods").css("width")) + 4, "left": 212});
          break;
        case "waitTake":
          $(".waitForReceiving a").css("color", "#C40000");
          $(".redLine").css({"width": parseInt($(".waitForReceiving").css("width")) + 3, "left": 311});
          break;
        case "evaluate":
          $(".remainToBeEvaluated a").css("color", "#C40000");
          $(".redLine").css({"width": parseInt($(".remainToBeEvaluated").css("width")) + 3, "left": 410});
          break;
        default:
          $(".allOrderForm a").css("color", "#C40000");
          $(".redLine").css({"width": $(".allOrderForm").css("width"), "left": "0px"});
          break;
      }
    }
  }
}
</script>

<style scoped>

</style>
