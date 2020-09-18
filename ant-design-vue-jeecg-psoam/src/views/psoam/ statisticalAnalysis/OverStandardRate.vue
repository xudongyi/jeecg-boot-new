<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
      <a-row  :gutter="24">

          <a-col :xl="7" :lg="12" :md="12" :sm="24" >
            <a-form-item label="监测类型">
              <a-radio-group  button-style="solid" :value="queryParam.moniterType" @change="moniterChange">
                <a-radio-button  value="0,1,2">
                  全部
                </a-radio-button>
                <a-radio-button  value="0">
                  废水
                </a-radio-button>
                <a-radio-button value="1">
                  废气
                </a-radio-button>
                <a-radio-button value="2">
                  VOCS
                </a-radio-button>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :xl="9" :lg="12" :md="12" :sm="24" >
            <a-form-item label="数据类型">
              <a-radio-group  button-style="solid" :value="queryParam.dateType" @change="dateTypeChange" >
                <a-radio-button  value="date">
                  日数据
                </a-radio-button>
                <a-radio-button value="month">
                  月数据
                </a-radio-button>
                <a-radio-button value="quarterly">
                  季度数据
                </a-radio-button>
                <a-radio-button value="year">
                  年度数据
                </a-radio-button>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :xl="8" :lg="12" :md="12" :sm="24">
            <a-form-item label="日期">
              <a-date-picker :show-time="false"
                             :format="dateFormat[queryParam.dateType].value" placeholder="请选择开始时间"
                             :valueFormat="dateFormat[queryParam.dateType].value"
                             :mode="dateFormat[queryParam.dateType].dataType"
                             class="query-group-cust"  v-model="queryParam.dataTime_begin"   />
              <span class="query-group-split-cust" v-show="queryParam.dateType!=='quarterly'"></span>
              <a-date-picker :show-time="false" v-show="queryParam.dateType!=='quarterly'"
                             :format="dateFormat[queryParam.dateType].value" placeholder="请选择结束时间"
                             :valueFormat="dateFormat[queryParam.dateType].value"
                             :mode="dateFormat[queryParam.dateType].dataType"
                             class="query-group-cust"  v-model="queryParam.dataTime_end"  />
              <a-select v-model="queryParam.quarterly" :dropdownMatchSelectWidth="false"  v-show="queryParam.dateType==='quarterly'"
                        style="margin-left: 5px;width: calc(50% - 10px);"
                         :allowClear="true" placeholder="请选择" show-search optionFilterProp="children">
                <!--                <a-select-option value="">请选择</a-select-option>-->
                <a-select-option  key="第一季度" :value="1">
                  第一季度
                </a-select-option>
                <a-select-option  key="第二季度" :value="2">
                  第二季度
                </a-select-option>
                <a-select-option  key="第三季度" :value="3">
                  第三季度
                </a-select-option>
                <a-select-option  key="第四季度" :value="4">
                  第四季度
                </a-select-option>

              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row  :gutter="24">
          <a-col :xl="5" :lg="6" :md="7" :sm="24">
            <a-form-item label="行政区域">
              <area-link-select @change="areaChange" type="cascader" v-model="queryParam.area" show-search style="width: 100%" optionFilterProp="children"/>
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="6" :md="7" :sm="24">
            <a-form-item label="企业名称">
              <a-select v-model="queryParam.companyId" @change="companyNameChange" :allowClear="allowClear" placeholder="请选择" show-search style="width: 100%"
                        optionFilterProp="children">
                <a-select-option v-for="item in companyNames" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="6" :md="7" :sm="24">
            <a-form-item label="监测点名称">
              <a-select v-model="queryParam.mn" :allowClear="allowClear" placeholder="请选择" show-search style="width: 100%" optionFilterProp="children">
                <a-select-option v-for="item in items" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="6" :md="7" :sm="24">
            <span style="float: left;overflow: hidden;margin-left: 20px" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <!--              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>-->
              <a-button type="primary" icon="download" style="margin-left: 8px" @click="handleExportXls('报')">导出</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>

    </div>
    <div>

    </div>
    <a-table
      style="margin-top: 10px"
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
    >



    </a-table>
  </a-card>
