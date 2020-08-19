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
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
            <a-form-item label="数据类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-radio-group v-model="queryParam.dataType" button-style="solid" @change="dataTypeChange" style="width: 100%">
                <a-radio-button value="day" style="width: 33.3%;text-align:center;">
                  日
                </a-radio-button>
                <a-radio-button value="month" style="width: 33.3%;text-align:center;">
                  月
                </a-radio-button>
                <a-radio-button value="year" style="width: 33.3%;text-align:center;">
                  年
                </a-radio-button>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="11" :md="12" :sm="24">
            <a-form-item label="数据时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-date-picker
                :placeholder="placeholder"
                :format="format"
                :mode="mode2"
                :value="queryParam.searchTime"
                @change="searchTimeChange"
                @panelChange="handlePanelChange"
                :showTime="showTime"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="toSearchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button type="primary" icon="download" style="margin-left: 8px" @click="handleExportXls('站点质量排名')">导出</a-button>
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
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="{ x: 2600 }"
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
    name: "SiteQualityRank",
    //混入的create优先级最高，没办法在他查询之前做预处理
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      JDictSelectTag,
      JDate,
      AreaLinkSelect
    },
    data () {
      return {
        mode2: "date",
        placeholder:"日期",
        format:"YYYY-MM-DD",
        showTime:{format:'YYYY-MM-DD'},
        labelCol: {
          xs: {span: 24},
          sm: {span: 6},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        description: '站点质量排名页面',
        tagColors:{
          1:'#00E400',
          2:'#ffe400',
          3:'#FF7E00',
          4:'#FF0000',
          5:'#99004C',
          6:'#7E0023',
        },
        queryParam: {
          companyIds:this.$store.getters.userInfo.companyIds.join(','),
          dataType:'day',
          searchTime:''
        },
        items:[],
        siteOriginal:[],
        columns:[],

        // 表头
        columns1: [
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
            width:160
          },
          {
            title:'监测点位名称',
            align:"center",
            dataIndex: 'siteName',
            fixed:'left',
            width:160
          },
          {
            title:'数据时间',
            align:"center",
            dataIndex: 'dataTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            },
            fixed:'left',
            width:160
          },
          {
            title:'AQI',
            align:"center",
            dataIndex: 'aqi',
            sorter: (a, b) => a.aqi - b.aqi
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
            title:'站点排名',
            align:"center",
            dataIndex: 'rank',
            sorter: (a, b) => a.rank - b.rank
          },
          {
            title:'SO2 μg/m3',
            align:"center",
            dataIndex: 'a21026Avg',
            sorter: (a, b) => a.a21026Avg - b.a21026Avg
          },
          {
            title:'NO2 μg/m3',
            align:"center",
            dataIndex: 'a21004Avg',
            sorter: (a, b) => a.a21004Avg - b.a21004Avg
          },
          {
            title:'PM10(1h)μg/m3',
            align:"center",
            dataIndex: 'a3400201Avg',
            sorter: (a, b) => a.a3400201Avg - b.a3400201Avg
          },
          {
            title:'PM10(24h)μg/m3',
            align:"center",
            dataIndex: 'a3400224Avg',
            sorter: (a, b) => a.a3400224Avg - b.a3400224Avg
          },
          {
            title:'COμg/m3',
            align:"center",
            dataIndex: 'a21005Avg',
            sorter: (a, b) => a.a21005Avg - b.a21005Avg
          },
          {
            title:'O3(1h)μg/m3',
            align:"center",
            dataIndex: 'a0502401Avg',
            sorter: (a, b) => a.a0502401Avg - b.a0502401Avg
          },
          {
            title:'O3(8h)μg/m3',
            align:"center",
            dataIndex: 'a0502408Avg',
            sorter: (a, b) => a.a0502408Avg - b.a0502408Avg
          },
          {
            title:'PM2.5(1h)μg/m3',
            align:"center",
            dataIndex: 'a3400401Avg',
            sorter: (a, b) => a.a3400401Avg - b.a3400401Avg
          },
          {
            title:'PM2.5(24h)μg/m3',
            align:"center",
            dataIndex: 'a3400424Avg',
            sorter: (a, b) => a.a3400424Avg - b.a3400424Avg
          }
        ],
        columns2:[
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
            width:150
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
            title:'站点排名',
            align:"center",
            dataIndex: 'rank',
            sorter: (a, b) => a.rank - b.rank
          },
          {
            title:() => {
              return (
                <div>
                  <span>二氧化硫(SO2)</span><br/>
                  <span>月平均浓度(μg/m3)</span>
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
                  <span>月平均浓度(μg/m3)</span>
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
                  <span>月平均浓度(μg/m3)</span>
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
                  <span>月平均浓度(μg/m3)</span>
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
                  <span>月平均浓度(μg/m3)</span>
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
                  <span>月平均浓度(μg/m3)</span>
                </div>
              )},
            align:"center",
            dataIndex: 'a34004Avg',
            sorter: (a, b) => a.a34004Avg - b.a34004Avg
          }
        ],
        columns3:[
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
            customRender: this.getAreaByCode,
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
            title:'年份',
            align:"center",
            dataIndex: 'year',
            fixed:'left',
            width:150
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
              }else if(text==='6'){
                return "六级";
              }
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
            title:'站点排名',
            align:"center",
            dataIndex: 'rank',
            sorter: (a, b) => a.rank - b.rank
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
          list: "/day/airqDay/querySiteDay",
          exportXlsUrl: "/day/airqDay/exportSiteDay"
        },
        dictOptions:{},
        areaHandler:'',

      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
        loadAreaDate()
      },
      dataTypeChange(e){
        const dataType = e.target.value;
        this.dataType = dataType;
        this.queryParam.searchTime = null;
        if(dataType==="day"){
          this.mode2= "date";
          this.format="YYYY-MM-DD";
          this.placeholder="日期";
          this.queryParam.dataType = "day";
          this.showTime = {format:'YYYY-MM-DD'};
          this.columns = this.columns1;
        }else if(dataType==="month"){
          this.mode2 = "month";
          this.placeholder="月份";
          this.format="YYYY-MM";
          this.queryParam.dataType = "month";
          this.showTime = {format:'YYYY-MM'};
          this.columns = this.columns2;
        }else if(dataType==="year"){
          this.mode2 = "year";
          this.placeholder="年份";
          this.format="YYYY";
          this.queryParam.dataType = "year";
          this.showTime = {format:'YYYY'};
          this.columns = this.columns3;
        }
        this.loadData(1);
      },
      searchTimeChange(value,dataStr) {
        if(value)
          this.queryParam.searchTime = this.parseDate(value);
        else
          this.queryParam.searchTime =''
      },
      handlePanelChange(value, mode) {
        if(value)
          this.queryParam.searchTime = this.parseDate(value);
        else
          this.queryParam.searchTime =''
      },
      parseDate(value){
        return  value.format(this.format);
      },
      toSearchReset() {
        this.queryParam = {companyIds:this.$store.getters.userInfo.companyIds.join(','),dataType:'day'};
        this.columns =this.columns1;
        this.mode2= "date";
        this.format="YYYY-MM-DD";
        this.placeholder="日期";
        this.queryParam.dataType = "day";
        this.showTime = {format:'YYYY-MM-DD'};
        this.columns = this.columns1;
        this.loadData(1);
      },
      initArea(){
        this.areaHandler = new AreaHandler()
      },
      areaChange(val){
        let _this = this
        _this.items=[]
        if(_this.siteOriginal)
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
    },
    mounted(){
      let that = this;
      that.columns = that.columns1
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
    right: 103%;
  }
</style>