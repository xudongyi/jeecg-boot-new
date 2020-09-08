<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="行政区域">
              <area-link-select @change="areaChange" type="cascader" v-model="queryParam.area" show-search style="width: 100%" optionFilterProp="children"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="企业名称">
              <a-select v-model="queryParam.companyId" @change="companyNameChange" :allowClear="allowClear" placeholder="请选择" show-search style="width: 100%" optionFilterProp="children">
                <!--                <a-select-option value="">请选择</a-select-option>-->
                <a-select-option v-for="item in companyNames" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="监测点名称">
              <a-select v-model="queryParam.mn" :allowClear="allowClear" placeholder="请选择" show-search style="width: 100%" optionFilterProp="children">
                <!--                <a-select-option :value="companyIds">请选择</a-select-option>-->
                <a-select-option v-for="item in items" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button :ghost="checkedRadio!=='total'" @click="check('total')" :style="checkedRadio==='total'?radio_check:{}"
                class="radio-column total">总数&#12288;{{companyFlagNum.total}}</a-button>
      <a-button :ghost="checkedRadio!=='normal'" @click="check('normal')" :style="checkedRadio==='normal'?radio_check:{}"
                class="radio-column normal">正常&#12288;{{companyFlagNum.normal}}</a-button>
      <a-button :ghost="checkedRadio!=='warn'" @click="check('warn')" :style="checkedRadio==='warn'?radio_check:{}"
                class="radio-column warn">预警&#12288;{{companyFlagNum.warn}}</a-button>
      <a-button :ghost="checkedRadio!=='standard'" @click="check('standard')" :style="checkedRadio==='standard'?radio_check:{}"
                class="radio-column standard">超标&#12288;{{companyFlagNum.standard}}</a-button>
      <a-button :ghost="checkedRadio!=='abnormal'" @click="check('abnormal')" :style="checkedRadio==='abnormal'?radio_check:{}"
                class="radio-column abnormal">异常&#12288;{{companyFlagNum.abnormal}}</a-button>
      <a-button :ghost="checkedRadio!=='offline'" @click="check('offline')" :style="checkedRadio==='offline'?radio_check:{}"
                class="radio-column offline">离线&#32;{{companyFlagNum.offline}}</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
<!--      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">-->
<!--        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项-->
<!--        <a style="margin-left: 24px" @click="onClearSelected">清空</a>-->
<!--      </div>-->

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="ID"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="scroll"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <div slot="filterDropdown">
          <a-card title="自定义列">
            <a-checkbox-group @change="onColSettingsChange" v-model="settingColumns" :defaultValue="settingColumns">
              <a-row>
                <template v-for="(item,index) in defColumns">
                  <template v-if="item.key!='rowIndex'&& item.dataIndex!='action'">
                    <a-col :span="12"><a-checkbox :value="item.dataIndex">{{ item.title }}</a-checkbox></a-col>
                  </template>
                </template>
              </a-row>
            </a-checkbox-group>
          </a-card>
        </div>

        <a-icon slot="filterIcon" type='setting' :style="{ fontSize:'16px',color:  '#108ee9',width:'100%' }" />
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>
      </a-table>
    </div>

  </a-card>
</template>

<script>
  import '@/assets/less/TableExpand.less'
  import {mixinDevice} from '@/utils/mixin'
  import {queryCompanyFlagNum, queryVOCsColumns} from "../../requestAction/request";
  import AreaLinkSelect from '../component/AreaLinkSelect'
  import {tableMixin} from "../mixin/tableMixin";
  import {mapButton} from "../constants/colorConstant";

  export default {
    name: "VOCsCurrentTrList",
    mixins:[mixinDevice,tableMixin],
    components: {
      AreaLinkSelect
    },
    data () {
      return {
        scroll:{},
        description: '实时监控（VOCs）',
        items:[],
        companyFlagNum:{},
        siteOriginal:[],
        areaHandler:'',
        companyNames:[],
        companyNameOriginal:[],
        companyIds:this.companyId,
        siteArea:'',
        allowClear:true,
        name:'',
        checkedRadio:"total",
        queryParam: {
          companyIds:this.$store.getters.userInfo.companyIds.join(','),
          tableName:"voc_current_tr_",
          dataStatus:"total"
        },
        siteType:2,
        //表头
        columns:[],
        //列设置
        settingColumns:[],
        defColumns:[],
        //列定义
        flexColumns: [
          {
            title: '',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:this.calcIndex,
            scopedSlots: {
              filterDropdown: 'filterDropdown',
              filterIcon: 'filterIcon'},
            fixed:'left'
          },
          {
            title:'企业名称',
            align:"center",
            dataIndex: 'company_name',
            fixed:'left',
            width:300,
          },
          {
            title:'监测点名称',
            align:"center",
            dataIndex: 'site_name',
            fixed:'left',
            width:300,
          },
          {
            title:'时间',
            align:"center",
            dataIndex: 'DATA_TIME',
            fixed:'left',
            width:200,
            customRender:function (text) {
              return text;
              // return !text?"":(text.length>10?text.substr(0,10):text)
            }
          }
        ],
        url: {
          list: "/onlineMonitor/list",
        },
        dictOptions:{},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      searchReset(){

      },
      searchCompanyFlagNum(){
        debugger
        let that = this;
        that.queryParam.type = that.siteType;
        queryCompanyFlagNum(that.queryParam).then(res => {
          if(res.result){
            that.companyFlagNum = res.result;
          }
        })

      },
      renderEmpty(val){
        if(val==null||val==='')
          return 'NA'
        return val
      },
      getColumns(){
        let that = this;
        that.queryParam.type = that.siteType;
        queryVOCsColumns(that.queryParam).then(res => {
          if(res.result){
            that.defColumns=[];
            for(var i=0;i<this.flexColumns.length;i++){
              that.defColumns.push(this.flexColumns[i]);
            }
            that.scroll={x:250*res.result.length};
            for(var i=0;i<res.result.length;i++){
              that.defColumns.push(res.result[i]);
            }
          }
          that.initColumns();
        })
      },
      searchQuery() {
        this.getColumns();
        this.searchCompanyFlagNum();
        this.creatData(1);
      },
      check(val){
        let _this = this;
        this.checkedRadio = val;
        this.queryParam.dataStatus = val;
        this.creatData(1);
      },
    },
    created() {
      this.getColumns();
      this.queryCompanyAndSite();
      this.searchCompanyFlagNum();
      this.creatData(1);
    },
    computed:{
      radio_check(){
        return {
          color:'white',
          background: mapButton[this.checkedRadio],
          'border-color': mapButton[this.checkedRadio],
        }
      },
    },
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
  .radio-column{
    font-size:13px;
    border-width: 1px ;
    width: 80px;
    height: 30px;
    border-radius: 4px;
    margin-left: 10px;
    padding: 0;
    text-align: center;
  }
  .total{
    color: #0098A1;
    background: #0098A1;
    border-color:#0098A1;
  }
  .normal{
    color: #00D660;;
    background: #00D660;
    border-color:#00D660;

  }
  .warn{
    color: #FF9810;
    background: #FF9810;
    border-color:#FF9810;

  }
  .standard{
    color: #FF1212;
    background: #FF1212;
    border-color:#FF1212;
  }
  .abnormal{
    color: #FF40FF;
    background: #FF40FF;
    border-color:#FF40FF;

  }
  .offline{
    color: #D2D2D2;
    background: #D2D2D2;
    border-color:#D2D2D2;

  }
</style>