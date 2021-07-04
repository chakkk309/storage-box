import Vue from "vue";
import Router from "vue-router";
import Login from "@/pages/Login";
import SideBar from "@/components/SideBar"
import Header from "@/components/Header"
import Home from "@/components/Home"
import CustomerManages from "@/pages/customerManages/CustomerManages"
import UserManages from "@/pages/userManages/UserManages"
import BoxManages from "@/pages/boxManages/BoxManages"
import BoxNumber from "@/pages/boxManages/BoxNumber"
import AdvertManages from "@/pages/AdvertManages/AdvertManages"
import ViewManages from "@/pages/ViewManages/ViewManages"
import OrderManages from "@/pages/orderManages/OrderManages"


Vue.use(Router);

export default new Router({
  routes: [{
      path: "/",
      name: "login",
      component: Login
    },
    {
      path: "/sidebar",
      name: "sidebar",
      component: SideBar
    }, {
      path: "/header",
      name: "header",
      component: Header
    }, {
      path: "/home",
      name: "home",
      component: Home,
      children: [{

          path: "/usermanages",
          name: "usermanages",
          component: UserManages
        }, {
          path: "/customermanages",
          name: "customermanages",
          component: CustomerManages
        },
        {
          path: "/boxmanages",
          name: "boxmanages",
          component: BoxManages,
        },
        {
          path: "/boxnumber",
          name: "boxnumber",
          component: BoxNumber,
        }, {
          path: "/advertmanages",
          name: "advertmanages",
          component: AdvertManages
        }, {
          path: "/viewmanages",
          name: "viewmanages",
          component: ViewManages
        }, {
          path: "/ordermanages",
          name: "ordermanages",
          component: OrderManages
        }
      ]

    },
  ],
  mode: "history"
});
