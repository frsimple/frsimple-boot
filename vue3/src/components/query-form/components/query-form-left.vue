<script lang="ts" setup>
defineProps({
  span: {
    type: Number,
    default: 6,
  },
});
</script>

<template>
  <t-col :lg="span" :md="12" :sm="12" :xl="span" :xs="12">
    <div class="left-panel">
      <slot />
    </div>
  </t-col>
</template>

<style lang="less" scoped>
.left-panel {
  display: flex;
  flex-wrap: wrap;
  align-content: center;
  align-items: center;
  justify-content: flex-start;
  margin: 0 0 20px 0;
}
</style>
