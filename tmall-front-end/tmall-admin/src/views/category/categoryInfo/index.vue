<template>
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
      label="日期"
      width="120">
      <template slot-scope="scope">{{ scope.row.date }}</template>
    </el-table-column>
    <el-table-column
      prop="name"
      label="姓名"
      width="120">
    </el-table-column>
    <el-table-column
      prop="address"
      label="地址"
      show-overflow-tooltip>
    </el-table-column>
  </el-table>
</template>

<script>
  import categoryInfo from '@/api/category/categoryInfo'

  export default {
    data() {
      return {
        list: [], // 查询结果data
        searchObj: {}, // 条件查询对象
        current: 1, //起始页默认为1
        limit: 5, // 每页显示数据条数，默认5条

        multipleSelection: [] // 选择数组
      }
    },

    current() {
        this.findPageProductInfo()
    },

    methods: {
      // 分页条件显示
      findPageProductInfo(){
          categoryInfo.findPageProductInfo(this.current, this.limit, this.searchObj)
            .then(response => {
                this.list = response.data
            })
      },

      // 记录选择     
      handleSelectionChange(val) {
        this.multipleSelection = val;
      }
    }
  }
</script>