import Vue from "vue";
import router from "./router";
import "vant/lib/index.css";
import Vant from "vant";
import App from "./App";
import {
  Toast
} from "vant";
import axios from "axios";

import {
  Lazyload
} from "vant";
import {
  Image as VanImage
} from 'vant';


Vue.use(VanImage);
Vue.use(Lazyload);

Vue.prototype.$axios = axios;
Vue.use(Vant);
Vue.use(Toast);

Vue.config.productionTip = false;



// 请求拦截
axios.interceptors.request.use(
  config => {
    // console.log(config)
    if ("prod-api/common/login" != config.url) {
      if (localStorage.getItem("token")) {

        config.headers["Authorization"] =
          " Bearer " + localStorage.getItem("token");

        // console.log(localStorage.getItem("token"));
        // 让每个请求携带自定义token 根据实际情况自行修改
    
      }
      
     
    }
    return config;
  },
  err => {
    // todo待完善接口拦截;
    return Promise.reject(err);
  }
);
/* 响应拦截器 */
 
// axios.interceptors.response.use(function (response) { // ①10010 token过期（30天） ②10011 token无效
//  console.log(response)
// if(response.config.headers["Authorization"] !=" Bearer " + localStorage.getItem("token")) {

   
      // Storage.localRemove('token') // 删除已经失效或过期的token（不删除也可以，因为登录后覆盖）
   
      // router.replace({
   
          // path: '/' // 到登录页重新获取token
   
      // })
   
  // } else if (response.data.token) { // 判断token是否存在，如果存在说明需要更新token
   
    //  localStorage.setItem('token', response.config.headers["Authorization"]) // 覆盖原来的token(默认一天刷新一次)
   
  // }
   
      // return response
   
  // }, function (error) {
   
      // return Promise.reject(error)
   
  // })
   

/* eslint-disable no-new */
new Vue({
  el: "#app",
  router,
  components: {
    App
  },
  template: "<App/>"
});
