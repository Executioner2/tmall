import md5Util from "./md5Util";

let Base64 = require('js-base64').Base64

// 加密了的盐
const base_slot = md5Util.encrypt("2Executioner")

export default {
  // base64编码
  encode(data) {
    let val = Base64.encode(data)
    // 简单加密
    val = Base64.encode(val + base_slot)
    return val
  },

  // base64解码
  decode(data) {
    let val = Base64.decode(data)
    // 把盐剔除
    val = val.substring(0, val.length - base_slot.length)
    val = Base64.decode(val)
    return val
  }
}
