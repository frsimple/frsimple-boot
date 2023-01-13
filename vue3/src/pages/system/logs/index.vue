<template>
  <div>
    <div class="sp-role-left">
      <t-card :bordered="false">
        <div class="sp-role-left-header">
          <t-row :gutter="10" justify="end">
            <t-col :span="2" :offset="2">
              <t-select
                v-model="params.status"
                :options="[
                  { label: '成功', value: '0' },
                  { label: '失败', value: '1' },
                ]"
                clearable
                placeholder="请选择请求状态"
                @change="statusChange"
                @clear="clearName(3)"
              />
            </t-col>
            <t-col :span="2">
              <t-input
                v-model="params.username"
                placeholder="请输入登录账号"
                type="search"
                clearable
                @clear="clearName(0)"
                @enter="firstFetch"
              ></t-input>
            </t-col>
            <t-col :span="2">
              <t-input
                v-model="params.nickName"
                placeholder="请输入用户昵称"
                type="search"
                clearable
                @clear="clearName(1)"
                @enter="firstFetch"
              ></t-input>
            </t-col>
            <t-col :span="2">
              <t-input
                v-model="params.path"
                placeholder="请输入接口路径"
                type="search"
                clearable
                @clear="clearName(2)"
                @enter="firstFetch"
              ></t-input>
            </t-col>
            <t-col :span="1">
              <t-button theme="default" variant="outline" @click="firstFetch">查询</t-button>
            </t-col>
          </t-row>
        </div>
        <t-table
          row-key="createtime"
          :data="data"
          :max-height="tableHeight"
          :columns="columns"
          :pagination="pagination"
          :loading="dataLoading"
          :loading-props="{ size: '23px', text: '加载中...' }"
          @page-change="onPageChange"
        >
          <template #time="{ row }">
            {{ row.time ? row.time + 'ms' : '' }}
          </template>
          <template #status="{ row }">
            <div v-if="row.status == '0'">
              <check-circle-filled-icon style="color: green" /><span style="vertical-align: text-top">成功</span>
            </div>
            <div v-if="row.status == '1'">
              <error-circle-filled-icon style="color: red" /><span style="vertical-align: text-top"
                >失败
                <t-tooltip :content="row.error"
                  ><t-tag shape="round" theme="danger" size="small" variant="light">error</t-tag></t-tooltip
                ></span
              >
            </div>
          </template>
          <template #ip="{ row }">
            <t-tooltip :content="row.useragent"
              ><t-tag shape="round" theme="primary" size="small" variant="light">useragent</t-tag></t-tooltip
            >
            {{ row.ip }}
          </template>
          <template #username="{ row }">
            {{ row.username + '/' + row.nickName }}
          </template>
          <template #servicename="{ row }">
            {{ row.servicename }}<br />
            <t-tooltip v-if="row.params" :content="decodeURI(row.params)"
              ><t-tag shape="round" theme="primary" size="small" variant="light">params</t-tag></t-tooltip
            >{{ row.path }}<br />
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
import { ref, onMounted, reactive, getCurrentInstance } from 'vue';
import { CheckCircleFilledIcon, ErrorCircleFilledIcon } from 'tdesign-icons-vue-next';
import { logsList } from '@/api/system/logs';
// 获取table全局高度
const tableHeight = getCurrentInstance().appContext.config.globalProperties.$tableHeight;

const firstFetch = async () => {
  pagination.value.current = 1;
  await fetchData();
};

// 左侧角色菜单列表数据start
const data = ref([]);
const columns = [
  {
    width: 100,
    colKey: 'status',
    title: '状态',
    ellipsis: true,
  },
  {
    width: 120,
    colKey: 'username',
    title: '登录用户',
    ellipsis: true,
  },
  {
    width: 160,
    colKey: 'servicename',
    title: '接口信息',
    ellipsis: true,
  },
  {
    width: 140,
    colKey: 'ip',
    title: 'IP地址',
    align: 'center',
    ellipsis: true,
  },
  // {
  //   width: 300,
  //   colKey: 'useragent',
  //   title: '请求来源',
  //   ellipsis: true,
  // },
  {
    width: 150,
    colKey: 'createTime',
    title: '访问时间',
  },
  {
    width: 100,
    colKey: 'time',
    title: '耗时',
  },
];
const dataLoading = ref(false);
const pagination = ref({
  pageSize: 20,
  total: 0,
  current: 1,
});
const params = reactive({
  path: '',
  username: '',
  nickName: '',
  status: '',
});
const onPageChange = async (pageInfo) => {
  pagination.value.current = pageInfo.current;
  pagination.value.pageSize = pageInfo.pageSize;
  await fetchData();
};
const statusChange = async (value) => {
  params.status = value;
  pagination.value.current = 1;
  await fetchData();
};
const clearName = async (index) => {
  if (index === 0) {
    params.username = '';
  } else if (index === 1) {
    params.nickName = '';
  } else if (index === 2) {
    params.path = '';
  } else if (index === 3) {
    params.status = '';
    return;
  }
  pagination.value.current = 1;
  await fetchData();
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
  } = await logsList(queryData);
  data.value = list;
  pagination.value.total = total;
  dataLoading.value = false;
};
// 左侧角色菜单列表数据end

// vue的api
onMounted(async () => {
  await fetchData();
});
</script>

<style lang="less" scoped>
@import '@/style/variables';
.t-tag {
  cursor: pointer;
}
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
