<template>
  <div>
    <t-row :gutter="10">
      <t-col :span="8">
        <div class="sp-role-left">
          <t-card :bordered="false">
            <div class="sp-role-left-header">
              <t-row>
                <t-col :span="6">
                  <t-button v-if="authAdd" @click="addRow">新增角色</t-button>
                </t-col>
                <t-col :span="6">
                  <t-row :gutter="10">
                    <t-col :flex="1" :span="6" :offset="4">
                      <t-input
                        v-model="params.name"
                        placeholder="请输入角色名称"
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
              :loadingProps="{ size: '23px', text: '加载中...' }"
              @page-change="onPageChange"
            >
              <template #operation="{ row }">
                <t-button v-if="authEdit" size="small" variant="outline" theme="default" @click="editRow(row)"
                  >修改</t-button
                >
                <t-button v-if="authDel" size="small" variant="outline" theme="danger" @click="delRow(row)"
                  >删除</t-button
                >
                <t-button size="small" variant="outline" theme="success" @click="loadTree(row)">设置权限</t-button>
              </template>
            </t-table>
          </t-card>
        </div>
      </t-col>
      <t-col :span="4">
        <div>
          <t-card
            :bordered="false"
            :title="selectRole.name ? '角色(' + selectRole.name + ')' : '未选择角色'"
            header-bordered
          >
            <template #actions>
              <t-button
                v-if="authEdit"
                theme="primary"
                :disabled="!selectRole.name"
                :loading="saveLoad.load"
                @click="saveRole"
                >{{ saveLoad.text }}</t-button
              >
            </template>
            <t-loading size="small" :loading="menuLoad" show-overlay>
              <t-tree
                ref="menuTree"
                v-model="selectMenu"
                :data="items"
                activable
                hover
                transition
                expand-all
                :expand-on-click-node="false"
                :checkable="checkable"
                :check-strictly="checkStrictly"
                :value-mode="valueMode"
                :empty="'请选择并点击角色'"
              >
                <template #label="{ node }">
                  <div class="sp-menu-list">
                    <t-icon
                      v-if="node.data.icon"
                      :name="node.data.icon"
                      :class="{ 'menu-unactive': !node.checked, 'menu-active': node.checked }"
                    />
                    <span
                      style="margin-left: 6px"
                      :class="{
                        'menu-unactive': !node.checked,
                        'menu-active': node.checked,
                      }"
                      >{{ node.label }}</span
                    >
                  </div>
                </template>
              </t-tree>
            </t-loading>
          </t-card>
        </div>
      </t-col>
    </t-row>
    <!-- 新增/修改角色 -->
    <t-dialog
      v-model:visible="visibleModal"
      width="350"
      :close-on-overlay-click="false"
      :header="!roleForm.id ? '新增角色' : '修改角色'"
      mode="modal"
      draggable
      :confirm-btn="saveBtn"
      :on-confirm="onSubmit"
    >
      <template #body>
        <t-form ref="form" :label-align="'top'" :data="roleForm" :layout="'inline'" :rules="rules">
          <t-form-item label="角色名称" name="name">
            <t-input v-model="roleForm.name" :style="{ width: '300px' }" placeholder="请输入角色名称"></t-input>
          </t-form-item>
          <t-form-item label="角色描述" name="remark">
            <t-textarea v-model="roleForm.remark" :style="{ width: '300px' }" placeholder="请输入角色描述"></t-textarea>
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
import {
  roleList,
  addRoleInfo,
  editRoleInfo,
  delRoleInfo,
  roleTreeAll,
  roleTree,
  saveRoleMenu,
} from '@/api/system/role';
import { useUserStore } from '@/store';
// 权限控制
const userStore = useUserStore();
const authAdd = computed(() => userStore.roles.includes('system:role:add'));
const authEdit = computed(() => userStore.roles.includes('system:role:edit'));
const authDel = computed(() => userStore.roles.includes('system:role:del'));

const firstFetch = async () => {
  pagination.value.current = 1;
  await fetchRoleData();
};

