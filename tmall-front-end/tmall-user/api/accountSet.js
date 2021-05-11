import request from '@/utils/request'
import login from "./login";

const api_weChat_url = "/api/user/weChat"

export default {
  // 生成二维码
  weChatQRCode(type) {
    return login.weChatQRCode(type)
  },

  // 微信绑定轮询
  pollingBinding(state) {
      return request({
        url: `${api_weChat_url}/polling/binding/${state}`,
        method: 'post'
      })
  },

  // 确认要微信绑定
  confirmWeChatBinding(state) {
    return request({
      url: `${api_weChat_url}/confirm/weChat/binding/${state}`,
      method: 'post'
    })
  }

}
