<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['name']" placeholder="请输入名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['code']" placeholder="请输入编号"></a-input>
            </a-form-item>
          </a-col>
         
          <a-col :span="12">
            <a-form-item label="图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-image-upload isMultiple v-decorator="['pic']"></j-image-upload>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="品牌" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['brand']" placeholder="请输入品牌"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['rank']" :trigger-change="true" dictCode="dengji" placeholder="请选择等级"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="有效期" :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <a-input v-decorator="['valid']" placeholder="请输入有效期" style="width: 50%"/>天
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="适合人群" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['people']" :trigger-change="true" dictCode="use_people" placeholder="请选择适合人群"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="含防腐剂" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="radio" v-decorator="['isFangfuji']" :trigger-change="true" dictCode="yn" placeholder="请选择含防腐剂"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="存储方法" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['storage']" placeholder="请输入存储方法"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="营养成分" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="['yingyangDesc']" rows="4" placeholder="请输入营养成分"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="功效说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="['gongxiaoDesc']" rows="4" placeholder="请输入功效说明"/>
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
  import JImageUpload from '@/components/jeecg/JImageUpload'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
   import JTreeSelect from '@/components/jeecg/JTreeSelect'

  export default {
    name: 'NzProductForm',
    components: {
      JFormContainer,
      JImageUpload,
      JDictSelectTag,
      JTreeSelect,
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
          add: "/nz/nzProduct/add",
          edit: "/nz/nzProduct/edit",
          queryById: "/nz/nzProduct/queryById"
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
          this.form.setFieldsValue(pick(this.model,'name','code','pic','brand','rank','valid','people','isFangfuji','storage','yingyangDesc','gongxiaoDesc'))
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
        this.form.setFieldsValue(pick(row,'name','code','pic','brand','rank','valid','people','isFangfuji','storage','yingyangDesc','gongxiaoDesc'))
      },
    }
  }
</script>