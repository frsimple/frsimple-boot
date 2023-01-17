<template>
  <div class="sp-main-info">
    <t-loading size="small" :loading="loading" show-overlay>
      <t-form ref="form" :data="aliForm" @submit="onSubmit" labelWidth="160px" :rules="rules">
        <t-form-item label="Region" name="region">
          <t-input v-model="aliForm.region" clearable placeholder="请输入Region"></t-input>
        </t-form-item>
        <t-form-item label="存储桶" name="workspace">
          <t-input v-model="aliForm.workspace" clearable placeholder="请输入存储桶"></t-input>
        </t-form-item>
        <t-form-item label="appId" name="appid">
          <t-input v-model="aliForm.appid" clearable placeholder="请输入appId"></t-input>
        </t-form-item>
        <t-form-item label="secretId" name="accesskeyid">
          <t-input v-model="aliForm.accesskeyid" type="password" clearable placeholder="请输入secretId"></t-input>
        </t-form-item>
        <t-form-item label="secretKey" name="accesskeysecret">
          <t-input v-model="aliForm.accesskeysecret" type="password" clearable placeholder="请输入secretKey"></t-input>
        </t-form-item>
        <t-form-item style="padding-top: 8px">
          <t-button theme="primary" type="submit" :loading="saveBtn.loading" style="margin-right: 10px">{{
            saveBtn.text
          }}</t-button>
        </t-form-item>
      </t-form>
    </t-loading>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, reactive } from 'vue';
import { MessagePlugin, DialogPlugin } from 'tdesign-vue-next';
import { addOrUpdateOss, getOssInfo } from '@/api/system/oss';

const ossType = ref('TENCENTCOS');
const form = ref(null);

const loading = ref(false);
const saveBtn = reactive({
  text: '保存',
  loading: false,
});

const initData = async () => {
  form.value.reset();
  loading.value = true;
  try {
    let res = await getOssInfo(ossType.value);
    if (res.data) {
      aliForm.id = res.data.id;
      aliForm.region = res.data.region;
      aliForm.appid = res.data.appid;
      aliForm.workspace = res.data.workspace;
      aliForm.accesskeyid = res.data.accessKeyId;
      aliForm.accesskeysecret = res.data.accessKeySecret;
    }
  } catch (error) {
  } finally {
    loading.value = false;
  }
};

//表单
const rules = {
  region: [{ required: true, message: '请输入Region', type: 'error', trigger: 'change' }],
  workspace: [{ required: true, message: '请输入Bucket', type: 'error', trigger: 'change' }],
  appid: [{ required: true, message: '请输入appId', type: 'error', trigger: 'change' }],
  accesskeyid: [{ required: true, message: '请输入secretId', type: 'error', trigger: 'change' }],
  accesskeysecret: [{ required: true, message: '请输入secretKey', type: 'error', trigger: 'change' }],
};
const aliForm = reactive({
  id: '',
  region: '',
  workspace: '',
  appid: '',
  accesskeyid: '',
  accesskeysecret: '',
});
const onSubmit = async ({ validateResult }) => {
  if (validateResult === true) {
    const confirmDia = DialogPlugin({
      header: '提醒',
      body: '是否确定保存(腾讯Cos)本次的修改?',
      confirmBtn: '确定',
      //cancelBtn: '暂不',
      onConfirm: async ({ e }) => {
        saveBtn.text = '保存中...';
        saveBtn.loading = true;
        confirmDia.hide();
        let subForm = {
          id: aliForm.id,
          region: aliForm.region,
          workspace: aliForm.workspace,
          appid: aliForm.appid,
          accessKeyId: aliForm.accesskeyid,
          accessKeySecret: aliForm.accesskeysecret,
          type: ossType.value,
        };
        try {
          let res = await addOrUpdateOss(subForm);
          if (res.code === 0) {
            await initData();
            MessagePlugin.success('保存成功');
          } else {
            MessagePlugin.error('保存失败：' + res.msg);
          }
        } catch (error) {
          MessagePlugin.error('保存失败：' + error);
        } finally {
          saveBtn.text = '保存';
          saveBtn.loading = false;
        }
      },
      onClose: ({ e, trigger }) => {
        confirmDia.hide();
      },
    });
  }
};
//vue的api
onMounted(async () => {
  await initData();
});

defineExpose({
  initData,
});
</script>

<style lang="less" scoped>
@import '@/style/variables';

.sp-main-info {
  padding: 30px;
  //width: 50%;
}
</style>
