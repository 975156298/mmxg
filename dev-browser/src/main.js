import Vue from 'vue'

import 'normalize.css/normalize.css'// A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN'

import '@/styles/index.scss' // global css
import * as filters from './filters' // global filters

import App from './App'
import router from './router'
import store from './store'
import 'font-awesome/css/font-awesome.min.css'
import '@/permission' // permission control

Vue.use(ElementUI, { locale })
// register global utility filters.

Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false
new Vue({
	el: '#app',
	router,
	store,
	template: '<App/>',
	components: { App }
})
