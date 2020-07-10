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
      <a-layout>
          <a-layout-sider theme="light" width="260">
            <a-form :form="form">
              <a-form-item label="站点类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['siteType']" @change="selectChangeSiteType" :trigger-change="true" dictCode="siteType" placeholder="请选择站点类型"/>
              </a-form-item>
              <a-form-item label="所属区域" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <area-link-select type="cascader" v-decorator="['area']"  @change="selectChangeArea" placeholder="请选择省市区"/>
              </a-form-item>
              <a-form-item label="站点名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-search type="list" v-decorator="['siteName']" placeholder="查找站点" @change="onChange">
                </a-input-search>
              </a-form-item>

            </a-form>
            <div>
              <a-tree
                :checkable="true"
                :expanded-keys="expandedKeys"
                :auto-expand-parent="autoExpandParent"
                :treeData="treeData"
                @check="onCheck"
                @select="onSelect"
                @expand="onExpand"
              >
                <template slot="title" slot-scope="{ title }">
                  <span v-if="title.indexOf(searchValue) > -1">
                    {{ title.substr(0, title.indexOf(searchValue)) }}
                    <span style="color: #f50">{{ searchValue }}</span>
                    {{ title.substr(title.indexOf(searchValue) + searchValue.length) }}
                  </span>
                  <span v-else>{{ title }}</span>
                </template>
              </a-tree>
            </div>
          </a-layout-sider>
          <a-layout-content>
            <a-table
              ref="table"
              size="middle"
              bordered
              rowKey="id"
              :columns="columns"
              :dataSource="dataSource"
              :pagination="ipagination"
              :loading="loading"
              :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
              class="j-table-force-nowrap"
              @change="handleTableChange">
              <span slot="isUsed" slot-scope="isUsed">
                <div :style="{color: isUsed === '停用'? 'red':'black'}">{{isUsed}}</div>
              </span>
            </a-table>
          </a-layout-content>
      </a-layout>

    </a-spin>
  </j-modal>


</template>

