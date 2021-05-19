import request from '@/utils/request'

const api_orderInfo_url = "/api/order/orderInfo"

export default {
  // 下单
  settlement(order) {
    return request({
      url: `${api_orderInfo_url}/auth/settlement`,
      method: "post",
      data: order
    })
  }

}

