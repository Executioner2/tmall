import request from '@/utils/request'

const api_url = '/admin/category/property'

export default {
  // 分页条件显示属性
  findPageProperty(current, limit, searchObj){
    return request({
      url: `${api_url}/list/${current}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },
  // 新增属性
  save(property){
    return request({
      url: `${api_url}/save`,
      method: 'post',
      data: property
    })
  },
  // 删除属性
  remove(id){
    return request({
      url: `${api_url}/${id}`,
      method: 'delete'
    })
  },
  // 批量删除属性
  batchRemove(idList){
    return request({
      url: `${api_url}/batchRemove`,
      method: 'delete',
      data: idList
    })
  },
  // 更新属性
  update(property){
    return request({
      url: `${api_url}/update`,
      method: 'put',
      data: property
    })
  }

}
