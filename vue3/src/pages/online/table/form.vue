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
      <t-form ref="dataFormRef" :data="dataForm" :layout="'inline'" :rules="rules">
        <t-row>
          <t-col :span="6">
            <t-form-item label="数据库" name="dbName">
              <t-select v-model="dataForm.dbName" placeholder="请选择" :style="{ width: '300px' }">
                <t-option
                  v-for="item in enums.get('sys_ConnName')"
                  :key="item.code"
                  :value="item.code"
                  :label="item.name"
                ></t-option>
              </t-select>
            </t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item label="编号" name="code">
              <t-input v-model="dataForm.code" clearable :style="{ width: '300px' }" />
            </t-form-item>
          </t-col>
        </t-row>
        <t-row>
          <t-col :span="6">
            <t-form-item label="表名" name="tableCode">
              <t-input v-model="dataForm.tableCode" clearable :style="{ width: '300px' }" />
            </t-form-item>
          </t-col>
          <t-col :span="6">
            <t-form-item label="显示名称" name="tableName">
              <t-input v-model="dataForm.tableName" clearable :style="{ width: '300px' }" />
            </t-form-item>
          </t-col>
        </t-row>
        <t-row>
          <t-col :span="6">
            <t-form-item label="排序号" name="sortIndex">
              <t-input v-model="dataForm.sortIndex" clearable :style="{ width: '300px' }" />
            </t-form-item>
          </t-col>
        </t-row>
        <t-row style="width: 100%">
          <t-col :span="12">
            <query-form-top :span="12">
              <t-button theme="primary" @click="handleAdd()"> 添加 </t-button>
            </query-form-top>
            <t-form-item label="" label-width="0px" name="column" style="width: 100%">
              <t-table
                ref="detailTable"
                :loading="false"
                :data="dataForm.column"
                row-key="code"
                :columns="detailColumns"
              >
                <template #operation="{ $index }">
                  <t-button theme="danger" variant="text" shape="round" @click="handleDelete($index)">删除</t-button>
                </template>
              </t-table>
            </t-form-item>
          </t-col>
        </t-row>
      </t-form>
    </template>
  </t-dialog>
</template>

<script lang="ts">
export default {
  name: 'FormTable',
};
</script>

<script setup lang="ts">
import { ref, computed, reactive, onBeforeMount } from 'vue';
import { Input, MessagePlugin, Select } from 'tdesign-vue-next';
import { doEdit, getInfo } from '@/api/online/table';
import { getEnumItemList } from '@/api/base/data/enumItem';
import { EnumItem } from '@/types/enum';
import QueryFormTop from '@/components/query-form/components/query-form-top.vue';

const emits = defineEmits(['fetch-data']);
const dataFormRef = ref(null);
const dataForm = ref({
  dbName: null,
  code: null,
  tableCode: null,
  tableName: null,
  sortIndex: null,
  column: [],
});

const rules = ref({
  dbName: [{ required: true, message: '请选择数据库' }],
  name: [{ required: true, message: '请输入名称' }],
  code: [{ required: true, message: '请输入编号' }],
});
const title = ref('');
const dialogFormVisible = ref(false);
const enums = ref<null | Map<string, Array<EnumItem>>>(new Map());

const detailColumns = computed(() => [
  { colKey: 'row-select', align: 'center', width: 50, type: 'multiple' },
  {
    title: '字段编号',
    align: 'center',
    colKey: 'code',
    width: 150,
    edit: {
      component: Input,
      props: {
        clearable: true,
        autofocus: true,
      },
      defaultEditable: true,
      abortEditOnEvent: ['onEnter'],
      onEdited: (context: any) => {
        dataForm.value.column.splice(context.rowIndex, 1, context.newRowData);
      },
    },
  },
  {
    title: '字段名称',
    align: 'center',
    width: 150,
    colKey: 'name',
    edit: {
      component: Input,
      props: {
        clearable: true,
      },
      defaultEditable: true,
      abortEditOnEvent: ['onEnter'],
      onEdited: (context: any) => {
        dataForm.value.column.splice(context.rowIndex, 1, context.newRowData);
      },
    },
  },
  {
    title: '数据域',
    align: 'center',
    width: 150,
    colKey: 'name',
    edit: {
      component: Select,
      props: {
        clearable: true,
        option: true,
      },
      defaultEditable: true,
      abortEditOnEvent: ['onEnter'],
      onEdited: (context: any) => {
        dataForm.value.column.splice(context.rowIndex, 1, context.newRowData);
      },
    },
  },
  {
    title: '数据类型',
    align: 'center',
    width: 150,
    colKey: 'areaType',
    edit: {
      component: Select,
      props: {
        clearable: true,
        option: true,
      },
      defaultEditable: true,
      abortEditOnEvent: ['onEnter'],
      onEdited: (context: any) => {
        dataForm.value.column.splice(context.rowIndex, 1, context.newRowData);
      },
    },
  },
  {
    title: '长度',
    align: 'center',
    width: 100,
    colKey: 'dataLength',
    edit: {
      component: Input,
      props: {
        clearable: true,
        option: true,
      },
      defaultEditable: true,
      abortEditOnEvent: ['onEnter'],
      onEdited: (context: any) => {
        dataForm.value.column.splice(context.rowIndex, 1, context.newRowData);
      },
    },
  },
  {
    title: '小数位数',
    align: 'center',
    width: 120,
    colKey: 'decimalDigits',
    edit: {
      component: Input,
      props: {
        clearable: true,
        option: true,
      },
      defaultEditable: true,
      abortEditOnEvent: ['onEnter'],
      onEdited: (context: any) => {
        dataForm.value.column.splice(context.rowIndex, 1, context.newRowData);
      },
    },
  },
  { title: '操作', align: 'center', width: 100, colKey: 'operation' },
]);

onBeforeMount(() => {
  getEnums();
});

const getEnums = async () => {
  const { data } = await getEnumItemList('sys_ConnName');
  if (data && data.length > 0) {
    for (const i in data) {
      if (!i) continue;
      enums.value.set(data[i].key, JSON.parse(data[i].value));
    }
  }
};

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
  dataForm.value = data;
};
const handleAdd = () => {
  dataForm.value.column.push({
    code: '',
    name: '',
  });
};
const handleDelete = (index: any) => {
  dataForm.value.column.splice(index, 1);
};
const saveBtn = reactive({
  content: '保存',
  loading: false,
});
const close = () => {
  dataFormRef.value.reset();
  dialogFormVisible.value = false;
};
const save = async () => {
  const result = await dataFormRef.value.validate();
  if (typeof result !== 'object' && result) {
    const { message } = await doEdit(dataForm.value);
    close();
    emits('fetch-data');
    MessagePlugin.success(message);
  }
};
defineExpose({
  showEdit,
});
</script>
