<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
            <a-form-item label="所属区域">
              <area-link-select type="cascader" v-model="queryParam.area" placeholder="请选择所属区域" show-search style="width: 100%" optionFilterProp="children"/>
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
            <a-form-item label="监测点位名称">
              <a-select v-model="queryParam.mn" show-search style="width: 100%" optionFilterProp="children" placeholder="请选择监测点位名称">
                <!--                <a-select-option :value="companyIds">请选择</a-select-option>-->
                <a-select-option v-for="item in items" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="10" :lg="11" :md="12" :sm="24">
            <a-form-item label="数据时间">
              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间" class="query-group-cust" v-model="queryParam.dataTime_begin"></j-date>
              <span class="query-group-split-cust"></span>
              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间" class="query-group-cust" v-model="queryParam.dataTime_end"></j-date>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="4" :lg="7" :md="8" :sm="24">
              <a-form-item label="状态">
                <j-dict-select-tag v-model="queryParam.state" placeholder="请选择状态"  dictCode="airDataStatus" :excludeFields="['0']"/>
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="toSearchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="toHandleAdd" type="primary" icon="plus">新增</a-button>
      <a-button @click="batchSubmit" type="primary" icon="snippets">批量提交</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('airq_hour')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
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
        :rowSelection="rowSelection"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="toHandleEdit(record)" v-if="record.state ===2 || record.status===4">编辑</a>
          <a-divider type="vertical" v-if="record.state ===2 || record.state===4"/>
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)" v-if="record.state ===2 || record.state===4">
            <a>删除</a>
          </a-popconfirm>
          <a @click="handleView(record)" v-if="record.state ===1 ||  record.state===3">查看</a>



        </span>

      </a-table>
    </div>

    <airqHour-modal ref="modalForm" @ok="modalFormOk"></airqHour-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import AirqHourModal from './modules/AirqHourModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import JDate from '@/components/jeecg/JDate.vue'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import {loadAreaDate} from '../component/areaUtil'
  import AreaHandler from "../component/AreaHandler"
  import {querySiteNameAndMn} from "../../requestAction/request";
  import Vue from 'vue'
  import AreaLinkSelect from '../component/AreaLinkSelect'
  import {getAction} from "../../../api/manage";

  export default {
    name: "AirqHourManInsert",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      JDictSelectTag,
      JDate,
      AirqHourModal,
      AreaLinkSelect
    },
    data () {
      return {
        description: 'airq_hour管理页面',
        queryParam: {
          companyIds:this.$store.getters.userInfo.companyIds.join(',')
        },
        items:[],
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
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
            title:'数据时间',
            align:"center",
            dataIndex: 'dataTime'
          },
          {
            title:'小时数据平台状态',
            align:"center",
            dataIndex: 'state_dictText'
          },
          {
            //title:'a21026Avg',
            title:'SO2 μg/m3',
            align:"center",
            dataIndex: 'a21026Avg'
          },
          {
            //title:'a21004Avg',
            title:'NO2 μg/m3',
            align:"center",
            dataIndex: 'a21004Avg'
          },
          {
            //title:'a3400201Avg',
            title:'PM10(1h)μg/m3',
            align:"center",
            dataIndex: 'a3400201Avg'
          },
          {
            //title:'a3400224Avg',
            title:'PM10(24h)μg/m3',
            align:"center",
            dataIndex: 'a3400224Avg'
          },
          {
            //title:'a21005Avg',
            title:'COμg/m3',
            align:"center",
            dataIndex: 'a21005Avg'
          },
          {
            //title:'a0502401Avg',
            title:'O3(1h)μg/m3',
            align:"center",
            dataIndex: 'a0502401Avg'
          },
          {
            //title:'a0502408Avg',
            title:'O3(8h)μg/m3',
            align:"center",
            dataIndex: 'a0502408Avg'
          },
          {
            //title:'a3400401Avg',
            title:'PM2.5(1h)μg/m3',
            align:"center",
            dataIndex: 'a3400401Avg'
          },
          {
            //title:'a3400424Avg',
            title:'PM2.5(24h)μg/m3',
            align:"center",
            dataIndex: 'a3400424Avg'
          },
          {
            //title:'a01002Avg',
            title:'温度(°C)',
            align:"center",
            dataIndex: 'a01001Avg'
          },
          {
            //title:'a01002Avg',
            title:'湿度(%)',
            align:"center",
            dataIndex: 'a01002Avg'
          },
          {
            //title:'a01007Avg',
            title:'风速(m/s)',
            align:"center",
            dataIndex: 'a01007Avg'
          },
          {
            title:'风向',
            align:"center",
            dataIndex: 'a01008Avg_dictText'
          },
          {
            // title:'a01006Avg',
            title:'气压(kPa)',
            align:"center",
            dataIndex: 'a01006Avg'
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
        url: {
          list: "/hour/airqHour/queryManInsertAirqHour",
          delete: "/hour/airqHour/delete",
          deleteBatch: "/hour/airqHour/deleteBatch",
          batchSubmit:"/hour/airqHour/batchSubmit",
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
      rowSelection() {
        return {
          getCheckboxProps: record => ({
            props: {
              disabled: record.state !== 2,
              name: record.projectName,
            },
          }),
          selectedRowKeys: this.selectedRowKeys,
          onChange: this.onSelectChange
        };
      },
    },
    methods: {
      initDictConfig(){
        loadAreaDate()
      },
      toSearchReset() {
        this.queryParam = {companyIds:this.$store.getters.userInfo.companyIds.join(',')};
        this.loadData(1);
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
      toHandleAdd:function(){
        this.handleAdd();
        this.$refs.modalForm.monitorTag = 'add';
      },
      toHandleEdit:function (record) {
        this.handleEdit(record);
        this.$refs.modalForm.monitorTag = 'edit';
        this.$refs.modalForm.title="编辑";
      },
      handleView: function (record) {
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.monitorTag = 'view';
        this.$refs.modalForm.title = "查看";
        this.$refs.modalForm.disableSubmit = true;
      },
      batchSubmit: function () {
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          let ids = "";
          for (var a = 0; a < this.selectedRowKeys.length; a++) {
            ids += this.selectedRowKeys[a] + ",";
          }
          let that = this;
          this.$confirm({
            title: "确认提交",
            content: "是否提交选中数据?",
            onOk: function () {
              that.loading = true;
              getAction(that.url.batchSubmit, {ids: ids}).then((res) => {
                if (res.success) {
                  that.$message.success(res.message);
                  that.loadData();
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.message);
                }
              }).finally(() => {
                that.loading = false;
              });
            }
          });
        }
      },
    },
    mounted(){

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