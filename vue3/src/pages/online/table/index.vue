<template>
  <div>
    <t-card :bordered="false">
      <query-form>
        <query-form-left :span="6">
          <t-button content="新增" theme="primary" @click="handleEdit">
            <template #icon>
              <add-icon />
            </template>
          </t-button>
        </query-form-left>
        <query-form-right :span="6">
          <t-form :layout="'inline'" label-width="0px" :model="query" @submit.prevent>
            <t-form-item>
              <t-space>
                <t-input v-model="query.searchValue" clearable placeholder="请输入关键字" :style="{ width: '300px' }" />
                <t-button theme="primary" type="submit" @click="queryData">
                  <template #icon> <search-icon /> </template>查询
                </t-button>
              </t-space>
            </t-form-item>
          </t-form>
        </query-form-right>
      </query-form>
      <t-table
        :data="dataList"
        :columns="columns"
        row-key="id"
        vertical-align="top"
        :hover="true"
        :loading="listLoading"
        :pagination="pagination"
        :selected-row-keys="selectRows"
        @page-change="onPageChange"
        @select-change="setSelectRows"
      >
        <template #type="{ row }">
          {{ tableEnumFormatter(row.dbName, enums.get('sys_enumType')) }}
        </template>
        <template #createTime="{ row }">
          {{ tableDateFormat(row.createTime) }}
        </template>
        <template #modifyTime="{ row }">
          {{ tableDateFormat(row.modifyTime) }}
        </template>
        <template #operation="{ row }">
          <t-button theme="primary" variant="text" shape="round" @click="handleEdit(row)">编辑</t-button>
          <t-button theme="danger" variant="text" shape="round" @click="handleDelete(row)">删除</t-button>
        </template>
      </t-table>
      <edit-form ref="editRef" @fetch-data="fetchData" />
    </t-card>
  </div>
</template>

<script lang="ts">
export default {
  name: 'ListTable',
};
</script>
<script setup lang="ts">
import { ref, onMounted, onBeforeMount } from 'vue';
import { MessagePlugin, DialogPlugin } from 'tdesign-vue-next';
import { AddIcon, SearchIcon } from 'tdesign-icons-vue-next';
import { getList, doDelete } from '@/api/online/table';
import { tableDateFormat, tableEnumFormatter } from '@/utils/index';
import EditForm from './form.vue';
import { getEnumItemList } from '@/api/base/data/enumItem';
import QueryForm from '@/components/query-form/index.vue';
import QueryFormLeft from '@/components/query-form/components/query-form-left.vue';
import QueryFormRight from '@/components/query-form/components/query-form-right.vue';
import { EnumItem } from '@/types/enum';

const dataList = ref([]);
const columns = ref([
  { colKey: 'row-select', align: 'center', width: 50, type: 'multiple', fixed: 'left' },
  { title: '库名', align: 'center', width: 120, colKey: 'dbName', fixed: 'left' },
  { title: '编号', align: 'center', width: 120, colKey: 'code', fixed: 'left' },
  { title: '表名', align: 'center', width: 150, colKey: 'tableCode', fixed: 'left' },
  { title: '显示名称', align: 'center', width: 150, colKey: 'tableName', fixed: 'left' },
  { title: '创建时间', align: 'center', width: 250, colKey: 'createTime' },
  { title: '创建人', align: 'center', width: 150, colKey: 'createUserName' },
  { title: '修改时间', align: 'center', width: 200, colKey: 'modifyTime' },
  { title: '修改人', align: 'center', width: 200, colKey: 'modifyUserName' },
  { title: '排序号', align: 'center', width: 180, colKey: 'sortIndex' },
  { title: '操作', align: 'center', width: 200, colKey: 'operation', fixed: 'right' },
]);
const enums = ref<null | Map<string, Array<EnumItem>>>(new Map());

const editRef = ref(null);
const listLoading = ref(false);
const selectRows = ref([]);
const query = ref({
  searchValue: '',
});
const pagination = ref({
  pageSize: 20,
  total: 0,
  current: 1,
});

onBeforeMount(() => {
  getEnums();
});

onMounted(() => {
  fetchData();
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
const setSelectRows = (val: any) => {
  selectRows.value = val;
};
const queryData = () => {
  pagination.value.current = 1;
  fetchData();
};
const handleEdit = (row: any) => {
  if (row.id) {
    editRef.value.showEdit(row.id);
  } else {
    editRef.value.showEdit();
  }
};
const handleDelete = (row: any) => {
  let ids = null;
  if (row.id) {
    ids = row.id;
  } else if (selectRows.value.length > 0) {
    ids = selectRows.value.join();
  } else {
    MessagePlugin.error('未选中任何行');
  }
  const confirmDia = DialogPlugin({
    header: '提醒',
    body: `是否确认删除？`,
    confirmBtn: '确定',
    onConfirm: async ({ e }) => {
      confirmDia.hide();
      const { message } = await doDelete({ ids });
      MessagePlugin.success(message);
      await fetchData();
    },
    onClose: ({ e, trigger }) => {
      confirmDia.hide();
    },
  });
};
const fetchData = async () => {
  listLoading.value = true;
  selectRows.value = [];
  const queryData = {
    ...query.value,
    ...pagination.value,
  };
  const {
    data: { list, total },
  } = await getList(queryData);
  dataList.value = list;
  pagination.value.total = total;
  listLoading.value = false;
};
const onPageChange = async (pageInfo: any) => {
  pagination.value.current = pageInfo.current;
  pagination.value.pageSize = pageInfo.pageSize;
  await fetchData();
};
</script>
