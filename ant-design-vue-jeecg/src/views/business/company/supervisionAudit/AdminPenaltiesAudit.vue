<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :xl="10" :lg="11" :md="12" :sm="24">
            <a-form-item label="发文日期">
              <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.reportDate_begin"></j-date>
              <span class="query-group-split-cust"></span>
              <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.reportDate_end"></j-date>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="申报状态">
              <j-dict-select-tag placeholder="请选择申报状态" v-model="queryParam.status" dictCode="statue"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="企业名称">
              <a-select v-model="queryParam.companyId" show-search style="width: 100%" optionFilterProp="children">
                <a-select-option v-for="item in items" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="toSearchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <!--  <a @click="handleToggleSearch" style="margin-left: 8px" v-if="role === 'monitor'">
                  {{ toggleSearchStatus ? '收起' : '展开' }}
                  <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
                </a>-->
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;" v-if="role === 'monitor'">
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

        class="j-table-force-nowrap"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="toHandleEdit(record)" v-if="record.status=='1'">审核</a>
          <a @click="handleView(record)" v-else>查看</a>
        </span>

      </a-table>
    </div>

    <admin-penalties-audit-modal ref="modalForm" @ok="modalFormOk"></admin-penalties-audit-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import AdminPenaltiesAuditModal from './AdminPenaltiesAuditModal'
  import JDate from '@/components/jeecg/JDate.vue'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import {getAction} from "../../../../api/manage";
  import {queryCompanyName} from "../../requestAction/request";
  import moment from 'moment'

  export default {
    name: "AdminPenaltiesAudit",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      JDate,
      AdminPenaltiesAuditModal
    },
    props: {
      companyId:'',
    },
    data () {
      return {
        description: '行政处罚信息管理页面',
        items:[],
        queryParam: {
          companyIds: this.$store.getters.userInfo.companyIds.join(',')
        },
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
            title:'文件名称',
            align:"center",
            dataIndex: 'documentName'
          },
          {
            title:'文件编号',
            align:"center",
            dataIndex: 'documentNo'
          },
          {
            title:'企业名称',
            align:"center",
            dataIndex: 'companyName'
          },
          {
            title:'发文日期',
            align:"center",
            dataIndex: 'reportDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'申报时间',
            align:"center",
            dataIndex: 'createTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'申报人',
            align:"center",
            dataIndex: 'createName'
          },
          {
            title:'审核时间',
            align:"center",
            dataIndex: 'updateTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'审核人',
            align:"center",
            dataIndex: 'updateName'
          },
          {
            title:'申报状态',
            align:"center",
            dataIndex: 'status_dictText'
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
          list: "/cap/companyAdminPenalties/list",
          // delete: "/cap/companyAdminPenalties/delete",
          // deleteBatch: "/cap/companyAdminPenalties/deleteBatch",
          // batchDeclare: "/cap/companyAdminPenalties/batchDeclare"
          // exportXlsUrl: "/cap/companyAdminPenalties/exportXls",
          // importExcelUrl: "cap/companyAdminPenalties/importExcel",
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
      initDictConfig(){
      },
      handleView: function (record) {
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "查看";
        this.$refs.modalForm.disableSubmit = true;
      },
      toHandleEdit:function (record) {
        this.handleEdit(record);
        this.monitor = 'edit';
        this.$refs.modalForm.title="行政处罚信息";
        this.$refs.modalForm.disableSubmit = false;
      },
      toSearchReset() {
        this.queryParam = {companyIds:this.queryParam.companyIds};
        this.loadData(1);
      },

    },
    created(){
      let that = this;
      //查询企业名称
      queryCompanyName({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res) => {
        if(res.success){
          that.items = res.result;
        }
      });
    },

  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>