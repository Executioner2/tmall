import request from '@/utils/request'

const api_url = "/admin/user/userInfo"

export default {
  // 分页显示用户
  findPageUserInfo(current, limit, searchObj) {
    return request({
      url: `${api_url}/list/${current}/${limit}`,
      method: "get",
      params: searchObj
    })
  },
  // 锁定/解锁 用户
  lock(id, status) {
    return request({
      url: `${api_url}/lock/${id}/${status}`,
      method: "put"
    })
  },
  //审批用户认证
  authUser(id, authStatus) {
    return request({
      url: `${api_url}/authUser/${id}/${authStatus}`,
      method: "put"
    })
  }

}
