<template>
  <div>
    <!-- 搜索框 -->
    <search/>

    <!-- 轮播和导航-->
    <div id="searchAndNav" class="searchAndNav">
      <img id="catear" src="~/assets/img/site/catear.png" style="left: 500px; top: 38px; display: none"/>   <!--间隔120px-->
      <div class="navAndRotDiv" id="navAndRotDiv">
        <div class="navDiv">
          <div class="head">
            <span class="glyphicon glyphicon-th-list"></span>
            <span id="text">商品分类</span>
          </div>
          <div class="rightMenu">
            <span ref="snr1" @mouseenter="catearShow($refs.snr1)" @mouseleave="catearNone">
                <a href="#"><img src="~/assets/img/site/chaoshi.png" height="72" width="180"/></a>
            </span>
            <span ref="snr2" @mouseenter="catearShow($refs.snr2)" @mouseleave="catearNone">
              <a href="#"><img src="~/assets/img/site/guoji.png" height="72" width="180"/></a>
            </span>
            <span ref="snr3" @mouseenter="catearShow($refs.snr3)" @mouseleave="catearNone">
              <a href="#">平板电视</a>
            </span>
            <span ref="snr4" @mouseenter="catearShow($refs.snr4)" @mouseleave="catearNone">
              <a href="#">马桶</a>
            </span>
            <span ref="snr5" @mouseenter="catearShow($refs.snr5)" @mouseleave="catearNone">
              <a href="#">沙发</a>
            </span>
            <span ref="snr6" @mouseenter="catearShow($refs.snr6)" @mouseleave="catearNone">
              <a href="#">电热水器</a>
            </span>
          </div>
        </div>
      </div>

      <div id="lunBoDiv">
        <div id="carousel-of-product" class="carousel slide carousel-of-product" data-ride="carousel" data-interval="2000">
          <ol class="carousel-indicators">
            <li data-target="#carousel-of-product" data-slide-to="0" class="active"></li>
            <li data-target="#carousel-of-product" data-slide-to="1"></li>
            <li data-target="#carousel-of-product" data-slide-to="2"></li>
            <li data-target="#carousel-of-product" data-slide-to="3"></li>
            <li data-target="#carousel-of-product" data-slide-to="4"></li>
          </ol>
          <div class="carousel-inner" role="listbox">
            <div class="item active"><img src="~/assets/img/lunbo/1.jpg"/></div>
            <div class="item"><img src="~/assets/img/lunbo/2.jpg"/></div>
            <div class="item"><img src="~/assets/img/lunbo/3.jpg"/></div>
            <div class="item"><img src="~/assets/img/lunbo/4.jpg"/></div>
            <div class="item"><img src="~/assets/img/lunbo/5.jpg"/></div>
          </div>
        </div>
      </div>
    </div>
    <div style="clear: both"></div>

    <!--这里放商品分类-->
    <div class="categoryWithCarousel" @mouseleave="floatMenuHide" id="categoryWithCarousel">
      <div class="leftMenu">
        <div v-for="(item, index) in list" :key="index" class="link" @mouseenter="floatMenuBlock(item.id)">
          <span class="glyphicon glyphicon-link"></span>
          <a :href="'/category/' + item.id">{{item.name}}</a>
        </div>
      </div>
      <div id="rightFloatMenu" class="rightFloatMenu">
        <div class="row" v-for="(item, index) in floatMenu" :key="index">
          <span v-for="(it) in item" :key="it.id" v-if="it.subTitle != '' && it.subTitle != null">
              <!-- 随机上色 -->
              <a :href="'/product/' + it.id" :style="Math.random() > 0.8 ? 'color: #87CEFA': '' ">{{it.subTitle}}</a>
          </span>
        </div>
      </div>
    </div>

    <!--这里是商品主体部分-->
    <div id="productBody">
      <div class="productList" id="productList">
        <div class="productDiv" v-for="(item, index) in list" :key="index">
          <div class="productTitle">{{item.name}}</div>
          <div class="productItem" v-for="(product) in item.params.hotList" :key="product.id">
            <a href="#" class="product">
              <img :src="product.params.imageUrl"/>
            </a>
            <a href="#" class="productItemDescLink">
              <span class="productItemDesc">[热销] {{product.name}}</span>
            </a>
            <span>{{product.promotePrice}}</span>
          </div>
        </div>
        <div class="endDiv" id="endDiv">
          <img src="~/assets/img/site/end.png" height="53" width="82"/>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
  import search from "../layouts/search"
  import home from "../api/home"

  export default {
    components: {search},
    data() {
      return {
          list: [], // 后端返回的首页信息
          floatMenu: null, // 浮动菜单的值
      }
    },
    created() {
      this.listCategoryAndHotProduct()
    },
    methods: {
      // 首页显示分类列表和热销商品
      listCategoryAndHotProduct() {
        home.listCategoryAndHotProduct()
          .then(response => {
            this.list = response.data
          })
      },

      // 显示浮动菜单
      floatMenuBlock(val) {
        for (let i = 0; i < this.list.length; i++) {
          if (val == this.list[i].id) {
            this.floatMenu = this.list[i].params.floatMenu
            break
          }
        }
        // 让跑马灯的点击按钮置于最下层
        $(".carousel-indicators").css("z-index","0");
      },

      // 隐藏浮动菜单
      floatMenuHide() {
        this.floatMenu = null;
        // 让跑马灯的点击按钮恢复原来层级
        $(".carousel-indicators").css("z-index","2");
      },

      // 导航栏浮动图片隐藏
      catearNone(){
        $("#catear").css("display","none");
      },

      // 导航栏浮动图片显示
      catearShow(val){
        let width = parseInt($(val).css("width"))/2+3;
        let left = $(val).position().left+width;
        let top = $(val).position().top-20;
        let catear = $("#catear");
        catear.css({"left":""+left+"px","top":""+top+"px"});
        catear.fadeIn(500);
      }

    }
  }

</script>
