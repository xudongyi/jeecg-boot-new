<template>
    <div class="online-status-companies">
        <div class="subtitle-gradient sources-title" style="height: 14.29%;margin-left: 5%">
            <span :style="titleLineHeight">污染源企业在线状况分析（近30天）</span>
        </div>
        <div class="lines-charts">
            <div class="line-charts-item" id="onlineRate">

            </div>
            <div class="line-charts-item" id="dataTransferRate">

            </div>
            <div class="line-charts-item" id="dataEfficient">

            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "onlineStatusCompanies",
        data(){
            return{
                option : {
                    title:{
                        text:'COD',
                        top:10,
                        left:'40%',
                        textStyle:{
                            color:'rgba(255,255,255,1)',
                            fontSize:14,
                            fontWeight:400,
                        }
                    },

                    color: '',
                    tooltip: {
                        trigger: "axis",
                        formatter: function(params) {
                            let html = '';
                            params.forEach(v => {//background-color:${color[v.componentIndex]}color:${color[v.componentIndex]};
                                console.log(v)
                                html += `<div style="color: #666;font-size: 14px;line-height: 24px">
                                <span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;"></span>
                               ${v.name}月
                                <span style="font-weight:700;font-size: 18px">${v.value}</span>
                                吨`;
                            })
                            return html
                        },
                        extraCssText: 'background: #fff; border-radius: 0;box-shadow: 0 0 3px rgba(0, 0, 0, 0.2);color: #333;',
                        axisPointer: {
                            type: 'shadow',
                            shadowStyle: {
                                color: '#ffffff',
                                shadowColor: 'rgba(225,225,225,1)',
                                shadowBlur: 5
                            }
                        }
                    },
                    grid: {
                        containLabel: true
                    },
                    xAxis: [{
                        type: "category",
                        boundaryGap: false,
                        axisLabel: {
                            formatter: '{value}月',
                            textStyle: {
                                color: "#333"
                            }
                        },
                        axisLine: {
                            lineStyle: {
                                color: "#D9D9D9"
                            }
                        },
                        data: []
                    }],
                    yAxis: [{
                        type: "value",
                        name: '单位：吨',
                        max:100,
                        axisLabel: {
                            textStyle: {
                                color: "#666"
                            }
                        },
                        nameTextStyle: {
                            color: "#666",
                            fontSize: 12,
                            lineHeight: 40
                        },
                        splitLine: {
                            lineStyle: {
                                type: "dashed",
                                color: "#E9E9E9"
                            }
                        },
                        axisLine: {
                            show: false
                        },
                        axisTick: {
                            show: false
                        }
                    }],
                    series: [ {

                        type: "line",
                        smooth: true,
                        // showSymbol: false,
                        symbolSize: 8,
                        zlevel: 3,
                        lineStyle: {
                            normal: {
                                color: [],//线条颜色

                            }
                        },
                        areaStyle: {
                            normal: {
                                //渐变色
                                // color: new echarts.graphic.LinearGradient(
                                //     0,
                                //     0,
                                //     0,
                                //     1,
                                //     [{
                                //         offset: 0,
                                //         color: hexToRgba(color[1], 0.3)
                                //     },
                                //         {
                                //             offset: 1,
                                //             color: hexToRgba(color[1], 0.1)
                                //         }
                                //     ],
                                //     false
                                // ),
                                shadowColor:'',
                                shadowBlur: 10
                            }
                        },
                        data: []
                    }]
                }
            }
        },
        computed:{
            titleLineHeight() {
                return this.$store.getters.titleFont
            },

        },
    }
</script>

<style scoped>
    .online-status-companies{
        height: 35%;
        width: 100%;
        background-color: white;
    }
</style>