</template>

<script>
    import {tableMixin} from "../mixin/tableMixin";
    import AreaLinkSelect from "../component/AreaLinkSelect";
    import moment from 'moment';
    import {getAction,downFile} from "../../../api/manage";
    //超标率
    export default {
        name: "OverStandardRate",
        mixins:[tableMixin],
        components:{AreaLinkSelect},
        data(){
          return{
            queryParam:{
              moniterType:'0,1,2',
              dateType:'date',
              quarterly:1,
              dataTime_begin:moment().format(),
              dataTime_end:moment().format()
            },
            dateFormat:{
              'month':{value:"YYYY-MM",dataType:'month'},
              'year':{value:"YYYY",dataType:'year'},
              'date':{value:"YYYY-MM-DD",dataType:'date'},
              'quarterly':{value:"YYYY",dataType:'year'},
            },
            allowClear:false,
            companyNames:[],
            columns:[
              {
                title: '序号',
                dataIndex: '',
                key:'rowIndex',
                width:60,
                align:"center",
                customRender:this.calcIndex,
                scopedSlots: {
                  filterDropdown: 'filterDropdown',
                  filterIcon: 'filterIcon'},
              },
              {
                title:'企业名称',
                align:"center",
                dataIndex: 'companyName',
              },
              {
                title:'监测点名称',
                align:"center",
                dataIndex: 'siteName',
              },
              {
                title:'时间',
                align:"center",
                dataIndex: 'period',
              },
              {
                title:'总有效数据条数',
                align:"center",
                dataIndex: 'totalCount',
              },
              {
                title:'超标数据条数',
                align:"center",
                dataIndex: 'overCount',
              },
              {
                title:'超标率',
                align:"center",
                dataIndex: 'overRate',
                sorter: (a, b) => a.overRate - b.overRate,
              },
            ],
            dataSource:[]
          }
        },
        methods:{
          moniterChange(val){
            this.queryParam.moniterType = val.target.value;
            this.searchQuery();
          },
          dateTypeChange(val){
            this.queryParam.dateType = val.target.value;
            this.searchQuery();
          },
          searchQuery(){
              let param = {...this.queryParam};
              if(param.companyId==null){
                param.companyId = this.$store.getters.userInfo.companyIds.join(',');
              }
              console.log(param)
              getAction("/psoam/companyInfo/overStandardRate",param).then(res=>{
                console.log(res)
                if(res.success){
                  this.dataSource = res.result;
                }
              })
          },
          handleTableChange(pagination, filters, sorter) {
            // console.log(pagination, filters, sorter)
            //分页、排序、筛选变化时触发
            //TODO 筛选
            this.ipagination = pagination;

          },
          handleExportXls(fileName){
            if(!fileName || typeof fileName != "string"){
              fileName = "导出文件"
            }
            let param = {...this.queryParam};
            if(param.companyId==null){
              param.companyId = this.$store.getters.userInfo.companyIds.join(',');
            }

            console.log("导出参数",param)
            downFile("/psoam/companyInfo/exportOverStandardRate",param).then((data)=>{
              if (!data) {
                this.$message.warning("文件下载失败")
                return
              }
              if (typeof window.navigator.msSaveBlob !== 'undefined') {
                window.navigator.msSaveBlob(new Blob([data],{type: 'application/vnd.ms-excel'}), fileName+'.xls')
              }else{
                let url = window.URL.createObjectURL(new Blob([data],{type: 'application/vnd.ms-excel'}))
                let link = document.createElement('a')
                link.style.display = 'none'
                link.href = url
                link.setAttribute('download', fileName+'.xls')
                document.body.appendChild(link)
                link.click()
                document.body.removeChild(link); //下载完成移除元素
                window.URL.revokeObjectURL(url); //释放掉blob对象
              }
            })
          },
        },
        created(){
          this.queryCompanyAndSite();
          this.searchQuery();
        }
    }
</script>

<style scoped>

</style>