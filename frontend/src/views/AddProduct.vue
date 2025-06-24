<template>
  <div class="p-6">
    <!-- 如果不是嵌套 iframe，才显示头部 -->
    <el-page-header
      v-if="!isEmbedded"
      :content="isEdit ? '修改商品' : '添加商品'"
      @back="goBack"
      class="mb-4"
    />

    <el-card>
      <el-form
        :model="product"
        label-width="100px"
        :rules="rules"
        ref="productForm"
      >
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="product.name" placeholder="请输入商品名称" />
        </el-form-item>

        <el-form-item label="价格" prop="price">
          <el-input v-model.number="product.price" type="number" placeholder="请输入价格" />
        </el-form-item>

        <el-form-item label="库存" prop="stock">
          <el-input v-model.number="product.stock" type="number" placeholder="请输入库存" />
        </el-form-item>

        <el-form-item label="图片地址" prop="imageUrl">
          <el-input v-model="product.imageUrl" placeholder="图片 URL" />
          <img
            v-if="product.imageUrl"
            :src="product.imageUrl"
            alt="预览图"
            style="max-height: 120px; margin-top: 10px"
          />
        </el-form-item>

        <el-form-item label="主分类">
          <el-select
            v-model="parentId"
            placeholder="请选择主分类"
            style="width: 100%"
            @change="loadSubcategories"
          >
            <el-option
              v-for="parent in parentCategories"
              :key="parent.id"
              :label="parent.name"
              :value="parent.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="子分类" prop="categoryId">
          <el-select
            v-model="product.categoryId"
            filterable
            placeholder="请选择子分类"
            style="width: 100%"
          >
            <el-option
              v-for="sub in subcategories"
              :key="sub.id"
              :label="sub.name"
              :value="sub.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm">提交</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '@/utils/axios.js'
import { ElMessage } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

// 是否在 iframe 中
const isEmbedded = ref(false)
onMounted(() => {
  isEmbedded.value = window.self !== window.top
})

const goBack = () => {
  if (window.history.length <= 1) {
    router.push('/products')
  } else {
    router.back()
  }
}

const isEdit = ref(false)
const productId = ref(null)
const productForm = ref()

const product = ref({
  name: '',
  price: null,
  imageUrl: '',
  stock: 0,
  categoryId: null
})

const parentId = ref(null)
const parentCategories = ref([])
const subcategories = ref([])
const allCategories = ref([])

const rules = {
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  price: [
    { required: true, message: '请输入价格', trigger: 'blur' },
    { type: 'number', min: 0.01, message: '价格必须大于 0', trigger: 'blur' }
  ],
  stock: [
    { required: true, message: '请输入库存', trigger: 'blur' },
    { type: 'number', min: 0, message: '库存不能为负数', trigger: 'blur' }
  ],
  imageUrl: [{ required: true, message: '请输入图片地址', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择子分类', trigger: 'change' }]
}

const loadParentCategories = async () => {
  try {
    const res = await axios.get('/categories')
    allCategories.value = res.data
    parentCategories.value = res.data.filter(c => c.parentId === null)
  } catch {
    ElMessage.error('加载主分类失败')
  }
}

const loadSubcategories = async () => {
  if (!parentId.value) {
    subcategories.value = []
    product.value.categoryId = null
    return
  }
  try {
    const res = await axios.get(`/categories/parent/${parentId.value}`)
    subcategories.value = res.data
    product.value.categoryId = null
  } catch {
    ElMessage.error('加载子分类失败')
  }
}

const loadProduct = async (id) => {
  try {
    const { data } = await axios.get(`/products/${id}`)
    product.value = {
      name: data.name,
      price: data.price,
      imageUrl: data.imageUrl,
      stock: data.stock,
      categoryId: data.categoryId
    }
    const category = allCategories.value.find(c => c.id === data.categoryId)
    if (category) {
      parentId.value = category.parentId
      await loadSubcategories()
      product.value.categoryId = data.categoryId
    }
  } catch {
    ElMessage.error('商品信息加载失败')
  }
}

const submitForm = async () => {
  try {
    await productForm.value.validate()
    product.value.price = parseFloat(product.value.price).toFixed(2) * 1
    if (isEdit.value) {
      await axios.put(`/products/${productId.value}`, product.value)
      ElMessage.success('商品修改成功')
      if (isEmbedded.value) {
  window.parent.postMessage('edit-success', '*')
}
    } else {
      await axios.post('/products', product.value)
      ElMessage.success('商品添加成功')
    }
    resetForm()
    router.push('/products')
  } catch {
    ElMessage.error('操作失败')
  }
}

const resetForm = () => {
  productForm.value.resetFields()
  parentId.value = null
  subcategories.value = []
}

onMounted(async () => {
  await loadParentCategories()
  const id = route.query.id
  if (id) {
    isEdit.value = true
    productId.value = id
    await loadProduct(id)
  }
})
</script>

<style scoped>
.p-6 {
  padding: 1.5rem;
}
.mb-4 {
  margin-bottom: 1rem;
}
</style>
