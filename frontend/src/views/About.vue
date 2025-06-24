<template>
  <div class="p-6">
    <el-page-header content="About" @back="$router.back" class="mb-4" />
    <el-card>
      <p>{{ ('aboutIntro') }}</p>
      <ul>
        <li>Vue 3</li>
        <li>Vite</li>
        <li>Pinia</li>
        <li>Element Plus</li>
        <li>Axios</li>
        <li>Vue Router</li>
      </ul>
    </el-card>
  </div>
</template>