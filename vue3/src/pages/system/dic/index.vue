<template>
  <div>
    <t-row :gutter="10">
      <t-col :span="7">
        <div class="sp-role-left">
          <t-card :bordered="false">
            <div class="sp-role-left-header">
              <t-row>
                <t-col :span="6">
                  <t-button v-if="authAdd" @click="addRow">新增字典</t-button>
                  <t-popconfirm content="是否确认立即刷新字典缓存 ?" @confirm="refCache">
                    <t-button theme="default">刷新缓存</t-button>
                  </t-popconfirm>
                </t-col>
                <t-col :span="6">
                  <t-row :gutter="10">
                    <t-col :flex="1" :span="6" :offset="4">
                      <t-input
                        v-model="params.label"
                        placeholder="请输入字典名称/编码"
                        type="search"
                        clearable
                        @clear="clearName"
                        @enter="firstFetch"
                      ></t-input>
                    </t-col>
                    <t-col :flex="1" :span="1">
                      <t-button theme="default" variant="outline" @click="firstFetch">查询</t-button>
                    </t-col>
                  </t-row>
                </t-col>
              </t-row>
            </div>
            <t-table
              row-key="id"
              :data="data"
              :max-height="'calc(98vh - 235px)'"
              :columns="columns"
              :table-layout="'fixed'"
              :pagination="pagination"
              :loading="dataLoading"
              :loading-props="{ size: '23px', text: '加载中...' }"
              @page-change="onPageChange"
            >
              <template #operation="{ row }">
                <t-button v-if="authEdit" size="small" variant="outline" theme="default" @click="editRow(row)"
                  >修改</t-button
                >
                <t-button v-if="authDel" size="small" variant="outline" theme="danger" @click="delRow(row)"
                  >删除</t-button
                >
                <t-button size="small" variant="outline" theme="success" @click="loadValues(row)">设置字典项</t-button>
              </template>
            </t-table>
          </t-card>
        </div>
      </t-col>
      <t-col :span="5">
        <div>
          <t-card
            :bordered="false"
            :title="selectDict.label ? '字典(' + selectDict.label + ')' : '未选择字典'"
            header-bordered
          >
            <template #actions>
              <t-button v-if="authAdd" theme="primary" :disabled="!selectDict.code" @click="addVRow"
                >新增字典项</t-button
              >
            </template>
            <t-table
              row-key="id"
              :data="items"
              :max-height="'calc(98vh - 235px)'"
              :columns="columns1"
              :table-layout="'fixed'"
              :loading="valuesLoad"
              :loading-props="{ size: '23px', text: '加载中...' }"
            >
              <template #operation="{ row }">
                <t-button v-if="authEdit" size="small" variant="outline" theme="default" @click="editVRow(row)"
                  >修改</t-button
                >
                <t-button v-if="authDel" size="small" variant="outline" theme="danger" @click="delVRow(row)"
                  >删除</t-button
                >
              </template>
            </t-table>
          </t-card>
        </div>
      </t-col>
    </t-row>
    <!-- 新增/修改字典项 -->
    <t-dialog
      v-model:visible="visibleModal"
      width="350"
      :close-on-overlay-click="false"
      :header="!dictForm.id ? '新增字典项' : '修改字典项'"
      mode="modal"
      draggable
      :confirm-btn="saveBtn"
      :on-confirm="onSubmit"
    >
      <template #body>
        <t-form ref="form" :label-align="'top'" :data="dictForm" :layout="'inline'" :rules="rules">
          <t-form-item label="字典项" name="label">
            <t-input v-model="dictForm.label" :style="{ width: '300px' }" placeholder="请输入字典项"></t-input>
          </t-form-item>
          <t-form-item label="字典值" name="value">
            <t-input v-model="dictForm.value" :style="{ width: '300px' }" placeholder="请输入字典值"></t-input>
          </t-form-item>
        </t-form>
      </template>
    </t-dialog>

    <!-- 新增/修改字典 -->
    <t-dialog
      v-model:visible="visibleModal1"
      width="350"
      :close-on-overlay-click="false"
      :header="!dictForm1.id ? '新增字典' : '修改字典'"
      mode="modal"
      draggable
      :confirm-btn="saveBtn1"
      :on-confirm="onSubmit1"
    >
      <template #body>
        <t-form ref="form1" :label-align="'top'" :data="dictForm1" :layout="'inline'" :rules="rules1">
          <t-form-item label="字典编码" name="code">
            <t-input v-model="dictForm1.code" :style="{ width: '300px' }" placeholder="请输入字典项"></t-input>
          </t-form-item>
          <t-form-item label="字典名称" name="label">
            <t-input v-model="dictForm1.label" :style="{ width: '300px' }" placeholder="请输入字典值"></t-input>
          </t-form-item>
        </t-form>
      </template>
    </t-dialog>
  </div>
