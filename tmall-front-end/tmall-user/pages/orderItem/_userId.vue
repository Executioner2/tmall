<template>
  <div>

    <!--这里放简单搜索栏-->
    <simple-search/>

    <!--这里放结算里面嵌套订单项-->
    <div class="settleAccountsButton" id="settleAccountsButton">
      <div class="settleAccountsUpDiv">
        <span>已选商品 (不含运费)</span>
        <span class="settlementAmount totalAmount" id="settlementAmount1">￥{{totalAmountStr}}</span>
        <button :style="balanceBtnBackgroundColor" :disabled="balanceBtnDisable" @click="balance">结 算</button>
      </div>

      <!--这里放订单项内容-->
      <div class="orderItemContent" id="orderItemContent">
        <table class="orderItemContentTable" id="orderItemContentTable">
          <tr id="tableTitle">
            <td>
              <a href="javascript:void(0)" class="cartPullSelectBtn">
                <img class="cartPullSelect" @click="pullSelect" :src="selectStatusImageUrl"/>
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
              <a href="javascript:void(0)" class="cartSelectBtn" >
                <img class="cartSelect" @click="select($event, item)" src="~assets/img/site/cartNotSelected.png"/>
              </a>
              <img class="productImage" :src="item.params.productInfo.params.imageUrl" />
            </td>
            <td>
              <div class="productInformation">
                <router-link :to="'/product/' + item.params.productInfo.id">{{item.params.productInfo.name}}</router-link>
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
                <span><input type="text" class="productNumber" @blur="updateAmountAndStock(item)" name="productNumber" @keyup="inputNumber(item)" v-model="item.number"></span>
                <a href="javascript:void(0)" @click="numberAdd(item.id)" class="numberAdd">+</a>
              </div>
            </td>
            <td>
              <span>￥</span><span :ref="item.id" class="productMoney"></span>
            </td>
            <td>
              <a href="javascript:void(0)" class="deleteProductItem" @click="removeOrderItem(item.id)">删除</a>
            </td>
          </tr>
        </table>
      </div>

      <div class="settleAccountsDownDiv">
        <a href="javascript:void(0)" @click="pullSelect" class="cartPullSelectBtn">
          <img class="cartPullSelect" :src="selectStatusImageUrl"/>
        </a>
        <span>全选</span>
        <span class="rightSpan pull-right">
            <span>已选商品 <span class="productNumberSum">{{totalNumber}}</span> 件</span>
            <span>合计 (不含运费)：</span>
            <span class="settlementAmount totalAmount" id="settlementAmount2">￥{{totalAmountStr}}</span>
            <button :style="balanceBtnBackgroundColor" :disabled="balanceBtnDisable" @click="balance">结 算</button>
        </span>
      </div>
    </div>

  </div>
</template>

<script>
import SimpleSearch from "../../layouts/simpleSearch";
import orderItem from "../../api/orderItem";

import selectUrl from "../../assets/img/site/cartSelected.png"
import notSelectUrl from "../../assets/img/site/cartNotSelected.png"
import moneyFormat from "../../assets/js/moneyFormat";
import storage from "../../assets/js/storage";

