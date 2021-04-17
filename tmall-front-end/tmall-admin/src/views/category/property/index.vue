<template>
  <div>
    <!-- 属性查询 -->
    <el-form :inline="true" :model="searchObj" class="demo-form-inline" style="margin: 20px 0px 0px 10px">
      <el-form-item label="属性名称">
        <el-input v-model="searchObj.name" placeholder="属性名称"></el-input>
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
        <el-button style="margin-left: 10px" type="primary" @click="findPageProperty(1)">查询</el-button>
      </el-form-item>
      <!-- 批量删除按钮 -->
      <div style="float: right; margin-right: 10px">
        <el-button :disabled="delDisable" @click="batchRemove" type="danger" plain>批量删除</el-button>
      </div>
    </el-form>


    <!-- 属性显示 -->
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
        width="300">
        <template slot-scope="scope">{{ (current - 1) * limit + scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column
        prop="name"
        label="属性名称"
        show-overflow-tooltip>
      </el-table-column>
      <el-table-column
        label="编辑"
        width="300"
        align="center">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="编辑属性" placement="top">
            <el-link @click="showDialog(scope.row.id, scope.row.name)" :underline="false"><i style="font-size: 15px" class="el-icon-edit"></i></el-link>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        label="删除"
        width="300"
        align="center">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="删除属性" placement="top">
            <el-link  @click="remove(scope.row.id)" :underline="false"><i style="font-size: 15px" class="el-icon-delete"></i></el-link>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>

    <!-- 属性分页 -->
    <div style="margin-top: 20px">
      <el-pagination
        :page-size="limit"
        @current-change="findPageProperty"
        align="center"
        :current-page="current"
        background
        layout="prev, pager, next"
        :total="total">
      </el-pagination>
    </div>

    <!-- 添加属性 -->
    <div style="margin: 40px;">
      <el-form style="width:400px; margin:0 auto; border: #FAEBCC 1px solid; border-radius: 7px;"
               align="center"
               label-width="80px"
               :model="propertyObj">
        <div style="background-color: #FCF8E3; color: #8A6D3B; line-height: 40px">
          <span>新增属性</span>
        </div>
        <el-form-item label="属性名称" style="margin: 10px">
          <el-input v-model.trim="propertyObj.name"></el-input>
        </el-form-item>
        <!--提交按钮-->
        <el-button style="margin-bottom: 10px" type="success" @click="addProperty">提交</el-button>
      </el-form>
    </div>

    <!-- 修改属性 -->
    <el-dialog
      title="编辑属性"
      :visible.sync="dialogVisible"
      width="450px">
      <!-- 编辑属性 -->
      <el-form style="width:400px; margin:0 auto; border: #FAEBCC 1px solid; border-radius: 7px;"
               align="center"
               label-width="80px"
               :model="editObj">
        <div style="background-color: #FCF8E3; color: #8A6D3B; line-height: 40px">
          <span>编辑属性</span>
        </div>
        <el-form-item label="属性名称" style="margin: 10px">
          <el-input v-model.trim="editObj.name" @input="onInput()"></el-input>
        </el-form-item>
        <!--提交按钮-->
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button style="margin-bottom: 10px" type="success" @click="updateProperty">提交</el-button>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
  import property from '@/api/category/property'
  import strUtil from "@/utils/myUtil/strUtil";

  export default {
    data() {
      return {
        categoryId: null, // 分类id
        current: 1, // 起始页
        limit: 5, // 每页记录数
        total: null, // 总记录数
        searchObj: {}, // 查询对象
        list: [], // 查询结果list
        propertyObj: {}, // 属性对象
        idList: [], // 批量删除的属性id
        delDisable: true, // 批量删除按钮可用状态
        dialogVisible: false, // 模态框默认不显示
        editObj: {}, // 属性编辑对象
      }
    },

    created() {
      this.categoryId = this.$route.params.id
      this.findPageProperty(1)
    },

    methods: {
      // 分页显示属性
      findPageProperty(val){
        this.current = val
        this.searchObj.categoryId = this.categoryId
        property.findPageProperty(this.current, this.limit, this.searchObj)
          .then(response => {
            this.list = response.data.records
            this.total = response.data.total
          })
      },

      // 初始化值
      init(){
        this.searchObj = {}
        this.propertyObj = {}
        this.idList = []
        this.delDisable = true
        this.dialogVisible = false
      },

      // 添加属性
      addProperty() {
        if (strUtil.isEmpty(this.propertyObj.name)) {
          this.$message.warning("属性名不能为空")
          return
        }
        this.propertyObj.categoryId = this.categoryId
        property.save(this.propertyObj)
          .then(response => {
            this.init() // 清空缓存值
            this.findPageProperty(this.current) // 刷新列表
          })
      },

      // 删除属性
      remove(id) {
        this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          property.remove(id)
            .then(response => {
              this.findPageProperty(1)
            })
            .catch(error => {
              this.$message.warning("删除失败！")
            })
        })
      },

      // 批量删除
      batchRemove(){
        this.$confirm('此操作将永久删除选中记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 取得idList调用后端删除记录
          let idList = this.getIdList()
          property.batchRemove(idList)
            .then(response => {
              this.findPageProperty(1)
            })
            .catch(error => {
              this.$message.warning("删除失败！")
            })
        })
      },

      // 解析multipleSelection获得idList
      getIdList(){
        let idList = []
        this.multipleSelection.forEach(function (value) {
          idList.push(value.id)
        })
        return idList
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

      // 打开编辑属性模态框
      showDialog(id, name) {
        this.editObj = {}
        this.editObj.name = name
        this.editObj.id = id
        this.dialogVisible = true
      },

      // 更新编辑后的属性
      updateProperty() {
        if (strUtil.isEmpty(this.editObj.name)) {
          this.$message.warning("属性名不能为空")
          return
        }
        property.update(this.editObj)
          .then(response => {
            this.init()
            this.findPageProperty(this.current)
          })
      },

      // 输入时强制刷新，解决input嵌套过深无法刷新
      onInput(){
        this.$forceUpdate();
      }

    }

  }
</script>
