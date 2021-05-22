import request from '@/utils/request'

const api_orderInfo_url = "/api/order/orderInfo"

export default {
  // 显示用户订单信息
  listOrderInfo(status) {
    return request({
      url: `${api_orderInfo_url}/auth/list/orderInfo/${status}`,
      method: "post",
    })
  },

  // 一键发货
  deliver(orderId) {
    return request({
      url: `${api_orderInfo_url}/auth/deliver/${orderId}`,
      method: "post",
    })
  },

}
