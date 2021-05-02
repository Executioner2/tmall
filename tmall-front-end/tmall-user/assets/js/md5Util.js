import md5 from 'js-md5';

export default {
  // MD5加密
  encrypt(data) {
    // md5转码
    data = md5(data)
    // 加盐
    let result = ""
    let hexChars = [ '0', '1', '2', '3', '4', '5', '6', '7', '8',
      '9', 'a', 'b', 'c', 'd', 'e', 'f' ];
    for (let i = 0; i < data.length; i++) {
      // 转换为二进制
      let binary = parseInt(data.charCodeAt(i).toString(2))
      // 根据位运算从盐中选取颗粒
      result += hexChars[binary >>> 4 & 0xf]
      result += hexChars[binary & 0xf]
    }
    return result
  }
}
