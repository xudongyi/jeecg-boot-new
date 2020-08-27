<template>
    <div class="mian-pollutant">
        <div class="subtitle-gradient sources-title" style="height: 16.67%;">
            <span :style="titleLineHeight">主要污染物排放趋势分析</span>
        </div>
        <div class="line-charts">
            <div class="line-chart-item" id="COD">

            </div>
            <div class="line-chart-item" id="NH">

            </div>
            <div class="line-chart-item" id="NO2">

            </div>
            <div class="line-chart-item" id="OH">

            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "MainPollutant",
        data(){
            return {
                CODData:[
                    {
                        name:1,
                        value:80
                    },
                    {
                        name:2,
                        value:70
                    },
                    {
                        name:3,
                        value:50
                    },
                    {
                        name:4,
                        value:75
                    },
                    {

                        name:5,
                        value:40
                    },
                    {
                        name:6,
                        value:55
                    },
                ],
                NHData:[
                    {
                        name:1,
                        value:80
                    },
                    {
                        name:2,
                        value:70
                    },
                    {
                        name:3,
                        value:50
                    },
                    {
                        name:4,
                        value:75
                    },
                    {

                        name:5,
                        value:40
                    },
                    {
                        name:6,
                        value:55
                    },
                ],
                NO2Data:[
                    {
                        name:1,
                        value:80
                    },
                    {
                        name:2,
                        value:70
                    },
                    {
                        name:3,
                        value:50
                    },
                    {
                        name:4,
                        value:75
                    },
                    {

                        name:5,
                        value:40
                    },
                    {
                        name:6,
                        value:55
                    },
                ],
                OHData:[
                    {
                        name:1,
                        value:80
                    },
                    {
                        name:2,
                        value:70
                    },
                    {
                        name:3,
                        value:50
                    },
                    {
                        name:4,
                        value:75
                    },
                    {

                        name:5,
                        value:40
                    },
                    {
                        name:6,
                        value:55
                    },
                ],
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
        methods:{
            hexToRgba(hex, opacity){
                let rgbaColor = "";
                let reg = /^#[\da-f]{6}$/i;
                if (reg.test(hex)) {
                    rgbaColor = `rgba(${parseInt("0x" + hex.slice(1, 3))},${parseInt(
                        "0x" + hex.slice(3, 5)
                    )},${parseInt("0x" + hex.slice(5, 7))},${opacity})`;
                }
                return rgbaColor;
            },
            drawCOD(){
                this.CODecharts = this.$echarts.init(document.getElementById("COD"));
                this.option.color = '#625024';
                this.option.series[0].lineStyle.normal.color= '#625024';
                this.option.series[0].areaStyle.normal.shadowColor= this.hexToRgba('#625024',0.1);
                this.option.series[0].data= this.CODData.map(v => v.value);
                this.option.xAxis.data= this.CODData.map(v => v.name);
                this.CODecharts.setOption(this.option);
            },
            drawNH(){
                this.NHecharts = this.$echarts.init(document.getElementById("NH"));
                this.option.color = '#097958';
                this.option.series[0].lineStyle.normal.color= '#097958';
                this.option.series[0].areaStyle.normal.shadowColor= this.hexToRgba('#097958',0.1);
                this.option.series[0].data= this.NHData.map(v => v.value);
                this.option.xAxis.data= this.NHData.map(v => v.name);
                this.NHecharts.setOption(this.option);
            },
            drawNO2(){
                this.NO2echarts = this.$echarts.init(document.getElementById("NO2"));
                this.option.color = '#005089';
                this.option.series[0].lineStyle.normal.color= '#005089';
                this.option.series[0].areaStyle.normal.shadowColor= this.hexToRgba('#005089',0.1);
                this.option.series[0].data= this.NO2Data.map(v => v.value);
                this.option.xAxis.data= this.NO2Data.map(v => v.name);
                this.NO2echarts.setOption(this.option);
            },
            drawOH(){
                this.OHecharts = this.$echarts.init(document.getElementById("OH"));
                this.option.series[0].lineStyle.normal.color= '#071559';
                this.option.series[0].areaStyle.normal.shadowColor= this.hexToRgba('#071559',0.1);
                this.option.color = '#071559';
                this.option.series[0].data= this.OHData.map(v => v.value);
                this.option.xAxis.data= this.OHData.map(v => v.name);
                this.OHecharts.setOption(this.option);
            }
        },
        mounted() {
            this.drawCOD();
            this.drawNH();
            this.drawNO2();
            this.drawOH();
        }
    }
</script>

<style scoped>
    .mian-pollutant{
        width: 100%;
        height: 30%;
    }
    .line-charts{
        width: 100%;
        height: 83%;
    }
    .line-chart-item{
        width: 25%;
        height: 100%;
        float: left;
    }
</style>
