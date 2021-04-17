<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item,index) in levelList" :key="item.path">
        <span v-if="item.meta.noBar || index==levelList.length-1" class="no-redirect">{{ item.meta.title }}</span>
        <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script>
import pathToRegexp from 'path-to-regexp'
import cookies from 'js-cookie'
import strUtil from "@/utils/myUtil/strUtil";

export default {
  data() {
    return {
      levelList: null
    }
  },
  watch: {
    $route() {
      this.getBreadcrumb()
    }
  },
  created() {
    this.getBreadcrumb()
  },
  methods: {
    getBreadcrumb() {
      // only show routes with meta.title
      let matched = this.$route.matched.filter(item => item.meta && item.meta.title)
      // const first = matched[0]

      // if (!this.isDashboard(first)) {
      //   matched = [{ path: '/dashboard', meta: { title: 'Dashboard' }}].concat(matched)
      // }

      this.levelList = matched.filter(item => item.meta && item.meta.title && item.meta.breadcrumb !== false)
      this.levelList.forEach(function (value) {
        if (value.meta.title === "categoryName") {
          value.meta.title = cookies.get("categoryName")
        }
      })

    },

    isDashboard(route) {
      const name = route && route.name
      if (!name) {
        return false
      }
      return name.trim().toLocaleLowerCase() === 'Dashboard'.toLocaleLowerCase()
    },
    pathCompile(path) {
      // To solve this problem https://github.com/PanJiaChen/vue-element-admin/issues/561
      const { params } = this.$route
      var toPath = pathToRegexp.compile(path)
      return toPath(params)
    },
    handleLink(item) {
      // 如果转发路径末尾是 /:id  表示动态给个id
      let redirectUrl = item.redirect
      let urlPrefix = ""
      let urlSuffix = ""
      if (!strUtil.isEmpty(redirectUrl)) {
        urlPrefix = redirectUrl.substring(0, redirectUrl.length - 3) // 取前面
        urlSuffix = redirectUrl.substring(redirectUrl.length - 4, redirectUrl.length) // 取后面/:id的长度
      }
      // 如果是动态的，就从当前的url中取得最后
      if (urlSuffix === "/:id") {
        redirectUrl = urlPrefix + cookies.get('urlLast')
        this.$router.push(redirectUrl)
        return
      } else {
        const {redirect, path} = item
        if (redirect) {
          this.$router.push(redirect)
          return
        }
        this.$router.push(this.pathCompile(path))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 50px;
  margin-left: 8px;

  .no-redirect {
    color: #97a8be;
    cursor: text;
  }
}
</style>
