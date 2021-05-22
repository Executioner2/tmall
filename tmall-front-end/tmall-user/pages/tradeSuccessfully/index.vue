<template>
  <div>
    <simple-search/>

    <div class="theGoodsSuccessfully" id="theGoodsSuccessfully">
      <img src="~assets/img/site/orderFinish.png" height="36" width="35"/>
      <span>交易已经成功，卖家将收到您的货款。</span>
    </div>
  </div>
</template>

<script>
import SimpleSearch from "../../layouts/simpleSearch";
export default {
  components: {SimpleSearch}
}
</script>

