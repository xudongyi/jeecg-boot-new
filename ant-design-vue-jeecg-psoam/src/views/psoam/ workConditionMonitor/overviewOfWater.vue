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
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
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
                <a-select-option v-for="item in items" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="4" :lg="5" :md="6" :sm="24">
            <a-form-item label="阀门状态">
              <a-select v-model="queryParam.valveStatus" :allowClear="allowClear" placeholder="请选择"
                        show-search style="width: 100%" optionFilterProp="children" >
                <!--                <a-select-option :value="companyIds">请选择</a-select-option>-->
                <a-select-option v-for="(item,index) in valveStatus" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="2" :lg="3" :md="4" :sm="24">
            <span style="float: left;overflow: hidden;margin-left: 20px" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <!--              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>-->

            </span>
          </a-col>
        </a-row>
        <a-row>

          <a-col :xl="5" :lg="6" :md="7" :sm="24" >
            <a-form-item >
              <a-radio-group  button-style="solid" :value="queryParam.dataType" >
                <a-radio-button  value="date">
                  正常
                </a-radio-button>
                <a-radio-button value="month">
                  故障
                </a-radio-button>
                <a-radio-button value="month">
                  离线
                </a-radio-button>
                <a-radio-button value="month">
                  停运
                </a-radio-button>
              </a-radio-group>
            </a-form-item>
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
  import {queryWaterColumns} from "../../requestAction/request";
  import moment from 'moment'
  //废水工况总览
  export default {
    name: "overviewOfWater",
    mixins:[tableMixin],
    components: {
      AreaLinkSelect,
      JDate
    },
    data(){
      return {
        queryParam: {
          companyIds:this.$store.getters.userInfo.companyIds.join(','),
          valveStatus:'',
        },
        items:[],
        siteOriginal:[],
        areaHandler:'',
        companyNames:[],
        companyNameOriginal:[],
        siteArea:'',
        name:'',
        allowClear:false,
        showDate:false,

        valveStatus:[{key:'yes',value:'是'},{key:'no',value:'否'}],
        //表头
        columns:[],
        //列设置
        settingColumns:[],

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
            width:300,
          },
          {
            title:'监测点名称',
            align:"center",
            dataIndex: 'site_name',
            fixed:'left',
            width:300,
          },
          {
            title:'时间',
            align:"center",
            dataIndex: 'data_time',
            fixed:'left',
            width:200,
            customRender:this.dataTimeRender
          }
        ],
        //列定义
        defColumns: [],
        siteType:0,
        url:{
          list:'/psoam/Water/queryWaterColumns',
          exportXlsUrl:'/psoam/Water/exportWaterHistory',
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


      //查询数据
      searchQuery(){
        this.loadData()
      },
      searchReset(){
        this.loadData(1);
      },



    },
    created(){
      //先查询表头
      // this.getColumns();
      //级联的行政区域
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