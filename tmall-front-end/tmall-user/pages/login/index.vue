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
                <input type="text" name="username">
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
                <input type="password" name="password">
              </td>
            </tr>
          </table>
          <div class="hint">不要输入真实的天猫账号密码</div>
          <div class="otherOperating">
            <span class="forgetLoginPassword"><a href="#">忘记登录密码</a></span>
            <span>
              <span class="wechat_login"><a href="#">微信登录</a></span>
              <span class="freeRegister"><a href="#">免费注册</a></span>
            </span>
            <div style="clear: both"></div>
          </div>
          <button>登录</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "index"
}
</script>

<style scoped>

</style>
