<template>
  <div class="sp-main-info">
    <t-card :bordered="false" :hover-shadow="false">
      <t-tabs v-model="selectTab" :placement="'top'" @change="changeTabs">
        <t-tab-panel :value="'ALI'" label="阿里云" :destroy-on-hide="false">
          <ali-sms ref="aliSms" />
        </t-tab-panel>
        <t-tab-panel :value="'TENCENT'" label="腾讯云" :destroy-on-hide="false">
          <template #panel>
            <tencent-sms ref="tencentSms" />
          </template>
        </t-tab-panel>
      </t-tabs>
    </t-card>
  </div>
</template>

<script lang="ts">
export default {
  name: 'CenterUser',
};
</script>

<script setup lang="ts">
import { ref, onMounted, computed, reactive } from 'vue';
import { MessagePlugin, DialogPlugin } from 'tdesign-vue-next';
import AliSms from './alsms.vue';
import TencentSms from './tencentsms.vue';

const aliSms = ref(null);
const tencentSms = ref(null);
const selectTab = ref('ALI');
const changeTabs = async (value) => {
  if (value === 'ALI') {
    aliSms.value.initData();
  } else if (value === 'TENCENT') {
    tencentSms.value.initData();
  }
};
// vue的api
onMounted(async () => {});
</script>

<style lang="less" scoped>
@import '@/style/variables';
</style>
