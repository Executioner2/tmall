import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  // 分类管理
  {
    path: '/',
    component: Layout,
    redirect: '/category/categoryInfo',
    meta: { title: '分类管理', icon: 'nested' },
    children: [
      {
        path: 'category/categoryInfo',
        name: '分类管理',
        component: () => import('@/views/category/categoryInfo/index'),
        meta: { title: '分类管理'}
      },
      {
        path: '/',
        component: () => import('@/views/category/index'),
        name: 'categoryName',  // 要根据name做动态面包屑，这个参数要有
        meta: { title: 'categoryName', noBar: true}, // noBar 自定义属性，面包屑不可用
        hidden: true,
        children: [
          {
            path: 'category/property/:id',
            name: '属性管理',
            component: () => import('@/views/category/property/index'),
            meta: { title: '属性管理'},
            hidden: true
          }
        ]
      },
      {
        path: 'product',
        hidden: true,
        redirect: '/product/:id',
        name: 'categoryName',  // 要根据name做动态面包屑，这个参数要有
        meta: { title: 'categoryName'},
        component: () => import('@/views/product/index'), // Parent router-view
        children: [
          {
            path: ':id',
            name: '产品管理',
            meta: { title: '产品管理'},
            component: () => import('@/views/product/productInfo/index'),
            hidden: true,
          },
          {
            path: '/',
            name: 'productName',  // 要根据name做动态面包屑，这个参数要有
            meta: { title: 'productName', noBar: true},
            component: () => import('@/views/product/index'), // Parent router-view
            hidden: true,
            children: [
              {
                path: 'productImage/:id',
                component: () => import('@/views/product/productImage/index'),
                meta: { title: '产品图片管理'},
                hidden: true
              },
              {
                path: 'propertyValue/:id',
                component: () => import('@/views/product/propertyValue/index'),
                meta: { title: '编辑产品属性'},
                hidden: true
              },
            ]
          },
        ]
      }

    ]
  },

  // 用户管理
  {
    path: '/user',
    component: Layout,
    redirect: '/user/userInfo',
    name: '用户管理',
    meta: { title: '用户管理', icon: 'el-icon-user-solid' },
    children: [
      {
        path: 'userInfo',
        name: '用户列表',
        component: () => import('@/views/user/userInfo/index'),
        meta: { title: '用户列表', icon: 'el-icon-s-custom' }
      },
      {
        path: 'auth',
        name: '用户认证审批',
        component: () => import('@/views/user/auth/index'),
        meta: { title: '用户认证审批', icon: 'el-icon-s-check' }
      }
    ]
  },

  // 订单管理
  {
    path: '/order',
    component: Layout,
    redirect: '/order/orderInfo',
    children: [
      {
        path: 'orderInfo',
        name: '订单管理',
        component: () => import('@/views/order/orderInfo/index'),
        meta: { title: '订单管理', icon: 'form' }
      }
    ]
  },

  // 热销商品数据可视化
  {
    path: '/statistics',
    component: Layout,
    redirect: '/statistics/info',
    children: [
      {
        path: '/info',
        name: '数据可视化',
        component: () => import('@/views/statistics/index'),
        meta: { title: '数据可视化', icon: 'el-icon-s-data' }
      }
    ]
  },


  // {
  //   path: '/example',
  //   component: Layout,
  //   redirect: '/example/table',
  //   name: 'Example',
  //   meta: { title: 'Example', icon: 'el-icon-s-help' },
  //   children: [
  //     {
  //       path: 'table',
  //       name: 'Table',
  //       component: () => import('@/views/table/index'),
  //       meta: { title: 'Table', icon: 'table' }
  //     },
  //     {
  //       path: 'tree',
  //       name: 'Tree',
  //       component: () => import('@/views/tree/index'),
  //       meta: { title: 'Tree', icon: 'tree' }
  //     }
  //   ]
  // },
  //
  // {
  //   path: '/form',
  //   component: Layout,
  //   children: [
  //     {
  //       path: 'index',
  //       name: 'Form',
  //       component: () => import('@/views/form/index'),
  //       meta: { title: 'Form', icon: 'form' }
  //     }
  //   ]
  // },
  //
  // {
  //   path: '/nested',
  //   component: Layout,
  //   redirect: '/nested/menu1',
  //   name: 'Nested',
  //   meta: {
  //     title: 'Nested',
  //     icon: 'nested'
  //   },
  //   children: [
  //     {
  //       path: 'menu1',
  //       component: () => import('@/views/nested/menu1/index'), // Parent router-view
  //       name: 'Menu1',
  //       meta: { title: 'Menu1' },
  //       children: [
  //         {
  //           path: 'menu1-1',
  //           component: () => import('@/views/nested/menu1/menu1-1'),
  //           name: 'Menu1-1',
  //           meta: { title: 'Menu1-1' }
  //         },
  //         {
  //           path: 'menu1-2',
  //           component: () => import('@/views/nested/menu1/menu1-2'),
  //           name: 'Menu1-2',
  //           meta: { title: 'Menu1-2' },
  //           children: [
  //             {
  //               path: 'menu1-2-1',
  //               component: () => import('@/views/nested/menu1/menu1-2/menu1-2-1'),
  //               name: 'Menu1-2-1',
  //               meta: { title: 'Menu1-2-1' }
  //             },
  //             {
  //               path: 'menu1-2-2',
  //               component: () => import('@/views/nested/menu1/menu1-2/menu1-2-2'),
  //               name: 'Menu1-2-2',
  //               meta: { title: 'Menu1-2-2' }
  //             }
  //           ]
  //         },
  //         {
  //           path: 'menu1-3',
  //           component: () => import('@/views/nested/menu1/menu1-3'),
  //           name: 'Menu1-3',
  //           meta: { title: 'Menu1-3' }
  //         }
  //       ]
  //     },
  //     {
  //       path: 'menu2',
  //       component: () => import('@/views/nested/menu2/index'),
  //       name: 'Menu2',
  //       meta: { title: 'menu2' }
  //     }
  //   ]
  // },
  //
  // {
  //   path: 'external-link',
  //   component: Layout,
  //   children: [
  //     {
  //       path: 'https://panjiachen.github.io/vue-element-admin-site/#/',
  //       meta: { title: 'External Link', icon: 'link' }
  //     }
  //   ]
  // },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
