<template>
  <div>
    <div id="account_set_div">
      <el-tabs tab-position="left" value="1" style="height: 800px;">
        <el-tab-pane :disabled="true"/>
        <el-tab-pane name="1" label="基本信息">
          <div id="account_basic_info" class="right_panel">
            <div id="account_basic_info_head">
              <div style="position: relative; display: inline-block; height: 80px; width: 80px;">
                <el-avatar style="z-index: -1; position: absolute" shape="square" :size="80" :src="userInfo.avatar"></el-avatar>
                <a @click="dialogShow(3)" href="javascript:void(0)" id="edit_avatar_a">修改头像</a>
              </div>
              <div id="account_basic_info_head_nick">
                <div>
                  <input ref="nickName" @blur="editNickName(0)" :disabled="inputTextClass == 'input_text_show'" :class="inputTextClass" type="text" v-model="userInfo.nickName">
                  <span><a @click="editNickName(1)" href="javascript:void(0)"><i class="el-icon-edit"></i></a></span>
                </div>
                <h4></h4><span style="">会员名：{{userInfo.name}}</span>
              </div>
            </div>
          </div>
          <div id="account_security_set" class="right_panel">
            <div id="security_level">
              <span class="account_security_set_title">安全等级</span>
              <el-progress style="width: 78%; display: inline-block; margin-left: 20px" :status="securityLevelStatus" :stroke-width="26" :percentage="point"></el-progress>
              <span id="security_point">{{point}}分</span>
            </div>
            <div id="pass_edit_div">
              <span class="account_security_set_title">密码修改</span>
              <span class="hint_red">建议亲每隔一段时间修改密码呢！</span>
              <a href="javascript:void(0)"><i class="el-icon-edit"></i></a>
            </div>
            <div id="email_bind_div">
              <span class="account_security_set_title">邮箱绑定</span>
              <span class="hint_green">已绑定邮箱：{{userInfo.email}}</span>
              <a @click="dialogShow(0)" href="javascript:void(0)"><i class="el-icon-edit"></i></a>
            </div>
            <div id="phone_bind_div">
              <span class="account_security_set_title">手机号绑定</span>
              <span :class="userInfo.phone ? 'hint_green' : 'hint_red'">{{userInfo.phone ? '手机号已绑定' : '手机号未绑定'}}</span>
              <a href="javascript:void(0)"><i class="el-icon-edit"></i></a>
            </div>
            <div id="weChat_bind_div">
              <span class="account_security_set_title">微信号绑定</span>
              <span :class="userInfo.openid ? 'hint_green' : 'hint_red'">{{userInfo.openid ? '微信号已绑定' : '微信号未绑定'}}</span>
              <a v-if="!userInfo.openid" @click="dialogShow(1)" href="javascript:void(0)"><i class="el-icon-edit"></i></a>
              <el-button v-if="userInfo.openid" size="mini" style="float: right; margin-right: 70px" type="danger" @click="unWeChatBinding">解除绑定</el-button>
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

    <!--dialogVisible-->
    <!-- 微信绑定模态框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      :width="dialogWidth"
      :before-close="handleClose">

      <!--  邮箱绑定 -->
      <div id="email_binding_div" v-if="target == 0">
        <div v-if="!emailBindNew" style="width: 100%; margin: 0px auto; text-align: center">
          <div style="height: 20px">
            <div id="title" style="margin-bottom: 20px;font-size: 12px" v-if="isSendCode">已向邮箱{{userInfo.email}}发送验证码，请注意查收</div>
          </div>
          <el-input @input="emailCodeCheck(1)" style="width: 50%; margin-right: 30px" v-model.number="emailCode" placeholder="请输入验证码"></el-input>
          <el-button style="width: 110px" type="primary" :disabled="isSendCode" @click="sendEmailCodeToOld" v-text="sendBtnText"></el-button>
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
      </div>
      <span v-if="target == 0" slot="footer" class="dialog-footer">
          <el-button v-if="!emailBindNew" type="primary" :disabled="next" @click="emailBindNew = true">下一步</el-button>
          <el-button v-if="emailBindNew" type="primary" :disabled="binding" @click="emailBind">绑定</el-button>
      </span>

      <!-- 微信绑定 -->
      <div id="weChat_binding_div" v-if="target == 1">
        <div>
          <!-- 获取微信登录二维码 -->
          <div style="margin: 0px auto; width: 220px; height: 220px" class="qrcode" ref="qrCodeUrl"></div>
        </div>
        <div style="margin-top: 10px; text-align: center">
          <el-alert style="padding: 10px" v-if="state" title="微信扫码成功，请确认是否绑定微信号" :closable="false" type="success" show-icon>
          </el-alert>
          <el-button style="margin-top: 10px" type="primary" :disabled="!state" @click="confirmBindWeChat" v-text="'确认绑定'"></el-button>
        </div>
      </div>

      <!-- 手机号绑定 -->
      <div id="phone_binding_div" v-if="target == 2">

      </div>

      <!-- 上传头像 -->
      <div id="upload_avatar_div" v-if="target == 3" style="text-align: center">
        <el-upload
          class="avatar-uploader"
          name="file"
          :action="uploadUrl"
          :show-file-list="false"
          :headers="importHeaders"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload">
          <img v-if="imageUrl" :src="imageUrl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import qrcode from "qrcodejs2";
