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
        <t-form-item label="名称" name="name">
          <t-input v-model="form.name" clearable :style="{ width: '300px' }" />
        </t-form-item>
        <t-form-item label="编号" name="code">
          <t-input v-model="form.code" clearable :style="{ width: '300px' }" />
        </t-form-item>
        <t-form-item label="排序号" name="sortIndex">
          <t-input v-model="form.sortIndex" clearable :style="{ width: '300px' }" />
        </t-form-item>
        <t-form-item label="说明" name="description">
          <t-textarea
            v-model="form.description"
            :style="{ width: '725px' }"
            placeholder="请输入"
            name="description"
            :autosize="{ minRows: 3, maxRows: 5 }"
          />
        </t-form-item>
        <div>
          <div>
            <t-button @click="handleAdd">新增</t-button>
          </div>
          <t-form-item label-width="0px" name="detail">
            <t-table ref="detailTable" :loading="false" :data="form.detail" row-key="name" :columns="detailColumns">
              <template #operation="{ $index }">
                <t-button theme="danger" variant="text" shape="round" @click="handleDelete($index)">删除</t-button>
              </template>
            </t-table>
          </t-form-item>
        </div>
      </t-form>
    </template>
  </t-dialog>
</template>

<script lang="ts">
export default {
  name: 'FormDataType',
};
</script>

<script setup lang="ts">
import { ref, computed, reactive } from 'vue';
import { Input, MessagePlugin } from 'tdesign-vue-next';
import { doEdit, getInfo } from '@/api/online/datatype';

const emits = defineEmits(['fetch-data']);
const formRef = ref(null);
const form = ref({
  name: null,
  code: null,
  sortIndex: null,
  description: null,
  detail: [],
});

const rules = ref({
  name: [{ required: true, message: '请输入名称' }],
  code: [{ required: true, message: '请输入编号' }],
});
const title = ref('');
const dialogFormVisible = ref(false);

const detailColumns = computed(() => [
  { colKey: 'row-select', align: 'center', width: 50, type: 'multiple', fixed: 'left' },
  {
    title: '数据库名称',
    align: 'center',
    colKey: 'name',
    fixed: 'left',
    width: 200,
    edit: {
      component: Input,
      props: {
        clearable: true,
        autofocus: true,
      },
      defaultEditable: true,
      rules: [{ required: true, message: '不能为空' }],
      abortEditOnEvent: ['onEnter'],
      onEdited: (context: any) => {
        form.value.detail.splice(context.rowIndex, 1, context.newRowData);
      },
    },
  },
  {
    title: '字段类型编号',
    align: 'center',
    width: 200,
    colKey: 'code',
    fixed: 'left',
    edit: {
      component: Input,
      props: {
        clearable: true,
        autofocus: true,
      },
      defaultEditable: true,
      rules: [{ required: true, message: '不能为空' }],
      abortEditOnEvent: ['onEnter'],
      onEdited: (context: any) => {
        form.value.detail.splice(context.rowIndex, 1, context.newRowData);
      },
    },
  },
  { title: '操作', align: 'center', width: 200, colKey: 'operation' },
]);

const showEdit = (id: any) => {
  if (!id) {
    title.value = '添加';
  } else {
    title.value = '编辑';
    initData(id);
  }
  dialogFormVisible.value = true;
};
const initData = async (id: any) => {
  const { data } = await getInfo(id);
  form.value = data;
};
const handleAdd = () => {
  form.value.detail.push({
    code: '',
    name: '',
  });
};
const handleDelete = (index: any) => {
  form.value.detail.splice(index, 1);
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
