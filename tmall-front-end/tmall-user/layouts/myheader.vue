<template>
  <div>
    <!--这里放nav-->
    <nav class="top">
      <div class="navTopDiv">
        <a href="/">
          <span class="glyphicon glyphicon-home redColor"></span>天猫首页
        </a>
        <span>喵，欢迎来天猫</span>
        <router-link :to="userInfo == null ? '/login' : '/accountSet'" ref="name">{{userInfo == null ? "请登录" : userInfo.name}}</router-link>
        <a v-show="userInfo" @click="quit" href="javascript:void(0)">退出</a>
        <router-link v-show="!userInfo" to="/regist">免费注册</router-link>
        <span class="pull-right">
            <a :href="userInfo == null ? '/login' : '/order/' + userInfo.id">我的订单</a>
            <a :href="userInfo == null ? '/login' : '/orderItem/' + userInfo.id">
                <span class="glyphicon glyphicon-shopping-cart redColor"></span>购物车<strong>{{userInfo == null ? 0 : userInfo.productNumber == null ? 0 : userInfo.productNumber }}</strong>件
            </a>
        </span>
      </div>
    </nav>

    <el-dialog
      @open="dialogOpen"
      @closed="dialogClosed"
      :visible.sync="dialogVisible"
      :before-close="handlerClose"
      :show-close="false"
      width="330px">
      <template slot="title"><div style="height: 0px; font-size: 16px; font-weight: bold">在线验证</div></template>
      <div style="text-align: center">喝口水休息一会儿再回来吧</div>
      <div style="height: 21px; margin: 5px 0px 10px">
        <el-alert v-if="msgIsShow" :title="hint" :closable="false" type="warning" center show-icon>
        </el-alert>
      </div>
      <div>
        <el-slider v-model="slider" :show-tooltip="false" @change="onlineConfirm"></el-slider>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import header from "../api/header";
import storage from "../assets/js/storage";

export default {
  data() {
    return {
      userInfo: null, // 用户信息
      slider: 0, // 滑块的值
      count: 3, // 滑动错误计数
      dialogVisible: false,
      hint: "", // 提示
      msgIsShow: false, // 提示默认不显示
      confirmTimer: null, // 在线确认计时器
      confirmTime: 0, // 在线确认时间
    }
  },
  name: "myheader",
  mounted() {
    this.getUserInfo()
    if (storage.getItem("token")) {
      this.onlineConfirmTimer()
    }

    //监听刷新事件
    window.addEventListener('beforeunload', e => this.beforeunloadHandler(e))
    // 在线验证弹框出现时阻止F5刷新
    this.stopF5Refresh()
  },
  watch: {
    $route() {
      this.getUserInfo()
      if (storage.getItem("token")) {
        this.onlineConfirmTimer()
      }
    },
  },
  created() {
  },
  methods: {
    // 空方法，避免模态框被关闭
    handlerClose() {},

    // 初始化一些值
    init() {
      this.count = 0
      this.slider = 0
      this.dialogVisible = false
      this.msgIsShow = false
    },

    // 浏览器刷新事件，如果是在线验证时刷新则清空localStorage
    beforeunloadHandler (e) {
      if (this.confirmTimer) {
        storage.removeLoginStatus()
      }
    },

    // 在线验证弹框出现时阻止F5刷新
    stopF5Refresh() {
      let my = this
      document.onkeydown = function (e) {
        let evt = window.event || e;
        let code = evt.keyCode || evt.which;
        if (code == 116) {
          if (my.dialogVisible) {
            evt.preventDefault(); // 取消默认刷新行为
          } else { // 允许刷新
            window.location.reload()
          }
        }
      }
    },

    // 模态框关闭
    dialogClosed() {
      clearInterval(this.confirmTimer) // 清除确认计时器
      this.confirmTimer = null
    },

    // 重新登录
    reLogin() {
      storage.removeLoginStatus() // 移除localStorage所有项
      clearInterval(this.confirmTimer) // 清除确认计时器
      this.dialogVisible = false // 关闭模态框，也是为了清空计时器
      window.location.href = "/login"
    },

    // 模态框打开
    dialogOpen() {
      this.confirmTime = 60*5 // 设置5分钟给用户在线确认，若超过5分钟用户未确认则视为离线
      let my = this
      this.confirmTimer = setInterval(function () {
        --my.confirmTime
        if (my.confirmTime == 0) { // 超时未确认，重新登录
          my.$message.error("确认超时，请重新登录")
          my.reLogin()
        }
      },1000)
    },

    // 在线状态确认
    onlineConfirm() {
      if (this.slider != 100) {
        --this.count
        if (this.count == 0) {
          this.$message.error("请重新登录")
          this.reLogin()
        }
        this.hint = "请把滑块拖至最右方，还剩" + this.count + "次机会！"
        this.msgIsShow = true
        this.slider = 0
        this.confirmTime = 60*5 // 用户滑动了滑块，确认时间重置
      } else { // 验证成功，从新获取userInfo
        this.$message.success("欢迎回来！")
        this.init()
        this.getUserInfo()
        this.onlineConfirmTimer()
      }
    },

    // 30分钟计时，到时间后会弹出用户在线确认模态框
    onlineConfirmTimer() {
      let time = 30*60
      let timer = setInterval(() => {
        if (--time == 0) {
          this.dialogVisible = true
          clearInterval(timer)
        }
      }, 1000)
    },

    // 获取用户信息
    getUserInfo() {
      if (storage.getItem("token")){
        // 从后端获取
        header.getUserInfo()
          .then(response => {
            this.userInfo = response.data
            // 存入localStorage中
            storage.setItem("userInfo", this.userInfo)
            storage.setItem("toDataLoad", true, -1)
          })
      }
    },

    // 用户退出
    quit() {
      storage.removeItem("token")
      storage.removeItem("userInfo")
      this.$router.push("/login")
    }

  }
}
</script>

<style scoped>

</style>
