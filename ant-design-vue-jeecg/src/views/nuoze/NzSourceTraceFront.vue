<template>
  <div>
    <a-timeline>
      <div v-if="resultList.length > 0">
        <div style="font-size: 20px">产品:{{ sourceIds_dictText }} 批次:{{ sourceBatchIds_dictText }}</div>
        <a-divider />
      </div>
      <div v-else>
        <div><h1>暂无溯源信息</h1></div>
      </div>
      <a-timeline-item v-for="item in resultList">
        {{ item.createTime }} {{ item.createBy_dictText }} {{ item.node_dictText }}</a-timeline-item
      >
    </a-timeline>
    <a-divider />
    <a-timeline v-if="type==='2'">
      原药材溯源
      <div v-for="(value, key) in resultMap" :key="key">
       
        <a-collapse  :bordered="false">
          <template #expandIcon="props">
            <a-icon type="caret-right" :rotate="props.isActive ? 90 : 0" />
          </template>
          <a-collapse-panel :key="key" :header="key" :style="customStyle">
            <a-timeline-item v-for="item in value">
              {{ item.createTime }} {{ item.createBy_dictText }} {{ item.node_dictText }}
            </a-timeline-item>
          </a-collapse-panel>
        </a-collapse>

        <div><span>&nbsp;</span></div>
      </div>
    </a-timeline>
  </div>
</template>

<script>
import '@/assets/less/TableExpand.less'
import NzSourceTraceModal from './modules/NzSourceTraceModal'
import { getAction } from '@/api/manage'

export default {
  name: 'NzSourceTraceFront',
  components: {
    NzSourceTraceModal,
    getAction,
  },
  data() {
    return {
      description: '溯源页面',

      url: {
        list: '/trace/sourceBatch',
        listSrcTraceByPrdBatch: '/trace/prdSourcetrace',
      },
      dictOptions: {},
      resultList: {},
      optnum: '',
      sourceIds_dictText: '',
      sourceBatchIds_dictText: '',
      resultMap: {},
      type:'',
      customStyle:'background: #f7f7f7;border-radius: 4px;margin-bottom: 24px;border: 0;overflow: hidden',
    }
  },
  created() {
    this.listTrace()
  },
  computed: {},
  methods: {
    initDictConfig() {},
    listTrace() {
      //1原药材, 2产品
      this.type = this.$route.query.type
      //批次
      let batchid = this.$route.query.batchid

      let batchURL = this.url.list

      if (this.type != '1') {
        batchURL = '/trace/productBatch'
        this.listSrcTraceByPrdBatch(batchid)
      }

      let params = { batchid: batchid }
      getAction(batchURL, params).then((res) => {
        if (res.success && res.result.total > 0) {
          this.resultList = res.result.records
          // console.info(this.resultList[0]);
          this.optnum = this.resultList[0].optnum
          if (this.type == '1') {
            this.sourceIds_dictText = this.resultList[0].sourceIds_dictText
            this.sourceBatchIds_dictText = this.resultList[0].sourceBatchIds_dictText
          } else {
            this.sourceIds_dictText = this.resultList[0].productId_dictText
            this.sourceBatchIds_dictText = this.resultList[0].productBatchId_dictText
          }
        } else {
          this.optnum = ''
          this.sourceIds_dictText = ''
          this.sourceBatchIds_dictText = ''
        }
      })
    },
    listSrcTraceByPrdBatch(batchid) {
      let params = { batchid: batchid }
      let batchURL = this.url.listSrcTraceByPrdBatch
      getAction(batchURL, params).then((res) => {
        if (res.success) {
          //将分页中的records   转为map
          let map = {}
          let list = []
          res.result.records.forEach((row) => {
            list = map[row.sourceBatchIds_dictText]
            if (list) {
              list.push({
                createBy_dictText: row.createBy_dictText,
                node_dictText: row.node_dictText,
                createTime: row.createTime,
              })
              // map[row.sourceBatchIds_dictText].push([{'createBy_dictText':row.createBy_dictText,'node_dictText':row.node_dictText,'createTime':row.createTime}])
            } else {
              list = [
                {
                  createBy_dictText: row.createBy_dictText,
                  node_dictText: row.node_dictText,
                  createTime: row.createTime,
                },
              ]
            }
            map[row.sourceBatchIds_dictText] = list
            //map[row.id] = {name: row.name, content: row.content}
          })
          this.resultMap = map
        }
      })
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>