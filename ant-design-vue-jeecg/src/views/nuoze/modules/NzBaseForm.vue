<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="12" >
            <a-form-item label="基地名" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['name',{rules: [{ required: true, message: '请输入基地名'}]}]" placeholder="请输入基地名"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-select-user-by-dep v-decorator="['head']"/>
            </a-form-item>
          </a-col>
          
          <a-col :span="12" >
            <a-form-item label="地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['address']" placeholder="请输入地址"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="面积" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['area', validatorRules.area]" placeholder="请输入公顷数" style="width: 30%"/>公顷
            </a-form-item>
          </a-col>
          
          <a-col :span="12" >
            <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="['desconte']" rows="4" placeholder="请输入描述"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-image-upload isMultiple v-decorator="['pic']"></j-image-upload>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
      <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs" style="show:none">
      <a-tab-pane tab="" :key="refKeys[0]" :forceRender="true">
         <j-editable-table
          :ref="refKeys[0]"
          :loading="nzAreaTable.loading"
          :columns="nzAreaTable.columns"
          :dataSource="nzAreaTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
         />
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>

  import pick from 'lodash.pick'
  import { getAction } from '@/api/manage'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JImageUpload from '@/components/jeecg/JImageUpload'
  import JSelectUserByDep from '@/components/jeecgbiz/JSelectUserByDep'
  import JEditor from '@/components/jeecg/JEditor'

  export default {
    name: 'NzBaseForm',
    mixins: [JEditableTableMixin],
    components: {
      JFormContainer,
      JImageUpload,
      JSelectUserByDep,
      JEditor,
    },
    data() {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        labelCol2: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperCol2: {
          xs: { span: 24 },
          sm: { span: 20 },
        },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
          tel: {
            rules: [
              { required: false},
              { pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
            ]
          },
          area: {
            rules: [
              { required: false},
              { pattern: /^-?\d+\.?\d*$/, message: '请输入数字!'},
            ]
          },
        },
        refKeys: ['nzArea', ],
        tableKeys:['nzArea', ],
        activeKey: 'nzArea',
        // 区块
        nzAreaTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '',
              key: 'name',
              
              defaultValue: '',
            }
          ]
        },
        url: {
          add: "/nz/nzBase/add",
          edit: "/nz/nzBase/edit",
          queryById: "/nz/nzBase/queryById",
          nzArea: {
            list: '/nz/nzBase/queryNzAreaByMainId'
          },
        }
      }
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
        required: false
      },
      //表单模式：false流程表单 true普通表单
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
      addBefore(){
        this.form.resetFields()
        this.nzAreaTable.dataSource=[]
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model,'name','head','address','area','desconte','pic')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          //this.requestSubTableData(this.url.nzArea.list, params, this.nzAreaTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        return {
          ...main, // 展开
          nzAreaList: allValues.tablesValue[0].values,
        }
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          })
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row,'name','head','address','area','desconte','pic'))
     },

    }
  }
</script>