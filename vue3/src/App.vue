<template>
  <t-watermark
    :watermark-content="{
      text: 'FrSimpleBoot',
    }"
    :width="120"
    :height="60"
    :y="120"
    :x="80"
    :z-index="1000"
  >
    <router-view :class="[mode]" />
  </t-watermark>
</template>
<script setup lang="ts">
import { computed, onMounted } from 'vue';
import config from '@/config/style';
import { useSettingStore } from '@/store';

const store = useSettingStore();

const mode = computed(() => {
  return store.displayMode;
});

onMounted(() => {
  store.updateConfig({ ...config });
});
</script>
<style lang="less" scoped>
@import '@/style/variables.less';

#nprogress .bar {
  background: var(--tdvns-brand-color) !important;
}
</style>
