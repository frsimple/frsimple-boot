<template>
  <div>
    <div class="sp-role-left">
      <t-card :bordered="false">
        <div class="sp-role-left-header">
          <t-row>
            <t-col :span="6">
              <t-button v-if="authAdd" @click="addRow">新增客户端</t-button>
            </t-col>
            <t-col :span="6">
              <t-row :gutter="10">
                <t-col :flex="1" :span="6" :offset="4">
                  <t-input
                    v-model="params.clientId"
                    placeholder="请输入客户端ID"
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
          row-key="clientId"
          :data="data"
          :max-height="'calc(98vh - 235px)'"
          :columns="columns"
          :table-layout="'fixed'"
          :pagination="pagination"
          :loading="dataLoading"
          @page-change="onPageChange"
        >
          <template #authorizedGrantTypes="{ row }">
            <t-tag
              v-for="item in row.authorizedGrantTypes.split(',')"
              :key="item"
              theme="primary"
              size="small"
              variant="light"
              :style="{ margin: '3px' }"
            >
              {{ authGrantTypes[item] }}
            </t-tag>
          </template>
          <template #scope="{ row }">
            <span style="font-size: 12px"> {{ row.scope.replaceAll(',', '｜') }}</span>
          </template>
          <template #accessTokenValidity="{ row }">
            {{ row.accessTokenValidity + 's' }}
          </template>
          <template #refreshTokenValidity="{ row }">
            {{ row.refreshTokenValidity + 's' }}
          </template>
          <template #resourceIds="{ row }">
            <t-tag v-for="item in row.resourceIds.split(',')" :key="item" theme="primary" size="small" variant="light">
              <template #icon>
                <t-icon name="laptop" />
              </template>
              {{ item }}
            </t-tag>
          </template>
          <template #clientId="{ row }">
            {{ row.clientId }}
          </template>
          <template #operation="{ row }">
            <t-button v-if="authEdit" size="small" variant="outline" theme="default" @click="editRow(row)"
              >修改</t-button
            >
            <t-button v-if="authDel" size="small" variant="outline" theme="danger" @click="delRow(row)">删除</t-button>
          </template>
        </t-table>
      </t-card>
    </div>
    <!-- 新增/修改角色 -->
    <t-dialog
      v-model:visible="visibleModal"
      width="900"
      :close-on-overlay-click="false"
      :header="opt === 'add' ? '新增客户端用户' : '修改客户端用户'"
      mode="modal"
      draggable
      :confirm-btn="saveBtn"
      :on-confirm="onSubmit"
    >
      <template #body>
        <t-form ref="form" :label-align="'top'" :data="clientForm" :layout="'inline'" :rules="rules">
          <t-form-item label="客户端用户ID" name="clientId">
            <t-input
              v-model="clientForm.clientId"
              :disabled="opt === 'edit'"
              :style="{ width: '400px' }"
              placeholder="请输入客户端用户ID"
            ></t-input>
          </t-form-item>
          <t-form-item label="客户端用户密码" name="clientSecret">
            <t-input
              v-model="clientForm.clientSecret"
              :style="{ width: '400px' }"
              placeholder="请输入客户端用户密码"
            ></t-input>
          </t-form-item>
          <t-form-item label="资源服务" name="resourceIds" help="微服务的application.name，输入完成需要按下回车确认">
            <t-tag-input
              v-model="clientForm.resourceIds"
              :style="{ width: '400px' }"
              clearable
              placeholder="请输入资源服务"
              @change="rChage"
            ></t-tag-input>
          </t-form-item>
          <t-form-item label="授权范围" name="scope" help="一般默认为all，输入完成需要按下回车确认">
            <t-tag-input
              v-model="clientForm.scope"
              clearable
              :style="{ width: '400px' }"
              placeholder="请输入授权范围"
              @change="sChage"
            ></t-tag-input>
          </t-form-item>
          <t-form-item label="权限类型" name="authorizedGrantTypes">
            <t-select
              v-model="clientForm.authorizedGrantTypes"
              :style="{ width: '820px' }"
              placeholder="请选择权限类型"
              multiple
              :options="authGrantList"
            ></t-select>
          </t-form-item>
          <t-form-item label="授权回调地址" name="webServerRedirectUri">
            <t-input
              v-model="clientForm.webServerRedirectUri"
              :style="{ width: '400px' }"
              placeholder="请输入授权回调地址"
            ></t-input>
          </t-form-item>
          <t-form-item label="token有效时长(s)" name="accessTokenValidity">
            <t-input
              v-model="clientForm.accessTokenValidity"
              :style="{ width: '400px' }"
              type="number"
              placeholder="请输入token有效时长"
            ></t-input>
          </t-form-item>
          <t-form-item label="刷新token有效时长(s)" name="refreshTokenValidity">
            <t-input
              v-model="clientForm.refreshTokenValidity"
              :style="{ width: '400px' }"
              type="number"
              placeholder="请输入刷新token有效时长"
            ></t-input>
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
import { oauthList, addClient, editClient, delClient, getClient } from '@/api/system/oauth';
import { dicVals } from '@/api/common';
import { useUserStore } from '@/store';
// 权限控制
const userStore = useUserStore();
const authAdd = computed(() => userStore.roles.includes('system:oauth:add'));
const authEdit = computed(() => userStore.roles.includes('system:oauth:edit'));
const authDel = computed(() => userStore.roles.includes('system:oauth:del'));

