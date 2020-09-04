<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen

    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
        <div class="company-info">
            <div class="info-item"><span>企业地址:</span> <span>{{companyInfo.address}}</span></div>
            <div class="info-item" >
              <div class="float_left"><span>企业简称:</span><span>{{companyInfo.short_name}}</span></div>
              <div class="float_left"><span>所属行业:</span><span>{{dictVal('industry',companyInfo.industry)}}</span></div>
            </div>
            <div class="info-item">
              <div class="float_left"><span>行政区域:</span><span>{{getAreaByCode(companyInfo.administrative_region)}}</span> </div>
              <div class="float_left"><span>环保联系人:</span><span>{{companyInfo.env_protect_contact}}</span> </div>
            </div>

        </div>

        <div class="points-radio">
          <button-tabs :tabList="companyInfo.points" @change = "querydetail"></button-tabs>
        </div>
      <div class="points-info">
        <div class="info-item">
          <div class="point_left"><span>站点级别:</span><span>{{dictVal('siteLevel',companyInfo.points[showPointInex].site_level)}}</span></div>
          <div class="point_left"><span>站点类型:</span><span>{{dictVal('siteType',companyInfo.points[showPointInex].site_type)}}</span></div>
          <div class="point_left"><span>进出口:</span><span>{{dictVal('imorex',companyInfo.points[showPointInex].imorex)}}</span></div>

        </div>
        <div class="info-item" >
          <div class="point_left"><span>站点位置:</span><span>{{companyInfo.points[showPointInex].location}}</span></div>
          <div class="float_left_67"><span>排放去向:</span><span>{{dictVal('direction',companyInfo.points[showPointInex].direction)}}</span></div>
        </div>
        <div class="info-item-min"> <span>数据时间:</span><span>{{companyInfo.short_name}}</span></div>

      </div>
      <a-table :columns="columns" :dataSource="dataSource"  bordered>

      </a-table>
    </a-spin>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">企业详情</a-button>
      <a-button type="primary" @click="handleCancel">历史数据</a-button>
      <a-button type="primary" @click="handleCancel">超标数据</a-button>
      <a-button type="primary" @click="handleCancel">报警信息</a-button>
      <a-button type="primary" @click="handleCancel">工况监控</a-button>
    </template>
  </a-modal>
</template>

<script>

  import {dataDictMixin} from "../../mixin/dataDictMixin";
  import {querySiteInfos} from "../../../requestAction/request"
  import ButtonTabs from "../../component/ButtonTabs";
  export default {
    name: "companyModal",
    mixins:[dataDictMixin],
    components: {
      ButtonTabs
    },
    props: {
    },
    data () {
      return {
        title:"",
        width:800,
        visible: false,
        confirmLoading: false,
        companyInfo:{},
        checkedRadio:0,
        radio_check:{
          color: 'white',
          background: '#0098A2',
        },
        showPointInex:0,
        columns:[],
        ocolumns:[
          {
            align:'center',
            dataIndex:'pollutionName',
            title:'污染物名',
          },
          {
            align:'center',
            dataIndex:'value',
            title:'监测值'
          },
          {
            align:'center',
            dataIndex:'unit',
            title:'单位'
          },
          {
            align:'center',
            dataIndex:'dataStatus',
            title:'数据状态'
          },
          {
            align:'center',
            dataIndex:'deviceStatus',
            title:'设备状态'
          }

        ],
        zscolumns:[
          {
            align:'center',
            dataIndex:'pollutionName',
            title:'污染物名',
          },
          {
            align:'center',
            dataIndex:'value',
            title:'监测值'
          },
          {
            align:'center',
            dataIndex:'zsValue',
            title:'监测值'
          },
          {
            align:'center',
            dataIndex:'unit',
            title:'单位'
          },
          {
            align:'center',
            dataIndex:'dataStatus',
            title:'数据状态'
          },
          {
            align:'center',
            dataIndex:'deviceStatus',
            title:'设备状态',
            customRender:function (text) {
                return text;
            }
          }

        ],
        dataSource:[]
      }
    },
    created () {
    },
    mounted() {

    },
    methods: {
      //item 是企业信息
      show(item){
        console.log(item)
        this.title = item.company_name;
        this.companyInfo  = item;
        this.visible=true;
      },
      handleCancel(){
        this.visible=false;
      },
      querydetail(index){
        console.log(this.companyInfo.points[index])
        this.showPointInex = index;
        let _this = this;
        debugger
        querySiteInfos({mn:this.companyInfo.points[index].mn,siteType:this.companyInfo.points[index].site_type})
          .then(res=>{
            console.log(res)
            if(res.success){
              if(res.result.hasZs)
                _this.columns=_this.zscolumns
              else{
                _this.columns=_this.ocolumns
              }
              _this.dataSource = res.result.dataList;
            }else{
              this.$message.error(res.message);
            }
        })
      }
    }
  }
</script>
<style>
  .points-radio{

  }
  .points-radio{
    padding-top: 10px;

  }
  .points-info{
    margin-top: 10px;
    width: 100%;
    height: 100px;
  }
  .info-item-min{

    margin: 10px 20px 0 20px;
    font-size: 12px;
    font-family: Microsoft YaHei;
    font-weight: 400;
    color: #1F1F1F;
    line-height: 25px;
    opacity: 0.9;
    height: 25px;
  }

  .company-info{
    background: #ebf8f8;
    padding-top:10px ;
    /*opacity: 0.08;*/
    width: 100%;
    height: 100px;
  }
  .ant-modal-body{
    padding: 0;
  }
  .float_left{
    float: left;
    width: 50%;
  }
  .float_left_67{
    float: left;
    width: 67%;
  }
  .point_left{
    float: left;
    width: 33%;
  }
  .info-item{
    margin: 0 20px;
    font-size: 14px;
    font-family: Microsoft YaHei;
    font-weight: 400;
    color: #1F1F1F;
    line-height: 30px;
    opacity: 0.9;
    height: 30px;
  }
  .ant-modal-header{
    text-align: center;
    background: rgba(0, 152, 162, 0.8);
    padding: 10px 24px;
    border-bottom: 0px ;
  }
  .ant-modal-close-x{
    line-height: 44px;
  }


</style>