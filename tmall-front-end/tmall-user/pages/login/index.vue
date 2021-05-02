<template>
  <div>
    <!--这里放登录页面主体部分-->
    <div class="loginPage" id="loginPage">
      <a class="simpleLogo" href="#"><img src="~assets/img/site/simpleLogo.png" height="27" width="190"/></a>
      <div style="clear: both"></div>
      <div class="mainBodyDiv">
        <div class="loginInputBox">
          <div class="title">账户登录</div>
          <table>
            <tr>
              <td>
                <span class="glyphicon glyphicon-user"></span>
              </td>
              <td>
                <input type="text" v-model="account" @keyup="checkSendBtn" placeholder="用户名/手机号/电子邮箱">
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
                <input type="text" v-model="userLogin.emailCode" placeholder="请输入验证码">
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
              <span class="wechat_login"><a href="#">微信登录</a></span>
              <span class="freeRegister"><a href="#">免费注册</a></span>
            </span>
            <div style="clear: both"></div>
          </div>
          <button id="login_btn" @click="login">登录</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import md5Util from "../../assets/js/md5Util";
import base64Util from "../../assets/js/base64Util";
import login from "../../api/login";

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
    }
  },
  created() {

  },
  methods: {
    // 用户登录
    login() {
      // 封装登录信息
      this.packLoginInfo()
      login.userLogin(this.userLogin)
        .then(response => {
          this.token = response.data
          // TODO token以能正常获取，接着做跳转
          alert(this.token)
          console.log(this.token)
        })
    },

    // 封装登录信息
    packLoginInfo() {
      let account = this.account
      let password = this.password
      if (account == null || password == null) {
        this.$message.warning("请输入用户名和密码")
        return
      }
      account = account.trim()
      if(account === "") {
        this.$message.warning("请输入用户名和密码")
        return
      }
      // 对用户名进行base64编码
      this.userLogin.account = base64Util.encode(account)
      // 对用户密码进行MD5加密
      this.userLogin.password = md5Util.encrypt(password)
    },

    // 发送邮箱验证码
    sendEmailCode() {
      // 封装登录信息
      this.packLoginInfo()
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
    }


  }
}
</script>

<style scoped>

</style>
