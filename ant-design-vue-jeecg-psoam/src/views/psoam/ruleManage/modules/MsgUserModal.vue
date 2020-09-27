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

        <div style="height: 10%">
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

        <div style="height: 450px">
          <a-row type="flex" justify="space-around" align="top">
            <a-col :span="8">
              <a-card title="所有站点" style="height: 450px">
                <a-input-search style="margin-bottom: 8px" placeholder="输入企业或者站点名称查询" @change="onChange" />
                <div class="a-tree-scroll">

                  <a-tree
                    style="height: 250px"
                    :expanded-keys="expandedKeys"
                    :autoExpandParent="autoExpandParent"
                    :checkable="true"
                    :checkStrictly = "false"
                    :checkedKeys="checkedKeys"
                    :treeData="treeData"
                    :selected-keys="selectedKeys"
                    @expand="onExpand"
                    @check="onCheck"
                    @select="onSelect"
                  >
                    <template slot="title" slot-scope="{ title }">
                      <span v-if="title.indexOf(searchValue) > -1" style="float: left;margin-left: 4px">
                        {{ title.substr(0, title.indexOf(searchValue)) }}
                        <span style="color: #f50">{{ searchValue }}</span>
                        {{ title.substr(title.indexOf(searchValue) + searchValue.length) }}
                      </span>
                      <span v-else  style="float: left;margin-left: 4px">{{ title}}</span>
                    </template>

                  </a-tree>
                </div>
              </a-card>
            </a-col>
            <a-col :span="4">
              <div style="height: 380px">
                <a-button-group style="top: 50%;">
                  <a-button type="primary" @click="deleteArea" :disabled="disable"> <a-icon type="left" />删除 </a-button>
                  <a-button type="primary" @click="addArea" :disabled="disable"> 新增<a-icon type="right" /> </a-button>
                </a-button-group>
              </div>
            </a-col>
            <a-col :span="8">
              <a-card title="已选站点" style="height: 450px">
                <a-input-search style="margin-bottom: 8px" placeholder="输入企业或者站点名称查询" @change="onChange2" />
                <div class="a-tree-scroll">

                  <a-tree
                    style="height: 250px"
                    :expanded-keys="expandedKeys2"
                    :autoExpandParent="autoExpandParent2"
                    :checkable="true"
                    :checkStrictly = "false"
                    :checkedKeys="checkedKeys2"
                    :treeData="treeData2"
                    :selected-keys="selectedKeys"
                    @expand="onExpand2"
                    @check="onCheck2"
                    @select="onSelect2"
                  >
                    <template slot="title" slot-scope="{ title }">
                      <span v-if="title.indexOf(searchValue2) > -1" style="float: left;margin-left: 4px">
                        {{ title.substr(0, title.indexOf(searchValue2)) }}
                        <span style="color: #f50">{{ searchValue2 }}</span>
                        {{ title.substr(title.indexOf(searchValue2) + searchValue2.length) }}
                      </span>
                      <span v-else  style="float: left;margin-left: 4px">{{ title}}</span>
                    </template>

                  </a-tree>

                </div>
              </a-card>
            </a-col>
          </a-row>
        </div>


      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">关闭</a-button>
      <a-button type="primary" @click="handleOk">确定</a-button>
    </template>
  </j-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import {duplicateCheck } from '@/api/api'
  import {queryCompanyName} from "../../../requestAction/request";
  import { getAction,postAction} from '@/api/manage'
  import {dataDictMixin} from "../../mixin/dataDictMixin";

  export default {
    name: "MsgUserModal",
    mixins:[dataDictMixin],
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:1100,
        visible: false,
        items:[],
        model: {},
        siteInfos:[],
        companyInfos:{},
        mnInfos:{},
        treeData:[],
        expandedKeys:[],
        autoExpandParent:true,
        searchValue:'',
        selectedKeys:[],
        halfCheckedKeys:[],
        halfCheckedKeys2:[],
        checkedKeys:[],
        treeData2:[],
        expandedKeys2:[],
        autoExpandParent2:true,
        searchValue2:'',
        checkedKeys2:[],
        targetKeys:[],
        originalData:[],
        originalData2:[],

        disable:true,
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
          add: "/wr/warnRule/addMsgRule",
          edit: "/wr/warnRule/editX",
        }
      }
    },
    watch: {
      targetKeys() {
       this.treeData =  this.handleTreeData(this.treeData, this.targetKeys)
      }
    },
    methods: {
      init() {
        let _this = this;
        let param  = {
          companyIds :_this.$store.getters.userInfo.companyIds.join(','),
        };
        //先把所有的数据都查询出来
        getAction("/wr/warnRule/queryMsgUserInfo",param).then((res) => {
          if (res.success) {
            _this.originalData = res.result
            _this.treeData = this.dealSiteData(res.result);
            _this.onChange({target:{value:''}});//默认全部打开
          }
        });
      },
      dealSiteData(siteInfos){
        let siteSource = [];
        let _this =this;
        _this.siteInfos = siteInfos;
        siteInfos.forEach(e=>{
          if(siteSource[e.area])
            siteSource[e.area].push(e);
          else
            siteSource[e.area]=[{...e}]
        });
        let result = [];
        Object.keys(siteSource).map(key =>{
          let siteInfo = [];
          _this.companyInfos={};
          siteSource[key].forEach(e=>{
            if(siteInfo[e.companyId])
              siteInfo[e.companyId].push(e);
            else
            {
              siteInfo[e.companyId]=[{...e}];
              _this.companyInfos[e.companyId] = e.companyName;
            }
          });
          let childrens = [];
          Object.keys(siteInfo).map(companyId=>{
            let types = [];
            _this.mnInfos = {};
            siteInfo[companyId].forEach(e=>{
              if(types[e.mn])
                types[e.mn].push({key:e.ruleType+','+e.mn,title:_this.dictVal('warnType',e.ruleType),scopedSlots: { title: 'title' }});
              else {
                types[e.mn]=[{key:e.ruleType+','+e.mn,title:_this.dictVal('warnType',e.ruleType),scopedSlots: { title: 'title' }}];
                _this.mnInfos[e.mn] = e.siteName;
              }
            });
            let child = [];
            Object.keys(types).map(mn=>{
              child.push({
                key:mn,
                title:_this.mnInfos[mn],
                scopedSlots: { title: 'title' },
                children:types[mn],
              });
            });
            childrens.push({
              key:companyId,
              title: _this.companyInfos[companyId],
              scopedSlots: { title: 'title' },
              checkable:false,
              children:child,
            })
          });
          result.push({
            key:key,
            title:_this.getAreaByCode(key,[2]),
            scopedSlots: { title: 'title' },
            checkable:false,
            children:childrens,

          })
        });
        return result;
      },
      onChange(e){
        this.expandedKeys = [];
        const value = e.target.value;
        this.find(this.treeData,value);
        //console.log(this.expandedKeys,value);

        this.searchValue= value;
        this.autoExpandParent= true
      },
      onChange2(e){
        this.expandedKeys2 = [];
        const value = e.target.value;
        this.find2(this.treeData2,value);
        //console.log(this.expandedKeys2,value);

        this.searchValue2= value;
        this.autoExpandParent2= true
      },
      find(treeDate,val){

        let _this = this;
        treeDate.forEach(e=>{
          //console.log(e);
          if(e.children){
            if(this.find(e.children,val)){
              //console.log(e.key);
              _this.expandedKeys.push(e.key);
              return true
            }

          }
          if (e.title.indexOf(val) > -1) {
            //console.log(e.key);
            _this.expandedKeys.push(e.key);
            return true
          }
        })
      },
      find2(treeDate,val){

        let _this = this;
        treeDate.forEach(e=>{
          //console.log(e);
          if(e.children){
            if(this.find2(e.children,val)){
              //console.log(e.key);
              _this.expandedKeys2.push(e.key);
              return true
            }

          }
          if (e.title.indexOf(val) > -1) {
            //console.log(e.key);
            _this.expandedKeys2.push(e.key);
            return true
          }
        })
      },
      onExpand(expandedKeys) {
        this.expandedKeys = expandedKeys;
        this.autoExpandParent = false;
      },
      onExpand2(expandedKeys) {
        this.expandedKeys2 = expandedKeys;
        this.autoExpandParent2 = false;
      },
      onSelect(val) {
        // this.selectedKeys = val;
        // this.selectChange();
      },
      // selectChange(){
      //   let siteInfo;
      //   for(let i=0;i<this.siteInfos.length;i++){
      //     debugger
      //     if(this.selectedKeys[0]===this.siteInfos[i].mn){
      //       siteInfo = this.siteInfos[i];
      //       break;
      //     }
      //   }
      //   this.$emit("selectChange",siteInfo);
      // },
      handleTreeData(data, targetKeys = []) {
        console.log();
        data.forEach(item => {
          item['disabled'] = targetKeys.includes(item.key);
          if(item.children) {
            this.handleTreeData(item.children, targetKeys)
          }
        });
        return data
      },
      onCheck(checkedKeys, info) {
        //修改选择数据
        this.checkedKeys = checkedKeys;
        this.halfCheckedKeys= info.halfCheckedKeys;

      },

      onCheck2(checkedKeys, info) {
        this.checkedKeys2 = checkedKeys;
        this.halfCheckedKeys2= info.halfCheckedKeys;

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
        this.originalData2 = []
        let _this = this
        this.originalData.forEach(e=>{
          _this.checkedKeys.forEach(a=>{
            var typeKey = a.split(',');
            if(typeKey.length>1){
              if(typeKey[0]===e.ruleType && typeKey[1]===e.mn){
                _this.originalData2.push(e)
              }
            }

          })
        });
        //
        this.treeData2 =   this.dealSiteData(this.originalData2);
        this.onChange2({target:{value:''}});//默认全部打开
        let targetKeys = [];
        this.checkedKeys.forEach(e=>{
           targetKeys.push(e);
        });
        _this.targetKeys = targetKeys
        this.checkedKeys2   = [];
        this.checkedKeys   = this.targetKeys
      },
      deleteArea(){
        //右边的处理完了
        let result = [];
        let _this = this;
        this.originalData2.forEach(e=>{
          if(_this.checkedKeys2.indexOf(e.ruleType+','+e.mn)<0)
            result.push(e)
        });
        this.treeData2 =   this.dealSiteData(result);
        let targetKeys = [];
        result.forEach(e=>{
           targetKeys.push(e.ruleType+','+e.mn);
        });
        _this.targetKeys = targetKeys
        this.checkedKeys   = this.targetKeys
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
        let _this = this;
        //查询企业名称
        queryCompanyName({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res) => {
          if(res.success){
            _this.items = res.result.companyNames;
          }
        });
        this.init();


        this.checkedKeys = []
        this.checkedKeys2 = []
        // _this.expandedKeys = []
        this.targetKeys = []

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
            //console.log("$$$",res)
            if (res.success) {
              //预处理一下所有数据
              _this.treeData2 =   this.dealSiteData(res);
              _this.originalData2 = res.result
              let targetKeys = []
              res.result.forEach(e=>{
                targetKeys.push(e.key)
              })
              _this.targetKeys = targetKeys
              _this.checkedKeys = _this.targetKeys
              _this.checkedKeys2 = _this.targetKeys
              // _this.expandedKeys = _this.targetKeys


            }
          })
        }else{
          _this.treeData2 =   [];
          _this.originalData2 = [];
          _this.checkedKeys = [];
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
            formData.mnAndTypes = this.originalData2.map(e=>{
              return e.ruleType+'+'+e.mn
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
  .a-tree-scroll{
    height: 310px;
    overflow: auto;
  }
</style>