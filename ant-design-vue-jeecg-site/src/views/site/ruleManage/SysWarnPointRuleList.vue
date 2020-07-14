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

          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
<!--              <a-menu-item>-->
<!--                <a @click="toHandleEdit(record)">编辑</a>-->
<!--              </a-menu-item>-->
              <a-menu-item>
                <a @click="handleView(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

        <span slot="isUsed" slot-scope="isUsed">
          <div :style="{color: isUsed === '停用'? 'red':'black'}">{{isUsed}}</div>
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
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:this.calcIndex
          },
          {
            title:'站点名称',
            align:"center",
            dataIndex: 'siteName',
            customRender: this.renderContent
          },
          {
            title:'站点类型',
            align:"center",
            dataIndex: 'siteType_dictText',
            customRender: this.renderContent
          },
          {
            title:'所属单位',
            align:"center",
            dataIndex: 'companyName',
            customRender: this.renderContent
          },
          {
            title:'站点级别',
            align:"center",
            dataIndex: 'siteLevel_dictText',
            customRender: this.renderContent
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
    watch: {
      dataSource(val){
        console.log(val)
        this.rowspan(val)
        console.log(this.spanArr,this.position)
      }

    },
    methods: {
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
            if(userData[index].siteName === userData[index-1].siteName ){
              _this.spanArr[ _this.position] += 1;
              _this.spanArr.push(0);
            }else{
              _this.spanArr.push(1);
              _this.position = index;
            }
          }
        });
      },
      calcIndex: function (t,r,index) {
        console.log(t,r,index)
        return parseInt(index)+1+(this.ipagination.current-1)*this.ipagination.pageSize;
      },
      initDictConfig(){
      },
      modalFormOk(){
        this.loadData();
      },
      handleView: function (record) {
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "详情";
        this.$refs.modalForm.disableSubmit = true;
      },
      toHandleEdit:function(record){
        this.handleEdit(record);
        this.$refs.modalForm.title="编辑";
        this.$refs.modalForm.disableSubmit = false;
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>