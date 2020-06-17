<template>
  <div>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="申报状态">
              <j-dict-select-tag placeholder="请选择申报状态" v-model="queryParam.status" dictCode="statue" @input="searchQuery" :excludeFields="['0','4']"/>
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
   <!-- <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('company_product_material')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>-->

    <!-- table区域-begin -->
    <div>
      <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
         <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
         <a style="margin-left: 24px" @click="onClearSelected">清空</a>
       </div>-->

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
          <a @click="handleEdit(record)">审核</a>
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
    <clean-product-audit ref="cleanProductModal"></clean-product-audit>
    <online-info-audit ref="onlineInfoModal"></online-info-audit>
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
  import DirtyAllowAudit from "./modules/DirtyAllowAudit";
  import CleanProductAudit from "./modules/CleanProductAudit";
  import OnlineInfoAudit from "./modules/OnlineInfoAudit";
  import UserInfoAudit from "./modules/UserInfoAudit";
  import DirtyAllowAudit from "./modules/DirtyAllowAudit"
  import moment from 'moment'

  export default {
    name: "BaseAuditList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      AcceptanceAudit, JDictSelectTag
      ,BasicInfoAudit//基础信息审核
      ,UserInfoAudit//员工信息审核
      ,PreventionAudit,
      EnvTaxAudit,
      DirtyAllowAudit,
      CleanProductAudit,
      OnlineInfoAudit
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
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
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
    },
    methods: {
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
      handleEdit(record){
        //先关闭所有浮窗
        this.$refs.basicInfoModal.visible = false;
        this.$refs.acceptanceModal.visible = false;
        this.$refs.userInfoModal.visible = false;
        this.$refs.preventionModal.visible = false;
        this.$refs.envTaxModal.visible = false;
        this.$refs.dirtyAllowModal.visible = false;
        this.$refs.cleanProductModal.visible = false;
        this.$refs.onlineInfoModal.visible = false;
        //打开需要展示的浮窗
        if("company_baseinfo"=== record.fromTable){
          this.$refs.basicInfoModal.visible = true;
          this.$refs.basicInfoModal.auditModal( record);
        }
        if("company_acceptance"===record.fromTable){
          this.$refs.acceptanceModal.visible = true;
          this.$refs.acceptanceModal.auditModal( record);
        }
        if("company_userinfo"===record.fromTable){
          this.$refs.userInfoModal.visible = true;
          this.$refs.userInfoModal.auditModal( record);
        }
        if("company_prevention"===record.fromTable){
          this.$refs.preventionModal.visible = true;
          this.$refs.preventionModal.auditModal( record);
        }
        if("company_env_tax"===record.fromTable){
          this.$refs.envTaxModal.visible = true;
          this.$refs.envTaxModal.auditModal( record);
        }
        if("company_dirty_allow"===record.fromTable){
          this.$refs.dirtyAllowModal.visible = true;
          this.$refs.dirtyAllowModal.auditModal( record);
        }
        if("company_clean_product"===record.fromTable){
          this.$refs.cleanProductModal.visible = true;
          this.$refs.cleanProductModal.auditModal( record);
        }
        if("company_online_info"===record.fromTable){
          this.$refs.onlineInfoModal.visible = true;
          this.$refs.onlineInfoModal.auditModal( record);
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