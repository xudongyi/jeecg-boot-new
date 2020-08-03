<template>

  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
            <a-form-item label="行政区域：">
              <area-link-select @change="areaChange" type="cascader" v-model="queryParam.area" show-search style="width: 100%" optionFilterProp="children"/>
            </a-form-item>
          </a-col>
          <a-col :xl="5" :lg="7" :md="8" :sm="24">
            <a-form-item label="监控点名称：">
              <a-select v-model="queryParam.mn" placeholder="请输入监控点名称"  show-search style="width: 100%" optionFilterProp="children">
                <!--                <a-select-option :value="companyIds">请选择</a-select-option>-->
                <a-select-option v-for="item in sites" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="10" :lg="6" :md="8" :sm="24">
            <a-form-item label="数据时间：">
              <a-date-picker placeholder="请选择数据时间"     class="query-group-cust" :show-time="{ format: 'HH' }"
                             @change="handleDateChange"   @panelChange="panelChange"
                             format="YYYY-MM-DD HH"  :value="queryParam.datatime" valueFormat="YYYY-MM-DD HH" />
              <span class="query-group-split-cust"></span>
              <a-date-picker placeholder="请选择数据时间" :value="queryParam.datatime2"     class="query-group-cust" :show-time="{ format: 'HH' }"      @change="handleDateChange2"
                             format="YYYY-MM-DD HH"  valueFormat="YYYY-MM-DD HH"  />
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="localReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button type="primary" icon="download"  style="margin-left: 8px"  @click="handleExportXls('空气质量指数实时报')">导出</a-button>

            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <a-table
      :columns="columns"
      :dataSource="dataSource"
      :pagination="ipagination"
      class="j-table-force-nowrap"
      bordered
      @change="handleTableChange"
      :scroll="{x:4000}"

    >

        <span slot="leveltype" slot-scope="text,record">
          <a-tag :color="tagColors[record.level]" style="width: 90%">
            {{dictVal(record.level)}}
          </a-tag>
         </span>

    </a-table>


  </a-card>

</template>

