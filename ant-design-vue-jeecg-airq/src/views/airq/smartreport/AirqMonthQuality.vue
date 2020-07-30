<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
            <a-form-item label="所属区域">
              <area-link-select @change="areaChange" type="cascader" v-model="queryParam.area" placeholder="请选择所属区域" show-search style="width: 100%" optionFilterProp="children"/>
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
            <a-form-item label="监测点位名称">
              <a-select v-model="queryParam.mn" show-search style="width: 100%" optionFilterProp="children" placeholder="请选择监测点位名称">
                <!--                <a-select-option :value="companyIds">请选择</a-select-option>-->
                <a-select-option v-for="item in items" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="11" :md="12" :sm="24">
            <a-form-item label="数据时间">
              <a-range-picker
              :placeholder="['开始月份','结束月份']"
              format="YYYY-MM"
              :value="timeValue"
              :mode="['month','month']"
              @change="searchTimeChange"
              @panelChange="handlePanelChange"
            />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="toSearchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button type="primary" icon="export" style="margin-left: 8px" @click="handleExportXls('空气质量年报')">导出</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        bordered
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="{ x: 2000 }"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <span slot="airLevel" slot-scope="text,record">
          <a-tag :color="tagColors[record.level]" style="width: 90%">
            {{ text}}
          </a-tag>
         </span>

      </a-table>
    </div>

  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import JDate from '@/components/jeecg/JDate.vue'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import {loadAreaDate} from '../component/areaUtil'
  import AreaHandler from "../component/AreaHandler"
  import {querySiteNameAndMn} from "../../requestAction/request";
  import Vue from 'vue'
  import AreaLinkSelect from '../component/AreaLinkSelect'

  export default {
    name: "AirqMonthQuality",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      JDictSelectTag,
      JDate,
      AreaLinkSelect
    },
    data () {
      return {
        description: '管理页面',
        searchTime:[],
        timeValue:[],
        tagColors:{
          1:'#00E400',
          2:'#EFD600',
          3:'#FF7E00',
          4:'#FF0000',
          5:'#99004C',
          6:'#7E0023',
        },
        queryParam: {
          companyIds:this.$store.getters.userInfo.companyIds.join(',')
        },
        items:[],
        siteOriginal:[],
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:this.calcIndex,
            fixed:'left'
          },
          {
            title:'行政区域',
            align:"center",
            dataIndex: 'area',
            customRender:this.getAreaByCode,
            fixed:'left',
            width:150
          },
          {
            title:'监测点位名称',
            align:"center",
            dataIndex: 'siteName',
            fixed:'left',
            width:150
          },
          {
            title:'数据时间',
            align:"center",
            dataIndex: 'month',
            fixed:'left',
            width: 100
          },
          {
            title:'综合指数',
            align:"center",
            dataIndex: 'totalI'
          },
          {
            title:'首要污染物',
            align:"center",
            dataIndex: 'meaning'
          },
          {
            title:'空气质量指数级别',
            align:"center",
            dataIndex: 'level',
            customRender:function (text){
              if(text === '1'){
                return "一级";
              }else if(text==='2'){
                return "二级";
              }else if(text==='3'){
                return "三级";
              }else if(text==='4'){
                return "四级";
              }else if(text==='5'){
                return "五级";
              }else
                return "六级";
            },
            sorter: (a, b) => a.level - b.level
          },
          {
            title:'空气质量指数类别',
            align:"center",
            dataIndex: 'level_dictText',
            key: 'airLevel',
            scopedSlots: { customRender:'airLevel'},
            sorter: (a, b) => a.level - b.level
          },
          {
            title:() => {
              return (
                <div>
                  <span>二氧化硫(SO2)</span><br/>
                  <span>年平均浓度(μg/m3)</span>
                </div>
              )},
            align:"center",
            dataIndex: 'a21026Avg',
            sorter: (a, b) => a.a21026Avg - b.a21026Avg
          },
          {
            title:() => {
              return (
                <div>
                  <span>二氧化氮(NO2)</span><br/>
                  <span>年平均浓度(μg/m3)</span>
                </div>
              )},
            align:"center",
            dataIndex: 'a21004Avg',
            sorter: (a, b) => a.a21004Avg - b.a21004Avg
          },
          {
            title:() => {
              return (
                <div>
                  <span>PM10</span><br/>
                  <span>年平均浓度(μg/m3)</span>
                </div>
              )},
            align:"center",
            dataIndex: 'a34002Avg',
            sorter: (a, b) => a.a34002Avg - b.a34002Avg
          },
          {
            title:() => {
              return (
                <div>
                  <span>一氧化碳(CO)</span><br/>
                  <span>年平均浓度(μg/m3)</span>
                </div>
              )},
            align:"center",
            dataIndex: 'a21005Avg',
            sorter: (a, b) => a.a21005Avg - b.a21005Avg
          },
          {
            title:() => {
              return (
                <div>
                  <span>臭氧(O3)</span><br/>
                  <span>年平均浓度(μg/m3)</span>
                </div>
              )},
            align:"center",
            dataIndex: 'a05024Avg',
            sorter: (a, b) => a.a05024Avg - b.a05024Avg
          },
          {
            title:() => {
              return (
                <div>
                  <span>PM2.5</span><br/>
                  <span>年平均浓度(μg/m3)</span>
                </div>
              )},
            align:"center",
            dataIndex: 'a34004Avg',
            sorter: (a, b) => a.a34004Avg - b.a34004Avg
          }
        ],
        url: {
          list: "/month/airqMonth/queryAirqMonthQuality",
          exportXlsUrl: "/month/airqMonth/exportAirqMonthQuality",
          //importExcelUrl: "/month/airqMonth/importExcel",
        },
        dictOptions:{},
        areaHandler:''
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
        loadAreaDate()
      },
      toSearchReset() {
        this.queryParam = {companyIds:this.$store.getters.userInfo.companyIds.join(',')};
        this.timeValue = [];
        this.searchTime = [];
        this.loadData(1);
      },
      initArea(){
        this.areaHandler = new AreaHandler()
      },
      areaChange(val){
        let _this = this
        _this.items=[]
        _this.siteOriginal.forEach(e=>{
          if(e.area === val){
            _this.items.push(e)
          }
        })
      },
      getAreaByCode(text){
        if(!text)
          return ''
        //初始化
        if(this.areaHandler==='')
        {
          this.initArea()
        }
        let arr = [];
        this.areaHandler.getAreaBycode(text,arr);
        return arr[0]+arr[1]+arr[2]
      },
      calcIndex: function (t,r,index) {
        return parseInt(index)+1+(this.ipagination.current-1)*this.ipagination.pageSize;
      },
      searchTimeChange(value,dataStr) {
        this.timeValue = value;
        this.searchTime = this.parseDate(value);
        this.queryParam.searchTime = this.parseDate(value);
      },
      handlePanelChange(value, mode) {
        debugger
        this.timeValue = value;
        this.searchTime = this.parseDate(value);
        this.queryParam.searchTime = this.parseDate(value).join(",");
      },
      parseDate(value){
        return  [value[0].format("YYYY-MM"),value[1].format("YYYY-MM")];
      },
    },
    mounted(){
      let that = this;
      querySiteNameAndMn({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res)=>{
        if(res.success){
          that.siteOriginal = res.result;
          that.items = res.result;
        }
      })
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
<style>
  .ant-badge-dot{
    top: 50%;
    right: 105%;
  }
</style>