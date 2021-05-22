import request from '@/utils/request'

const api_orderItem_url = "/api/order/orderItem"

export default {
  getOrderItemAndProductInfo(id) {
    return request({
      url: `${api_orderItem_url}/auth/getOrderItemDetailsById/${id}`,
      method: "get"
    })
  },


}
