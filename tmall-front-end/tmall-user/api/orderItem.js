import request from '@/utils/request'

const api_orderItem_url = "/api/order/orderItem"

export default {
  // 获取订单项（订单项对应的商品信息）
  getOrderItem() {
    return request({
      url: `${api_orderItem_url}/auth/getOrderItem`,
      method: "post"
    })
  }
}
