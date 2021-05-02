import request from '@/utils/request'

const api_userInfo_url = "/api/user/userInfo"

export default {
  // 发送登录验证码到邮箱
  sendEmailCode(userLogin) {
    return request({
      url: `${api_userInfo_url}/login/send/emailCode`,
      method: "post",
      data: userLogin
    });
  }
}
