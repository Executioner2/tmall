<template>
  <div>
    <div id="account_set_div">
      <el-tabs tab-position="left" value="1" style="height: 800px;">
        <el-tab-pane :disabled="true"/>
        <el-tab-pane name="1" label="基本信息">
          <div id="account_basic_info" class="right_panel">
            <div id="account_basic_info_head">
              <el-avatar shape="square" :size="80" :src="userInfo.photoUrl"></el-avatar>
              <div id="account_basic_info_head_nick">
                <h4>{{userInfo.nick}}</h4>
                <h4></h4><span style="">会员名：{{userInfo.name}}</span>
              </div>
            </div>
          </div>
          <div id="account_security_set" class="right_panel">
            <div id="security_level">
              <span class="account_security_set_title">安全等级</span>
              <el-progress style="width: 78%; display: inline-block; margin-left: 20px" status="exception" :stroke-width="26" :percentage="25"></el-progress>
              <span id="security_point">25分</span>
            </div>
            <div id="pass_edit_div">
              <span class="account_security_set_title">密码修改</span>
              <span class="hint_red">建议亲每隔一段时间修改密码呢！</span>
              <a href="javascript:void(0)"><i class="el-icon-edit"></i></a>
            </div>
            <div id="email_bind_div">
              <span class="account_security_set_title">邮箱绑定</span>
              <span class="hint_green">已绑定邮箱：{{userInfo.email}}</span>
              <a @click="emailBindDialog = true" href="javascript:void(0)"><i class="el-icon-edit"></i></a>
            </div>
            <div id="phone_bind_div">
              <span class="account_security_set_title">手机号绑定</span>
              <span class="hint_red">手机号未绑定</span>
              <a href="javascript:void(0)"><i class="el-icon-edit"></i></a>
            </div>
            <div id="weChat_bind_div">
              <span class="account_security_set_title">微信号绑定</span>
              <span class="hint_red">微信号未绑定</span>
              <a @click="weChatQRCode" href="javascript:void(0)"><i class="el-icon-edit"></i></a>
            </div>
            <div class="security_question_div">
              <span class="account_security_set_title">密保问题</span>
              <span class="hint_red">未设置密保</span>
              <a href="javascript:void(0)"><i class="el-icon-edit"></i></a>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane :disabled="true"/>
        <el-tab-pane label="安全设置">安全设置</el-tab-pane>
        <el-tab-pane :disabled="true"/>
        <el-tab-pane label="实名认证">实名认证</el-tab-pane>
        <el-tab-pane :disabled="true"/>
        <el-tab-pane label="其它设置">其它设置</el-tab-pane>
        <el-tab-pane :disabled="true"/>
      </el-tabs>
    </div>

    <!-- 微信绑定模态框 -->
    <el-dialog
      title="微信绑定"
      :visible.sync="weChatBindDialog"
      width="30%"
      :before-close="weChatBindDialogClose">
      <div>
        <!-- 获取微信登录二维码 -->
        <div style="margin: 0px auto;" class="qrcode" ref="qrCodeUrl"></div>
      </div>
    </el-dialog>

    <!-- 邮箱绑定模态框 -->
    <el-dialog
      title="邮箱绑定"
      :visible.sync="emailBindDialog"
      width="350px"
      style="padding: 0px"
      :before-close="handledClose">
      <div v-if="!emailBindNew" style="width: 100%; margin: 0px auto; text-align: center">
        <div style="height: 20px">
          <div id="title" style="margin-bottom: 20px;font-size: 12px" v-if="isSendCode">已向邮箱{{userInfo.email}}发送验证码，请注意查收</div>
        </div>
        <el-input @input="emailCodeCheck(1)" style="width: 50%; margin-right: 30px" v-model.number="emailCode" placeholder="请输入验证码"></el-input>
        <el-button style="width: 110px" id="send_code_btn" type="primary" :disabled="isSendCode" @click="sendEmailCodeToOld" v-text="sendBtnText"></el-button>
      </div>
      <div id="email_bind_new" v-if="emailBindNew">
        <div>
          <div style="height: 20px">
            <div id="title2" style="margin-bottom: 20px;font-size: 12px" v-if="isSendCode2">已向邮箱{{email}}发送验证码，请注意查收</div>
          </div>
          <span style="font-size: 16px; font-weight: bold">电子邮箱：</span>
          <el-input style="width: 69%;" v-model="email" placeholder="请输入绑定邮箱"></el-input>
          <div style="margin-top: 20px">
            <el-input @input="emailCodeCheck(2)" style="width: 50%; margin-right: 30px" v-model.number="emailCode2" placeholder="请输入验证码"></el-input>
            <el-button style="width: 110px" type="primary" :disabled="isSendCode2" @click="sendEmailCodeToNew" v-text="sendBtnText2"></el-button>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button v-if="!emailBindNew" type="primary" :disabled="next" @click="emailBindNew = true">下一步</el-button>
        <el-button v-if="emailBindNew" type="primary" :disabled="binding" @click="emailBind">绑定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>

import login from "../../api/login";
import qrcode from "qrcodejs2";
import storage from "../../assets/js/storage";

