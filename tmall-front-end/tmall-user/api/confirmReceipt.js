import request from '@/utils/request'

const api_orderInfo_url = "/api/order/orderInfo"

export default {
  // 获取订单详情信息
  getOrderInfoDetails(orderId) {
    return request({
      url: `${api_orderInfo_url}/auth/getOrderInfoDetails/${orderId}`,
      method: "post"
    })
  },

  // 确认收货
  confirmReceipt(orderId) {
    return request({
      url: `${api_orderInfo_url}/auth/confirmReceipt/${orderId}`,
      method: "post"
    })
  }

}
