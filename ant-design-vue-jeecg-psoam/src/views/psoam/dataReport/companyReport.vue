<template>
  <a-card :bordered="false"  :tab-list="tabList"  style="width:100%"
          :active-tab-key="key" @tabChange="tabChange">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="5" :lg="5" :md="5" :sm="24">
            <a-form-item label="行政区域">
              <area-link-select @change="areaChange" type="cascader" v-model="queryParam.area" show-search style="width: 100%"
                                optionFilterProp="children"/>
            </a-form-item>
          </a-col>
          <a-col :xl="7" :lg="7" :md="7" :sm="24">
            <a-form-item label="企业名称">
              <a-select v-model="queryParam.companyId" :dropdownMatchSelectWidth="false"
                        @change="companyChange" :allowClear="true" placeholder="请选择" show-search style="width: 100%" optionFilterProp="children">
                <!--                <a-select-option value="">请选择</a-select-option>-->
                <a-select-option v-for="item in companyNames"  :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="5" :md="5" :sm="24">
            <a-form-item label="数据时间">
              <a-date-picker :show-time="false"
                             :format="dateFormat[key].value" placeholder="请选择时间"
                             :valueFormat="dateFormat[key].value"
                             :mode="dateFormat[key].mode"
                               v-model="queryParam.dataTime" />
            </a-form-item>
          </a-col>
          <a-col :xl="3" :lg="3" :md="3" :sm="24" v-show="key==='quarterly'">
              <a-select v-model="queryParam.quarterly" :dropdownMatchSelectWidth="false"
                        @change="companyChange" :allowClear="true" placeholder="请选择" show-search style="width: 100%" optionFilterProp="children">
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
          </a-col>
          <a-col :xl="4" :lg="4" :md="4" :sm="24">
            <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
            <!--              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>-->
            <a-button type="primary" icon="download" style="margin-left: 8px" @click="handleExportXls()">导出</a-button>
          </a-col>
        </a-row>
      </a-form>
      <div style="width: 100%;text-align: center"><span>{{companyName}}</span></div>
      <a-tabs type="card" @change="siteChange" :active-key="siteKey" >
        <a-tab-pane v-for="(item,index) in siteItems" :key="index" :tab="item.value" >
          <!--表格  表格不排序  pagination=false-->
          <a-table
            style="margin-top: 10px"
            ref="table"
            size="middle"
            bordered
            rowKey="id"
            :columns="columns"
            :dataSource="dataSource"
            :pagination="false"
            :loading="loading"
            class="j-table-force-nowrap"
          >

            <a-icon slot="filterIcon" type='setting'
                    :style="{ fontSize:'16px',color:  '#108ee9',width:'100%' }" />

          </a-table>


        </a-tab-pane>

      </a-tabs>
    </div>



  </a-card>

</template>

<script>
  import AreaLinkSelect from '../component/AreaLinkSelect'
  import {reportMixin} from "../mixin/reportMixin"
    import {queryCompanyName, querySiteNameAndMn} from "../../requestAction/request";
    import AreaHandler from "../component/AreaHandler";
    import {getAction,downFile} from "../../../api/manage";
    import moment from 'moment'
  export default {
      name: "companyReport",
      mixins:[reportMixin],
      components:{AreaLinkSelect},
      data(){
          return {
            queryParam:{
              dataTime:moment().format("YYYY-MM-DD"),
              quarterly:1
            },
            areaHandler:'',
            companyNames:[],//显示数据  根据area筛选后的数据
            companyNameOriginal:[],//原始全量数据
            companyName:'',
            siteOriginal:[],
            siteKey:0,
            siteItems:[],//站点tabs
            //表格相关
            columns:[],
            dataSource:[],
            loading:false,
          }
      },
      computed:{

      },
      methods:{


        siteChange(val){
          console.log(val);
          this.siteKey = val;
          this.searchQuery();
        },
        tabChange(val){
          console.log(val)
          this.key =val;
          this.searchQuery();
        },
        companyChange(val){
          if(val==null)
            return '';
          this.companyName = this.companyNames.filter(e=>
            e.key ===val
          )[0].value;

          let _this = this;
          _this.siteItems=[]
          this.siteOriginal.forEach(e=>{
            if(e.companyId === _this.queryParam.companyId){
              _this.siteItems.push(e)
            }
          });
          console.log("筛选站点",_this.siteItems);
          this.searchQuery();
        },
        callback(val){
          console.log(val)
        },
        queryCompanyAndSite(){
          let _this = this;
          //查询企业名称
          queryCompanyName({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res) => {
            if(res.success){
              _this.companyNameOriginal = res.result.companyNames;
              _this.companyNames = res.result.companyNames;
              // console.log("!!",that.companyNames);
              _this.queryParam.companyId =   _this.companyNames[0].key;

              console.log("查询企业名称")
              querySiteNameAndMn({companyIds:this.$store.getters.userInfo.companyIds.join(',')})
                .then((res)=>{
                  if(res.success){
                    _this.siteOriginal = res.result;
                    console.log("查询站点")

                    _this.companyChange( _this.queryParam.companyId);
                    //默认查询第一个站点的
                    if( _this.siteItems.length>0){

                      _this.searchQuery();

                    }
                  }
                });
            }
          });

        },
        searchQuery(){
          //参数 area-企业的行政区域  companyId公司 dataTime查询时间  quarterly季度  年和季度都是查询月
          let params = this.queryParam;
          params.dataType = this.key;
          params.mn = this.siteItems[this.siteKey].key;
          let _this = this
          // _this.siteItems[siteKey].key: "MN32019283002"
          // siteId
          getAction("/psoam/companyReport/queryWaterReport",params).then(res=>{
            console.log(res)
            _this.columns=[];
            _this.dataSource=[];
            if(res.success){
              _this.pushIndexColumn();
              //表头
              res.result.columns.forEach(e=>{
                _this.columns.push(e);
              })
              _this.dataSource =  res.result.records;
            }else{
              this.$message.error(res.message);
            }


          })


        },
        handleExportXls(){
          let _this =this
          let fileName = this.companyName+this.siteItems[this.siteKey].value
          +this.queryParam.dataTime+this.key==="quarterly"?"第"+this.queryParam.quarterly+"季度":"";

          let param = {...this.queryParam};
          param.dataType = this.key;
          param.mn = this.siteItems[this.siteKey].key;
          console.log("导出参数",param)
          downFile("/psoam/companyReport/exportWaterReport",param).then((data)=>{
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

        pushIndexColumn(){
          this.columns.push(
          {
            title: '序号',
              dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:this.calcIndex,
          });
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
        areaChange(val){
          let _this = this;
          _this.items=[];
          //选择地区筛选公司名称
          _this.companyNames=[];
          if(this.queryParam.area != null){
            _this.companyNameOriginal.forEach(b=>{
              if(b.area === val){
                _this.companyNames.push(b);
              }
            })
          }else {
            _this.companyNames =_this.companyNameOriginal;
          }
        },
      },created() {
          this.queryCompanyAndSite()
      }
    }
</script>

<style scoped>

</style>