import request from '@/utils/request'

const api_url = '/admin/category/categoryInfo'

export default{
    // 分页条件显示
    findPageCategoryInfo(current, limit, searchObj){
      return request({
          url: `${api_url}/list/${current}/${limit}`,
          method: 'get',
          params: searchObj
      })
    },
    // 添加分类
    save(categoryInfo){
      return request({
          url: `${api_url}/save`,
          method: 'post',
          data: categoryInfo
      })
    },
    // 单个删除分类
    remove(id){
      return request({
        url: `${api_url}/${id}`,
        method: 'delete'
      })
    },

    // 批量删除分类
    batchRemove(idList){
      return request({
        url: `${api_url}/batchRemove`,
        method: 'delete',
        data: idList
      })
    },

    // 更新分类
    updateCategory(categoryObj){
      return request({
        url: `${api_url}/update`,
        method: 'put',
        data: categoryObj
      })
    }
}
