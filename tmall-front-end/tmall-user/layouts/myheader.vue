<template>
  <div>
    <!--这里放nav-->
    <nav class="top">
      <div class="navTopDiv">
        <a href="/">
          <span class="glyphicon glyphicon-home redColor"></span>天猫首页
        </a>
        <span>喵，欢迎来天猫</span>
        <a href="/login">请登录</a>
        <span class="pull-right">
            <a :href="'/order/' + userId">我的订单</a>
            <a href="#">
                <span class="glyphicon glyphicon-shopping-cart redColor"></span>购物车<strong>0</strong>件
            </a>
        </span>
      </div>
    </nav>
  </div>
</template>

<script>
export default {
  data() {
    return {
      userId: 123,
    }
  },
  name: "myheader"
}
</script>

<style scoped>

</style>