const firstFetch = async () => {
  pagination.value.current = 1;
  await fetchData();
};

const rChage = async (val, context) => {
  console.log(val, context);
  clientForm.resourceIds = val;
};
const sChage = async (val, context) => {
  console.log(val, context);
  clientForm.scope = val;
};

// 字典初始化start
const authGrantTypes = ref({});
const authGrantList = ref([]);
const initDicts = async () => {
  const res = await dicVals('SP_AUTHTYPE');
  authGrantList.value = res.data;
  res.data.forEach((row) => {
    authGrantTypes.value[row.value] = row.label;
  });
};
// 字典初始化end

// 新增/修改弹窗start
const visibleModal = ref(false);
const clientForm = reactive({
  clientId: '',
  resourceIds: [],
  clientSecret: '',
  scope: [],
  authorizedGrantTypes: [],
  webServerRedirectUri: '',
  accessTokenValidity: '',
  refreshTokenValidity: '',
});
const form = ref(null);
const saveBtn = reactive({
  content: '保存',
  loading: false,
});
const idValidator = async (val) => {
  if (opt.value === 'add' && val) {
    const res = await getClient(val);
    if (res.data) {
      return { result: false, message: '客户端用户ID重复', type: 'error' };
    }
  }
  return { result: true, type: 'success' };
};
const rules = {
  clientId: [{ required: true, message: '请输入客户端用户ID', type: 'error' }, { validator: idValidator }],
  resourceIds: [{ required: true, message: '请输入授权资源服务', type: 'error' }],
  clientSecret: [{ required: true, message: '请输入客户端密码', type: 'error' }],
  scope: [{ required: true, message: '请输入授权范围', type: 'error' }],
  authorizedGrantTypes: [{ required: true, message: '请选择权限类型', type: 'error' }],
  webServerRedirectUri: [
    { required: true, message: '请输入授权成功跳转页面', type: 'error' },
    { url: { protocols: ['http', 'https', 'ftp'] }, message: '请输入正确的地址' },
  ],
  accessTokenValidity: [{ required: true, message: '请输入token有效时长(s)', type: 'error' }],
  refreshTokenValidity: [{ required: true, message: '请输入刷新token有效时长(s)', type: 'error' }],
};
const onSubmit = async () => {
  const result = await form.value.validate();
  if (typeof result !== 'object' && result) {
    saveBtn.content = '保存中...';
    saveBtn.loading = true;
    const submitForm = {
      resourceIds: clientForm.resourceIds.join(','),
      clientSecret: clientForm.clientSecret,
      scope: clientForm.scope.join(','),
      authorizedGrantTypes: clientForm.authorizedGrantTypes.join(','),
      webServerRedirectUri: clientForm.webServerRedirectUri,
      accessTokenValidity: clientForm.accessTokenValidity,
      refreshTokenValidity: clientForm.refreshTokenValidity,
      clientId: null,
    };
    if (opt.value === 'add') {
      submitForm.clientId = clientForm.clientId;
      try {
        const result1 = await addClient(submitForm);
        if (result1.code === 0) {
          visibleModal.value = false;
          await fetchData();
          MessagePlugin.success('保存成功');
        } else {
          MessagePlugin.error(`保存失败：${result1.msg}`);
        }
      } catch (error) {
        MessagePlugin.error('保存失败');
      } finally {
        saveBtn.content = '保存';
        saveBtn.loading = false;
      }
    } else {
      submitForm.clientId = clientForm.clientId;
      try {
        const result1 = await editClient(submitForm);
        if (result1.code === 0) {
          visibleModal.value = false;
          fetchData();
          MessagePlugin.success('保存成功');
        } else {
          MessagePlugin.error(`保存失败：${result1.msg}`);
        }
      } catch (error) {
        MessagePlugin.error('保存失败');
      } finally {
        saveBtn.content = '保存';
        saveBtn.loading = false;
      }
    }
  }
};
// 新增/修改弹窗end

