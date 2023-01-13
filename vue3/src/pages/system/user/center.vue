<template>
  <div class="sp-main-info">
    <t-row :gutter="10">
      <t-col :span="4">
        <t-card :bordered="false" :hover-shadow="false" :style="{ textAlign: 'center' }">
          <t-avatar v-if="!curUser || !curUser.avatar" size="200px">
            <template #icon>
              <t-icon name="user-circle" />
            </template>
          </t-avatar>
          <t-avatar v-else size="200px" :image="curUser.avatar" />
          <t-upload
            v-model="files"
            :tips="'上传文件大小在 5M 以内'"
            theme="custom"
            :before-upload="beforeUpload"
            :request-method="requestMethod"
          >
            <t-button theme="default" variant="outline" :style="{ marginTop: '10px' }">
              <template #icon> <t-icon name="photo"></t-icon> </template>更换头像</t-button
            >
          </t-upload>
        </t-card>
      </t-col>
      <t-col :span="8">
        <t-card :bordered="false" :hover-shadow="false">
          <t-tabs v-model="selectTab" :placement="'top'" @change="changeTabs">
            <t-tab-panel :value="1" label="个人信息" :destroy-on-hide="false">
              <base-c ref="basec" @updateUserInStore="updateUserStore" />
            </t-tab-panel>
            <t-tab-panel :value="2" label="安全设置" :destroy-on-hide="false">
              <template #panel>
                <setting ref="setting" />
              </template>
            </t-tab-panel>
            <t-tab-panel :value="3" label="账号绑定" :destroy-on-hide="false">
              <template #panel>
                <bang-c ref="bangc" />
              </template>
            </t-tab-panel>
          </t-tabs>
        </t-card>
      </t-col>
    </t-row>
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
import { useUserStore } from '@/store';
import { updateAvatar } from '@/api/system/user';
import BaseC from './base.vue';
import Setting from './setting.vue';
import BangC from './bang.vue';

const userStore = useUserStore();
const curUser = computed(() => {
  return userStore.curUser;
});
const updateUserStore = async () => {
  await userStore.getUserInfo();
};
// 切换tab
const selectTab = ref(1);
const basec = ref(null);
const setting = ref(null);
const bangc = ref(null);
const changeTabs = async (value) => {
  if (value === 1) {
    await basec.value.initData();
  } else if (value === 2) {
    await setting.value.initData();
  } else if (value === 3) {
    await bangc.value.initData();
  }
};

// 更新头像start
const files = ref([]);
const requestMethod = async (file) => {
  const form = new FormData();
  form.append('file', file.raw);
  try {
    const res = await updateAvatar(form);
    if (res.code === 0) {
      MessagePlugin.success('更换成功');
      await userStore.getUserInfo();
      return new Promise((resolve) => {
        resolve({ status: 'success', response: { url: res.msg } });
      });
    }
    MessagePlugin.success(`更换失败:${res.msg}`);
    return new Promise((resolve) => {
      resolve({ status: 'fail', error: '上传失败' });
    });
  } catch (er) {
    MessagePlugin.success(`更换失败:${er}`);
    return new Promise((resolve) => {
      // resolve 参数为关键代码
      resolve({ status: 'fail', error: '上传失败' });
    });
  }
};
const beforeUpload = (file) => {
  if (file.size > 5 * 1024 * 1024) {
    MessagePlugin.warning('上传的图片不能大于5M');
    return false;
  }
  return true;
};
// 更新头像end

// vue的api
onMounted(async () => {});
</script>

<style lang="less" scoped>
@import '@/style/variables';
</style>
