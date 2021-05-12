import request from '@/utils/request'
import login from "./login";

const api_userInfo_url = "/api/user/userInfo"
const api_weChat_url = "/api/user/weChat"

export default {
  // 生成二维码
  weChatQRCode(type) {
    return login.weChatQRCode(type)
  },

  // 微信绑定轮询
  pollingBinding(state) {
      return request({
        url: `${api_weChat_url}/auth/polling/binding/${state}`,
        method: 'post'
      })
  },

  // 确认要微信绑定
  confirmWeChatBinding(state) {
    return request({
      url: `${api_weChat_url}/auth/confirm/weChat/binding/${state}`,
      method: 'post'
    })
  },

  // 获取用户详情信息
  getUserDetailsInfo() {
    return request({
      url: `${api_userInfo_url}/auth/getUserDetailsInfo`,
      method: 'post'
    })
  },

  // 微信解除绑定
  unWechatBinding() {
    return request({
      url: `${api_userInfo_url}/auth/unWechatBinding`,
      method: 'post'
    })
  },

  // 修改用户昵称
  updateNickName(nickName) {
    return request({
      url: `${api_userInfo_url}/auth/update/nickName/${nickName}`,
      method: 'post'
    })
  }

}
