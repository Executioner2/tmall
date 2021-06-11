import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import cookie from "js-cookie";
import storage from "../assets/js/storage";

// 创建axios实例
const service = axios.create({
  //baseURL: 'http://192.168.123.233',
  baseURL: 'http://localhost',
  timeout: 15000 // 请求超时时间
})
// http request 拦截器
service.interceptors.request.use(
  config => {
    // token 先不处理，后续使用时在完善
    // return config

      // // 判断cookie中是否有token
      // if(cookie.get('token')){
      //   // 把token放到config中
      //   config.headers['token'] = cookie.get('token')
      // }

      // 判断localStorage中是否有token
      let token = storage.getItem('token')
      if(token){
        // 把token放到config中
        config.headers['token'] = token
    }
    return config
  },
  err => {
    return Promise.reject(err)
  })
// http response 拦截器
service.interceptors.response.use(
  response => {
    if (response.data.code !== 200) {
      Message({
        message: response.data.message,
        type: 'error',
        duration: 5 * 1000
      })
      if (response.data.code === 508) { // 需要登录
        // 清空storage中所有值，并跳转到登陆页面
        storage.removeLoginStatus()
        this.$router.push("/login")
      }
      return Promise.reject(response.data)
    } else {
      return response.data
    }
  },
  error => {
    return Promise.reject(error.response)
  })
export default service
