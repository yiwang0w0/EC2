<template>
  <div class="p-6">
    <el-page-header content="用户注册" @back="goBack" class="mb-4" />

    <el-card>
      <el-form :model="registerForm" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="registerForm.email" placeholder="请输入邮箱" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitRegister">注册</el-button>
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

const router = useRouter()
const formRef = ref()

const registerForm = ref({
  username: '',
  password: '',
  email: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }]
}

const submitRegister = async () => {
  await formRef.value.validate(async valid => {
    if (!valid) return

    try {
      const res = await axios.post('/register', registerForm.value)
      if (res.data.code === 0) {
        ElMessage.success(res.data.message)
        router.push('/login')
      } else {
        ElMessage.error(res.data.message || '注册失败')
      }
    } catch (err) {
      ElMessage.error('请求失败，请稍后再试')
    }
  })
}

const resetForm = () => {
  formRef.value.resetFields()
}

const goBack = () => {
  if (window.history.length <= 1) router.push('/')
  else router.back()
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
