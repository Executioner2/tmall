import request from '@/utils/request'

const api_url = "/admin/order/orderItem"

export default {
  // 分页条件显示订单
  getOrderItem(orderId) {
    return request({
      url: `${api_url}/show/${orderId}`,
      method: "get"
    })
  },
}
