import request from '@/utils/request'

const api_categoryInfo_url = "/api/category/categoryInfo"
const api_productInfo_url = "/api/product/productInfo"

export default {
  // 根据id查询出分类
  getCategoryInfo(id) {
    return request({
      url: `${api_categoryInfo_url}/${id}`,
      method: "get"
    })
  },

  // 根据分类id和查询条件显示商品
  listProductInfo(current, limit, searchObj) {
    return request({
      url: `${api_productInfo_url}/list/${current}/${limit}`,
      method: "get",
      params: searchObj
    })
  }
}
