<template>
  <div>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="申报状态">
              <j-dict-select-tag placeholder="请选择申报状态" v-model="queryParam.status" dictCode="statue"  @input="searchQuery" :excludeFields="['0','4']"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="审核单元">
              <j-dict-select-tag  placeholder="请选择审核单元" v-model="queryParam.fromTable" dictCode="fromTable"  @input="searchQuery"/>
            </a-form-item>
          </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="企业名称">
                <a-select  style="width: 100%" placeholder="请输入企业名称" @change="handleChange" :value="value">
                  <a-select-option value="请选择">请选择</a-select-option>
                  <a-select-option v-for="item in items" :key="item.value"  >
                    {{item.value}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="localReset" icon="reload" style="margin-left: 8px">重置</a-button>
             <!-- <a @click="handleToggleSearch" style="margin-left: 8px">
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
    <div class="table-operator">
      <!--<a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('company_product_material')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>-->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchPass"><a-icon type="delete"/>审核通过</a-menu-item>
          <a-menu-item key="2" @click="batchFail"><a-icon type="delete"/>审核不通过</a-menu-item>
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
        <template slot="createTime" slot-scope="text">
        {{timeFormat(text)}}
        </template>
        <template slot="updateTime" slot-scope="text">
          {{timeFormat(text)}}
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)" v-show = "record.status==='1'">审核</a>
          <a @click="handleView(record)"  v-show = "record.status!=='1'">查看</a>
<!--
          <a-divider type="vertical" />
-->
          <!--<a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>-->
        </span>
      </a-table>
    </div>

  </a-card>
  <basic-info-audit ref ="basicInfoModal" ></basic-info-audit>
    <acceptance-audit ref="acceptanceModal"></acceptance-audit>
    <prevention-audit ref="preventionModal"></prevention-audit>
    <env-tax-audit ref="envTaxModal"></env-tax-audit>
    <dirty-allow-audit ref="dirtyAllowModal"></dirty-allow-audit>
    <risk-waste-audit ref="riskWasteModal"></risk-waste-audit>
    <solid-waste-audit ref="solidWasteModal"></solid-waste-audit>
    <radiate-waste-audit ref="radiateWasteModal"></radiate-waste-audit>
    <clean-product-audit ref="cleanProductModal"></clean-product-audit>
    <online-info-audit ref="onlineInfoModal"></online-info-audit>
    <user-info-audit ref="userInfoModal"/>
    <product-material-audit ref="productmaterialModal"/>
    <env-trial-audit ref="envtrialModal"/>
    <qualification-audit  ref="qualificationAudit"></qualification-audit>
  </div>

