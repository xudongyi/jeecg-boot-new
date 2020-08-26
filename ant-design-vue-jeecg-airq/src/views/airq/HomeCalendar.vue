<template>
  <div>
    <div class="titleRow">
      <div class="top">
        <span class="mark"></span>
        <span class="caption">蓝天日历</span>
        <span class="updateTime"><a-month-picker placeholder="Select month" :value="queryParam.queryTime" format="YYYY-MM"  valueFormat="YYYY-MM"  @change="monthChange" /></span>
      </div>
    </div>
    <div id="calendar" style="width:100%;height:500px;z-index:19;"></div>
  </div>

</template>

<script>
  import moment from 'moment'
  import {queryAirHomeCalendar} from "../requestAction/request";

  export default {
      name: "HomeCalendar",
      data(){
        return {
          queryParam:{queryTime:moment().format('YYYY-MM')},
          queryResult:[],
        }
      },
      methods:{
        monthChange(val){
          this.queryParam.queryTime = val;
          this.searchQuery();
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
          if(!this.myChart){
            let dom  = document.getElementById("calendar")
            option ={
              renderer: "Canvas",
              height:370
            };

            this.myChart = echarts.init( dom,null,option);
          }
          let that = this;

          let heatmapData = [];
          let calendar = [];
          let series = [];
          let dateArr = [];
          this.countDays = 0;
          for(let i = 0;i<this.queryResult.length; i++){
            let tmp = this.queryResult[i]
            heatmapData.push([tmp.dataTime,tmp.AQI,tmp.firstCode]);
          }
          var date = +echarts.number.parseDate(this.queryParam.queryTime + '-01');
          let a = moment(this.queryParam.queryTime,'YYYY-MM')
          a= a.add(1, 'months')
          var end = +echarts.number.parseDate(a.format('YYYY-MM')+'-01');
          var dayTime = 3600 * 24 * 1000;
          for (let time = date; time < end; time += dayTime) {
            let tmp = echarts.format.formatTime('yyyy-MM-dd', time);
            dateArr.push([tmp,'','']);
          }
          calendar.push(
            {
              orient: 'vertical',
              left: 'center',
              top:20,
              yearLabel:{
                show:false
              },
              dayLabel: {
                firstDay: 7,
                nameMap: ['日', '一', '二', '三', '四', '五', '六'],
                margin: 0
              },
              monthLabel: {
                show:false
              },
              cellSize: [55,50],
              range: this.queryParam.queryTime,
            }
          );
          //日期
          series.push({
            type: 'scatter',
            coordinateSystem: 'calendar',
            symbolSize: 1,
            //calendarIndex:0,
            label: {
              show: true,
              formatter: function (params) {
                var d = echarts.number.parseDate(params.value[0]);
                return d.getDate()+"\n\n" ;
              },
              color: '#000000',
              fontSize:12
            },
            data: dateArr
          });
          //AQI
          series.push({
            type: 'scatter',
            coordinateSystem: 'calendar',
            symbolSize: 1,
            //calendarIndex:0,
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

          series.push({
            type: 'scatter',
            coordinateSystem: 'calendar',
            symbolSize: 1,
            //calendarIndex: 0,
            label: {
              show: true,
              formatter: function (params) {
                return '\n\n\n\n' + params.value[2].split(',')[0];
              },
              color: '#000000',
              fontSize: 8
            },
            data: heatmapData
          });
          series.push({
            type: 'heatmap',
            coordinateSystem: 'calendar',
            symbolSize: 1,
            //calendarIndex: i,
            color: '#ffffff',

            data: heatmapData
          });
          let option = {
            tooltip: {
              formatter: function (params) {
                  return [params.value[0].substring(0,11),"空气质量等级:"+that.getLvl(params.value[1]),"AQI指数:"+params.value[1],"首要污染物:"+params.value[2]].join('<br />');
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
        searchQuery(){
          let that = this;
          queryAirHomeCalendar(this.queryParam).then(res=>{
            if(res.success){
              console.log(res);
              that.queryResult = res.result.dataList;
              console.log("!",that.queryResult);
              that.drawCalendar()
            }else{
              that.$message.error(res.message);
            }
          })
        }
      },
      mounted(){
        this.searchQuery();
      },

  }
</script>

<style scoped>
  .titleRow {
    width: 100%;
    height: 35px;
  }
  .titleRow .block {
    width: 8px;
    height: 18px;
    background: rgba(1, 142, 237, 1);
    float: left;
    margin: 15px 0 0 10px;
  }
  .smallTitle {
    width: 30%;
    height: 18px;
    padding: 12px 0 0 27px;
    font-size: 16px;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: rgba(17, 17, 17, 1);
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
    width: 65px;
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
    height: 35px;
    float: right;
    position: absolute;
    top:1.5px;
    right: 1%;
  }
</style>