import request from '@/utils/request'

const api_url = '/admin/product/productInfo'

export default{
    // 分页条件显示
    findPageProductInfo(current, limit, searchObj){
        return request({
            url: `${api_url}/list/${current}/${limit}`,
            method: 'get',
            params: searchObj
        })
    }
}
