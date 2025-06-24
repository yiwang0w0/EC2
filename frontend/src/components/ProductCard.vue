<template>
  <div class="card">
    <img :src="product.image" alt="product" />
    <h3>{{ product.name[locale] }}</h3>
    <p>￥{{ product.price }}</p>
  </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
defineProps(['product'])

const { locale } = useI18n()
</script>

<style scoped>
.card {
  width: 180px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  text-align: center;
}
.card img {
  width: 100%;
  height: auto;
}
</style>
