// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue";
import App from "./App";
import router from "./router";
// import store from "./store";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
// import httpService from './service/http-service'
import axios from "axios";

Vue.prototype.$axios = axios;
Vue.config.productionTip = false;
// Vue.config.axios = true;
Vue.use(ElementUI);
/* eslint-disable no-new */

// 请求拦截
axios.interceptors.request.use(config => {
  if ("prod-api/common/login" != config.url) {
    if (localStorage.getItem("token")) {
      config.headers["Authorization"] =
        " Bearer " + localStorage.getItem("token");
      console.log(localStorage.getItem("token"));
      // 让每个请求携带自定义token 根据实际情况自行修改
    }
  }
  return config
}, err => {
  // todo待完善接口拦截
  return Promise.reject(err);
});
new Vue({
  el: "#app",
  router,
  // store,
  components: {
    App
    // httpService
  },
  template: "<App/>"
});
