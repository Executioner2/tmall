<template>
  <div>
    <!--这里放简单搜索栏-->
    <simple-search/>

    <!--这里放注册页面主体-->
    <div style="height: 50px">
      <div id="registerErrorMessageDiv" v-show="visibilityHint">
        <span id="error">{{hintText}}</span>
      </div>
    </div>
    <div class="registerPage" id="registerPage">
      <table>
        <tr>
          <td>
            <span class="hint">设置会员名</span>
          </td>
        </tr>
        <tr>
          <td>登录名</td>
          <td><input @blur="nameCheck" type="text" v-model="name" placeholder="会员名一旦设置成功，无法修改"></td>
        </tr>
        <tr>
          <td>
            <span class="hint">设置登录密码</span>
          </td>
          <td>登录时验证，保护账号信息</td>
        </tr>
        <tr>
          <td>登录密码</td>
          <td><input type="password" @blur="passwordCheck" v-model="password" placeholder="设置你的登录密码"></td>
        </tr>
        <tr class="spaceTr">
          <td colspan="2"></td>
        </tr>
        <tr>
          <td>密码确认</td>
          <td><input type="password" @blur="confirmPasswordCheck" v-model="confirmPassword" placeholder="请再次输入你的密码"></td>
        </tr>
        <tr>
          <td>
            <span class="hint">设置绑定邮箱</span>
          </td>
          <td>密码找回，消息接收</td>
        </tr>
        <tr>
          <td>邮箱地址</td>
          <td><input type="text" @keyup="emailCheckKeyUp" @blur="emailCheck" v-model="email" placeholder="请输入你的电子邮箱地址"></td>
        </tr>
        <tr class="spaceTr">
          <td colspan="2"></td>
        </tr>
        <tr id="send_code_tr">
          <td>
            <button id="send_code_btn" @click="sendEmailCode" :disabled="sendCodeDisable" v-text="sendBtnText"></button>
          </td>
          <td>
            <input type="text" v-model="emailCode" @keyup="emailCodeCheckKeyUp" @blur="emailCodeCheck" :disabled="codeDisable" placeholder="请输入验证码">
          </td>
        </tr>
        <tr class="spaceTr">
          <td colspan="2"></td>
        </tr>
        <tr>
          <td colspan="2">
            <button type="button" :disabled="registerDisable" id="submitBtn" @click="registerSubmit">提 交</button>
          </td>
        </tr>
      </table>
    </div>

  </div>
</template>

<script>
import SimpleSearch from "../../layouts/simpleSearch";
import base64Util from "../../assets/js/base64Util";
import md5Util from "../../assets/js/md5Util";
import register from "../../api/register";
import cookie from "js-cookie";