</template>

<script lang="ts">
export default {
  name: 'ListBase',
};
</script>

<script setup lang="ts">
import { ref, onMounted, computed, reactive } from 'vue';
import { MessagePlugin, DialogPlugin } from 'tdesign-vue-next';
import { listDict, listDict1, dictValues, addDict, editDict, delDict, refDictCache } from '@/api/system/dict';
import { useUserStore } from '@/store';

const userStore = useUserStore();
// 权限控制
const authAdd = computed(() => {
  return userStore.roles.includes('system:dict:add');
});
const authEdit = computed(() => {
  return userStore.roles.includes('system:dict:edit');
});
const authDel = computed(() => {
  return userStore.roles.includes('system:dict:del');
});
const firstFetch = async () => {
  pagination.value.current = 1;
  await fetchData();
};
// 刷新缓存
const cacheLoad = ref(false);
const refCache = async () => {
  cacheLoad.value = true;
  MessagePlugin.loading('正在刷新中...');
  await refDictCache();
  cacheLoad.value = false;
  MessagePlugin.closeAll();
  MessagePlugin.success('字典缓存刷新完成');
};
// 右侧菜单树形start
const items = ref([]);
const valuesLoad = ref(false);
const columns1 = [
  {
    width: 160,
    colKey: 'label',
    title: '字典项',
    ellipsis: true,
    align: 'center',
  },
  {
    width: 160,
    colKey: 'value',
    title: '字典值',
    ellipsis: true,
    align: 'center',
  },
  {
    colKey: 'operation',
    title: '操作',
    width: 220,
    align: 'center',
    cell: 'operation',
    fixed: 'right',
  },
];
const fetchDictData = async () => {
  items.value = [];
  valuesLoad.value = true;
  try {
    const { data: list } = await dictValues({
      code: selectDict.code,
    });
    items.value = list;
  } catch (er) {
    MessagePlugin.error(er.message);
  } finally {
    valuesLoad.value = false;
  }
};
const addVRow = async () => {
  form.value.reset();
  dictForm.id = '';
  visibleModal.value = true;
};
const editVRow = async (row) => {
  form.value.reset();
  dictForm.id = row.id;
  dictForm.label = row.label;
  dictForm.value = row.value;
  visibleModal.value = true;
};
const delVRow = async (row) => {
  const confirmDia = DialogPlugin({
    header: '提醒',
    body: `是否确定删除(${row.label})字典项？`,
    confirmBtn: '继续删除',
    // cancelBtn: '在考虑下',
    onConfirm: ({ e }) => {
      confirmDia.hide();
      delDict({
        id: row.id,
      })
        .then((res) => {
          fetchDictData();
          MessagePlugin.success(res.message);
        })
        .catch((error) => {
          MessagePlugin.error(error.message);
        });
    },
    onClose: ({ e, trigger }) => {
      confirmDia.hide();
    },
  });
};
// 右侧菜单属性end

// 新增/修改弹窗start
const visibleModal = ref(false);
const dictForm = reactive({
  id: '',
  label: '',
  value: '',
});
const form = ref(null);
const saveBtn = reactive({
  content: '保存',
  loading: false,
});
const rules = {
  label: [{ required: true, message: '请输入字典项', type: 'error' }],
  value: [{ required: true, message: '请输入字典值', type: 'error' }],
};
const onSubmit = async () => {
  const result = await form.value.validate();
  if (typeof result !== 'object' && result) {
    saveBtn.content = '保存中...';
    saveBtn.loading = true;
    const submitForm = {
      label: dictForm.label,
      value: dictForm.value,
      code: '',
      id: '',
    };
    if (!dictForm.id) {
      submitForm.code = selectDict.code;
      try {
        const { message } = await addDict(submitForm);
        visibleModal.value = false;
        MessagePlugin.success(message);
        await fetchDictData();
      } catch (error) {
        MessagePlugin.error(error.message);
      } finally {
        saveBtn.content = '保存';
        saveBtn.loading = false;
      }
    } else {
      submitForm.id = dictForm.id;
      try {
        const { message } = await editDict(submitForm);
        visibleModal.value = false;
        MessagePlugin.success(message);
        await fetchDictData();
      } catch (error) {
        MessagePlugin.error(error.message);
      } finally {
        saveBtn.content = '保存';
        saveBtn.loading = false;
      }
    }
  }
};
// 新增/修改弹窗end

