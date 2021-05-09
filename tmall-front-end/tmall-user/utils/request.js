import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
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

      // 判断localStorage中是否有token
      if(storage.getItem('token')){
        // 把token放到config中
        config.headers['token'] = storage.getItem('token')
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
      return Promise.reject(response.data)
    } else {
      return response.data
    }
  },
  error => {
    return Promise.reject(error.response)
  })
export default service
