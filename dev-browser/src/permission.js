import router from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css'// Progress 进度条样式
import { Message } from 'element-ui'
import { getLoginState } from "./utils/auth"
import { login, logout, getInfo } from '@/api/login'

const whiteList = ['/login','/logout'] // 不重定向白名单
router.beforeEach((to, from, next) => {
	NProgress.start()
  if(getLoginState() === 'true'){
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      if (!store.getters.role) {

        store.dispatch('GetInfo').then(res => { // 拉取用户信息
          window.myinfo = res.data
          next()
        }).catch((error) => {
          store.dispatch('FedLogOut').then(() => {
            Message.error('验证失败，请重新登录!')
            logout();
            next({ path: '/login' })
          })
        })
      } else {
        next()
      }
    }
  }else{
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next('/login')
      NProgress.done()
    }
  }
})

router.afterEach(() => {
	NProgress.done() // 结束Progress
})
