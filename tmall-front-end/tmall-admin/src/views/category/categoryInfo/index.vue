<template>
<div>
  <!-- 条件查询分类 -->
  <el-form :inline="true" :model="searchObj" class="demo-form-inline" style="margin: 20px 0px 0px 10px">
    <el-form-item label="分类名称">
      <el-input v-model="searchObj.name" placeholder="分类名称"></el-input>
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
      <el-button style="margin-left: 10px" type="primary" @click="findPageCategoryInfo(1)">查询</el-button>
    </el-form-item>
    <!-- 批量删除按钮 -->
    <div style="float: right; margin-right: 10px">
      <el-button :disabled="delDisable" @click="batchRemove" type="danger" plain>批量删除</el-button>
    </div>
  </el-form>

  <!-- 显示分类 -->
  <el-table
    ref="multipleTable"
    :data="list"
    tooltip-effect="dark"
    style="width: 100%"
    :row-style="{height: '80px'}"
    @selection-change="handleSelectionChange">
    <el-table-column
      type="selection"
      width="55">
    </el-table-column>
    <el-table-column
      label="序号"
      width="55">
      <template slot-scope="scope">{{ (current - 1) * limit + scope.$index + 1 }}</template>
    </el-table-column>
    <el-table-column
      label="图片"
      show-overflow-tooltip>
      <template slot-scope="scope">
        <img :src="scope.row.imageUrl" width="380">
      </template>
    </el-table-column>
    <el-table-column
      prop="name"
      label="分类名称"
      width="200"
      align="center">
    </el-table-column>
    <el-table-column
      label="属性管理"
      width="100"
      align="center">
      <template slot-scope="scope">
        <el-tooltip class="item" effect="dark" content="属性管理" placement="top">
          <router-link @click.native="saveName(scope.row.name)" :to="'/category/property/' + scope.row.id"> <!--跳转到隐藏路由-->
            <el-link :underline="false"><i style="font-size: 15px" class="el-icon-receiving"></i></el-link>
          </router-link>
        </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column
      label="产品管理"
      width="100"
      align="center">
      <template slot-scope="scope">
        <router-link @click.native="saveName(scope.row.name)" :to="'/product/' + scope.row.id">
          <el-tooltip class="item" effect="dark" content="产品管理" placement="top">
            <el-link :underline="false"><i style="font-size: 15px" class="el-icon-shopping-cart-2"></i></el-link>
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
          <el-link @click="showDialog(scope.row.id, scope.row.name)" :underline="false"><i style="font-size: 15px" class="el-icon-edit"></i></el-link>
        </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column
      label="删除"
      width="80"
      align="center">
      <template slot-scope="scope">
        <el-tooltip class="item" effect="dark" content="删除分类" placement="top">
          <el-link  @click="remove(scope.row.id)" :underline="false"><i style="font-size: 15px" class="el-icon-delete"></i></el-link>
        </el-tooltip>
      </template>
    </el-table-column>
  </el-table>

  <!-- 分类分页 -->
  <div style="margin-top: 20px">
    <el-pagination
      :page-size="limit"
      @current-change="findPageCategoryInfo"
      align="center"
      :current-page="current"
      background
      layout="prev, pager, next"
      :total="total">
    </el-pagination>
  </div>

  <!-- 添加分类 -->
  <div style="margin: 40px;">
    <el-form style="width:400px; margin:0 auto; border: #FAEBCC 1px solid; border-radius: 7px;"
    align="center"
    label-width="80px"
    :model="categoryObj">
      <div style="background-color: #FCF8E3; color: #8A6D3B; line-height: 40px">
        <span>分类添加</span>
      </div>
      <el-form-item label="分类名称" style="margin: 10px">
        <el-input v-model.trim="categoryObj.name"></el-input>
      </el-form-item>
      <el-form-item label="分类图片" style="margin: 10px">
        <el-upload
          style="float: left"
          class="avatar-uploader"
          :action="fileUrl"
          :show-file-list="false"
          :on-success="onUploadSuccess">
          <el-button size="small" type="Info">点击上传</el-button>
          <span style="margin-left: 10px" slot="tip" class="el-upload__tip">{{ uploadHint }}</span>
        </el-upload>
      </el-form-item>
      <!--提交按钮-->
      <el-button style="margin-bottom: 10px" type="success" @click="addCategory">提交</el-button>
    </el-form>
  </div>

  <!-- 分类编辑模态框 -->
  <el-dialog
    title="编辑分类"
    :visible.sync="dialogVisible"
    @close="editObj.imageUrl = null"
    width="450px">
      <!-- 编辑分类 -->
      <el-form style="width:400px; margin:0 auto;"
               align="center"
               label-width="80px"
               :model="editObj">
        <el-form-item label="分类名称" style="margin: 10px">
          <el-input v-model.trim="editObj.name" @input="onInput()"></el-input>
        </el-form-item>
        <el-form-item label="分类图片" style="margin: 10px">
          <el-upload
            style="float: left"
            class="avatar-uploader"
            :action="fileUrl"
            :show-file-list="false"
            :on-success="onUploadSuccess">
            <el-button size="small" type="Info">点击上传</el-button>
            <span style="margin-left: 10px" slot="tip" class="el-upload__tip">{{ uploadHint }}</span>
          </el-upload>
        </el-form-item>
        <!--提交按钮-->
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button style="margin-bottom: 10px" type="success" @click="updateCategoryInfo">提交</el-button>
      </el-form>
  </el-dialog>

