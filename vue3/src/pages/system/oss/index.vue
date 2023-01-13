<template>
  <div class="sp-main-info">
    <t-row :gutter="10">
      <t-col :span="5">
        <t-card :bordered="false" :hover-shadow="false">
          <t-tabs v-model="selectTab" :placement="'top'" @change="changeTabs">
            <t-tab-panel :value="'MINIO'" label="Minio" :destroy-on-hide="false">
              <template #panel>
                <minioOss ref="minio" />
              </template>
            </t-tab-panel>
          </t-tabs>
        </t-card>
      </t-col>
      <t-col :span="7">
        <t-card :bordered="false" :hover-shadow="false" :title="'(' + title + ')文件搜索'" header-bordered>
          <div style="padding-bottom: 10px">
            <t-row>
              <t-col :span="3"> <span style="color: red">查询最多50个文件</span> </t-col>
              <t-col :span="9">
                <t-row :gutter="10">
                  <t-col :flex="1" :span="6" :offset="4">
                    <t-input v-model="params.prefix" placeholder="请输入文件路径前缀" type="search" clearable></t-input>
                  </t-col>
                  <t-col :flex="1" :span="1">
                    <t-button theme="default" variant="outline" @click="fetchData(0)">查询</t-button>
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
            :loading="dataLoading"
          >
            <template #size="{ row }">
              {{ row.size + 'b' }}
            </template>
            <template #operation="{ row }">
              <t-button size="small" variant="outline" theme="primary" :disabled="row.size == 0" @click="download(row)"
                >下载</t-button
              >
            </template>
          </t-table>
          <!-- <template #footer>
            <t-row :align="'right'" justify="center">
              <t-col :span="2">
                <t-button block variant="text"> 上一页 </t-button>
              </t-col>
              <t-col :span="2">
                <t-button block variant="text"> 下一页 </t-button>
              </t-col>
            </t-row>
          </template> -->
        </t-card>
      </t-col>
    </t-row>
  </div>
</template>

<script lang="ts">
export default {
  name: 'CenterUser',
};
</script>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { MessagePlugin } from 'tdesign-vue-next';
import { listFiles, downloadFileLink } from '@/api/system/oss';
import minioOss from './minio.vue';

const selectTab = ref('MINIO');
const minio = ref(null);
const title = ref('MINIO');
const dataLoading = ref(false);
const data = ref();
const columns = [
  {
    width: 340,
    colKey: 'key',
    title: '附件路径',
  },
  {
    width: 120,
    colKey: 'size',
    title: '附件大小',
  },
  {
    width: 230,
    colKey: 'updateDate',
    title: '修改时间',
  },
  {
    colKey: 'operation',
    title: '操作',
    width: 80,
    cell: 'operation',
    fixed: 'right',
  },
];
const params = reactive({
  prefix: '',
  nextmarker: '',
});
const changeTabs = async (value) => {
  if (value) {
    params.prefix = '';
    params.nextmarker = '';
    data.value = [];
  }
  if (value === 'MINIO') {
    title.value = 'Minio';
    minio.value.initData();
  }
};
const fetchData = async (index) => {
  if (index && index === 0) {
    params.nextmarker = '';
  }
  dataLoading.value = true;
  try {
    const res = await listFiles({
      type: selectTab.value,
      prefix: params.prefix,
      nextmarker: params.nextmarker,
    });
    if (res.code === 0) {
      if (res.data.nextmarker) {
        params.nextmarker = res.data.nextmarker;
      } else {
        params.nextmarker = '';
      }
      data.value = res.data.fileList;
    } else {
      MessagePlugin.error(`查询错误:${res.msg}`);
    }
  } catch (error) {
    MessagePlugin.error(`查询错误:${error}`);
  } finally {
    dataLoading.value = false;
  }
};
const download = async (row) => {
  // let res = await downloadFile({
  //   type: selectTab.value,
  //   key: row.key,
  // });
  // let blob = new Blob([res]);
  // let downloadElement = document.createElement('a');
  // let href = window.URL.createObjectURL(blob); //创建下载的链接
  // downloadElement.href = href;
  // downloadElement.download = row.key; //下载后文件名
  // document.body.appendChild(downloadElement);
  // downloadElement.click(); //点击下载
  // document.body.removeChild(downloadElement); //下载完成移除元素
  // window.URL.revokeObjectURL(href); //释放掉blob对象

  const res = await downloadFileLink({
    type: selectTab.value,
    key: row.key,
  });
  if (res.code === 0) {
    const downloadElement = document.createElement('a');
    downloadElement.href = res.data;
    downloadElement.download = row.key; // 下载后文件名
    // downloadElement.target = '#';
    document.body.appendChild(downloadElement);
    downloadElement.click(); // 点击下载
    document.body.removeChild(downloadElement); // 下载完成移除元素
  }
};
</script>

<style lang="less" scoped>
@import '@/style/variables';
</style>
