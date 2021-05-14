<template>
  <div>
    <!--这里放登录页面主体部分-->
    <div class="loginPage" id="loginPage">
      <a class="simpleLogo" href="#"><img src="~assets/img/site/simpleLogo.png" height="27" width="190"/></a>
      <div style="clear: both"></div>
      <div class="mainBodyDiv">
        <div class="loginInputBox">
          <div class="title" v-text="loginTitle"></div>
          <div id="account_login_div" v-if="loginWay">
            <table>
              <tr>
                <td>
                  <span class="glyphicon glyphicon-user"></span>
                </td>
                <td>
                  <input type="text" v-model="account" @keyup="checkSendBtn" placeholder="会员名/手机号/电子邮箱">
                </td>
              </tr>
              <tr>
                <td></td>
              </tr>
              <tr>
                <td>
                  <span class="glyphicon glyphicon-lock"></span>
                </td>
                <td>
                  <input type="password" v-model="password" @keyup="checkSendBtn" placeholder="请输入用户密码">
                </td>
              </tr>
            </table>
            <div id="email_code">
                <span>
                  <input type="text" v-model="emailCode" placeholder="请输入验证码">
                </span>
                <span>
                  <button id="send_code_btn" :disabled="isDisabled" @click="sendEmailCode" v-text="sendBtnText"></button>
                </span>
            </div>
            <div style="clear: both"></div>
            <div class="hint">不要输入真实的天猫账号密码</div>
            <div class="otherOperating">
              <span class="forgetLoginPassword"><a href="#">忘记登录密码</a></span>
              <span>
                <span class="wechat_login"><a href="javascript:void(0)" @click="creatQRCode">微信登录</a></span>
                <span class="freeRegister"><a href="/regist">免费注册</a></span>
              </span>
              <div style="clear: both"></div>
            </div>
            <button id="login_btn" @click="login">登录</button>
          </div>
          <div id="weChat_login_div" v-if="!loginWay">
            <div>
              <div style="margin: 50px auto; height: 200px; width: 200px" class="qrcode" ref="qrCodeUrl"></div>
            </div>
            <div style="margin-top: 20px; padding-left: 30px; padding-right: 30px; width: 100%">
              <span style="float:left;" class="freeRegister"><a href="/regist">免费注册</a></span>
              <span style="float: right; " class="wechat_login"><a href="javascript:void(0)" @click="accountLogin">账号登录</a></span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import md5Util from "../../assets/js/md5Util";
import base64Util from "../../assets/js/base64Util";
import login from "../../api/login";
import qrcode from 'qrcodejs2'
import cookie from "js-cookie"
import storage from "../../assets/js/storage";

