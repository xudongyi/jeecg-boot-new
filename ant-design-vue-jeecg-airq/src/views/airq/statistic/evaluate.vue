<template>
  <a-layout>
    <a-layout-sider theme="light" width="20%" style="border:1px solid rgba(217,217,217,1);border-radius:5px">
      <div style="height:7%">
        <span style="width:8px;height:18px;background:rgba(1,142,237,1);float:left;margin: 17px 10px 0px 5px"></span>
        <span
          style="width:64px;height:17px;font-size:16px;font-family:Microsoft YaHei;font-weight:bold;color:rgba(17,17,17,1);line-height:52px;">查询条件</span>
      </div>
      <div
        style="border-top:1px solid rgba(217,217,217,1);height: 88%">
        <a-form>
          <a-form-item label="数据类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-radio-group default-value="day" button-style="solid" @change="dataTypeChange" style="width: 100%">
              <a-radio-button value="day" style="width: 33.3%;text-align:center;">
                日
              </a-radio-button>
              <a-radio-button value="month" style="width: 33.3%;text-align:center;">
                月
              </a-radio-button>
              <a-radio-button value="year" style="width: 33.3%;text-align:center;">
                年
              </a-radio-button>
            </a-radio-group>
          </a-form-item>
          <a-form-item label="查询时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-range-picker
              :placeholder="placeholder"
              :format="format"
              :mode="mode2"
              :value="timeValue"
              @change="searchTimeChange"
              @panelChange="handlePanelChange"
            />
          </a-form-item>
          <a-form-item label="行政区域" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <area-link-select @change="selectChangeArea" placeholder="请选择省市区" v-model="area"/>
          </a-form-item>
          <a-form-item label="站点名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input-search type="list" v-decorator="['siteName']" placeholder="查找站点"
                            @change="onChange" :value="searchValue">
            </a-input-search>
          </a-form-item>
        </a-form>
        <div class="a-tree-scroll" style="border-top:1px solid rgba(217,217,217,1);">
          <a-tree
            :checkable="true"
            :expanded-keys="expandedKeys"
            :auto-expand-parent="autoExpandParent"
            :treeData="treeData"
            :checkedKeys = "checkedKeys"
            @check="onCheck"
            @expand="onExpand"
          >
            <template slot="title" slot-scope="{ title }">
                      <span v-if="title.indexOf(searchValue) > -1">
                        {{ title.substr(0, title.indexOf(searchValue)) }}
                        <span style="color: #f50">{{ searchValue }}</span>
                        {{ title.substr(title.indexOf(searchValue) + searchValue.length) }}
                      </span>
              <span v-else>{{ title }}</span>
            </template>
          </a-tree>
        </div>
      </div>
      <span
        style="text-align:center;display:block;height: 6%;line-height: 34px;border-top:1px solid rgba(217,217,217,1);">
              <a-button type="primary" @click="searchQuery" style="background-color: #0098A1" size="small">查询</a-button>
              <a-button type="primary"  @click="searchReset" style="margin-left: 111px" size="small">重置</a-button>
      </span>
    </a-layout-sider>
    <a-layout-content theme="light" width="80%"
                      style="background: #FFFFFF;border:1px solid rgba(217,217,217,1);border-radius:5px;margin-left: 8px">
      <div style="border-bottom: 1px solid rgba(217,217,217,1);height: 7%">
        <span style="width:8px;height:18px;background:rgba(1,142,237,1);float:left;margin: 17px 10px 0px 5px"></span>
        <span
          style="width:64px;height:17px;font-size:16px;font-family:Microsoft YaHei;font-weight:bold;color:rgba(17,17,17,1);line-height:52px;">分析结果</span>
      </div>
      <div style="margin: 8px 9px;height: 7%">
        <a-radio-group default-value="AQI" button-style="solid" style="width: 50%" @change="pollutionTypeChange">
          <a-radio-button value="AQI" style="width: 10%;text-align:center;">
            AQI
          </a-radio-button>
          <a-radio-button value="A3400424" style="width: 10%;text-align:center;">
            PM2.5
          </a-radio-button>
          <a-radio-button value="A3400224" style="width: 10%;text-align:center;">
            PM10
          </a-radio-button>
          <a-radio-button value="A21026" style="width: 10%;text-align:center;">
            SO<sub>2</sub>
          </a-radio-button>
          <a-radio-button value="A21004" style="width: 10%;text-align:center;">
            NO<sub>2</sub>
          </a-radio-button>
          <a-radio-button value="A21005" style="width: 10%;text-align:center;">
            CO
          </a-radio-button>
          <a-radio-button value="A0502408" style="width: 10%;text-align:center;">
            O<sub>3</sub>
          </a-radio-button>
        </a-radio-group>
        <a-button type="primary"  style="float:right;margin-right: 17px" @click="downPic" size="small">导出图片</a-button>
      </div>
      <div id="evaluate" style="width:100%;height:80%;z-index:19;"></div>
    </a-layout-content>
  </a-layout>
