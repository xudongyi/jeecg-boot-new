<template>
    <div class="major-emissions">
        <div class="subtitle-gradient sources-title" style="height: 14.29%">
            <span :style="titleLineHeight">全市各行业主要污染物排放分析</span>
        </div>
        <a-radio-group name="radioGroup" :default-value="1" style="margin-left: 5px;" >
            <a-radio :value="1" class="radio-column" :style="radioFontStyle">
                COD
            </a-radio>
            <a-radio :value="2" class="radio-column" :style="radioFontStyle">
                氨氮
            </a-radio>
            <a-radio :value="3" class="radio-column" :style="radioFontStyle">
                二氧化碳
            </a-radio>
            <a-radio :value="4" class="radio-column" :style="radioFontStyle">
               氢氧化物
            </a-radio>


        </a-radio-group>
        <!--图表 -->
        <div id = 'emissionsBar'>

        </div>
        <div class="bar-comment" >
            <div v-for="item in dataSource" :key="item.name" >
                <a-badge :color="item.itemStyle.normal.color" text="" />
                <span :style="commentStyle" class="comment-text">{{item.name+':'+item.value+'%'}}</span>
            </div>
        </div>
    </div>
</template>

<script>
    //全市各行业主要污染物排放分析
    export default {
        name: "MajorEmissions",
        data(){
            return {
                //图表数据
                dataSource:[
                    {
                        value: 57.58,
                        name:'有色金属冶炼与压延加工',
                        itemStyle: {
                            normal: {
                                color: "#9b59b6"
                            }
                        }
                    },
                    {
                        value: 92.35,
                        name:'造纸与纸制品',
                        itemStyle: {
                            normal: {
                                color: "#f1c40f"
                            }
                        }
                    },
                    {
                        value: 73.54,
                        name:'有色金属矿采选业',
                        itemStyle: {
                            normal: {
                                color: "#e74c3c"
                            }
                        }
                    },
                    {
                        value: 14.9,
                        name:'非金属采矿选',
                        itemStyle: {
                            normal: {
                                color: "#34495e"
                            }
                        }
                    },
                    {
                        value: 28.6,
                        name:'化学原料与产品制作',
                        itemStyle: {
                            normal: {
                                color: "#2ecc71"
                            }
                        }
                    },
                    {
                        value: 58,
                        name:'其他',
                        itemStyle: {
                            normal: {
                                color: "#3396d9"
                            }
                        }
                    },
                ],
                option:{
                    tooltip:{
                        position: [10, 10]
                    },
                    angleAxis: {
                        interval: 1,
                        type: 'category',
                        axisLine: {
                            show: true,
                        },
                        axisLabel: {

                            show: true,

                        },
                    },
                    radiusAxis: {
                        min: 0,
                        max: 100,
                        interval: 20,//刻度线20一划
                        axisLine: {
                            show: true,
                            lineStyle: {
                                color: "rgba(255,255,255,0.6)",
                                width: 1,
                                type: "solid"
                            },
                        },
                        axisLabel: {
                            formatter: function(val){
                                if(20===val||60===val||100===val)//强制只有20 60 100的刻度标
                                    return val+"%"
                                return ''
                            },
                            showMaxLabel:true,
                            showMinLabel:false,
                            show: true,
                            padding: [0, 0, 12*this.$store.getters.scale^2, 0],
                            color: "#b0caff",
                            fontSize: 12*this.$store.getters.scale
                        },
                        splitLine: {
                            lineStyle: {
                                color: "rgba(255,255,255,0.2)",
                                width: 1,
                                type: "solid"
                            }
                        }
                    },
                    polar: {},
                    series: [{
                        type: 'bar',
                        data: [],
                        coordinateSystem: 'polar',
                    }],
                }
            }
        },
        computed:{
            titleLineHeight() {
                return this.$store.getters.titleFont
            },
            radioFontStyle(){
                return {
                    'font-size':20*this.$store.getters.scale*this.$store.getters.scale+'px',
                    'line-height':40*this.$store.getters.scale+'px',
                    'margin-left':10*this.$store.getters.scale*this.$store.getters.scale+'px',
                }
            },
            commentStyle(){
                return{
                    'font-size':14*this.$store.getters.scale+'px',
                }
            }
            // radioSize(){
            //     if(this.$store.getters.scale<0.6)
            //         return 'small';
            //     if(this.$store.getters.scale<0.8)
            //         return 'default '
            //     return "large"
            // }
        },
        watch:{
            titleLineHeight(){
                  this.option.radiusAxis.axisLabel.fontSize =  12*this.$store.getters.scale;
                  this.option.radiusAxis.axisLabel.padding[2] =  12*this.$store.getters.scale^2;
                    this.myChart.resize();
                    this.myChart.setOption(this.option);
            }
        },
        mounted() {
            let dom = document.getElementById("emissionsBar");
            this.myChart = this.$echarts.init(dom);
            //修改数据
            this.option.series[0].data=this.dataSource;
            this.myChart.setOption(this.option);
        }
    }
</script>

<style>
    .major-emissions{
        height: 35%;
    }
    #emissionsBar{
        height: 70%;
        width: 50%;
        float: left;
    }
    .radio-column{
        color: white;
        margin-right: 0px;
    }
    .ant-radio-inner{
        background: #00032b;
        border-color: #147be0;
    }
    .bar-comment{
        height: 60%;
        width: 50%;
        float: right;
        margin-top: 9%;
    }
    .ant-badge-status-dot{
        width: 10px;
        height: 10px;
    }
    .comment-text{
        margin-left: 2px;
        color: white;
    }
</style>
