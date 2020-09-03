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
  import Vue from 'vue'
  import AreaLinkSelect from '../component/AreaLinkSelect'
  import JDate from '@/components/jeecg/JDate.vue'
  import {tableMixin} from "../mixin/tableMixin";
  import {queryVocsColumns} from "../../requestAction/request";

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
            companyIds:this.$store.getters.userInfo.companyIds.join(',')
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
            }
          ],
          siteType:2,
        }
      },
      methods:{
        searchQuery(){

        },
        searchReset(){
          this.queryParam = {companyIds:this.$store.getters.userInfo.companyIds.join(',')};
          this.loadData(1);
        },
        handleExportXls(){

        },

        getColumns(){
          let _this = this;
          _this.queryParam.type = 2;
          queryVocsColumns(_this.queryParam).then(res => {
            //console.log(res)
            if(res.result){
              _this.scroll={x:250*res.result.length};
              for(var i=0;i<res.result.length;i++){
                _this.defColumns.push(res.result[i]);
              }
            }
            _this.initColumns();
          })
        },
      },
      mounted(){
        this.getColumns();
        this.queryCompanyAndSite();
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