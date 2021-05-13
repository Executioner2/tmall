<template>
  <div>

    <!--这里放简单搜索栏-->
    <simple-search/>

    <!--这里放结算里面嵌套订单项-->
    <script>
      /*格式化货币*/
      function formattingMoney(money){
        money = money.toString()
        let length = parseInt(money).toString().length;
        let rem = length % 3;
        let newMoney = new Array();
        let flag = false;
        for (var i = 0; i < money.length; i++) {
          if (money[i] == '.'){
            flag = money.substring(i);
            break;
          }
        }
        for (var i = 0; i < length; i++) {
          if(i % 3 - rem == 0 && i > 0){
            newMoney += ',';
          }
          newMoney += money[i];
        }
        if(!flag){
          newMoney += '.00';
        }else{
          newMoney += flag;
        }
        return newMoney;
      }

    </script>

    <div class="settieAccountsButton" id="settieAccountsButton">
      <div class="settieAccountsUpDiv">
        <span>已选商品 (不含运费)</span>
        <span class="settlementAmount totalAmount" id="settlementAmount1">￥{{totalAmount}}</span>
        <button disabled>结 算</button>
      </div>

      <!--这里放订单项内容-->
      <div class="orderItemContent" id="orderItemContent">
        <table class="orderItemContentTable" id="orderItemContentTable">
          <tr id="tableTitle">
            <td>
              <a href="javascript:void(0)" class="cartPullSelectBtn">
                <img class="cartPullSelect" src="~assets/img/site/cartNotSelected.png"/>
              </a>
              <span>全选</span>
            </td>
            <td>商品信息</td>
            <td>单价</td>
            <td>数量</td>
            <td>金额</td>
            <td>操作</td>
          </tr>
          <tr class="tableBody" v-for="(item, index) in list" :key="index" >
            <td>
              <a href="javascript:void(0)" class="cartSelectBtn">
                <img class="cartSelect" src="~assets/img/site/cartNotSelected.png"/>
              </a>
              <img class="productImage" :src="item.params.productInfo.params.imageUrl" />
            </td>
            <td>
              <div class="productInformation">
                <a href="#">{{item.params.productInfo.name}}</a>
                <div class="cartProductLinkInnerDiv">
                  <img src="~assets/img/site/creditcard.png" height="11" width="16"/>
                  <img src="~assets/img/site/7day.png" height="16" width="16"/>
                  <img src="~assets/img/site/promise.png" height="16" width="16"/>
                </div>
              </div>
            </td>
            <td>
              <div><del>￥{{item.params.productInfo.orignalPrice}}</del></div>
              <div>￥{{item.params.productInfo.promotePrice}}</div>
            </td>
            <td>
              <div>
                <a href="javascript:void(0)" @click="numberSub(item.id)" class="numberSub">-</a>
                <span><input type="text" class="productNumber" @blur="updateAmount(item)" name="productNumber" @keyup="inputNumber(item)" v-model="item.number"></span>
                <a href="javascript:void(0)" @click="numberAdd(item.id)" class="numberAdd">+</a>
              </div>
            </td>
            <td>
              <span>￥</span><span ref="productMoney" class="productMoney"></span>
            </td>
            <td>
              <a href="javascript:void(0)" class="deleteProductItem">删除</a>
            </td>
          </tr>
        </table>
      </div>

      <div class="settieAccountsDownDiv">
        <a href="javascript:void(0)" class="cartPullSelectBtn">
          <img class="cartPullSelect" src="~assets/img/site/cartNotSelected.png"/>
        </a>
        <span>全选</span>
        <span class="rightSpan pull-right">
                <span>已选商品 <span class="productNumberSum">0</span> 件</span>
                <span>合计 (不含运费)：</span>
                <span class="settlementAmount totalAmount" id="settlementAmount2">￥{{totalAmount}}</span>
                <button disabled>结 算</button>
            </span>
      </div>
    </div>

  </div>
</template>

<script>
import SimpleSearch from "../../layouts/simpleSearch";
import orderItem from "../../api/orderItem";
export default {
  name: "_userId",
  components: {SimpleSearch},
  data() {
    return {
      list: [], // 订单项信息集合
      totalAmount: "0.00", // 总金额
    }
  },
  created() {
    this.getOrderItem()
  },
  methods: {
    // 获取购物车商品详情
    getOrderItem() {
      orderItem.getOrderItem()
        .then(response => {
          this.list = response.data
          this.productAmount()
        })
    },

    // 同类商品金额计算
    productAmount() {
      for (let i = 0; i < this.list.length; i++) {
        this.updateAmount(this.list[i])
      }
    },

    // 商品数量 -1
    numberSub(orderItemId) {
      for (let i = 0; i < this.list.length; i++) {
        let value = this.list[i]
        if (value.id === orderItemId) { // 根据订单项id判断是哪个订单项更新商品数量
          if (value.number === 1) { // 如果商品数量已经是1了就不能再减了
            return
          }
          --value.number
          // 重新计算商品金额
          this.updateAmount(value)
          return
        }

        this.updateAmount(this.list[i])
      }
    },

    // 商品数量 +1
    numberAdd(orderItemId) {
      for (let i = 0; i < this.list.length; i++) {
        let value = this.list[i]
        if (value.id === orderItemId) { // 根据订单项id判断是哪个订单项更新商品数量
          if (value.number === value.params.productInfo.stock) { // 如果商品数量已经等于商品库存了就不能再加了
            return
          }
          ++value.number
          // 重新计算商品金额
          this.updateAmount(value)
          return
        }
      }
    },

    // 更新商品价格
    updateAmount(item) {
      let price = item.params.productInfo.promotePrice
      item.amount = (price * item.number).toFixed(2)
      this.$nextTick(() => {
        this.$refs.productMoney[0].innerText = item.amount
      })

    },

    // 输入商品数量
    inputNumber(val) {
      if (val.number == "" || val.number == null || val.number == 0) {
        val.number = 1
        return
      }
      if (isNaN(val.number)) {
        val.number = parseInt(val.number.substring(0, val.number.length - 1))
        return
      }
      if (val.number > val.params.productInfo.stock) {
        val.number = val.params.productInfo.stock
      }
    },

  }
}
</script>

<style scoped>

</style>
