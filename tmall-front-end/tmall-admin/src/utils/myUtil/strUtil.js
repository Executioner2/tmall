export default {
  // 判断字符串是否为空
  isEmpty(value) {
    if (value == null || value == "") return true
  },

  // 判断是否为合法金额
  isLegalAmount(value) {
    return !isNaN(parseFloat(value)) && isFinite(value)
  },

  // 判断是否为整数
  isNumber(value) {
    let reg = new RegExp("^[0-9]+$")
    if (reg.test(value)) {
      return true
    } else {
      return false
    }

  }

}
