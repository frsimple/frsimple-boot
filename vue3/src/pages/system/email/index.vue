<template>
  <div class="sp-main-info">
    <t-row :gutter="10">
      <t-col :span="5">
        <t-card :bordered="false" :hover-shadow="false" header-bordered title="邮件配置">
          <div>
            <t-loading size="small" :loading="loading" show-overlay>
              <t-form ref="form" :data="configForm" label-width="160px" :rules="rules" @submit="onSubmit">
                <t-form-item label="是否开启SSL" name="isssl">
                  <t-switch v-model="configForm.isssl" size="large" :label="['开', '关']"> </t-switch>
                </t-form-item>
                <t-form-item label="SMTP服务地址" name="host">
                  <t-input v-model="configForm.host" clearable placeholder="请输入SMTP服务地址"></t-input>
                </t-form-item>
                <t-form-item label="端口号" name="port" help="默认25端口">
                  <t-input v-model="configForm.port" clearable placeholder="请输入端口号"></t-input>
                </t-form-item>
                <t-form-item label="邮箱地址" name="username">
                  <t-input v-model="configForm.username" clearable placeholder="请输入邮箱地址"></t-input>
                </t-form-item>
                <t-form-item label="授权密钥" name="password">
                  <t-input
                    v-model="configForm.password"
                    clearable
                    placeholder="请输入授权密钥"
                    type="password"
                  ></t-input>
                </t-form-item>
                <t-form-item label="签名" name="siteName">
                  <t-input v-model="configForm.siteName" clearable placeholder="请输入签名"></t-input>
                </t-form-item>
                <t-form-item style="padding-top: 8px">
                  <t-button theme="primary" type="submit" :loading="saveBtn.loading" style="margin-right: 10px">{{
                    saveBtn.text
                  }}</t-button>
                </t-form-item>
              </t-form>
            </t-loading>
          </div>
        </t-card>
      </t-col>
      <t-col :span="7">
        <t-card :bordered="false" :hover-shadow="false" header-bordered title="新建邮件">
          <template #actions>
            <t-button
              theme="primary"
              type="submit"
              :loading="saveBtn1.loading"
              style="margin-right: 10px"
              :disabled="isSend"
              @click="onSubmit1"
              >{{ saveBtn1.text }}</t-button
            >
          </template>
          <t-form ref="form1" :data="emailForm" label-width="160px" :rules="rules1">
            <t-form-item label="收件人" name="tos" help="需要回车确认收件人地址">
              <t-tagInput v-model="emailForm.tos" clearable placeholder="请输入收件人地址"></t-tagInput>
            </t-form-item>
            <t-form-item label="标题" name="title">
              <t-input v-model="emailForm.title" clearable placeholder="请输入标题"></t-input>
            </t-form-item>
            <t-form-item label="内容" name="content">
              <t-textarea
                v-model="emailForm.content"
                clearable
                placeholder="请输入内容"
                :maxlength="500"
                :autosize="{ minRows: 5 }"
              ></t-textarea>
            </t-form-item>
            <t-form-item label="附件" name="files">
              <t-upload
                ref="upload"
                v-model="emailForm.files"
                :request-method="customUpload"
                tips="上传文件大小在 5M 以内"
                placeholder="支持批量上传文件，文件格式不限，最多只能上传 10 份文件"
                @change="fileChange"
              ></t-upload>
              <!-- <t-upload
                v-model="files"
                :tips="tips"
                theme="custom"
                :before-upload="beforeUpload"
                :request-method="customUpload"
                multiple
              >
                <t-button theme="default">点击上传</t-button>
              </t-upload> -->
              <!-- <div v-if="files && files.length" class="list-custom">
                <ul>
                  <li v-for="(item, index) in files" :key="index">{{ item.name }}</li>
                </ul>
              </div> -->
            </t-form-item>
          </t-form>
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
import { ref, onMounted, reactive, nextTick } from 'vue';
import { MessagePlugin, DialogPlugin } from 'tdesign-vue-next';
import { send } from 'vite';
import { saveOrUpdate, sendEmail, emailCfg } from '@/api/system/email';

