// 本地存储工具js
export default {
  // 设置item
  setItem(key, value, ttl) {
    let data = {}
    // 当前时间戳
    let timestamp = new Date().getTime()
    if (ttl >= 0) { // 如果ttl大于等于0则设置ttl
      data.ttl = ttl + timestamp
    } else if (ttl == null) { // 如果为null，则设置默认ttl 30分钟
      data.ttl = timestamp + 30*60*1000
    } else if (ttl == -1) { // 如果ttl为-1代表永久存储
      data.ttl = null
    }

    data.value = value
    // 转换为json格式的字符串
    data = JSON.stringify(data)
    localStorage.setItem(key, data)
  },

  // 根据key获取value
  getItem(key) {
    let data = localStorage.getItem(key)
    if (data == null) {
      return null
    }
    // 把json字符串转换为json对象
    data = JSON.parse(data)
    // 如果设置了ttl，则判断是否出于有效状态
    if (data.ttl != null) {
      // 当前时间戳
      let timestamp = new Date().getTime()
      if (timestamp - data.ttl >= 0) {
        // 如果当前时间大于等于ttl则data失效
        // 删除失效的key
        localStorage.removeItem(key)
        return null
      }
    }
    // 返回data的value
    return data.value
  },

  // 移除item
  removeItem(key) {
    localStorage.removeItem(key)
  },

  // 更新ttl
  updateTtl(key, ttl) {
    // 当前时间戳
    let timestamp = new Date().getTime()
    let data = localStorage.getItem(key)
    if (data == null) {
      return null
    }
    // 把json字符串转换为json对象
    data = JSON.parse(data)
    if (ttl >= 0) { // 如果ttl大于等于0则设置ttl
      data.ttl = ttl + timestamp
    } else if (ttl == null) { // 如果为null，则设置默认ttl 30分钟
      data.ttl = timestamp + 30*60*1000
    } else if (ttl == -1) { // 如果ttl为-1代表永久存储
      data.ttl = null
    }

    data = JSON.stringify(data)
    localStorage.setItem(key, data)
  },

  // 移除所有项
  removeLoginStatus() {
    localStorage.removeItem("token")
    localStorage.removeItem("userInfo")
  }


}
