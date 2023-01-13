<template>
  <div class="sp-main-info">
    <t-loading size="small" :loading="loading" show-overlay>
      <t-form ref="form" :data="userForm" label-width="160px" :rules="rules" @submit="onSubmit">
        <t-form-item label="用户昵称" name="nickName">
          <t-input v-model="userForm.nickName" :maxlength="10" placeholder="请输入用户昵称" :suffix="suffix"></t-input>
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

<script lang="ts">
export default {
  name: 'UserBase',
};
</script>

<script setup lang="ts">
import { ref, onMounted, computed, reactive } from 'vue';
import { MessagePlugin, DialogPlugin } from 'tdesign-vue-next';
import { queryUser, updateUser } from '@/api/system/user';

const form = ref(null);

const loading = ref(false);
const saveBtn = reactive({
  text: '保存',
  loading: false,
});

const emit = defineEmits(['updateUserInStore']);
const suffix = computed(() => {
  return `${userForm.nickName.length}/10`;
});
const initData = async () => {
  form.value.reset();
  loading.value = true;
  try {
    const res = await queryUser();
    if (res.data) {
      userForm.id = res.data.id;
      userForm.nickName = res.data.nickName;
    }
  } catch (error) {
    MessagePlugin.error(error.message);
  } finally {
    loading.value = false;
  }
};

// 表单
const rules = {
  nickName: [{ required: true, message: '请输入用户昵称', type: 'error', trigger: 'change' }],
};
const userForm = reactive({
  id: '',
  nickName: '',
});
const onSubmit = async ({ validateResult }) => {
  if (validateResult === true) {
    const confirmDia = DialogPlugin({
      header: '提醒',
      body: '是否确定保存本次修改?',
      confirmBtn: '确定',
      // cancelBtn: '暂不',
      onConfirm: async ({ e }) => {
        saveBtn.text = '保存中...';
        saveBtn.loading = true;
        confirmDia.hide();
        const subForm = {
          id: userForm.id,
          nickName: userForm.nickName,
        };
        try {
          const res = await updateUser(subForm);
          if (res.code === 0) {
            emit('updateUserInStore');
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
