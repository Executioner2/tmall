<template>
  <div>
    <!-- 条件查询 -->
    <el-form :inline="true" :model="searchObj" class="demo-form-inline" style="margin: 20px 0px 0px 10px">
      <el-form-item label="订单号">
        <el-input style="width: 170px" v-model="searchObj.outTradeNo" placeholder="天猫订单号"></el-input>
      </el-form-item>
      <el-form-item label="用户ID">
        <el-input style="width: 170px" v-model="searchObj.userId" placeholder="用户ID"></el-input>
      </el-form-item>
      <el-form-item label="订单状态">
        <el-select style="width: 100px" v-model="searchObj.orderStatus" placeholder="请选择">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item style="margin-left: 10px" label="创建日期">
        <div class="block" style="display: inline-block">
          <el-date-picker
            style="width: 160px"
            value-format="yyyy-MM-dd"
            v-model="searchObj.createDateBegin"
            type="date"
            placeholder="选择日期">
          </el-date-picker>
        </div>
        <span> - </span>
        <div class="block" style="display: inline-block">
          <el-date-picker
            style="width: 160px"
            value-format="yyyy-MM-dd"
            v-model="searchObj.createDateEnd"
            type="date"
            placeholder="选择日期">
          </el-date-picker>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button style="margin-left: 10px" type="primary" @click="findPageOrderInfo(1)">查询</el-button>
        <el-button @click="reset" type="danger" plain>重置</el-button>
      </el-form-item>
    </el-form>

    <el-table
      :data="list"
      style="width: 100%"
      :row-style="{height: '65px'}">
      <el-table-column
        type="expand">
         <template slot-scope="scope">
           <div style="width: 100%; text-align: center;" v-for="(item) in scope.row.params.orderItems">
             <el-image style="width: 50px;" :src="item.params.productInfo.params.imageUrl"></el-image>
             <div style="display: inline-block; margin-left: 20px; padding-top: 15px; vertical-align: top">
               <el-link type="primary" style="margin-right: 80px">{{item.params.productInfo.name}}</el-link>
               <span style="margin-right: 80px">{{item.number + "个"}}</span>
               <span>{{"单价：¥" + item.params.productInfo.promotePrice}}</span>
             </div>
           </div>
         </template>
      </el-table-column>
      <el-table-column
        label="序号"
        width="50">
        <template slot-scope="scope">{{ (current - 1) * limit + scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column
        label="状态"
        width="70"
        prop="params.orderStatusStr">
      </el-table-column>
      <el-table-column
        label="金额"
        width="75"
        prop="params.totalMoney">
      </el-table-column>
      <el-table-column
        label="商品数量"
        width="80"
        align="center"
        header-align="left"
        prop="params.number">
      </el-table-column>
      <el-table-column
        label="买家名称"
        width="120"
        prop="params.customerName">
      </el-table-column>
      <el-table-column
        label="创建时间"
        prop="createDate">
      </el-table-column>
      <el-table-column
        label="支付时间"
        prop="payDate">
      </el-table-column>
      <el-table-column
        label="发货时间"
        prop="deliveryDate">
      </el-table-column>
      <el-table-column
        label="确认收货时间"
        prop="confirmDate">
      </el-table-column>
      <el-table-column
        label="操作"
        width="150">
        <template slot-scope="scope">
          <el-button v-if="scope.row.orderStatus == 1" @click="deliver(scope.row.id)" type="primary" size="mini">发货</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div style="margin-top: 20px">
      <el-pagination
        :page-size="limit"
        @current-change="findPageOrderInfo"
        align="center"
        :current-page="current"
        background
        layout="prev, pager, next"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import orderInfo from "@/api/order/orderInfo";

export default {
  data() {
    return {
      list: [], // 订单列表
      current: 1, // 起始页
      limit: 5, // 每页记录数
      total: null, // 总记录数
      searchObj: {}, // 查询条件对象
      options: [{ // 订单状态
        value: 0,
        label: "待付款"
      },{
        value: 1,
        label: "待发货"
      },{
        value: 2,
        label: "待收货"
      },{
        value: 3,
        label: "待评价"
      }]
    }
  },
  created() {
    this.findPageOrderInfo(1)
  },
  methods: {
    // 分页条件查询
    findPageOrderInfo(val){
      this.current = val
      orderInfo.findPageOrderInfo(this.current, this.limit, this.searchObj)
        .then(response => {
          this.list = response.data.records
          this.total = response.data.total
        })
    },

    // 发货
    deliver(id) {
      orderInfo.deliverGoods(id)
        .then(response => {
          this.findPageOrderInfo(this.current)
          this.$message.success("发货成功！")
        })
        .catch(error => {
          this.$message.error("发货失败！")
        })
    },

    // 清空查询条件
    reset(){
      this.searchObj = {}
    }
  }
}
</script>
