export default {
  // 货币格式化
  format(data) {
    // 123.12 format  123.12
    // 1234.12 format  1,234.12
    // 12345.12 format  12,345.12
    // 123456.12 format  123,456.12
    // 1234567.12 format  1,234,567.12
    data = data.toString() // 转为字符串
    let pointBefore = data.substring(0, data.indexOf("."))
    let pointAfter = data.substring(data.indexOf("."), data.length)
    if (pointBefore.length <= 3) { // 如果是第一种就直接返回
      return data
    }
    pointBefore = pointBefore.split("").reverse().join("") // 反转一手
    let temp = ""
    for (let i = 1; i <= pointBefore.length; i++) {
      temp += pointBefore[i-1]
      if (i % 3 == 0 && i != pointBefore.length) {
        temp += ","
      }
    }
    pointBefore = temp.split("").reverse().join("") // 再反转
    return pointBefore + pointAfter
  }
}
