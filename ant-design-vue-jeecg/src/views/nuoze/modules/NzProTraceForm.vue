<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
         <a-col :span="12">
            <a-form-item label="工艺" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['node',{rules: [{ required: true, message: '请选择工艺'}]}]" :trigger-change="true" dictCode="cplsygc" placeholder="请选择工艺"/>
            </a-form-item>
          </a-col>
         <a-col :span="12">
            <a-form-item label="流水号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['optnum',{rules: [{ required: true, message: '请输入流水号'}]}]" placeholder="按批次规则填写流水号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-select-user-by-dep v-decorator="['createBy',{rules: [{ required: true, message: '请输入负责人'}]}]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="操作日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择操作日期" v-decorator="['createTime',{rules: [{ required: true, message: '请选择时间'}]}]" :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%"/>
            </a-form-item>
          </a-col>
           <a-col :span="12">
            <a-form-item label="产品" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-search-select-tag v-decorator="['productId',{rules: [{ required: true, message: '请选择产品'}]}]" dict="nz_product,name,id" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="产品批次" :labelCol="labelCol" :wrapperCol="wrapperCol">
             <j-search-select-tag v-decorator="['productBatchId',{rules: [{ required: true, message: '请选择产品批次'}]}]" placeholder="请选择产品批次" dict="nz_prod_batch,name,id" />
            </a-form-item>
          </a-col>
         
         
          <a-col :span="24">
            <a-form-item label="图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-image-upload isMultiple v-decorator="['pic']"></j-image-upload>
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
  import JDate from '@/components/jeecg/JDate'  
  import JImageUpload from '@/components/jeecg/JImageUpload'
  import JSelectUserByDep from '@/components/jeecgbiz/JSelectUserByDep'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'

  export default {
    name: 'NzProTraceForm',
    components: {
      JFormContainer,
      JDate,
      JImageUpload,
      JSelectUserByDep,
      JDictSelectTag,
      JSearchSelectTag,
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
        },
        url: {
          add: "/nz/nzProTrace/add",
          edit: "/nz/nzProTrace/edit",
          queryById: "/nz/nzProTrace/queryById"
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
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'optnum','createBy','createTime','productBatchId','productId','node','pic'))
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
            console.log("表单提交数据",formData)
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
        this.form.setFieldsValue(pick(row,'optnum','createBy','createTime','productBatchId','productId','node','pic'))
      },
    }
  }
</script>