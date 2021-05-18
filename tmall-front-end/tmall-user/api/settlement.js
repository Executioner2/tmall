import request from '@/utils/request'

const api_orderInfo_url = "/api/order/orderInfo"
const api_weChat_url = "/api/order/weChat"

export default {
  // 下单
  settlement(order) {
    return request({
      url: `${api_orderInfo_url}/auth/settlement`,
      method: "post",
      data: order
    })
  }

  // 微信支付二维码
}

