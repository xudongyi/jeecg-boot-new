<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
            <a-form-item label="所属区域">
              <area-link-select type="cascader" v-model="queryParam.area" show-search style="width: 100%" optionFilterProp="children"/>
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
            <a-form-item label="监测点位名称">
              <a-select v-model="queryParam.mn" show-search style="width: 100%" optionFilterProp="children">
<!--                <a-select-option :value="companyIds">请选择</a-select-option>-->
                <a-select-option v-for="item in items" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="9" :lg="11" :md="12" :sm="24">
            <a-form-item label="发布时间">
              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间" class="query-group-cust" v-model="queryParam.createTime_begin"></j-date>
              <span class="query-group-split-cust"></span>
              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间" class="query-group-cust" v-model="queryParam.createTime_end" ></j-date>
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
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

        <div slot="filterDropdown">
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
  import JDate from '@/components/jeecg/JDate.vue'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import {loadAreaDate} from '../component/areaUtil'
  import AreaHandler from "../component/AreaHandler"
  import {querySiteNameAndMn} from "../../requestAction/request";
  import Vue from 'vue'
  import AreaLinkSelect from '../component/AreaLinkSelect'

  export default {
    name: "AirqHourList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
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
        items:[],
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
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            },
            scopedSlots: {
              filterDropdown: 'filterDropdown',
              filterIcon: 'filterIcon'},
          },
          // {
          //   // title:'dataTime',
          //   title:'数据时间',
          //   align:"center",
          //   dataIndex: 'dataTime',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
          // },
          {
            title:'行政区域',
            align:"center",
            dataIndex: 'area',
            customRender:this.getAreaByCode,
          },
          {
            title:'监测点位名称',
            align:"center",
            dataIndex: 'siteName',
          },
          {
            //title:'createTime',
            title:'发布时间',
            align:"center",
            dataIndex: 'createTime',
          },
          {
            title:'空气质量指数(AQI)',
            align:"center",
            dataIndex: 'aqi',
            sorter: (a, b) => a.aqi - b.aqi
          },
          {
            title:'首要污染物',
            align:"center",
            dataIndex: 'meaning'
          },
          // {
          //   title:'mn',
          //   align:"center",
          //   dataIndex: 'mn'
          // },
          // {
          //   title:'state',
          //   align:"center",
          //   dataIndex: 'state'
          // },
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
            }
          },
          {
            title:'空气质量指数类别',
            align:"center",
            dataIndex: 'level_dictText',
            key: 'airLevel',
            scopedSlots: { customRender:'airLevel'},
            sorter: (a, b) => a.level_dictText - b.level_dictText
          },
          {
            //title:'a21026Avg',
            title:'SO2 μg/m3',
            align:"center",
            dataIndex: 'a21026Avg',
            sorter: (a, b) => a.a21026Avg - b.a21026Avg
          },
          {
            //title:'a21004Avg',
            title:'NO2 μg/m3',
            align:"center",
            dataIndex: 'a21004Avg',
            sorter: (a, b) => a.a21004Avg - b.a21004Avg
          },
          {
            //title:'a3400201Avg',
            title:'PM10(1h)μg/m3',
            align:"center",
            dataIndex: 'a3400201Avg',
            sorter: (a, b) => a.a3400201Avg - b.a3400201Avg
          },
          {
            //title:'a3400224Avg',
            title:'PM10(24h)μg/m3',
            align:"center",
            dataIndex: 'a3400224Avg',
            sorter: (a, b) => a.a3400224Avg - b.a3400224Avg
          },
          {
            //title:'a21005Avg',
            title:'COμg/m3',
            align:"center",
            dataIndex: 'a21005Avg',
            sorter: (a, b) => a.a21005Avg - b.a21005Avg
          },
          {
            //title:'a0502401Avg',
            title:'O3(1h)μg/m3',
            align:"center",
            dataIndex: 'a0502401Avg',
            sorter: (a, b) => a.a0502401Avg - b.a0502401Avg
          },
          {
            //title:'a0502408Avg',
            title:'O3(8h)μg/m3',
            align:"center",
            dataIndex: 'a0502408Avg',
            sorter: (a, b) => a.a0502408Avg - b.a0502408Avg
          },
          {
            //title:'a3400401Avg',
            title:'PM2.5(1h)μg/m3',
            align:"center",
            dataIndex: 'a3400401Avg',
            sorter: (a, b) => a.a3400401Avg - b.a3400401Avg
          },
          {
            //title:'a3400424Avg',
            title:'PM2.5(24h)μg/m3',
            align:"center",
            dataIndex: 'a3400424Avg',
            sorter: (a, b) => a.a3400424Avg - b.a3400424Avg
          },
          {
            //title:'a01002Avg',
            title:'温度(°C)',
            align:"center",
            dataIndex: 'a01001Avg',
            sorter: (a, b) => a.a01001Avg - b.a01001Avg
          },
          {
            //title:'a01002Avg',
            title:'湿度(%)',
            align:"center",
            dataIndex: 'a01002Avg',
            sorter: (a, b) => a.a01002Avg - b.a01002Avg
          },
          {
            //title:'a01007Avg',
            title:'风速(m/s)',
            align:"center",
            dataIndex: 'a01007Avg',
            sorter: (a, b) => a.a01007Avg - b.a01007Avg
          },
          {
            //title:'a21003Avg',
            title:'风向',
            align:"center",
            dataIndex: 'a01008Avg',
            sorter: (a, b) => a.a01008Avg - b.a01008Avg
          },
          {
            // title:'a01006Avg',
            title:'气压(kPa)',
            align:"center",
            dataIndex: 'a01006Avg',
            sorter: (a, b) => a.a01006Avg - b.a01006Avg
          },
          // {
          //   //title:'a21002Avg',
          //   title:'NOx',
          //   align:"center",
          //   dataIndex: 'a21002Avg'
          // },
          // {
          //   //title:'a21003Avg',
          //   title:'NO',
          //   align:"center",
          //   dataIndex: 'a21003Avg'
          // },
        ],
        url: {
          list: "/hour/airqHour/queryLastAirqHour",
          delete: "/hour/airqHour/delete",
          deleteBatch: "/hour/airqHour/deleteBatch",
          exportXlsUrl: "/hour/airqHour/exportXls",
          importExcelUrl: "hour/airqHour/importExcel",
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
      initArea(){
        this.areaHandler = new AreaHandler()
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
      }
    },
    mounted(){
      this.initColumns();
      let that = this;
      querySiteNameAndMn({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res)=>{
        if(res.success){
          console.log("!!",res.result);
          that.items = res.result;
        }
      })
    }

  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>