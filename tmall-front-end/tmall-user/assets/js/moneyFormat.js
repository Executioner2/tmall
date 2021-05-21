export default {
  // 货币格式化
  format(data) {
    // 123 format 123.00
    // 123.12 format  123.12
    // 1234.12 format  1,234.12
    // 12345.12 format  12,345.12
    // 123456.12 format  123,456.12
    // 1234567.12 format  1,234,567.12
    data = data.toString() // 转为字符串
    let pointBefore = data.substring(0, data.indexOf("."))
    let pointAfter = data.substring(data.indexOf("."), data.length)

    // 如果没有小数或者小数不足两位则添0补齐
    if (pointBefore == "") {
      pointBefore = pointAfter
      pointAfter = ".00"
    }else if (pointAfter.length == 2) {
      pointAfter += "0"
    }

    if (pointBefore.length <= 3) { // 如果是第一、二种就直接返回
      return pointBefore + pointAfter
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
