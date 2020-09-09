<template>
  <a-card :bordered="false"  :tab-list="tabList"  style="width:100%"
          :active-tab-key="key">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="6" :md="6" :sm="24">
            <a-form-item label="行政区域">
              <area-link-select @change="areaChange" type="cascader" v-model="queryParam.area" show-search style="width: 100%"
                                optionFilterProp="children"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="6" :md="6" :sm="24">
            <a-form-item label="企业名称">
              <a-select v-model="queryParam.companyId"  :allowClear="true" placeholder="请选择" show-search style="width: 100%" optionFilterProp="children">
                <!--                <a-select-option value="">请选择</a-select-option>-->
                <a-select-option v-for="item in companyNames" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="6" :md="6" :sm="24">
            <a-form-item label="数据时间">
              <a-date-picker :show-time="false"
                             :format="dateFormat[key].value" placeholder="请选择开始时间"
                             :valueFormat="dateFormat[key].value"
                             :mode="dateFormat[key].mode"
                             class="query-group-cust"  v-model="queryParam.dataTime"   />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="6" :md="6" :sm="24">
            <a-form-item label="数据时间">
              <a-date-picker :show-time="false"
                             :format="dateFormat[key].value" placeholder="请选择开始时间"
                             :valueFormat="dateFormat[key].value"
                             :mode="dateFormat[key].mode"
                             class="query-group-cust"  v-model="queryParam.dataTime"   />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </div>
  </a-card>

</template>

<script>
  import AreaLinkSelect from '../component/AreaLinkSelect'
  import {reportMixin} from "../mixin/reportMixin"
    import {queryCompanyName, querySiteNameAndMn} from "../../requestAction/request";
    import AreaHandler from "../component/AreaHandler";
    export default {
      name: "companyReport",
      mixins:[reportMixin],
      components:{AreaLinkSelect},
      data(){
          return {
            queryParam:{


            },
            areaHandler:'',
            companyNames:[],//显示数据  根据area筛选后的数据
            companyNameOriginal:[],//原始全量数据
          }
      },
      methods:{
        callback(val){
          console.log(val)
        },
        queryCompanyAndSite(){
          let that = this;

          //查询企业名称
          queryCompanyName({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res) => {
            if(res.success){
              that.companyNameOriginal = res.result.companyNames;
              that.companyNames = res.result.companyNames;
              // console.log("!!",that.companyNames);
            }
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