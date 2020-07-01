<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="新国标污染因子">
              <a-input placeholder="请输入新国标污染因子" v-model="queryParam.code"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="污染因子名称">
              <a-input placeholder="请输入污染因子名称" v-model="queryParam.meaning"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="污染因子类型">
                <j-dict-select-tag placeholder="请选择污染因子类型" v-model="queryParam.type" dictCode="pollution_code_type"/>
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
      <a-button type="primary" icon="download" @click="handleExportXls('污染因子表')">导出</a-button>
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

    <sysPollutionCode-modal ref="modalForm" @ok="modalFormOk"></sysPollutionCode-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SysPollutionCodeModal from '../../../../../jeecg-boot/jeecg-boot-module-site/src/main/java/org/jeecg/modules/business/src/main/java/org/jeecg/modules/business/spc/vue/modules/SysPollutionCodeModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "SysPollutionCodeList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      JDictSelectTag,
      SysPollutionCodeModal
    },
    data () {
      return {
        description: '污染因子表管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'新国标污染因子',
            align:"center",
            dataIndex: 'code'
          },
          {
            title:'污染因子名称',
            align:"center",
            dataIndex: 'meaning'
          },
          {
            title:'老国标污染因子',
            align:"center",
            dataIndex: 'oldCode'
          },
          {
            title:'浓度单位',
            align:"center",
            dataIndex: 'chromaUnit'
          },
          {
            title:'计量单位-浓度-数学表达式',
            align:"center",
            dataIndex: 'chromaUnitMath'
          },
          {
            title:'排放量单位',
            align:"center",
            dataIndex: 'amountUnit'
          },
          {
            title:'计量单位-总量-数学表达式',
            align:"center",
            dataIndex: 'amountUnitMath'
          },
          {
            title:'是否有折算值',
            align:"center",
            dataIndex: 'isZs_dictText'
          },
          {
            title:'格式值',
            align:"center",
            dataIndex: 'format'
          },
          {
            title:'污染因子类型',
            align:"center",
            dataIndex: 'type_dictText'
          },
          {
            title:'数据是否重复',
            align:"center",
            dataIndex: 'isRepeat_dictText'
          },
          {
            title:'是否含有排放量',
            align:"center",
            dataIndex: 'isTotal_dictText'
          },
          {
            title:'是否为主要污染物',
            align:"center",
            dataIndex: 'isImportant_dictText'
          },
          {
            title:'异常值上限',
            align:"center",
            dataIndex: 'errorMax'
          },
          {
            title:'异常值下限',
            align:"center",
            dataIndex: 'errorMin'
          },
          {
            title:'排序',
            align:"center",
            dataIndex: 'sort'
          },
          {
            title:'是否启用',
            align:"center",
            dataIndex: 'isUse_dictText'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'content'
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy'
          },
          {
            title:'创建时间',
            align:"center",
            dataIndex: 'createTime'
          },
          {
            title:'更新人',
            align:"center",
            dataIndex: 'updateBy'
          },
          {
            title:'更新时间',
            align:"center",
            dataIndex: 'updateTime'
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
          list: "/spc/sysPollutionCode/list",
          delete: "/spc/sysPollutionCode/delete",
          deleteBatch: "/spc/sysPollutionCode/deleteBatch",
          exportXlsUrl: "/spc/sysPollutionCode/exportXls",
          importExcelUrl: "spc/sysPollutionCode/importExcel",
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