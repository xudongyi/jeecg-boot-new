<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="站点名称">
              <a-input placeholder="请输入站点名称" v-model="queryParam.siteName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="站点类型">
              <j-dict-select-tag placeholder="请选择站点类型" v-model="queryParam.siteType" dictCode="siteType" :disabled="isDetail"/>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="所属单位">
                <a-select v-model="queryParam.companyId" show-search style="width: 100%" optionFilterProp="children">
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="item in items" :key="item.value" :value="item.key">
                    {{item.value}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="站点级别">
                <j-dict-select-tag placeholder="请选择站点级别" v-model="queryParam.siteLevel" dictCode="siteLevel"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="所属区域">
                <area-link-select type="cascader" v-model="queryParam.area" placeholder="请选择省市区"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="站点状态">
                <j-dict-select-tag placeholder="请选择站点状态" v-model="queryParam.siteState" dictCode="siteState"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="数采仪MN号">
                <a-input placeholder="请输入数采仪MN号" v-model="queryParam.mnCode"></a-input>
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
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
      <a-button @click="addClick" type="primary" icon="plus" v-if="isDetail">新增</a-button>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('监测站点表')">导出</a-button>
       <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
         <a-button type="primary" icon="import">导入</a-button>
       </a-upload>-->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;" v-if="isDetail">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项
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
        @change="handleTableChange"
        v-if="!isDetail">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="pcaSlot" slot-scope="text">
          <div>{{ getPcaText(text) }}</div>
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
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item><a @click="handleDetail(record)" v-if="isDetail">编辑</a></a-menu-item>
              <a-menu-item><a @click="viewDetail(record)">详情</a></a-menu-item>
              <a-menu-item v-if="isDetail">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>

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
        @change="handleTableChange"
        v-if="isDetail">
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="pcaSlot" slot-scope="text">
          <div>{{ getPcaText(text) }}</div>
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
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item><a @click="handleDetail(record)" v-if="isDetail">编辑</a></a-menu-item>
              <a-menu-item><a @click="viewDetail(record)">详情</a></a-menu-item>
              <a-menu-item v-if="isDetail">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <jmodal-site ref="modalForm" @ok="ok" :disable="disable"></jmodal-site>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import {mixinDevice} from '@/utils/mixin'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import SiteMonitorPointModal from './modules/SiteMonitorPointModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import Area from '@/components/_util/Area'
  import {queryCompanyName} from '../../requestAction/request'
  import SiteDetail from "./SiteDetail";
  import JmodalSite from "./modules/JmodalSite";
  import AreaLinkSelect from "../component/AreaLinkSelect";

  export default {
    name: "SiteMonitorPointList",
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      JDictSelectTag,
      AreaLinkSelect,
      SiteMonitorPointModal,
      SiteDetail,
      JmodalSite
    },
    data() {
      return {
        description: '监测站点表管理页面',
        isDetail: true,
        queryParam: {
          siteType: this.$route.params.siteType
        },
        disable: true,
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: "center",
            customRender: function (t, r, index) {
              return parseInt(index) + 1;
            }
          },
          {
            title: '站点名称',
            align: "center",
            dataIndex: 'siteName'
          },
          {
            title: '站点类型',
            align: "center",
            dataIndex: 'siteType_dictText'
          },
          {
            title: '所属单位',
            align: "center",
            dataIndex: 'companyName'
          },
          {
            title: '站点级别',
            align: "center",
            dataIndex: 'siteLevel_dictText'
          },
          {
            title: '所属区域',
            align: "center",
            dataIndex: 'area',
            scopedSlots: {customRender: 'pcaSlot'}
          },
          {
            title: '站点状态',
            align: "center",
            dataIndex: 'siteState_dictText'
          },
          {
            title: '数采仪MN号',
            align: "center",
            dataIndex: 'mnCode'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: "center",
            // fixed:"right",
            width: 147,
            scopedSlots: {customRender: 'action'}
          }
        ],
        url: {
          list: "/site/siteMonitorPoint/list",
          delete: "/site/siteMonitorPoint/delete",
          deleteBatch: "/site/siteMonitorPoint/deleteBatch",
          /* exportXlsUrl: "/site/siteMonitorPoint/exportXls",
           importExcelUrl: "site/siteMonitorPoint/importExcel",*/
        },
        dictOptions: {},
        pcaData: ''
      }
    },
    created() {
      // this.columns = this.orginalcolumns
      this.pcaData = new Area()
      queryCompanyName().then((res) => {
        if (res.success) {
          this.items = res.result;
        }
      })
      if(!this.$route.params.siteType){
        this.isDetail=false;
      }
      // this.orginalcolumns.forEach(e=>{
      //   if(e.dataIndex!=='imorex_dictText')
      //     this.columns.push(e)
      // })
    },
    watch: {
      "$route": {
        handler(route) {
          this.siteType = this.$route.params.siteType
          if (this.$route.params.siteType) {
            this.queryParam.siteType = this.$route.params.siteType;
            this.loadData(1);
          }
        },
      }
    },
    /*computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },*/
    methods: {
      getPcaText(code) {
        return this.pcaData.getText(code);
      },
      initDictConfig() {
      },
      handleDetail(record) {
        this.disable = false;
        this.$emit("detail", record,this.disable)
      },
      addClick() {
        this.$refs.modalForm.visible = true;
        this.$refs.modalForm.confirmLoading = false;
        this.$refs.modalForm.addClick(this.queryParam.siteType)
        this.disable = false;
      },
      viewDetail(record) {
        debugger
        this.disable = true;
        this.$emit("detail", record,this.disable);
      },
      ok() {
        this.loadData(1)
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>