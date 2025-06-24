import { createI18n } from 'vue-i18n'

const messages = {
  en: {
    welcome: 'Welcome!',
    homepage: 'This is the homepage',
    goAbout: 'Go to About Page',
    goProducts: 'View Products',
    langSelect: 'Language:',
    productList: 'Product List',
    loading: 'Loading...',
    login: 'Login',
    register: 'Register',
    goCategories: 'View Categories',
    goAddProduct: 'Add Product',
  },
  zh: {
    welcome: '欢迎光临！',
    homepage: '这是首页',
    goAbout: '去关于页面',
    goProducts: '查看商品',
    langSelect: '切换语言：',
    productList: '商品列表',
    loading: '加载中...',
    login: '登录',
    register: '注册',
    goCategories: '查看分类',
    goAddProduct: '添加商品',
  },
  ja: {
    welcome: 'ようこそ！',
    homepage: 'これはホームページです',
    goAbout: 'Aboutページへ移動',
    goProducts: '商品を見る',
    langSelect: '言語を選択：',
    productList: '商品一覧',
    loading: '読み込み中...',
    login: 'ログイン',
    register: '登録',
    goCategories: 'カテゴリーを見る',
    goAddProduct: '商品を追加',
  }
}

const i18n = createI18n({
  legacy: false,
  locale: 'ja',
  fallbackLocale: 'en',
  messages
})

export default i18n 