import Vue from 'vue'
import Router from 'vue-router'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
 * hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
 *                                if not set alwaysShow, only more than one route under the children
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noredirect           if `redirect:noredirect` will no redirct in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
 **/
export const constantRouterMap = [
  {path: '/login', component: () => import('@/views/login/index'), hidden: true},
  {path: '/404', component: () => import('@/views/404'), hidden: true},

  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    children: [{
      path: 'dashboard',
      component: () => import('@/views/dashboard/index'),
      name: 'dashboard',
      meta: {title: '首页', noCache: true}
    }]
  },
  {
    path: '/jurisdiction',
    component: Layout,
    children: [
      // {
      //   path: 'index',
      //   name: 'backgroundUser',
      //   component: () => import('@/views/backgrounduser/index'),
      //   meta: { title: '后台用户管理', noCache: true }
      // },
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/user/index'),
        meta: {title: '用户管理', noCache: true}
      }
    ]
  },
  {
    path: '/bpmanage',
    component: Layout,
    children: [
      {
        path: 'bpsetup',
        name: 'bpsetup',
        component: () => import('@/views/bpsetup/index'),
        meta: {title: '当日积分价格', noCache: true}
      }
    ]
  },
  {
    path: '/privateEquityPoints',
    component: Layout,
    children: [
      {
        path: 'privateEquityPoints',
        name: 'privateEquityPoints',
        component: () => import('@/views/privateEquityPoints/index'),
        meta: {title: '私募积分新增记录', noCache: true}
      }
    ]
  },
  {
    path: '/privateTransaction',
    component: Layout,
    children: [
      {
        path: 'privateTransaction',
        name: 'privateTransaction',
        component: () => import('@/views/privateTransaction/index'),
        meta: {title: '私募积分交易记录', noCache: true}
      }
    ]
  },
  {
    path: '/priceLog',
    component: Layout,
    children: [
      {
        path: 'priceLog',
        name: 'priceLog',
        component: () => import('@/views/priceLog/index'),
        meta: {title: '价格记录', noCache: true}
      }
    ]
  },
  /*{
    path: '/invitation',
    component: Layout,
    children: [
      {
        path: 'invitation',
        name: 'invitation',
        component: () => import('@/views/invitation/index'),
        meta: { title: '邀请码', noCache: true }
      }
    ]
  },*/
  {
    path: '/refill',
    component: Layout,
    children: [
      {
        path: 'refill',
        name: 'refill',
        component: () => import('@/views/refill/index'),
        meta: {title: '账户充值', noCache: true}
      }
    ]
  },
  {
    path: '/withdrawals',
    component: Layout,
    children: [
      {
        path: 'withdrawals',
        name: 'withdrawals',
        component: () => import('@/views/withdrawals/index'),
        meta: {title: '账户提现', noCache: true}
      }
    ]
  },
  {path: '*', redirect: '/404', hidden: true}
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
})

