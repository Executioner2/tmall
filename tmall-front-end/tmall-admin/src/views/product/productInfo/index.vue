<template>
  <div>
    <!-- 产品查询 -->
    <el-form :inline="true" :model="searchObj" class="demo-form-inline" style="margin: 20px 0px 0px 10px">
      <el-form-item label="产品名称">
        <el-input style="width: 170px" v-model="searchObj.keyword" placeholder="产品名称/产品小标题"></el-input>
      </el-form-item>
      <el-form-item style="margin-left: 10px" label="价格区间">
        <el-input style="width: 120px; display: inline-block" v-model.trim="searchObj.lowPrice" @input="searchObj.lowPrice = checkInput(searchObj.lowPrice)" placeholder="最低售价"></el-input>
        <span> - </span>
        <el-input style="width: 120px; display: inline-block" v-model.trim="searchObj.highPrice" @input="searchObj.highPrice = checkInput(searchObj.highPrice)" placeholder="最高售价"></el-input>
      </el-form-item>
      <el-form-item style="margin-left: 10px" label="创建日期">
        <div class="block" style="display: inline-block">
          <el-date-picker
            style="width: 160px"
            value-format="yyyy-MM-dd"
            v-model="searchObj.createTimeBegin"
            type="date"
            placeholder="选择日期">
          </el-date-picker>
        </div>
        <span> - </span>
        <div class="block" style="display: inline-block">
          <el-date-picker
            style="width: 160px"
            value-format="yyyy-MM-dd"
            v-model="searchObj.createTimeEnd"
            type="date"
            placeholder="选择日期">
          </el-date-picker>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button style="margin-left: 10px" type="primary" @click="findPageProductInfo(1)">查询</el-button>
        <el-button @click="searchObj = {}" type="danger" plain>重置</el-button>
      </el-form-item>
      <!-- 批量删除按钮 -->
      <div style="float: right; margin-right: 10px">
        <el-button :disabled="delDisable" @click="batchRemove" type="danger" plain>批量删除</el-button>
      </div>
    </el-form>

    <!-- 显示产品 -->
    <el-table
      ref="multipleTable"
      :data="list"
      tooltip-effect="dark"
      size="small"
      style="width: 100%"
      @selection-change="handleSelectionChange">
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>
      <el-table-column
        label="序号"
        width="50">
        <template slot-scope="scope">{{ (current - 1) * limit + scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column
        label="图片"
        width="100"
        align="left">
        <template slot-scope="scope">
          <img :src="scope.row.params.imageUrl" width="50">
        </template>
      </el-table-column>
      <el-table-column
        prop="name"
        label="产品名称"
        show-overflow-tooltip
        align="left">
      </el-table-column>
      <el-table-column
        align="left"
        prop="subTitle"
        label="产品小标题"
        width="300">
      </el-table-column>
      <el-table-column
        prop="orignalPrice"
        label="原价格"
        width="80"
        align="center">
      </el-table-column>
      <el-table-column
        prop="promotePrice"
        label="优惠价格"
        width="80"
        align="center">
      </el-table-column>
      <el-table-column
        prop="stock"
        label="库存数量"
        width="80"
        align="center">
      </el-table-column>
      <el-table-column
        label="图片管理"
        width="80"
        align="center">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="图片管理" placement="top">
            <router-link :to="'/product/productImage/' + scope.row.id"> <!--跳转到隐藏路由-->
              <el-link :underline="false"><i style="font-size: 15px" class="el-icon-picture-outline"></i></el-link>
            </router-link>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        label="设置属性"
        width="80"
        align="center">
        <template slot-scope="scope">
          <router-link :to="'/product/propertyValue/' + scope.row.id">
            <el-tooltip class="item" effect="dark" content="设置属性" placement="top">
              <el-link :underline="false"><i style="font-size: 15px" class="el-icon-receiving"></i></el-link>
            </el-tooltip>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column
        label="编辑"
        width="50"
        align="center">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="编辑产品" placement="top">
            <el-link @click="showDialog(scope.row)" :underline="false"><i style="font-size: 15px" class="el-icon-edit"></i></el-link>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        label="删除"
        width="50"
        align="center">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="删除产品" placement="top">
            <el-link  @click="remove(scope.row.id)" :underline="false"><i style="font-size: 15px" class="el-icon-delete"></i></el-link>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>

    <!-- 产品分页 -->
    <div style="margin-top: 20px">
      <el-pagination
        :page-size="limit"
        @current-change="findPageProductInfo"
        align="center"
        :current-page="current"
        background
        layout="prev, pager, next"
        :total="total">
      </el-pagination>
    </div>

    <!-- 添加产品 -->
    <div style="margin: 40px;">
      <el-form style="width:460px; margin:0 auto; border: #FAEBCC 1px solid; border-radius: 7px;"
               align="center"
               label-width="90px"
               :model="productObj">
        <div style="background-color: #FCF8E3; color: #8A6D3B; line-height: 40px">
          <span>新增产品</span>
        </div>
        <el-form-item label="产品名称" style="margin: 10px">
          <el-input v-model.trim="productObj.name"></el-input>
        </el-form-item>
        <el-form-item label="产品小标题" style="margin: 10px">
          <el-input v-model.trim="productObj.subTitle"></el-input>
        </el-form-item>
        <el-form-item label="原价格" style="margin: 10px">
          <el-input v-model.trim="productObj.orignalPrice" @input="productObj.orignalPrice = checkInput(productObj.orignalPrice)"></el-input>
        </el-form-item>
        <el-form-item label="优惠价格" style="margin: 10px">
          <el-input v-model.trim="productObj.promotePrice" @input="productObj.promotePrice = checkInput(productObj.promotePrice)"></el-input>
        </el-form-item>
        <el-form-item label="库存" style="margin: 10px">
          <el-input v-model.number="productObj.stock" @input="productObj.stock = isNaN(productObj.stock) ? 0 : productObj.stock"></el-input>
        </el-form-item>
        <!--提交按钮-->
        <el-button style="margin-bottom: 10px" type="success" @click="addProduct">提交</el-button>
      </el-form>
    </div>

    <!-- 更新产品模态框 -->
    <el-dialog
      title="编辑分类"
      :visible.sync="dialogVisible"
      width="520px">
      <!-- 编辑产品 -->
      <el-form style="width:460px; margin:0 auto;"
               align="center"
               label-width="90px"
               :model="editObj">
        <el-form-item label="产品名称" style="margin: 10px">
          <el-input v-model.trim="editObj.name"></el-input>
        </el-form-item>
        <el-form-item label="产品小标题" style="margin: 10px">
          <el-input v-model.trim="editObj.subTitle"></el-input>
        </el-form-item>
        <el-form-item label="原价格" style="margin: 10px">
          <el-input v-model.trim="editObj.orignalPrice" @input="editObj.orignalPrice = checkInput(editObj.orignalPrice)"></el-input>
        </el-form-item>
        <el-form-item label="优惠价格" style="margin: 10px">
          <el-input v-model.trim="editObj.promotePrice" @input="editObj.promotePrice = checkInput(editObj.promotePrice)"></el-input>
        </el-form-item>
        <el-form-item label="库存" style="margin: 10px">
          <el-input v-model.number="editObj.stock" @input="editObj.stock = isNaN(editObj.stock) ? 0 : editObj.stock"></el-input>
        </el-form-item>
        <!--提交按钮-->
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button style="margin-bottom: 10px" type="success" @click="updateProductInfo">提交</el-button>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
import productInfo from "@/api/product/productInfo"
import strUtil from "@/utils/myUtil/strUtil"
import cookies from 'js-cookie'

export default {
    data() {
      return {
        searchObj: {}, // 查询对象
        list: [], // 数据集合
        current: 1, // 起始页
        limit: 5, //每页记录数
        total: null, // 总记录数
        productObj: {}, // 添加的产品对象
        editObj: {}, // 编辑的对象
        delDisable: true, // 删除按钮是否可用  :disable 内的true 和 false 参数是字符串
        dialogVisible: false, // 模态框默认不显示
        categoryId: null, // 分类id
      }
    },

    created() {
      this.categoryId = this.$route.params.id
      cookies.set("urlLast", this.categoryId) // 把categoryId存入到cookies，做面包屑导航
      this.findPageProductInfo(1)
    },

    methods: {
      // 分页显示商品
      findPageProductInfo(val){
        this.current = val
        this.searchObj.categoryId = this.categoryId
        productInfo.findPageProductInfo(this.current, this.limit, this.searchObj)
          .then(response => {
            this.list = response.data.records
            this.total = response.data.total
          })
      },

      // 记录选择
      handleSelectionChange(val) {
        this.multipleSelection = val;
        if (this.multipleSelection.length != 0) {
          this.delDisable = false
        } else {
          this.delDisable = true
        }
      },

      // 解析 multipleSelection 取得idList
      getIdList() {
        let idList = []
        this.multipleSelection.forEach(function (value) {
          idList.push(value.id)
        })
        return idList
      },

      // 初始化值
      init() {
        this.searchObj = {}
        this.productObj = {}
        this.dialogVisible = false
        this.delDisable = true
      },

      // 添加/编辑 表单值合法性检测
      checkValue(obj) {
        if (strUtil.isEmpty(obj.name)){
          this.$message.warning("产品名称不能为空")
          return false
        }
        if (strUtil.isEmpty(obj.orignalPrice)){
          this.$message.warning("产品原价格不能为空")
          return false
        }
        if (strUtil.isEmpty(obj.promotePrice)){
          this.$message.warning("产品实际价格不能为空")
          return false
        }
        if (strUtil.isEmpty(obj.stock)){
          this.$message.warning("产品库存不能为空")
          return false
        }
        // 判断是否为合法金额
        if (!strUtil.isLegalAmount(obj.orignalPrice)
          || !strUtil.isLegalAmount(obj.promotePrice)){
          this.$message.warning("非法金额")
          return false
        }
        // 判断库存是否为合法数字
        if (!strUtil.isNumber(obj.stock)){
          this.$message.warning("非法数值，请输入正整数")
          return false
        }
        return true
      },

      // 添加产品
      addProduct() {
        // 检查值是否合法
        if (!this.checkValue(this.productObj)) {
          return
        }
        this.productObj.categoryId = this.categoryId
        // 调用接口添加产品
        productInfo.saveProductInfo(this.productObj)
          .then(response => {
            this.init() // 清空缓存值
            this.findPageProductInfo(this.current)
          })
      },

      // 删除产品
      remove(id) {
        this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          productInfo.remove(id)
            .then(response => {
              this.findPageProductInfo(1)
            })
            .catch(error => {
              this.$message.warning("删除失败！")
            })
        })
      },

      // 批量删除产品
      batchRemove(){
        this.$confirm('此操作将永久删除选中记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 取得idList调用后端删除记录
          let idList = this.getIdList()
          productInfo.batchRemove(idList)
            .then(response => {
              this.findPageProductInfo(1)
            })
            .catch(error => {
              this.$message.warning("删除失败！")
            })
        })
      },

      // 价格查询框输入时检测数值合法性
      checkInput(price) {
        // 如果长度大于1，且首位为0，则把首位的0去掉
        if (price != null && price.length > 1 && price.substring(0, 1) == 0) {
          price = price.substring(1, price.length)
        }
        // 如果输入了一个其他字符导致不是数值型，则删除最后输入的那个字符
        if (isNaN(price) && price != null) {
          price = price.substring(0, price.length - 1)
        }
        return price
      },

      // 显示模态框
      showDialog(obj) {
        // this.editObj = obj  // 导致 this.editObj 指向 this.productInfo  修改模态框的值改变了productInfo表格中的值
        this.editObj = JSON.parse(JSON.stringify(obj)) // 这样只是拷贝属性，不会指向同一个对象
        this.dialogVisible = true
      },

      // 更新产品
      updateProductInfo() {
        // 检查值是否合法
        if (!this.checkValue(this.editObj)) {
          return
        }
        // 如果合法就更新产品
        productInfo.updateProductInfo(this.editObj)
          .then(response => {
            this.init()
            this.findPageProductInfo(this.current)
          })
      },

    }
  }
</script>
