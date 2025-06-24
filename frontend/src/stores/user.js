import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  // 从本地加载初始状态
  const token = ref(localStorage.getItem('token') || '')
  const username = ref(localStorage.getItem('username') || '')

  // 是否已登录（根据 token 判断）
  const isLoggedIn = computed(() => Boolean(token.value))

  // 设置用户（登录成功时调用）
  function setUser(data) {
    token.value = data.token
    username.value = data.username
    localStorage.setItem('token', data.token)
    localStorage.setItem('username', data.username)
  }

  // 登出（清除状态和本地）
  function logout() {
    token.value = ''
    username.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('username')
  }

  return {
    token,
    username,
    isLoggedIn,
    setUser,
    logout
  }
})
