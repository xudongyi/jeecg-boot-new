<template>
    <div  class="main-content">
        <div id = 'container'>

        </div>
        <div class="mark-label" >
            <div class="label-column label-border">
                <div class="label-icon label-icon1"></div>
                <span style="color:#00FFFF" >全部</span>
            </div>
            <div class="label-column label-border">
                <div class="label-icon label-icon2"></div>
                <span style="color:#00FFFF" >正常</span>
            </div>
            <div class="label-column label-border-longer">
                <div class="label-icon-longer label-icon3"></div>
                <span style="color:#00FFFF" >部分离线</span>
            </div>
            <div class="label-column label-border">
                <div class="label-icon label-icon4"></div>
                <span style="color:#00FFFF" >异常</span>
            </div>
            <div class="label-column label-border">
                <div class="label-icon label-icon5"></div>
                <span style="color:#00FFFF" >超标</span>
            </div>
            <div class="label-column label-border-longer">
                <div class="label-icon-longer label-icon6"></div>
                <span style="color:#00FFFF" >全部离线</span>
            </div>
        </div>

    </div>
</template>

<script>
    import nantong from '../../assets/json/nantong'
    export default {
        name: "ContentModule",
        data(){
            return {

            }
        },
        computed:{
          scale(){
              return this.$store.getters.scale
          }
        },
        watch:{
            scale(){
                if(this.myChart)
                    this.myChart.resize()

            },
        },
        created(){

            // console.log( this.contentStyle)
        },
        mounted() {

            let dom = document.getElementById("container");
            this.myChart = this.$echarts.init(dom);
            this.myChart.showLoading();
            // let index = -1;
            // var timer = setInterval(function() {
            //     // 隐藏提示框
            //     myChart.dispatchAction({
            //         type: 'hideTip',
            //         seriesIndex: 0,
            //         dataIndex: index
            //     });
            //     // 显示提示框
            //     myChart.dispatchAction({
            //         type: 'showTip',
            //         seriesIndex: 0,
            //         dataIndex: index + 1
            //     });
            //     // 取消高亮指定的数据图形
            //     myChart.dispatchAction({
            //         type: 'downplay',
            //         seriesIndex: 0,
            //         dataIndex: index
            //     });
            //     // 高亮指定的数据图形
            //     myChart.dispatchAction({
            //         type: 'highlight',
            //         seriesIndex: 0,
            //         dataIndex: index + 1
            //     });
            //     index++;
            //     if (index > 8) {
            //         index = -1;
            //     }
            // }, 2000);


            // myChart.on('mousemove', function(e) {
            //     clearInterval(timer);
            //     myChart.dispatchAction({
            //         type: 'downplay',
            //         seriesIndex: 0
            //     });
            //     myChart.dispatchAction({
            //         type: 'highlight',
            //         seriesIndex: 0,
            //         dataIndex: e.dataIndex
            //     });
            //     myChart.dispatchAction({
            //         type: 'showTip',
            //         seriesIndex: 0,
            //         dataIndex: e.dataIndex
            //     });
            // }); //---------------------------------------------鼠标移入静止播放
            // myChart.on('mouseout', function(e) {
            //     clearInterval(timer);
            //     myChart.dispatchAction({
            //         type: 'downplay',
            //         seriesIndex: 0,
            //         dataIndex: e.dataIndex
            //     }); //鼠标移出后先把上次的高亮取消
            //     timer = setInterval(function() {
            //         // 隐藏提示框
            //         myChart.dispatchAction({
            //             type: 'hideTip',
            //             seriesIndex: 0,
            //             dataIndex: index
            //         });
            //         // 显示提示框
            //         myChart.dispatchAction({
            //             type: 'showTip',
            //             seriesIndex: 0,
            //             dataIndex: index + 1
            //         });
            //         // 取消高亮指定的数据图形
            //         myChart.dispatchAction({
            //             type: 'downplay',
            //             seriesIndex: 0,
            //             dataIndex: index
            //         });
            //         // 高亮指定的数据图形
            //         myChart.dispatchAction({
            //             type: 'highlight',
            //             seriesIndex: 0,
            //             dataIndex: index + 1
            //         });
            //         index++;
            //         if (index > 8) {
            //             index = -1;
            //         }
            //     }, 2000);
            // });


            this.$echarts.registerMap('nantong', nantong);
            this.myChart.hideLoading();
            var mapDate = [
                {
                    name:'检测站点4',
                    value: [120.927871,32.008808,25]
                },
                {
                    name:'检测站点4',
                    value: [120.840399,32.022324,30]
                },
                {
                    name:'检测站点4',
                    value: [120.510345,32.552213,60]
                },
                {
                    name:'检测站点4',
                    value: [120.21, 30.209,200]
                },
                {
                    name:'检测站点4',
                    value: [121.070062,32.086648,-1]
                },
                {
                    name:'检测站点4',
                    value: [121.166166,31.894507,40]
                },
                {
                    name:'检测站点4',
                    value: [121.673436,31.831699,60]
                },
                {
                    name:'检测站点4',
                    value: [121.069858,31.968657,-2]
                },
                {
                    name:'检测站点4',
                    value: [121.043647,32.325238,40]
                },
                {
                    name:'检测站点3',
                    value: [120.824335,32.356155,20]
                },
                {
                    name:'检测站点2',
                    value: [121.185158,32.309364,1]
                },
                {
                    name:'检测站点1',
                    value: [120.563238,32.366663,400],

                }
            ];


            let option = {
                tooltip:{

                    trigger:'item',
                },

                visualMap: {
                    min: 0,
                    max: 600,

                    seriesIndex: 1,
                    show:false,
                    type: 'piecewise',
                    right: 100,
                    bottom: 100,
                    textStyle: {
                        color: '#FFFF'
                    },
                    splitList: [
                        {
                            gt: 100,
                            color: '#e72c31',
                        }, //大于5万人
                        {
                            gt: 50,
                            lte: 100,
                            color: '#e78120',
                        }, //3-5万人
                        {
                            gte: 0,
                            lte: 50,
                            color: '#30e837',
                        }, //1-3万人
                        {
                            lt: 0,
                            gte:-1,
                            color: '#05828e',
                        },
                        {
                            lt:-1,
                            color: '#67696f',
                        }
                    ]
                },

                geo: {
                    map: 'nantong',
                    aspectScale: 0.75, //长宽比
                    zoom: 1.1,
                    roam: false,
                    itemStyle: {
                        normal: {
                            areaColor: '#101011',
                            shadowColor: '#101011',
                            shadowOffsetX: 0,
                            shadowOffsetY: 10
                        },
                        emphasis: {
                            areaColor: '#2c97e4',
                            borderWidth: 0,
                            color: 'green',
                            label: {
                                show: false
                            }
                        }
                    }
                },
                series: [ {
                    type: 'map',
                    roam: true,
                    label: {
                        normal: {
                            show: true,
                            textStyle: {
                                color: '#fff'
                            }
                        },
                        emphasis: {
                            textStyle: {
                                color: '#fff'
                            }
                        }
                    },
                    itemStyle: {
                        normal: {
                            borderColor: '#2ab8ff',
                            borderWidth: 1.5,
                            areaColor: '#12235c'
                        },
                        emphasis: {
                            areaColor: '#2AB8FF',
                            borderWidth: 0,
                            color: 'green'
                        }
                    },
                    zoom: 1.1,
                    roam: false,
                    map: 'nantong' //使用
                    // data: this.difficultData //热力图数据   不同区域 不同的底色
                },
                    {
                        type: 'scatter',
                        coordinateSystem: 'geo',
                        showEffectOn: 'render',

                        hoverAnimation: true,


                        symbol: 'pin',
                        symbolSize: [24,24],
                        // symbolKeepAspect:true,
                        data: mapDate,
                        tooltip:{

                            formatter (value){
                                console.log(value)
                                return value.data.name
                            },
                            trigger:'item',
                        },
                    }

                ]
            };
            this.myChart.setOption(option);

        }
    }
