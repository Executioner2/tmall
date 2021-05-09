import request from '@/utils/request'

const api_userInfo_url = "/api/user/userInfo"

export default {
  // 获取用户信息
  getUserInfo() {
    return request({
      url: `${api_userInfo_url}/auth/getUserInfo`,
      method: 'post'
    })
  }
}