<script>
  import AreaLinkSelect from '../../component/AreaLinkSelect'
  import { httpAction ,getAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JAreaLinkage from '@comp/jeecg/JAreaLinkage'
  import {querySiteName} from "../../../requestAction/request";

  export default {
    name: "SysWarnPointRuleModal",
    mixins:[JeecgListMixin, mixinDevice],
    components: { 
      JDictSelectTag,
      JAreaLinkage,
      AreaLinkSelect
    },
    data () {
      return {
        autoExpandParent:true,
        expandedKeys:[],
        data:[],
        treeData:[],
        dataList:[],
        searchValue:'',
        form: this.$form.createForm(this),
        title:"操作",
        width:1200,
        visible: false,
        model: {},
        items:[],
        siteData:[],
        monitorIds:'',
        ruleIds:'',
        selectedRowKeys:[],
        queryParam:{siteType:this.siteType,
                    area:this.area},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          add: "/swpr/sysWarnPointRule/add",
          edit: "/swpr/sysWarnPointRule/edit",
          list: "/swr/sysWarnRule/list",
        },
        columns: [
          {
            title:'策略类型',
            align:"center",
            dataIndex: 'ruleType_dictText'
          },
          {
            title:'是否发送短信',
            align:"center",
            dataIndex: 'isSendMsg_dictText'
          },
          {
            title:'发送频率(次/天)',
            align:"center",
            dataIndex: 'msgRate'
          },
          {
            title:'短信接收时段',
            align:"center",
            dataIndex: 'warnStarttime',
            customRender: function (text,record) {
              if(text)
                return text.substring(0,5) + '~' + (record.warnEndtime).substring(0,5);
            }

          },
          {
            title:'策略说明',
            align:"center",
            dataIndex: 'content'
          },
          {
            title:'策略状态',
            align:"center",
            dataIndex: 'isUsed_dictText',
            scopedSlots: { customRender: 'isUsed' },
          }]
      }
    },
    created () {
      this.queryParam.siteType = '';
      this.queryParam.area = '';

      let _this= this;
      getAction("/sys/sysArea/list",{active:'1'}).then((res) => {
        if (res.success) {
          _this.data = _this.dealAreaData(res);
          _this.selectChange();
        }
      })

    },
    methods: {


      dealAreaData(res){
        let areaSource = [];
        const province = res.result['86'];
        Object.keys(province).map(key => {
          areaSource.push({key: key, title: province[key], children: []});
          const city = res.result[key];
          Object.keys(city).map(key2 => {

            areaSource[areaSource.length - 1].children.push({key: key2, title: city[key2], children: []});
            const qu = res.result[key2];
            Object.keys(qu).map(key3 => {
              let arrindex = areaSource[areaSource.length - 1].children.length - 1;
              areaSource[areaSource.length - 1].children[arrindex].children.push({key: key3, title: qu[key3]});
            })
          })
        });
        return areaSource
      },
      onExpand(expandedKeys) {
        this.expandedKeys = expandedKeys;
        this.autoExpandParent = false;
      },
      onChange(e) {
        this.expandedKeys = [];
        const value = e.target.value;
        this.find(this.treeData,value);
        console.log(this.expandedKeys,value);

        this.searchValue= value;
        this.autoExpandParent= true

      },
      find(treeDate,val){

        let _this = this;
        treeDate.forEach(e=>{
          console.log(e);
          if(e.children){
            if(this.find(e.children,val)){
              console.log(e.key);
              _this.expandedKeys.push(e.key);
              return true
            }

          }
          if (e.title.indexOf(val) > -1) {
            console.log(e.key);
            _this.expandedKeys.push(e.key);
            return true
          }
        })
      },
      onCheck(checkedKeys, info) {
        //修改选择数据
        this.checkedKeys = checkedKeys;  //只要站点的
        this.halfCheckedKeys= info.halfCheckedKeys;
      },
      //同选择-暂时不做
      onSelect(selectedKeys) {

      },
      selectChangeSiteType(val){
        this.queryParam.siteType = val;
        this.selectChange()
      },
      selectChangeArea(val){
        this.queryParam.area = val;
        this.selectChange()
      },
      selectChange(){
        let that = this;
          querySiteName({siteType: this.queryParam.siteType,area: this.queryParam.area}).then((res)=>{
            this.siteData =  res.result;
            let data = [];
              //省
              that.data.forEach((a)=>{
                //市
                let city = [];
                a.children.forEach((b)=>{
                  let qu = [];
                  //区
                  b.children.forEach((c)=>{
                    let site = [];
                    res.result.forEach((e)=>{
                    if(c.key === e.area)
                    site.push({key:e.key,title:e.siteName})
                  });
                    if(site.length>0){
                      c.children = site;
                      qu.push(c)
                    }
                });
                  if(qu.length>0){
                    b.children = qu;
                    city.push(b)
                  }
              });
                if(city.length>0){
                  a.children = city;
                  data.push(a)
                }
            });
            that.treeData=data;
            console.log("!!!!!!",res);
            if(res.success)
              that.items = res.result;
          });


      },
      add () {
        this.edit({});
      },
      edit (record) {
        //根据策略的 id--- 查询这个一个策略
        if(record.ruleId){
          this.queryParam = {id:record.ruleId};
          this.selectedRowKeys=[record.ruleId]
        }else{
          this.queryParam = {};
          this.selectedRowKeys=[]
        }

        this.loadData();
        console.log(this.dataSource)
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'siteName','siteType','area'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        const sites = [];
        // const rules = [];
        this.checkedKeys.forEach((e)=>{
          this.siteData.forEach((s)=>{
            if(e === s.key){
              sites.push(e);
            }
          })
        });
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
            formData.monitorIds = sites;
            formData.ruleIds = this.selectedRowKeys;
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
        this.form.setFieldsValue(pick(row,'monitorId','ruleId','isUsed'))
      },

      
    }
  }
</script>