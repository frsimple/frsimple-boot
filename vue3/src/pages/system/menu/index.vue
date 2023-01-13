<template>
  <div>
    <t-row :gutter="10">
      <t-col :span="2">
        <div class="sp-menu-tree">
          <t-card :bordered="false">
            <t-loading size="small" :loading="menuLoad" show-overlay>
              <t-tree
                ref="menuTree"
                :data="items"
                activable
                hover
                transition
                expand-all
                :expand-on-click-node="false"
                :default-actived="activeNode"
                @active="activeChange"
              >
                <template #label="{ node }">
                  <div class="sp-menu-list">
                    <t-icon
                      :name="node.data.icon"
                      :class="{ 'menu-unactive': !node.actived, 'menu-active': node.actived }"
                    />
                    <span
                      style="margin-left: 6px"
                      :class="{ 'menu-unactive': !node.actived, 'menu-active': node.actived }"
                      >{{ node.label }}</span
                    >
                  </div>
                </template>
              </t-tree>
            </t-loading>
          </t-card>
        </div>
      </t-col>
      <t-col :span="10">
        <t-row>
          <t-col :span="12">
            <t-card
              :title="selectNode.id ? '下级菜单(' + selectNode.name + ')' : '未选中菜单'"
              :bordered="false"
              :style="{ borderRadius: '8px' }"
            >
              <div class="sp-menu-query">
                <t-row>
                  <t-col :span="8">
                    <div style="text-align: left">
                      <t-button
                        v-if="authAdd"
                        theme="primary"
                        type="button"
                        :disabled="!selectNode.id || selectNode.component !== 'layout'"
                        @click="addMenu"
                      >
                        添加下级菜单
                      </t-button>
                    </div>
                  </t-col>
                  <t-col :span="3">
                    <div>
                      <t-row justify="center" :gutter="10">
                        <t-col :flex="1">
                          <t-input
                            v-model="params.name"
                            class="form-item-content"
                            type="search"
                            clearable
                            placeholder="请输入菜单名称"
                            :style="{ minWidth: '134px' }"
                            @clear="clearIpt"
                            @enter="firstFetch"
                          />
                        </t-col>
                      </t-row>
                    </div>
                  </t-col>
                  <t-col :span="1" class="operation-container">
                    <t-button
                      theme="default"
                      variant="outline"
                      type="submit"
                      :style="{ marginLeft: '8px' }"
                      :disabled="!selectNode.id"
                      @click="firstFetch"
                    >
                      查询
                    </t-button>
                  </t-col>
                </t-row>
              </div>
              <div class="sp-menu-content">
                <div class="table-container">
                  <t-table
                    row-key="id"
                    :data="data"
                    :expanded-row-keys="expandedRowKeys"
                    :expand-on-row-click="false"
                    :expand-icon="expandIcon"
                    :max-height="'calc(98vh - 340px)'"
                    :columns="columns"
                    :ellipsis="true"
                    :table-layout="tableLayout"
                    :pagination="pagination"
                    :loading="dataLoading"
                    :loading-props="{ size: '23px', text: '加载中...' }"
                    @page-change="onPageChange"
                    @expand-change="rehandleExpandChange"
                  >
                    <template #expandedRow="{ row }">
                      <div class="sp-submenu-context">
                        <t-table
                          row-key="id"
                          bordered
                          size="small"
                          :empty="'暂无权限数据'"
                          :data="row.subMenu"
                          :loading-props="{ size: '23px', text: '加载中...' }"
                          :columns="[
                            {
                              width: 120,
                              colKey: 'name',
                              title: '权限名称',
                              align: 'center',
                            },
                            {
                              width: 100,
                              colKey: 'authCode',
                              title: '权限编码',
                              align: 'center',
                            },
                            {
                              colKey: 'operation',
                              title: '操作',
                              width: 100,
                              cell: 'operation',
                              align: 'center',
                            },
                          ]"
                        >
                          <template #operation="{ row }">
                            <t-button
                              v-if="authEdit"
                              size="small"
                              variant="outline"
                              theme="default"
                              @click="editBtnMenu(row)"
                              >修改</t-button
                            >
                            <t-button
                              v-if="authDel"
                              size="small"
                              variant="outline"
                              theme="danger"
                              @click="delBtnMenu(row)"
                              >删除</t-button
                            >
                          </template>
                        </t-table>
                      </div>
                    </template>
                    <template #icon="{ row }">
                      <t-icon :name="row.meta.icon"></t-icon>
                    </template>
                    <template #operation="{ row }">
                      <t-button v-if="authEdit" size="small" variant="outline" theme="default" @click="editMenu(row)"
                        >修改</t-button
                      >
                      <t-button v-if="authDel" size="small" variant="outline" theme="danger" @click="delMenu(row)"
                        >删除</t-button
                      >
                      <t-button
                        v-if="row.component !== 'layout'"
                        size="small"
                        variant="outline"
                        theme="success"
                        @click="addBtnMenu(row)"
                        >添加权限</t-button
                      >
                    </template>
                  </t-table>
                </div>
              </div>
            </t-card>
          </t-col>
        </t-row>
      </t-col>
    </t-row>
    <!-- 新增/修改下级菜单窗口 -->
    <t-dialog
      v-model:visible="visibleModal"
      width="700"
      :close-on-overlay-click="false"
      :header="!menuForm.id ? '新增菜单信息-' + selectNode.name : '修改菜单信息-' + selectNode.name"
      mode="modal"
      draggable
      :confirm-btn="saveBtn"
      :on-confirm="onSubmit"
    >
      <template #body>
        <t-form ref="form" :label-align="'top'" :data="menuForm" :layout="'inline'" :rules="rules">
          <t-form-item label="菜单名称" name="name">
            <t-input v-model="menuForm.name" :style="{ width: '300px' }" placeholder="请输入菜单名称"></t-input>
          </t-form-item>
          <t-form-item label="菜单图标" name="icon">
            <!-- <t-input v-model="menuForm.icon" :style="{ width: '300px' }" placeholder="请输入菜单图标"></t-input> -->
            <t-select
              v-model="menuForm.icon"
              :style="{ width: '300px' }"
              :popup-props="{ overlayStyle: { width: '300px' } }"
              placeholder="请选择图标"
            >
              <t-option v-for="item in iconList" :key="item.stem" :value="item.stem" class="overlay-options">
                <div>
                  <t-icon :name="item.stem" />
                </div>
              </t-option>
              <template #valueDisplay>
                <t-icon :name="menuForm.icon" size="medium" :style="{ marginRight: '8px' }" />{{ menuForm.icon }}
              </template>
            </t-select>
          </t-form-item>
          <t-form-item label="访问路径" name="path">
            <t-input v-model="menuForm.path" :style="{ width: '300px' }" placeholder="请输入访问路径"></t-input>
          </t-form-item>
          <t-form-item label="排序" name="sort">
            <t-input
              v-model="menuForm.sort"
              type="number"
              :style="{ width: '300px' }"
              placeholder="请输入排序"
            ></t-input>
          </t-form-item>
          <t-form-item v-if="selectNode.id === '999999'" label="是否菜单" name="single" :label-width="80">
            <t-radio-group v-model="menuForm.single" :style="{ width: '300px' }">
              <t-radio value="1">是</t-radio>
              <t-radio value="0">否</t-radio>
            </t-radio-group>
          </t-form-item>
          <t-form-item v-if="selectNode.id !== '999999' || menuForm.single === '1'" label="组件路径" name="component">
            <t-input v-model="menuForm.component" :style="{ width: '300px' }" placeholder="请输入组件路径"></t-input>
          </t-form-item>
        </t-form>
      </template>
    </t-dialog>
    <!-- 新增/修改权限菜单窗口 -->
    <t-dialog
      v-model:visible="visibleModal1"
      width="700"
      :close-on-overlay-click="false"
      :header="!menuForm1.id ? '新增菜单信息-' + selectNode1.name : '修改菜单信息-' + selectNode1.name"
      mode="modal"
      draggable
      :confirm-btn="saveBtn1"
      :on-confirm="onSubmit1"
    >
      <template #body>
        <t-form ref="form1" :label-align="'top'" :data="menuForm1" :layout="'inline'" :rules="rules1">
          <t-form-item label="权限名称" name="name">
            <t-input v-model="menuForm1.name" :style="{ width: '300px' }" placeholder="请输入权限名称"></t-input>
          </t-form-item>
          <t-form-item label="权限编码" name="authCode">
            <t-input v-model="menuForm1.authCode" :style="{ width: '300px' }" placeholder="请输入权限编码"></t-input>
          </t-form-item>
        </t-form>
      </template>
    </t-dialog>
  </div>
