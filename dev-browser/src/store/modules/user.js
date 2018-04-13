import { login, logout, getInfo } from '@/api/login'
import { getLoginState, setLoginState, removeLoginState } from '@/utils/auth'

function formatMenus(menus) {
  menus.forEach((menu, index) => {
    delete menu.parent
    menu.meta = {title: menu.title, icon: menu.icon}
    delete menu.icon
    delete menu.createdAt
    delete menu.deletedAt
    if (menu.children) {
      formatMenus(menu.children)
    }
  })
  return menus
}

const user = {
	state: {
		loginState: getLoginState(),
		name: '',
		avatar: '',
		role: '',
    menus :[]
	},

	mutations: {
		SET_LOGIN_STATE: (state, loginState) => {
			state.loginState = loginState
		},
		SET_NAME: (state, name) => {
			state.name = name
		},
		SET_AVATAR: (state, avatar) => {
			state.avatar = avatar
		},
		SET_ROLE: (state, role) => {
			state.role = role
		},
    SET_MENUS :(state, menus) => {
      state.menus = menus
    }
	},

	actions: {
		// 获取用户信息
		GetInfo({ commit }) {
			return new Promise((resolve, reject) => {
				getInfo().then(response => {
			 		const data = response.data
          if(data.type == 'ADMIN'){
            commit('SET_ROLE', data.type)
            commit('SET_NAME', data.name)
          }else {
            reject()
          }
			// 		commit('SET_AVATAR', data.avatar)
          // commit('SET_MENUS',formatMenus(data.menus))
					resolve(response)
				}).catch(error => {
					reject(error)
				})
			})
		},

		// 登出
		LogOut({ commit, state }) {
			return new Promise((resolve, reject) => {
				logout(state.loginState).then(() => {
					commit('SET_LOGIN_STATE', '')
					commit('SET_ROLE', '')
					removeLoginState()
					resolve()
				}).catch(error => {
					reject(error)
				})
			})
		},

		// 前端 登出
		FedLogOut({ commit }) {
			return new Promise(resolve => {
				commit('SET_LOGIN_STATE', '')
				removeLoginState()
				resolve()
			})
		}
	}
}

export default user
