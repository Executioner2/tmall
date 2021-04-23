import request from '@/utils/request'

const api_url = "/api/category/categoryInfo"

export default {
  // 首页显示分类列表和热销商品
  listCategoryAndHotProduct() {
    return request({
      url: `${api_url}/list`,
      method: "get"
    })
  }
}