</template>

<script setup lang="tsx">
import { onMounted, reactive, ref, computed } from 'vue';
import { manifest } from 'tdesign-icons-vue-next/lib/manifest';
import { MessagePlugin, DialogPlugin } from 'tdesign-vue-next';
import { ChevronRightCircleIcon } from 'tdesign-icons-vue-next';
import {
  getMenuList,
  getTreeMenuAll,
  addMenuInfo,
  editMenuInfo,
  delMenuInfo,
  getBtnList,
  addBtnMenuInfo,
  editBtnMenuInfo,
  delBtnMenuInfo,
} from '@/api/system/menu';
import { useUserStore } from '@/store';

// 权限控制
const userStore = useUserStore();
const authAdd = computed(() => userStore.roles.includes('center:menu:add'));
const authEdit = computed(() => userStore.roles.includes('center:menu:edit'));
const authDel = computed(() => userStore.roles.includes('center:menu:del'));

const iconList = ref(manifest);
// 菜单权限按钮部分-start
const visibleModal1 = ref(false);
const form1 = ref(null);
const saveBtn1 = reactive({
  content: '保存',
  loading: false,
});
const menuForm1 = reactive({
  id: '',
  name: '',
  authCode: '',
});
const selectNode1 = reactive({
  id: '',
  name: '',
});
const rules1 = {
  name: [{ required: true, message: '请输入权限名称', type: 'error' }],
  authCode: [{ required: true, message: '请输入权限编码', type: 'error' }],
};
const addBtnMenu = async (row) => {
  selectNode1.id = row.id;
  selectNode1.name = row.name;
  form1.value.reset();
  menuForm1.id = '';
  visibleModal1.value = true;
};
const editBtnMenu = async (row) => {
  data.value.forEach((menu) => {
    if (menu.id === row.parentid) {
      selectNode1.id = menu.id;
      selectNode1.name = menu.name;
    }
  });
  form1.value.reset();
  menuForm1.id = row.id;
  menuForm1.name = row.name;
  menuForm1.authCode = row.authCode;
  visibleModal1.value = true;
};
const delBtnMenu = async (row) => {
  data.value.forEach((menu) => {
    if (menu.id === row.parentid) {
      selectNode1.id = menu.id;
      selectNode1.name = menu.name;
    }
  });
  const confirmDia = DialogPlugin({
    header: '删除菜单权限提醒',
    body: '删除菜单权限之后，将关联的权限数据和角色权限数据都将进行删除，此操作不可逆，是否确定继续？',
    confirmBtn: '继续删除',
    // cancelBtn: '在考虑下',
    onConfirm: async ({ e }) => {
      confirmDia.hide();
      try {
        const { message } = await delBtnMenuInfo({ id: row.id });
        // 重新加载数据
        const submenu = await getBtnList({
          id: selectNode1.id,
        });
        data.value.filter(async (menu) => {
          if (menu.id === selectNode1.id) {
            menu.subMenu = submenu.data;
          }
          return menu;
        });
        MessagePlugin.success(message);
      } catch (e) {
        MessagePlugin.success(e.message);
      }
    },
    onClose: () => {
      confirmDia.hide();
    },
  });
};
const onSubmit1 = async () => {
  const result = await form1.value.validate();
  if (typeof result !== 'object' && result) {
    saveBtn1.content = '保存中...';
    saveBtn1.loading = true;
    const submitForm = {
      name: menuForm1.name,
      authCode: menuForm1.authCode,
      parentId: null,
      id: null,
    };
    if (!menuForm1.id) {
      submitForm.parentId = selectNode1.id;
      try {
        const { message } = await addBtnMenuInfo(submitForm);
        visibleModal1.value = false;
        // 重新加载数据
        const submenu = await getBtnList({
          id: selectNode1.id,
        });
        data.value.filter(async (menu) => {
          if (menu.id === selectNode1.id) {
            menu.subMenu = submenu.data;
          }
          return menu;
        });
        MessagePlugin.success(message);
      } catch (error) {
        MessagePlugin.error(error.message);
      } finally {
        saveBtn1.content = '保存';
        saveBtn1.loading = false;
      }
    } else {
      submitForm.id = menuForm1.id;
      try {
        const { message } = await editBtnMenuInfo(submitForm);
        visibleModal1.value = false;
        // 重新加载数据
        const submenu = await getBtnList({
          id: selectNode1.id,
        });
        data.value.filter(async (menu) => {
          if (menu.id === selectNode1.id) {
            menu.subMenu = submenu.data;
          }
          return menu;
        });
        MessagePlugin.success(message);
      } catch (error) {
        MessagePlugin.error('保存失败');
      } finally {
        saveBtn1.content = '保存';
        saveBtn1.loading = false;
      }
    }
  }
};
// 菜单权限按钮部分-end

