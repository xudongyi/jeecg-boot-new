<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="联系人">
              <a-input placeholder="请输入姓名" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="手机号码">
              <a-input placeholder="请输入手机" v-model="queryParam.mobile"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="所属单位">
              <a-select v-model="queryParam.companyId" :allowClear="allowClear" placeholder="请选择" show-search style="width: 100%" optionFilterProp="children">
                <a-select-option v-for="item in companyNames" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="责任单位">
              <a-select v-model="queryParam.zrCompanyId" :allowClear="allowClear" placeholder="请选择" show-search style="width: 100%" optionFilterProp="children">
                <a-select-option v-for="item in companyNames" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="责任站点">
              <a-select v-model="queryParam.mn" :allowClear="allowClear" placeholder="请选择" show-search style="width: 100%" optionFilterProp="children">
                <a-select-option v-for="item in items" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;margin-left:15px" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button @click="batchDel" type="primary" icon="delete">删除</a-button>
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
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
              <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                <a>删除</a>
              </a-popconfirm>
        </span>

      </a-table>
    </div>

    <MsgUserModal ref="modalForm"></MsgUserModal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import {commonUtil} from "../mixin/CommonUtil";
  import {tableMixin} from "../mixin/tableMixin";
  import {queryCompanyName,querySiteNameAndMn} from "../../requestAction/request";
  import MsgUserModal from "./modules/MsgUserModal";
  import {getAction} from "../../../api/manage";

  export default {
    name: "MsgUserList",
    mixins:[tableMixin,commonUtil],
    components: {
      MsgUserModal
    },
    data () {
      return {
        description: '短信联系人配置',
        mergeKeys:['name','mobile','companyName'],
        queryParam:{
          companyIds: this.$store.getters.userInfo.companyIds.join(','),
        },
        allowClear: true,
        companyNames: [],
        items:[],
        siteType: '0,1,2',
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
            title:'责任单位',
            align:"center",
            dataIndex: 'zrCompanyName'
          },
          {
            title:'责任站点',
            align:"center",
            dataIndex: 'siteName'
          },
          {
            title:'短信接收策略',
            align:"center",
            dataIndex: 'ruleTypeName'
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
          list: "/wr/warnRule/listMsgRule",
          delete: "/wr/warnRule/deleteX",
          deleteBatch: "/wr/warnRule/deleteBatchX",
        },
        dictOptions:{},
        spanArr:[],
        position:0,
      }
    },
    created(){
      //查询企业名称
      queryCompanyName({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res) => {
        if(res.success){
          this.companyNames = res.result.companyNames;
        }
      });
      querySiteNameAndMn({companyIds:this.$store.getters.userInfo.companyIds.join(','),siteType:this.siteType}).then((res)=>{
        if(res.success){
          //console.log("!!",res.result);
          var a=[];
          res.result.forEach(e=>{
            if(e.siteType === '0' || e.siteType === '1' || e.siteType === '2'){
              a.push(e)
            }
          });
          this.items = a;
        }
      });
      this.queryData(1);
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
      searchQuery(){
        this.loadData(1);
      },
      queryData(arg){
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result;
            this.ipagination.total = res.result.length;

          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
        //对param
      },
      handleTableChange(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        //TODO 筛选
        if (Object.keys(sorter).length > 0) {
          this.isorter.order = "ascend" == sorter.order ? "asc" : "desc"
        }
        this.ipagination = pagination;
        this.queryData();
      },
      handleAdd: function () {
        this.$refs.modalForm.add();
        this.$refs.modalForm.title = "新增";
        this.$refs.modalForm.disableSubmit = false;
      },
      handleEdit: function (record) {
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "编辑";
        this.$refs.modalForm.disableSubmit = false;
      },
      batchDel: function () {
        if(!this.url.deleteBatch){
          this.$message.error("请设置url.deleteBatch属性!");
          return
        }
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
            title: "确认删除",
            content: "是否删除选中数据?",
            onOk: function () {
              that.loading = true;
              deleteAction(that.url.deleteBatch, {ids: ids}).then((res) => {
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
      handleDelete: function (id) {
        if(!this.url.delete){
          this.$message.error("请设置url.delete属性!");
          return
        }
        var that = this;
        deleteAction(that.url.delete, {id: id}).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.loadData();
          } else {
            that.$message.warning(res.message);
          }
        });
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>