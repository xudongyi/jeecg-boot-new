<template>
  <a-layout>
    <a-layout-sider theme="light" width="20%" style="border:1px solid rgba(217,217,217,1);border-radius:5px">
      <div style="height:52px">
        <span style="width:8px;height:18px;background:rgba(1,142,237,1);float:left;margin: 17px 10px 0px 5px"></span>
        <span
          style="width:64px;height:17px;font-size:16px;font-family:Microsoft YaHei;font-weight:bold;color:rgba(17,17,17,1);line-height:52px;">查询条件</span>
      </div>
      <div
        style="border-top:1px solid rgba(217,217,217,1);height: 650px">
        <a-form>


          <a-form-item label="行政区域" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <area-link-select @change="selectChangeArea" placeholder="请选择省市区" v-model="area"/>
          </a-form-item>
          <a-form-item label="站点名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input-search type="list" v-decorator="['siteName']" placeholder="查找站点"
                            @change="onChange" :value="searchValue">
            </a-input-search>
          </a-form-item>

          <a-form-item label="申报年份" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-select v-model="timeValue" show-search >
              <a-select-option v-for="item in years" :key="item.value"  :value="item.value">
                {{item.value}}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-form>
        <div class="a-tree-scroll" style="border-top:1px solid rgba(217,217,217,1);">
          <a-tree
            :checkable="false"
            :expanded-keys="expandedKeys"
            :auto-expand-parent="autoExpandParent"
            :treeData="treeData"
            :selectedKeys="selectedKeys"
            @select="onCheck"
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
      <div style="border-bottom: 1px solid rgba(217,217,217,1);height: 52px">
        <span style="width:8px;height:18px;background:rgba(1,142,237,1);float:left;margin: 17px 10px 0px 5px"></span>
        <span
          style="width:64px;height:17px;font-size:16px;font-family:Microsoft YaHei;font-weight:bold;color:rgba(17,17,17,1);line-height:52px;">分析结果</span>
      </div>
      <div style="margin: 8px 9px;height: 52px">
        <a-radio-group default-value="AQI" button-style="solid" style="width: 50%" @change="pollutionTypeChange">
          <a-radio-button value="AQI" style="width: 10%;text-align:center;">
            AQI
          </a-radio-button>
          <a-radio-button value="A34004" style="width: 10%;text-align:center;">
            PM2.5
          </a-radio-button>
          <a-radio-button value="A34002" style="width: 10%;text-align:center;">
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
          <a-radio-button value="A05024" style="width: 10%;text-align:center;">
            O<sub>3</sub>
          </a-radio-button>
        </a-radio-group>
        <a-button type="primary"  style="float:right;margin-right: 17px" @click="downPic" size="small">导出图片</a-button>
      </div>
      <div style="text-align: center">
        <span style="font-size:18px;font-weight:bold;color:rgba(0,0,0,1);">南通市蓝天日历（{{timeValue}} 年）统计</span>
      </div>
      <div style="text-align: right">
      <span style="font-size:14px;font-weight:400;color:rgba(1,1,1,1);">南通市{{timeValue}}年优良天数累计：</span>
        <span style="font-size:18px;font-weight:Bold;color:#189FF0;">{{countDays}}</span>
        <span style="font-size:14px;font-weight:400;color:rgba(1,1,1,1);">天&#12288;</span>
      </div>
      <div id="calendar" style="width:100%;height:500px;z-index:19;"></div>
    </a-layout-content>
  </a-layout>
</template>

