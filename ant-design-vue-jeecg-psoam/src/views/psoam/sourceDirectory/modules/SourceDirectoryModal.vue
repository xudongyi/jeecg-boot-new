<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-row :gutter="24">
      <a-col :xl="6" :lg="7" :md="8" :sm="24">
        <a-form-item label="企业名称">
          <a-select v-model="queryParam.companyId" show-search style="width: 100%" optionFilterProp="children">
            <a-select-option :value="companyIds">请选择</a-select-option>
            <a-select-option v-for="item in items" :key="item.value" :value="item.key">
              {{item.value}}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
        <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
            </span>
      </a-col>
    </a-row>
    <a-row type="flex" justify="space-around" align="top">
      <a-col :span="9">
        <a-card title="未选择企业1家">
          <a-table
            ref="table"
            size="middle"
            bordered
            rowKey="id"
            :columns="columns"
            :dataSource="unSelectDataSource"
            :pagination="unSelectIpagination"
            :loading="loading"
            :rowSelection="{selectedRowKeys: unSelectedRowKeys, onChange: unSelectChange}"
            class="j-table-force-nowrap"
            @change="handleUnSelectTableChange"
          >

          </a-table>
        </a-card>
      </a-col>
      <a-col :span="4">
        <div style="height: 800px">
          <a-button-group style="top: 50%;">
            <a-button type="primary" @click="deleteCompany" :disabled="selectDisable">
              <a-icon type="left"/>
              删除
            </a-button>
            <a-button type="primary" @click="addCompany" :disabled="unSelectDisable"> 新增
              <a-icon type="right"/>
            </a-button>
          </a-button-group>
        </div>
      </a-col>
      <a-col :span="9">
        <a-card title="已选选择企业2家">
          <a-table
            ref="table"
            size="middle"
            bordered
            rowKey="id"
            :columns="columns"
            :dataSource="selectDataSource"
            :pagination="selectIpagination"
            :loading="loading"
            :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: selectChange}"
            class="j-table-force-nowrap"
            @change="handleSelectTableChange"
          >

          </a-table>
        </a-card>
      </a-col>
    </a-row>
  </a-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import {validateDuplicateValue} from '@/utils/util'
  import {queryCompanyName} from "../../../requestAction/request";
  import {getAction, postAction} from '@/api/manage'
  import {filterObj} from '@/utils/util';

  export default {
    name: "SourceDirectoryModal",
    components: {},
    data() {
      return {
        unSelectDisable: true,
        selectDisable: true,
        columns: [
          {
            title: '企业名称',
            align: "center",
            dataIndex: 'company_name'
          }],
        unSelectDataSource: [],
        unSelectIpagination: {
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        selectDataSource: [],
        selectIpagination: {
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        unSelectedRowKeys: [],
        unSelectionRows: [],
        selectedRowKeys: [],
        selectionRows: [],
        loading: false,
        queryParam: [],
        items: [],
        companyIds: this.$store.getters.userInfo.companyIds.join(','),
        /* 高级查询条件生效状态 */
        superQueryFlag: false,
        /* 高级查询条件 */
        superQueryParams: '',
        /** 高级查询拼接方式 */
        superQueryMatchType: 'and',
        stayAdd: [],
        stayDelete: [],
        form: this.$form.createForm(this),
        title: "操作",
        width: 1000,
        visible: false,
        model: {},
        labelCol: {
          xs: {span: 24},
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        confirmLoading: false,
        validatorRules: {
          companyId: {
            rules: [
              {required: true, message: '请输入企业id!'},
            ]
          },
          intensiveUnit: {
            rules: [
              {required: true, message: '请输入重点排污单位 0:否   1:是!'},
            ]
          },
          intensiveCompany: {
            rules: [
              {required: true, message: '请输入重点监控企业 0:否   1:是!'},
            ]
          },
        },
        url: {
          selectList: "/sourceDirectory/sourceDirectory/selectList",
          unSelectList: "/sourceDirectory/sourceDirectory/unSelectList",
          edit: "/sourceDirectory/sourceDirectory/edit"
        }
      }
    },
    created() {
      this.queryCompany();
      this.loadUnSelectData(1);
      this.loadSelectData(1);
    },
    methods: {
      add() {
        this.edit({});
      },
      edit(record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'companyId', 'sourceType', 'intensiveUnit', 'intensiveCompany'))
        })
      },
      close() {
        this.$emit('close');
        this.visible = false;
      },
      handleOk() {
        const that = this;
        postAction(this.url.edit, {stayAdd: this.stayAdd, stayDelete: this.stayDelete}).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.$emit('ok');
          } else {
            that.$message.warning(res.message);
          }
        }).finally(() => {
          that.confirmLoading = false;
          that.close();
        })
    },
    handleCancel() {
      this.close()
    },
    popupCallback(row) {
      this.form.setFieldsValue(pick(row, 'companyId', 'sourceType', 'intensiveUnit', 'intensiveCompany'))
    },
    addCompany() {
      this.unSelectionRows.forEach(item => {
        this.selectDataSource.push(item);
        //待新增
        this.stayAdd.push(item.id);
        this.removeDataSource(this.unSelectDataSource, item);
      })
      this.unSelectIpagination.total = this.unSelectIpagination.total - this.unSelectionRows.length;
      this.selectIpagination.total = this.selectIpagination.total + this.unSelectionRows.length;
      if (this.selectIpagination.total > 0) {
        this.selectDisable = false;
      } else {
        this.selectDisable = true;
      }
      if (this.unSelectIpagination.total > 0) {
        this.unSelectDisable = false;
      } else {
        this.unSelectDisable = true;
      }
    },
    deleteCompany() {
      this.selectionRows.forEach(item => {
        this.unSelectDataSource.push(item);
        //待删除
        this.stayDelete.push(item.id);
        this.removeDataSource(this.selectDataSource, item);
      })
      this.selectIpagination.total = this.selectIpagination.total - this.selectionRows.length;
      this.unSelectIpagination.total = this.unSelectIpagination.total + this.selectionRows.length;
      if (this.selectIpagination.total > 0) {
        this.selectDisable = false;
      } else {
        this.selectDisable = true;
      }
      if (this.unSelectIpagination.total > 0) {
        this.unSelectDisable = false;
      } else {
        this.unSelectDisable = true;
      }
    },
    loadSelectData(arg) {
      if (!this.url.selectList) {
        this.$message.error("请设置url.list属性!")
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.selectIpagination.current = 1;
      }
      var params = this.getSelectQueryParams();//查询条件
      this.loading = true;
      getAction(this.url.selectList, params).then((res) => {
        if (res.success) {
          this.selectDataSource = res.result.records;
          this.selectIpagination.total = res.result.total;
          if (res.result.total > 0) {
            this.selectDisable = false;
          }
        }
        if (res.code === 510) {
          this.$message.warning(res.message)
        }
        this.loading = false;
      })
    },
    loadUnSelectData(arg) {
      if (!this.url.unSelectList) {
        this.$message.error("请设置url.list属性!")
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.unSelectIpagination.current = 1;
      }
      var params = this.getUnSelectQueryParams();//查询条件
      this.loading = true;
      getAction(this.url.unSelectList, params).then((res) => {
        if (res.success) {
          this.unSelectDataSource = res.result.records;
          this.unSelectIpagination.total = res.result.total;
          if (res.result.total > 0) {
            this.unSelectDisable = false;
          }
        }
        if (res.code === 510) {
          this.$message.warning(res.message)
        }
        this.loading = false;
      })
    },
    handleSelectTableChange(pagination, filters, sorter) {
      //分页、排序、筛选变化时触发
      //TODO 筛选
      if (Object.keys(sorter).length > 0) {
        this.isorter.column = sorter.field;
        this.isorter.order = "ascend" == sorter.order ? "asc" : "desc"
      }
      this.selectIpagination = pagination;
      this.loadSelectData();
    },
    handleUnSelectTableChange(pagination, filters, sorter) {
      //分页、排序、筛选变化时触发
      //TODO 筛选
      if (Object.keys(sorter).length > 0) {
        this.isorter.column = sorter.field;
        this.isorter.order = "ascend" == sorter.order ? "asc" : "desc"
      }
      this.unSelectIpagination = pagination;
      this.loadUnSelectData();
    },
    selectChange(selectedRowKeys, selectionRows) {
      this.selectedRowKeys = selectedRowKeys;
      this.selectionRows = selectionRows;
    },
    onClearSelected() {
      this.selectedRowKeys = [];
      this.selectionRows = [];
    },
    unSelectChange(selectedRowKeys, selectionRows) {
      this.unSelectedRowKeys = selectedRowKeys;
      this.unSelectionRows = selectionRows;
    },
    onClearUnSelected() {
      this.unSelectedRowKeys = [];
      this.unSelectionRows = [];
    },
    queryCompany() {
      //查询企业名称
      let that = this;
      queryCompanyName({companyIds: this.$store.getters.userInfo.companyIds.join(',')}).then((res) => {
        if (res.success) {
          that.items = res.result;
        }
      });
    },
    searchQuery() {
      this.loadUnSelectData(1);
      this.loadSelectData(1);
    },
    handleSuperQuery(params, matchType) {
      //高级查询方法
      if (!params) {
        this.superQueryParams = ''
        this.superQueryFlag = false
      } else {
        this.superQueryFlag = true
        this.superQueryParams = JSON.stringify(params)
        this.superQueryMatchType = matchType
      }
      this.loadData(1)
    },
    getUnSelectQueryParams() {
      //获取查询条件
      let sqp = {}
      if (this.superQueryParams) {
        sqp['superQueryParams'] = encodeURI(this.superQueryParams)
        sqp['superQueryMatchType'] = this.superQueryMatchType
      }
      var param = Object.assign(sqp, this.queryParam, this.isorter, this.filters);
      param.field = this.getQueryField();
      param.pageNo = this.unSelectIpagination.current;
      param.pageSize = this.unSelectIpagination.pageSize;
      return filterObj(param);
    },
    getSelectQueryParams() {
      //获取查询条件
      let sqp = {}
      if (this.superQueryParams) {
        sqp['superQueryParams'] = encodeURI(this.superQueryParams)
        sqp['superQueryMatchType'] = this.superQueryMatchType
      }
      var param = Object.assign(sqp, this.queryParam, this.isorter, this.filters);
      param.field = this.getQueryField();
      param.pageNo = this.selectIpagination.current;
      param.pageSize = this.selectIpagination.pageSize;
      return filterObj(param);
    },
    getQueryField() {
      //TODO 字段权限控制
      var str = "id,";
      this.columns.forEach(function (value) {
        str += "," + value.dataIndex;
      });
      return str;
    },
    removeDataSource(array, val) {
      var index = array.indexOf(val);
      if (index > -1) {
        array.splice(index, 1);
      }
    }
  }
  }
</script>