export default {
  components: {SimpleSearch},
  data() {
    return {
      list: [], // 订单项信息集合
      selectStatusImageUrl: notSelectUrl, // 选中状态态图片url，默认未选中
      isFullSelect: false, // 是否是全选
      totalAmount: 0, // 总金额
      totalAmountStr: "0.00", // 总金额格式化
      totalNumber: 0, // 已选商品总数
      balanceBtnDisable: true, // 结算按钮不可用
      balanceBtnBackgroundColor: "background-color: #AAAAAA", // 结算按钮背景色
    }
  },
  created() {
    this.getOrderItem()
  },
  methods: {
    // 更新商品库存数量
    updateStock(item) {
      orderItem.updateStock(item)
        .then(response => {
          let stock = response.data
          // 如果返回的商品库存不为空则更新库存信息
          if (stock) {
            item.params.productInfo.stock = stock
          }
        })
    },

    // 结算
    balance() {
      // 遍历订单项，如果标记为选中那么就加入订单
      let shopping = {}
      shopping.orderItems = []
      this.list.forEach(function (value) {
        if (value.isSelect) {
          shopping.orderItems.push(value) // 加入到集合中
        }
      })
      shopping.totalAmountStr = this.totalAmountStr
      shopping.totalAmount = this.totalAmount
      // 不传入后端了，直接就把这个数据带到下单页面
      // 存入localStorage
      storage.setItem("shopping", shopping, -1)
      this.$router.push("/settlement");
    },

    // 删除订单
    removeOrderItem(id) {
      this.$confirm("是否要把该商品移除购物车？", "提示", {
        confirmButtonText: "是",
        cancelButtonText: "否",
        type: "warning"
      }).then(() => {
        // 移除商品，刷新页面
        orderItem.removeProduct(id)
          .then(() => {
            this.$router.go(0)
          })
      })
    },

    // 全选
    pullSelect() {
      this.isFullSelect = !this.isFullSelect
      if (this.isFullSelect) { // 如果是选中状态
        // 所有订单项做选中标记
        this.list.forEach(function (value) {
          value.isSelect = true
        })
        // 设置图标为选中
        this.selectStatusImageUrl = selectUrl
        $(".cartSelect").attr("src", selectUrl)
      } else { // 不是选中状态
        // 所有订单项做未选中标记
        this.list.forEach(function (value) {
          value.isSelect = false
        })
        // 设置图标为未选中
        this.selectStatusImageUrl = notSelectUrl
        $(".cartSelect").attr("src", notSelectUrl)
      }

      this.countTotalAmount() // 计算总金额
    },

    // 单选
    select(event, item) {
      let el = event.target // 获取点击事件被点到的目标元素
      if (item.isSelect == null) { // 如果item.isSelect为null则给个初始值
        item.isSelect = false
      }
      item.isSelect = !item.isSelect
      if (item.isSelect) { // 如果点击的时候是false，那么要改变为true
        el.src = selectUrl
      } else { // 反之
        el.src = notSelectUrl
      }
      if (this.forEachSelect()) { // 如果为true则表示全选
        // 更新全选图标
        this.selectStatusImageUrl = selectUrl
        this.isFullSelect = true
      } else {
        // 更新全不选图标
        this.selectStatusImageUrl = notSelectUrl
        this.isFullSelect = false
      }
      this.countTotalAmount() // 计算总金额
    },

    // 遍历所有单选项
    forEachSelect() {
      for (let i = 0; i < this.list.length; i++) {
        // 如果有一个没有被选中则返回false
        if (!this.list[i].isSelect) {
          return false
        }
      }
      // 执行到这里表示都选中了，返回true
      return true
    },

    // 计算总金额
    countTotalAmount() {
      this.totalAmount = 0
      this.totalNumber = 0
      for (let i = 0; i < this.list.length; i++) {
        let item = this.list[i]
        if (item.isSelect) { // 如果是选择状态
          this.totalAmount = (parseFloat(this.totalAmount) + parseFloat(item.amount)).toFixed(2)
          // 已选商品总数量
          this.totalNumber += item.number
        }
      }
      if (this.totalAmount === 0) { // 如果购物车已选商品总金额为0则结算按钮不可用
        this.balanceBtnDisable = true
        this.balanceBtnBackgroundColor = "background-color: #AAAAAA" // 设置背景色为灰色
      } else { // 否则反之
        this.balanceBtnDisable = false
        this.balanceBtnBackgroundColor = "background-color: #C40000" // 设置背景色为红色
      }
      this.totalAmountStr = moneyFormat.format(this.totalAmount) // 格式化
    },

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
          // 更新商品库存
          this.updateStock(value)
          return
        }
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
          // 更新商品库存
          this.updateStock(value)
          return
        }
      }
    },

    // 更新商品价格
    updateAmount(item) {
      let price = item.params.productInfo.promotePrice
      item.amount = (price * item.number).toFixed(2)
      // dom渲染完毕后填充text，dom渲染完成后才有ref成员
      this.$nextTick(() => {
        // 这里手动赋值是因为@blur不能触发v-text更新
        item.amountStr = moneyFormat.format(item.amount) // 格式化一手
        this.$refs[item.id][0].innerText = item.amountStr
      })

      this.countTotalAmount() // 计算总金额
    },

    // 输入商品数量
    inputNumber(val) {
      if (val.number == "" || val.number == null || val.number == 0) {
        val.number = 1
      } else if (isNaN(val.number)) {
        val.number = parseInt(val.number.substring(0, val.number.length - 1))
      } else if (val.number > val.params.productInfo.stock) {
        val.number = val.params.productInfo.stock
      }
    },

    // 更新商品库存和金额
    updateAmountAndStock(item) {
      this.updateAmount(item)
      // 更新商品库存
      this.updateStock(item)
    },

  }
}
</script>

<style scoped>

</style>
