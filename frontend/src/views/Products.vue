<template>
  <div class="p-6">
    <el-page-header content="商品列表" @back="goBack" class="mb-4" />
    <el-button type="primary" @click="goHome">回到首页</el-button>
    <el-card shadow="hover">
      <div class="mb-4" style="display:flex;justify-content:space-between;align-items:center;">
        <el-form :inline="true">
          <el-form-item label="搜索商品">
            <div style="display: flex; gap: 4px">
              <!-- 分类下拉：父分类加粗 -->
              <el-select v-model="selectedCategoryId" placeholder="分类" clearable style="width: 140px">
                  <el-option
                  :key="0"
                  :label="'全部'"
                  :value="null"
                >
                  <span style="font-weight: bold">全部</span>
                </el-option>
                <el-option
                  v-for="category in categories"
                  :key="category.id"
                  :label="category.name"
                  :value="category.id"
                >
                  <span :style="{ fontWeight: category.parentId === null ? 'bold' : 'normal' }">
                    {{ category.name }}
                  </span>
                </el-option>
              </el-select>

              <!-- 商品名输入 -->
              <el-input
                v-model="search"
                placeholder="请输入关键词"
                clearable
                style="width: 200px"
              />
            </div>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="filterProducts">搜索</el-button>
            <el-button @click="resetFilter">重置</el-button>
          </el-form-item>
        </el-form>

        <!-- 添加商品按钮（新标签页） -->
        <el-button type="primary" @click="handleAddProduct">添加商品</el-button>
      </div>

      <el-table :data="pagedProducts" :loading="loading" stripe border style="width: 100%">
        <el-table-column label="主分类">
          <template #default="{ row }">
            {{ categoryMap[parentMap[row.categoryId]] || '未知' }}
          </template>
        </el-table-column>
        <el-table-column label="子分类">
          <template #default="{ row }">
            {{ categoryMap[row.categoryId] || '未知' }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名" />
        <el-table-column prop="price" label="价格" />
        <el-table-column prop="stock" label="库存" />
        <el-table-column label="图片">
          <template #default="{ row }">
            <el-image
              :src="row.imageUrl"
              style="width: 80px; height: 80px"
              :preview-src-list="[row.imageUrl]"
              fit="cover"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="editProduct(row.id)">修改</el-button>
            <el-button size="small" type="danger" @click="deleteProduct(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :current-page="currentPage"
        @current-change="handlePageChange"
        class="mt-4"
      />
    </el-card>

    <!-- 修改商品弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      title="修改商品"
      width="60%"
      destroy-on-close
    >
      <iframe
        v-if="dialogVisible"
        :src="`/add-product?id=${editId}`"
        style="width: 100%; height: 600px; border: none"
      />
    </el-dialog>
  </div>
</template>

<script>
import axios from '@/utils/axios'

export default {
  data() {
    return {
      products: [],
      total: 0,
      loading: false,
      search: '',
      categories: [],
      selectedCategoryId: null,
      categoryMap: {},
      parentMap: {},
      currentPage: 1,
      pageSize: 5,
      dialogVisible: false,
      editId: null
    }
  },
  computed: {
    pagedProducts() {
      return this.products
    }
  },
  methods: {
    async fetchProducts() {
      this.loading = true
      const params = { page: this.currentPage, size: this.pageSize }
      if (this.search) params.search = this.search
      if (this.selectedCategoryId !== null) params.categoryId = this.selectedCategoryId
      const res = await axios.get('/products', { params })
      this.products = res.data.records
      this.total = res.data.total
      this.loading = false
    },
    async fetchCategories() {
      try {
        const res = await axios.get('/categories')
        this.categories = res.data
        this.buildCategoryMap()
      } catch (error) {
        console.error('加载分类失败:', error)
      }
    },
    buildCategoryMap() {
      this.categoryMap = {}
      this.parentMap = {}
      this.categories.forEach(cat => {
        this.categoryMap[cat.id] = cat.name
        this.parentMap[cat.id] = cat.parentId
      })
    },
    filterProducts() {
      this.currentPage = 1
      this.fetchProducts()
    },
    resetFilter() {
      this.search = ''
      this.selectedCategoryId = null
      this.currentPage = 1
      this.fetchProducts()
    },
    handlePageChange(val) {
      this.currentPage = val
      this.fetchProducts()
    },
    goBack() {
      this.$router.back()
    },
        goHome() {
      this.$router.push('/')  // 或直接跳转首页路径
    },
    editProduct(id) {
      this.editId = id
      this.dialogVisible = true
    },
    handleAddProduct() {
      window.open('/add-product', '_blank')
    },
    async deleteProduct(id) {
      try {
        await this.$confirm(
          '此操作将永久删除该商品，是否继续？',
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        await axios.delete(`/products/${id}`)
        ElMessage.success('删除成功')
        this.fetchProducts()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
        // 如果是取消，不做任何处理
      }
    },
    handleIframeMessage(event) {
      if (event.data === 'edit-success') {
    this.dialogVisible = false
    this.fetchProducts() // 重新加载商品
    }
  }
  },
    mounted() {
    this.fetchProducts()
    this.fetchCategories()
    window.addEventListener('message', this.handleIframeMessage)
  },
    beforeUnmount() {
    window.removeEventListener('message', this.handleIframeMessage)
  },

}

</script>
