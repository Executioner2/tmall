<template>
  <div>

    <!--这里放简单搜索栏-->
    <simple-search/>

    <!--这里放titleImage-->
    <div id="titleImage" class="titleImage">
      <img :src="categoryInfo.imageUrl" height="110" width="1010"/>
    </div>

    <!--这里是把产品图片和基本信息装起来的div-->
    <div class="productImageAndProductBasicInformationDiv" id="productImageAndProductBasicInformationDiv">
      <!--这里放产品图片-->
      <div id="productImage">
        <img id="showImage" :src="showImage.url"/>
        <div class="productImageList">
          <img v-for="(item, index) in productImage.thumbnail" @mouseenter="showImage = item" :key="index" :src="item.url"/>
        </div>
      </div>

      <!--这里放基本信息-->
      <div id="productBasicInformation" class="productBasicInformation">
        <div class="biTitle">
          {{productInfo.name}}
        </div>
        <div class="sketchDiv">
          <span>{{productInfo.subTitle}}</span>
        </div>
        <div class="juHuaSuanCountDown">
          <span>聚划算</span>
          <span>此商品即将参加聚划算，<span class="countDow">1天19小时</span>后开始</span>
        </div>
        <div class="priceDiv">
          <div>
            <img src="~/assets/img/site/gouwujuan.png"/>
            <span>全天猫实物商品通用</span>
          </div>
          <div>
            <span>价格</span>
            <span><del>￥{{moneyFormat(productInfo.orignalPrice)}}</del></span>
          </div>
          <div>
            <span>促销价</span>
            <span>￥</span>
            <span>{{moneyFormat(productInfo.promotePrice)}}</span>
          </div>
        </div>
        <div class="salesAndReviews">
          <span>销量 <span class="sales">{{productInfo.salesVolume}}</span></span>
          <span>累计评价 <span class="reviews">{{countReview}}</span></span>
        </div>
        <div class="numberDiv">
          <span>数量</span>
          <span><input type="text" ref="number" v-model="orderItem.number" @keyup="setProductNumber" id="productNumber"></span>
          <span class="arrow">
                <a href="javascript:void(0)" id="upKey" @click="numberAdd">
                    <span class="updown">
                        <img src="~/assets/img/site/increase.png" height="3" width="7"/>
                    </span>
                </a>
                <span class="updownMiddle"></span>
                <a href="javascript:void(0)" id="downKey" @click="numberSub">
                    <span class="updown">
                        <img src="~/assets/img/site/decrease.png" height="3" width="7"/>
                    </span>
                </a>
            </span>

          <span>件 库存<span class="inventory">{{productInfo.stock}}</span>件</span>
        </div>
        <div class="commitment">
          <span>服务承诺</span>
          <a href="#">正品保证</a>
          <a href="#">极速退款</a>
          <a href="#">赠运费险</a>
          <a href="#">七天无理由退换</a>
        </div>
        <div class="buyAndJoinShopping">
          <button id="buy" @click="buy">立即购买</button>
          <button id="joinShopping" @click="joinOrderItemClick"><span class="glyphicon glyphicon-shopping-cart"></span>加入购物车</button>
        </div>
      </div>
    </div>

    <!--这里放产品详情-->
    <div id="productParticulars" v-show="reviewShow == false" class="productParticulars">
      <div class="productDetailTopPart">
        <a href="#nowhere" @click="reviewShow = false" class="productDetailTopPartSelectedLink">商品详情</a>
        <a href="#nowhere" @click="selectReview"  class="productDetailTopReviewLink">累计评价 <span class="evaluateNumber">{{countReview}}</span></a>
      </div>
      <div class="productParameterDiv">
        <div class="titleDiv">产品参数：</div>
        <div class="productParamterList">
          <span v-for="(item, index) in propertyAndValueVoList" :key="index">{{item.propertyName}}: {{item.propertyValue}}</span>
        </div>
      </div>
      <div class="productDescribeImageDiv">
        <img v-for="(item, index) in productImage.details" :key="index" :src="item.url"/>
      </div>
    </div>

    <!--这里放积累评价-->
    <div id="productCumulativeAssessment" v-show="reviewShow == true" class="productCumulativeAssessment">
      <div class="productDetailTopPart">
        <a href="javascript:void(0)" @click="reviewShow = false" class="productDetailTopPartSelectedLink">商品详情</a>
        <a href="javascript:void(0)" @click="selectReview" class="productDetailTopReviewLink">累计评价 <span class="evaluateNumber">{{countReview}}</span></a>
      </div>
      <div class="cumulativeAssessmentDiv">
        <div class="productEvaluateItem" v-for="(item, index) in reviews" :key="index">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              {{item.content}}
            </div>
            <div class="productEvaluateItemDate">
              {{item.createTime}}
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            {{item.anonymity}}
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
      </div>
      <!-- 分页 -->
      <div class="block" style="margin-top: 50px">
        <el-pagination
          :page-size="limit"
          :current-page="current"
          align="center"
          @current-change="listReviewByProductId"
          layout="prev, pager, next"
          :total="total">
        </el-pagination>
      </div>
    </div>

    <!-- 登录模态框 -->
    <el-dialog
      :visible.sync="dialogVisible"
      width="370px">
      <div class="loginInputBox">
        <div class="title" v-text="loginTitle"></div>
        <div id="account_login_div" v-if="loginWay">
          <table>
            <tr>
              <td>
                <span class="glyphicon glyphicon-user"></span>
              </td>
              <td>
                <input type="text" v-model="account" @keyup="checkSendBtn" placeholder="会员名/手机号/电子邮箱">
              </td>
            </tr>
            <tr>
              <td></td>
            </tr>
            <tr>
              <td>
                <span class="glyphicon glyphicon-lock"></span>
              </td>
              <td>
                <input type="password" v-model="password" @keyup="checkSendBtn" placeholder="请输入用户密码">
              </td>
            </tr>
          </table>
          <div id="email_code">
                <span>
                  <input type="text" v-model="emailCode" placeholder="请输入验证码">
                </span>
            <span>
                  <button id="send_code_btn" :disabled="isDisabled" @click="sendEmailCode" v-text="sendBtnText"></button>
                </span>
          </div>
          <div style="clear: both"></div>
          <div class="hint">不要输入真实的天猫账号密码</div>
          <div class="otherOperating">
            <span class="forgetLoginPassword"><a href="#">忘记登录密码</a></span>
            <span>
                <span class="wechat_login"><a href="javascript:void(0)" @click="creatQRCode">微信登录</a></span>
                <span class="freeRegister"><a href="/regist">免费注册</a></span>
              </span>
            <div style="clear: both"></div>
          </div>
          <button id="login_btn" @click="login">登录</button>
        </div>
        <div id="weChat_login_div" v-if="!loginWay">
          <div>
            <div v-if="!isGzh" style="margin: 50px auto; height: 200px; width: 200px" class="qrcode" ref="qrCodeUrl"></div>
          </div>
          <div v-if="isGzh"  style="text-align: center; margin: 50px auto; height: 200px; width: 200px"><img src="~assets/img/site/officialAccountsQRCode.png" alt="微信公众号二维码"></div>
          <div style="margin-top: 20px; padding-left: 30px; padding-right: 30px; width: 100%">
            <span style="float:left;" class="freeRegister"><a href="/regist">免费注册</a></span>
            <span v-if="!isGzh" style="margin-left: 38px"><a href="javascript:void(0)" @click="QRCodeCut(true)">关注公众号</a></span>
            <span v-if="isGzh" style="margin-left: 44px"><a @click="QRCodeCut(false)" href="javascript:void(0)">微信登录</a></span>
            <span style="float: right; " class="wechat_login"><a href="javascript:void(0)" @click="accountLogin">账号登录</a></span>
          </div>
        </div>
      </div>
    </el-dialog>


  </div>
