<template>
  <div class="fineDays" style="width: 100%;height: 100%">
    <div class="top">
      <span class="mark"></span>
      <span class="caption">大气环境质量优良天数分析（近6个月）</span>
    </div>
    <div id="charts" :style="fineDaysStyle"></div>
  </div>
</template>

<script>
  export default {
    name: "fineDays",
    data() {
      return {
        option: {
          tooltip: {
            trigger: 'axis',
            formatter: function (params) {
              var htmlStr = '';
              for(var i=0;i<params.length;i++){
                var param = params[i];
                var xName = param.name;//x轴的名称
                var seriesName = param.seriesName;//图例名称
                var value = param.value;//y轴值
                var color = param.color;//图例颜色
                var componentSubType = param.componentSubType;//类型
                if(i===0){
                  htmlStr += xName + '<br/>';//x轴的名称
                }
                htmlStr +='<div>';
                //为了保证和原来的效果一样，这里自己实现了一个点的效果
                if(value!="-"){
                  htmlStr += '<span style="margin-right:5px;display:inline-block;width:10px;height:10px;border-radius:5px;background-color:'+color+';"></span>';
                }
                // 文本颜色设置--2020-07-23(需要设置,请解注释下面一行)
                //htmlStr += '<span style="color:'+color+'">';

                //圆点后面显示的文本
                if(componentSubType==="bar"){
                  htmlStr += seriesName + '：' + value;
                }else if(componentSubType==="line"){
                  if(value!="-"){
                    htmlStr += seriesName + '：' + value+'%';
                  }
                }
                // 文本颜色设置--2020-07-23(需要设置,请解注释下面一行)
                //htmlStr += '</span>';
                htmlStr += '</div>';
              }
              return htmlStr;
            }
          },
          color: ['#23CFCF', '#5A88D6', '#FFC000'],
          legend: {
            data: ['2019', '2020', '同比增长率'],
            top: 10,
            left: 'center'
          },
          xAxis: {
            type: 'category',
            data: ["一月", "二月", "三月", "四月", "五月", "六月"],
          },
          yAxis: [{
            type: 'value',
            name: "优良天数"
          }, {
            type: 'value',
            name: '同比增长率%',
            min: 0,
            max: 100,
            position: 'right',
            axisLabel: {
              formatter: '{value}%'
            }
          }],
          series: [{
            name: '2019',
            type: 'bar',
            barGap:0,
           // barWidth: '60%',
            data: [10, 52, 200, 334, 390, 330, 220]
          }, {
            name: '2020',
            barGap:0,
            type: 'bar',
            //barWidth: '60%',
            data: [10, 52, 200, 334, 390, 330, 220]
          }, , {
            name: '同比增长率',
            type: 'line',
            data: [1, 5, 2, 34, 30, 30, 20]
          }]
        }
      }
    },
    methods: {
      showCharts() {
        var echarts = require('echarts');
        var myChart = echarts.init(document.getElementById('charts'));
        myChart.setOption(this.option);
      }
    },
    created() {

    },
    mounted() {
      this.showCharts();
    },
    props: {
      fineDaysStyle: {
        type:Object
      }
    }
  }
</script>

<style scoped>
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
    width: 42%;
    height: 20px;
    font-size: 16px;
    float: left;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: rgba(17, 17, 17, 1);
    line-height: 20px;
    margin-left: 5px
  }
</style>