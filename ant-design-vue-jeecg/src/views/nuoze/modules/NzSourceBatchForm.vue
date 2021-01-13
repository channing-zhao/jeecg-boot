<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-item label="批次号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input
                v-decorator="['name', { rules: [{ required: true, message: '请输入批次号' }] }]"
                placeholder="请输入批次批次号"
              ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="原药材" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag
                type="list"
                v-decorator="['sourceId', { rules: [{ required: true, message: '请选择原药材' }] }]"
                :trigger-change="true"
                dictCode="nz_source,name,id"
                placeholder="请选择原药材"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="['remark']" rows="4" placeholder="请输入说明" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="种植基地" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-multi-select-tag
                type="list_multi"
                v-decorator="['areaIds', { rules: [{ required: true, message: '请选择基地' }] }]"
                :trigger-change="true"
                dictCode="nz_base,name,id"
                placeholder="请选择所在基地"
              />
            </a-form-item>
          </a-col>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
<div style="margin：0 auto"> 
    <a-divider />
     <a-timeline>
      <a-timeline-item v-for="item in resultList">
        {{ item.createTime }} {{ item.createBy_dictText }} {{ item.node_dictText }}</a-timeline-item
      >
    </a-timeline>
</div>
  </a-spin>
</template>

<script>
import { httpAction, getAction } from '@/api/manage'
import pick from 'lodash.pick'
import { validateDuplicateValue } from '@/utils/util'
import JFormContainer from '@/components/jeecg/JFormContainer'
import JMultiSelectTag from '@/components/dict/JMultiSelectTag'
import JDictSelectTag from '@/components/dict/JDictSelectTag'

export default {
  name: 'NzSourceBatchForm',
  components: {
    JFormContainer,
    JMultiSelectTag,
    JDictSelectTag,
  },
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {},
      required: false,
    },
    //表单模式：true流程表单 false普通表单
    formBpm: {
      type: Boolean,
      default: false,
      required: false,
    },
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
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
      validatorRules: {},
      url: {
        list: '/trace/sourceBatch',
        add: '/nz/nzSourceBatch/add',
        edit: '/nz/nzSourceBatch/edit',
        queryById: '/nz/nzSourceBatch/queryById',
      },
      dictOptions: {},
      resultList: {},
     
   }
  },
  computed: {
    formDisabled() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return false
        }
        return true
      }
      return this.disabled
    },
    showFlowSubmitButton() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return true
        }
      }
      return false
    },
  },
  created() {
    //如果是流程中表单，则需要加载流程表单data
    this.showFlowData() 
  },
  methods: {
    add() {
      this.edit({})
    },
    edit(record) {
      
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'name', 'sourceId', 'remark', 'areaIds'))
      })
      this.listTrace(record)
    },
    //渲染流程表单数据
    showFlowData() {
      if (this.formBpm === true) {
        let params = { id: this.formData.dataId }
        getAction(this.url.queryById, params).then((res) => {
          if (res.success) {
            this.edit(res.result)
          }
        })
      }
    },
    submitForm() {
      const that = this
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
          let formData = Object.assign(this.model, values)
          console.log('表单提交数据', formData)
          httpAction(httpurl, formData, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
            })
        }
      })
    },
    popupCallback(row) {
      this.form.setFieldsValue(pick(row, 'name', 'sourceId', 'remark', 'areaIds'))
    },
    listTrace(row) {
      //批次
      let batchid = row.id
      let params = { batchid: batchid }
      getAction( this.url.list, params).then((res) => {
        if (res.success && res.result.total > 0) {
          this.resultList = res.result.records
        } 
      })
    },
  },
}
</script>