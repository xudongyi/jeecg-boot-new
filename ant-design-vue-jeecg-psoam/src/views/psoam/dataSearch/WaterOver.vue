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
              <a-select v-model="queryParam.typeIndex" :allowClear="allowClear" placeholder="请选择" show-search style="width: 100%" optionFilterProp="children">
                <!--                <a-select-option :value="companyIds">请选择</a-select-option>-->
                <a-select-option v-for="(item,index) in dataTypes" :key="item" :value="index">
                  {{item}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

        </a-row>
        <a-row>
          <a-col :xl="10" :lg="11" :md="12" :sm="24" v-show="showDate">
            <a-form-item label="日期">
              <j-date :show-time="true" :date-format="dateFormat[queryParam.typeIndex]" placeholder="请选择开始时间" class="query-group-cust" v-model="queryParam.dataTime_begin"></j-date>
              <span class="query-group-split-cust"></span>
              <j-date :show-time="true" :date-format="dateFormat[queryParam.typeIndex]" placeholder="请选择结束时间" class="query-group-cust" v-model="queryParam.dataTime_end" ></j-date>
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
        <a-row>
          <a-col :xl="12" :lg="7" :md="8" :sm="24">
            <a-checkbox-group v-model="checkedList" :options="plainOptions" :disabled="disabled[queryParam.typeIndex]" style="margin-left: 20px"/>
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
  import {loadAreaDate} from '../component/areaUtil'
  import AreaHandler from "../component/AreaHandler"
  import {querySiteNameAndMn,queryCompanyName} from "../../requestAction/request";
  import Vue from 'vue'
  import AreaLinkSelect from '../component/AreaLinkSelect'
  import JDate from '@/components/jeecg/JDate.vue'

  export default {
    name: "WaterOver",
    components: {
      AreaLinkSelect,
      JDate
    },
    data(){
      return {
        queryParam: {
          companyIds:this.$store.getters.userInfo.companyIds.join(',')
        },
        items:[],
        siteOriginal:[],
        areaHandler:'',
        companyNames:[],
        companyNameOriginal:[],
        companyIds:this.companyId,
        siteArea:'',
        name:'',
        dataTypes:["实时", "分钟", "小时", "日"],
        allowClear:true,
        showDate:false,
        dateFormat:{
          0:"YYYY-MM-DD HH:mm:ss",
          1:"YYYY-MM-DD HH:mm",
          2:"YYYY-MM-DD HH",
          3:"YYYY-MM-DD"
        },
        checkedList:[],
        plainOptions:['最大值', '最小值', '平均值'],
        disabled:{0:true,1:false,2:false,3:false},
        //表头
        columns:[],
        //列设置
        settingColumns:[],
        //列定义
        defColumns: [
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
            dataIndex: 'companyName',
            fixed:'left',
            width:160
          },
          {
            title:'监测点名称',
            align:"center",
            dataIndex: 'siteName',
            fixed:'left',
            width:160
          },
          {
            title:'日期',
            align:"center",
            dataIndex: 'dataTime',
            fixed:'left',
            width:160
          },
          {
            title:'空气质量指数类别',
            align:"center",
            dataIndex: 'level_dictText',
          },
          {
            title:'SO2 μg/m3',
            align:"center",
            dataIndex: 'a21026Avg',
          },
          {
            title:'NO2 μg/m3',
            align:"center",
            dataIndex: 'a21004Avg',
          },
          {
            title:'PM10(1h)μg/m3',
            align:"center",
            dataIndex: 'a3400201Avg',
          },
          {
            title:'PM10(24h)μg/m3',
            align:"center",
            dataIndex: 'a3400224Avg',
          },
          {
            title:'COμg/m3',
            align:"center",
            dataIndex: 'a21005Avg',
          },
          {
            title:'O3(1h)μg/m3',
            align:"center",
            dataIndex: 'a0502401Avg',
          },
          {
            title:'O3(8h)μg/m3',
            align:"center",
            dataIndex: 'a0502408Avg',
          },
          {
            title:'PM2.5(1h)μg/m3',
            align:"center",
            dataIndex: 'a3400401Avg',
          },
          {
            title:'PM2.5(24h)μg/m3',
            align:"center",
            dataIndex: 'a3400424Avg',
          },
          {
            title:'温度(°C)',
            align:"center",
            dataIndex: 'a01001Avg',
          },
          {
            title:'湿度(%)',
            align:"center",
            dataIndex: 'a01002Avg',
          },
          {
            title:'风速(m/s)',
            align:"center",
            dataIndex: 'a01007Avg',
          },
          {
            title:'风向',
            align:"center",
            dataIndex: 'a01008Avg_dictText',
          },
          {
            title:'气压(kPa)',
            align:"center",
            dataIndex: 'a01006Avg',
          }
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
    methods:{
      handleTableChange(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        //TODO 筛选
        this.ipagination = pagination;

      },
      initDictConfig(){
        loadAreaDate()
      },
      initArea(){
        this.areaHandler = new AreaHandler()
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
      areaChange(val){
        let _this = this;
        _this.items=[];
        if(this.queryParam.companyId != null){
          _this.name=this.queryParam.companyId;
          _this.siteOriginal.forEach(e=>{
            if(e.area === val && e.companyId === _this.name){
              _this.items.push(e);
            }
          })
        }else if(this.queryParam.area != null){
          _this.siteOriginal.forEach(e=>{
            if(e.area === val){
              _this.items.push(e);
            }
          })
        }else {
          _this.items = _this.siteOriginal;
        }
        //选择地区筛选公司名称
        _this.companyNames=[];
        if(this.queryParam.area != null){
          _this.companyNameOriginal.forEach(b=>{
            if(b.area === val){
              _this.companyNames.push(b);
            }
          })
        }else {
          _this.companyNames =_this.companyNameOriginal;
        }
      },
      companyNameChange(val){
        console.log(this.queryParam.companyId)
        let _this = this;
        _this.items=[];
        if(this.queryParam.area != null){
          _this.siteArea=this.queryParam.area;
          _this.siteOriginal.forEach(e=>{
            if(e.companyId === val && e.area === _this.siteArea){
              _this.items.push(e)
            }
          })
        }else if(this.queryParam.companyId != null){

          _this.siteOriginal.forEach(e=>{
            if(e.companyId === val){
              _this.items.push(e)
            }
          })
        }else {
          _this.items = _this.siteOriginal;
        }

        if(this.queryParam.companyId != null){
          _this.showDate = true;
        }else {
          _this.showDate = false;
        }
      },
      //列设置更改事件
      onColSettingsChange (checkedValues) {
        var key = this.$route.name+":colsettings";
        Vue.ls.set(key, checkedValues, 7 * 24 * 60 * 60 * 1000)
        this.settingColumns = checkedValues;
        const cols = this.defColumns.filter(item => {
          if(item.key =='rowIndex'|| item.dataIndex=='action'){
            return true
          }
          if (this.settingColumns.includes(item.dataIndex)) {
            return true
          }
          return false
        })
        this.columns =  cols;
      },
      initColumns(){
        //权限过滤（列权限控制时打开，修改第二个参数为授权码前缀）
        //this.defColumns = colAuthFilter(this.defColumns,'testdemo:');

        var key = this.$route.name+":colsettings";
        let colSettings= Vue.ls.get(key);
        if(colSettings==null||colSettings==undefined){
          let allSettingColumns = [];
          this.defColumns.forEach(function (item,i,array ) {
            allSettingColumns.push(item.dataIndex);
          })
          this.settingColumns = allSettingColumns;
          this.columns = this.defColumns;
        }else{
          this.settingColumns = colSettings;
          const cols = this.defColumns.filter(item => {
            if(item.key =='rowIndex'|| item.dataIndex=='action'){
              return true;
            }
            if (colSettings.includes(item.dataIndex)) {
              return true;
            }
            return false;
          })
          this.columns =  cols;
        }
      },
      searchQuery(){

      },
      searchReset(){
        this.queryParam = {companyIds:this.$store.getters.userInfo.companyIds.join(',')};
        this.loadData(1);
      },
      handleExportXls(){

      }
    },
    mounted(){
      this.initColumns();
      let that = this;
      querySiteNameAndMn({companyIds:this.$store.getters.userInfo.companyIds.join(','),siteType:0}).then((res)=>{
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
          console.log("!!",that.companyNames);
        }
      });
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