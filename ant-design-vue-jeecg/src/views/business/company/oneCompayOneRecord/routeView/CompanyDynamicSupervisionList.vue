<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24" v-if="role === 'monitor'">
            <a-form-item label="申报状态">
              <j-dict-select-tag placeholder="请选择申报状态" v-model="queryParam.status" dictCode="statue" :excludeFields="['4']"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24" v-if="role === 'monitor'">
            <a-form-item label="企业名称">
              <a-select v-model="queryParam.companyId" show-search style="width: 100%" optionFilterProp="children">
                <a-select-option :value="companyIds">请选择</a-select-option>
                <a-select-option v-for="item in items" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="申报年份">
                <a-select v-model="queryParam.reportYear" show-search @visible-change="yearChange($event)">
                  <a-select-option v-for="item in years" :key="item.value"  :value="item.value">
                    {{item.value}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="toSearchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->

    <div class="table-operator" v-if="role === 'monitor'">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <!--
      <a-button type="primary" icon="download" @click="handleExportXls('企业年度动态监管')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      -->
      <a-button @click="batchDeclare" type="primary" icon="snippets">批量申报</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay" >
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;" v-if="role==='monitor'">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>



      <a-table v-if="role === 'monitor'"
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

        <span slot="action" slot-scope="text, record">
          <a @click="toHandleEdit(record)" v-if="record.status=='0' || record.status=='3'">编辑</a>
          <a @click="toHandleEdit(record)" v-if="record.status=='2'">申请修改</a>
          <a @click="handleView(record)" v-if="record.status=='1' || record.status=='4'">查看</a>
          <a-divider type="vertical" v-if="record.status=='0' || record.status=='3'" />
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a v-if="record.status=='0' || record.status=='3'">删除</a>
          </a-popconfirm>
        </span>


      </a-table>

      <a-table v-if="role !== 'monitor'"
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

        <span slot="action" slot-scope="text, record">
          <a @click="handleView(record)">查看</a>
        </span>


      </a-table>
    </div>

    <companyDynamicSupervision-modal ref="modalForm" @ok="modalFormOk" :companyId="companyId" :monitor="role"></companyDynamicSupervision-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import CompanyDynamicSupervisionModal from './CompanyDynamicSupervisionModal'
  import store from '@/store/'
  import {getAction} from "../../../../../api/manage";
  import {queryCompanyName} from "../../../requestAction/request";

  export default {
    name: "CompanyDynamicSupervisionList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      CompanyDynamicSupervisionModal
    },
    props:{
      companyId:'',
      role:'',
      listType:''
    },
    data () {
      return {
        description: '企业年度动态监管管理页面',
        years:[],
        items:[],
        companyid:'',
        monitor:'',
        companyIds: this.companyId,
        queryParam: {
          companyId: this.companyId
        },
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
            title:'材料名称',
            align:"center",
            dataIndex: 'documentName'
          },
          {
            title:'材料类型',
            align:"center",
            dataIndex: 'documentType_dictText'
          },
          {
            title:'申报年份',
            align:"center",
            dataIndex: 'reportYear'
          },

          // {
          //   title:'数据状态',
          //   align:"center",
          //   dataIndex: 'status_dictText'
          // },
          // {
          //   title:'企业id',
          //   align:"center",
          //   dataIndex: 'companyId'
          // },
          //
          //
          // {
          //   title:'内容',
          //   align:"center",
          //   dataIndex: 'content',
          //   scopedSlots: {customRender: 'fileSlot'}
          // },
          // {
          //   title:'申报人',
          //   align:"center",
          //   dataIndex: 'createBy'
          // },
          // {
          //   title:'申报时间',
          //   align:"center",
          //   dataIndex: 'createTime',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
          // },
          // {
          //   title:'审核时间',
          //   align:"center",
          //   dataIndex: 'updateTime',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
          // },
          // {
          //   title:'审核人',
          //   align:"center",
          //   dataIndex: 'updateBy'
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
          list: "/cds/companyDynamicSupervision/list/"+this.listType,
          delete: "/cds/companyDynamicSupervision/delete",
          deleteBatch: "/cds/companyDynamicSupervision/deleteBatch",
          batchDeclare: "/cds/companyDynamicSupervision/batchDeclare"
          // exportXlsUrl: "/cds/companyDynamicSupervision/exportXls",
          // importExcelUrl: "cds/companyDynamicSupervision/importExcel",
        },
        dictOptions:{},
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
              disabled: record.status != '0',
              name: record.id,
            },
          }),
          selectedRowKeys: this.selectedRowKeys,
          onChange: this.onSelectChange
        };
      }
    },
    methods: {
      calcIndex: function (t,r,index) {

        return parseInt(index)+1+(this.ipagination.current-1)*this.ipagination.pageSize;
      },
      modalFormOk(){
        this.loadData(1);
      },
      initDictConfig(){
      },
      handleView: function (record) {
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "查看";
        this.$refs.modalForm.disableSubmit = true;
      },
      handleAddMy:function(){
        this.handleAdd();
        this.monitor = 'add';
      },
      toHandleEdit:function(record){
        this.handleEdit(record);
        this.monitor = 'edit';
        this.$refs.modalForm.title="年度动态监管";
      },
      toSearchReset() {
        this.queryParam = {companyIds:this.companyIds};
        this.loadData(1);
      },
      batchDeclare: function () {
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
            title: "确认申报",
            content: "是否申报选中数据?",
            onOk: function () {
              that.loading = true;
              getAction(that.url.batchDeclare, {ids: ids}).then((res) => {
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
      yearChange () {
        var myDate = new Date()
        var startYear = myDate.getFullYear() - 20// 起始年份
        var endYear = myDate.getFullYear() + 20// 结束年份

        this.years = []
        for (var i = startYear; i <= endYear; i++) {
          this.years.push({value: i, label: i})
        }
      }
    },
    mounted() {
      this.yearChange();
      let that = this;
      //查询企业名称
      queryCompanyName({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res) => {
        if(res.success){
          that.items = res.result;
        }
      });
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>