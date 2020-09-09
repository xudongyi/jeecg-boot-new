<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="6" :md="6" :sm="24">
            <a-form-item label="所属区域">
              <area-link-select @change="areaChange" type="cascader" v-model="queryParam.area" show-search style="width: 100%" optionFilterProp="children"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="6" :md="6" :sm="24">
            <a-form-item label="企业名称">
              <a-select v-model="queryParam.companyId" @change="companyNameChange" :allowClear="allowClear" placeholder="请选择" show-search style="width: 100%" optionFilterProp="children">
                <!--                <a-select-option value="">请选择</a-select-option>-->
                <a-select-option v-for="item in companyNames" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="6" :md="6" :sm="24">
            <a-form-item label="监测点名称">
              <a-select v-model="queryParam.mn" :allowClear="allowClear" placeholder="请选择" show-search style="width: 100%" optionFilterProp="children">
                <!--                <a-select-option :value="companyIds">请选择</a-select-option>-->
                <a-select-option v-for="item in items" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="6" :md="6" :sm="24">
            <a-form-item label="主要污染物">
              <main-pollution-select pollutionType="0" @change="mainPollutionChange"></main-pollution-select>

            </a-form-item>
          </a-col>

        </a-row>
        <a-row>

          <a-col :xl="5" :lg="6" :md="7" :sm="24" >
            <a-form-item label="数据类型">
              <a-radio-group  button-style="solid" :value="queryParam.dataType" @change="dataTypeChange">
                <a-radio-button value="date">
                  日
                </a-radio-button>
                <a-radio-button value="month">
                  月
                </a-radio-button>
                <a-radio-button value="year">
                  年
                </a-radio-button>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :xl="10" :lg="11" :md="12" :sm="24">
            <a-form-item label="日期">
              <a-date-picker :show-time="dateFormat[queryParam.dataType].showTime"
                             :format="dateFormat[queryParam.dataType].value" placeholder="请选择开始时间"
                             :valueFormat="dateFormat[queryParam.dataType].value"
                             :mode="queryParam.dataType"
                             class="query-group-cust"  v-model="queryParam.dataTime_begin"   />
              <span class="query-group-split-cust"></span>
              <a-date-picker :show-time="dateFormat[queryParam.dataType].showTime"
                             :format="dateFormat[queryParam.dataType].value" placeholder="请选择结束时间"
                             :valueFormat="dateFormat[queryParam.dataType].value"
                             :mode="queryParam.dataType"
                             class="query-group-cust"  v-model="queryParam.dataTime_end"  />
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;margin-left: 20px" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <!--              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>-->
              <a-button type="primary" icon="download" style="margin-left: 8px" @click="handleExportXls('报')">导出</a-button>
            </span>
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

        <a-icon slot="filterIcon" type='setting'
                :style="{ fontSize:'16px',color:  '#108ee9',width:'100%' }" />

      </a-table>
    </div>

  </a-card>
</template>

<script>

  import AreaLinkSelect from '../component/AreaLinkSelect'
  import {tableMixin} from "../mixin/tableMixin";
  import {queryWaterColumns} from "../../requestAction/request";
  import moment from 'moment';
  import mainPollutionSelect from "../component/mainPollutionSelect";
  import {getAction} from "../../../api/manage";

  export default {
    name: "WaterHistory",
    mixins:[tableMixin],
    components: {
      AreaLinkSelect,
      mainPollutionSelect
    },
    data(){
      return {
        queryParam: {
          dataTime_begin:moment().days(moment().days()-6).format("YYYY-MM-DD"),
          dataTime_end:moment().format("YYYY-MM-DD"),
          companyIds:this.$store.getters.userInfo.companyIds.join(','),
          dataType:'date',
        },
        items:[],
        companyNames:[],
        companyNameOriginal:[],
        siteArea:'',
        name:'',
        allowClear:false,
        showDate:false,
        dateFormat:{
          'month':{value:"YYYY-MM",showTime:false},
          'year':{value:"YYYY",showTime:false},
          'date':{value:"YYYY-MM-DD",showTime:false},
        },
        //表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:this.calcIndex,
            scopedSlots: {
              filterDropdown: 'filterDropdown',
              filterIcon: 'filterIcon'},
          },
          {
            title:'企业名称',
            align:"center",
            dataIndex: 'companyName',
          },
          {
            title:'监测点名称',
            align:"center",
            dataIndex: 'siteName',
          },
          {
            title:'数据时间',
            align:"center",
            dataIndex: 'dataTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'监测因子',
            align:"center",
            dataIndex: 'code'
          },
          {
            title:'监测值',
            align:"center",
            dataIndex: 'value'
          },
          {
            title:'标准值',
            align:"center",
            dataIndex: 'standardValue'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'unit'
          },
          {
            title:'超标倍数',
            align:"center",
            dataIndex: 'overTimes'
          },
        ],
        //列定义

        siteType:0,
        url:{
          list:'/psoam/waterCurrentOverproof/list',
          exportXlsUrl:'/psoam/waterCurrentOverproof/exportXls',
        }

      }
    },
    methods: {
      dataTimeRender(text) {
        return moment(text).format(this.dateFormat[this.queryParam.dataType].value)
      },
      handleDateChange(mom, dateStr) {
        console.log(mom, dateStr)
        console.log(this.dateFormat[this.queryParam.dataType].value, this.queryParam.dataTime_begin)
      },
      handleDateChange2(mom, dateStr) {
        console.log(mom, dateStr)
        this.queryParam.dataTime_end = dateStr
      },
      mainPollutionChange(value) {
        console.log(value),
        this.queryParam.pollutionCode = value.target.value;
      },
      dataTypeChange(value) {
        console.log(value)
        this.queryParam.dataType = value.target.value;
        // this.loadData(1);
      },
      //查询数据
      searchQuery() {
        this.queryData()
      },
      searchReset() {
        this.loadData(1);
      },
      queryData(arg){
        let param = this.getQueryParams();

      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        //如果
        if(this.queryParam.dataType==='month'){
          params.dataTime_begin =  params.dataTime_begin+"-01";
          params.dataTime_end = moment( params.dataTime_end,'YYYY-MM').endOf('month').format("YYYY-MM-DD");
        }
        if(this.queryParam.dataType==='year'){
          params.dataTime_begin =  params.dataTime_begin+"-01-01";
          params.dataTime_end = moment( params.dataTime_end,'YYYY').endOf('year').format("YYYY-MM-DD");
        }
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dealColumns(res.result.columns);
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
            console.log(this.dataSource)
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
        //对param
      }

    },

    created(){
      //先查询表头
      // this.getColumns();
      //级联的行政区域
      this.queryCompanyAndSite();
      this.queryData(1);
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