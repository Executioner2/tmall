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
                <input type="text" placeholder="请输入验证码">
              </span>
              <span>
                <button id="send_code_btn" :disabled="isDisabled" @click="sendEmailCode">发送验证码</button>
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
          <button id="login_btn">登录</button>
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
    }
  },
  created() {

  },
  methods: {
    // 发送邮箱验证码
    sendEmailCode() {
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
      // 用户名解码
      base64Util.decode(this.userLogin.account)
      login.sendEmailCode(this.userLogin)
        .then(response => {
          console.log(response)
        })

    },

    // 检测发送验证码按钮是否可用
    checkSendBtn() {
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
