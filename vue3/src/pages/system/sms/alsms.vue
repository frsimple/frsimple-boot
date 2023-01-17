<template>
  <div class="sp-main-info">
    <t-loading size="small" :loading="loading" show-overlay>
      <t-form ref="form" :data="aliForm" label-width="160px" :rules="rules" @submit="onSubmit">
        <t-form-item label="Endpoint" name="endpoint" help="默认为：dysmsapi.aliyuncs.com">
          <t-input v-model="aliForm.endpoint" clearable placeholder="请输入Endpoint"></t-input>
        </t-form-item>
        <t-form-item label="地域(region)" name="region" help="没有特殊要求，默认为空">
          <t-input v-model="aliForm.region" clearable placeholder="请输入地域"></t-input>
        </t-form-item>
        <t-form-item label="accessKeyId" name="accessKeyId">
          <t-input v-model="aliForm.accessKeyId" type="password" clearable placeholder="请输入accessKeyId"></t-input>
        </t-form-item>
        <t-form-item label="accessKeySecret" name="accessKeySecret">
          <t-input
            v-model="aliForm.accessKeySecret"
            type="password"
            clearable
            placeholder="请输入accessKeySecret"
          ></t-input>
        </t-form-item>
        <t-form-item label="默认签名" name="sign">
          <t-input v-model="aliForm.sign" clearable placeholder="请输入默认签名"></t-input>
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
import { ref, onMounted, reactive } from 'vue';
import { MessagePlugin, DialogPlugin } from 'tdesign-vue-next';
import { saveOrUpdate, smsConfig } from '@/api/system/sms';

const smsType = ref('ALI');
const form = ref(null);

const loading = ref(false);
const saveBtn = reactive({
  text: '保存',
  loading: false,
});

const initData = async () => {
  form.value.reset();
  aliForm.endpoint = 'dysmsapi.aliyuncs.com';
  loading.value = true;
  try {
    const res = await smsConfig(smsType.value);
    if (res.data) {
      aliForm.id = res.data.id;
      aliForm.endpoint = res.data.endpoint;
      aliForm.region = res.data.region;
      aliForm.accessKeyId = res.data.secretId;
      aliForm.accessKeySecret = res.data.secretKey;
      aliForm.sign = res.data.sign;
    }
  } catch (error) {
    return;
  } finally {
    loading.value = false;
  }
};

// 表单
const rules = {
  endpoint: [{ required: true, message: '请输入Endpoint', type: 'error', trigger: 'change' }],
  accessKeyId: [{ required: true, message: '请输入accesskeyId', type: 'error', trigger: 'change' }],
  accessKeySecret: [{ required: true, message: '请输入accessKeySecret', type: 'error', trigger: 'change' }],
  sign: [{ required: true, message: '请输入默认签名', type: 'error', trigger: 'change' }],
};
const aliForm = reactive({
  id: '',
  endpoint: 'dysmsapi.aliyuncs.com',
  region: '',
  accessKeyId: '',
  accessKeySecret: '',
  sign: '',
});
const onSubmit = async ({ validateResult }) => {
  if (validateResult === true) {
    const confirmDia = DialogPlugin({
      header: '提醒',
      body: '是否确定保存(阿里云短信)本次的修改?',
      confirmBtn: '确定',
      // cancelBtn: '暂不',
      onConfirm: async ({ e }) => {
        saveBtn.text = '保存中...';
        saveBtn.loading = true;
        confirmDia.hide();
        const subForm = {
          id: aliForm.id,
          endpoint: aliForm.endpoint,
          region: aliForm.region,
          secretId: aliForm.accessKeyId,
          secretKey: aliForm.accessKeySecret,
          type: smsType.value,
          sign: aliForm.sign,
        };
        try {
          const res = await saveOrUpdate(subForm);
          if (res.code === 0) {
            await initData();
            MessagePlugin.success('保存成功');
          } else {
            MessagePlugin.error(`保存失败：${res.msg}`);
          }
        } catch (error) {
          MessagePlugin.error(`保存失败：${error}`);
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
// vue的api
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
  width: 50%;
}
</style>
