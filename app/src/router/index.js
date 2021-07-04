import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/pages/Login'
import Register from '@/pages/Register'
import Index from '@/pages/Index'
import OrderList from '@/pages/OrderList'
import ListDetail from '@/pages/ListDetail'
import operate from '@/pages/operate'
import PersonCenter from '@/pages/PersonCenter'
import IntegralRecord from '@/pages/IntegralRecord'
import AddrManage from '@/pages/AddrManage'
import AlterPassword from '@/pages/AlterPassword'
import suggestion from '@/pages/suggestion'
import aboutMe from '@/pages/aboutMe'
import accountManagement from '@/pages/accountManagement'
import nullBox from '@/pages/nullBox'
import AddrAdd from '@/pages/AddrAdd'
import addrEdit from '@/pages/addrEdit'
import appointment from '@/pages/appointment'
import advertDetail from '@/pages/advertDetail'
Vue.use(Router)
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

export default new Router({
  routes: [{
      path: '/',
      name: 'login',
      component: Login
    }, {
      path: '/register',
      name: 'register',
      component: Register
    }, {
      path: '/index',
      name: "index",
      component: Index
    }, {
      path: '/orderlist',
      name: "orderlist",
      component: OrderList
    }, {
      path: '/listdetail/:id',
      name: "listdetail",
      component: ListDetail
    }, {
      path: '/operate',
      name: "operate",
      component: operate
    }, {
      path: '/personcenter',
      name: "personcenter",
      component: PersonCenter
    }, {
      path: '/integralrecord',
      name: "integralrecord",
      component: IntegralRecord
    }, {
      path: '/addrmanage',
      name: "addrmanage",
      component: AddrManage
    },
    {
      path: '/alterpassword',
      name: 'AlterPassword',
      component: AlterPassword

    }, {
      path: '/suggestion',
      name: 'suggestion',
      component: suggestion
    }, {
      path: '/aboutme',
      name: "aboutme",
      component: aboutMe
    }, {
      path: '/accountmanagement',
      name: "accountmanagement",
      component: accountManagement
    }, {
      path: '/nullbox',
      name: "nullbox",
      component: nullBox
    }, {
      path: '/addradd',
      name: "addradd",
      component: AddrAdd
    }, {
      path: '/addredit',
      name: "addredit",
      component: addrEdit
    }, {
      path: '/appointment',
      name: "appointment",
      component: appointment
    }, {
      path: '/advertdetail',
      name: "advertdetail",
      component: advertDetail
    }
  ],
})
