<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <div>
          <a-row>
            <a-col span="12">
              <a-form-item label="联系人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['name',validatorRules.name]" placeholder="请输入联系人姓名"></a-input>
              </a-form-item>
            </a-col>
            <a-col span="12">
              <a-form-item label="手机号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['mobile',validatorRules.mobile]" placeholder="请输入手机号码"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col span="12">
              <a-form-item label="所属单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-select v-decorator="['companyId', validatorRules.companyId]" show-search style="width: 100%" placeholder="请输入所属单位" optionFilterProp="children">
                  <a-select-option v-for="item in items" :key="item.key" :value="item.key">
                    {{item.value}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
        </div>

        <div>
          <a-row type="flex" justify="space-around" align="top">
            <a-col :span="8">
              <a-card title="所有站点">
                <a-tree
                  autoExpandParent
                  :checkable="true"
                  :checkStrictly = "false"
                  :checkedKeys="checkedKeys"
                  :treeData="treeData"
                  @check="onCheck"
                  @select="onSelect"
                />
              </a-card>
            </a-col>
            <a-col :span="4">
              <div style="height: 800px">
                <a-button-group style="top: 50%;">
                  <a-button type="primary" @click="deleteArea" :disabled="disable"> <a-icon type="left" />删除 </a-button>
                  <a-button type="primary" @click="addArea" :disabled="disable"> 新增<a-icon type="right" /> </a-button>
                </a-button-group>
              </div>
            </a-col>
            <a-col :span="8">
              <a-card title="已选站点" >
                <a-tree
                  :defaultExpandAll="true"
                  autoExpandParent

                  :checkable="true"
                  :checkStrictly = "false"
                  :checkedKeys="checkedKeys2"
                  :treeData="treeData2"
                  @check="onCheck2"
                  @select="onSelect2"
                />
              </a-card>
            </a-col>
          </a-row>
        </div>


      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>
  import {ajaxGetDictItems,getDictItemsFromCache} from '@/api/api'
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import {queryCompanyName} from "../../../requestAction/request";
  import { getAction,postAction} from '@/api/manage'
  import Vue from "vue";
  import {querySiteName} from "../../../requestAction/request";
  import {duplicateCheck } from '@/api/api'

  export default {
    name: "SysWarnUserPointModal",
    components: { 
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:1000,
        visible: false,
        items:[],
        model: {},
        disable:true,
        treeDataSource:[],
        originalData:[],
        originalData2:[],
        checkedKeys:[],
        treeData2:[],
        checkedKeys2:[],
        videoHight:'',
        targetKeys:[],
        expandedKeys:[],
        dictOptions:{},
        halfCheckedKeys:[],
        halfCheckedKeys2:[],
        siteData:[],
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
          mobile: {
            rules: [
              {required: true, message: '手机号码不能为空'},
              {validator: this.validateMobile}
            ]
          },
          name: {
            rules: [
              {required: true, message: '姓名不能为空'}
            ]
          },
          companyId: {
            rules: [
              {required: true, message: '企业不能为空'}
            ]
          }
        },
        url: {
          add: "/swup/sysWarnUserPoint/add",
          edit: "/swup/sysWarnUserPoint/edit",
        }
      }
    },
    computed: {
      treeData() {
        return this.handleTreeData(this.treeDataSource, this.targetKeys)
      }
    },
    created () {
      let that = this;
      //查询企业名称
      queryCompanyName().then((res) => {
        if (res.success) {
          that.items = res.result;
        }
      });
      this.init();

    },
    methods: {
      init() {
        let _this = this;

        //先把所有的数据都查询出来
        querySiteName().then((res) => {
          if (res.success) {
            _this.originalData = res.result
            _this.treeDataSource = this.dealSiteData(res);
          }
        });


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
        if(record) {
          //普通字典数据
          if(!this.dictOptions[record])
            this.initDictData(record)
          let result =
            this.dictOptions[record].find(e=>{
              return  e.value===text;
            });
          if(result==null)
            return text;
          return result.text;
        }
        return text;

      },
      dealSiteData(res){
        let siteSource = [];
        let _this =this
        res.result.forEach((e)=>{
          if(siteSource[e.siteType])
            siteSource[e.siteType].push({key:e.key,title:e.siteName})
          else
            siteSource[e.siteType]=[{key:e.key,title:e.siteName}]
        })
        let result = []
        Object.keys(siteSource).map(key => {
          result.push({
            key:key,
            title:_this.dictVal(key,'siteType'),
            children:siteSource[key]
          })
        })
        return result
      },
      handleTreeData(data, targetKeys = []) {
        data.forEach(item => {
          item['disabled'] = targetKeys.includes(item.key)
          if(item.children) {
            this.handleTreeData(item.children, targetKeys)
          }
        })
        return data
      },
      onCheck(checkedKeys, info) {
        //修改选择数据
        this.checkedKeys = checkedKeys
        this.halfCheckedKeys= info.halfCheckedKeys

      },
      onSelect(selectedKeys) {
        // selectedKeys.forEach(e=>{
        //   //
        //   this.checkedKeys.push(e)
        // })
      },
      onCheck2(checkedKeys, info) {
        console.log(checkedKeys,info)

        this.checkedKeys2 = checkedKeys
        this.halfCheckedKeys2= info.halfCheckedKeys

      },
      //同选择-暂时不做
      onSelect2(selectedKeys) {
        // selectedKeys.forEach(e=>{
        //   //
        //   this.checkedKeys2.push(e)
        // })
      },
      addArea(){
        //处理左边新增
        this.targetKeys   = []
        this.originalData2 = []
        let _this = this
        this.originalData.forEach(e=>{
          _this.checkedKeys.forEach(element=>{
            if(element===e.key){
              _this.originalData2.push(e)
              console.log(e)
            }
          })
         })
        //
        this.treeData2 =   this.dealSiteData({result:_this.originalData2});

        this.targetKeys = []
        this.checkedKeys.forEach(
          e=>{
            _this.targetKeys.push(e)
          }
        )

        this.checkedKeys2   = this.checkedKeys

      },
      deleteArea(){
        //右边的处理完了
        let result = []
        let _this = this
        this.originalData2.forEach(e=>{
          _this.checkedKeys2.forEach(element=>{
            if(element===e.key)
              result.push(e)
          })
        })
        this.treeData2 =   this.dealSiteData({result:result});
        this.targetKeys   = this.checkedKeys2
        this.checkedKeys   = this.checkedKeys2
      },
      areaChange(checkedKeys,halfCheckedKeys){


      //   this.disable=true;
      //   let _this = this;
      //   postAction("/sys/sysArea/change",{keys:[...checkedKeys ,...halfCheckedKeys]}).then((res)=>{
      //     if(res.success)
      //       this.$message.success(res.message)
      //     else
      //       this.$message.error(res.message)
      //   }).finally(() => {
      //     _this.init()
      //     Vue.ls.remove("sys_areas")
      //   })
      },
      validateMobile(rule, value, callback){
        let params = {
          tableName: 'sys_warn_user',
          fieldName: 'mobile',
          fieldVal: value,
          dataId: this.model.warnUserid
        };
        if (!value || new RegExp(/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/).test(value)) {
          duplicateCheck(params).then((res) => {
            if (res.success) {
              callback()
            } else {
              callback("该联系人已存在!")
            }
          })
        } else {
          callback("您的手机号码格式不正确!");
        }

      },
      add () {
        this.edit({});
      },
      edit (record) {
        this.checkedKeys = []
        this.checkedKeys2 = []
        // _this.expandedKeys = []
        this.targetKeys = []
        let _this = this;
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name','mobile','companyId'))
        })
        _this.disable=false
        if(record.id){


        //查询
        getAction("/swup/sysWarnUserPoint/querySelectedSite",{warnUserid:record.warnUserid}).then((res) => {
          console.log("$$$",res)
          if (res.success) {
            //预处理一下所有数据
            _this.treeData2 =   this.dealSiteData(res);
            _this.originalData2 = res.result
            res.result.forEach(e=>{
              _this.targetKeys.push(e.key)
            })
            _this.checkedKeys = _this.targetKeys
            _this.checkedKeys2 = _this.targetKeys
            // _this.expandedKeys = _this.targetKeys


          }
        })
        }else{
          _this.treeData2 =   [];
          _this.originalData2 = []
          _this.checkedKeys = []
          _this.checkedKeys2 = []
        }
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
            formData.monitorIds = this.originalData2.map(e=>{
              return e.key
            });

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
              that.close();
            })
          }
         
        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'warnUserid','monitorId'))
      },

      
    }
  }
</script>
<style scoped>
  .tree-transfer .ant-transfer-list:first-child {
    width: 50%;
    flex: none;
  }
</style>