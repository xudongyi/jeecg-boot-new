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
          <a-col :xl="10" :lg="11" :md="12" :sm="24">
            <a-form-item label="数据时间">
              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间" class="query-group-cust" v-model="queryParam.dataTime_begin"></j-date>
              <span class="query-group-split-cust"></span>
              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间" class="query-group-cust" v-model="queryParam.dataTime_end"></j-date>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="4" :lg="7" :md="8" :sm="24">
              <a-form-item label="等级">
                <j-dict-select-tag v-model="queryParam.level" placeholder="请选择状态"  dictCode="airLevel"/>
              </a-form-item>
            </a-col>
            <a-col :xl="4" :lg="7" :md="8" :sm="24">
              <a-form-item label="来源">
                <j-dict-select-tag v-model="queryParam.state" placeholder="请选择状态"  dictCode="dataFrom"/>
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="toSearchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
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
        :scroll="{ x: 2800 }"
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
    name: "SiteQualityEvaluate",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      JDictSelectTag,
      JDate,
      AreaLinkSelect
    },
    data () {
      return {
        description: 'airq_hour管理页面',
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
            width:100
          },
          {
            title:'监测点位名称',
            align:"center",
            dataIndex: 'siteName',
            fixed:'left',
            width:100
          },
          {
            title:'数据时间',
            align:"center",
            dataIndex: 'dataTime',
            fixed:'left',
            width:100
          },
          {
            title:'AQI',
            align:"center",
            dataIndex: 'aqi',
            width:100
          },
          {
            title:'首要污染物',
            align:"center",
            dataIndex: 'meaning',
            width:150
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
            width:150
          },
          {
            title:'空气质量指数类别',
            align:"center",
            dataIndex: 'level_dictText',
            key: 'airLevel',
            scopedSlots: { customRender:'airLevel'},
            width:150
          },
          {
            title:'数据来源',
            align:"center",
            dataIndex: 'state',
            customRender:function (text){
              if(text === 0){
                return "自动采集";
              }else if(text===3){
                return "手动采集";
              }
            },
            width:150
          },
          {
            title:'对健康影响情况',
            align:"center",
            dataIndex: 'advice',
            width:500
          },
          {
            title:'建议采取的措施',
            align:"center",
            dataIndex: 'levelContent',
            width:500
          }
        ],
        url: {
          list: "/hour/airqHour/querySiteQualityEvaluate",
        },
        dictOptions:{},
        areaHandler:''
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
      toSearchReset() {
        this.queryParam = {companyIds:this.$store.getters.userInfo.companyIds.join(',')};
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
    right: 103%;
  }
</style>