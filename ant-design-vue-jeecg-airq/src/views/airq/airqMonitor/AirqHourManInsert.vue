<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="10" :lg="11" :md="12" :sm="24">
            <a-form-item label="数据时间">
              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间" class="query-group-cust" v-model="queryParam.dataTime_begin"></j-date>
              <span class="query-group-split-cust"></span>
              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间" class="query-group-cust" v-model="queryParam.dataTime_end"></j-date>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="小时数据平台状态">
                <j-dict-select-tag placeholder="请选择小时数据平台状态"  dictCode="airDataStatus" :excludeFields="['0']"/>
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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('airq_hour')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
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

    <airqHour-modal ref="modalForm" @ok="modalFormOk"></airqHour-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import AirqHourModal from './modules/AirqHourModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import JDate from '@/components/jeecg/JDate.vue'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "AirqHourManInsert",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      JDictSelectTag,
      JDate,
      AirqHourModal
    },
    data () {
      return {
        description: 'airq_hour管理页面',
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
            title:'行政区域',
            align:"center",
            dataIndex: 'area',
            customRender:this.getAreaByCode,
          },
          {
            title:'监测点位名称',
            align:"center",
            dataIndex: 'siteName',
          },
          {
            title:'数据时间',
            align:"center",
            dataIndex: 'dataTime'
          },
          {
            title:'小时数据平台状态',
            align:"center",
            dataIndex: 'state_dictText'
          },
          {
            //title:'a21026Avg',
            title:'SO2 μg/m3',
            align:"center",
            dataIndex: 'a21026Avg'
          },
          {
            //title:'a21004Avg',
            title:'NO2 μg/m3',
            align:"center",
            dataIndex: 'a21004Avg'
          },
          {
            //title:'a3400201Avg',
            title:'PM10(1h)μg/m3',
            align:"center",
            dataIndex: 'a3400201Avg'
          },
          {
            //title:'a3400224Avg',
            title:'PM10(24h)μg/m3',
            align:"center",
            dataIndex: 'a3400224Avg'
          },
          {
            //title:'a21005Avg',
            title:'COμg/m3',
            align:"center",
            dataIndex: 'a21005Avg'
          },
          {
            //title:'a0502401Avg',
            title:'O3(1h)μg/m3',
            align:"center",
            dataIndex: 'a0502401Avg'
          },
          {
            //title:'a0502408Avg',
            title:'O3(8h)μg/m3',
            align:"center",
            dataIndex: 'a0502408Avg'
          },
          {
            //title:'a3400401Avg',
            title:'PM2.5(1h)μg/m3',
            align:"center",
            dataIndex: 'a3400401Avg'
          },
          {
            //title:'a3400424Avg',
            title:'PM2.5(24h)μg/m3',
            align:"center",
            dataIndex: 'a3400424Avg'
          },
          {
            //title:'a01002Avg',
            title:'温度(°C)',
            align:"center",
            dataIndex: 'a01001Avg'
          },
          {
            //title:'a01002Avg',
            title:'湿度(%)',
            align:"center",
            dataIndex: 'a01002Avg'
          },
          {
            //title:'a01007Avg',
            title:'风速(m/s)',
            align:"center",
            dataIndex: 'a01007Avg'
          },
          {
            //title:'a21003Avg',
            title:'风向',
            align:"center",
            dataIndex: 'a01008Avg'
          },
          {
            // title:'a01006Avg',
            title:'气压(kPa)',
            align:"center",
            dataIndex: 'a01006Avg'
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
          list: "/hour/airqHour/queryManInsertAirqHour",
          delete: "/hour/airqHour/delete",
          deleteBatch: "/hour/airqHour/deleteBatch",
          exportXlsUrl: "/hour/airqHour/exportXls",
          importExcelUrl: "hour/airqHour/importExcel",
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