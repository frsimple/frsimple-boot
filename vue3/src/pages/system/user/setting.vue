<template>
  <div class="sp-main-info">
    <t-loading size="small" :loading="loading" show-overlay>
      <t-list :split="true">
        <t-list-item>
          <t-list-item-meta title="账户密码">
            <template #description>
              <div :style="{ color: '#00000073' }">当前密码强度：强</div>
            </template>
          </t-list-item-meta>
          <template #action>
            <t-button variant="text" theme="primary" @click="optValue(0)"> 修改 </t-button>
          </template>
        </t-list-item>
        <t-list-item>
          <t-list-item-meta title="关联手机">
            <template #description>
              <div :style="{ color: '#00000073' }">已绑定手机：{{ setFrom.phone }}</div>
            </template>
          </t-list-item-meta>
          <template #action>
            <t-button variant="text" theme="primary" @click="optValue(1)"> 修改 </t-button>
          </template>
        </t-list-item>
        <t-list-item>
          <t-list-item-meta title="关联邮箱">
            <template #description>
              <div :style="{ color: '#00000073' }">已绑定邮箱：{{ setFrom.email }}</div>
            </template>
          </t-list-item-meta>
          <template #action>
            <t-button variant="text" theme="primary" @click="optValue(2)"> 修改 </t-button>
          </template>
        </t-list-item>
      </t-list>
    </t-loading>

    <!-- 修改手机号 -->
    <t-dialog
      v-model:visible="visibleModal"
      width="330"
      :close-on-overlay-click="false"
      :header="'修改关联手机'"
      mode="modal"
      draggable
      :confirm-btn="saveBtn"
      :on-confirm="onSubmit"
    >
      <template #body>
        <t-form ref="form" :label-align="'top'" :data="phoneForm" :rules="phoneRules">
          <t-form-item label="用户密码" name="password">
            <t-input
              v-model="phoneForm.password"
              :style="{ width: '260px' }"
              placeholder="请输入用户密码"
              type="password"
            ></t-input>
          </t-form-item>
          <t-form-item label="新手机号" name="phone">
            <t-input v-model="phoneForm.phone" :style="{ width: '260px' }" placeholder="请输入新手机号"></t-input>
          </t-form-item>
          <t-form-item label="验证码" class="verification-code" name="code">
            <t-input v-model="phoneForm.code" placeholder="请输入短信验证码"> </t-input>
            <t-button
              variant="outline"
              :disabled="countDown > 0 || isCanSendSms"
              :style="{ marginLeft: '10px' }"
              @click="sendSmsHandle"
            >
              {{ countDown == 0 ? '发送验证码' : `${countDown}秒后可重发` }}
            </t-button>
          </t-form-item>
        </t-form>
      </template>
    </t-dialog>

    <!-- 修改邮箱 -->
    <t-dialog
      v-model:visible="visibleModal1"
      width="330"
      :close-on-overlay-click="false"
      :header="'修改关联邮箱'"
      mode="modal"
      draggable
      :confirm-btn="saveBtn1"
      :on-confirm="onSubmit1"
    >
      <template #body>
        <t-form ref="form1" :label-align="'top'" :data="phoneForm1" :rules="phoneRules1">
          <t-form-item label="用户密码" name="password">
            <t-input
              v-model="phoneForm1.password"
              :style="{ width: '260px' }"
              placeholder="请输入用户密码"
              type="password"
            ></t-input>
          </t-form-item>
          <t-form-item label="新邮箱地址" name="email">
            <t-input v-model="phoneForm1.email" :style="{ width: '260px' }" placeholder="请输入新邮箱地址"></t-input>
          </t-form-item>
          <t-form-item label="验证码" class="verification-code" name="code">
            <t-input v-model="phoneForm1.code" placeholder="请输入邮箱验证码"> </t-input>
            <t-button
              variant="outline"
              :disabled="countDown > 0 || isCanSendSms1"
              :style="{ marginLeft: '10px' }"
              @click="sendSmsHandle1"
            >
              {{ countDown == 0 ? '发送验证码' : `${countDown}秒后可重发` }}
            </t-button>
          </t-form-item>
        </t-form>
      </template>
    </t-dialog>

    <!-- 修改密码 -->
    <t-dialog
      v-model:visible="visibleModal2"
      width="450"
      :close-on-overlay-click="false"
      :header="'修改用户密码'"
      mode="modal"
      draggable
      :confirm-btn="saveBtn2"
      :on-confirm="onSubmit2"
    >
      <template #body>
        <t-form ref="form2" :label-align="'top'" :data="passwordForm" :rules="prules">
          <t-form-item label="当前密码" name="password">
            <t-input
              v-model="passwordForm.password"
              :style="{ width: '400px' }"
              placeholder="请输入当前密码"
              type="password"
            ></t-input>
          </t-form-item>
          <t-form-item label="新密码" name="nPassword">
            <t-input
              v-model="passwordForm.nPassword"
              :style="{ width: '400px' }"
              type="password"
              placeholder="请输入新密码"
            ></t-input>
          </t-form-item>
          <t-form-item label="确认新密码" name="rnPassword">
            <t-input
              v-model="passwordForm.rnPassword"
              :style="{ width: '400px' }"
              placeholder="请输入确认新密码"
              type="password"
            ></t-input>
          </t-form-item>
        </t-form>
      </template>
    </t-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, reactive } from 'vue';
