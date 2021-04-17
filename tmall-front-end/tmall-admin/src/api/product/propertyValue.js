import request from '@/utils/request'

const api_url = '/admin/product/propertyValue'

export default {
  // 显示属性
  show(pid) {
    return request({
      url: `${api_url}/show/${pid}`,
      method: "get"
    })
  },
  // 更新商品属性值
  update(propertyValue) {
    return request({
      url: `${api_url}/update`,
      method: "put",
      data: propertyValue
    })
  }
}
