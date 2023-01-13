import { createApp } from 'vue';

import TDesign from 'tdesign-vue-next';
import 'tdesign-vue-next/es/style/index.css';

import { store } from './store';
import router from './router';
import '@/style/index.less';
import './permission';
import App from './App.vue';

const app = createApp(App);

app.use(TDesign);
app.use(router);
app.use(store);
// 定义一个全局高度多tabs和非多tabs的
app.config.globalProperties.$tableHeight = '68vh';
app.mount('#app');
