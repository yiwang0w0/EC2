<template>
  <div class="p-6">
    <el-page-header content="分类管理" @back="goBack" class="mb-4" />

    <!-- 按钮组移至右上角 -->
    <div class="top-actions">
      <el-button type="primary" @click="showAddDialog = true">添加分类</el-button>
      <el-button
        :type="canEdit ? 'warning' : 'info'"
        :disabled="!canEdit"
        @click="openEditDialog"
      >
        修改分类
      </el-button>
      <el-button
        :type="canDelete ? 'danger' : 'info'"
        :disabled="!canDelete"
        @click="handleDeleteCategory"
      >
        删除分类
      </el-button>
    </div>

    <el-card>
      <!-- 树结构 -->
      <el-tree
        ref="treeRef"
        :data="treeData"
        :props="defaultProps"
        node-key="id"
        :highlight-current="true"
        :expand-on-click-node="false"
        :default-expand-all="false"
        @node-click="handleNodeClick"
        class="category-tree"
      >
        <template #default="{ data }">
          <div
            class="tree-node"
            :class="{
              'is-root': !data.parentId,
              'is-child': !!data.parentId,
              'selected': selectedCategoryId === data.id
            }"
          >
            <span :class="{ 'root-font': !data.parentId, 'child-font': !!data.parentId }">
              {{ data.name }}
            </span>
          </div>
        </template>
      </el-tree>
    </el-card>


    <!-- 添加弹窗 -->
    <el-dialog v-model="showAddDialog" title="添加分类" width="400px">
      <el-form :model="newCategory" label-width="80px">
        <el-form-item label="分类名" required>
          <el-input v-model="newCategory.name" />
        </el-form-item>
        <el-form-item label="父类">
          <el-select v-model="newCategory.parentId" placeholder="不选为主分类" clearable>
            <el-option
              v-for="item in parentOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="submitNewCategory">提交</el-button>
      </template>
    </el-dialog>

    <!-- 修改弹窗 -->
    <el-dialog v-model="showEditDialog" title="修改分类名" width="400px">
      <el-form :model="editCategory" label-width="80px">
        <el-form-item label="新名称" required>
          <el-input v-model="editCategory.name" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="submitEditCategory">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import axios from '@/utils/axios'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const goBack = () => router.push('/')

const rawCategories = ref([])
const treeData = ref([])
const parentOptions = ref([])
const selectedCategoryId = ref(null)
const selectedCategoryData = ref(null)

const showAddDialog = ref(false)
const newCategory = ref({ name: '', parentId: null })

const showEditDialog = ref(false)
const editCategory = ref({ id: null, name: '' })

const treeRef = ref(null)

const defaultProps = {
  children: 'children',
  label: 'name'
}

// 删除按钮是否可用（不可删有子节点的父分类）
const canDelete = computed(() => {
  if (!selectedCategoryId.value) return false
  const hasChildren = rawCategories.value.some(c => c.parentId === selectedCategoryId.value)
  return !hasChildren
})

// 修改按钮是否可用
const canEdit = computed(() => !!selectedCategoryId.value)

const handleNodeClick = (data) => {
  selectedCategoryId.value = data.id
  selectedCategoryData.value = data
}

// 清除选中（点击页面其他区域时）
const clearSelectionOnOutsideClick = (e) => {
  const treeEl = document.querySelector('.el-tree')
  const actionEl = document.querySelector('.top-actions')

  if (
    (treeEl && treeEl.contains(e.target)) ||
    (actionEl && actionEl.contains(e.target))
  ) {
    return
  }

  selectedCategoryId.value = null
  selectedCategoryData.value = null
}
onMounted(() => {
  document.addEventListener('click', clearSelectionOnOutsideClick)
})
onBeforeUnmount(() => {
  document.removeEventListener('click', clearSelectionOnOutsideClick)
})

const loadCategories = async () => {
  const res = await axios.get('/categories')
  rawCategories.value = res.data

  const buildTree = (parentId = null) => {
    return res.data
      .filter(c => c.parentId === parentId)
      .map(c => ({
        ...c,
        children: buildTree(c.id)
      }))
  }

  treeData.value = buildTree()
  parentOptions.value = res.data.filter(c => c.parentId === null)
}

const submitNewCategory = async () => {
  if (!newCategory.value.name) return ElMessage.warning('请输入名称')
  await axios.post('/categories', newCategory.value)
  ElMessage.success('添加成功')
  showAddDialog.value = false
  newCategory.value = { name: '', parentId: null }
  await loadCategories()
}

const openEditDialog = () => {
  if (!selectedCategoryId.value) return
  editCategory.value = {
    id: selectedCategoryId.value,
    name: selectedCategoryData.value.name
  }
  showEditDialog.value = true
}

const submitEditCategory = async () => {
  await axios.put(`/categories/${editCategory.value.id}`, {
    name: editCategory.value.name
  })
  ElMessage.success('修改成功')
  showEditDialog.value = false
  await loadCategories()
}

const handleDeleteCategory = async () => {
  if (!selectedCategoryId.value) return

  try {
    await axios.get(`/categories/can-delete/${selectedCategoryId.value}`)

    await ElMessageBox.confirm('确定删除此分类吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await axios.delete(`/categories/${selectedCategoryId.value}`)
    ElMessage.success('删除成功')
    selectedCategoryId.value = null
    selectedCategoryData.value = null
    await loadCategories()
  } catch (err) {
    if (err && err !== 'cancel' && err !== 'close') {
      ElMessage.error('请求失败')
    }
    // 若是 cancel 或 close，则无需任何提示
  }
}


onMounted(loadCategories)
</script>
<style scoped>
.p-6 {
  padding: 1.5rem;
}
.mb-4 {
  margin-bottom: 1rem;
}

/* 移动按钮组至右上角 */
.top-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-bottom: 1rem;
  margin-top: -0.5rem;
}

/* 分类字体与结构层级样式 */
.el-tree-node__content {
  min-height: 52px;
  align-items: center;
  padding-left: calc(20px * var(--el-tree-node-level));
  position: relative;
  overflow: visible;
}
.el-tree-node__content.is-current {
  background-color: #ecf5ff !important;
  border-radius: 6px;
}
.el-tree-node__content:hover {
  background-color: #f5f7fa;
  border-radius: 6px;
}
.node-name {
  display: flex;
  align-items: center;
  gap: 8px;
}
.is-parent .node-name {
  font-size: 24px;
  font-weight: 600;
}
.is-child .node-name {
  font-size: 18px;
  font-weight: 500;
}
</style>