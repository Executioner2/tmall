import request from '@/utils/request'

const api_url = '/admin/product/productInfo'

export default {
  // 分页显示产品
  findPageProductInfo(current, limit, searchObj){
    return request({
        url: `${api_url}/list/${current}/${limit}`,
        method: "get",
        params: searchObj
    })
  },

  // 删除产品
  remove(id) {
    return request({
      url: `${api_url}/${id}`,
      method: "delete"
    })
  },

  // 批量删除产品
  batchRemove(idList) {
    return request({
      url: `${api_url}/batchRemove`,
      method: "delete",
      data: idList
    })
  },

  // 添加产品
  saveProductInfo(productInfo) {
    return request({
      url: `${api_url}/save`,
      method: "post",
      data: productInfo
    })
  },

  // 编辑产品
  updateProductInfo(productInfo) {
    return request({
      url: `${api_url}/update`,
      method: "put",
      data: productInfo
    })
  }

}