import { MessagePlugin, DialogPlugin } from 'tdesign-vue-next';
import { saveOrUpdate, smsConfig } from '@/api/system/sms';
import { updatePwd, queryUser, checkPwd, sendMsg, updatePhone, sendEmail, updateEmail } from '@/api/system/user';
import { useCounter } from '@/hooks';

const loading = ref(false);

// 修改邮箱start
const isCanSendSms1 = computed(() => {
  if (phoneForm1.email) {
    return false;
  }
  return true;
});
const sendSmsHandle1 = async () => {
  try {
    const res = await sendEmail({
      email: phoneForm1.email,
    });
    if (res.code === 0) {
      handleCounter();
    } else {
      MessagePlugin.error(res.msg);
    }
  } catch (error) {
    MessagePlugin.error(`发送失败：${error}`);
  }
};
const visibleModal1 = ref(false);
const form1 = ref(null);
const saveBtn1 = reactive({
  text: '保存',
  loading: false,
});
const onSubmit1 = async () => {
  const result = await form1.value.validate();
  if (result === true) {
    saveBtn1.text = '保存中...';
    saveBtn1.loading = true;
    const submitForm = {
      password: phoneForm1.password,
      code: phoneForm1.code,
    };
    try {
      const result1 = await updateEmail(submitForm);
      if (result1.code === 0) {
        visibleModal1.value = false;
        await initData();
        MessagePlugin.success('关联邮箱修改成功');
      } else {
        MessagePlugin.error(`关联邮箱修改失败:${result1.msg}`);
      }
    } catch (error) {
      MessagePlugin.error(`关联邮箱修改失败:${error}`);
    } finally {
      saveBtn1.text = '保存';
      saveBtn1.loading = false;
    }
  }
};
const pwdCheck1 = async (val) => {
  if (val) {
    const res = await checkPwd({
      password: phoneForm1.password,
    });
    if (res.code === 1) {
      return { result: false, message: '密码错误', type: 'error' };
    }
  }
  return { result: true, type: 'success' };
};
const phoneRules1 = {
  password: [{ required: true, message: '请输入用户密码', type: 'error' }, { validator: pwdCheck1 }],
  email: [
    { required: true, message: '请输入更换的邮箱地址', type: 'error' },
    { email: true, message: '请输入正确邮箱地址' },
  ],
  code: [{ required: true, message: '请输入验证码', type: 'error' }],
};
const phoneForm1 = reactive({
  email: '',
  code: '',
  password: '',
});
// 修改邮箱end

