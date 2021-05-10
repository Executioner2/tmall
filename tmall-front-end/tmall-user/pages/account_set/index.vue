<template>
  <div>
    <div id="account_set_div">
      <el-tabs tab-position="left" value="1" style="height: 800px;">
        <el-tab-pane disabled="true"/>
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
              <span class="hint_green">已绑定邮箱：1205878539@qq.com</span>
              <a href="javascript:void(0)"><i class="el-icon-edit"></i></a>
            </div>
            <div id="phone_bind_div">
              <span class="account_security_set_title">手机号绑定</span>
              <span class="hint_red">手机号未绑定</span>
              <a href="javascript:void(0)"><i class="el-icon-edit"></i></a>
            </div>
            <div class="security_question_div">
              <span class="account_security_set_title">密保问题</span>
              <span class="hint_red">未设置密保</span>
              <a href="javascript:void(0)"><i class="el-icon-edit"></i></a>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane disabled="true"/>
        <el-tab-pane label="安全设置">
<!--          <div id="account_security_set" class="right_panel">-->
<!--            <div id="security_level">-->
<!--              <span class="account_security_set_title">安全等级</span>-->
<!--              <el-progress style="width: 70%; display: inline-block; margin-left: 20px" status="exception" :stroke-width="26" :percentage="25"></el-progress>-->
<!--              <span id="security_point">25分</span>-->
<!--            </div>-->
<!--            <div id="pass_edit_div">-->
<!--              <span class="account_security_set_title">密码修改</span>-->
<!--              <span class="hint_red">建议亲每隔一段时间修改密码呢！</span>-->
<!--              <a href="javascript:void(0)"><i class="el-icon-edit"></i></a>-->
<!--            </div>-->
<!--            <div id="email_bind_div">-->
<!--              <span class="account_security_set_title">邮箱绑定</span>-->
<!--              <span class="hint_green">已绑定邮箱：1205878539@qq.com</span>-->
<!--              <a href="javascript:void(0)"><i class="el-icon-edit"></i></a>-->
<!--            </div>-->
<!--            <div id="phone_bind_div">-->
<!--              <span class="account_security_set_title">手机号绑定</span>-->
<!--              <span class="hint_red">手机号未绑定</span>-->
<!--              <a href="javascript:void(0)"><i class="el-icon-edit"></i></a>-->
<!--            </div>-->
<!--            <div class="security_question_div">-->
<!--              <span class="account_security_set_title">密保问题</span>-->
<!--              <span class="hint_red">未设置密保</span>-->
<!--              <a href="javascript:void(0)"><i class="el-icon-edit"></i></a>-->
<!--            </div>-->
<!--          </div>-->
        </el-tab-pane>
        <el-tab-pane disabled="true"/>
        <el-tab-pane label="实名认证">实名认证</el-tab-pane>
        <el-tab-pane disabled="true"/>
        <el-tab-pane label="其它设置">其它设置</el-tab-pane>
        <el-tab-pane disabled="true"/>
      </el-tabs>
    </div>
  </div>
</template>

<script>

export default {
  name: "index",

  data() {
    return {
      userInfo: {}, // 用户信息
    }
  },
  created() {
    this.userInfo.nick = "。"
    this.userInfo.name = "zyx511688"
    this.userInfo.photoUrl = "_nuxt/assets/img/site/photo.png"
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
