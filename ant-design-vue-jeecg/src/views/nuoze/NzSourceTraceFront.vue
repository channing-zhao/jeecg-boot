<template>
  
  <a-timeline>
    
    <div style="font-size: 20px;">产品:{{sourceIds_dictText}} 批次:{{sourceBatchIds_dictText}} 流水号:{{optnum}}</div>
    <div><h1>----------------------------</h1></div>
    <a-timeline-item v-for="item in resultList"> {{item.createBy_dictText}} 于 {{item.createTime}}  {{item.node_dictText}}</a-timeline-item>

  </a-timeline>
</template>



<script>

  import '@/assets/less/TableExpand.less'
  import NzSourceTraceModal from './modules/NzSourceTraceModal'
  import {  getAction } from '@/api/manage'
  export default {
    name: 'NzSourceTraceFront',
   
    components: {
      NzSourceTraceModal,
      getAction
    },
    data () {
      return {
        description: '溯源页面',
       
        url: {
          list: "/trace/sourceBatch"
        },
        dictOptions:{},
        resultList:{},
        optnum:'',
        sourceIds_dictText:'',
        sourceBatchIds_dictText:''
      }
    },
    created() {
      //1原药材, 2产品 
      let type = this.$route.query.type;
      //批次
      let batchid = this.$route.query.batchid;
      
      let batchURL = this.url.list;
      
      if(type != '1'){
        batchURL = "/trace/productBatch";
      }
      
      let params = {batchid:batchid};
      getAction(batchURL,params).then((res)=>{
        console.info(res.result);
        if(res.success){
          this.resultList = res.result.records;
          //console.info(this.resultList[0].optnum);
          this.optnum = this.resultList[0].optnum;
          this.sourceIds_dictText= this.resultList[0].sourceIds_dictText;
          this.sourceBatchIds_dictText= this.resultList[0].sourceBatchIds_dictText;
        }
      });
    },
    computed: {
      
    },
    methods: {
      initDictConfig(){
      }

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';

</style>