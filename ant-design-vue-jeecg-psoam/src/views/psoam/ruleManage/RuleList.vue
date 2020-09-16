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
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <a-form-item label="监控点类型">
                            <a-select v-model="queryParam.siteType" :allowClear="allowClear" placeholder="请选择" show-search style="width: 100%" optionFilterProp="children">
                              <a-select-option v-for="(item,index) in sites" :key="item.value" :value="item.key">
                                {{item.value}}
                              </a-select-option>
                            </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="2" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
            </span>
          </a-col>
        </a-row>

        <a-row>
          <a-col :xl="12" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="updateRule">更新策略</a-button>
              <a-button type="primary" @click="deleteRule" style="margin-left: 8px">删除策略</a-button>
            </span>
          </a-col>
          <a-col :xl="12" :lg="7" :md="8" :sm="24" style="padding-top: 1%">
            <span style="width: 16%;float: right;font-size: 14px;color: #000000;padding-left: 4px">策略未设置</span>
            <span  :style="{'background':'url('+bgImgs[2]+') no-repeat center',width: '14px',height:'14px',float: 'right',margin:'4px 0 0 0'}"></span>
            <span style="width: 16%;float: right;font-size: 14px;color: #000000;padding-left: 4px">策略停用</span>
            <span  :style="{'background':'url('+bgImgs[1]+') no-repeat center',width: '14px',height:'14px',float: 'right',margin:'4px 0 0 0'}"></span>
            <span  style="width: 16%;float: right;font-size: 14px;color: #000000;padding-left: 4px">策略正常</span>
            <span  :style="{'background':'url('+bgImgs[0]+') no-repeat center',width: '14px',height:'14px',float: 'right',margin:'4px 0 0 0'}"></span>
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
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        :loading="loading"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <div slot="icons" slot-scope="text, record" :style="{'background':'url('+tableImg[text]+') no-repeat center',width: '100%',height:'20px'}"></div>

        <span slot="action" slot-scope="text, record">
            <a @click="handleView(record)">详情</a>
        </span>

      </a-table>

    </div>

  </a-card>
</template>

<script>
  import Vue from 'vue'
  import AreaLinkSelect from '../component/AreaLinkSelect'
  import JDate from '@/components/jeecg/JDate.vue'
  import {tableMixin} from "../mixin/tableMixin";
  import mainPollutionSelect from "../component/mainPollutionSelect";
  import moment from 'moment'
  import {getAction} from "../../../api/manage";
  import {queryCompanyName, querySiteNameAndMn} from "../../requestAction/request";
  import AreaHandler from "../component/AreaHandler";
  export default {
    name: "RuleList",
    mixins:[tableMixin],
    components: {
      AreaLinkSelect,
      JDate
    },
    data() {
      return {
        queryParam: {
          companyIds: this.$store.getters.userInfo.companyIds.join(','),
        },
        siteType:'',
        items: [],
        siteOriginal: [],
        areaHandler: '',
        companyNames: [],
        companyNameOriginal: [],
        siteArea: '',
        name: '',
        bgImgs: {
          0: require('@/assets/icon_strategy_normal_b.png'),
          1: require('@/assets/icon_strategy_deactivate_b.png'),
          2: require('@/assets/icon_strategy_notset_b.png'),
        },
        tableImg: {
          0: require('@/assets/icon_strategy_normal_a.png'),
          1: require('@/assets/icon_strategy_deactivate_a.png'),
          2: require('@/assets/icon_strategy_notset_a.png'),
        },
        allowClear: true,
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
            title:'超标报警(实时)',
            align:"center",
            dataIndex: 'realTimeOver',
            scopedSlots: { customRender: 'icons' }
          },
          {
            title:'超标报警(小时)',
            align:"center",
            dataIndex: 'hourOver',
            scopedSlots: { customRender: 'icons' }
          },
          {
            title:'超标报警(日)',
            align:"center",
            dataIndex: 'dayOver',
            scopedSlots: { customRender: 'icons' }
          },
          {
            title:'离线报警',
            align:"center",
            dataIndex: 'offLine',
            scopedSlots: { customRender: 'icons' }
          },
          {
            title:'设备故障报警',
            align:"center",
            dataIndex: 'deviceFail',
            scopedSlots: { customRender: 'icons' }
          },
          {
            title:'定值报警',
            align:"center",
            dataIndex: 'constant',
            scopedSlots: { customRender: 'icons' }
          },
          {
            title:'量程报警',
            align:"center",
            dataIndex: 'measureDistance',
            scopedSlots: { customRender: 'icons' }
          },
          {
            title:'数据异常报警',
            align:"center",
            dataIndex: 'dataAbnormal',
            scopedSlots: { customRender: 'icons' }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            // fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url:{
          list:'/wr/warnRule/list',
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
      //地区企业站点级联
      areaChange(val){
        let _this = this;
        _this.items=[];
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
      //企业站点级联
      companyNameChange(val){
        console.log(this.queryParam.companyId)
        let _this = this;
        _this.items=[];
        _this.siteOriginal.forEach(e=>{
          if(e.companyId === val){
            _this.items.push(e)
          }
        });

        if(this.queryParam.companyId != null){
          _this.showDate = true;
        }else {
          _this.showDate = false;
        }
      },
      queryCompanyAndSite(){
        let that = this;
        querySiteNameAndMn({companyIds:this.$store.getters.userInfo.companyIds.join(','),siteType:this.siteType}).then((res)=>{
          if(res.success){
            //console.log("!!",res.result);
            that.siteOriginal = res.result;
            that.siteitems = res.result;
          }
        });
        if(this.queryParam.area != null){
          this.items = that.siteitems;
        }else {
          this.items = '';
        }
        //查询企业名称
        queryCompanyName({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res) => {
          if(res.success){
            that.companyNameOriginal = res.result.companyNames;
            that.companyNames = res.result.companyNames;
            // console.log("!!",that.companyNames);
          }
        });
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