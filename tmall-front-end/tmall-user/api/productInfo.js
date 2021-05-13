import request from '@/utils/request'

const api_productInfo_url = "/api/product/productInfo"
const api_review_url = "/api/product/review"
const api_orderItem_url = "/api/order/orderItem"

export default {
  // 根据商品id查询出商品信息
  getProductInfoById(id) {
    return request({
      url: `${api_productInfo_url}/${id}`,
      method: "get"
    })
  },

  // 根据商品id分页查询商品评价
  listReviewByProductId(current, limit, productId) {
    return request({
      url: `${api_review_url}/list/${current}/${limit}/${productId}`,
      method: "get"
    })
  },

  // 加入购物车
  joinOrderItem(orderItem) {
    return request({
      url: `${api_orderItem_url}/auth/join/orderItem`,
      method: "post",
      data: orderItem
    })
  }
}
