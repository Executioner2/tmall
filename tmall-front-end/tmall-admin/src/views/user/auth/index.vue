<template>
  <div>
    <!-- 条件查询 -->
    <el-form :inline="true" :model="searchObj" class="demo-form-inline" style="margin: 20px 0px 0px 10px">
      <el-form-item label="用户ID">
        <el-input style="width: 170px" v-model="searchObj.id" placeholder="用户ID"></el-input>
      </el-form-item>
      <el-form-item label="用户关键字">
        <el-input style="width: 170px" v-model="searchObj.keyword" placeholder="用户手机号 昵称 姓名"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button style="margin-left: 10px" type="primary" @click="findPageUserInfo(1)">查询</el-button>
        <el-button @click="reset" type="danger" plain>重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 显示用户 -->
    <el-table
      :data="list"
      border
      style="width: 100%; margin: 10px auto">
      <el-table-column
        label="序号"
        width="55">
        <template slot-scope="scope">{{ (current - 1) * limit + scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column
        prop="name"
        label="姓名"
        width="120">
      </el-table-column>
      <el-table-column
        prop="openid"
        label="微信号"
        width="160">
      </el-table-column>
      <el-table-column
        prop="nickName"
        label="昵称"
        width="160">
      </el-table-column>
      <el-table-column
        prop="phone"
        label="手机号"
        width="270">
      </el-table-column>
      <el-table-column
        prop="email"
        label="邮箱"
        width="270">
      </el-table-column>
      <el-table-column
        label="详情信息"
        show-overflow-tooltip>
        <template slot-scope="scope">
          <el-link :underline="false" @click="checkLink" type="primary">查看</el-link>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        width="200">
        <template slot-scope="scope">
          <el-button @click="authBtn(scope.row.id, 2)" type="primary" size="mini">通过</el-button>
          <el-button @click="authBtn(scope.row.id, -1)" type="danger" size="mini">不通过</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页显示 -->
    <div style="margin-top: 20px">
      <el-pagination
        :page-size="limit"
        @current-change="findPageUserInfo"
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
import userInfo from "@/api/user/userInfo";

export default {
  data() {
    return {
      list: [], // 用户列表集合
      searchObj: {authStatus: 1}, // 用户查询对象
      current: 1, // 起始页
      limit: 5, // 每页记录数
      total: null, // 总记录数
      statusOptions: [{ // 用户锁定状态
        value: 0,
        label: '锁定'
      }, {
        value: 1,
        label: '未锁定'
      }]
    }
  },
  created() {
    this.findPageUserInfo(1)
  },
  methods: {
    // 分页条件显示用户
    findPageUserInfo(val) {
      this.current = val
      userInfo.findPageUserInfo(this.current, this.limit, this.searchObj)
        .then(response => {
          this.list = response.data.records
          this.total = response.data.total
        })
    },

    // 重置
    reset() {
      this.searchObj = {authStatus: 1}
      this.findPageUserInfo(1)
    },

    // 改变用户锁定状态
    authBtn(id, authStatus) {
      userInfo.authUser(id, authStatus)
        .then(response => {
          this.findPageUserInfo(this.current)
        })
    },

    // 查看连接
    // TODO 做用户审核信息详情查看页面
    checkLink() {
      this.$message.info('这个功能暂时没做哦！')
    }

  }
}
</script>
