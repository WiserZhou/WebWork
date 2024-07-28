import {createApp} from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import router from './router'
import app from './App.vue'

createApp(app).use(ElementPlus).use(router).mount('#app')
