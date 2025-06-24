import axios from 'axios'

// 创建 Axios 实例
const instance = axios.create({
  baseURL: '/api',
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器（可选）：自动附加 token
instance.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器（可选）：处理错误统一提示
instance.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      alert(res.msg || '请求失败')
      return Promise.reject(new Error(res.msg || 'error'))
    }
    return res
  },
  error => {
    if (error.response) {
      console.error('后端返回错误：', error.response.data)
      alert(error.response.data.message || '发生错误')
    } else if (error.request) {
      console.error('请求未收到响应：', error.message)
      alert('请求失败，服务器未响应')
    } else {
      console.error('请求配置出错：', error.message)
      alert('请求配置异常')
    }
    return Promise.reject(error)
  }
)

export default instance
