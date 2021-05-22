import request from '@/utils/request'

const api_orderItem_url = "/api/order/orderItem"
const api_review_url = "/api/product/review"

export default {
  // 取得商品和订单项信息
  getOrderItemAndProductInfo(id) {
    return request({
      url: `${api_orderItem_url}/auth/getOrderItemDetailsById/${id}`,
      method: "get"
    })
  },

  // 提交评价
  submitReview(review) {
    return request({
      url: `${api_review_url}/auth/add`,
      method: "post",
      data: review
    })
  }


}
