<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button v-if="latestArchived.total>0" @click="toDetail" type="primary" icon="profile">查看</a-button>
      <a-button  v-if="latestArchived.pend===0" @click="toApply" type="primary" icon="form">申报</a-button>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('企业申报基础表')">导出</a-button>
       <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
         <a-button type="primary" icon="import">导入</a-button>
       </a-upload>-->
      <!-- <a-dropdown v-if="selectedRowKeys.length > 0">
         <a-menu slot="overlay">
           <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
         </a-menu>
         <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
       </a-dropdown>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <!--<div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
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
        <template slot="title" slot-scope="currentPageData">
          申报记录
        </template>
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
          <a @click="handleToDetail(record)">查看</a>

          <!--<a-divider type="vertical" />
          <a-dropdown>
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
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import CompanyApplyModal from "./childModules/CompanyApplyModal";
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import {queryLatestArchivedData} from "../../../requestAction/request";
  import moment from "moment";

  export default {
    name: "CompanyApplyList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      CompanyApplyModal
    },
  props:{
    companyId:'',
    fromTable:'',

  },
    data () {
      return {
        description: '企业申报基础表管理页面',
        queryParam:{
          companyId:this.companyId,
          fromTable:this.fromTable,
          status : '!0'//不查询暂存

        },
        latestArchived:{},
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
            title:'申报日期',
            align:"center",
            dataIndex: 'createTime',
            customRender:function (text) {
              return moment(text).format('YYYY-MM-DD HH:mm:ss')
            }
          },
          {
            title:'生效日期',
            align:"center",
            dataIndex: 'updateTime',
            customRender:function (text) {
              return text==null||text==="" ? text:moment(text).format('YYYY-MM-DD HH:mm:ss')
            }
          },
          {
            title: '数据状态',
            align: "center",
            dataIndex: 'status_dictText'
          },

          {
            title: '详情',
            dataIndex: 'action',
            align:"center",
            // fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/company/apply/list",
          // delete: "/testOneDemo/companyApply/delete",
          // deleteBatch: "/testOneDemo/companyApply/deleteBatch",
          // exportXlsUrl: "/testOneDemo/companyApply/exportXls",
          // importExcelUrl: "testOneDemo/companyApply/importExcel",
        },
        dictOptions:{},
      }
    },
    methods: {
      calcIndex: function (t,r,index) {

        return parseInt(index)+1+(this.ipagination.current-1)*this.ipagination.pageSize;
      },
      initDictConfig(){
      },
      handleToDetail(record)
      {
        this.$emit("applyDetail",record);
      } ,
      toDetail(){
        this.$emit("toDetail");
      },
      toApply(){
        this.$emit("toApply");
      },
      init(){
        let that = this;
        //查询最新归档信息
        queryLatestArchivedData({companyId:this.companyId,fromTable:this.fromTable}).then((res)=>{
          if(res.success){
            that.latestArchived = res.result;
          }else{
            that.$message.warning(res.message);
          }

        });
      },
      reInit(){
        this.loadData();
        this.init();
      }

    },
    mounted() {
      this.init();
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>