</script>

<style scoped>
    .main-content{
        background: url("../../assets/module/mainContent.png") no-repeat 50%;
        margin:0.5% 0 0 25%;
        position:relative;
        width:50%;
        height:100%;
        background-size:cover;
    }
    #container{
        width:100%;
        height:80%;
        top: 10%;
        position:absolute;
    }
    .mark-label{
        width:320px;
        height:80px;
        position:absolute;
        right:30px;
        top:10px;
    }
    .label-column{
        padding: 8px 5px 0 15px;
        float: left;
        margin:5px 10px 0 0;
    }
    .label-border{
        width:25%;
        height:40%;
        background:url("../../assets/module/label_border01.png")no-repeat;
        background-size:cover;
    }
    .label-border-longer{
        width:34%;
        height:40%;
        background:url("../../assets/module/label_border02.png")no-repeat;
        background-size:cover;
    }
    .label-icon1{
        background:url("../../assets/module/icon_all.png") no-repeat;
    }
    .label-icon2{
        background:url("../../assets/module/icon_normal.png") no-repeat;
    }
    .label-icon3{
        background:url("../../assets/module/icon_Partiallyoffline.png") no-repeat;
    }
    .label-icon4{
        background:url("../../assets/module/icon_abnormal.png") no-repeat;
    }
    .label-icon5{
        background:url("../../assets/module/icon_exceedstandard.png") no-repeat;
    }
    .label-icon6{
        background:url("../../assets/module/icon_alloffline.png") no-repeat;
    }
    .label-icon{
        height:80%;
        width:25% ;
        float: left;
        margin-right: 5%;
        background-size:cover;
    }
    .label-icon-longer{
        height:80%;
        width:17% ;
        float: left;
        margin-right: 10%;
        background-size:cover;
    }
</style>
