import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import router from './router'
import store from './store'
import './plugins/element.js'

Vue.config.productionTip = false

axios.defaults.baseURL='http://localhost:2333'
let token = localStorage.getItem('token')
if (token != null) {
  axios.defaults.headers.common['Authorization'] = 'Bearer ' + localStorage.getItem('token')
}

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
