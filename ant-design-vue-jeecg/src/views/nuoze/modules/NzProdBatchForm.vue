<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-item label="批次号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['name',{rules: [{ required: true, message: '请输入批次号'}]}]" placeholder="请输入批次号"></a-input>
            </a-form-item>
          </a-col>
         <a-col :span="12">
            <a-form-item label="产品" :labelCol="labelCol" :wrapperCol="wrapperCol">
             <j-dict-select-tag type="list"  v-decorator="['productId',{rules: [{ required: true, message: '请选择产品'}]}]" :trigger-change="true" dictCode="nz_product,name,id" placeholder="请选择产品"/>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="原药材批次" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-multi-select-tag type="list_multi" v-decorator="['sourceBatchIds',{rules: [{ required: true, message: '请选择原药材批次'}]}]" :trigger-change="true" dictCode="nz_source_batch,name,id" placeholder="请选择原药材批次"/>
            </a-form-item>
          </a-col>

            <a-col :span="12">
            <a-form-item label="说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="['remark']" rows="4" placeholder="请输入说明"/>
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
  import JMultiSelectTag from "@/components/dict/JMultiSelectTag"

  export default {
    name: 'NzProdBatchForm',
    components: {
      JFormContainer,
      JMultiSelectTag,
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
          add: "/nz/nzProdBatch/add",
          edit: "/nz/nzProdBatch/edit",
          queryById: "/nz/nzProdBatch/queryById"
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
          this.form.setFieldsValue(pick(this.model,'name','productId','remark','sourceBatchIds'))
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
        this.form.setFieldsValue(pick(row,'name','productId','remark','sourceBatchIds'))
      },
    }
  }
</script>