// 新增/修改字典弹窗start
const visibleModal1 = ref(false);
const dictForm1 = reactive({
  id: '',
  label: '',
  code: '',
});
const form1 = ref(null);
const saveBtn1 = reactive({
  content: '保存',
  loading: false,
});
const codeValidator = async (val) => {
  if (val) {
    const { data: list } = await listDict1({
      code: val,
      id: dictForm1.id,
    });
    if (list.length !== 0) {
      return { result: false, message: '字典编码重复', type: 'error' };
    }
  }
  return { result: true, type: 'success' };
};
const rules1 = {
  label: [{ required: true, message: '请输入字典项', type: 'error' }],
  code: [{ required: true, message: '请输入字典编码', type: 'error' }, { validator: codeValidator }],
};
const onSubmit1 = async () => {
  console.log('開始保存');
  const result = await form1.value.validate();
  console.log('開始保存1');
  if (typeof result !== 'object' && result) {
    saveBtn1.content = '保存中...';
    saveBtn1.loading = true;
    const submitForm = {
      label: dictForm1.label,
      code: dictForm1.code,
      value: null,
      id: null,
    };
    if (!dictForm1.id) {
      submitForm.value = '#';
      try {
        const { message } = await addDict(submitForm);
        visibleModal1.value = false;
        MessagePlugin.success(message);
        await fetchData();
      } catch (error) {
        MessagePlugin.error(error.message);
      } finally {
        saveBtn1.content = '保存';
        saveBtn1.loading = false;
      }
    } else {
      submitForm.id = dictForm1.id;
      try {
        const { message } = await editDict(submitForm);
        visibleModal1.value = false;
        MessagePlugin.success(message);
        selectDict.code = dictForm1.code;
        selectDict.label = dictForm1.label;
        await fetchData();
      } catch (error) {
        MessagePlugin.error(error.message);
      } finally {
        saveBtn1.content = '保存';
        saveBtn1.loading = false;
      }
    }
  }
};
// 新增/修改弹窗end

// 左侧角色菜单列表数据start
const data = ref([]);
const columns = [
  {
    width: 150,
    colKey: 'code',
    title: '字典编码',
    ellipsis: true,
  },
  {
    width: 160,
    colKey: 'label',
    title: '字典名称',
    ellipsis: true,
  },
  {
    width: 160,
    colKey: 'createtime',
    title: '创建时间',
  },
  {
    colKey: 'operation',
    title: '操作',
    width: 220,
    cell: 'operation',
    fixed: 'right',
  },
];
const dataLoading = ref(false);
const pagination = ref({
  pageSize: 20,
  total: 0,
  current: 1,
});
const params = reactive({
  label: '',
});
const onPageChange = async (pageInfo) => {
  pagination.value.current = pageInfo.current;
  pagination.value.pageSize = pageInfo.pageSize;
  await fetchData();
};
const clearName = async () => {
  params.label = '';
  pagination.value.current = 1;
  await fetchData();
};
const selectDict = reactive({
  code: '',
  label: '',
});
const loadValues = async (row) => {
  selectDict.code = row.code;
  selectDict.label = row.label;
  await fetchDictData();
};
const addRow = async () => {
  form1.value.reset();
  dictForm1.id = '';
  visibleModal1.value = true;
};
const editRow = async (row) => {
  form1.value.reset();
  dictForm1.id = row.id;
  dictForm1.code = row.code;
  dictForm1.label = row.label;
  visibleModal1.value = true;
};
const delRow = async (row) => {
  const confirmDia = DialogPlugin({
    header: '提醒',
    body: `删除之后将对应的字典项同时删除，是否确定删除字典(${row.label})？`,
    confirmBtn: '继续删除',
    // cancelBtn: '在考虑下',
    onConfirm: ({ e }) => {
      confirmDia.hide();
      delDict({
        id: row.id,
      })
        .then((res) => {
          fetchData();
          MessagePlugin.success(res.message);
        })
        .catch((error) => {
          MessagePlugin.error(error.message);
        });
    },
    onClose: ({ e, trigger }) => {
      confirmDia.hide();
    },
  });
};

const fetchData = async () => {
  data.value = [];
  dataLoading.value = true;
  const queryData = {
    ...params,
    ...pagination.value,
  };
  try {
    const {
      data: { list, total },
    } = await listDict(queryData);
    data.value = list;
    pagination.value.total = total;
  } catch (er) {
    MessagePlugin.error(er.message);
  } finally {
    dataLoading.value = false;
  }
};
// 左侧角色菜单列表数据end

// vue的api
onMounted(async () => {
  // await fetchData();
  await fetchData();
});
</script>

<style lang="less" scoped>
@import '@/style/variables';
.menu-active {
  color: var(--td-brand-color) !important;
}
.menu-unactive {
  color: var(--tdvns-text-color-primary) !important;
}
.menu-text {
  vertical-align: middle;
}
.sp-role-left {
  border-radius: 8px;
  .sp-role-left-header {
    padding-bottom: 30px;
  }
}
</style>
