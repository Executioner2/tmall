import request from '@/utils/request'

const api_url = "/admin/product/productImage"

export default {
  // 显示图片
  show(productId) {
    return request({
      url: `${api_url}/show/${productId}`,
      method: "get"
    })
  },
  // 批量删除图片
  batchRemove(idList) {
    return request({
      url: `${api_url}/batchRemove`,
      method: "delete",
      data: idList
    })
  },
  // 删除图片
  remove(id) {
    return request({
      url: `${api_url}/${id}`,
      method: "delete"
    })
  }

}
