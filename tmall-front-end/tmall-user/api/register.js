import request from '@/utils/request'

const api_userInfo_url = "/api/user/userInfo"

export default {
  // 用户名重复性检测，返回Ture表示重复，返回False表示不重复
  userNameRepeatCheck(name) {
    return request({
      url: `${api_userInfo_url}/register/nameRepeatCheck/${name}`,
      method: "post"
    })
  },

  // 检测邮箱地址是否重复，同上
  userEmailRepeatCheck(email) {
    return request({
      url: `${api_userInfo_url}/register/emailRepeatCheck/${email}`,
      method: "post"
    })
  },

  // 发送验证码到邮箱
  sendEmailCode(email) {
    return request({
      url: `${api_userInfo_url}/register/send/${email}`,
      method: "post"
    })
  },

  // 用户注册
  userRegister(userInfo) {
    return request({
      url: `${api_userInfo_url}/register/user`,
      method: "post",
      data: userInfo
    })
  }


}
