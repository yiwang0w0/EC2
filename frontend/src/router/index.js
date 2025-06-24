import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import { ElMessage } from 'element-plus'

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/about', name: 'About', component: () => import('../views/About.vue') },
  { path: '/products', name: 'Products', component: () => import('../views/Products.vue') },
  { path: '/categories', name: 'Categories', component: () => import('../views/CategoryView.vue') },
  { path: '/add-product', name: 'AddProduct', component: () => import('../views/AddProduct.vue') },
  { path: '/login', name: 'Login', component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', component: () => import('../views/Register.vue') },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// ✅ 登录状态守卫
router.beforeEach((to, from, next) => {
  const isLoggedIn = !!localStorage.getItem('username')

  // 需要登录的路由
  const protectedRoutes = ['/products', '/categories', '/add-product']

  if (protectedRoutes.includes(to.path) && !isLoggedIn) {
    ElMessage.warning('请先登录')
    next('/login')
  } else {
    next()
  }
})

export default router
