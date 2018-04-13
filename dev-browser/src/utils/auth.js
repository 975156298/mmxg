import Cookies from 'js-cookie'

const loginState = 'isLogin'

export function getLoginState() {
	return Cookies.get(loginState)
}

export function setLoginState(state) {
	return Cookies.set(loginState, state)
}

export function removeLoginState() {
	return Cookies.remove(loginState)
}
