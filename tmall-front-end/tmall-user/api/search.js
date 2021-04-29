import request from '@/utils/request'

const api_productInfo_url = "/api/product/productInfo"

export default {
  // 根据关键字搜索商品
  searchProductInfo(current, limit, keyword) {
    return request({
      url: `${api_productInfo_url}/search/productInfo/${current}/${limit}/${keyword}`,
      method: "get"
    })
  }
}
