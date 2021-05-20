import request from '@/utils/request'

const api_weChat_url = "/api/order/weChat"
const api_orderInfo_url = "/api/order/orderInfo"

export default {
  // 微信支付二维码
  createNative(orderId) {
    return request({
      url: `${api_weChat_url}/auth/createNative/${orderId}`,
      method: "post",
    })
  },

  // 微信支付轮询
  polling(orderId) {
    return request({
      url: `${api_weChat_url}/auth/payStatus/poll/${orderId}`,
      method: "post",
    })
  },

  // 根据订单id或取订单信息
  getOrderInfo(orderId) {
    return request({
      url: `${api_orderInfo_url}/auth/getOrderInfo/${orderId}`,
      method: "post",
    })
  },

  // 点击支付
  pay(orderId) {
    return request({
      url: `${api_weChat_url}/auth/pay/${orderId}`,
      method: "post",
    })
  }

}
