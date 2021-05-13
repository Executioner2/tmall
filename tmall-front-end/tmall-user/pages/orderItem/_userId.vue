<template>
  <div>

    <!--这里放简单搜索栏-->
    <simple-search/>

    <!--这里放结算里面嵌套订单项-->
    <script>
      $(function () {
        initProductMoney();
        $(".cartPullSelectBtn").click(fullSelect);
        $("#orderItemContentTable .cartSelect").click(multipleChoice);
        $("#orderItemContentTable .productNumber").keyup(productNumberFun);
        $("#orderItemContentTable .tableBody .numberSub").click(productNumberSub);
        $("#orderItemContentTable .tableBody .numberAdd").click(productNumberAdd);
        $("#orderItemContentTable .tableBody .deleteProductItem").click(deleteProductItem);
      });

      /*全选*/
      function fullSelect() {
        let cartPullSelectrctSrc = $(".cartPullSelect").attr("src");
        let totalAmount = 0;
        let productNumberSum = 0;
        if(cartPullSelectrctSrc.indexOf("cartSelected.png") == -1){  /*如果没选中*/
          $(".cartPullSelect").attr("src","~assets/img/site/cartSelected.png");
          $(".cartSelect").attr("src","~assets/img/site/cartSelected.png");
          let tableBody= $("#orderItemContentTable .tableBody");
          tableBody.each(function (index) {
            $(this).css("background-color","#FFF8E1");
            totalAmount += parseFloat($(".tableBody :eq("+index+")").attr("productMoney"));
            productNumberSum += parseInt(parseFloat($(".tableBody :eq("+index+") .productNumber").val()));
          });
        }else{
          $(".cartPullSelect").attr("src","~assets/img/site/cartNotSelected.png");
          $(".cartSelect").attr("src","~assets/img/site/cartNotSelected.png");
          let tableBody= $("#orderItemContentTable .tableBody");
          tableBody.each(function () {
            $(this).css("background-color","white");
          });
        }
        $("#settieAccountsButton .totalAmount").attr("totalAmount",totalAmount);
        totalAmount = formattingMoney(totalAmount);
        $("#settieAccountsButton .totalAmount").text("￥"+totalAmount);
        $("#settieAccountsButton .productNumberSum").text(productNumberSum);
      }

      /*单选*/
      function multipleChoice(){
        let trNum = $("#orderItemContentTable tr").length - 1;
        let selectedNum = 0;
        let tr = this.parentNode.parentNode.parentNode;
        let totalAmount = parseFloat($("#settieAccountsButton .totalAmount").attr("totalAmount"));
        let productMoney = parseFloat($("#orderItemContentTable tr:eq("+tr.rowIndex+")").attr("productMoney"));
        let productNumberSum = parseInt($("#settieAccountsButton .productNumberSum").text());
        let productNumber = parseInt($("#orderItemContentTable tr:eq("+tr.rowIndex+") .productNumber").val());
        if($(this).attr("src").indexOf("cartSelected.png") == -1){
          $(this).attr("src","~assets/img/site/cartSelected.png");
          tr.style.backgroundColor = "#FFF8E1";
          totalAmount += productMoney;
          productNumberSum += productNumber;
        }else{
          $(this).attr("src","~assets/img/site/cartNotSelected.png");
          $(".cartPullSelect").attr("src","~assets/img/site/cartNotSelected.png");
          tr.style.backgroundColor = "white";
          totalAmount -= productMoney;
          productNumberSum -= productNumber;
        }
        $("#settieAccountsButton .totalAmount").attr("totalAmount", totalAmount);
        totalAmount = formattingMoney(totalAmount);
        $("#settieAccountsButton .totalAmount").text("￥" + totalAmount);
        $("#settieAccountsButton .productNumberSum").text(productNumberSum);
        for (var i = 0; i < trNum; i++) {
          if($("#orderItemContentTable tr:eq("+(i+1)+") .cartSelect").attr("src").indexOf("cartNotSelected.png") == -1){
            selectedNum++;
          }
        }
        if(selectedNum == 0){
        }else if(selectedNum == trNum){
          $(".cartPullSelect").attr("src","~assets/img/site/cartSelected.png");
        }
      }

      /*计算金额*/
      function initProductMoney(){
        let tr = $("#orderItemContentTable tr");
        tr.each(function (index) {
          if(index > 0) {
            let price = $("#orderItemContent tr:eq("+index+")").attr("price");
            let number = $("#orderItemContent tr:eq("+index+") .productNumber").val();
            let money = price * number;
            $("#orderItemContent tr:eq("+index+")").attr("productMoney",money);
            money = formattingMoney(money);
            $("#orderItemContent tr:eq("+index+") .productMoney").text(money);
          }
        })
      }

      /*输入产品数量*/
      function productNumberFun(){
        let index = this.parentNode.parentNode.parentNode.parentNode.rowIndex;
        let inventory = parseInt($("#orderItemContentTable tr:eq("+index+")").attr("inventory"));
        if(isNaN($(this).val())){
          $(this).val(parseInt($(this).val()));
        }
        if(parseInt($(this).val()) == 0){
          $(this).val(1);
        }else if(parseInt($(this).val()) > inventory){
          $(this).val(inventory);
        }
        initProductMoney();
      }

      /*产品数量+1*/
      function productNumberAdd(){
        let index = this.parentNode.parentNode.parentNode.rowIndex;
        let inventory = parseInt($("#orderItemContentTable tr:eq("+index+")").attr("inventory"));
        let tr = $("#orderItemContentTable tr:eq("+index+") .productNumber");
        if(parseInt(tr.val()) <= inventory){
          tr.val(parseInt(tr.val())+1);
        }
        initProductMoney();
      }

      /*产品数量—1*/
      function productNumberSub(){
        let index = this.parentNode.parentNode.parentNode.rowIndex;
        let tr = $("#orderItemContentTable tr:eq("+index+") .productNumber");
        if(parseInt(tr.val()) > 1){
          tr.val(parseInt(tr.val())-1);
        }
        initProductMoney();
      }

      /*删除当前产品项*/
      function deleteProductItem() {
        this.parentNode.parentNode.remove();
      }

      /*格式化货币*/
      function formattingMoney(money){
        money = money.toString()
        let length = parseInt(money).toString().length;
        let rem = length % 3;
        let newMoney = new Array();
        let flag = false;
        for (var i = 0; i < money.length; i++) {
          if (money[i] == '.'){
            flag = money.substring(i);
            break;
          }
        }
        for (var i = 0; i < length; i++) {
          if(i % 3 - rem == 0 && i > 0){
            newMoney += ',';
          }
          newMoney += money[i];
        }
        if(!flag){
          newMoney += '.00';
        }else{
          newMoney += flag;
        }
        return newMoney;
      }

    </script>

    <div class="settieAccountsButton" id="settieAccountsButton">
      <div class="settieAccountsUpDiv">
        <span>已选商品 (不含运费)</span>
        <span class="settlementAmount totalAmount" id="settlementAmount1" totalAmount="0">￥0.00</span>
        <button disabled>结 算</button>
      </div>

      <!--这里放订单项内容-->
      <div class="orderItemContent" id="orderItemContent">
        <table class="orderItemContentTable" id="orderItemContentTable">
          <tr id="tableTitle">
            <td>
              <a href="javascript:void(0)" class="cartPullSelectBtn">
                <img class="cartPullSelect" src="~assets/img/site/cartNotSelected.png"/>
              </a>
              <span>全选</span>
            </td>
            <td>商品信息</td>
            <td>单价</td>
            <td>数量</td>
            <td>金额</td>
            <td>操作</td>
          </tr>
          <tr class="tableBody" price="5306" productMoney="0" inventory="123">
            <td>
              <a href="javascript:void(0)" class="cartSelectBtn">
                <img class="cartSelect" src="~assets/img/site/cartNotSelected.png"/>
              </a>
              <img class="productImage" src="~assets/img/jieSuanLingShiTu/3665.jpg" />
            </td>
            <td>
              <div class="productInformation">
                <a href="#">美国iRobot扫地机器人吸尘器全自动家用智能扫地机650 天猫电器城</a>
                <div class="cartProductLinkInnerDiv">
                  <img src="~assets/img/site/creditcard.png" height="11" width="16"/>
                  <img src="~assets/img/site/7day.png" height="16" width="16"/>
                  <img src="~assets/img/site/promise.png" height="16" width="16"/>
                </div>
              </div>
            </td>
            <td>
              <div><del>￥7580.0</del></div>
              <div>￥5306.0</div>
            </td>
            <td>
              <div>
                <a href="javascript:void(0)" class="numberSub">-</a>
                <span><input type="text" class="productNumber" name="productNumber" value="2"></span>
                <a href="javascript:void(0)" class="numberAdd">+</a>
              </div>
            </td>
            <td>
              <span>￥</span><span class="productMoney"></span>
            </td>
            <td>
              <a href="javascript:void(0)" class="deleteProductItem">删除</a>
            </td>
          </tr>
          <tr class="tableBody" price="152.75" productMoney="0" inventory="12">
            <td>
              <a href="javascript:void(0)" class="cartSelectBtn">
                <img class="cartSelect" src="~assets/img/site/cartNotSelected.png"/>
              </a>
              <img class="productImage" src="~assets/img/jieSuanLingShiTu/8510.jpg" />
            </td>
            <td>
              <div class="productInformation">
                <a href="#">阔腿裤三件套装女夏装2016新款大码雪纺时尚休闲气质棉麻九分裤潮</a>
                <div class="cartProductLinkInnerDiv">
                  <img src="~assets/img/site/creditcard.png" height="11" width="16"/>
                  <img src="~assets/img/site/7day.png" height="16" width="16"/>
                  <img src="~assets/img/site/promise.png" height="16" width="16"/>
                </div>
              </div>
            </td>
            <td>
              <div><del>￥235.0</del></div>
              <div>￥152.75</div>
            </td>
            <td>
              <div>
                <a href="javascript:void(0)" class="numberSub">-</a>
                <span><input type="text" class="productNumber" name="productNumber" value="1"></span>
                <a href="javascript:void(0)" class="numberAdd">+</a>
              </div>
            </td>
            <td>
              <span>￥</span><span class="productMoney"></span>
            </td>
            <td>
              <a href="javascript:void(0)" class="deleteProductItem">删除</a>
            </td>
          </tr>
        </table>
      </div>

      <div class="settieAccountsDownDiv">
        <a href="javascript:void(0)" class="cartPullSelectBtn">
          <img class="cartPullSelect" src="~assets/img/site/cartNotSelected.png"/>
        </a>
        <span>全选</span>
        <span class="rightSpan pull-right">
                <span>已选商品 <span class="productNumberSum">0</span> 件</span>
                <span>合计 (不含运费)：</span>
                <span class="settlementAmount totalAmount" id="settlementAmount2" totalAmount="0">￥0.00</span>
                <button disabled>结 算</button>
            </span>
      </div>
    </div>

  </div>
</template>

<script>
import SimpleSearch from "../../layouts/simpleSearch";
export default {
  name: "_userId",
  components: {SimpleSearch}
}
</script>

<style scoped>

</style>