const customUpload = async (files) => {
  return new Promise((resolve) => {
    // resolve 参数为关键代码
    resolve({ status: 'success', response: { url: 'https://tdesign.gtimg.com/site/avatar.jpg' } });
  });
};
const isSend = ref(true);
// 配置信息
const loading = ref(false);
const saveBtn = reactive({
  loading: false,
  text: '保存',
});
const configForm = reactive({
  id: '',
  host: '',
  port: '25',
  username: '',
  password: '',
  siteName: '',
  isssl: false,
});
const form = ref(null);
const rules = {
  isssl: [{ required: true, message: '请选择是否开启SSL', type: 'error', trigger: 'change' }],
  host: [{ required: true, message: '请输入SMTP服务地址', type: 'error', trigger: 'change' }],
  port: [{ required: true, message: '请输入端口号', type: 'error', trigger: 'change' }],
  username: [{ required: true, message: '请输入邮箱地址', type: 'error', trigger: 'change' }],
  password: [{ required: true, message: '请输入授权密钥', type: 'error', trigger: 'change' }],
  siteName: [{ required: true, message: '请输入签名', type: 'error', trigger: 'change' }],
};
const onSubmit = async ({ validateResult }) => {
  if (validateResult === true) {
    const confirmDia = DialogPlugin({
      header: '提醒',
      body: '是否确定保存(邮件配置)本次的修改?',
      confirmBtn: '确定',
      // cancelBtn: '暂不',
      onConfirm: async ({ e }) => {
        confirmDia.hide();
        saveBtn.loading = true;
        saveBtn.text = '保存中...';
        try {
          const formSub = {
            id: configForm.id,
            host: configForm.host,
            port: configForm.port,
            username: configForm.username,
            password: configForm.password,
            siteName: configForm.siteName,
            isssl: configForm.isssl ? 1 : 0,
          };
          const res = await saveOrUpdate(formSub);
          if (res.code === 0) {
            isSend.value = false;
            MessagePlugin.success('保存成功');
          } else {
            MessagePlugin.error(`保存失败:${res.msg}`);
          }
        } catch (error) {
          MessagePlugin.error(`保存失败:${error}`);
        } finally {
          saveBtn.loading = false;
          saveBtn.text = '保存';
        }
      },
      onClose: ({ e, trigger }) => {
        confirmDia.hide();
      },
    });
  }
};
// 配置信息end

// 发送邮件
const upload = ref(null);
const fileChange = async (values) => {
  console.log(values);
};
const form1 = ref(null);
const saveBtn1 = reactive({
  loading: false,
  text: '发送',
});
const emailForm = reactive({
  title: '',
  tos: [],
  content: '',
  files: [],
});
const rules1 = {
  title: [{ required: true, message: '请输入标题', type: 'error', trigger: 'change' }],
  tos: [{ required: true, message: '请输入收件人地址', type: 'error', trigger: 'change' }],
  content: [{ required: true, message: '请输入内容', type: 'error', trigger: 'change' }],
};
const onSubmit1 = async () => {
  const result = await form1.value.validate();
  if (result === true) {
    const confirmDia = DialogPlugin({
      header: '提醒',
      body: '是否确定发送?',
      confirmBtn: '确定',
      // cancelBtn: '暂不',
      onConfirm: async ({ e }) => {
        confirmDia.hide();
        saveBtn1.loading = true;
        saveBtn1.text = '发送中...';
        try {
          const formdData = new FormData();
          formdData.append('title', emailForm.title);
          formdData.append('tos', emailForm.tos.join(','));
          formdData.append('content', emailForm.content);
          // 循环封装附件对象
          if (emailForm.files && emailForm.files.length != 0) {
            emailForm.files.forEach((row) => {
              formdData.append('files', row.raw);
            });
          }
          const res = await sendEmail(formdData);
          if (res.code === 0) {
            MessagePlugin.success('发送成功');
          } else {
            MessagePlugin.error(`发送失败：${res.msg}`);
          }
        } catch (error) {
          MessagePlugin.error(`发送失败：${error}`);
        } finally {
          saveBtn1.loading = false;
          saveBtn1.text = '发送';
        }
      },
      onClose: ({ e, trigger }) => {
        confirmDia.hide();
      },
    });
  }
};
// 发送邮件end
const initData = async () => {
  const res = await emailCfg();
  if (res.data && res.data.length != 0) {
    configForm.id = res.data[0].id;
    configForm.host = res.data[0].host;
    configForm.port = res.data[0].port;
    configForm.username = res.data[0].username;
    configForm.password = res.data[0].password;
    configForm.siteName = res.data[0].siteName;
    if (res.data[0].isssl === 1) {
      configForm.isssl = true;
    } else {
      configForm.isssl = false;
    }
    isSend.value = false;
  } else {
    isSend.value = true;
    form.value.reset();
    configForm.id = '';
  }
};
// vue的api
onMounted(() => {
  nextTick(() => {
    initData();
  });
});
</script>

<style lang="less" scoped>
@import '@/style/variables';
</style>

<style>
.t-upload__flow-bottom {
  display: none !important;
}
</style>
