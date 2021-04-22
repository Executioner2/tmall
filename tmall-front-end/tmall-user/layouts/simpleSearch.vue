<template>
  <div>
    <div class="simpleSearchBox" id="simpleSearchBox">
      <a href="#">
        <img class="logo" id="simpelLogo" src="~/assets/img/site/simpleLogo.png"/>
      </a>
      <div class="pull-right">
        <div class="searchDiv">
          <input type="text" name="keyword" placeholder="平衡车 原汁机">
          <button class="searchButton">搜天猫</button>
        </div>
        <div class="searchBelow">
          <span><a href="#">冰箱</a><span>|</span></span>
          <span><a href="#">空调</a><span>|</span></span>
          <span><a href="#">女表</a><span>|</span></span>
          <span><a href="#">男表</a></span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "simpleSearch"
}
</script>

<style scoped>

</style>
