<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="所属区域">
              <area-link-select @change="areaChange" type="cascader" v-model="queryParam.area" show-search
                                style="width: 100%" optionFilterProp="children"/>
            </a-form-item>
          </a-col>
          <a-col :xl="7" :lg="7" :md="8" :sm="24">
            <a-form-item label="企业名称">
              <a-select v-model="queryParam.companyId"
                        placeholder="请选择" show-search style="width: 100%" optionFilterProp="children">
                <!--                <a-select-option value="">请选择</a-select-option>-->
                <a-select-option v-for="item in companyNames" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="7" :lg="7" :md="8" :sm="24">
            <a-form-item label="企业类型">
              <j-dict-select-tag placeholder="请选择企业类型" v-model="queryParam.companyType" dictCode="company_type"/>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
          <a-col :xl="7" :lg="7" :md="8" :sm="24">
            <a-form-item label="所属行业">
              <j-dict-select-tag placeholder="请选择所属行业" v-model="queryParam.industry" dictCode="industry"/>
            </a-form-item>
          </a-col>
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <a-form-item label="是否重点排污单位">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.intensiveUnit" dictCode="yn"/>
            </a-form-item>
          </a-col>
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <a-form-item label="是否重点监测企业">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.intensiveCompany" dictCode="yn"/>
            </a-form-item>
          </a-col>
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <a-form-item label="站点类型">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.siteType" dictCode="siteType"/>
            </a-form-item>
          </a-col>
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <a-form-item label="监管级别">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.siteLevel" dictCode="siteLevel"/>
            </a-form-item>
          </a-col>
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <a-form-item label="运行状态">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.siteState" dictCode="siteState"/>
            </a-form-item>
          </a-col>
          </template>
        </a-row>
        <a-row>
          <a-col :md="6" :sm="8">
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
      <a-button @click="handleAdd" type="primary" icon="plus">添加污染源企业</a-button>
    </div>

    <!-- table区域-begin -->
    <div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="companyId"
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
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
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
          <a-divider type="vertical"/>
          <a-dropdown>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.companyId)">
                  <a>删除</a>
                </a-popconfirm>
          </a-dropdown>
        </span>

      </a-table>
    </div>
  <source-directory-modal ref="modalForm" @ok="modalFormOk"></source-directory-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import {mixinDevice} from '@/utils/mixin'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {queryCompanyName} from "../../requestAction/request";
  import AreaHandler from "../component/AreaHandler";
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import AreaLinkSelect from '../component/AreaLinkSelect'
  import SourceDirectoryModal from "./modules/SourceDirectoryModal";
  import {commonUtil} from "../mixin/CommonUtil";
  import {dataDictMixin} from "../mixin/dataDictMixin";

  export default {
    name: "SourceDirectoryList",
    mixins: [JeecgListMixin, mixinDevice,commonUtil,dataDictMixin],
    components: {
      JDictSelectTag,
      AreaLinkSelect,
      SourceDirectoryModal
    },
    data() {
      return {
        description: '污染源名录库管理页面',
        companyNames:[],
        companyNameOriginal:[],
        areaHandler: '',
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
            title: '企业名称',
            align: "center",
            dataIndex: 'companyName',
            customRender: this.renderContent
          },
          {
            title: '企业简称',
            align: "center",
            dataIndex: 'shortName',
            customRender: this.renderContent
          },
          {
            title: '是否重点排污单位',
            align: "center",
            dataIndex: 'intensiveUnit',
            customRender: this.renderYn
          },
          {
            title: '企业类型',
            align: "center",
            dataIndex: 'companyType',
            customRender:this.renderCompanyType

          },
          {
            title: '行政区域',
            align: "center",
            dataIndex: 'administrativeRegion',
            customRender:this.renderContentArea
          },
          {
            title: '所属行业',
            align: "center",
            dataIndex: 'industry',
            customRender:this.renderIndustry
          },
          {
            title: '站点名称',
            align: "center",
            dataIndex: 'siteName'
          },
          {
            title: '站点类型',
            align: "center",
            dataIndex: 'siteType',
            customRender:this.dictValByIndex
          },
          {
            title: '监管级别',
            align: "center",
            dataIndex: 'siteLevel',
            customRender:this.dictValByIndex
          },
          {
            title: '运行状态',
            align: "center",
            dataIndex: 'siteState',
            customRender:this.dictValByIndex
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
          list: "/sourceDirectory/sourceDirectory/list",
          delete: "/sourceDirectory/sourceDirectory/delete"
        },
        dictOptions: {},
        mergeKeys:['companyName', 'shortName' ,
        'intensiveUnit',
        'companyType',
        'administrativeRegion',
        'industry',
      ],
      }
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig() {
        this.areaHandler = new AreaHandler()
      },
      dictValByIndex(value, row, index,key){
        return this.dictVal(key.dataIndex,value)
      },
      renderCompanyType (value, row, index,key)  {
        return this.renderContent( this.dictVal("company_type",value),row,index,key)
      },
      renderYn (value, row, index,key)  {
        console.log(this.dictVal("yn",value));
        return this.renderContent( this.dictVal("yn",value),row,index,key)

      },
      renderIndustry(value, row, index,key){
        return this.renderContent( this.dictVal("industry",value),row,index,key)
      },
      initArea(){
        this.areaHandler = new AreaHandler()
      },
      getAreaByCode(text){
        if(!text)
          return '';
        //初始化
        if(this.areaHandler==='')
        {
          this.initArea()
        }
        let arr = [];
        this.areaHandler.getAreaBycode(text,arr);
        return arr[0]+arr[1]+arr[2]
      },
      //地区企业站点级联
      areaChange(val) {
        let _this = this;
        _this.items = [];
        //选择地区筛选公司名称
        _this.companyNames = [];
        if (this.queryParam.area != null) {
          console.log("1",_this.companyNameOriginal);
          _this.companyNameOriginal.forEach(b => {
            if (b.area === val) {
              _this.companyNames.push(b);
            }
          })
        } else {
          console.log("2",_this.companyNameOriginal);
          _this.companyNames = _this.companyNameOriginal;
        }
      },
      queryCompany(){
        //查询企业名称
        let that = this;
        queryCompanyName({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res) => {
          if(res.success){
            that.companyNameOriginal = res.result.companyNames;
            that.companyNames = res.result.companyNames;
            // console.log("!!",that.companyNames);
          }
        });
      }
    },
    created() {
      this.queryCompany();
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>