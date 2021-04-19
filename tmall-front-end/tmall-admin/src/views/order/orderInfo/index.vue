<template>
  <div>
    <el-table
      :data="list"
      style="width: 100%">
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="商品名称">
              <span>{{ props.row.name }}</span>
            </el-form-item>
            <el-form-item label="所属店铺">
              <span>{{ props.row.shop }}</span>
            </el-form-item>
            <el-form-item label="商品 ID">
              <span>{{ props.row.id }}</span>
            </el-form-item>
            <el-form-item label="店铺 ID">
              <span>{{ props.row.shopId }}</span>
            </el-form-item>
            <el-form-item label="商品分类">
              <span>{{ props.row.category }}</span>
            </el-form-item>
            <el-form-item label="店铺地址">
              <span>{{ props.row.address }}</span>
            </el-form-item>
            <el-form-item label="商品描述">
              <span>{{ props.row.desc }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column
        label="序号"
        width="50">
        <template slot-scope="scope">{{ (current - 1) * limit + scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column
        label="状态"
        prop="params.orderStatusStr">
      </el-table-column>
      <el-table-column
        label="金额"
        prop="">
      </el-table-column>
      <el-table-column
        label="买家名称"
        prop="">
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
