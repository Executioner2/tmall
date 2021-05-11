import request from '@/utils/request'

const api_userInfo_url = "/api/user/userInfo"
const api_weChat_url = "/api/user/weChat"

export default {
  // 发送登录验证码到邮箱
  sendEmailCode(userLogin) {
    return request({
      url: `${api_userInfo_url}/login/send/emailCode`,
      method: "post",
      data: userLogin
    });
  },

  // 登录
  userLogin(userLogin) {
    return request({
      url: `${api_userInfo_url}/login`,
      method: "post",
      data: userLogin
    });
  },

  // 获取登录二维码
  weChatQRCode(type) {
    return request({
      url: `${api_weChat_url}/get/QRCode/${type}`,
      method: "get"
    });
  },

  // 轮询
  polling(state) {
    return request({
      url: `${api_weChat_url}/polling/${state}`,
      method: "post"
    });
  },


}
