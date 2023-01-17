<template>
  <div class="sp-main-info">
    <t-row :gutter="10">
      <t-col :span="5">
        <t-card :bordered="false" :hover-shadow="false">
          <t-tabs v-model="selectTab" :placement="'top'" @change="changeTabs">
            <t-tab-panel :value="'ALIOSS'" label="阿里Oss" :destroy-on-hide="false">
              <aliOss ref="ali"></aliOss>
            </t-tab-panel>
            <t-tab-panel :value="'TENCENTCOS'" label="腾讯Cos" :destroy-on-hide="false">
              <template #panel>
                <tencentOss ref="tencent" />
              </template>
            </t-tab-panel>
            <t-tab-panel :value="'MINIO'" label="Minio" :destroy-on-hide="false">
              <template #panel>
                <minioOss ref="minio" />
              </template>
            </t-tab-panel>
          </t-tabs>
        </t-card>
      </t-col>
      <t-col :span="7">
        <t-card :bordered="false" :hover-shadow="false" :title="'(' + title + ')文件搜索'" headerBordered>
          <div style="padding-bottom: 10px">
            <t-row>
              <t-col :span="3"> <span style="color: red">查询最多50个文件</span> </t-col>
              <t-col :span="9">
                <t-row :gutter="10">
                  <t-col :flex="1" :span="6" :offset="4">
                    <t-input placeholder="请输入文件路径前缀" type="search" clearable v-model="params.prefix"></t-input>
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
              <t-button size="small" variant="outline" theme="primary" @click="download(row)" :disabled="row.size == 0"
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
import { ref, onMounted, computed, reactive, watch, nextTick } from 'vue';
import { MessagePlugin, DialogPlugin } from 'tdesign-vue-next';
import { listFiles, downloadFile, downloadFileLink } from '@/api/system/oss';
import aliOss from './ali.vue';
import tencentOss from './tencent.vue';
import minioOss from './minio.vue';
const selectTab = ref('ALIOSS');
const minio = ref(null);
const tencent = ref(null);
const ali = ref(null);
const title = ref('阿里Oss');
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
  if (value === 'ALIOSS') {
    title.value = '阿里Oss';
    ali.value.initData();
  } else if (value === 'TENCENTCOS') {
    title.value = '腾讯Cos';
    tencent.value.initData();
  } else if (value === 'MINIO') {
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
    let res = await listFiles({
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
      MessagePlugin.error('查询错误:' + res.msg);
    }
  } catch (error) {
    MessagePlugin.error('查询错误:' + error);
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

  let res = await downloadFileLink({
    type: selectTab.value,
    key: row.key,
  });
  if (res.code === 0) {
    let downloadElement = document.createElement('a');
    downloadElement.href = res.data;
    downloadElement.download = row.key; //下载后文件名
    downloadElement.target = '_blank';
    document.body.appendChild(downloadElement);
    downloadElement.click(); //点击下载
    document.body.removeChild(downloadElement); //下载完成移除元素
  }
};
//vue的api
onMounted(async () => {});
</script>

<style lang="less" scoped>
@import '@/style/variables';
</style>
