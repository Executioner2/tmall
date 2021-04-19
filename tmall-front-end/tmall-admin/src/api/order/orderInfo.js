import request from '@/utils/request'

const api_url = "/admin/order/orderInfo"

export default {
  // 分页条件显示订单
  findPageOrderInfo(current, limit, searchObj) {
    return request({
      url: `${api_url}/list/${current}/${limit}`,
      method: "get",
      params: searchObj
    })
  },
  // 订单发货
  deliverGoods(id) {
    return request({
      url: `${api_url}/deliverGoods/${id}`,
      method: "put"
    })
  }
}
