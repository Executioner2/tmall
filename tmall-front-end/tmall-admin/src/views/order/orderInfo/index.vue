<template>
  <div>
    <el-table
      :data="list"
      style="width: 100%">
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
          <el-button v-if="scope.row.status == 1" @click="authBtn(scope.row.id, 2)" type="primary" size="mini">发货</el-button>
        </template>
      </el-table-column>
    </el-table>
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
    }
  }
}
</script>