</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {queryCompanyName} from "../../requestAction/request";
  import BasicInfoAudit from "./modules/BasicInfoAudit";
  import AcceptanceAudit from "./modules/AcceptanceAudit";
  import PreventionAudit from "./modules/PreventionAudit";
  import EnvTaxAudit from "./modules/EnvTaxAudit";
  import CleanProductAudit from "./modules/CleanProductAudit";
  import OnlineInfoAudit from "./modules/OnlineInfoAudit";
  import UserInfoAudit from "./modules/UserInfoAudit";
  import DirtyAllowAudit from "./modules/DirtyAllowAudit"
  import RiskWasteAudit from "./modules/RiskWasteAudit";
  import SolidWasteAudit from "./modules/SolidWasteAudit";
  import RadiateWasteAudit from "./modules/RadiateWasteAudit";
  import ProductMaterialAudit from "./modules/ProductMaterialAudit";
  import EnvTrialAudit from "./modules/EnvTrialAudit";
  import QualificationAudit from "./modules/QualificationAudit";
  import moment from 'moment'
  import {postAction} from '@/api/manage'

  export default {
    name: "BaseAuditList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ProductMaterialAudit,
      AcceptanceAudit, JDictSelectTag
      ,BasicInfoAudit//基础信息审核
      ,UserInfoAudit//员工信息审核
      ,PreventionAudit,
      EnvTaxAudit,
      DirtyAllowAudit,
      RiskWasteAudit,
      SolidWasteAudit,
      RadiateWasteAudit,
      CleanProductAudit,
      OnlineInfoAudit,
      EnvTrialAudit,
      QualificationAudit
    },

    data () {
      return {
        items:[],
        value:'',
        queryParam:{companyIds:this.$store.getters.userInfo.companyIds.join(',')},
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
            title:'企业名称',
            align:"center",
            dataIndex: 'companyName'
          },
          {
            title:'审核单元',
            align:"center",
            dataIndex: 'fromTable_dictText'
          },
          {
            title:'申报时间',
            align:"center",
            dataIndex: 'createTime',
            scopedSlots: { customRender: 'createTime' }
          },
          {
            title:'申报状态',
            align:"center",
            dataIndex: 'status_dictText'
          },
          {
            title:'审核人',
            align:"center",
            dataIndex: 'updateBy'
          },
          {
            title:'生效时间',
            align:"center",
            dataIndex: 'updateTime',
            scopedSlots: { customRender: 'updateTime' }
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
          list: "/company/apply/listByUserId",
          batchPass: "/company/apply/batchPass",
          batchFail: "/company/apply/batchFail",
          // delete: "/testOneDemo/companyProductMaterial/delete",
          // deleteBatch: "/testOneDemo/companyProductMaterial/deleteBatch",
          // exportXlsUrl: "/testOneDemo/companyProductMaterial/exportXls",
          // importExcelUrl: "testOneDemo/companyProductMaterial/importExcel",
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
              disabled: record.status !== '1',
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
      timeFormat(val){
        if(val==null)
          return '';
        return moment(val).format('YYYY-MM-DD HH:mm:ss');
      },
      handleChange(value) {
        console.log(value)
        this.value = value;

        if(value!=="请选择"){
          this.queryParam.companyIds = this.items.find(e=>{
            return  e.value === value;
          }).key;
        }else{
          this.queryParam.companyIds = this.$store.getters.userInfo.companyIds.join(',');
        }
        console.log(this.queryParam)
      },
      localReset(){
        this.value ='';
        this.queryParam = {companyIds:this.$store.getters.userInfo.companyIds.join(',')};
        this.loadData(1);
      },
      batchPass() {
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          var ids = "";
          for (var a = 0; a < this.selectedRowKeys.length; a++) {
            ids += this.selectedRowKeys[a] + ",";
          }
          let that = this;
          this.$confirm({
            title: "确认通过",
            content: "是否批准选中数据?",
            onOk: function () {
              that.loading = true;
              postAction(that.url.batchPass, {ids: ids}).then((res) => {
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

      batchFail() {
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          var ids = "";
          for (var a = 0; a < this.selectedRowKeys.length; a++) {
            ids += this.selectedRowKeys[a] + ",";
          }
          let that = this;
          this.$confirm({
            title: "确认不通过",
            content: "是否不批准选中数据?",
            onOk: function () {
              that.loading = true;
              postAction(that.url.batchFail, {ids: ids}).then((res) => {
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
      handleView(record){
        record.isView = true;
        this.handleEdit(record);
      },
      handleEdit(record){


          //先关闭所有浮窗
          this.$refs.basicInfoModal.visible = false;
          this.$refs.acceptanceModal.visible = false;
          this.$refs.userInfoModal.visible = false;
          this.$refs.preventionModal.visible = false;
          this.$refs.envTaxModal.visible = false;
          this.$refs.dirtyAllowModal.visible = false;
          this.$refs.riskWasteModal.visible = false;
          this.$refs.cleanProductModal.visible = false;
          this.$refs.onlineInfoModal.visible = false;
          this.$refs.productmaterialModal.visible = false;
          this.$refs.envtrialModal.visible = false;
          this.$refs.qualificationAudit.visible = false;

          //打开需要展示的浮窗
          if ("company_baseinfo" === record.fromTable) {
            this.$refs.basicInfoModal.visible = true;
            this.$refs.basicInfoModal.auditModal(record);
          }
          if ("company_acceptance" === record.fromTable) {
            this.$refs.acceptanceModal.visible = true;
            this.$refs.acceptanceModal.auditModal(record);
          }
          if ("company_userinfo" === record.fromTable) {
            this.$refs.userInfoModal.visible = true;
            this.$refs.userInfoModal.auditModal(record);
          }
          if ("company_prevention" === record.fromTable) {
            this.$refs.preventionModal.visible = true;
            this.$refs.preventionModal.auditModal(record);
          }
          if ("company_env_tax" === record.fromTable) {
            this.$refs.envTaxModal.visible = true;
            this.$refs.envTaxModal.auditModal(record);
          }
          if ("company_dirty_allow" === record.fromTable) {
            this.$refs.dirtyAllowModal.visible = true;
            this.$refs.dirtyAllowModal.auditModal(record);
          }
          if("company_risk_waste"===record.fromTable){
            this.$refs.riskWasteModal.visible = true;
            this.$refs.riskWasteModal.auditModal( record);
          }
          if("company_solid_waste"===record.fromTable){
            this.$refs.solidWasteModal.visible = true;
            this.$refs.solidWasteModal.auditModal( record);
          }
          if("company_radiate_waste"===record.fromTable){
            this.$refs.radiateWasteModal.visible = true;
            this.$refs.radiateWasteModal.auditModal( record);
          }

          if ("company_clean_product" === record.fromTable) {
            this.$refs.cleanProductModal.visible = true;
            this.$refs.cleanProductModal.auditModal(record);
          }
          if ("company_online_info" === record.fromTable) {
            this.$refs.onlineInfoModal.visible = true;
            this.$refs.onlineInfoModal.auditModal(record);
          }
          if ("company_product_material" === record.fromTable) {
            this.$refs.productmaterialModal.visible = true;
            this.$refs.productmaterialModal.auditModal(record);
          }
          if ("company_env_trial" === record.fromTable) {
            this.$refs.envtrialModal.visible = true;
            this.$refs.envtrialModal.auditModal(record);
          }
        //打开需要展示的浮窗
        if ("company_qualification" === record.fromTable) {
          this.$refs.qualificationAudit.visible = true;
          this.$refs.qualificationAudit.auditModal(record);
        }
      },

    },

    mounted() {
      let that = this;
      console.log(this.$store.getters.userInfo);
      queryCompanyName({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res) => {

        if(res.success){
          that.items = res.result;
          that.items.forEach(e=>{
            if(e.key===that.value){
              that.value=e.value;
            }
          });
        }
      });
    },

  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>