</template>

<script>
  import {querySiteName,queryStatistic} from "../../requestAction/request";
  import AreaLinkSelect from '../component/AreaLinkSelect'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import {mixinDevice} from '@/utils/mixin'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {getAction} from '@/api/manage'
  import moment from 'moment'

  export default {
    name: "evaluate",
    mixins: [mixinDevice],
    components: {
      JDictSelectTag,
      AreaLinkSelect,
    },

    data() {
      return {
        mode2: ["date","date"],
        placeholder:["开始日期","结束日期"],
        format:"YYYY-MM-DD",
        titleText:"大气环境质量(AQI)评价分析结果",
        radioObj:{"AQI":"AQI","A3400424":"PM2.5","A3400224":"PM10","A21026":"SO2","A21004":"NO2","A21005":"CO","A0502408":"O3"},
        autoExpandParent: true,
        expandedKeys: [],
        siteType:3,
        data: [],
        myChart:{},
        dataType:'day',
        pollutionType:"AQI",
        searchTime:[],
        timeValue:[],
        subtext:"",
        treeData: [],
        dataList: [],
        searchValue: '',
        items: [],
        siteData: [],
        checkedKeys: [],
        area:"",
        queryParam: {},
        labelCol: {
          xs: {span: 24},
          sm: {span: 6},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
      }
    },
    methods: {
      selectChangeArea(val) {
        this.queryParam.area = val;
        this.selectChange()
      },
      selectChange() {
        let that = this;
        querySiteName({area: this.queryParam.area,companyIds:this.$store.getters.userInfo.companyIds.join(','),siteType:this.siteType}).then((res) => {
          that.siteData = res.result;
          that.treeData = [];
          let newObj = that.dealAreaData(that.data);
          let checkObj = []
          //省
          newObj.forEach((a) => {
            //市
            let city = [];
            a.children.forEach((b) => {
              let qu = [];
              //区
              b.children.forEach((c) => {
                let site = [];
                res.result.forEach((e) => {
                  if (c.key === e.area){
                    site.push({key: e.key, title: e.siteName})
                    checkObj.push(e.key)
                  }

                });
                if (site.length > 0) {
                  c.children = site;
                  qu.push(c)
                }
              });
              if (qu.length > 0) {
                b.children = qu;
                city.push(b)
              }
            });
            if (city.length > 0) {
              a.children = city;
              that.treeData.push(a)
            }
          });
          that.checkedKeys = checkObj
          if (res.success)
            that.items = res.result;
        });


      },
      dealAreaData(res) {
        let areaSource = [];
        const province = res.result['86'];
        Object.keys(province).map(key => {
          areaSource.push({key: key, title: province[key], children: []});
          const city = res.result[key];
          Object.keys(city).map(key2 => {

            areaSource[areaSource.length - 1].children.push({key: key2, title: city[key2], children: []});
            const qu = res.result[key2];
            Object.keys(qu).map(key3 => {
              let arrindex = areaSource[areaSource.length - 1].children.length - 1;
              areaSource[areaSource.length - 1].children[arrindex].children.push({key: key3, title: qu[key3]});
            })
          })
        });

        console.log(areaSource)
        return areaSource
      },
      onExpand(expandedKeys) {
        this.expandedKeys = expandedKeys;
        this.autoExpandParent = false;
      },
      onChange(e) {
        this.expandedKeys = [];
        const value = e.target.value;
        this.find(this.treeData, value);
        console.log(this.expandedKeys, value);

        this.searchValue = value;
        this.autoExpandParent = true

      },
      find(treeDate, val) {

        let _this = this;
        treeDate.forEach(e => {
          console.log(e);
          if (e.children) {
            if (this.find(e.children, val)) {
              console.log(e.key);
              _this.expandedKeys.push(e.key);
              return true
            }

          }
          if (e.title.indexOf(val) > -1) {
            console.log(e.key);
            _this.expandedKeys.push(e.key);
            return true
          }
        })
      },
      onCheck(checkedKeys, info) {
        //修改选择数据
        this.checkedKeys = checkedKeys;  //只要站点的
        this.halfCheckedKeys = info.halfCheckedKeys;
      },
      drawLine(data,titleText) {
        var echarts = require('echarts');
        var myChart = echarts.init(document.getElementById('evaluate'));
        this.myChart = myChart;
        myChart.setOption({
          title: {
            text: titleText,
            subtext: this.subtext,
            left: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{b} : {c} ({d}%)'
          },
          legend: {
            bottom: 10,
            left: 'center',
            data: ['优', '良', '轻度污染', '中度污染', '重度污染',"严重污染"]
          },
          series: [
            {
              type: 'pie',
              radius: '65%',
              center: ['50%', '50%'],
              selectedMode: 'single',
              data: data,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',

                }
              },
              itemStyle: {
                normal:{
                  color:function(params) {
                    //自定义颜色
                    var colorList = [
                      '#00E400','#EFD600','#FF7E00','#FF0000','#99004C', '#7E0023'
                    ];
                    return colorList[params.dataIndex]
                  },

                }
              }
            }
          ]
        });
      },
      parseDate(value){
        if(value.length!=0){
          return  [value[0].format(this.format),value[1].format(this.format)];
        }else {
          return  [];
        }

      },
      searchQuery(){
        let  checkSites = [];
        let that = this;

        if(that.searchTime && that.searchTime.length>0){
          that.subtext = "("+that.searchTime.join("~")+")";
        }else{
          that.subtext = "";
        }
        if(!that.checkedKeys||that.checkedKeys.length==0){
          this.$message.error("请选择站点！");
          return;
        }
        that.checkedKeys.forEach(e=>{
          that.siteData.forEach(element=>{
            if(e===element.key)
              checkSites.push(e)
            }
          )
        })
        queryStatistic({dataType: that.dataType,pollutionType:that.pollutionType,searchTime:that.searchTime.join(","),checkedKeys:checkSites.join(",")}).then((res) => {
          var evaluate = document.getElementById("evaluate");
          if(!res.result || res.result.length==0){
            that.myChart.clear();
            evaluate.style.background=`url(${require("@/assets/diynodata.png")}) no-repeat center`;
          }else{
            evaluate.style.background='';
            that.drawLine(res.result,this.titleText);
          }
        })
      },
      searchReset(){
        this.timeValue = [];
        this.checkedKeys = [];
        this.searchValue = "";
        this.area = "";
      },
      dataTypeChange(e){
        const dataType = e.target.value;
        this.dataType = dataType;
        this.timeValue = [];
        if(dataType==="day"){
          this.mode2= ["date","date"]
          this.format="YYYY-MM-DD"
          this.placeholder=["开始日期","结束日期"]
        }else if(dataType==="month"){
          this.mode2 = ["month","month"]
          this.placeholder=["开始月份","结束月份"]
          this.format="YYYY-MM"
        }else if(dataType==="year"){
          this.mode2 = ["year","year"]
          this.placeholder=["开始年份","结束年份"]
          this.format="YYYY"
        }
        this.searchQuery();
      },
      pollutionTypeChange(e){
        this.pollutionType = e.target.value;
        this.titleText = "大气环境质量("+this.radioObj[e.target.value]+")评价分析结果" ;
        this.searchQuery();
      },
      searchTimeChange(value,dataStr) {
        this.timeValue = value;
        this.searchTime = this.parseDate(value);
      },
      handlePanelChange(value, mode) {
        this.timeValue = value;
        this.searchTime = this.parseDate(value);
      },
      downPic(){
       let base64Url = this.myChart.getDataURL({
          pixelRatio: 2,
          backgroundColor: '#fff'
        });
        let aLink = document.createElement('a');
        let parts = base64Url.split(';base64,');
        let contentType = parts[0].split(':')[1];
        let raw = window.atob(parts[1]);
        let rawLength = raw.length;
        let uInt8Array = new Uint8Array(rawLength);
        for (let i = 0; i < rawLength; ++i) {
          uInt8Array[i] = raw.charCodeAt(i);
        }
        let blob = new Blob([uInt8Array], {type: contentType});
        let evt = document.createEvent("HTMLEvents");
        evt.initEvent("click", true, true);//initEvent 不加后两个参数在FF下会报错  事件类型，是否冒泡，是否阻止浏览器的默认行为
        aLink.download = "大气环境质量评价分析结果_"+new moment().format('YYYYMMDDHHmmss');
        aLink.href = URL.createObjectURL(blob);
        // aLink.dispatchEvent(evt);
        //aLink.click()
        aLink.dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true, view: window}));//兼容火狐
      },
    }, created() {
      this.queryParam.area = '';
      let _this = this;
      getAction("/sys/sysArea/list", {active: '1'}).then((res) => {
        if (res.success) {
          _this.data = res
          console.log(_this.data)
          _this.selectChange();
        }
      })
    }, mounted() {
      var that = this
      let  checkSites = []
      querySiteName({area: that.queryParam.area,companyIds:that.$store.getters.userInfo.companyIds.join(','),siteType:that.siteType}).then((res) => {
        let sites = res.result;
        sites.forEach(e=>{
          checkSites.push(e.key);
        })
        queryStatistic({dataType: that.dataType,pollutionType:that.pollutionType,searchTime:that.searchTime.join(","),checkedKeys:checkSites.join(",")}).then((res) => {
          let evaluate = document.getElementById("evaluate");
          if(!res.result|| res.result.length==0){
            that.myChart.clear();
            evaluate.style.background=`url(${require("@/assets/diynodata.png")}) no-repeat center`;
          }else{
            evaluate.style.background='';
            that.drawLine(res.result,this.titleText);
          }
        })
      })
    }
  }
</script>

<style scoped>
  .a-tree-scroll {
    overflow: auto;
    height: 400px;
    border-top: 1px solid rgba(217, 217, 217, 1);
  }
  .ant-form-item {
    margin-bottom: 10px;
  }
</style>