import accountSet from "../../api/accountSet";
import storage from "../../assets/js/storage";

const uploadUrl = "http://localhost/api/user/userInfo/auth/upload/avatar";
export default {
  name: "index",

  data() {
    return {
      userInfo: {}, // 用户信息
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
      uuid: null, // redis中获取token的依据
      state: false, // 用户微信扫码状态
      point: 0, // 安全分数
      securityLevelStatus: 'exception', // 安全等级状态
      target: 0, // 当前模态框类型（邮箱：0，微信：1，手机号：2, 头像上传：3）
      dialogVisible: false,
      dialogWidth: "0px", // 模态框宽度
      dialogTitle: "", // 模态框title
      inputTextClass: "input_text_show", // input text的类，用于编辑或显示用户名这两不同样式的切换
      imageUrl: null, // 头像url地址
      uploadUrl: uploadUrl, // 文件上传地址
      importHeaders: {token: null}, // token，上传图片附带
    }
  },
  mounted() {
    this.importHeaders.token = storage.getItem("token")
  },
  created() {
    this.getUserDetailsInfo()
  },
  methods: {
    // 上传头像
    handleAvatarSuccess(res, file) {
      this.$message.success("头像修改成功")
      this.imageUrl = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      let list = ["image/png", "image/jpg", "image/jpeg"]
      const isJPG = list.indexOf(file.type) != -1;
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 png jpg jpeg格式!');
        return false
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
        return false
      }
      return isJPG && isLt2M;
    },

    // 更改用户昵称
    editNickName(val) {
      if (val == 1) {
        this.inputTextClass = "input_text_edit"
        this.$nextTick(function () {
          this.$refs.nickName.focus()
        })
      } else if (val == 0) {
        let nickName = this.userInfo.nickName
        if (nickName == null || nickName.trim() == "") {
          this.$message.info("昵称不能为空")
          return
        }
        // 向后端发送修改用户名的请求
        accountSet.updateNickName(nickName)
          .then(response => {
            this.$message.success("修改昵称成功，请刷新页面")
            this.inputTextClass = "input_text_show"
            // 更新localStorage中的用户信息
            let user = storage.getItem("userInfo")
            user.name = nickName
            storage.setItem("userInfo", user, 30*60*1000)
          })
          .catch(error => {
            this.$message.warning("修改昵称失败")
          })

      }
    },

    // 解除微信绑定
    unWeChatBinding() {
      accountSet.unWechatBinding()
        .then(response => {
          this.$router.go(0)
        })
    },

    // 模态框显示
    dialogShow(val) {
      this.target = val
      this.dialogVisible = true

      if (this.target == 0) { // 邮箱
        this.dialogWidth = "350px"
        this.dialogTitle = "邮箱绑定"
        this.sendBtnText = "发送验证码"
        this.sendBtnText2 = "发送验证码"
      } else if (this.target == 1) { // 微信
        this.dialogWidth = "320px"
        this.dialogTitle = "微信绑定"
        this.creatQRCode()

      } else if (this.target == 2) { // 手机号

      } else if (this.target == 3) { // 上传头像
        this.dialogWidth = "250px"
        this.dialogTitle = "头像上传"
      }
    },

    // 模态框关闭
    handleClose() {
      if (this.target == 0) { // 邮箱
        this.isSendCode = false
        this.emailBindNew = false
        this.emailCode = null
        this.binding = false
        this.next = false
        clearInterval(this.interval)
      } else if (this.target == 1) { // 微信
        clearInterval(this.interval)

      } else if (this.target == 2) { // 手机号

      } else if (this.target == 3) { // 头像
        this.$router.go(0)
      }
      this.dialogVisible = false
    },

    // 用户信息查询
    getUserDetailsInfo() {
      accountSet.getUserDetailsInfo()
        .then(response => {
          this.userInfo = response.data
          this.securityLevel()
        })
    },

    // 安全等级计算
    securityLevel() {
      let list = [];
      list.push(this.userInfo.email)
      list.push(this.userInfo.phone)
      list.push(this.userInfo.openid)
      list.push(this.userInfo.question)
      for (let i = 0; i < list.length; i++) {
        if (list[i] != null) {
          this.point += 25
        }
      }
      if (this.point == 0) {
        this.securityLevelStatus = 'exception'
        $("#security_point").css({"color": "#F56C6C"})
      } else if (this.point == 100) {
        this.securityLevelStatus = 'success'
        $("#security_point").css({"color": "#67C23A"})
      } else {
        this.securityLevelStatus = 'warning'
        $("#security_point").css({"color": "#E6A23C"})
      }
    },

    // 确认绑定微信
    confirmBindWeChat() {
      accountSet.confirmWeChatBinding(this.uuid)
        .then(response => {
           if (response.data) {
             this.$message.success("绑定成功！")
             // this.handleClose()
             this.$router.go(0)
           }
        })
    },

    // 获取二维码url
    weChatQRCode() {
      accountSet.weChatQRCode(1) // type为0表示登录二维码 为1表示微信绑定二维码
        .then(response => {
          this.qrcodeUrl = response.data.QRCodeUrl
          this.uuid = response.data.uuid
          new qrcode(this.$refs.qrCodeUrl, {
            text: this.qrcodeUrl, // 需要转换为二维码的内容
          })
        })
    },

    // 创建二维码
    creatQRCode() {
      // 创建二维码
      this.$nextTick(() => {
        this.weChatQRCode()
      })

      let time = 120;
      // 轮询
      this.interval = setInterval(() => {
        if (--time == 0) {
          // 每隔120秒刷新一次二维码
          this.$nextTick(() => {
            this.weChatQRCode()
          })
          time = 120
        }
        // 向后端发送请求查询用户是否扫码
        accountSet.pollingBinding(this.uuid)
          .then(response => {
            this.state = response.data
            if (this.state) { // 如果扫码成功定时器关闭
              clearInterval(this.interval)
            }
          })
      }, 1000)
    },

    // 邮箱绑定模态框
    emailBind() {
      // TODO 向后端发送邮箱绑定请求
      this.$message.success("邮箱绑定成功")
      // this.handleClose()
      this.$router.go(0)
    },

    // 发送验证码到旧邮箱
    sendEmailCodeToOld() {
      this.isSendCode = true
      let time = 60
      this.sendBtnText = "已发送 " + time
      this.interval = setInterval(() => {
        --time
        this.sendBtnText = "已发送 " + time
        if (time == 0) {
          clearInterval(this.interval)
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
      this.interval = setInterval(() => {
        --time
        this.sendBtnText2 = "已发送 " + time
        if (time == 0) {
          clearInterval(this.interval)
          this.sendBtnText2 = "重新发送"
          this.isSendCode2 = false
        }
      }, 1000)
    },

  }
}
</script>

<style>
/*头像上传样式，element-ui提供的*/
.avatar-uploader .el-upload {
  margin: 0px auto;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader .el-upload input{
  display: none;
  /*visibility: hidden;*/
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 150px;
  height: 150px;
  line-height: 150px;
  text-align: center;
}
.avatar {
  width: 150px;
  height: 150px;
  display: block;
}
</style>

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
#account_set_div #account_basic_info_head #edit_avatar_a{
  color: white;
  text-decoration: none;
  width: 100%;
  background-color: rgba(0,0,0,0.2);
  z-index: 1;
  position: absolute;
  bottom: 0px;
  padding-left: 15px
}
#account_set_div #account_basic_info_head #edit_avatar_a:hover{
  color: #A0CFFF;
}
#account_set_div #account_basic_info_head_nick {
  height: 80px;
  width: 80%;
  line-height: 35px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  display: inline-block;
  vertical-align: top;
  margin-left: 10px;
  padding-left: 5px;
}
#account_set_div #account_basic_info_head_nick .input_text_edit {
  width: 160px;
  font-size: 14px;
  font-weight: bold;
  height: 20px;
}
#account_set_div #account_basic_info_head_nick .input_text_show {
  width: 160px;
  background-color: unset;
  border: none;
  font-size: 14px;
  font-weight: bold;
  height: 20px;
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
