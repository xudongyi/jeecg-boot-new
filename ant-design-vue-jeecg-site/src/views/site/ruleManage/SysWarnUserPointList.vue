<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="姓名">
              <a-input placeholder="请输入姓名" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="手机">
              <a-input placeholder="请输入手机" v-model="queryParam.mobile"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
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
              <a-menu-item>
                <a @click="toHandleEdit(record)">编辑</a>
              </a-menu-item>
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

      </a-table>
    </div>

    <sysWarnUserPoint-modal ref="modalForm" @ok="modalFormOk"></sysWarnUserPoint-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SysWarnUserPointModal from './modules/SysWarnUserPointModal'
  import {commonUtil} from "../mixins/CommonUtil";

  export default {
    name: "SysWarnUserPointList",
    mixins:[JeecgListMixin, mixinDevice,commonUtil],
    components: {
      SysWarnUserPointModal
    },
    data () {
      return {
        description: '站点报警短信接收人配置管理页面',
        mergeKeys:['name','mobile','companyName'],
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
            title:'联系人姓名',
            align:"center",
            dataIndex: 'name',
            customRender: this.renderContent
          },
          {
            title:'手机号码',
            align:"center",
            dataIndex: 'mobile',
            customRender: this.renderContent
          },
          {
            title:'所属单位',
            align:"center",
            dataIndex: 'companyName',
            customRender: this.renderContent

          },
          {
            title:'责任站点',
            align:"center",
            dataIndex: 'siteName'
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
          list: "/swup/sysWarnUserPoint/list",
          delete: "/swup/sysWarnUserPoint/delete",
          deleteBatch: "/swup/sysWarnUserPoint/deleteBatch",
          exportXlsUrl: "/swup/sysWarnUserPoint/exportXls",
          importExcelUrl: "swup/sysWarnUserPoint/importExcel",
        },
        dictOptions:{},
        spanArr:[],
        position:0,
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
            if(userData[index].mobile === userData[index-1].mobile ){
              _this.spanArr[ _this.position] += 1;
              _this.spanArr.push(0);
            }else{
              _this.spanArr.push(1);
              _this.position = index;
            }
          }
        });
      },
      initDictConfig(){
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
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>