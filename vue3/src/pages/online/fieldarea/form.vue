<template>
  <t-dialog
    v-model:visible="dialogFormVisible"
    :header="title"
    width="1000"
    :close-on-overlay-click="false"
    :confirm-btn="saveBtn"
    :on-confirm="save"
    :on-close="close"
  >
    <template #body>
      <t-form ref="formRef" :data="form" :layout="'inline'" :rules="rules">
        <t-form-item label="数据域编号" name="code">
          <t-input v-model="form.code" clearable :style="{ width: '300px' }" />
        </t-form-item>
        <t-form-item label="显示名称" name="name">
          <t-input v-model="form.name" clearable :style="{ width: '300px' }" />
        </t-form-item>
        <t-form-item label="数据类型" name="fieldType">
          <t-select v-model="form.fieldType" placeholder="请选择" :style="{ width: '300px' }" @change="onChange">
            <t-option v-for="item in optionEnum" :key="item.key" :value="item.key" :label="item.value"></t-option>
          </t-select>
        </t-form-item>
        <t-form-item label="数据类型编号" name="fieldCode">
          <t-input v-model="form.fieldCode" clearable :style="{ width: '300px' }" />
        </t-form-item>
        <t-form-item label="长度" name="fieldLength">
          <t-input v-model="form.fieldLength" clearable :style="{ width: '300px' }" />
        </t-form-item>
        <t-form-item label="小数位数" name="fieldDecimalLength">
          <t-input v-model="form.fieldDecimalLength" clearable :style="{ width: '300px' }" />
        </t-form-item>
      </t-form>
    </template>
  </t-dialog>
</template>

<script lang="ts">
export default {
  name: 'FormFieldArea',
};
</script>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { MessagePlugin } from 'tdesign-vue-next';
import { doEdit, getInfo } from '@/api/online/fieldarea';
import { getEnumItemList } from '@/api/base/data/enumItem';
import { EnumItem } from '@/types/enum';

const emits = defineEmits(['fetch-data']);
const formRef = ref(null);
const form = ref({
  sortIndex: null,
  code: null,
  name: null,
  fieldType: null,
  fieldCode: null,
  fieldLength: null,
  fieldDecimalLength: null,
});

const rules = ref({
  code: [{ required: true, message: '请输入数据域代码' }],
  name: [{ required: true, message: '请输入显示名称' }],
  fieldType: [{ required: true, message: '请选择数据类型' }],
});
const title = ref('');
const dialogFormVisible = ref(false);
const enums = ref<null | Map<string, Array<EnumItem>>>(new Map());

const showEdit = (id: any) => {
  if (!id) {
    title.value = '添加';
  } else {
    title.value = '编辑';
    initData(id);
  }
  dialogFormVisible.value = true;
};
onMounted(async () => {
  getEnums();
});
const getEnums = async () => {
  const { data } = await getEnumItemList('sys_DataDbName');
  if (data && data.length > 0) {
    for (const i in data) {
      if (!i) continue;
      enums.value.set(data[i].key, JSON.parse(data[i].value));
    }
  }
};
const onChange = (value: any) => {
  form.value.fieldCode = value;
};
const initData = async (id: any) => {
  const { data } = await getInfo(id);
  form.value = data;
};
const saveBtn = reactive({
  content: '保存',
  loading: false,
});
const close = () => {
  formRef.value.reset();
  dialogFormVisible.value = false;
};
const save = async () => {
  const result = await formRef.value.validate();
  if (typeof result !== 'object' && result) {
    const { message } = await doEdit(form.value);
    close();
    emits('fetch-data');
    MessagePlugin.success(message);
  }
};
defineExpose({
  showEdit,
});
</script>