// 右侧菜单树形start
const items = ref([]);
const menuLoad = ref(false);
const valueMode = ref('onlyLeaf');
const checkable = ref(true);
const checkStrictly = ref(false);
const selectMenu = ref([]);
const fetchData = async () => {
  menuLoad.value = true;
  try {
    const { data: list } = await roleTreeAll();
    items.value = list;
    const res1 = await roleTree({
      role: selectRole.id,
    });
    selectMenu.value = await removeParentNode(items.value, res1.data, []);
  } catch (e) {
    console.log(e);
  } finally {
    menuLoad.value = false;
  }
};
// 树形权限处理stat
const allMenu = reactive([]);
const removeParentNode = async (json, idArr, temp) => {
  for (let i = 0; i < json.length; i++) {
    const item = json[i];
    if (!allMenu.includes(item)) {
      allMenu.push(item);
    }
    if (item.children && item.children.length !== 0) {
      await removeParentNode(item.children, idArr, temp);
    } else if (idArr.includes(item.id)) {
      temp.push(item.id);
    }
  }
  return temp;
};
const getSlelectKeys = () => {
  let result = [];
  selectMenu.value.forEach((row) => {
    result.push(row);
  });
  selectMenu.value.forEach((val) => {
    result = forKeys(allMenu, val, result);
  });
  return result.join(',');
};
const forKeys = (list, value, temp) => {
  for (let i = 0; i < list.length; i++) {
    const item = list[i];
    if (item.id == value && item.parentId != '999999' && !temp.includes(item.parentId)) {
      temp.push(item.parentId);
      forKeys(list, item.parentId, temp);
    } else if (item.id == value && item.parentId != '999999' && !temp.includes(item.id)) {
      temp.push(item.id);
    }
  }
  return temp;
};
// 树形权限处理end
const saveLoad = reactive({
  load: false,
  text: '保存',
});
const saveRole = async () => {
  saveLoad.load = true;
  saveLoad.text = '保存中..';
  menuLoad.value = true;
  try {
    const { message } = await saveRoleMenu({
      role: selectRole.id,
      menu: getSlelectKeys(),
    });
    MessagePlugin.success(message);
  } catch (er) {
    MessagePlugin.error(er.message);
  } finally {
    menuLoad.value = false;
    saveLoad.load = false;
    saveLoad.text = '保存';
  }
};
// 右侧菜单属性end

// 新增/修改弹窗start
const visibleModal = ref(false);
const roleForm = reactive({
  id: '',
  name: '',
  remark: '',
});
const form = ref(null);
const saveBtn = reactive({
  content: '保存',
  loading: false,
});
const rules = {
  name: [{ required: true, message: '请输入角色名称', type: 'error' }],
  remark: [{ required: true, message: '请输入角色描述', type: 'error' }],
};
const onSubmit = async () => {
  const result = await form.value.validate();
  if (typeof result !== 'object' && result) {
    saveBtn.content = '保存中...';
    saveBtn.loading = true;
    const submitForm = {
      name: roleForm.name,
      remark: roleForm.remark,
      id: '',
    };
    if (!roleForm.id) {
      try {
        const { message } = await addRoleInfo(submitForm);
        visibleModal.value = false;
        fetchRoleData();
        MessagePlugin.success(message);
      } catch (error) {
        MessagePlugin.error('保存失败');
      } finally {
        saveBtn.content = '保存';
        saveBtn.loading = false;
      }
    } else {
      submitForm.id = roleForm.id;
      try {
        const { message } = await editRoleInfo(submitForm);
        visibleModal.value = false;
        fetchRoleData();
        MessagePlugin.success(message);
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
    width: 180,
    colKey: 'id',
    title: '角色ID',
    ellipsis: true,
  },
  {
    width: 200,
    colKey: 'name',
    title: '角色名称',
  },
  {
    width: 200,
    colKey: 'createTime',
    title: '创建时间',
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
  name: '',
});
const onPageChange = async (pageInfo) => {
  pagination.value.current = pageInfo.current;
  pagination.value.pageSize = pageInfo.pageSize;
  await fetchRoleData();
};
const clearName = async () => {
  params.name = '';
  pagination.value.current = 1;
  await fetchRoleData();
};
const selectRole = reactive({
  id: '',
  name: '',
});
const loadTree = async (row) => {
  selectRole.id = row.id;
  selectRole.name = row.name;
  await fetchData();
};
const addRow = async () => {
  form.value.reset();
  roleForm.id = '';
  visibleModal.value = true;
};
const editRow = async (row) => {
  form.value.reset();
  roleForm.id = row.id;
  roleForm.name = row.name;
  roleForm.remark = row.remark;
  visibleModal.value = true;
};
const delRow = async (row) => {
  const confirmDia = DialogPlugin({
    header: '提醒',
    body: '如果存在关联该角色的用户，请先取消关联在进行删除，删除角色将删除角色关联的所有权限数据，此操作不可逆，是否确认删除？',
    confirmBtn: '继续删除',
    // cancelBtn: '在考虑下',
    onConfirm: ({ e }) => {
      confirmDia.hide();
      delRoleInfo({
        id: row.id,
      })
        .then((res) => {
          fetchRoleData();
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

const fetchRoleData = async () => {
  data.value = [];
  dataLoading.value = true;
  const queryData = {
    ...params,
    ...pagination.value,
  };
  try {
    const {
      data: { list, total },
    } = await roleList(queryData);
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
  await fetchRoleData();
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
