<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
         <a-col :span="12">
            <a-form-item label="原药材" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list"  v-decorator="['sourceIds',{rules: [{ required: true, message: '请选择原药材'}]}]" :trigger-change="true" @change="onChangeBatch" dictCode="nz_source,name,id" placeholder="请选择原药材" />
            </a-form-item>
          </a-col>
         
          <a-col :span="12">
            <a-form-item label="原药材批次" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select v-decorator="['sourceBatchIds',{rules: [{ required: true, message: '请选择原药材'}]}]" mode='multiple'>
                <a-select-option v-for="d in templateDatas" :key="d.value" :value="d.value">{{d.text}}</a-select-option>
              </a-select>

            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['amount', validatorRules.amount]" placeholder="请输入数量" style="width: 70%"/>公斤
            </a-form-item>
          </a-col>
          <a-col :span="12">
             <a-form-item label="操作" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-radio-group name="type" v-decorator="[ 'type', {initialValue:1}]">
                <a-radio :value="1">入库</a-radio>
                <a-radio :value="0">出库</a-radio>
              </a-radio-group>
             </a-form-item>  
          </a-col>
           <a-col :span="12">
            <a-form-item label="流水号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['unit']" placeholder="请输入流水号"/>
            </a-form-item>
          </a-col>
           <a-col :span="12">
            <a-form-item label="运输方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['transport']" placeholder="请输入运输方式"/>
            </a-form-item>
          </a-col>
           <a-col :span="12">
            <a-form-item label="负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-select-user-by-dep v-decorator="['createBy']"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="操作日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择操作日期" v-decorator="['createTime']" :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JMultiSelectTag from "@/components/dict/JMultiSelectTag"
  import JDate from '@/components/jeecg/JDate'  
  import JSelectUserByDep from '@/components/jeecgbiz/JSelectUserByDep'

  export default {
    name: 'NzSrcWarehouseInForm',
    
    components: {
      JFormContainer,
      JDictSelectTag,
      JMultiSelectTag,
      JDate,
      JSelectUserByDep,
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
        required: false
      },
      //表单模式：true流程表单 false普通表单
      formBpm: {
        type: Boolean,
        default: false,
        required: false
      },
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        form: this.$form.createForm(this),
        //sourceids :[],
        templateDatas:[],
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
          amount: {
            rules: [
              { required: true,message:'请输入数字'},
              { pattern: /^-?\d+\.?\d*$/, message: '请输入数字!'},
            ]
          },
        },
        url: {
          add: "/nz/nzSrcWarehouseIn/add",
          edit: "/nz/nzSrcWarehouseIn/edit",
          queryById: "/nz/nzSrcWarehouseIn/queryById",
          querySourceList: "/nz/nzSourceBatch/querySourceListByBatchids",
          listBatch:"/nz/nzSourceBatch/list"
                    
        }
      }
    },
    computed: {
      formDisabled(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return false
          }
          return true
        }
        return this.disabled
      },
      showFlowSubmitButton(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return true
          }
        }
        return false
      }
    },
    created () {
      //如果是流程中表单，则需要加载流程表单data
      this.showFlowData();
    },
    methods: {
      //改变批次自动匹配原药材
      onChangeBatch(value){
       // console.info("change==>"+value)
        let params = {sourceId:value,pageSize:50};
          getAction(this.url.listBatch,params).then((res)=>{
            if(res.success){
              //先置空
              this.templateDatas = [];
               console.info(" ==>"+res.result.records);
             res.result.records.forEach((r)=>{
                this.templateDatas.push({
                  value:r.id,
                  text:r.name
                })
              })
              //console.info(this.templateDatas);

            }
          });
      },
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
       
        this.model = Object.assign({}, record);
         
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'sourceIds','unit','transport','createBy','createTime','sourceBatchIds','amount','type'))
        })
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          });
        }
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            let bids = formData.sourceBatchIds;
           
            formData.sourceBatchIds = bids.toString();
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'sourceIds','unit','transport','createBy','createTime','sourceBatchIds','amount','type'))
      },
    }
  }
</script>