const form = ref(null);
const visibleModal = ref(false);
const items = ref([]);
const dataLoading = ref(false);
const menuLoad = ref(false);
const menuTree = ref(null);
const activeNode = ref(['999999']);
const data = ref([]);
const expandedRowKeys = ref([]);
const menuForm = reactive({
  id: '',
  name: '',
  icon: '',
  path: '',
  single: '',
  component: '',
  sort: null,
});
const params = reactive({
  parentId: '',
  name: '',
  type: '',
});
const pagination = ref({
  pageSize: 20,
  total: 0,
  current: 1,
});
const selectNode = reactive({
  id: '',
  name: '',
  component: 'layout',
});
const tableLayout = ref('fixed');
const fixedTopAndBottomRows = ref(false);
const expandIcon = (h, { row }) => {
  // 第一行不显示展开图标
  if (row.component === 'layout' || row.component === 'link') return false;
  // 其他行，使用表格同款展开图标
  return <ChevronRightCircleIcon />;
};
// 新增/修改菜单信息
const saveBtn = reactive({
  content: '保存',
  loading: false,
});
const delMenu = async (row) => {
  const confirmDia = DialogPlugin({
    header: '删除菜单提醒',
    body: '删除菜单之后，将关联的权限数据和角色权限数据都将进行删除，此操作不可逆，是否确定继续？',
    confirmBtn: '继续删除',
    onConfirm: async () => {
      confirmDia.hide();
      try {
        const { message } = await delMenuInfo({ id: row.id });
        MessagePlugin.success(message);
        fetchData();
        fetchTreeData();
      } catch (e) {
        MessagePlugin.error(e.message);
      }
    },
    onClose: () => {
      confirmDia.hide();
    },
  });
};
const addMenu = async () => {
  form.value.reset();
  menuForm.id = '';
  visibleModal.value = true;
};
const editMenu = async (row) => {
  form.value.reset();
  menuForm.id = row.id;
  menuForm.name = row.name;
  menuForm.icon = row.meta.icon;
  menuForm.path = row.path;
  menuForm.component = row.component;
  menuForm.sort = row.sort;
  if (selectNode.id === '999999') {
    if (menuForm.component === 'layout') {
      menuForm.single = '0';
    } else {
      menuForm.single = '1';
    }
  }
  visibleModal.value = true;
};