</template>

<script>
import SimpleSearch from "../../layouts/simpleSearch";
import category from "../../api/category";
import productInfo from "../../api/productInfo";
import storage from "../../assets/js/storage";
import login from "../../api/login";
import base64Util from "../../assets/js/base64Util";
import md5Util from "../../assets/js/md5Util";
import qrcode from "qrcodejs2";
import moneyFormat from "../../assets/js/moneyFormat";

export default {
  components: {SimpleSearch},
  data() {
    return {
      categoryInfo: {}, // 分类对象
      productId: null, // 商品id
      productInfo: {}, // 商品信息
      countReview: 0, // 商品评价数
      propertyAndValueVoList: [], // 商品属性和属性值
      productImage: {thumbnail: [], details: []}, //商品图片
      showImage: {}, // 展示图
      reviewShow: false, // 评价框是否显示
      current: 1, // 商品评价起始页
      limit: 15, // 每页显示评价数量
      total: null, // 总页数
      reviews: [], // 评价集合
      firstReviewLoad: true, // 第一次加载评价
      orderItem: {}, // 购物车
      dialogVisible: false, // 模态框

      userLogin: {}, // 用户登录
      isDisabled: true, // 发送验证码按钮不可用
      account: null, // 用户名
      password: null, // 密码
      sendBtnText: "发送验证码", // 发送按钮文本
      isSend: false, // 验证码是否已发送
      token: null, // token
      emailCode: null, // 邮箱验证码
      loginTitle: "账户登录", // login title文本
      loginWay: true, // 登录方式，true账号登录，false微信登录
      uuid: null, // 拿取token的凭证
      qrcodeUrl: null, // 二维码地址
      state: null, // 账号状态
      isGzh: false, // 公众号二维码是否显示

    }
  },
  created() {
    this.productId = this.$route.params.productId
    this.getProductInfoById()
    this.init()
  },
  methods:{
    // 数据初始化
    init() {
      this.orderItem.number = 1
      this.orderItem.productId = this.productId
    },

    // 货币格式化
    moneyFormat(data) {
      return moneyFormat.format(data)
    },

    // 加入购物车
    async joinOrderItemClick() { // 同步执行
      let token = storage.getItem("token")
      if (!token) { // 如果token为空，则表示没有登录
        // 弹出登录模态框
        this.dialogVisible = true
      } else { // 否则加入购物车
        if (await this.joinOrderItem()) { // 需要等待的异步方法
          this.$message.success("成功加入购物车！刷新页面更新商品数量")
        } else {
          this.$message.error("加入购物车失败！")
        }
      }
    },

    // 加入购物车请求
    joinOrderItem() {
      return productInfo.joinOrderItem(this.orderItem)
        .then(response => {
          if (response.data) {
            $("#joinShopping")[0].disabled = true
            $("#joinShopping").css({"background-color": "#8c939d", "border": "none"})
            // 更新localStorage中userInfo的购物车商品数量
            let userInfo = storage.getItem("userInfo")
            userInfo.productNumber += this.orderItem.number
            storage.setItem("userInfo", userInfo)
            return response.data
          }
        })
    },


    // 立即购买
    async buy() {
      let token = storage.getItem("token")
      if (!token) { // 如果token为空，则表示没有登录
        // 弹出登录模态框
        this.dialogVisible = true
      } else { // 否则直接购买，跳转到下单页
        // 先添加到购物车，然后跳转到下单页面
        let orderItem = await this.joinOrderItem()
        if (orderItem) {
          // 把该商品所在购物车信息存入localStorage中
          let shopping = {}
          orderItem.amount = (parseFloat(orderItem.number) * parseFloat(this.productInfo.promotePrice)).toFixed(2)
          orderItem.params.productInfo = this.productInfo
          orderItem.params.productInfo.params.imageUrl = this.productImage.thumbnail[0].url
          orderItem.amountStr = moneyFormat.format(orderItem.amount)
          shopping.orderItems = []
          shopping.totalAmountStr = orderItem.amountStr
          shopping.totalAmount = orderItem.amount
          shopping.orderItems.push(orderItem)
          // 存入localStroage
          storage.setItem("shopping", shopping, -1)
          // 跳转到下单页面
          this.$router.push("/settlement")
        } else {
          this.$message.error("加入购物车失败！")
        }
      }
    },

    // 根据分类id查询分类
    getCategoryInfo() {
      category.getCategoryInfo(this.productInfo.categoryId)
        .then(response => {
          this.categoryInfo = response.data
        })
    },

    // 根据商品id查询出商品信息
    getProductInfoById() {
      productInfo.getProductInfoById(this.productId)
        .then(response => {
          this.productInfo = response.data
          this.countReview = response.data.params.countReview
          this.productImage = response.data.params.productImage
          this.propertyAndValueVoList = response.data.params.propertyAndValueVoList
          this.showImage = this.productImage.thumbnail[0]
          this.getCategoryInfo() // 查询分类
        })
    },

    // 查询商品评价
    selectReview() {
      if (this.reviewShow) { // 如果当前已在评价div则不查询直接返回
        return
      }
      // 如果是第一次加载评价就向服务器发送加载请求，若不是则直接显示评价div
      if (this.firstReviewLoad) {
        // 查询商品评价
        this.listReviewByProductId(1)
        this.firstReviewLoad = false
      }
      this.reviewShow = true
    },

    // 商品评价查询
    listReviewByProductId(val) {
      this.current = val
      productInfo.listReviewByProductId(this.current, this.limit, this.productId)
        .then(response => {
          this.reviews = response.data.records
          this.total = response.data.total
        })
    },

    // 商品数量增加
    numberAdd(){
      if(this.orderItem.number < this.productInfo.stock){
        ++this.orderItem.number
      }
      // 手动更新text文本值，v-model不好使了
      this.updateNumber()
    },

    // 设置商品数量
    setProductNumber() {
      let number = this.orderItem.number
      if (number == "" || number == null || number == 0) {
        this.orderItem.number = 1
      } else if (isNaN(number)) {
        this.orderItem.number = parseInt(number.substring(0, number.length - 1))
      } else if (number > this.productInfo.stock) {
        this.orderItem.number = this.productInfo.stock
      }
      // 手动更新text文本值，v-model不好使了
      this.updateNumber()
    },

    // 商品数量减少
    numberSub(){
      if(this.orderItem.number > 1){
        --this.orderItem.number
      }
      // 手动更新text文本值，v-model不好使了
      this.updateNumber()
    },

    // 手动更新商品数量 text文本值， v-model不好使了
    updateNumber() {
      this.$nextTick(() => {
        this.$refs.number.value = this.orderItem.number
      })
    },

    /* 以下为用户登录页面复制的代码 */
    // 用户登录
    login() {
      // 封装登录信息
      if (!this.packLoginInfo()) {
        return
      }
      if (this.emailCode == null || this.emailCode == "") {
        this.$message.warning("请输入验证码")
        return
      }
      login.userLogin(this.userLogin)
        .then(response => {
          this.token = response.data
          storage.setItem("token", this.token, -1)
          this.$router.go(0) // 刷新当前页
        })
    },

    // 封装登录信息
    packLoginInfo() {
      let account = this.account
      let password = this.password
      if (account == null || password == null) {
        this.$message.warning("请输入用户名和密码")
        return false
      }
      account = account.trim()
      if(account === "") {
        this.$message.warning("请输入用户名和密码")
        return false
      }
      // 对用户名进行base64编码
      this.userLogin.account = base64Util.encode(account)
      // 对验证码进行base64编码
      this.userLogin.emailCode = base64Util.encode(this.emailCode)
      // 对用户密码进行MD5加密
      this.userLogin.password = md5Util.encrypt(password)
      return true
    },

    // 发送邮箱验证码
    sendEmailCode() {
      // 封装登录信息
      if (!this.packLoginInfo()) {
        return
      }
      login.sendEmailCode(this.userLogin)
        .then(response => {
          // 设置发送状态为已发送
          this.isSend = true
          // 发送成功，提示发送成功
          this.$message.success("验证码已发送，有效时间十分钟，请注意查收！")
          // 发送按钮不可用
          this.isDisabled = true
          $("#send_code_btn").css({"background-color": "#CBCBCB", "color": "#333333"})
          // 发送按钮显示倒计时
          let i = 60
          this.sendBtnText = "已发邮箱 " + i
          let timer = setInterval(() => {
            i--
            this.sendBtnText = "已发邮箱 " + i
            if (i == 0) {
              this.isSend = false
              this.isDisabled = false
              this.sendBtnText = "重新发送"
              $("#send_code_btn").css({"background-color": "#C40000", "color": "white"})
              clearInterval(timer)
              timer = null
            }
          }, 1000)
          this.$once("hook:beforeDestroy", () => {
            if (timer) {
              clearInterval(timer)
              timer = null
            }
          })
        })

    },

    // 检测发送验证码按钮是否可用
    checkSendBtn() {
      // 如果发了验证码，这个方法就不可用
      if (this.isSend) {
        return
      }
      let account = this.account
      let password = this.password
      if (account == null || password == null) {
        this.isDisabled = true // 设置发送验证码按钮不可用
        $("#send_code_btn").css({"background-color": "#CBCBCB", "color": "#333333"})
        return
      }
      account = account.trim()
      if(account === "") {
        this.isDisabled = true // 设置发送验证码按钮不可用
        $("#send_code_btn").css({"background-color": "#CBCBCB", "color": "#333333"})
        return
      }
      this.isDisabled = false // 设置发送验证码按钮可用
      $("#send_code_btn").css({"background-color": "#C40000", "color": "white"})
    },

    // 账号登录
    accountLogin() {
      this.loginTitle = "账号登录"
      this.loginWay = true
      this.isGzh = false
    },

    // 获取二维码url
    weChatQRCode() {
      login.weChatQRCode(0) // type为0表示登录二维码 为1表示微信绑定二维码
        .then(response => {
          this.qrcodeUrl = response.data.QRCodeUrl
          this.uuid = response.data.uuid
          new qrcode(this.$refs.qrCodeUrl, {
            title: "",
            text: this.qrcodeUrl, // 需要转换为二维码的内容
          })
        })
    },

    // 创建二维码
    creatQRCode() {
      this.loginTitle = "扫码登录"
      this.loginWay = false
      // 创建二维码
      this.$nextTick(() => {
        this.weChatQRCode()
      })

      let time = 60;
      // 轮询
      let timer = setInterval(() => {
        if (--time == 0) {
          // 每隔60秒刷新一次二维码
          this.$nextTick(() => {
            this.weChatQRCode()
          })
          time = 60
        }
        // 向后端发送请求查询用户是否扫码
        login.polling(this.uuid)
          .then(response => {
            if (response.data != null) {
              this.state = response.data.state
              this.token = response.data.token
              clearInterval(this.interval)
              if (this.state == 520) { // 邮箱未绑定，跳转到注册页面
                storage.setItem("tempToken", this.token) // 设置为临时token
                // cookie.set("tempToken", this.token)
                // window.location.href = "/regist"
                this.$router.push("/regist")
              } else { // 刷新当前页
                storage.setItem("token", this.token, -1) // 设置token生命周期为半小时（默认不设置ttl为半小时）
                // cookie.set("token", this.token)
                // window.location.href = "/"
                this.$router.go(0)
              }
            }
          })
      }, 1000)
      this.$once("hook:beforeDestroy", () => {
        clearInterval(timer)
        timer = null
      })
    },

    // 公众号与微信扫码登录二维码切换
    QRCodeCut(flag) {
      this.isGzh = flag
      if (!flag) {
        // 不为真，重新加载微信登录二维码
        this.weChatQRCode()
      }
    },

  }
}
</script>

<style scoped>

</style>
