<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="所属区域">
              <area-link-select @change="areaChange" type="cascader" v-model="queryParam.area" show-search style="width: 100%" optionFilterProp="children"/>
            </a-form-item>
          </a-col>
          <a-col :xl="7" :lg="7" :md="8" :sm="24">
            <a-form-item label="企业名称">
              <a-select v-model="queryParam.companyId" @change="companyNameChange" :allowClear="allowClear" placeholder="请选择" show-search style="width: 100%" optionFilterProp="children">
<!--                <a-select-option value="">请选择</a-select-option>-->
                <a-select-option v-for="item in companyNames" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="7" :lg="7" :md="8" :sm="24">
            <a-form-item label="监测点名称">
              <a-select v-model="queryParam.mn" :allowClear="allowClear" placeholder="请选择" show-search style="width: 100%" optionFilterProp="children">
<!--                <a-select-option :value="companyIds">请选择</a-select-option>-->
                <a-select-option v-for="item in items" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <a-form-item label="数据类型">
              <a-select v-model="queryParam.dataType" :allowClear="allowClear" placeholder="请选择"
                        show-search style="width: 100%" optionFilterProp="children" @change="dataTypeChange">
<!--                <a-select-option :value="companyIds">请选择</a-select-option>-->
                <a-select-option v-for="(item,index) in dataTypes" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

        </a-row>
        <a-row>
          <a-col :xl="10" :lg="11" :md="12" :sm="24" v-show="showDate">
            <a-form-item label="日期">
              <a-date-picker :show-time="dateFormat[queryParam.dataType].showTime" :format="dateFormat[queryParam.dataType].value" placeholder="请选择开始时间" :valueFormat="dateFormat[queryParam.dataType].value"
                             class="query-group-cust"  v-model="queryParam.dataTime_begin"   />
              <span class="query-group-split-cust"></span>
              <a-date-picker :show-time="dateFormat[queryParam.dataType].showTime" :format="dateFormat[queryParam.dataType].value" placeholder="请选择结束时间" :valueFormat="dateFormat[queryParam.dataType].value"
                             class="query-group-cust"  v-model="queryParam.dataTime_end"  />
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;margin-left: 20px" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
<!--              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>-->
              <a-button type="primary" icon="download" style="margin-left: 8px" @click="handleExportXls('历史数据(VOCs)')">导出</a-button>
            </span>
          </a-col>

        </a-row>
        <a-row>
          <a-col :xl="12" :lg="7" :md="8" :sm="24">
            <a-checkbox-group v-model="checkedList" :options="plainOptions" :disabled="disabled[queryParam.dataType]" @change="plainChange" style="margin-left: 20px"/>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- table区域-begin -->
    <div>

      <a-table
        style="margin-top: 10px"
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="{ x: 3100 }"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <div slot="filterDropdown" >
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

      </a-table>
    </div>

  </a-card>
</template>

<script>
  import AreaLinkSelect from '../component/AreaLinkSelect'
  import JDate from '@/components/jeecg/JDate.vue'
  import {tableMixin} from "../mixin/tableMixin";
  import {queryVocsColumns} from "../../requestAction/request";
  import moment from 'moment'

  export default {
      name: "VocsHistoryData",
      mixins:[tableMixin],
      components: {
        AreaLinkSelect,
        JDate
      },
      data(){
        return {
          queryParam: {
            dataTime_begin:'',
            dataTime_end:'',
            companyIds:this.$store.getters.userInfo.companyIds.join(','),
            dataType:'day',
          },
          items:[],
          siteOriginal:[],
          areaHandler:'',
          companyNames:[],
          companyNameOriginal:[],
          siteArea:'',
          name:'',
          allowClear:true,
          showDate:false,
          dateFormat:{
            'realTime':{value:"YYYY-MM-DD HH:mm:ss",showTime:{ format: 'HH:mm:ss' }},
            'minute':{value:"YYYY-MM-DD HH:mm",showTime:{ format: 'HH:mm' }},
            'hour':{value:"YYYY-MM-DD HH",showTime:{ format: 'HH' }},
            'day':{value:"YYYY-MM-DD",showTime:false},
          },
          checkedList:[],
          plainOptions:[{label:'最大值',value:'max'}, {label:'最小值',value:'min'}, {label:'平均值',value:'avg'}],
          disabled:{'realTime':true,'minute':false,'hour':false,'day':false},
          //表头
          columns:[],
          //列设置
          settingColumns:[],
          //列定义
          fixedColumns: [
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
              width:300
            },
            {
              title:'监测点名称',
              align:"center",
              dataIndex: 'site_name',
              fixed:'left',
              width:300
            },
            {
              title:'日期',
              align:"center",
              dataIndex: 'data_time',
              fixed:'left',
              width:200,
              customRender:this.dataTimeRender
            }
          ],
          siteType:2,
          //列定义
          defColumns: [],
          url:{
            list:'/psoam/voc/queryVocColumns',
            exportXlsUrl:'/psoam/voc/exportVocHistory',
          }
        }
      },
      methods:{
        dataTimeRender (text) {
          return  moment(text).format(this.dateFormat[this.queryParam.dataType].value)
        },
        handleDateChange(mom,dateStr){
          console.log(mom,dateStr)
          console.log(this.dateFormat[this.queryParam.dataType].value,this.queryParam.dataTime_begin)
        },
        handleDateChange2(mom,dateStr){
          console.log(mom,dateStr)
          this.queryParam.dataTime_end=dateStr
        },

        dataTypeChange(){
          if(this.queryParam.dataType === 'realTime'){
            this.checkedList = [];
          }else {
            this.checkedList = ['max', 'min', 'avg',];
          }
          this.loadData(1);
        },
        searchQuery(){
          this.loadData()
        },
        searchReset(){
          this.loadData(1);
        },
        //处理一下scroll
        dealScroll(){
          if(this.queryParam.dataType!=='realTime'){

            this.scroll={x:100*(this.columns.length-4)};

          }else{
            this.scroll={x:100*(this.checkedList.length+1)*(this.columns.length-4)}
          }
        }

      },
      created(){
        //先查询表头
        // this.getColumns();
        //级联的行政区域
        this.queryCompanyAndSite();
        this.loadData(1);
      }
    }
</script>

<style scoped>

</style>
<style>
  .ant-table-filter-dropdown {
    width: 30%  ;
    margin-left: 10%  ;}
</style>