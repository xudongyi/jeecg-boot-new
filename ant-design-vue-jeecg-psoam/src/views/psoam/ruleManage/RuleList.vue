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
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons" v-if="selectedRowKeys.length > 0">
              <a-button type="primary" @click="searchQuery">更新策略</a-button>
              <a-button type="primary" @click="deleteBatch(selectedRowKeys)" style="margin-left: 8px">删除策略</a-button>
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
            <a @click="handleDetail(record)">查看</a>
        </span>

      </a-table>

    </div>
    <RuleListModal ref="modalForm"></RuleListModal>
    <DeleteRuleModal ref="deleteModalForm" @deleted="queryData"></DeleteRuleModal>
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
  import RuleListModal from "./modules/RuleListModal";
  import DeleteRuleModal from "./modules/DeleteRuleModal";
  export default {
    name: "RuleList",
    mixins:[tableMixin],
    components: {
      DeleteRuleModal,
      RuleListModal,
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
            customRender: this.renderContent
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
            scopedSlots: { customRender: 'icons' },
            sorter: (a, b) => a.realTimeOver - b.realTimeOver
          },
          {
            title:'超标报警(小时)',
            align:"center",
            dataIndex: 'hourOver',
            scopedSlots: { customRender: 'icons' },
            sorter: (a, b) => a.hourOver - b.hourOver
          },
          {
            title:'超标报警(日)',
            align:"center",
            dataIndex: 'dayOver',
            scopedSlots: { customRender: 'icons' },
            sorter: (a, b) => a.dayOver - b.dayOver
          },
          {
            title:'离线报警',
            align:"center",
            dataIndex: 'offLine',
            scopedSlots: { customRender: 'icons' },
            sorter: (a, b) => a.offLine - b.offLine
          },
          {
            title:'设备故障报警',
            align:"center",
            dataIndex: 'deviceFail',
            scopedSlots: { customRender: 'icons' },
            sorter: (a, b) => a.deviceFail - b.deviceFail
          },
          {
            title:'定值报警',
            align:"center",
            dataIndex: 'constant',
            scopedSlots: { customRender: 'icons' },
            sorter: (a, b) => a.constant - b.constant
          },
          {
            title:'量程报警',
            align:"center",
            dataIndex: 'measureDistance',
            scopedSlots: { customRender: 'icons' },
            sorter: (a, b) => a.measureDistance - b.measureDistance
          },
          {
            title:'数据异常报警',
            align:"center",
            dataIndex: 'dataAbnormal',
            scopedSlots: { customRender: 'icons' },
            sorter: (a, b) => a.dataAbnormal - b.dataAbnormal
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
    watch: {
      dataSource(val) {
        console.log(val)
        this.rowspan(val)
        console.log(this.spanArr, this.position)
      }
    },
    methods:{
      renderContent (value, row, index)  {
        const obj = {
          children: value,
          attrs: {}
        };
        const _row = this.spanArr[index];
        const _col = _row> 0 ? 1 : 0;
        obj.attrs = {
          rowSpan: _row,
          colSpan: _col
        };
        return obj;
      },
      rowspan(userData){
        let _this = this
        _this. spanArr=[];
        _this. position=0;
        userData.forEach((item,index) => {
          if(index === 0){
            _this.spanArr.push(1);
            _this.position = 0;
          }else{
            //需要合并的地方判断
            if(userData[index].companyName === userData[index-1].companyName ){
              _this.spanArr[ _this.position] += 1;
              _this.spanArr.push(0);
            }else{
              _this.spanArr.push(1);
              _this.position = index;
            }
          }
        });
      },
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
      handleTableChange(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        //TODO 筛选
        if (Object.keys(sorter).length > 0) {
          this.isorter.order = "ascend" == sorter.order ? "asc" : "desc"
        }
        this.ipagination = pagination;
        this.queryData();
      },
      handleDetail(record){
        this.$refs.modalForm.view(record);
        this.$refs.modalForm.title = "详情";
        this.$refs.modalForm.record = record;
      },
      deleteBatch(selectedRowKeys){
        this.$refs.deleteModalForm.delete(selectedRowKeys);
        this.$refs.deleteModalForm.title = "删除策略";
        this.$refs.deleteModalForm.selectedKeys = selectedRowKeys;
      }
    },
    created(){
      this.queryCompanyAndSite();
      this.queryData(1);
    },
  }
</script>

<style scoped>

</style>