<template>
    <div class="major-analysis">
        <div class="subtitle-gradient sources-title">
            <span :style="titleLineHeight">污染物年排放大户分析</span>
        </div>
        <div class="radio-group">
            <a-button :ghost="checkedRadio!=='COD'" @click="check('COD')" :style="checkedRadio==='COD'?radio_check:radio_column" class="radio-column">COD</a-button>
            <a-button :ghost="checkedRadio!=='NH'" @click="check('NH')" :style="checkedRadio==='NH'?radio_check:radio_column"  class="radio-column">氨氮</a-button>
            <a-button :ghost="checkedRadio!=='SO2'" @click="check('SO2')" :style="checkedRadio==='SO2'?radio_check:radio_column"  class="radio-column">二氧化硫</a-button>
            <a-button :ghost="checkedRadio!=='NO'" @click="check('NO')" :style="checkedRadio==='NO'?radio_check:radio_column"  class="radio-column">氮氧化物</a-button>
        </div>
        <div id = 'analysis'>

        </div>

    </div>
</template>

<script>
    import {debounce} from "lodash";

    export default {
        name: "MajorPollutantAnalysis",
        data(){
            return {
                checkedRadio:'COD',


                dataSource:[
                    ['amount', 'product'],
                    [58, '康鑫药业'],
                    [78, '三闸纺织'],
                    [41, '钧鼎电子'],
                    [99,  '周家口食品'],
                    [20,  '中宝家访'],
                    [79, '三九只要'],
                    [91, '三菱重工'],
                    [100, '科顺建材'],
                    [20, '中天科技']
                ],
                option : {
                    dataset: {

                    },
                    tooltip: {},
                    color: ['#3398DB'],
                    grid: {containLabel: true,left:'5%',top:'10%'},
                    xAxis: {splitLine: {show: false}},
                    yAxis: {type: 'category',  axisLabel: { color: '#8EBAFB',fontSize:this.$original_height*this.$store.getters.scale/78},},
                    series: [
                        {
                            type: 'bar',
                            barWidth: '60%',
                            encode: {
                                // Map the "amount" column to X axis.
                                x: 'amount',
                                // Map the "product" column to Y axis
                                y: 'product'
                            },
                        },

                    ]
                }
            }
        },
        computed:{
            //样式
            radio_check(){


                return {
                    background:'linear-gradient(12deg,rgba(0,43,142,1) 0%,rgba(0,85,120,1) 100%)',
                    'border-radius':'4px',
                    color: 'white',
                    'border-color': 'rgba(0,85,120,1)',
                    'font-size':this.$original_height*this.$store.getters.scale/78+'px'
                }
            },
            radio_column(){
                return {
                    border: '2px solid rgba(8,199,241,1)',
                    'border-radius': '4px',
                    color: 'rgba(8,199,241,1)',
                    'font-size':this.$original_height*this.$store.getters.scale/78+'px'
                }
            },
            titleLineHeight(){
                if(this.analysis){
                    this.analysis.resize();
                    //字体大小
                    this.option.yAxis.axisLabel.fontSize=this.$original_height*this.$store.getters.scale/78;
                    this.analysis.setOption(this.option, true);
                }
                return{
                    'line-height':this.$original_height*this.$store.getters.scale/22+'px',
                    'font-size':this.$original_height*this.$store.getters.scale/45+'px',
                }
            },
        },
        methods:{
            check(val){
                this.checkedRadio = val
                this.dataSource.forEach((e,index)=>{
                    this.dataSource[index][0] = Math.random()*100
                })
                console.log(  this.dataSource)
                this.drawBar()

            },
            drawBar(){

                this.dataSource.sort((a,b)=>{
                    return a[0]-b[0]
                })
                this.option.dataset.source = this.dataSource
                // this.option.series[0].data = this.dataSource.map(function (e) {
                //     return e[1];
                // })
                this.analysis.setOption(this.option, true);
            },


        },


        mounted() {
            this.dom =  document.getElementById("analysis");
            this.analysis = this.$echarts.init(this.dom);

            this.drawBar()
        },

    }
</script>

<style scoped>

    .major-analysis{
        width: 100%;
        height: 50%;
    }
    .radio-group{
        margin-top: 2%;
        width: 100%;
        height: 30px;
    }

    .radio-column{
       margin-left: 2.5%;
        width:22%;
        height:100%;
        padding: 0  2%;
    }
    #analysis{
        width: 100%;
        height: 90%;
    }
</style>
