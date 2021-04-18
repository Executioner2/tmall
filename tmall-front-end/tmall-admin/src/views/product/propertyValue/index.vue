<template>
  <div>
    <!-- 显示属性值 -->
    <div class="editPropertyValue" id="editPropertyValue">
      <div class="editDiv">
        <template v-for="item in list">
          <div class="editItem">
            <div class="propertyName">{{ item.propertyName }}</div>
            <div><input :ref="item.propertyValueId" type="text" class="productValue" v-model="item.propertyValue" @blur="save(item)"></div>
          </div>
        </template>
      </div>
    </div>

  </div>
</template>

<script>
  import propertyValue from "@/api/product/propertyValue";

  export default {
    data() {
      return {
        list: [], // 结果集合
        productId: null, // 产品id
      }
    },
    created() {
      this.productId = this.$route.params.id
      this.show()
    },
    methods: {
      // 显示属性和属性值
      show() {
        propertyValue.show(this.productId)
          .then(response => {
            this.list = response.data
          })
      },

      // 保存属性
      save(item) {
        propertyValue.update(item)
          .then(response => {
            // 搞一个延迟执行设置颜色，时间到了恢复灰色
            this.$refs[item.propertyValueId][0].style.borderColor="green"
            setTimeout(() => {
              this.$refs[item.propertyValueId][0].style.borderColor="#D3D3D3"
            }, 2000)
          })
        .catch(error => {
          // 搞一个延迟执行设置颜色，时间到了恢复灰色
          this.$refs[item.propertyValueId][0].style.borderColor="red"
          setTimeout(() => {
            this.$refs[item.propertyValueId][0].style.borderColor="#D3D3D3"
          }, 2000)
        })
      }
    }

  }
</script>

<style>
  /*下面是产品属性编辑页面的CSS*/
  #editPropertyValue{
    width: 100%;
    margin-top: 40px;
  }
  #editPropertyValue .editDiv{
    width: 900px;
    margin: 0 auto;
  }
  #editPropertyValue .editDiv div{
    display: inline-block;
  }
  #editPropertyValue .editItem{
    height: 40px;
    margin-right: 40px;
  }
  #editPropertyValue .editItem input{
    border: #D3D3D3 1px solid;
  }
  #editPropertyValue .propertyName{
    width: 120px;
  }
  #editPropertyValue .productValue{
    width: 250px;
  }
</style>
