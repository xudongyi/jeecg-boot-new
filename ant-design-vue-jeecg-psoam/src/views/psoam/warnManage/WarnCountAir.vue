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
                        show-search style="width: 100%" optionFilterProp="children">
                <!--                <a-select-option :value="companyIds">请选择</a-select-option>-->
                <a-select-option v-for="(item,index) in dataTypes" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

        </a-row>
        <a-row>
          <a-col :xl="10" :lg="11" :md="12" :sm="24">
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
              <a-button type="primary" icon="download" style="margin-left: 8px" @click="handleExportXls('报警统计(废气)')">导出</a-button>
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
        class="j-table-force-nowrap"
        @change="handleTableChange">

      </a-table>
    </div>

  </a-card>
</template>

<script>
  import AreaLinkSelect from '../component/AreaLinkSelect'
  import JDate from '@/components/jeecg/JDate.vue'
  import {tableMixin} from "../mixin/tableMixin";
  import {queryWaterColumns} from "../../requestAction/request";
  import moment from 'moment'
  import {getAction} from "../../../api/manage";

  export default {
    name: "WarnCountAir",
    mixins:[tableMixin],
    components: {
      AreaLinkSelect,
      JDate
    },
    data() {
      return {
        queryParam: {
          companyIds: this.$store.getters.userInfo.companyIds.join(','),
          dataTime_begin: moment(moment().format("YYYY-MM-DD")).format("YYYY-MM-DD HH:mm:ss"),
          dataTime_end: moment().format("YYYY-MM-DD HH:mm:ss"),
          dataType:'realTime',
          type:1
        },
        items: [],
        siteOriginal: [],
        areaHandler: '',
        companyNames: [],
        companyNameOriginal: [],
        siteArea: '',
        name: '',
        allowClear: true,
        dateFormat:{
          'realTime':{value:"YYYY-MM-DD HH:mm:ss",showTime:{ format: 'HH:mm:ss' }},
          'minute':{value:"YYYY-MM-DD HH:mm",showTime:{ format: 'HH:mm' }},
          'hour':{value:"YYYY-MM-DD HH",showTime:{ format: 'HH' }},
          'day':{value:"YYYY-MM-DD",showTime:false},
        },
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: "center",
            customRender: this.calcIndex,
            scopedSlots: {
              filterDropdown: 'filterDropdown',
              filterIcon: 'filterIcon'
            },
          },
          {
            title: '企业名称',
            align: "center",
            dataIndex: 'companyName',
          },
          {
            title: '监测点名称',
            align: "center",
            dataIndex: 'siteName',
          },
          {
            title: '预警',
            align: "center",
            dataIndex: 'earlyWarnCount'
          },
          {
            title: '超标报警',
            align: "center",
            dataIndex: 'overWarnCount'
          },
          {
            title: '数据异常报警',
            align: "center",
            dataIndex: 'abnormalCount'
          },
          {
            title: '定值报警',
            align: "center",
            dataIndex: 'constantCount'
          }
        ],
        url:{
          list:'/warn/warnLog/warnCount',
          exportXlsUrl:'/warn/warnLog/exportWarnCount',
        }
      }
    },
    methods:{
      searchQuery(){
        this.queryData()
      },
      searchReset(){
        this.loadData(1);
      },
      queryData(arg){
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result;
            this.ipagination.total = res.result.length;

          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
        //对param
      },
    },
    created(){
      this.queryCompanyAndSite();
      this.queryData(1);
    }
  }
</script>

<style scoped>

</style>