export default {
  name: "index",

  data() {
    return {
      userInfo: {}, // 用户信息
      weChatBindDialog: false, // 微信绑定模态框，默认不显示
      emailBindDialog: false, // 邮箱绑定模态框显示状态，默认不显示
      emailCode: null, // 邮箱验证码
      isSendCode: false, // 是否发送验证码，默认false
      sendBtnText: "发送验证码", // 发送验证码按钮文本
      emailBindNew: false, // 是不是新邮箱绑定 TODO 注意判断用户是否绑定邮箱
      next: true, // 下一步
      isSendCode2: false,
      email: null, // 邮箱地址
      emailCode2: null,
      sendBtnText2: "发送验证码",
      binding: true,
      interval: null, // 轮询
    }
  },
  created() {
    this.userInfo.nick = "。"
    this.userInfo.name = "zyx511688"
    this.userInfo.photoUrl = "_nuxt/assets/img/site/photo.png"
    this.userInfo.email = "1205878539@qq.com"
  },
  methods: {
    // 微信绑定模态框关闭
    weChatBindDialogClose() {
      this.weChatBindDialog = false
      clearInterval(this.interval)
    },

    // 获取二维码url
    weChatQRCode() {
      this.weChatBindDialog = true
      login.weChatQRCode()
        .then(response => {
          this.qrcodeUrl = response.data.QRCodeUrl
          this.uuid = response.data.uuid
          new qrcode(this.$refs.qrCodeUrl, {
            text: this.qrcodeUrl, // 需要转换为二维码的内容
            width: 100,
            height: 100,
          })
        })
    },

    // 创建二维码
    creatQRCode() {
      this.loginTitle = "扫码登录"
      this.loginWay = false
      // 创建二维码
      this.$nextTick(() => {
        this.weChatQRCode()
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
                storage.setItem("tempToken", this.token, 30*60*1000) // 设置为临时token
                // cookie.set("tempToken", this.token)
                window.location.href = "/regist"
              } else { // 否则跳转到首页
                storage.setItem("token", this.token, 30*60*1000) // 设置token生命周期为半小时
                // cookie.set("token", this.token)
                window.location.href = "/"
              }
            }
          })
      }, 1000)
    },

    // 邮箱绑定模态框
    emailBind() {
      // TODO 向后端发送邮箱绑定请求
      this.$message.success("邮箱绑定成功")
      this.handledClose()
    },

    // 发送验证码到旧邮箱
    sendEmailCodeToOld() {
      this.isSendCode = true
      let time = 60
      this.sendBtnText = "已发送 " + time
      let timer = setInterval(() => {
        --time
        this.sendBtnText = "已发送 " + time
        if (time == 0) {
          clearInterval(timer)
          this.sendBtnText = "重新发送"
          this.isSendCode = false
        }
      }, 1000)
    },

    // 邮箱验证码校验
    emailCodeCheck(val) {
      if (val === 1) {
        if (this.emailCode >= 100000) {
          // TODO 向后端发送验证码校验请求
          this.next = false
        }
      } else if (val === 2) {
        if (this.emailCode2 >= 100000) {
          // TODO 向后端发送验证码校验请求
          this.binding = false
        }
      }

    },

    // 向新邮箱发送验证码
    sendEmailCodeToNew() {
      this.isSendCode2 = true
      let time = 60
      this.sendBtnText2 = "已发送 " + time
      let timer = setInterval(() => {
        --time
        this.sendBtnText2 = "已发送 " + time
        if (time == 0) {
          clearInterval(timer)
          this.sendBtnText2 = "重新发送"
          this.isSendCode2 = false
        }
      }, 1000)
    },

    // 模态框关闭后
    handledClose() {
      this.emailBindDialog = false
      this.isSendCode = false
      this.emailBindNew = false
      this.emailCode = null
      this.binding = false
      this.next = false
    }

  }
}
</script>

<style scoped>
#account_set_div {
  width: 1024px;
  margin: 100px auto;
}
#account_set_div .right_panel {
  margin-left: 80px;
}
#account_set_div #account_basic_info_head{
  width: 100%;
  margin-top: 20px;
  color: #999999;
}
#account_set_div #account_basic_info_head_nick {
  width: 80%;
  line-height: 40px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  display: inline-block;
  vertical-align: top;
  margin-left: 10px;
  padding-left: 5px;
}
#account_set_div #account_basic_info_head_nick h4:nth-child(1) {
  margin-bottom: 10px;
}
#account_set_div #account_basic_info_head_nick span {
  display: inline-block;
  font-size: 13px;
}
#account_set_div #account_security_set {
  margin-top: 10px;
}
#account_set_div #account_security_set .account_security_set_title {
  font-size: 15px;
  font-weight: bold;
}
#account_set_div #account_security_set #security_level #security_point {
  font-size: 12px;
  font-weight: bold;
  color: #F56C6C;
}
#account_set_div #account_security_set div {
  margin-top: 40px;
}
#account_set_div #account_security_set a {
  font-size: 20px;
  float: right;
  margin-right: 100px;
}
#account_security_set .hint_green{
  margin-left: 20px;
  color: #67C23A;
}
#account_security_set .hint_red{
  color: #F56C6C;
  margin-left: 20px;
}


</style>
