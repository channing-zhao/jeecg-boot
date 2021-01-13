<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="5">
            <a-form-item label="原药材">
              <j-search-select-tag v-model="queryParam.sourceId" dict="nz_source,name,id" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="5">
            <a-form-item label="批次号">
              <j-search-select-tag v-model="queryParam.name" dict="nz_source_batch,name,name" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="5">
            <a-form-item label="基地">
              <j-search-select-tag v-model="queryParam.areaIds" dict="nz_base,name,id" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="5">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('原药材批次')">导出</a-button>
      <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{ x: true }"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无图片</span>
          <img
            v-else
            :src="getImgView(text)"
            height="25px"
            alt=""
            style="max-width: 80px; font-size: 12px; font-style: italic"
          />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <template slot="barPath" slot-scope="text, record, index">
          <div class="anty-img-wrap">
            <img style="width: 28px; height: 28px" :src="getAvatarView(record.barPath)"   @click="showModal(record.barPath)" />
          </div>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>

      <a-modal v-model="barcodeModalVisible" title="微信扫一扫,手机查看批次溯源信息" @ok="handleOk">
        <div style="vertical-align: middle;text-align: center;">
         <div  id="barcodeImg"> <img style="width: 256px; height: 256px" :src="getAvatarView(curbarPath)" /></div>
          <a-button type="primary" icon="download" size="large" @click="print" >打印</a-button>
        </div>
      </a-modal>
    </div>

    <nz-source-batch-modal ref="modalForm" @ok="modalFormOk"></nz-source-batch-modal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import NzSourceBatchModal from './modules/NzSourceBatchModal'
import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'
import { putAction, getFileAccessHttpUrl } from '@/api/manage'
import JSearchSelectTag from '@/components/dict/JSearchSelectTag'

export default {
  name: 'NzSourceBatchList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    JSearchSelectTag,
    NzSourceBatchModal,
  },
  data() {
    return {
      curbarPath:'',
      barcodeModalVisible:false,
      description: '原药材批次管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },

        {
          title: '批次号',
          align: 'center',
          dataIndex: 'name',
        },
        {
          title: '原药材',
          align: 'center',
          dataIndex: 'sourceId_dictText',
        },
        {
          title: '种植基地',
          align: 'center',
          dataIndex: 'areaIds_dictText',
        },
        {
          title: '二维码',
          dataIndex: 'barPath',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'barPath' },
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/nz/nzSourceBatch/list',
        delete: '/nz/nzSourceBatch/delete',
        deleteBatch: '/nz/nzSourceBatch/deleteBatch',
        exportXlsUrl: '/nz/nzSourceBatch/exportXls',
        importExcelUrl: 'nz/nzSourceBatch/importExcel',
      },
      dictOptions: {},
    }
  },
  created() {},
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    initDictConfig() {},
    print(){
      let printdiv = document.getElementById("barcodeImg");
      console.info(printdiv);
      let newContent = printdiv.innerHTML;
      let oldContent = document.body.innerHTML;
      console.info(oldContent);
      document.body.innerHTML = newContent;

      window.print();
      window.location.reload();
      document.body.innerHTML = oldContent;

    },
    getAvatarView: function (avatar) {
      
      return getFileAccessHttpUrl(avatar)
    },
    showModal(avatar) {
      this.curbarPath = avatar
      this.barcodeModalVisible = true;
    },
    handleOk(e) {
      console.log(e);
      this.barcodeModalVisible = false;
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>