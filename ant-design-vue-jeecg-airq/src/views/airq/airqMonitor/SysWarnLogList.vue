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
              <a-select v-model="queryParam.id" show-search style="width: 100%" optionFilterProp="children" placeholder="请选择监测点位名称">
                <!--                <a-select-option :value="companyIds">请选择</a-select-option>-->
                <a-select-option v-for="item in items" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="10" :lg="11" :md="12" :sm="24">
            <a-form-item label="数据时间">
              <j-date date-format="YYYY-MM-DD" placeholder="请选择开始时间" class="query-group-cust" v-model="queryParam.warnTime_begin"></j-date>
              <span class="query-group-split-cust"></span>
              <j-date date-format="YYYY-MM-DD" placeholder="请选择结束时间" class="query-group-cust" v-model="queryParam.warnTime_end"></j-date>
            </a-form-item>
          </a-col>
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <a-form-item label="报警级别">
              <j-search-select-tag placeholder="请选择报警级别" v-model="queryParam.flag" dict="warnFlag"/>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="toSearchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button type="primary" icon="download" style="margin-left: 8px" @click="handleExportXls('报警日志表')">导出</a-button>
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
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <span slot="flag" slot-scope="text,record">
          <a-tag :color="colors[record.flag]">{{text}}</a-tag>
        </span>

      </a-table>
    </div>

  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
  import JDate from '@/components/jeecg/JDate.vue'
  import {loadAreaDate} from '../component/areaUtil'
  import AreaHandler from "../component/AreaHandler"
  import {querySiteNameAndId} from "../../requestAction/request";
  import Vue from 'vue'
  import AreaLinkSelect from '../component/AreaLinkSelect'

  export default {
    name: "SysWarnLogList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      JSearchSelectTag,
      JDate,
      AreaLinkSelect
    },
    data () {
      return {
        description: '报警日志表管理页面',
        colors:{
          0:'#FF2323',
          1:'#A1A1A1',
          2:'#CD66C2',
          3:'#FF9710',
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
            customRender:this.calcIndex
          },
          {
            title:'行政区域',
            align:"center",
            dataIndex: 'area',
            customRender:this.getAreaByCode
          },
          {
            title:'监测点位名称',
            align:"center",
            dataIndex: 'siteName'
          },
          {
            title:'报警类型',
            align:"center",
            dataIndex: 'flag_dictText',
            key: 'flag',
            scopedSlots: { customRender: 'flag' },
          },
          {
            title:'报警内容',
            align:"center",
            dataIndex: 'content'
          },
          {
            title:'报警时间',
            align:"center",
            dataIndex: 'warnTime'
          }
        ],
        url: {
          list: "/warn/sysWarnLog/warnLogList",
          exportXlsUrl: "/warn/sysWarnLog/exportSysWarnLog",
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
      },
      toSearchReset() {
        this.queryParam = {companyIds:this.$store.getters.userInfo.companyIds.join(',')};
        this.loadData(1);
      },
      initArea(){
        this.areaHandler = new AreaHandler()
      },
      areaChange(val){
        let _this = this;
        _this.items=[];
        _this.siteOriginal.forEach(e=>{
          if(e.area === val){
            _this.items.push(e)
          }
        })
      },
      getAreaByCode(text){
        if(!text)
          return '';
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
      querySiteNameAndId({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res)=>{
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