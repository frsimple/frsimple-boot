<template>
  <t-form
    ref="form"
    :class="['item-container', `login-${type}`]"
    :data="formData"
    :rules="FORM_RULES"
    label-width="0"
    @submit="onSubmit"
  >
    <t-tabs :default-value="'password'" class="logintab" @change="changeLoginT">
      <t-tab-panel :value="'password'" label="账号登录" :style="{ paddingTop: '30px' }">
        <!-- <template> -->
        <t-form-item name="userName">
          <t-input v-model="formData.userName" size="large" placeholder="请输入登录账号/邮箱地址/手机号">
            <template #prefix-icon>
              <t-icon name="user" />
            </template>
          </t-input>
        </t-form-item>
        <t-form-item name="password">
          <t-input
            v-model="formData.password"
            size="large"
            :type="showPsw ? 'text' : 'password'"
            clearable
            placeholder="请输入登录密码"
          >
            <template #prefix-icon>
              <t-icon name="lock-on" />
            </template>
            <template #suffix-icon>
              <t-icon :name="showPsw ? 'browse' : 'browse-off'" @click="showPsw = !showPsw" />
            </template>
          </t-input>
        </t-form-item>
        <transition name="fade" mode="out-in">
          <t-form-item v-if="formData.password && formData.userName" name="code">
            <t-input v-model.trim="formData.code" size="large" placeholder="请输入验证码">
              <template #prefix-icon>
                <t-icon name="mail" />
              </template>
            </t-input>
            <img
              v-if="codeImg"
              :src="codeImg"
              style="width: 120px; height: 35px; margin-left: 10px"
              @click="getImgCode"
            />
          </t-form-item>
        </transition>
        <div class="check-container remember-pwd">
          <!-- <t-checkbox>记住账号</t-checkbox> -->
          <!-- <span class="tip">忘记账号？</span> -->
        </div>
        <!-- </template> -->
      </t-tab-panel>
      <t-tab-panel :value="'phone'" label="手机登录" :style="{ paddingTop: '30px', paddingBottom: '30px' }">
        <!-- <template> -->
        <t-form-item name="phone">
          <t-input v-model="formData.phone" size="large" placeholder="请输入手机号">
            <template #prefix-icon>
              <t-icon name="mobile" />
            </template>
          </t-input>
        </t-form-item>
        <transition name="fade" mode="out-in">
          <t-form-item v-if="formData.phone" class="verification-code" name="code">
            <t-input v-model="formData.code" size="large" placeholder="请输入短信验证码">
              <template #prefix-icon>
                <t-icon name="mail" />
              </template>
            </t-input>
            <t-button variant="outline" :disabled="countDown > 0 || isCanSendSms" @click="sendSmsHandle">
              {{ countDown == 0 ? '发送验证码' : `${countDown}秒后可重发` }}
            </t-button>
          </t-form-item>
        </transition>
        <!-- </template> -->
      </t-tab-panel>
    </t-tabs>

    <!-- 扫码登陆 -->
    <!-- <template v-else-if="type == 'qrcode'">
      <div class="tip-container">
        <span class="tip">请使用微信扫一扫登录</span>
        <span class="refresh">刷新 <t-icon name="refresh" /> </span>
      </div>
      <qrcode-vue value="" :size="192" level="H" />
    </template> -->

    <!-- 手机号登陆 -->

    <t-form-item v-if="type !== 'qrcode'" class="btn-container">
      <t-button block size="large" type="submit" :loading="isLoad" :disabled="isCanLogin"> 登录 </t-button>
    </t-form-item>

    <!-- <div class="switch-container">
      <span v-if="type !== 'password'" class="tip" @click="switchType('password')">使用账号密码登录</span>
      <span v-if="type !== 'qrcode'" class="tip" @click="switchType('qrcode')">使用微信扫码登录</span>
      <span v-if="type !== 'phone'" class="tip" @click="switchType('phone')">使用手机号登录</span>
    </div> -->
  </t-form>
</template>

<script setup lang="ts">
import { onMounted, ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import QrcodeVue from 'qrcode.vue';
import { MessagePlugin } from 'tdesign-vue-next';
import { useCounter } from '@/hooks';
import { useUserStore, useTabsRouterStore } from '@/store';
import proxy from '@/config/proxy';
import { sendSms } from '@/api/system/auth';

const userStore = useUserStore();
const tabsRouter = useTabsRouterStore();

const isShow = ref(false);
const codeImg = ref('');
const INITIAL_DATA = {
  phone: '',
  userName: 'system',
  password: 'Frsimple@2022',
  code: '',
  sp: '',
  checked: false,
  device: 'web',
};

const changeLoginT = async (value) => {
  type.value = value;
};
const FORM_RULES = {
  phone: [
    { required: true, message: '手机号必填', type: 'error' },
    { telnumber: true, message: '请输入正确的手机号码' },
  ],
  userName: [{ required: true, message: '账号必填', type: 'error' }],
  password: [{ required: true, message: '密码必填', type: 'error' }],
  code: [{ required: true, message: '验证码必填', type: 'error' }],
};

const env = import.meta.env.MODE || 'development';
const type = ref('password');

const formData = ref({ ...INITIAL_DATA });
const showPsw = ref(false);
const isCanLogin = computed(() => {
  if (type.value === 'password') {
    return !(formData.value.userName && formData.value.password && formData.value.code);
  }
  if (type.value === 'phone') {
    return !(formData.value.phone && formData.value.code);
  }
  return false;
});
const isCanSendSms = computed(() => {
  if (type.value === 'phone') {
    if (formData.value.phone && formData.value.phone.length === 11) {
      return false;
    }
    return true;
  }
  return true;
});

const form = ref(null);
const [countDown, handleCounter] = useCounter();
const sendSmsHandle = async () => {
  try {
    const res = await sendSms({
      sp: formData.value.phone,
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
const switchType = (val: string) => {
  type.value = val;
};

const router = useRouter();

// 获取验证码
const getImgCode = async () => {
  const sp = String(Math.random() * 23);
  formData.value.sp = sp;
  codeImg.value = `${proxy[env].host}/center/system/auth/getCode?sp=${sp}`;
};

onMounted(async () => {
  await getImgCode();
});

const isLoad = ref(false);
const onSubmit = async ({ validateResult }) => {
  if (validateResult === true) {
    try {
      // 开始登陆操作
      isLoad.value = true;
      if (type.value === 'password') {
        await userStore.login(formData.value);
      } else if (type.value === 'phone') {
        await userStore.loginByPhone(formData.value);
      }

      tabsRouter.initTabs();
      MessagePlugin.success('登陆成功');
      router.push({
        path: '/main',
      });
    } catch (e) {
      if (type.value === 'password') {
        await getImgCode();
      }
      MessagePlugin.error(e.message);
    } finally {
      isLoad.value = false;
    }
  }
};
</script>

<style lang="less" scoped>
@import url('../index.less');

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.logintab {
  background-color: transparent !important;
}
.t-tabs__nav-container:after {
  background-color: transparent !important;
}
</style>
<style>
.t-tabs__nav-container.t-is-top::after {
  height: 0px !important;
}
</style>
