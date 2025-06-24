<template>
  <div class="p-6">
    <el-page-header content="用户登录" @back="goBack" class="mb-4" />

    <el-card>
      <el-form :model="loginForm" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitLogin">登录</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from '@/utils/axios.js'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const goBack = () => {
  if (window.history.length <= 1) router.push('/')
  else router.back()
}

const formRef = ref()

const loginForm = ref({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const submitLogin = async () => {
  await formRef.value.validate(async valid => {
    if (!valid) return

    try {
      const res = await axios.post('/login', loginForm.value)
      if (res.data.code === 0) {
        // ✅ 存入 pinia 和 localStorage
        userStore.setUser({
          username: loginForm.value.username,
          token: 'dev-token' // 👉 你可以换成后端返回的 token
        })
        ElMessage.success(res.data.message)
        router.push('/')
      } else {
        ElMessage.error(res.data.message || '登录失败')
      }
    } catch (err) {
      ElMessage.error('请求失败，请稍后再试')
    }
  })
}

const resetForm = () => {
  formRef.value.resetFields()
}
</script>

<style scoped>
.p-6 {
  padding: 1.5rem;
}
.mb-4 {
  margin-bottom: 1rem;
}
</style>