// 左侧角色菜单列表数据start
const data = ref([]);
const columns = [
  {
    width: 140,
    colKey: 'clientId',
    title: '客户端用户ID',
  },
  {
    width: 160,
    colKey: 'resourceIds',
    title: '资源服务',
  },
  {
    width: 180,
    colKey: 'scope',
    title: '授权范围',
  },
  {
    width: 180,
    colKey: 'authorizedGrantTypes',
    title: '权限类型',
  },
  {
    width: 140,
    colKey: 'accessTokenValidity',
    title: 'token时长',
    ellipsis: true,
  },
  // {
  //   width: 120,
  //   colKey: 'refreshTokenValidity',
  //   title: '刷新token时长',
  //   ellipsis: true,
  // },
  {
    width: 200,
    colKey: 'createTime',
    title: '更新时间',
  },
  {
    colKey: 'operation',
    title: '操作',
    width: 200,
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
  clientId: '',
});
const onPageChange = async (pageInfo) => {
  pagination.value.current = pageInfo.current;
  pagination.value.pageSize = pageInfo.pageSize;
  await fetchData();
};
const clearName = async () => {
  params.clientId = '';
  pagination.value.current = 1;
  await fetchData();
};
const opt = ref('add');
const addRow = async () => {
  opt.value = 'add';
  form.value.reset();
  clientForm.clientId = '';
  visibleModal.value = true;
};
const editRow = async (row) => {
  opt.value = 'edit';
  form.value.reset();
  clientForm.clientId = row.clientId;
  clientForm.resourceIds = row.resourceIds.split(',');
  clientForm.clientSecret = row.clientSecret;
  clientForm.scope = row.scope.split(',');
  clientForm.authorizedGrantTypes = row.authorizedGrantTypes.split(',');
  clientForm.webServerRedirectUri = row.webServerRedirectUri;
  clientForm.accessTokenValidity = row.accessTokenValidity;
  clientForm.refreshTokenValidity = row.refreshTokenValidity;
  visibleModal.value = true;
};
const delRow = async (row) => {
  const confirmDia = DialogPlugin({
    header: '提醒',
    body: `是否确认删除(${row.clientId})客户端？`,
    confirmBtn: '继续删除',
    // cancelBtn: '在考虑下',
    onConfirm: ({ e }) => {
      confirmDia.hide();
      delClient({
        clientId: row.clientId,
      })
        .then((res) => {
          if (res.code === 0) {
            fetchData();
            MessagePlugin.success('删除成功');
          } else {
            MessagePlugin.error(`删除失败：${res.msg}`);
          }
        })
        .catch((error) => {
          MessagePlugin.error('删除失败');
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
  try {
    const res = await oauthList({
      size: pagination.value.pageSize,
      current: pagination.value.current,
      clientId: params.clientId,
    });
    if (res.code === 0) {
      data.value = res.data.records;
      pagination.value.total = res.data.total;
    }
  } catch (er) {
  } finally {
    dataLoading.value = false;
  }
};
// 左侧角色菜单列表数据end

// vue的api
onMounted(async () => {
  await initDicts();
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
