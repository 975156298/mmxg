<template>
  <div class="dashboard-container">
    <div class="dashboard-text">欢迎&nbsp;{{name}}&nbsp;访问名码享购后台管理系统</div>
    <!--<div class="dashboard-text">role:<span :key='role'>{{role}}</span></div>-->
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
	name: 'dashboard',
	computed: {
		...mapGetters([
			'name',
			'role'
		])
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
    display: flex;
    align-items: center;
    flex-flow: column;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
