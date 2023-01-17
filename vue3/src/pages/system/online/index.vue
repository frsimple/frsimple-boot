<template>
  <div>
    <div class="sp-role-left">
      <t-card :bordered="false">
        <div class="sp-role-left-header">
          <t-row :gutter="10" justify="end">
            <t-col :span="2" :offset="8">
              <t-input
                v-model="params.name"
                placeholder="请输入登录账号/昵称"
                type="search"
                clearable
                @clear="clearName"
                @enter="firstFetch"
              ></t-input>
            </t-col>
            <t-col :span="1">
              <t-button theme="default" variant="outline" @click="firstFetch">查询</t-button>
            </t-col>
          </t-row>
        </div>
        <t-table
          row-key="username"
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
            <t-button size="small" variant="outline" theme="danger" @click="userOut(row)">踢他下线</t-button>
          </template>
        </t-table>
      </t-card>
    </div>
  </div>
</template>

<script lang="ts">
export default {
  name: 'ListBase',
};
</script>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue';
import { MessagePlugin, DialogPlugin } from 'tdesign-vue-next';
import { tokenList, userLogout } from '@/api/system/auth';

const firstFetch = async () => {
  pagination.value.current = 1;
  await fetchData();
};

// 左侧角色菜单列表数据start
const data = ref([]);
const columns = [
  {
    width: 120,
    colKey: 'userName',
    title: '登录用户',
    ellipsis: true,
  },
  {
    width: 120,
    colKey: 'nickName',
    title: '用户昵称',
    ellipsis: true,
  },
  {
    width: 120,
    colKey: 'device',
    title: '登录设备',
    ellipsis: true,
  },
  {
    width: 120,
    colKey: 'loginDate',
    title: '登录时间',
    align: 'center',
  },
  {
    width: 100,
    colKey: 'timeout',
    title: '有效期(s)',
  },
  {
    colKey: 'operation',
    title: '操作',
    width: 100,
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
  name: '',
});
const onPageChange = async (pageInfo) => {
  pagination.value.current = pageInfo.current;
  pagination.value.pageSize = pageInfo.pageSize;
  await fetchData();
};
const clearName = async () => {
  pagination.value.current = 1;
  params.name = '';
  await fetchData();
};
const userOut = async (row) => {
  const confirmDia = DialogPlugin({
    header: '下线提醒',
    body: `是否踢(${row.nickName}-${row.userName})下线？`,
    confirmBtn: '确定',
    onConfirm: async ({ e }) => {
      confirmDia.hide();
      const { message } = await userLogout({
        userId: row.userId,
      });
      MessagePlugin.success(message);
      await fetchData();
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
  const {
    data: { list, total },
  } = await tokenList(queryData);
  data.value = list;
  pagination.value.total = total;
  dataLoading.value = false;
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