// 修改手机号start
const isCanSendSms = computed(() => {
  if (phoneForm.phone && phoneForm.phone.length == 11) {
    return false;
  }
  return true;
});
const [countDown, handleCounter] = useCounter();
const sendSmsHandle = async () => {
  try {
    const res = await sendMsg({
      phone: phoneForm.phone,
    });
    if (res.code === 0) {
      handleCounter();
    } else {
      MessagePlugin.error(res.msg);
    }
  } catch (error) {
    MessagePlugin.error(`发送失败：${error}`);
  }
};
const visibleModal = ref(false);
const form = ref(null);
const saveBtn = reactive({
  text: '保存',
  loading: false,
});
const onSubmit = async () => {
  const result = await form.value.validate();
  if (result === true) {
    saveBtn.text = '保存中...';
    saveBtn.loading = true;
    const submitForm = {
      password: phoneForm.password,
      code: phoneForm.code,
    };
    try {
      const result1 = await updatePhone(submitForm);
      if (result1.code === 0) {
        visibleModal.value = false;
        await initData();
        MessagePlugin.success('关联手机修改成功');
      } else {
        MessagePlugin.error(`关联手机修改失败:${result1.msg}`);
      }
    } catch (error) {
      MessagePlugin.error(`关联手机修改失败:${error}`);
    } finally {
      saveBtn.text = '保存';
      saveBtn.loading = false;
    }
  }
};
const pwdCheck = async (val) => {
  if (val) {
    const res = await checkPwd({
      password: phoneForm.password,
    });
    if (res.code === 1) {
      return { result: false, message: '密码错误', type: 'error' };
    }
  }
  return { result: true, type: 'success' };
};
const phoneRules = {
  password: [{ required: true, message: '请输入用户密码', type: 'error' }, { validator: pwdCheck }],
  phone: [
    { required: true, message: '请输入更换新手机号', type: 'error' },
    { telnumber: true, message: '请输入正确的手机号码' },
  ],
  code: [{ required: true, message: '请输入验证码', type: 'error' }],
};
const phoneForm = reactive({
  phone: '',
  code: '',
  password: '',
});
// 修改手机end

const setFrom = reactive({
  email: '',
  phone: '',
});

const initData = async () => {
  loading.value = true;
  try {
    const res = await queryUser();
    if (res.data) {
      setFrom.email = res.data.email;
      setFrom.phone = res.data.phone;
    }
  } catch (error) {
  } finally {
    loading.value = false;
  }
};

const optValue = async (index) => {
  if (index === 0) {
    form2.value.reset();
    visibleModal2.value = true;
  } else if (index === 1) {
    form.value.reset();
    visibleModal.value = true;
  } else if (index === 2) {
    form1.value.reset();
    visibleModal1.value = true;
  }
};

// 修改密码start
const form2 = ref(null);
const visibleModal2 = ref(false);
const passwordForm = ref({
  username: '',
  password: '',
  nPassword: '',
  rnPassword: '',
});

const npwValidator = async (val) => {
  if (val) {
    const rex = /^\S*(?=\S{6,})(?=\S*\d)(?=\S*[A-Z])(?=\S*[a-z])(?=\S*[!@#$%^&*? ])\S*$/;
    if (!rex.test(val)) {
      return { result: false, message: '至少六位，需包含大小写字，数字和特殊字符', type: 'error' };
    }
  }
  return { result: true, type: 'success' };
};
const pwValidator1 = async (val) => {
  if (val) {
    const res = await checkPwd({
      password: passwordForm.value.password,
    });
    if (res.code === 1) {
      return { result: false, message: '密码错误', type: 'error' };
    }
  }
  return { result: true, type: 'success' };
};
const rnPasswordValidator = async (val) => {
  if (val) {
    if (val !== passwordForm.value.nPassword) {
      return { result: false, message: '两次输入的密码不一致', type: 'error' };
    }
  }
  return { result: true, type: 'success' };
};
const prules = {
  password: [{ required: true, message: '请输入当前密码', type: 'error' }, { validator: pwValidator1 }],
  nPassword: [{ required: true, message: '请输入新密码', type: 'error' }, { validator: npwValidator }],
  rnPassword: [{ required: true, message: '请输入确认新密码', type: 'error' }, { validator: rnPasswordValidator }],
};
const saveBtn2 = reactive({
  content: '保存',
  loading: false,
});
const onSubmit2 = async () => {
  const result = await form2.value.validate();
  if (typeof result !== 'object' && result) {
    saveBtn2.content = '保存中...';
    saveBtn2.loading = true;
    const submitForm = {
      nPassword: passwordForm.value.nPassword,
    };
    try {
      const result1 = await updatePwd(submitForm);
      if (result1.code === 0) {
        visibleModal2.value = false;
        await initData();
        MessagePlugin.success('密码修改成功');
      } else {
        MessagePlugin.error(`密码修改失败:${result1.msg}`);
      }
    } catch (error) {
      MessagePlugin.error(`密码修改失败:${error}`);
    } finally {
      saveBtn2.content = '保存';
      saveBtn2.loading = false;
    }
  }
};
// 修改密码end

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