<script>
  import {queryHourAirQuality, querySiteNameAndMn} from "../../requestAction/request";
  import AreaLinkSelect from "../component/AreaLinkSelect";
  import moment from 'moment'
  import AreaHandler from "../component/AreaHandler";
  import {ajaxGetDictItems, getDictItemsFromCache} from '@/api/api'
  import {downFile} from '@/api/manage'

  export default {
        name: "AirqHourQuality",
        components:{AreaLinkSelect},
        data(){
          return {
            /* table加载状态 */
            loading:false,
            queryParam: {},
            selectData:{},
            sites:[],
            siteOriginal:[],
            /* 数据源 */
            dataSource:[],
            /* 分页参数 */
            ipagination:{
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
            columns:[
              {
                title: '序号',
                dataIndex: '',
                key:'rowIndex',
                width:65,
                align:"center",
                customRender:this.calcIndex,
                fixed: 'left',

              },
              {
                title: '行政区域',
                dataIndex: 'area',
                align:"center",
                width:100,
                fixed: 'left',
                customRender: this.renderContentArea

              },
              {
                title: '监测点位名称',
                dataIndex: 'siteName',
                align:"center",
                width:160,
                fixed: 'left',
                customRender: this.renderContent

              },
              {
                title: '数据时间',
                dataIndex: 'dataTime',
                align:"center",
                width:160,
                fixed: 'left',

                // customRender: this.dataformat

              },
              {
                title: '空气质量指数(AQI)',
                dataIndex: 'aqi',
                align:"center",
                sorter: (a, b) => a.aqi - b.aqi,
              },
              {
                title: '首要污染物',
                dataIndex: 'meaning',
                align:"center",

              },
              {
                title: '空气质量指数级别',
                dataIndex: 'level',
                align:"center",
                customRender:function (text){
                  if(text === '1'){
                    return "一级";
                  }else if(text==='2'){
                    return "二级";
                  }else if(text==='3'){
                    return "三级";
                  }else if(text==='4'){
                    return "四级";
                  }else if(text==='5'){
                    return "五级";
                  }else
                    return "六级";
                },
                sorter: (a, b) => a.level - b.level,
              },
              {
                title: '空气质量指数类别',
                dataIndex: 'level_dictText',
                align:"center",
                key:'leveltype',
                scopedSlots: { customRender:'leveltype'},
              },
              {
                title: 'SO2(1h)',
                children: [
                  {
                    title: '浓度(μg/m3)',
                    dataIndex: 'a21026Avg',
                    align:"center",
                    customRender:this.renderEmpty,
                    sorter: (a, b) => a.a21026Avg - b.a21026Avg,

                  },
                  {
                    title: '分指数',
                    dataIndex: 'a21026Iaqi',
                    align:"center",
                    customRender:this.renderEmpty,
                    sorter: (a, b) => a.a21026Iaqi - b.a21026Iaqi,

                  }
                ]
              },
              {
                title: 'NO2(1h)',
                children: [
                  {
                    title: '浓度(μg/m3)',
                    dataIndex: 'a21004Avg',
                    align:"center",
                    customRender:this.renderEmpty,
                    sorter: (a, b) => a.a21004Avg - b.a21004Avg,


                  },
                  {
                    title: '分指数',
                    dataIndex: 'a21004Iaqi',
                    align:"center",
                    customRender:this.renderEmpty,
                    sorter: (a, b) => a.a21004Iaqi - b.a21004Iaqi,


                  }
                ]
              },
              {
                title: 'PM10(1h)',
                children: [
                  {
                    title: '浓度(μg/m3)',
                    dataIndex: 'a3400201Avg',
                    align:"center",
                    customRender:this.renderEmpty,
                    sorter: (a, b) => a.a3400201Avg - b.a3400201Avg,


                  },
                  {
                    title: '分指数',
                    dataIndex: 'a3400201Iaqi',
                    align:"center",
                    customRender:this.renderEmpty,
                    sorter: (a, b) => a.a3400201Iaqi - b.a3400201Iaqi,


                  }
                ]
              },
              {
                title: 'PM10(24h)',
                children: [
                  {
                    title: '浓度(μg/m3)',
                    dataIndex: 'a3400224Avg',
                    align:"center",
                    customRender:this.renderEmpty,
                    sorter: (a, b) => a.a3400224Avg - b.a3400224Avg,


                  },
                  {
                    title: '分指数',
                    dataIndex: 'a3400224Iaqi',
                    align:"center",
                    customRender:this.renderEmpty,
                    sorter: (a, b) => a.a3400224Iaqi - b.a3400224Iaqi,


                  }
                ]
              },
              {
                title: 'CO(1h)',
                children: [
                  {
                    title: '浓度(μg/m3)',
                    dataIndex: 'a21005Avg',
                    align:"center",
                    customRender:this.renderEmpty,
                    sorter: (a, b) => a.a21005Avg - b.a21005Avg,


                  },
                  {
                    title: '分指数',
                    dataIndex: 'a21005Iaqi',
                    align:"center",
                    customRender:this.renderEmpty,
                    sorter: (a, b) => a.a21005Iaqi - b.a21005Iaqi,
                  }
                ]
              },
              {
                title: 'O3(1h)',
                children: [
                  {
                    title: '浓度(μg/m3)',
                    dataIndex: 'a0502401Avg',
                    align:"center",
                    customRender:this.renderEmpty,
                    sorter: (a, b) => a.a0502401Avg - b.a0502401Avg,

                  },
                  {
                    title: '分指数',
                    dataIndex: 'a0502401Iaqi',
                    align:"center",
                    customRender:this.renderEmpty,
                    sorter: (a, b) => a.a0502401Iaqi - b.a0502401Iaqi,

                  }
                ]
              },
              {
                title: 'O3(8h)',
                children: [
                  {
                    title: '浓度(μg/m3)',
                    dataIndex: 'a0502408Avg',
                    align:"center",
                    customRender:this.renderEmpty,
                    sorter: (a, b) => a.a0502408Avg - b.a0502408Avg,


                  },
                  {
                    title: '分指数',
                    dataIndex: 'a0502408Iaqi',
                    align:"center",
                    customRender:this.renderEmpty,
                    sorter: (a, b) => a.a0502408Iaqi - b.a0502408Iaqi,

                  }
                ]
              },
              {
                title: 'PM2.5(1h)',
                children: [
                  {
                    title: '浓度(μg/m3)',
                    dataIndex: 'a3400401Avg',
                    align:"center",
                    customRender:this.renderEmpty,
                    sorter: (a, b) => a.a3400401Avg - b.a3400401Avg,


                  },
                  {
                    title: '分指数',
                    dataIndex: 'a3400401Iaqi',
                    align:"center",
                    customRender:this.renderEmpty,
                    sorter: (a, b) => a.a3400401Iaqi - b.a3400401Iaqi,

                  }
                ]
              },
              {
                title: 'PM2.5(24h)',
                children: [
                  {
                    title: '浓度(μg/m3)',
                    dataIndex: 'a3400424Avg',
                    align:"center",
                    customRender:this.renderEmpty,
                    sorter: (a, b) => a.a3400424Avg - b.a3400424Avg,

                  },
                  {
                    title: '分指数',
                    dataIndex: 'a3400424Iaqi',
                    align:"center",
                    customRender:this.renderEmpty,
                    sorter: (a, b) => a.a3400424Iaqi - b.a3400424Iaqi,

                  }
                ]
              },
            ],
            areaHandler:'',
            tagColors:{
              1:'#00E400',
              2:'#EFD600',
              3:'#FF7E00',
              4:'#FF0000',
              5:'#99004C',
              6:'#7E0023',
            },
            dictOptions:{}
          }
        },
      watch: {
        dataSource(val){
          this.rowspan(val)

        }
      },
        methods:{
          dataformat(val){

          },
          handleTableChange(pagination, filters, sorter) {
            //分页、排序、筛选变化时触发
            //TODO 筛选
            this.ipagination = pagination;

          },
          initDictData(dictCode) {

            //优先从缓存中读取字典配置
            if(getDictItemsFromCache(dictCode)){
              this.dictOptions[dictCode] = getDictItemsFromCache(dictCode);
              return
            }
            //根据字典Code, 初始化字典数组
            ajaxGetDictItems(dictCode, null).then((res) => {
              if (res.success) {
                this.dictOptions[dictCode] = res.result;
              }
            })
          },
          dictVal(text){
            if(text===null)
              return ''
              //普通字典数据
             this.initDictData('level')
              let result =
                this.dictOptions['level'].find(e=>{
                  return  e.value===text;
                });
              if(result==null)
                return text;
              return result.text;

          },
          renderEmpty(val){
            if(val==null||val==='')
              return 'NA'
            return val
          },
          renderContentArea(value, row, index){
            return this.renderContent(this.getAreaBycode(value),row,index)
          },
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
            if(userData===null)
              return
            userData.forEach((item,index) => {
              if(index === 0){
                _this.spanArr.push(1);
                _this.position = 0;
              }else{
                //需要合并的地方判断
                if(userData[index].siteName === userData[index-1].siteName ){
                  _this.spanArr[ _this.position] += 1;
                  _this.spanArr.push(0);
                }else{
                  _this.spanArr.push(1);
                  _this.position = index;
                }
              }
            });
          },
          initArea(){
            this.areaHandler = new AreaHandler()
          },
          getAreaBycode(text){
            if(!text)
              return ''
            //初始化
            if(this.areaHandler==='')
            {
              this.initArea()
            }
            let arr = [];
            this.areaHandler.getAreaBycode(text,arr);
            return arr[arr.length-1]
          },
          localReset(){
            this.queryParam={datatime:moment().hours(moment().hours()-1).format('YYYY-MM-DD HH'),datatime2:moment().hours(moment().hours()-1).format('YYYY-MM-DD HH')}
          },
          panelChange(value){
            console.log(value)
            this.queryParam.datatime=value.format('YYYY-MM-DD HH')
          },
          handleDateChange(mom,dateStr){
            console.log(mom,dateStr)
            this.queryParam.datatime=dateStr
          },
          handleDateChange2(mom,dateStr){
            this.queryParam.datatime2=dateStr
          },
          filterOption(input, option) {
            console.log(input, option)
            return (
              option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
            );
          },
          areaChange(val){
            let _this = this
            _this.sites=[]
            _this.siteOriginal.forEach(e=>{
              if(e.area === val){
                _this.sites.push(e)
              }
            })
          },
          calcIndex: function (t,r,index) {
            console.log(t,r,index)
            return parseInt(index)+1+(this.ipagination.current-1)*this.ipagination.pageSize;
          },
          searchQuery(){
            let that = this
            this.queryParam.companyIds=this.$store.getters.userInfo.companyIds.join(',')
            queryHourAirQuality(this.queryParam).then(res=>{
              console.log(res)
              that.dataSource = res.result
            })
          },
          pepareSelect(){
            let _this = this
            //查询公司
            querySiteNameAndMn({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res)=>{
              if(res.success){
                _this.siteOriginal = res.result;
                _this.sites = res.result;
                console.log(_this.sites)
              }
            })
          },
          handleExportXls(fileName){
            if(!fileName || typeof fileName != "string"){
              fileName = "导出文件"
            }
            let param = {...this.queryParam};
            console.log("导出参数",param)
            downFile("/hour/airqHour/exportQuality",param).then((data)=>{
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
        created() {
          this.localReset()
          //查询列表
          this.searchQuery();
          //查询  搜索条件
          this.pepareSelect()


        }

    }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>