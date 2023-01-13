<template>
  <t-col :span="12">
    <div class="top-panel">
      <slot />
    </div>
  </t-col>
</template>
<style lang="less" scoped>
.top-panel {
  display: flex;
  flex-wrap: wrap;
  align-content: center;
  align-items: center;
  justify-content: flex-start;
  margin: 0 0 20px 0;
}
</style>
