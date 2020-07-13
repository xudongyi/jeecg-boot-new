<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="项目名称">
              <a-input placeholder="请输入项目名称" v-model="queryParam.projectName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="批复文件号">
              <a-input placeholder="请输入批复文件号" v-model="queryParam.approveFilenum"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="localReset" icon="reload" style="margin-left: 8px">重置</a-button>
            <!--  <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>-->
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div class="table-operator"  v-show="isApply">
      <a-button @click="applyAdd" type="primary" icon="plus">新增</a-button>
      <a-button @click="batchApply" type="primary" icon="snippets">批量申报</a-button>

      <!--<a-button type="primary" icon="download" @click="handleExportXls('环评审批信息')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>-->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
         <!-- <a-menu-item key="2" @click="batchApply"><a-icon type="delete"/>申报</a-menu-item>-->
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;" v-show="isApply">
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
            <a @click="handleView(record)"  v-show="!isApply ">查看</a>
           <a @click="applyEdit(record)"  v-show="isApply && record.status !== '0' ">申请修改</a>
          <!-- 编辑和删除-->
          <a @click="applyEdit(record)"  v-show="isApply && record.status === '0' ">编辑</a>
          <a-divider type="vertical" v-show="isApply && record.status === '0' "  />
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)" v-show="isApply && record.status === '0'">
                  <a>删除</a>
                </a-popconfirm>
        </span>

      </a-table>
    </div>

    <companyEnvTrial-modal ref="modalForm" :company-id="companyId" @ok="modalFormOk" @declare="reloadApply"></companyEnvTrial-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import CompanyEnvTrialModal from '../../oneCompayOneRecord/routeView/modules/CompanyEnvTrialModal'
  import {getAction} from '@/api/manage'
  export default {
    name: "EnvTrialApplyList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      CompanyEnvTrialModal
    },
    props:{
      companyId:'',
      isApply:{
        type:Boolean,
        default:false
      }
    },
    data () {
      return {
        description: '环评审批信息管理页面',
        queryParam: {companyId:this.companyId},
        isorter:{
          column: 'status',
          order: 'asc',
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
            title:'项目名称',
            align:"center",
            dataIndex: 'projectName'
          },
          {
            title:'批复文件号',
            align:"center",
            dataIndex: 'approveFilenum'
          },
          {
            title:'审批单位',
            align:"center",
            dataIndex: 'approveUnit'
          },
          {
            title:'审批时间',
            align:"center",
            dataIndex: 'approveDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'数据状态',
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
          list: "/company/envTrial/list",
          delete:"/company/envTrial/delete",
          deleteBatch:"/company/envTrial/deleteBatch",
          batchApply:"/company/envTrial/batchApply"
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
              disabled: record.status !== '0',
              name: record.projectName,
            },
          }),
          selectedRowKeys: this.selectedRowKeys,
          onChange: this.onSelectChange
        };
      },
    },
    methods: {
      calcIndex: function (t,r,index) {

        return parseInt(index)+1+(this.ipagination.current-1)*this.ipagination.pageSize;
      },
      initDictConfig(){
      },
      handleView: function (record) {
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "查看";
        this.$refs.modalForm.disableSubmit = false;
      },
      applyAdd(){
        this.handleAdd();
        this.$refs.modalForm.disable = false;
      },
      applyEdit(record){
        this.$refs.modalForm.disable = false;
        this.handleEdit(record);
      },
      localReset(){
        this.queryParam = {companyId:this.companyId};
        this.loadData(1);
      },
      reloadApply(){
        this.$emit('reloadApply');
      },
      batchApply() {
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          var ids = "";
          for (var a = 0; a < this.selectedRowKeys.length; a++) {
            ids += this.selectedRowKeys[a] + ",";
          }
          var that = this;
          this.$confirm({
            title: "确认申报",
            content: "是否申报选中数据?",
            onOk: function () {
              that.loading = true;
              getAction(that.url.batchApply, {ids: ids}).then((res) => {
                if (res.success) {
                  that.$message.success(res.message);
                  that.loadData();
                  that.onClearSelected();
                  that.reloadApply();
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
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>