<template>
  <business-modal
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"

    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-descriptions title="申报详情" >
        <a-descriptions-item label="申报时间">
          {{model.createTime}}
        </a-descriptions-item>
        <a-descriptions-item label="申报人" :span="2">
          {{model.createBy}}
        </a-descriptions-item>

        <a-descriptions-item label="申报详情" :span="3">
          <a-table :columns="columns" :data-source="data">

          </a-table>
        </a-descriptions-item>
        <a-descriptions-item label="审核人">
          {{model.updateBy}}
        </a-descriptions-item>
        <a-descriptions-item label="申报状态">
          {{ geStatusValue(model.status)}}
        </a-descriptions-item>
        <a-descriptions-item label="生效时间">
         {{model.updateTime}}
        </a-descriptions-item>

      </a-descriptions>
    </a-spin>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">关闭</a-button>
    </template>
  </business-modal>
</template>

<script>
  import AreaHandler from "../../../../component/AreaHandler";
  import {loadAreaDate} from '../../../../requestAction/areaUtil'
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import BusinessModal from "../../../../component/BusinessModal";
  import {queryComparisonData} from "../../../../requestAction/request"
  import {ajaxGetDictItems,getDictItemsFromCache} from '@/api/api'

  export default {
    name: "CompanyApplyModal",
    components: {
      BusinessModal,

    },
    data () {
      return {
        form: this.$form.createForm(this),
        width:800,
        visible: false,
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
        columns : [
          {
            dataIndex: 'fieldName',
            title:'更改项',
            key: 'fieldName',

          },
          {
            title: '更改前',
            dataIndex: 'firstVal',
            key: 'firstVal',
            customRender: (text,record) => {
              return this.dictVal (text,record)
            }
          },
          {
            title: '更改后',
            dataIndex: 'secondVal',
            key: 'secondVal',
            customRender: (text,record) => {
              return this.dictVal (text,record)
            }
          },

        ],
        data : [
        ],
        dictOptions:{},
        areaHandler:''

      }
    },
    created :async  function() {
      this.initDictData("statue");
      await loadAreaDate()
      this.areaHandler = new AreaHandler()
    },
    methods: {
      initArea(){
        this.areaHandler = new AreaHandler()
      },
      initDictData(dictCode) {

        //优先从缓存中读取字典配置
        if(getDictItemsFromCache(dictCode)){
          this.dictOptions[dictCode] = getDictItemsFromCache(dictCode);
          return
        }
        //根据字典Code, 初始化字典数组
        ajaxGetDictItems(dictCode, null).then((res) => {
          if (res.success) {
            this.dictOptions[dictCode] = res.result;
          }
        })
      },
      dictVal(text,record){
        console.log(text,record)
        if(text===null)
          return ''
        if(record.dictCode) {
          //级联行政地址
          if(record.dictCode==='AREA') {
            console.log(this.areaHandler==='')
            //初始化
            if(this.areaHandler==='')
            {
              this.initArea()
            }

            let arr = [];
            this.areaHandler.getAreaBycode(text,arr);
            return arr.join('')
          }

          //普通字典数据
          if(!this.dictOptions[record.dictCode])
            this.initDictData(record.dictCode)
          let result =
            this.dictOptions[record.dictCode].find(e=>{
              return  e.value===text;
            });
          if(result==null)
            return text;
          return result.text;
        }
       return text;

      },
      detail (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      compareDetail(fromTable){
        let that = this;
        //查询前后明细
        queryComparisonData({beforeId:this.model.oldId,afterId:this.model.newId,fromTable:fromTable}).then((res)=>{
          if(res.success) {
            that.data = res.result;
          }else{
            this.$message.error(res.message);
          }
        });
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
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
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
         
        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'updateTime','companyId','status','content'))
      },
      geStatusValue(status){
        if(status===null)
          return status;
        let result =
          this.dictOptions.statue.find(e=>{
            return  e.value===status;
          });
        if(result==null)
          return status;
        return result.text;
      }

      
    }
  }
</script>