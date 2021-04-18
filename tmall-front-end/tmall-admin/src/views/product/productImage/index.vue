<template>
  <div>
    <div style="width: 100%; text-align: center; padding-top: 50px; min-width: 1024px">
      <!-- 左边的div -->
      <div id="leftDiv">
        <!-- 添加图片 -->
        <div>
          <el-form style="width:400px; border: #FAEBCC 1px solid; border-radius: 7px;"
                   align="center"
                   label-width="80px">
            <div style="background-color: #FCF8E3; color: #8A6D3B; line-height: 40px">
              <span>新增产品缩略图</span>
            </div>
            <div style="margin-top: 20px; font-size: 14px">
              <span>请选择本地图片 尺寸400X400 为佳</span>
            </div>
            <el-form-item label="产品图片" style="margin: 10px">
              <el-upload
                style="float: left"
                class="avatar-uploader"
                :action="uploadUrl + '/0'"
                name="files"
                :multiple="true"
                :on-success="show">
                <el-button size="small" type="Info">点击上传</el-button>
                <span style="margin-left: 10px" slot="tip" class="el-upload__tip"> 只能上传jpg/png文件，可批量上传 </span>
              </el-upload>
            </el-form-item>
          </el-form>
        </div>
        <!-- 批量删除按钮 -->
        <div style="margin: 20px 0 20px;" align="left">
          <el-button :disabled="delDisableA" @click="batchRemove('A')" type="danger" plain>批量删除</el-button>
        </div>
        <!-- 显示图片 -->
        <el-table
          :data="thumbnail"
          stripe
          style="width: 100%;"
          :header-cell-style="{background: '#DFF0D8', fontSize:'14px', color:'black', fontWeight:'700'}"
          :row-style="{height: '65px'}"
          :cell-style="{padding: '0'}"
          size="small"
          @selection-change="handleSelectionChangeA">
          <el-table-column
            type="selection"
            width="55">
          </el-table-column>
          <el-table-column
            label="序号"
            width="55">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="产品单个图片缩略图"
            width="200"
            align="center">
            <template slot-scope="scope">
              <img :src="scope.row.url" width="50">
            </template>
          </el-table-column>
          <el-table-column
            label="删除"
            align="center">
            <template slot-scope="scope">
              <el-tooltip class="item" effect="dark" content="删除图片" placement="top">
                <el-link @click="remove(scope.row.id)" :underline="false"><i style="font-size: 15px" class="el-icon-delete"></i></el-link>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 右边的div -->
      <div id="rightDiv">
        <!-- 添加图片 -->
        <div>
          <el-form style="width:400px; border: #FAEBCC 1px solid; border-radius: 7px; margin-bottom: 40px"
                   align="center"
                   label-width="80px">
            <div style="background-color: #FCF8E3; color: #8A6D3B; line-height: 40px">
              <span>新增产品详情图</span>
            </div>
            <div style="margin-top: 20px; font-size: 14px">
              <span>请选择本地图片 宽度790 为佳</span>
            </div>
            <el-form-item label="产品图片" style="margin: 10px">
              <el-upload
                style="float: left"
                class="avatar-uploader"
                :action="uploadUrl + '/1'"
                name="files"
                :multiple="true"
                :on-success="show">
                <el-button size="small" type="Info">点击上传</el-button>
                <span style="margin-left: 10px" slot="tip" class="el-upload__tip"> 只能上传jpg/png文件，可批量上传 </span>
              </el-upload>
            </el-form-item>
          </el-form>
        </div>
        <!-- 批量删除按钮 -->
        <div style="margin: 20px 0 20px;" align="left">
          <el-button :disabled="delDisableB" @click="batchRemove('B')" type="danger" plain>批量删除</el-button>
        </div>
        <!-- 显示图片 -->
        <el-table
          :data="details"
          stripe
          style="width: 100%;"
          :header-cell-style="{background: '#DFF0D8', fontSize:'14px', color:'black', fontWeight:'700'}"
          :row-style="{height: '65px'}"
          :cell-style="{padding: '0'}"
          size="small"
          @selection-change="handleSelectionChangeB">
          <el-table-column
            type="selection"
            width="55">
          </el-table-column>
          <el-table-column
            label="序号"
            width="55">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="产品详情图片缩略图"
            width="200"
            align="center">
            <template slot-scope="scope">
              <img :src="scope.row.url" width="100">
            </template>
          </el-table-column>
          <el-table-column
            label="删除"
            align="center">
            <template slot-scope="scope">
              <el-tooltip class="item" effect="dark" content="删除图片" placement="top">
                <el-link @click="remove(scope.row.id)" :underline="false"><i style="font-size: 15px" class="el-icon-delete"></i></el-link>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import productImage from "@/api/product/productImage";

const baseUploadUrl = "http://localhost/admin/product/productImage/batchUpload/" // 图片上传地址
  export default {
    data() {
      return {
        thumbnail: [], // 产品缩略图
        details: [], // 产品详情图
        productId: null, // 产品id
        uploadUrl: null, // 图片上传地址
        multipleSelectionA: null, // 多选
        multipleSelectionB: null, // 多选
        delDisableA: true, // 批量删除按钮初始状态
        delDisableB: true, // 批量删除按钮初始状态
      }
    },
    created() {
      this.productId = this.$route.params.id
      this.uploadUrl = baseUploadUrl + this.productId
      this.show()
    },
    methods: {
      // 显示产品图片
      show() {
        productImage.show(this.productId)
          .then(response => {
              this.thumbnail = response.data.thumbnail
              this.details = response.data.details
          })
      },

      // 记录选择
      handleSelectionChangeA(val) {
        this.multipleSelectionA = val;
        if (this.multipleSelectionA.length != 0) {
          this.delDisableA = false
        } else {
          this.delDisableA = true
        }
      },
      // 记录选择
      handleSelectionChangeB(val) {
        this.multipleSelectionB = val;
        if (this.multipleSelectionB.length != 0) {
          this.delDisableB = false
        } else {
          this.delDisableB = true
        }
      },

      // 解析multipleSelection获得idList
      getIdList(val){
        let idList = []
        if (val === 'A'){
          this.multipleSelectionA.forEach(function (value) {
            idList.push(value.id)
          })
        } else if (val === 'B'){
          this.multipleSelectionB.forEach(function (value) {
            idList.push(value.id)
          })
        }
        return idList
      },

      // 批量删除
      batchRemove(val) {
        this.$confirm('此操作将永久删除选中记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 取得idList调用后端删除记录
          let idList = this.getIdList(val)
          productImage.batchRemove(idList)
            .then(response => {
              this.multipleSelection = null
              this.show()
            })
            .catch(error => {
              this.$message.warning("删除失败！")
            })
        })
      },

      // 删除图片
      remove(id) {
        this.$confirm("此操作将永久删除选中记录, 是否继续？", "提示",  {
          confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
          productImage.remove(id)
            .then(response => {
              this.show()
            })
            .catch(error => {
              this.$message.error("删除失败！")
            })
        })
      },


    }
  }
</script>

<style>
  #rightDiv {
    display: inline-block;
    width: 400px;
    margin-left: 100px;
  }

  #leftDiv {
    display: inline-block;
    width: 400px;
    vertical-align: top;
    margin-right: 100px;
  }

</style>