<script>
  import {querySiteName,queryCalendarAirQuality} from "../../requestAction/request";
  import AreaLinkSelect from '../component/AreaLinkSelect'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import {mixinDevice} from '@/utils/mixin'
  import {getAction} from '@/api/manage'
  import moment from 'moment'

  export default {
    name: "calendar",
    mixins: [mixinDevice],
    components: {
      JDictSelectTag,
      AreaLinkSelect,
    },

    data() {
      return {
        radioObj:{"AQI":"AQI","A34004":"PM2.5","A34002":"PM10","A21026":"SO2","A21004":"NO2","A21005":"CO","A05024":"O3"},
        autoExpandParent: true,
        expandedKeys: [],
        siteType:3,
        data: [],
        pollutionType:"AQI",
        timeValue:new moment().format('YYYY'),
        treeData: [],
        searchValue: '',
        siteData: [],
        selectedKeys: [],
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
        years:[],
        queryResult:{},
        countDays:0,

      }
    },
    methods: {
      yearChange () {
        let myDate = new Date()
        let startYear = myDate.getFullYear() - 20// 起始年份
        let endYear = myDate.getFullYear() + 20// 结束年份

        this.years = []
        for (var i = startYear; i <= endYear; i++) {
          this.years.push({value: i, label: i})
        }
      },
      //计算空气污染等级
      getLvl(aqi){

        if(aqi<=50)
          return '优';
        if(aqi<=100)
          return '良';
        if(aqi<=150)
          return '轻度污染';
        if(aqi<=200)
          return '中度污染';
        if(aqi<=300)
          return '重度污染';
        return '严重污染';
      },
      backgroundColors(aqi){
        if(aqi<=50)
          return 'fragment1';
        if(aqi<=100)
          return 'fragment2';
        if(aqi<=150)
          return 'fragment3';
        if(aqi<=200)
          return 'fragment4';
        if(aqi<=300)
          return 'fragment5';
        return 'fragment6';
      },
      drawCalendar(){
        let echarts = require('echarts');
        let year = this.timeValue
        this.queryResult.months=[]
        for(let a =1 ;a<=12;a++){
          this.queryResult.months.push(year+'-'+(a<10?'0':'')+a)
        }


        if(!this.myChart){
          let dom  = document.getElementById("calendar")
           option ={
            renderer: "Canvas",
            height:1500
          }
          //dom.style.height = 100+360*parseInt(this.queryResult.months.length/3 +(this.queryResult.months.length%3>0?1:0) )+'px'
          this.myChart = echarts.init( dom,null,option);
        }
        let that = this;

        let heatmapData = [];
        let calendar = [];
        let series = [];
        this.countDays = 0;
        for(let i = 0;i<this.queryResult.dataList.length; i++){
          let tmp = this.queryResult.dataList[i]
          heatmapData.push([tmp.dataTime,tmp[this.pollutionType],tmp['firstCode']===''?'无':tmp['firstCode']]);
          if(tmp[this.pollutionType]<=100)
            this.countDays++;
        }
        let dataArr=[]
        var date = +echarts.number.parseDate(year + '-01-01');
        var end = +echarts.number.parseDate((+year + 1) + '-01-01');
        var dayTime = 3600 * 24 * 1000;
        for (let time = date; time < end; time += dayTime) {
          let tmp = echarts.format.formatTime('yyyy-MM-dd', time);
            dataArr.push([tmp,'','']);
        }

        for (let i = 0;i<this.queryResult.months.length; i++) {
          calendar.push(
            {
              orient: 'vertical',
              yearLabel:{
                show:false
              },
              dayLabel: {
                firstDay: 7,
                nameMap: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
                margin: 10
              },
              monthLabel: {
                nameMap: 'cn',
                margin: 10,
              },
              cellSize: 50,
                range: this.queryResult.months[i],
              left: 50+450*(i%3),
              top:100+360*parseInt(i/3)
            }
          )
          series.push({
            type: 'scatter',
            coordinateSystem: 'calendar',
            symbolSize: 1,
            calendarIndex:i,
            label: {
              show: true,
              formatter: function (params) {
                var d = echarts.number.parseDate(params.value[0]);
                return d.getDate()+"\n\n" ;
              },
              color: '#000000',
              fontSize:12
            },
            data: dataArr
          });
          series.push({
              type: 'scatter',
              coordinateSystem: 'calendar',
              symbolSize: 1,
              calendarIndex:i,
              label: {
                show: true,
                formatter: function (params) {

                  return   "{"+that.backgroundColors(params.value[1])+"|"+params.value[1] +"}";
                },
                color: '#000000',
                backgroundColor:
                function (params) {
                  return that.backgroundColors(params.value[1])
                },
                fontSize:18,
                verticalAlign:'middle',
                offset:[0,3],
                rich: {
                  fragment1: {
                    backgroundColor: 'rgba(0,228,0,1)',//优
                    color: '#fff',
                    padding: [1,10,1,10],

                  },
                  fragment2: {
                    backgroundColor: 'rgb(249,217,0)',
                    color: '#fff',
                    padding: [1,10,1,10],
                  },
                  fragment3: {
                    backgroundColor: 'rgba(255,126,0,1)',
                    color: '#fff',
                    padding: [1,10,1,10],
                  },
                  fragment4: {
                    backgroundColor: 'rgba(255,0,0,1)',
                    color: '#fff',
                    padding: [1,10,1,10],
                  },
                  fragment5: {
                    backgroundColor: 'rgba(153,0,76,1)',
                    color: '#fff',
                    padding: [1,10,1,10],
                  },
                  fragment6: {
                    backgroundColor: 'rgba(126,0,35,1)',
                    color: '#fff',
                  }
                }
              },
              data: heatmapData
            });
          if(this.pollutionType==="AQI") {
            series.push({
                type: 'scatter',
                coordinateSystem: 'calendar',
                symbolSize: 1,
                calendarIndex: i,
                label: {
                  show: true,
                  formatter: function (params) {

                    return '\n\n\n\n\n' + params.value[2].split(',')[0];
                  },
                  color: '#000000',
                  fontSize: 8
                },
                data: heatmapData
              })}
          series.push({
              type: 'heatmap',
              coordinateSystem: 'calendar',
              symbolSize: 1,
              calendarIndex: i,
              color: '#ffffff',

              data: heatmapData
          })

        }


        let option = {
          tooltip: {
            formatter: function (params) {
              if(that.pollutionType==="AQI")
                return [params.value[0]+":","空气质量等级:"+that.getLvl(params.value[1]),"AQI指数:"+params.value[1],"首要污染物:"+params.value[2]].join('<br />');
              else
                return [params.value[0]+":",that.radioObj[that.pollutionType]+"（IAQI指数）:"+params.value[1]].join('<br />');
            }
          },
          visualMap: {
            show: false,
            // min: 0,
            // max: 300,
            // calculable: true,
            // seriesIndex: [1],
            // orient: 'horizontal',
            // left: 'center',
            // bottom: 20,
            // inRange: {
            //   color: ['#e0ffff', '#006edd'],
            //   opacity: 0.3
            // },
            // controller: {
            //   inRange: {
            //     opacity: 0.5
            //   }
            // }
          },
          calendar: calendar,
          series: series
        };
        if (option && typeof option === "object") {
          this.myChart.setOption(option, true);
        }
      },
      selectChangeArea(val) {
        this.queryParam.area = val;
        this.selectChange()
      },
      //查询站点
      selectChange(callback) {
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
          that.selectedKeys = [checkObj[0]]
          if(callback)
            callback()
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
      onCheck(selectedKeys, info) {
        //修改选择数据
        this.selectedKeys = selectedKeys;  //只要站点的
        this.halfCheckedKeys = info.halfCheckedKeys;
      },

      parseDate(value){
        return  [value[0].format(this.format),value[1].format(this.format)];
      },
      searchQuery(){
        let  checkSites = [];
        let that = this;
        if(!that.selectedKeys||that.selectedKeys.length==0){
          this.$message.error("请选择站点！");
          return;
        }
        that.selectedKeys.forEach(e=>{
          that.siteData.forEach(element=>{
              if(e===element.key)
                checkSites.push(e)
            }
          )
        })
        let param = {year:this.timeValue,checkedKeys:checkSites.join(",")}
        queryCalendarAirQuality(param).then((res) => {
         if(res.success){
            that.queryResult = res.result
            that.drawCalendar()
         }else{
           that.$message.error(res.message);
         }
        })
      },
      searchReset(){
        this.timeValue = new Date().getFullYear();
        this.searchValue = "";
        this.area = "";
      },

      pollutionTypeChange(e){
        this.pollutionType = e.target.value;
        this.searchQuery();
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
    },
    created() {
      this.queryParam.area = '';
      let _this = this;
      getAction("/sys/sysArea/list", {active: '1'}).then((res) => {
        if (res.success) {
          _this.data = res
          // console.log(_this.data)
          _this.selectChange(_this.searchQuery);//做回掉函数
        }
      })
      this.yearChange();
    },
    mounted() {

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