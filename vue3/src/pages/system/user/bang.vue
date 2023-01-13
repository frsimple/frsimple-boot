<template>
  <div class="sp-main-info">
    <t-loading size="small" :loading="loading" show-overlay>
      <t-list :split="true">
        <t-list-item>
          <t-list-item-meta title="微信小程序">
            <template #description>
              <div :style="{ color: '#00000073' }">未绑定</div>
            </template>
            <template #image>
              <img src="../../../assets/img/weixin-mini-app.png" />
            </template>
          </t-list-item-meta>
          <template #action>
            <t-button variant="text" theme="primary"> 修改 </t-button>
          </template>
        </t-list-item>
      </t-list>
    </t-loading>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue';
import { MessagePlugin, DialogPlugin } from 'tdesign-vue-next';
import { saveOrUpdate, smsConfig } from '@/api/system/sms';
// import wechatMini from '@/assets/img/weixin-mini-app.png';

const smsType = ref('ALI');
const form = ref(null);

const loading = ref(false);
const saveBtn = reactive({
  text: '保存',
  loading: false,
});

const initData = async () => {
  aliForm.endpoint = 'dysmsapi.aliyuncs.com';
  loading.value = true;
  try {
    const res = await smsConfig(smsType.value);
    if (res.data) {
      aliForm.id = res.data.id;
      aliForm.endpoint = res.data.endpoint;
      aliForm.region = res.data.region;
      aliForm.accesskeyid = res.data.secretid;
      aliForm.accesskeysecret = res.data.secretkey;
      aliForm.sign = res.data.sign;
    }
  } catch (error) {
  } finally {
    loading.value = false;
  }
};

// 表单
const rules = {
  endpoint: [{ required: true, message: '请输入Endpoint', type: 'error', trigger: 'change' }],
  accesskeyid: [{ required: true, message: '请输入accesskeyId', type: 'error', trigger: 'change' }],
  accesskeysecret: [{ required: true, message: '请输入accessKeySecret', type: 'error', trigger: 'change' }],
  sign: [{ required: true, message: '请输入默认签名', type: 'error', trigger: 'change' }],
};
const aliForm = reactive({
  id: '',
  endpoint: 'dysmsapi.aliyuncs.com',
  region: '',
  accesskeyid: '',
  accesskeysecret: '',
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
          secretid: aliForm.accesskeyid,
          secretkey: aliForm.accesskeysecret,
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
  //width: 50%;
}
</style>
