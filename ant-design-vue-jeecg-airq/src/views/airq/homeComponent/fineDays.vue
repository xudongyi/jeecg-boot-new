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
  import {queryFineDays, querySiteState} from "../../requestAction/request";
  import moment from "moment";

  export default {
    name: "fineDays",
    data() {
      return {
        option: {
          tooltip: {
            trigger: 'axis'
          },
          color: ['#00E400', '#FFFF00'],
          legend: {
            data: ['优', '良'],
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
          }],
          series: [{
            name: '优',
            type: 'bar',
            barGap: 0,
            data: [0, 0, 0, 0, 0, 0, 0]
          }, {
            name: '良',
            barGap: 0,
            type: 'bar',
            data: [0, 0, 0, 0, 0, 0, 0]
          }]
        }
      }
    },
    methods: {
      showCharts() {
        var echarts = require('echarts');
        var myChart = echarts.init(document.getElementById('charts'));
        myChart.setOption(this.option);
      },
      selectFineDays() {
        queryFineDays({companyIds: this.$store.getters.userInfo.companyIds.join(',')}).then(res => {
          this.option.series[0].data = res.result.excellentDays;
          this.option.series[1].data = res.result.fineDays;
          this.option.xAxis.data = res.result.months;
          this.showCharts();
        })
      }
    },
    created() {

    },
    mounted() {
      this.selectFineDays();
    },
    props: {
      fineDaysStyle: {
        type: Object
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