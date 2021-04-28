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
      <script>
        $(function () {
          $("#productNumber").keyup(procuctNumberKeyUp);
          $("#upKey").click(numberAdd);
          $("#downKey").click(numberSub);
        });

        function procuctNumberKeyUp(){
          var number = $("#productNumber").val();
          var inventory = $("#productBasicInformation .inventory").text();
          if(isNaN(parseInt(number)) || parseInt(number) <= 0){
            $("#productNumber").val(1);
          }
          if(isNaN(number)){
            $("#productNumber").val(parseInt(number));
          }
          if(parseInt(number) > parseInt(inventory)){
            $("#productNumber").val(inventory);
          }
        }

        function numberAdd(){
          var number = $("#productNumber").val();
          var inventory = $("#productBasicInformation .inventory").text();
          if(parseInt(number) < parseInt(inventory)){
            $("#productNumber").val(parseInt(number)+1);
          }
        }

        function numberSub(){
          var number = $("#productNumber").val();
          if(parseInt(number) > 1){
            $("#productNumber").val(parseInt(number)-1);
          }
        }
      </script>
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
            <span><del>￥{{productInfo.orignalPrice}}</del></span>
          </div>
          <div>
            <span>促销价</span>
            <span>￥</span>
            <span>{{productInfo.promotePrice}}</span>
          </div>
        </div>
        <div class="salesAndReviews">
          <span>销量 <span class="sales">{{productInfo.salesVolume}}</span></span>
          <span>累计评价 <span class="reviews">{{countReview}}</span></span>
        </div>
        <div class="numberDiv">
          <span>数量</span>
          <span><input type="text" value="1" name="number" id="productNumber"></span>
          <span class="arrow">
                <a href="#nowhere" id="upKey">
                    <span class="updown">
                        <img src="~/assets/img/site/increase.png" height="3" width="7"/>
                    </span>
                </a>
                <span class="updownMiddle"></span>
                <a href="#nowhere" id="downKey">
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
          <button id="buy">立即购买</button>
          <button id="joinShopping"><span class="glyphicon glyphicon-shopping-cart"></span>加入购物车</button>
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
        <a href="#nowhere" @click="reviewShow = false" class="productDetailTopPartSelectedLink">商品详情</a>
        <a href="#nowhere" @click="selectReview" class="productDetailTopReviewLink">累计评价 <span class="evaluateNumber">{{countReview}}</span></a>
      </div>
      <div class="cumulativeAssessmentDiv">
        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              不错的购物，包装很精细，布料也不错舒服，因为之前一直购买品牌装，很好的一次网购，生完宝宝刚刚一个多月我一百五十斤穿2XL不错
            </div>
            <div class="productEvaluateItemDate">
              2016-08-10
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            哀****莉
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              面料很好，大小也刚刚好，本来买之前还担心，犹豫，等货到了，比我心中想象的好很多，大家放心购买，很满意的购物，以后还会来??
            </div>
            <div class="productEvaluateItemDate">
              2016-08-23
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            贤*闲
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              商品：宝贝料子和手感都不错，是值这价，卖家很贴心的送了内衣带，5分好评是必须的。喜欢的可以 下手了
            </div>
            <div class="productEvaluateItemDate">
              2016-08-16
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            孤*****爱
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              衣服收到了，给朋友买的，真心不错，料子也特别好，穿起来特别有气质，跟模特穿的效果差不多。。。好评，好评
            </div>
            <div class="productEvaluateItemDate">
              2016-08-19
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            哀****莉
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              衣服不错，手感不错，试穿很有型，真心的喜欢，谢谢老板
            </div>
            <div class="productEvaluateItemDate">
              2016-07-19
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            孤*****爱
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              喜欢(⊙o⊙)哦，美美哒，值得一试！！！宽松雪纺，很舒服
            </div>
            <div class="productEvaluateItemDate">
              2016-08-10
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            这****0
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              衣服已经收到，大小很合适，面料也可以，就是感觉衣服面料有一点厚，其他都还不错，款式也好看，希望店家多出一点新款，以后回来光顾你们。?
            </div>
            <div class="productEvaluateItemDate">
              2016-07-10
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            y****心
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              很漂亮的衣服，料子有垂感，我很满意，老公也说好呢
            </div>
            <div class="productEvaluateItemDate">
              2016-07-27
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            阿*丶
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              真不错，已经迫不及待的穿上了，朋友们都说好，挺有气质的，就是上衣稍微有点肥，不过肉肉被藏起来了！有看上的赶紧下单吧！
            </div>
            <div class="productEvaluateItemDate">
              2016-08-12
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            名*****_
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              质量是真的不错，适合个子高一点的美眉穿，很漂亮?
            </div>
            <div class="productEvaluateItemDate">
              2016-08-03
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            安**°
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              再买的时候纠结了好久，最后还是选择在这里购买，一次满意的网购，让我对网购有了希望，或刚收到，因觉得物超所值，就迫不及待拍了照片，那个玻璃的声音非常脆，我个人觉得挺好的，值得拥有！
            </div>
            <div class="productEvaluateItemDate">
              2016-08-08
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            这****0
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              服务：不错
            </div>
            <div class="productEvaluateItemDate">
              2016-08-20
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            醉**心
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>

        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              强烈推荐，我的天，质量也太好了，让我情何以堪，大爱啊。说说我的体格吧，162.105斤，腰围大概是22，穿m码刚刚好。快递那么快，特地跟店家说10号要外出店家也尽心备注的很好。真的特别感动，我收藏店铺了，下次还来你们家。衣服比雪纺舒服，薄薄的，现在穿刚好，我配了高跟鞋和小白鞋都很完美。店家还送了无痕肩带，真的太细心了，给一百个好评，赞。衣服我看了没有线头，薄薄的。不说了，我赶紧发给我姐让她看看。
            </div>
            <div class="productEvaluateItemDate">
              2016-08-17
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            安**°
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              此用户没有填写评论！
            </div>
            <div class="productEvaluateItemDate">
              2016-07-21
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            乱**型
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              衣服质量真心不错很厚实，上身效果很棒哦，穿起来很舒服，很洋气。价格也很合理……赞赞赞
            </div>
            <div class="productEvaluateItemDate">
              2016-08-19
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            丶***眠
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              衣服裤子都很合身，同事笑我说这是我买得最好看最时尚的一套衣服，衣服上身效果也不错，很是满意，还送了隐形带，正好用上，质量很好穿着凉快，唯一不足的就是洗了后需要烫一下才不皱，不过不影响衣服的美，很满意。上传两张没P的照片
            </div>
            <div class="productEvaluateItemDate">
              2016-08-08
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            乱**型
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              不好意思好久才来评价 质量好 上身效果好 主要有胖子穿的漂亮衣服啦 赞赞赞
            </div>
            <div class="productEvaluateItemDate">
              2016-07-11
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            l******1
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              网购以来为数不多的质量好到超乎意料，穿上特别舒服，大小也正合适，超赞！这个店里值得好好逛逛！
            </div>
            <div class="productEvaluateItemDate">
              2016-07-10
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            盗**记
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
        <div class="productEvaluateItem">
          <div class="productEvaluateItemDesc">
            <div class="productEvaluateItemContent">
              宝贝面料很舒服，一字领也非常美，说实话是充上衣买的！犹豫了很长时间才下定决心的，很喜欢，很满意！喜欢就是最值的！
            </div>
            <div class="productEvaluateItemDate">
              2016-07-24
            </div>
          </div>
          <div class="productEvaluateItemUserInfo">
            贤*闲
            <span class="userInfoGrayPart">（匿名）</span>
          </div>
        </div>
      </div>
    </div>

    <!--产品详情与积累评价切换的js-->
    <!--<script>
      $(function () {
        $(".productDetailTopPartSelectedLink").click(productParticularsShow);
        $(".productDetailTopReviewLink").click(productCumulativeAssessmentShow);
      });

      function productParticularsShow() {
        $(".productCumulativeAssessment").css("display","none");
        $(".productParticulars").css("display","block");
      }

      function productCumulativeAssessmentShow(){
        $(".productCumulativeAssessment").css("display","block");
        $(".productParticulars").css("display","none");
      }
    </script>-->

  </div>
</template>

<script>
import SimpleSearch from "../../layouts/simpleSearch";
import category from "../../api/category";
import productInfo from "../../api/productInfo";

export default {
  name: "_productId",
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

    }
  },
  created() {
    this.productId = this.$route.params.productId
    this.getProductInfoById()
    // this.getCategoryInfo()
  },
  methods:{
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

    }


  }
}
</script>

<style scoped>

</style>
