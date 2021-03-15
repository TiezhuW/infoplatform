import Vue from 'vue'
import VueRouter from 'vue-router'
import HouseQuery from '../views/HouseQuery';
import HouseAdd from '../views/HouseAdd';
import HouseUpdate from "../views/HouseUpdate";
import Login from "../views/Login";
import Register from "../views/Register";
import UserInfo from "../views/UserInfo";
import Collection from "../views/Collection";
import Crawler from "../views/Crawler";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: '二手房信息搜索引擎首页',
        component: HouseQuery
    },
    {
        path: '/houseAdd',
        name: '二手房信息添加',
        component: HouseAdd
    },
    {
        path: '/houseUpdate',
        name: '二手房信息修改',
        component: HouseUpdate,
    },
    {
        path: '/login',
        name: '用户登录',
        component: Login
    },
    {
        path: '/register',
        name: '用户注册',
        component: Register
    },
    {
        path: '/userInfo',
        name: '用户信息',
        component: UserInfo
    },
    {
        path: '/collection',
        name: '用户收藏',
        component: Collection
    },
    {
        path: '/crawler',
        name: '二手房信息爬取',
        component: Crawler
    }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
