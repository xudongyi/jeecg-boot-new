<template>
  <a-card :bordered="false">
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
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
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <sysWarnPointRule-modal ref="modalForm" @ok="modalFormOk"></sysWarnPointRule-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SysWarnPointRuleModal from './modules/SysWarnPointRuleModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "SysWarnPointRuleList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SysWarnPointRuleModal
    },
    data () {
      return {
        description: '站点报警策略表管理页面',
        // 表头
        columns: [
          {
            title:'站点名称',
            align:"center",
            dataIndex: 'siteName'
          },
          {
            title:'站点类型',
            align:"center",
            dataIndex: 'siteType_dictText'
          },
          {
            title:'所属单位',
            align:"center",
            dataIndex: 'companyName'
          },
          {
            title:'站点级别',
            align:"center",
            dataIndex: 'siteLevel_dictText'
          },
          {
            title:'策略类型',
            align:"center",
            dataIndex: 'ruleType_dictText'
          },
          {
            title:'策略状态',
            align:"center",
            dataIndex: 'isUsed_dictText',
            scopedSlots: { customRender: 'isUsed' },
          },
          // {
          //   title:'站点id',
          //   align:"center",
          //   dataIndex: 'monitorId_dictText'
          // },
          // {
          //   title:'策略id',
          //   align:"center",
          //   dataIndex: 'ruleId_dictText'
          // },
          // {
          //   title:'是否启用',
          //   align:"center",
          //   dataIndex: 'isUsed_dictText'
          // },
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
          list: "/swpr/sysWarnPointRule/list",
          delete: "/swpr/sysWarnPointRule/delete",
          deleteBatch: "/swpr/sysWarnPointRule/deleteBatch",
          exportXlsUrl: "/swpr/sysWarnPointRule/exportXls",
          importExcelUrl: "swpr/sysWarnPointRule/importExcel",
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
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>