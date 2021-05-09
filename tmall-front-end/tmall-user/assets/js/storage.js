// 本地存储工具js
export default {
  // 设置item
  setItem(key, value, ttl) {
    let data = {}
    // 当前时间戳
    let timestamp = new Date().getTime()
    if (ttl >= 0) { // 如果ttl大于等于0则设置ttl，否则为永久存储
      data.ttl = ttl + timestamp
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

    // 否则返回data的value
    return data.value
  }

}
