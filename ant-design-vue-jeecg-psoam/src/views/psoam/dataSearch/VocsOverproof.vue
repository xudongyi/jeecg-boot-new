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
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
            <a-form-item label="主要污染物">
              <a-select v-model="queryParam.code" :allowClear="allowClear" placeholder="请选择" show-search style="width: 100%" optionFilterProp="children">
                <!--                <a-select-option :value="companyIds">请选择</a-select-option>-->
                <a-select-option v-for="item in pollutionCode" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

        </a-row>
        <a-row>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="数据类型">
              <a-radio-group v-model="queryParam.dataType" default-value="day" button-style="solid" @change="dataTypeChange" style="width: 100%">
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
          <a-col :xl="10" :lg="11" :md="12" :sm="24" style="margin-left: 20px">
            <a-form-item label="日期">
              <a-date-picker :placeholder="placeholder" :format="format" :mode="mode2" :value="queryParam.searchTimeBegin" @change="searchTimeBeginChange"
                             @panelChange="handlePanelChangeBegin" :showTime="showTime"/>
              <span class="query-group-split-cust"></span>
              <a-date-picker :placeholder="placeholder" :format="format" :mode="mode2" :value="queryParam.searchTimeEnd" @change="searchTimeEndChange"
                             @panelChange="handlePanelChangeEnd" :showTime="showTime"/>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
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
  import {loadAreaDate} from '../component/areaUtil'
  import AreaHandler from "../component/AreaHandler"
  import {querySiteNameAndMn,queryCompanyName,queryPollutionCode} from "../../requestAction/request";
  import Vue from 'vue'
  import AreaLinkSelect from '../component/AreaLinkSelect'
  import JDate from '@/components/jeecg/JDate.vue'
  import moment from 'moment'

    export default {
      name: "VocsOverproof",
      components: {
        AreaLinkSelect,
        JDate
      },
      data() {
        return {
          queryParam: {
            companyIds: this.$store.getters.userInfo.companyIds.join(','),
            searchTimeBegin:moment().days(moment().days()-7).format("YYYY-MM-DD"),
            searchTimeEnd:moment().format("YYYY-MM-DD")
          },
          items: [],
          siteOriginal: [],
          areaHandler: '',
          companyNames: [],
          companyNameOriginal: [],
          pollutionCode:[],
          companyIds: this.companyId,
          siteArea: '',
          name: '',
          allowClear: true,
          mode2: "date",
          placeholder:"日期",
          format:"YYYY-MM-DD",
          showTime:{format:'YYYY-MM-DD'},
          // 表头
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
          dataSource:[],
          /* 分页参数 */
          ipagination:{
            current: 1,
            pageSize: 10,
            pageSizeOptions: ['10', '20', '30'],
            showTotal: (total, range) => {
              return range[0] + "-" + range[1] + " 共" + total + "条"
            },
            showQuickJumper: true,
            showSizeChanger: true,
            total: 0
          },
          /* table加载状态 */
          loading:false,
        }
      },
      methods: {
        handleTableChange(pagination, filters, sorter) {
          //分页、排序、筛选变化时触发
          //TODO 筛选
          this.ipagination = pagination;

        },
        initDictConfig() {
          loadAreaDate()
        },
        initArea() {
          this.areaHandler = new AreaHandler()
        },
        getAreaByCode(text) {
          if (!text)
            return '';
          //初始化
          if (this.areaHandler === '') {
            this.initArea()
          }
          let arr = [];
          this.areaHandler.getAreaBycode(text, arr);
          return arr[0] + arr[1] + arr[2]
        },
        areaChange(val) {
          let _this = this;
          _this.items = [];
          if (this.queryParam.companyId != null) {
            _this.name = this.queryParam.companyId;
            _this.siteOriginal.forEach(e => {
              if (e.area === val && e.companyId === _this.name) {
                _this.items.push(e);
              }
            })
          } else if (this.queryParam.area != null) {
            _this.siteOriginal.forEach(e => {
              if (e.area === val) {
                _this.items.push(e);
              }
            })
          } else {
            _this.items = _this.siteOriginal;
          }
          //选择地区筛选公司名称
          _this.companyNames = [];
          if (this.queryParam.area != null) {
            _this.companyNameOriginal.forEach(b => {
              if (b.area === val) {
                _this.companyNames.push(b);
              }
            })
          } else {
            _this.companyNames = _this.companyNameOriginal;
          }
        },
        companyNameChange(val) {
          //console.log(this.queryParam.companyId)
          let _this = this;
          _this.items = [];
          if (this.queryParam.area != null) {
            _this.siteArea = this.queryParam.area;
            _this.siteOriginal.forEach(e => {
              if (e.companyId === val && e.area === _this.siteArea) {
                _this.items.push(e)
              }
            })
          } else if (this.queryParam.companyId != null) {

            _this.siteOriginal.forEach(e => {
              if (e.companyId === val) {
                _this.items.push(e)
              }
            })
          } else {
            _this.items = _this.siteOriginal;
          }

          if (this.queryParam.companyId != null) {
            _this.showDate = true;
          } else {
            _this.showDate = false;
          }
        },
        dataTypeChange(e){
          const dataType = e.target.value;
          this.dataType = dataType;
          this.queryParam.searchTimeBegin = null;
          this.queryParam.searchTimeEnd = null;
          if(dataType==="day"){
            this.mode2= "date";
            this.format="YYYY-MM-DD";
            this.placeholder="日期";
            this.queryParam.dataType = "day";
            this.showTime = {format:'YYYY-MM-DD'};
          }else if(dataType==="month"){
            this.mode2 = "month";
            this.placeholder="月份";
            this.format="YYYY-MM";
            this.queryParam.dataType = "month";
            this.showTime = {format:'YYYY-MM'};
          }else if(dataType==="year"){
            this.mode2 = "year";
            this.placeholder="年份";
            this.format="YYYY";
            this.queryParam.dataType = "year";
            this.showTime = {format:'YYYY'};
          }
          this.loadData(1);
        },
        searchTimeBeginChange(value,dataStr) {
          if(value)
            this.queryParam.searchTimeBegin = this.parseDate(value);
          else
            this.queryParam.searchTimeBegin =''
        },
        handlePanelChangeBegin(value, mode) {
          if(value)
            this.queryParam.searchTimeBegin = this.parseDate(value);
          else
            this.queryParam.searchTimeBegin =''
        },
        searchTimeEndChange(value,dataStr) {
          if(value)
            this.queryParam.searchTimeEnd = this.parseDate(value);
          else
            this.queryParam.searchTimeEnd =''
        },
        handlePanelChangeEnd(value, mode) {
          if(value)
            this.queryParam.searchTimeEnd = this.parseDate(value);
          else
            this.queryParam.searchTimeEnd =''
        },
        parseDate(value){
          return  value.format(this.format);
        },
        searchQuery(){

        },
        searchReset(){
          this.queryParam = {companyIds:this.$store.getters.userInfo.companyIds.join(',')};
          this.loadData(1);
        },
        handleExportXls(){

        },
        calcIndex: function (t,r,index) {
          return parseInt(index)+1+(this.ipagination.current-1)*this.ipagination.pageSize;
        },
      },
      mounted(){
        //this.initColumns();
        let that = this;
        querySiteNameAndMn({companyIds:this.$store.getters.userInfo.companyIds.join(','),siteType:2}).then((res)=>{
          if(res.success){
            //console.log("!!",res.result);
            that.siteOriginal = res.result;
            that.items = res.result;
          }
        });
        //查询企业名称
        queryCompanyName({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res) => {
          if(res.success){
            that.companyNameOriginal = res.result.companyNames;
            that.companyNames = res.result.companyNames;
          }
        });
        //查询污染因子
        queryPollutionCode({siteType: 2}).then((res)=>{
          if(res.success){
            that.pollutionCode = res.result;
          }
        })
      }
    }
</script>

<style scoped>

</style>