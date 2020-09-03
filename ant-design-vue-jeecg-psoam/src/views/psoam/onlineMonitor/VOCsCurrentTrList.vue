<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="行政区域">
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
                <!--                <a-select-option :value="companyIds">请选择</a-select-option>-->
                <a-select-option v-for="item in items" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" >总数</a-button>
      <a-button @click="handleAdd" type="primary" >正常</a-button>
      <a-button @click="handleAdd" type="primary" >预警</a-button>
      <a-button @click="handleAdd" type="primary" >超标</a-button>
      <a-button @click="handleAdd" type="primary" >异常</a-button>
      <a-button @click="handleAdd" type="primary" >离线</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="scroll"
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
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>
      </a-table>
    </div>

  </a-card>
</template>

<script>
  import {loadAreaDate} from '../component/areaUtil'
  import '@/assets/less/TableExpand.less'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { mixinDevice } from '@/utils/mixin'
  import {queryVOCsColumns, queryCompanyName, querySiteNameAndMn} from "../../requestAction/request";
  import Vue from 'vue'
  import AreaHandler from "../component/AreaHandler";
  import AreaLinkSelect from '../component/AreaLinkSelect'
  import {tableMixin} from "../mixin/tableMixin";
  export default {
    name: "VOCsCurrentTrList",
    mixins:[mixinDevice,tableMixin,JeecgListMixin],
    components: {
      AreaLinkSelect
    },
    data () {
      return {
        scroll:{},
        description: '实时监控（废气）',
        items:[],
        siteOriginal:[],
        areaHandler:'',
        companyNames:[],
        companyNameOriginal:[],
        companyIds:this.companyId,
        siteArea:'',
        allowClear:true,
        name:'',
        queryParam: {
          companyIds:this.$store.getters.userInfo.companyIds.join(',')
        },
        siteType:2,
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
            width:300,
          },
          {
            title:'监测点名称',
            align:"center",
            dataIndex: 'monName',
            fixed:'left',
            width:300,
          },
          {
            title:'时间',
            align:"center",
            dataIndex: 'dataTime',
            fixed:'left',
            width:200,
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          }
        ],
        url: {
          list: "/onlineMonitor/waterCurrentTr/list",
        },
        dictOptions:{},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      renderEmpty(val){
        if(val==null||val==='')
          return 'NA'
        return val
      },
      getColumns(){
        let that = this;
        that.queryParam.type = that.siteType;
        queryVOCsColumns(that.queryParam).then(res => {
          if(res.result){
            that.scroll={x:250*res.result.length};
            for(var i=0;i<res.result.length;i++){
              that.defColumns.push(res.result[i]);
            }
          }
          that.initColumns();
        })
      },
      searchQuery() {
        this.getColumns();
        this.loadData(1);
      },
    },
    created() {
      this.getColumns();
      let that = this;
      querySiteNameAndMn({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res)=>{
        debugger
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
  @import '~@assets/less/common.less';
</style>