export default {
  name: "index",
  data() {
    return {
      userLogin: {}, // 用户登录
      isDisabled: true, // 发送验证码按钮不可用
      account: null, // 用户名
      password: null, // 密码
      sendBtnText: "发送验证码", // 发送按钮文本
      isSend: false, // 验证码是否已发送
      token: null, // token
      emailCode: null, // 邮箱验证码
      loginTitle: "账户登录", // login title文本
      loginWay: true, // 登录方式，true账号登录，false微信登录
      uuid: null, // 拿取token的凭证
      qrcodeUrl: null, // 二维码地址
      state: null, // 账号状态
      interval: null, // 微信登录轮询
    }
  },
  created() {

  },
  methods: {
    // 用户登录
    login() {
      // 封装登录信息
      if (!this.packLoginInfo()) {
        return
      }
      if (this.emailCode == null || this.emailCode == "") {
        this.$message.warning("请输入验证码")
        return
      }
      login.userLogin(this.userLogin)
        .then(response => {
          this.token = response.data
          storage.setItem("token", this.token, -1)
          // window.location.href = "/"
          this.$router.push("/")
        })
    },

    // 封装登录信息
    packLoginInfo() {
      let account = this.account
      let password = this.password
      if (account == null || password == null) {
        this.$message.warning("请输入用户名和密码")
        return false
      }
      account = account.trim()
      if(account === "") {
        this.$message.warning("请输入用户名和密码")
        return false
      }
      // 对用户名进行base64编码
      this.userLogin.account = base64Util.encode(account)
      // 对验证码进行base64编码
      this.userLogin.emailCode = base64Util.encode(this.emailCode)
      // 对用户密码进行MD5加密
      this.userLogin.password = md5Util.encrypt(password)
      return true
    },

    // 发送邮箱验证码
    sendEmailCode() {
      // 封装登录信息
      if (!this.packLoginInfo()) {
        return
      }
      login.sendEmailCode(this.userLogin)
        .then(response => {
          // 设置发送状态为已发送
          this.isSend = true
          // 发送成功，提示发送成功
          this.$message.success("验证码已发送，有效时间十分钟，请注意查收！")
          // 发送按钮不可用
          this.isDisabled = true
          $("#send_code_btn").css({"background-color": "#CBCBCB", "color": "#333333"})
          // 发送按钮显示倒计时
          let i = 60
          this.sendBtnText = "已发邮箱 " + i
          let time = setInterval(() => {
            i--
            this.sendBtnText = "已发邮箱 " + i
            if (i == 0) {
              this.isSend = false
              this.isDisabled = false
              this.sendBtnText = "重新发送"
              $("#send_code_btn").css({"background-color": "#C40000", "color": "white"})
              clearInterval(time)
            }
          }, 1000)
        })

    },

    // 检测发送验证码按钮是否可用
    checkSendBtn() {
      // 如果发了验证码，这个方法就不可用
      if (this.isSend) {
        return
      }
      let account = this.account
      let password = this.password
      if (account == null || password == null) {
        this.isDisabled = true // 设置发送验证码按钮不可用
        $("#send_code_btn").css({"background-color": "#CBCBCB", "color": "#333333"})
        return
      }
      account = account.trim()
      if(account === "") {
        this.isDisabled = true // 设置发送验证码按钮不可用
        $("#send_code_btn").css({"background-color": "#CBCBCB", "color": "#333333"})
        return
      }
      this.isDisabled = false // 设置发送验证码按钮可用
      $("#send_code_btn").css({"background-color": "#C40000", "color": "white"})
    },

    // 账号登录
    accountLogin() {
      this.loginTitle = "账号登录"
      this.loginWay = true
      if (this.interval != null) {
        clearInterval(this.interval)
      }
    },

    // 获取二维码url
    weChatQRCode(val) {
      login.weChatQRCode(0) // type为0表示登录二维码 为1表示微信绑定二维码
        .then(response => {
          this.qrcodeUrl = response.data.QRCodeUrl
          this.uuid = response.data.uuid
          new qrcode(this.$refs.qrCodeUrl, {
            text: this.qrcodeUrl, // 需要转换为二维码的内容
          })
        })
    },

    // 创建二维码
    creatQRCode(val) {
      this.loginTitle = "扫码登录"
      this.loginWay = false
      // 创建二维码
      this.$nextTick(() => {
        this.weChatQRCode(val)
      })

      let time = 60;
      // 轮询
      this.interval = setInterval(() => {
        if (--time == 0) {
          // 每隔60秒刷新一次二维码
          this.$nextTick(() => {
            this.weChatQRCode()
          })
          time = 60
        }
        // 向后端发送请求查询用户是否扫码
        login.polling(this.uuid)
          .then(response => {
            if (response.data != null) {
              this.state = response.data.state
              this.token = response.data.token
              clearInterval(this.interval)
              if (this.state == 520) { // 邮箱未绑定，跳转到注册页面
                storage.setItem("tempToken", this.token) // 设置为临时token
                // cookie.set("tempToken", this.token)
                // window.location.href = "/regist"
                this.$router.push("/regist")
              } else { // 否则跳转到首页
                storage.setItem("token", this.token, -1) // 设置token生命周期为半小时（默认不设置ttl为半小时）
                // cookie.set("token", this.token)
                // window.location.href = "/"
                this.$router.push("/")
              }
            }
          })
      }, 1000)
    }


  }
}
</script>

<style scoped>

</style>
