<template>
  <div class="p-6">
    <!-- 顶部右上角：登录状态判断 -->
    <el-row justify="end" class="mb-4">
      <template v-if="userStore.isLoggedIn">
        <span class="mr-2">👋 欢迎，{{ userStore.username }}</span>
        <el-button type="text" @click="logout">退出</el-button>
      </template>
      <template v-else>
        <el-button type="text" @click="$router.push('/login')">{{ $t('login') }}</el-button>
        <el-button type="text" @click="$router.push('/register')">{{ $t('register') }}</el-button>
      </template>
    </el-row>

    <!-- 欢迎语 -->
    <el-card shadow="hover" class="mb-6">
      <div class="text-center">
        <h1>{{ $t('welcome') }}</h1>
        <p style="font-size: 20px;">{{ $t('homepage') }}</p>
      </div>
    </el-card>

    <!-- 横向功能按钮区 -->
    <el-row :gutter="16" class="mb-6" justify="start">
      <el-col :span="6">
        <el-button type="primary" plain block @click="$router.push('/products')">
          {{ $t('goProducts') }}
        </el-button>
      </el-col>
      <el-col :span="6">
        <el-button type="success" plain block @click="$router.push('/categories')">
          {{ $t('goCategories') }}
        </el-button>
      </el-col>
    </el-row>

    <!-- 语言切换 -->
    <div style="display: flex; align-items: center; gap: 20px;">
      <el-form-item :label="$t('langSelect')" style="margin-bottom: 0;">
        <el-select v-model="lang" placeholder="语言" style="width: 150px">
          <el-option label="中文" value="zh" />
          <el-option label="English" value="en" />
          <el-option label="日本語" value="ja" />
        </el-select>
      </el-form-item>
      <span>🌐 当前语言：{{ lang }}</span>
    </div>

    <!-- 关于按钮右下角 -->
    <el-row justify="end" class="mt-4">
      <el-button plain type="info" @click="$router.push('/about')">
        {{ $t('goAbout') }}
      </el-button>
    </el-row>
  </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { ref, watch } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const { locale } = useI18n()
const lang = ref(locale.value)
watch(lang, val => (locale.value = val))

const userStore = useUserStore()
const router = useRouter()

const logout = () => {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/')
}
</script>

<style scoped>
.p-6 {
  padding: 1.5rem;
}
.mb-4 {
  margin-bottom: 1rem;
}
.mb-6 {
  margin-bottom: 2rem;
}
.text-center {
  text-align: center;
}
</style>