const onSubmit = async () => {
  const result = await form.value.validate();
  if (typeof result !== 'object' && result) {
    saveBtn.content = '保存中...';
    saveBtn.loading = true;
    const submitForm = {
      name: menuForm.name,
      component: '',
      parentId: selectNode.id,
      path: menuForm.path,
      sort: menuForm.sort,
      meta: null,
      id: null,
    };
    submitForm.component = menuForm.component;
    const meta = {
      title: menuForm.name,
      icon: menuForm.icon,
      single: false,
    };
    if (submitForm.parentId === '999999') {
      if (menuForm.single === '0') {
        submitForm.component = 'layout';
        meta.single = false;
      } else {
        meta.single = true;
      }
    }
    submitForm.meta = meta;
    if (!menuForm.id) {
      try {
        const { message } = await addMenuInfo(submitForm);
        visibleModal.value = false;
        await fetchData();
        await fetchTreeData();
        MessagePlugin.success(message);
      } catch (error) {
        MessagePlugin.error('保存失败');
      } finally {
        saveBtn.content = '保存';
        saveBtn.loading = false;
      }
    } else {
      submitForm.id = menuForm.id;
      try {
        const { message } = await editMenuInfo(submitForm);
        visibleModal.value = false;
        await fetchData();
        await fetchTreeData();
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
const rules = {
  name: [{ required: true, message: '请输入菜单名称', type: 'error' }],
  path: [{ required: true, message: '请输入访问路径', type: 'error' }],
  component: [{ required: true, message: '请输入组件路径', type: 'error' }],
  single: [{ required: true, message: '请选择是否菜单', type: 'error' }],
  sort: [{ required: true, message: '请输入排序', type: 'error' }],
};
// 结束

const rehandleExpandChange = async (value, { expandedRowData }) => {
  console.log('rehandleExpandChange', value, expandedRowData);
  if (value.length > expandedRowKeys.value.length) {
    const pid = value[value.length - 1];
    const res = await getBtnList({
      id: pid,
    });
    expandedRowKeys.value = value;
    expandedRowData[value.length - 1].subMenu = res.data;
  } else {
    expandedRowKeys.value = value;
  }
};

const activeChange = async (value: any, context: { node: any }) => {
  expandedRowKeys.value = [];
  if (value.length === 0) {
    data.value = [];
    pagination.value = {
      pageSize: 20,
      total: 0,
      current: 1,
    };
    selectNode.id = '';
    selectNode.name = '';
    selectNode.component = 'layout';
  } else {
    selectNode.id = context.node.value;
    selectNode.name = context.node.label;
    selectNode.component = context.node.data.component;
    params.parentId = context.node.value;
    await fetchTreeData();
  }
};
const fetchData = async () => {
  menuLoad.value = true;
  try {
    const { data: list } = await getTreeMenuAll();
    items.value = list;
  } catch (e) {
    MessagePlugin.error(e.message);
  } finally {
    menuLoad.value = false;
  }
};

const onPageChange = async (pageInfo) => {
  pagination.value.current = pageInfo.current;
  pagination.value.pageSize = pageInfo.pageSize;
  await fetchTreeData();
};

const clearIpt = async () => {
  params.name = '';
  pagination.value.current = 1;
  await fetchTreeData();
};
const fetchTreeData = async () => {
  dataLoading.value = true;
  params.type = 'c';
  const queryData = {
    ...params,
    ...pagination.value,
  };
  try {
    const {
      data: { list, total },
    } = await getMenuList(queryData);
    data.value = list;
    pagination.value.total = total;
  } catch (er) {
    MessagePlugin.error(er.message);
  } finally {
    dataLoading.value = false;
  }
};

const editData = async (row) => {
  console.log(row);
};
onMounted(async () => {
  selectNode.id = '999999';
  selectNode.name = '全部';
  selectNode.component = 'layout';
  await fetchData();
  params.parentId = '999999';
  await fetchTreeData();
});
const filterByText = ref();
const filterText = ref();

const columns = [
  {
    width: 220,
    colKey: 'name',
    title: '名称',
  },
  {
    width: 100,
    colKey: 'icon',
    title: '图标',
  },
  {
    width: 100,
    colKey: 'path',
    title: '路径',
    ellipsis: true,
  },
  {
    width: 300,
    colKey: 'component',
    title: '组件',
  },
  {
    width: 100,
    colKey: 'sort',
    title: '排序',
  },
  {
    colKey: 'createTime',
    title: '创建时间',
    width: 200,
  },
  {
    colKey: 'operation',
    title: '操作',
    width: 300,
    cell: 'operation',
    fixed: 'right',
  },
];

const searchForm = {
  name: '',
  no: undefined,
  status: undefined,
  type: '',
};

const formData = ref({ ...searchForm });
const rowKey = 'index';
const verticalAlign = 'top';
const hover = true;
const expanded = ['0', '0-0', '0-1', '0-2', '0-3', '0-4'];
const onInput = () => {
  filterByText.value = (node) => {
    const rs = node.label.indexOf(filterText.value) >= 0;
    return rs;
  };
};

const firstFetch = async () => {
  if (selectNode.id) {
    pagination.value.current = 1;
    await fetchTreeData();
  }
};
</script>

<style lang="less" scoped>
@import '@/style/variables.less';

.menu-active {
  color: var(--td-brand-color) !important;
}
.menu-unactive {
  color: var(--tdvns-text-color-primary) !important;
}
.sp-submenu-context {
  padding-left: 130px;
  width: 700px;
}
.sp-menu-tree {
  //background-color: var(--td-gray-color-13);
  text-align: center;
  border-radius: var(--tdvns-border-radius);
  //padding: 10px;
}
.sp-menu-query {
  //background-color: var(--td-gray-color-13);
  border-radius: var(--tdvns-border-radius);
  height: 50px;
  //padding: 10px;
}
.sp-menu-list {
  display: flex;
  align-items: center;
}
.operation-container {
  text-align: right;
}
.sp-menu-content {
  //height: calc(98vh - 90px);
  //margin-top: 10px;
  //padding: 10px;
  padding-top: 10px;
  .table-container {
    border-radius: var(--tdvns-border-radius);
    background-color: #ffffff;
    //padding: 10px;
  }
}
.table-tree-container {
  background-color: var(--tdvns-bg-color-container);
  border-radius: var(--tdvns-border-radius);

  .t-tree {
    margin-top: 24px;
  }
}

.list-tree-wrapper {
  overflow-y: hidden;
}

.list-tree-operator {
  //width: 200px;
  float: left;
  padding: 10px 10px;
}

.list-tree-content {
  border-left: 1px solid var(--tdvns-border-level-1-color);
  overflow: auto;
}
</style>
<style>
.overlay-options {
  display: inline-block;
  font-size: 20px;
}
.t-select__popup-reference {
  align-items: center;
}
</style>
