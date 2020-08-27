<template>

    <div class="annual-discharge">
        <div class="subtitle-gradient sources-title" style="height: 14.29%;margin-left: 5%">
            <span :style="titleLineHeight">全市各行业主要污染物排放分析</span>
        </div>
        <div class="pictorialBar">

            <div class="bar-charts">
                <div class="bar-title bar-title-left" :style="titleFontSize">
                    <span style="float: left">废水</span>
                    <span style="float: right">{{wastewaterDischarge}}吨</span>
                </div>
                <div id="wastewater_discharge">

                </div>
            </div>
            <div class="bar-charts">
                <div class="bar-title bar-title-right" :style="titleFontSize">
                    <span style="float: left">废水</span>
                    <span style="float: right">{{exhaustEmissions}}m³</span>
                </div>
                <div id="exhaust_emissions">

                </div>
            </div>
        </div>
    </div>

</template>

<script>
    export default {
        name: "AnnualDischarge",
        data(){
          return {
              wastewaterDischarge:21323.90,
              exhaustEmissions:21323.90,
              option:{
                  "grid": {
                  },
                  "tooltip": {},
                  "xAxis": {
                      "data": [],
                      "axisTick": {
                          "show": false
                      },
                      "axisLine": {
                          "show": false
                      },
                      "axisLabel": {
                          "show": false
                      }
                  },
                  "yAxis": {
                      "splitLine": {
                          "show": false
                      },
                      "axisTick": {
                          "show": false
                      },
                      "axisLine": {
                          "show": false
                      },
                      "axisLabel": {
                          "show": false
                      }
                  },
                  "series": [{
                      "name": "",
                      "type": "pictorialBar",
                      "symbolSize": [100, 45],
                      "symbolOffset": [0, -14],
                      "z": 12,
                      "data": [{
                          "name": "关井数",
                          "value": "981",
                          "trueVal": "98",
                          "symbolPosition": "end",
                          "itemStyle": {
                              "normal": {
                                  "color": "#00fff5"
                              }
                          }
                      }, {
                          "name": "开井数",
                          "value": "4991",
                          "trueVal": "499",
                          "symbolPosition": "end",
                          "itemStyle": {
                              "normal": {
                                  "color": "#ffcc00"
                              }
                          }
                      }, {
                          "name": "不在线",
                          "value": "21",
                          "trueVal": "2",
                          "symbolPosition": "end",
                          "itemStyle": {
                              "normal": {
                                  "color": "#b9b7ff"
                              }
                          }
                      }]
                  }, {
                      "name": "",
                      "type": "pictorialBar",
                      "symbolSize": [100, 45],
                      "symbolOffset": [0, 24],
                      "z": 12,
                      "data": [{
                          "name": "关井数",
                          "value": "981",
                          "trueVal": "98",
                          "itemStyle": {
                              "normal": {
                                  "color": "#43bafe"
                              }
                          }
                      }, {
                          "name": "开井数",
                          "value": "4991",
                          "trueVal": "499",
                          "itemStyle": {
                              "normal": {
                                  "color": "#ff7800"
                              }
                          }
                      }, {
                          "name": "不在线",
                          "value": "21",
                          "trueVal": "2",
                          "itemStyle": {
                              "normal": {
                                  "color": "#e9a5ff"
                              }
                          }
                      }]
                  }, {
                      "name": "",
                      "type": "pictorialBar",
                      "symbolSize": [150, 75],
                      "symbolOffset": [0, 44],
                      "z": 11,
                      "data": [{
                          "name": "关井数",
                          "value": "981",
                          "trueVal": "98",
                          "itemStyle": {
                              "normal": {
                                  "color": "transparent",
                                  "borderColor": "#43bafe",
                                  "borderWidth": 5
                              }
                          }
                      }, {
                          "name": "开井数",
                          "value": "4991",
                          "trueVal": "499",
                          "itemStyle": {
                              "normal": {
                                  "color": "transparent",
                                  "borderColor": "#ff7800",
                                  "borderWidth": 5
                              }
                          }
                      }, {
                          "name": "不在线",
                          "value": "21",
                          "trueVal": "2",
                          "itemStyle": {
                              "normal": {
                                  "color": "transparent",
                                  "borderColor": "#e9a5ff",
                                  "borderWidth": 5
                              }
                          }
                      }]
                  }, {
                      "name": "",
                      "type": "pictorialBar",
                      "symbolSize": [200, 100],
                      "symbolOffset": [0, 62],
                      "z": 10,
                      "data": [{
                          "name": "关井数",
                          "value": "981",
                          "trueVal": "98",
                          "itemStyle": {
                              "normal": {
                                  "color": "transparent",
                                  "borderColor": "#43bafe",
                                  "borderType": "dashed",
                                  "borderWidth": 5
                              }
                          }
                      }, {
                          "name": "开井数",
                          "value": "4991",
                          "trueVal": "499",
                          "itemStyle": {
                              "normal": {
                                  "color": "transparent",
                                  "borderColor": "#ff7800",
                                  "borderType": "dashed",
                                  "borderWidth": 5
                              }
                          }
                      }, {
                          "name": "不在线",
                          "value": "21",
                          "trueVal": "2",
                          "itemStyle": {
                              "normal": {
                                  "color": "transparent",
                                  "borderColor": "#e9a5ff",
                                  "borderType": "dashed",
                                  "borderWidth": 5
                              }
                          }
                      }]
                  }, {
                      "type": "bar",
                      "silent": true,
                      "barWidth": 100,
                      "barGap": "-100%",
                      "label": {
                          "normal": {
                              "show": true,
                              "position": "top",
                              "distance": 40,
                              "textStyle": {
                                  "color": "#fff",
                                  "fontSize": 40
                              }
                          }
                      },
                      "data": [{
                          "name": "关井数",
                          "value": "981",
                          "trueVal": "98",
                          "itemStyle": {
                              "normal": {
                                  "color": {
                                      "x": 0,
                                      "y": 0,
                                      "x2": 0,
                                      "y2": 1,
                                      "type": "linear",
                                      "global": false,
                                      "colorStops": [{
                                          "offset": 0,
                                          "color": "rgba(0,255,245,0.5)"
                                      }, {
                                          "offset": 1,
                                          "color": "#43bafe"
                                      }]
                                  }
                              }
                          }
                      }, {
                          "name": "开井数",
                          "value": "4991",
                          "trueVal": "499",
                          "itemStyle": {
                              "normal": {
                                  "color": {
                                      "x": 0,
                                      "y": 0,
                                      "x2": 0,
                                      "y2": 1,
                                      "type": "linear",
                                      "global": false,
                                      "colorStops": [{
                                          "offset": 0,
                                          "color": "rgba(255,204,0,0.5)"
                                      }, {
                                          "offset": 1,
                                          "color": "#ff7800"
                                      }]
                                  }
                              }
                          }
                      }, {
                          "name": "不在线",
                          "value": "21",
                          "trueVal": "2",
                          "itemStyle": {
                              "normal": {
                                  "color": {
                                      "x": 0,
                                      "y": 0,
                                      "x2": 0,
                                      "y2": 1,
                                      "type": "linear",
                                      "global": false,
                                      "colorStops": [{
                                          "offset": 0,
                                          "color": "rgba(185,183,255,0.5)"
                                      }, {
                                          "offset": 1,
                                          "color": "#e9a5ff"
                                      }]
                                  }
                              }
                          }
                      }]
                  }]
              }
          }
        },
        computed: {
            titleLineHeight() {
                return this.$store.getters.titleFont
            },
            titleFontSize() {
                return {
                    'line-height':600*this.$store.getters.scale/22+'px',
                    'font-size':16*this.$store.getters.scale+'px',
                }
            },
        },
        mounted() {
            this.waterChart = this.$echarts.init(document.getElementById("wastewater_discharge"));
            //修改数据
            this.option.series[0].data=this.dataSource;
            this.waterChart.setOption(this.option);
        }

    }
</script>

<style scoped>
    .annual-discharge{
        height: 35%;
        width: 100%;

    }
    .pictorialBar{
        height: 85%;
        width: 100%;

    }
    .bar-charts{
        float: left;
        width: 50%;
        height: 100%;
    }
    .bar-title{
        width:60%;
        height:10%;
        border-radius:4px;
        margin: 1% 20% 0 20%;
        padding: 0 5% 0 5%;
        color: white;
    }
    .bar-title-left{
        background:rgba(0,123,139,1);
    }
    .bar-title-right{
        background:rgba(33,139,255,1);
    }
    #wastewater_discharge{
        height: 89%;
        width: 100%;
        background: white;
    }
</style>
