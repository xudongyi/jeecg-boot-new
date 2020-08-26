<template>
  <div :style="height">
    <div class="left">
      <div class="airQuality">
        <div class="top">
          <span class="mark"></span>
          <span class="caption">辖区实时空气质量</span>
          <span class="updateTime">更新时间：{{updateTime}}时</span>
        </div>
        <div class="aqiIndex" :style="polluteDetailStyle">
          <div id="airDial" class="airDial" :style="airDialStyle"></div>
          <div class="airLevel"
               :style="{width:airLevelWidth,height:airLevelHeight,background:airLevelColor,lineHeight:airLevelHeight}">
            {{airLevelName}}
          </div>
        </div>
        <div class="polluteDetail" :style="polluteDetailStyle">
          <div class="wholePollute">
            <a-badge v-for="polluteDetail in polluteDetails" :count="polluteDetail.isFirstCode" id="badge"
                     :offset="offset">
              <a-button class="button"
                        :style="{width: buttonWidth,height:buttonHeight, background:polluteDetail.color,fontSize:buttonFontSize}">
                {{polluteDetail.value}} ug/m³<br>{{polluteDetail.key}}
              </a-button>
            </a-badge>
          </div>
        </div>
      </div>
      <div class="siteState">
        <site-state :scale="scale"></site-state>
      </div>
      <div class="alarm">
        <div class="headLine">
          <span class="block"></span>
          <span class="warn">实时报警</span>
        </div>

        <div class="allInfo">
          <div class="oneInfo" v-for="item in warnData" :key="item.key">
            <div class="leftInfo">
              <ul style="padding-top: 10%">
                <li class="iconArea" :style="'background:URL('+bgImgs[item.flag]+') no-repeat center'"></li>
                <li class="level" :style="{color:warnColors[item.flag]}">{{item.flagName}}</li>
              </ul>
            </div>
            <div class="rightInfo">
              {{item.content}}
            </div>
          </div>

        </div>
      </div>
    </div>
    <div class="right">
      <div class="fineDays">
        <fine-days :fineDaysStyle="fineDaysStyle"></fine-days>
      </div>
      <div class="calendar">
        <home-calendar :fineDaysStyle="fineDaysStyle" :cellSize="cellSize"></home-calendar>
      </div>
    </div>
  </div>
</template>

