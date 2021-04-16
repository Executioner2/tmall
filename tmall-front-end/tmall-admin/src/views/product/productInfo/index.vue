<template>
  <div>
    <!-- 搜索产品 -->
    <!-- 批量删除按钮 -->

    <!-- 显示产品 -->
    <!-- 显示分类 -->
    <el-table
      ref="multipleTable"
      :data="list"
      tooltip-effect="dark"
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
        width="55">
        <template slot-scope="scope">
          <img :src="scope.row.params.imageUrl" width="45">
        </template>
      </el-table-column>
      <el-table-column
        prop="name"
        label="分类名称"
        show-overflow-tooltip
        align="center">
      </el-table-column>
      <el-table-column
        label="属性管理"
        width="100"
        align="center">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="属性管理" placement="top">
            <router-link :to="'/category/property/' + scope.row.id"> <!--跳转到隐藏路由-->
              <el-link :underline="false"><i class="el-icon-receiving"></i></el-link>
            </router-link>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        label="产品管理"
        width="100"
        align="center">
        <template slot-scope="scope">
          <router-link :to="'/product/productInfo/' + scope.row.id">
            <el-tooltip class="item" effect="dark" content="产品管理" placement="top">
              <el-link :underline="false"><i class="el-icon-shopping-cart-2"></i></el-link>
            </el-tooltip>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column
        label="编辑"
        width="80"
        align="center">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="编辑分类" placement="top">
            <el-link @click="showDialog(scope.row.id, scope.row.name)" :underline="false"><i class="el-icon-edit"></i></el-link>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        label="删除"
        width="80"
        align="center">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="删除分类" placement="top">
            <el-link  @click="remove(scope.row.id)" :underline="false"><i class="el-icon-delete"></i></el-link>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->

    <!-- 添加产品 -->

    <!-- 更新产品产品 -->

  </div>
</template>

<script>
  import productInfo from "@/api/product/productInfo"

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
      this.findPageProduct(1)
    },

    methods: {
      // 分页显示商品
      findPageProduct(val){
        this.current = val
        this.searchObj.categoryId = this.categoryId
        productInfo.findPageProduct(this.current, this.limit, this.searchObj)
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

    }

  }
</script>