</div>
</template>

<script>
  import categoryInfo from '@/api/category/categoryInfo'
  import strUtil from "@/utils/myUtil/strUtil"
  import cookies from 'js-cookie'

  export default {
    data() {
      return {
        list: [], // 查询结果data
        searchObj: {}, // 条件查询对象
        current: 1, //起始页默认为1
        limit: 5, // 每页显示数据条数，默认5条
        total: null, // 总记录数
        fileUrl: 'http://localhost/admin/category/categoryInfo/saveImage', // 添加图片的url地址
        categoryObj: {}, //要添加的分类对象
        uploadHint: "只能上传jpg/png文件，且不超过10MB", // 文件上传提示
        multipleSelection: [], // 选择数组
        delDisable: true, // 删除按钮是否可用  :disable 内的true 和 false 参数是字符串
        dialogVisible: false, // 模态框默认不显示
        editObj: {} // 分类编辑对象
      }
    },

    created() {
        this.findPageCategoryInfo(1)
    },

    methods: {
      // 分页条件显示
      findPageCategoryInfo(val){
        this.current = val
        categoryInfo.findPageCategoryInfo(this.current, this.limit, this.searchObj)
          .then(response => {
              this.list = response.data.records
              this.total = response.data.total
          })
      },

      // 初始化值
      init(){
        this.limit = 5
        this.uploadHint = "只能上传jpg/png文件，且不超过10MB"
        this.categoryObj = {}
        this.searchObj = {}
        this.editObj = {}
        this.delDisable = true // 设置按钮不可用
        this.dialogVisible = false // 隐藏模态窗口
      },

      // 分类图片上传成功
      onUploadSuccess(response){
        if(response.code !== 200) {
          this.$message.error("上传失败")
          return
        }
        // 填充上传文件列表
        if (this.editObj.imageUrl != null){ // 如果编辑对象不为空则说明在编辑框，那就更新编辑对象的imageUrl
          this.editObj.imageUrl = response.data.imageUrl
        } else { // 否则是在添加框提交的图片上传
          this.categoryObj.imageUrl = response.data.imageUrl
        }
        this.uploadHint = "文件已上传  （" + response.data.filename + "）"
      },

      // 添加分类
      addCategory(){
        if (strUtil.isEmpty(this.categoryObj.name)) {
          this.$message.warning("分类名称不能为空！")
          return
        }
        categoryInfo.save(this.categoryObj)
          .then(response => {
            // 清空上传记录
            this.init()
            // 刷新页面
            this.findPageCategoryInfo(this.current)
          })
      },

      // 删除分类
      remove(id){
        this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          categoryInfo.remove(id)
            .then(response => {
              this.findPageCategoryInfo(1)
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
          categoryInfo.batchRemove(idList)
            .then(response => {
              this.findPageCategoryInfo(1)
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

      // 显示模态框
      showDialog(id, name){
        this.editObj = {}
        this.editObj.id = id
        this.editObj.name = name
        this.editObj.imageUrl = "" // 给imageUrl设一个空串，代表有可能再编辑框进行图片上传
        this.dialogVisible = true
      },

      // 更新分类
      updateCategoryInfo(){
        if (strUtil.isEmpty(this.editObj.name)) {
          this.$message.warning("分类名称不能为空！")
          return
        }
        categoryInfo.updateCategoryInfo(this.editObj)
          .then(response => {
            this.init()
            this.findPageCategoryInfo(this.current)
          })
      },

      // 输入时强制刷新，解决input嵌套过深无法刷新
      onInput(){
        this.$forceUpdate();
      },

      // 保存分类名，做面包屑导航
      saveName(name) {
        cookies.set("categoryName", name)
      }

    }
  }
</script>
