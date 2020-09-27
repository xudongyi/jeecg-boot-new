<template>
  <a-card :bordered="false">

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="addClick" type="primary" icon="plus" >新增</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
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
              <a-menu-item><a @click="handleDetail(record)">编辑</a></a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
        <span slot="siteState" slot-scope="siteState">
          <div :style="{color: siteState === '停用'? 'red':'black'}">{{siteState}}</div>
        </span>

      </a-table>
    </div>
    <jmodal-site ref="modalForm" @ok="ok"></jmodal-site>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import {mixinDevice} from '@/utils/mixin'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import SiteMonitorPointModal from './modules/SiteMonitorPointModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import Area from '@/components/_util/Area'
  import {queryCompanyName} from '../../../requestAction/request'
  import JmodalSite from "./modules/JmodalSite";
  import AreaLinkSelect from "../../../component/AreaLinkSelect";

  export default {
    name: "SiteMonitorPointList",
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      JDictSelectTag,
      AreaLinkSelect,
      SiteMonitorPointModal,
      JmodalSite
    },
    data() {
      return {
        description: '监测站点信息',
        disable: true,
        addButton:true,
        queryParam: {
          companyId: this.companyId
        },
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: "center",
            customRender:this.calcIndex
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
            title: '站点级别',
            align: "center",
            dataIndex: 'siteLevel_dictText'
          },
          {
            title: '数采仪MN号',
            align: "center",
            dataIndex: 'mn'
          },
          {
            title: '站点状态',
            align: "center",
            dataIndex: 'siteState_dictText',
            scopedSlots: { customRender: 'siteState' },
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
          list: "/siteInfo/list",
          delete: "/siteInfo/delete",
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
      // this.orginalcolumns.forEach(e=>{
      //   if(e.dataIndex!=='imorex_dictText')
      //     this.columns.push(e)
      // })
    },
    watch: {
      "$route": {
        handler(route) {
            this.loadData(1);
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
        this.addButton = true;
        this.$emit("detail", record,this.disable,this.addButton)
      },
      addClick() {
        this.$refs.modalForm.visible = true;
        this.$refs.modalForm.companyId = this.companyId;
        this.$refs.modalForm.confirmLoading = false;
        this.disable = false;
        this.addButton = false;
        this.$refs.modalForm.addClick(this.disable,this.addButton);
      },
      ok() {
        this.loadData(1)
      },
      calcIndex: function (t,r,index) {
        return parseInt(index)+1+(this.ipagination.current-1)*this.ipagination.pageSize;
      },
    },
    props:{
      companyId:""
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>