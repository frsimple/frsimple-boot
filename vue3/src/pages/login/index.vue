<template>
  <div class="login-wrapper">
    <login-header />
    <div class="login-container">
      <div class="title-container">
        <h1 class="title margin-no"></h1>
        <h1 class="title"></h1>
        <!-- <div class="sub-title">
          <p class="tip">{{ type == 'register' ? '已有账号?' : '没有账号吗?' }}</p>
          <p class="tip" @click="switchType(type == 'register' ? 'login' : 'register')">
            {{ type == 'register' ? '登录' : '注册新账号' }}
          </p>
        </div> -->
      </div>

      <login v-if="type === 'login'" />
      <register v-else @register-success="switchType('login')" />
      <tdesign-setting />
    </div>

    <footer class="copyright">
      © 2020-2022 FrSimple. All Rights Reserved .
      <a class="beian" href="https://beian.miit.gov.cn/" target="_blank">沪ICP备2022018704号</a>
    </footer>
  </div>
</template>
<script lang="ts">
export default {
  name: 'LoginIndex',
};
</script>
<script setup lang="ts">
import { ref } from 'vue';

import Login from './components/Login.vue';
import Register from './components/Register.vue';
import LoginHeader from './components/Header.vue';
import TdesignSetting from '@/layouts/setting.vue';

const type = ref('login');
const switchType = (val: string) => {
  type.value = val;
};
</script>

<style lang="less" scoped>
@import url('./index.less');

.beian {
  text-decoration: none;
  color: var(--tdvns-text-color-secondary);
}
</style>