export default {
  name: "index",
  components: {SimpleSearch},
  data() {
    return {
      name: null, // 用户名
      password: null, // 密码
      email: null, // 邮箱地址
      userInfo: {}, // 用户对象
      visibilityHint: false, // 提示可见，默认隐藏
      hintText: "", // 提示内容
      confirmPassword: null, // 确认密码
      emailCode: null, // 邮箱验证码
      sendCodeDisable: true, // 发送验证码按钮是否可用，初始不可用
      registerDisable: true, // 注册按钮是否可用，初始不可用
      codeDisable: true, // 验证码输入框是否可用，初始不可用
      sendBtnText: "发送验证码", // 发送验证码按钮文本内容
      isSend: false, // 发送状态
      token: null, // 根据token判断是否是微信扫码登录跳转到注册页面的，如果有值则说明是，最终提交就发送绑定请求

    }
  },
  created() {
    this.token = cookie.get("tempToken")
  },
  methods: {
    // 注册提交
    registerSubmit() {
      if (!this.nameCheck()) {
        return
      }
      if (!this.passwordCheck()) {
        return
      }
      if (!this.confirmPasswordCheck()) {
        return
      }
      if (!this.emailCodeCheck() && !this.emailCodeCheckKeyUp()) {
        return
      }
      // 对用户名进行base64编码
      this.userInfo.name = base64Util.encode(this.name);
      // 对邮箱地址进行base64编码
      this.userInfo.email = base64Util.encode(this.email);
      // 对验证码进行base64编码
      this.userInfo.emailCode = base64Util.encode(this.emailCode)
      // 对密码进行MD5加密
      this.userInfo.password = md5Util.encrypt(this.password);
      // 向后端发送绑定请求
      if (this.token != null) {
        register.emailBinding(this.token, this.userInfo)
          .then(response => {
              this.$message.success("绑定成功")
              setTimeout(() => {
                cookie.remove("tempToken") // 移除临时token
                cookie.set("token", response.data)
                window.location.href = "/"
              }, 2000)
            })
      } else {
        // 向后端发送注册请求
        register.userRegister(this.userInfo)
          .then(response => {
            this.$message.success("注册成功")
            setTimeout(() => {
              window.location.href = "/login"
            }, 2000)
          })
      }
    },

    // 用户名合法性检测
    nameCheck() {
      let name = this.name
      if (name == null) {
        this.visibilityHint = true
        this.hintText = "用户名不能为空！"
        return false
      }
      let regex = /^[a-zA-Z0-9_]{3,15}$/
      if (!regex.test(name)) {
        this.visibilityHint = true
        this.hintText = "用户名格式错误，支持大小写字母和下划线，长度在3-15以内！"
        return false
      }
      // 向后端发送查询，检测用户名是否被使用
      return register.userNameRepeatCheck(base64Util.encode(name))
        .then(response => {
          let flag = response.data
          if (flag) { // 如果为真表示用户名重复，不能使用
            this.visibilityHint = true
            this.hintText = "用户名已被使用，请重新取名！"
            return false
          } else {
            // 如果当前错误提示是因用户名引起，则隐藏提示，否则不隐藏
            regex = /用户名/
            if (regex.test(this.hintText)) {
              this.visibilityHint = false
            }
            return true
          }
        })
    },

    // 登陆密码合法性检测
    passwordCheck() {
      let password = this.password
      if (password == null) {
        this.visibilityHint = true
        this.hintText = "登陆密码不能为空！"
        return false
      }
      // 密码(以字母开头，长度在6~18之间，只能包含字母、数字和下划线)
      let regex = /^[a-zA-Z]\w{5,17}$/
      if (!regex.test(password)) {
        this.visibilityHint = true
        this.hintText = "登录密码格式错误（以字母开头，长度在6~18之间，只能包含字母、数字和下划线）"
        return false
      }
      // 如果当前提示框是密码问题提示，则隐藏它，否则不隐藏
      regex = /登录密码/
      if (regex.test(this.hintText)) {
        this.visibilityHint = false
      }
      return true
    },

    // 确认密码合法性检测
    confirmPasswordCheck() {
      let confirmPassword = this.confirmPassword
      let password = this.password
      if (confirmPassword == null) {
        this.visibilityHint = true
        this.hintText = "确认密码不能为空！"
        return false
      }
      if (password !== confirmPassword) {
        this.visibilityHint = true
        this.hintText = "确认密码与登陆密码不一致！"
        return false
      }
      // 如果当前提示框是确认密码问题提示，则隐藏它，否则不隐藏
      let regex = /确认密码/
      if (regex.test(this.hintText)) {
        this.visibilityHint = false
      }
      return true
    },

    // 键盘弹起检测
    emailCheckKeyUp() {
      // 如果是发送状态直接返回，不检测
      if (this.isSend) {
        return
      }
      let email = this.email
      let regex = /^\w+([-+.]\w+)*@qq.com$/
      if (regex.test(email)) {
        // 检测邮箱地址是否被使用
        return register.userEmailRepeatCheck(base64Util.encode(email))
          .then(response => {
            let flag = response.data
            if (flag) { // 如果为真，则表示邮箱地址已被使用
              this.visibilityHint = true
              this.hintText = "邮箱地址已被使用，请重新输入！"
              return false
            } else {
              // 如果当前提示框是邮箱问题提示，则隐藏它，否则不隐藏
              regex = /邮箱/
              if (regex.test(this.hintText)) {
                this.visibilityHint = false
              }
              this.sendCodeDisable = false // 设置验证码发送按钮可用
              $("#send_code_btn").css({"background-color": "#C40000", "color": "white"})
              return true
            }
          })
      } else if(!this.sendCodeDisable) {
        this.sendCodeDisable = true // 设置验证码发送按钮不可用
        $("#send_code_btn").css({"background-color": "#CBCBCB", "color": "#333333"})
      }
    },

    // 邮箱合法性检测
    emailCheck() {
      let email = this.email
      if (email == null) {
        this.visibilityHint = true
        this.hintText = "邮箱不能为空！"
        return false
      }
      let regex = /^\w+([-+.]\w+)*@qq.com$/
      if (!regex.test(email)) {
        this.visibilityHint = true
        this.hintText = "邮箱格式错误（暂且仅支持qq邮箱）！"
        return false
      }
    },

    // 发送验证码到邮箱
    sendEmailCode() {
      return register.sendEmailCode(base64Util.encode(this.email))
        .then(response => {
          // 发送状态改为已发送
          this.isSend = true
          // 发送成功，提示发送成功
          this.$message.success("验证码已发送，有效时间十分钟，请注意查收！")
          // 发送按钮不可用
          this.sendCodeDisable = true
          $("#send_code_btn").css({"background-color": "#CBCBCB", "color": "#333333"})
          // 发送按钮显示倒计时
          let i = 60
          this.sendBtnText = "已发邮箱 " + i
          let time = setInterval(() => {
            i--
            this.sendBtnText = "已发邮箱 " + i
            if (i == 0) {
              this.isSend = false
              this.sendCodeDisable = false
              this.sendBtnText = "重新发送"
              $("#send_code_btn").css({"background-color": "#C40000", "color": "white"})
              clearInterval(time)
            }
          }, 1000)
          this.codeDisable = false // 设置验证码输入框可用
        })
    },

    // 验证码输入按键弹起检测
    emailCodeCheckKeyUp() {
      let emailCode = this.emailCode
      let regex = /^[0-9]{6}$/
      if (regex.test(emailCode)) {
        // 如果当前提示框是验证码问题提示，则隐藏它，否则不隐藏
        regex = /验证码/
        if (regex.test(this.hintText)) {
          this.visibilityHint = false
        }
        this.registerDisable = false // 设置注册按钮可用
        $("#submitBtn").css({"background-color": "#C40000", "color": "white"})
        return true
      }
    },

    // 邮箱验证码合法性检测
    emailCodeCheck() {
      let emailCode = this.emailCode
      if (emailCode == null) {
        this.visibilityHint = true
        this.hintText = "验证码不能为空！"
        return false
      }
      let regex = /^[0-9]{6}$/
      if (!regex.test(emailCode)) {
        this.visibilityHint = true
        this.hintText = "验证码格式错误，验证码为6位数字"
        return false
      }
      return true
    }

  }

}
</script>

<style scoped>

</style>