<script>
  import {mixinDevice} from '@/utils/mixin'
  import siteState from "./homeComponent/siteState";
  import moment from 'moment'
  import FineDays from "./homeComponent/fineDays";
  import {queryAirQuality, queryAlarmInfo} from "../requestAction/request";
  import homeCalendar from './HomeCalendar'
  export default {
    name: "airHome",
    mixins: [mixinDevice],
    components: {FineDays, siteState,homeCalendar},
    data() {
      return {
        cellSize:[],
        airDial:{},
        airDialStyle: {},
        fineDaysStyle: {},
        height: {},
        warnData:[],
        companyIds:'',
        buttonFontSize:"",
        //背景图
        bgImgs: {
          0: require('@/assets/icon_Over-standardalarm.png'),
          1: require('@/assets/icon_offline.png'),
          2: require('@/assets/icon_error.png'),
          3: require('@/assets/icon_Over-standardwarning.png'),
        },
        //报警颜色
        warnColors: {
          0: '#F52626',
          1: '#A1A1A1',
          2: '#CD66C2',
          3: '#FFA736',
        },
        offset: [],
        airLevelName: "",
        airLevelColor: "",
        airLevelWidth: "",
        airLevelHeight: "",
        buttonWidth: "",
        buttonHeight: "",
        polluteDetailStyle: {},
        updateTime: moment().subtract(1, "hours").format('YYYY-MM-DD HH'),
        polluteDetails: [{key: "PM2.5", value: "320", isFirstCode: "首", color: "#F80000"}, {
          key: "PM10",
          value: "35",
          isFirstCode: "0",
          color: "#00E400"
        }, {key: "SO₂", value: "20", isFirstCode: "0", color: "#00E400"}, {
          key: "NO₂",
          value: "26",
          isFirstCode: "0",
          color: "#00E400"
        }, {
          key: "CO",
          value: "34",
          isFirstCode: "0",
          color: "#00E400"
        }, {key: "O₃", value: "20", isFirstCode: "0", color: "#00E400"}],
        scale: 1,
        option: {
          // backgroundColor: "#062a44",
          series: [
            {
              type: 'gauge',
              radius: '100%',
              center: ['50%', '50%'],
              startAngle: 220,
              endAngle: -40,
              min: 0,
              max: 500,
              splitNumber: 10,
              axisLine: {
                lineStyle: {
                  color: [
                    [0.1, '#00E400'],
                    [0.2, '#FFFF00'],
                    [0.3, '#FF7E00'],
                    [0.4, '#FF0000'],
                    [0.6, '#99004C'],
                    [1, '#7E0023']
                  ],
                  width: 18
                }
              },
              //分隔线样式。
              splitLine: {
                show: false,
              },

              //仪表盘轴线
              axisLabel: {
                show: true,
                color: '#000000',
                distance: -10,
                fontSize: 12,
                formatter: function (v) {
                  switch (v) {
                    case 0:
                      return '0';
                    case 50:
                      return '50';
                    case 100:
                      return '100';
                    case 150:
                      return '150';
                    case 200:
                      return '200';
                    case 300:
                      return '300';
                    case 500:
                      return '500';
                  }

                },
              },
              axisTick: {
                show: false
              },
              pointer: {
                show: false
              },
              //仪表盘详情，用于显示数据。
              detail: {
                show: true,
                splitNumber: 15,
                offsetCenter: [0, 15],
                color: '#ddd',
                formatter: function (params) {
                  return params
                },
                textStyle: {
                  color: '#FF0000',
                },
              },
              title: {
                offsetCenter: [0, 60],
                color: '#0098A1',
                fontSize: 16,
                fontWeight: "normal",
                fontFamily: "Adobe Heiti Std"
              },
              data: [{
                value: 0,
                name: '\n\n空气质量指数AQI'
              }]
            }
          ],

        },
      }
    },
    created() {
      //获取屏幕高度计算首页内容高度
      let containerHeight = document.body.clientHeight || document.documentElement.clientHeight;
      let containerWidth = document.body.clientWidth || document.documentElement.clientWidth;
      let height = 0;
      if (this.isMobile()) {
        height = containerHeight - 52 - 59 - 93;
        containerWidth = containerWidth - 200;
      } else {
        height = containerHeight - 59 - 93;
        containerWidth = containerWidth - 200;

      }
      this.height = {height: height + 'px'}
      let scale1 = 1;
      if (containerHeight < 1080) {
        scale1 = containerHeight / 1080;
      }
      let scale2 = 1;
      if (containerWidth < 1920) {
        scale2 = containerWidth / 1920;
      }
      this.scale = Math.min(scale1, scale2);
      //高度一致height
      //宽度 width/0.65*0.3
      // containerWidth  227/185
      let airDialHeight = height * 0.4 * 0.65;
      let airDivWidth = containerWidth * 0.6 * 0.98 * 0.3;
      //let airDialWidth = airDialHeight/185*227;
      this.airDialStyle = {
        width: airDivWidth + 'px',
        height: airDialHeight + 'px',
        //'margin-left':(airDivWidth-airDialWidth)/2+'px'
      }
      //计算仪表盘下方方形区域大小
      this.airLevelWidth = airDivWidth / 2 + "px";
      this.airLevelHeight = height * 0.4 * 0.1 + "px";
      //计算大气环境质量优良天数高度
      this.fineDaysStyle.height = height * 0.49 - 35 + "px";
      this.fineDaysStyle.width = containerWidth * 0.4-12 + "px";
      //计算蓝天日历没格宽高
      this.cellSize[0] = (containerWidth * 0.4-12)*0.11;
      this.cellSize[1] = (containerWidth * 0.4-12)*0.11/70*50;
      console.log(this.cellSize);
      //获取报警信息
      this.selectWarnInfo();
    },
    mounted() {
      //获取辖区实时空气质量宽高
      let width = document.getElementsByClassName("polluteDetail")[0].clientWidth;
      let height = document.getElementsByClassName("polluteDetail")[0].clientHeight;
      //计算每个按钮的长宽
      this.buttonWidth = width * 0.3 + "px";
      this.buttonHeight = height * 0.17 + "px";
      this.offset = [-width * 0.3, 0];
      this.buttonFontSize = 20*this.scale +"px";
      this.selectAirQuality();
    },
    watch: {},
    methods: {
      drawAirDial() {
        let airQualityHeight = document.getElementsByClassName("airQuality")[0].clientHeight;
        this.polluteDetailStyle = {height: airQualityHeight - 35 + "px"}
        // let airDialHeight = document.getElementsByClassName("airDial")[0].clientHeight;
        // let airDialWidth = document.getElementsByClassName("airDial")[0].clientWidth;
        this.airDial = this.$echarts.init(document.getElementById("airDial"));
        this.option.series[0].axisLine.lineStyle.width = 18 * this.scale;//宽度
        this.option.series[0].axisLabel.fontSize = 12 * this.scale;//刻度
        this.option.series[0].axisLabel.distance = -10 / this.scale;//刻度与表盘距离
        this.option.series[0].detail.textStyle.fontSize = 40 * this.scale;//aqi的值
        this.option.series[0].detail.offsetCenter[1] = 15 * this.scale;//aqi的偏移
        this.option.series[0].title.fontSize = 20 * this.scale;//aqi的值
        this.option.series[0].title.offsetCenter[1] = 60 * this.scale;//aqi的偏移
        this.airDial.setOption(this.option, true);
      },
      //查询报警信息
      selectWarnInfo(){
        let that = this;
        queryAlarmInfo({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then(res=>{
          //console.log("bj",res.result);
          that.warnData = res.result.dataList;
        })
      },
      //查询实时空气质量
      selectAirQuality(){
        let that = this;
        queryAirQuality({companyIds:this.$store.getters.userInfo.companyIds.join(','),dateTime:this.updateTime}).then(res=>{
          that.option.series[0].data[0].value = res.result.aqi;
          that.option.series[0].detail.textStyle.color=res.result.levelRgb;
          that.polluteDetails = res.result.polluteDetails;
          that.airLevelName = res.result.levelGrade;
          that.airLevelColor = res.result.levelRgb;
          that.drawAirDial();
        })
      }
    }
  }


</script>

<style scoped>
  /*布局样式开始*/
  .left {
    height: 100%;
    width: 60%;
    float: left;
  }

  .right {
    height: 100%;
    width: 40%;
    float: right;
  }

  .left .airQuality {
    height: 40%;
    width: 98%;
    margin-right: 2%;
  }

  .left .siteState {
    height: 18%;
    width: 98%;
    margin-right: 2%;
    margin-top: 1.5%;
  }

  .left .alarm {
    height: 38%;
    width: 98%;
    margin-right: 2%;
    margin-top: 1.5%;
  }

  .fineDays, .calendar {
    height: 49%;
    width: 100%;
  }

  .calendar {
    margin-top: 2.25%;
  }

  .airQuality, .siteState, .alarm, .fineDays, .calendar {
    background: rgba(255, 255, 255, 1);
    box-shadow: 1px 2px 6px 0px rgba(0, 0, 0, 0.2);
    border-radius: 8px;
  }

  /*布局样式结束*/

  /*辖区实时空气质量样式开始*/
  .aqiIndex {
    float: left;
  }

  .airDial {
    margin-top: 10px;
  }

  .airLevel {
    height: 15%;
    width: 50%;
    border-radius: 4px;
    margin: 0 auto;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: rgba(255, 255, 255, 1);
    background-color: #00A0E9;
    text-align: center;
  }

  .polluteDetail {
    height: 100%;
    width: 65%;
    float: right;
    text-align: center;
    display: flex;
    align-items: center;
  }

  .wholePollute {
    height: 85%;
    width: 100%;
  }

  .top {
    height: 35px;
    width: 100%;
    display: flex;
    align-items: center;
    text-align: center;
    position: relative;
  }

  .mark {
    width: 8px;
    height: 20px;
    background: rgba(1, 142, 237, 1);
    float: left;
    line-height: 35px;
    margin-left: 1%;
  }

  .caption {
    width: 130px;
    height: 20px;
    font-size: 16px;
    float: left;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: rgba(17, 17, 17, 1);
    line-height: 20px;
    margin-left: 5px
  }

  .updateTime {
    width: 200px;
    height: 20px;
    font-size: 14px;
    font-family: Microsoft YaHei;
    font-weight: 400;
    float: right;
    color: rgba(154, 154, 154, 1);
    line-height: 20px;
    position: absolute;
    right: 1%;
  }

  .button {
    border: 1px solid;
    opacity: 0.6;
    border-radius: 4px;
    color: rgba(255, 255, 255, 1);
  }

  #badge {
    margin: 2%;
  }

  /*辖区实时空气质量样式结束*/
  .headLine {
    width: 100%;
    height: 12%;
    display: flex;
    align-items: center;
    text-align: center;
    position: relative;
  }

  .headLine .block {
    width: 8px;
    height: 20px;
    background: rgba(1, 142, 237, 1);
    float: left;
    margin: 0 0 0 1%;
  }

  .headLine .warn {
    width: 65px;
    height: 18px;
    line-height: 20px;
    margin-left: 5px;
    font-size: 16px;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: rgba(17, 17, 17, 1);
  }

  .allInfo {
    width: 100%;
    height: 88%;
  }

  .oneInfo {
    width: 100%;
    height: 25%;
    border-bottom: 1px solid rgba(235, 235, 235, 1);
  }

  .leftInfo {
    width: 8%;
    height: 85%;
    float: left;
  }
  .leftInfo ul{
    padding:0;
    list-style:none;
  }
  .leftInfo li{
    display: block;
    padding:0;
    margin:0;
    list-style:none
  }

  .iconArea {
    width: 100%;
    height: 24px;
  }

  .level {
    width: 100%;
    height: 30%;
    font-size: 8px;
    text-align: center;
    font-family: Microsoft YaHei;
    font-weight: 400;
  }
  .rightInfo {
    width: 91%;
    height: 100%;
    float: right;
    padding-top: 1%;
    margin-right: 1%;
    text-indent: 2em;
    font-size:13px;
    font-family:Microsoft YaHei;
    font-weight:400;
    color:rgba(0,0,0,1);
  }

</style>