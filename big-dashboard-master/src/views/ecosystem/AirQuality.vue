<template>
    <div class="air-quality">
        <div class="subtitle-gradient sources-title">
            <span :style="titleLineHeight">空气质量</span>
            <span style="margin-left: 20%;" :style="titleLineHeight">{{currDate}}</span>
        </div>
        <div class="gauge-detail">
            <div id="airDial" class="air-dial"></div>
            <div class="air-level" :style="airfont">{{airlevel}}</div>
            <div class="first-code" :style="airfont">首要污染物{{firstCode}}</div>
        </div>
        <div class="air_bar">

            <p class="detal-line"><span class="detal-left" :style="airfont">NO2</span>     <span class="detal-right" :style="airfont">{{concentration}}μg/m³</span></p>
            <p class="detal-line"><span class="detal-left" :style="airfont">PM10</span>    <span class="detal-right" :style="airfont">{{concentration}}μg/m³</span></p>
            <p class="detal-line"><span class="detal-left" :style="airfont">PM2.5</span>   <span class="detal-right" :style="airfont">{{concentration}}μg/m³</span></p>
            <p class="detal-line"><span class="detal-left" :style="airfont">O3</span>      <span class="detal-right" :style="airfont">{{concentration}}μg/m³</span></p>
            <p class="detal-line"><span class="detal-left" :style="airfont">SO2</span>     <span class="detal-right" :style="airfont">{{concentration}}μg/m³</span></p>
            <p class="detal-line"><span class="detal-left" :style="airfont">CO</span>      <span class="detal-right" :style="airfont">{{concentration}}μg/m³</span></p>

        </div>
        <div class="air-data-details">
            <div class = "air-data-column">
                <span class="icon font_family" :style="iconFontSize">&#xe60f;</span>
                <div style="margin-left: 5%;float: left;">
                    <p class="normal-font" :style="airfontTitle">温度</p>
                    <p class=" normal-font" :style="airfontTitle">{{temperature}}℃</p>
                </div>
            </div>
            <div class = "air-data-column">
                <span class="icon font_family" :style="iconFontSize">&#xe60e;</span>
                <div style="margin-left: 5%;float: left;">
                    <p class="normal-font" :style="airfontTitle">湿度</p>
                    <p class=" normal-font" :style="airfontTitle">{{humidity}}%</p>
                </div>
            </div>
            <div class = "air-data-column">
                <span class="icon font_family" :style="iconFontSize">&#xe60b;</span>
                <div style="margin-left: 5%;float: left;">
                    <p class=" normal-font" :style="airfontTitle">风速</p>
                    <p class=" normal-font" :style="airfontTitle">{{windSpeed}}m/s</p>
                </div>
            </div>
            <div class = "air-data-column2">
                <span class="icon font_family" :style="iconFontSize">&#xe60f;</span>
                <!--        空格        -->
                <span class="air-info2 normal-font" :style="airfontTitle2">风向&#12288;{{windDirection}}</span>
            </div>
            <div class = "air-data-column2">
                <span class="icon font_family" :style="iconFontSize">&#xe60f;</span>
                <!--        空格        -->
                <span class="air-info2 normal-font" :style="airfontTitle2">气压{{airPressure}}KPa</span>
            </div>
        </div>
    </div>
</template>

<script>
    import moment from 'moment'
    import '../../components/icon/iconfont.css'

    export default {
        name: "AirQuality",
        data(){
            return {
                currDate:moment().hours(moment().hours()-1).minutes(0).format('YYYY-MM-DD HH:mm'),
                aqi:78,
                firstCode:'PM2.5',
                temperature:34,
                humidity:76,
                windSpeed:20,
                windDirection:'东南',
                airPressure:1100,
                concentration:125,
                option : {
                    // backgroundColor: "#062a44",
                    series: [
                        {
                            type: 'gauge',
                            radius: '100%',
                            center: ['50%', '50%'],
                            startAngle: 220,
                            endAngle: -40,
                            min:0,
                            max:500,
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
                                    width: 18*this.$store.getters.scale
                                }
                            },
                            //分隔线样式。
                            splitLine: {
                                show: false,
                            },

                            //仪表盘轴线
                            axisLabel: {
                                show: true,
                                color:'#ffffff',
                                distance:-10/this.$store.getters.scale,
                                fontSize:12*this.$store.getters.scale,
                                formatter: function(v) {
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
                                offsetCenter: [0, 15*this.$store.getters.scale],
                                color: '#ddd',
                                formatter: function(params) {
                                    return params
                                },
                                textStyle: {
                                    color: '#08C7F1',
                                    fontSize: 40*this.$store.getters.scale,
                                }
                            },
                            title: {
                                offsetCenter: [0, 60*this.$store.getters.scale],
                                color: '#08C7F1',
                                fontSize: 20*this.$store.getters.scale,
                                fontWeight: 500,

                            },

                            data: [{
                                value: 78,
                                name:    'AQI'
                            }]
                        }
                    ]
                },
                // barOption:{
                //     color: ['#3398DB'],
                //     tooltip: {
                //         trigger: 'axis',
                //         axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                //             type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                //         }
                //     },
                //     grid: {
                //         // left: '3%',
                //         // right: '4%',
                //         // bottom: '3%',
                //         containLabel: true,
                //         height:200
                //     },
                //     xAxis: [
                //         {
                //             type: 'category',
                //             data: ['SO2', 'NO2', 'PM10', 'CO', 'PM2.5', 'O3'],
                //             axisTick: {
                //                 alignWithLabel: true
                //             },
                //             axisLabel: { color: '#2AA7D3',fontSize:12,interval:0, rotate:40},
                //         }
                //     ],
                //     yAxis: [
                //         {
                //             type: 'value',
                //             name:'单位：μg/m³',
                //             axisLabel: { color: '#2AA7D3',fontSize:14},
                //             nameTextStyle: { color: '#2AA7D3',fontSize:14},
                //
                //
                //         }
                //     ],
                //     series: [
                //         {
                //             name: '直接访问',
                //             type: 'bar',
                //             barWidth: '40%',
                //             data: [10, 52, 200, 334, 390, 330]
                //         }
                //     ]
                // }


        }
        },
        computed:{
            airlevel(){
                if(this.aqi<=50)
                    return '优'
                if(this.aqi<=100)
                    return '良'
                if(this.aqi<=150)
                    return '轻度污染'
                if(this.aqi<=200)
                    return '中度污染'
                return '重度污染'
            },
            airfont(){
                return {
                    'line-height': this.$original_height * this.$store.getters.scale / 25 + 'px',
                    'font-size': this.$original_height * this.$store.getters.scale / 50 + 'px',
                }
            },
            airfontTitle(){
                return {
                    'font-size': this.$original_height * this.$store.getters.scale / 55 + 'px',
                }
            },
            airfontTitle2(){
                return {
                    'line-height': this.$original_height * this.$store.getters.scale / 19 + 'px',
                    'font-size': this.$original_height * this.$store.getters.scale / 50 + 'px',
                }
            },
            titleLineHeight() {
                if( this.airDial){
                    this.airDial.resize()
                    //样式
                    this.option.series[0].axisLine.lineStyle.width=18*this.$store.getters.scale;//宽度
                    this.option.series[0].axisLabel.fontSize=12*this.$store.getters.scale;//刻度
                    this.option.series[0].axisLabel.distance=-10/this.$store.getters.scale;//刻度与表盘距离
                    this.option.series[0].detail.textStyle.fontSize = 40*this.$store.getters.scale;//aqi的值
                    this.option.series[0].detail.offsetCenter[1] = 15*this.$store.getters.scale;//aqi的偏移
                    this.option.series[0].title.fontSize = 20*this.$store.getters.scale;//aqi的值
                    this.option.series[0].title.offsetCenter[1] = 60*this.$store.getters.scale;//aqi的偏移
                    this.drawAirDial()
                }

                return {
                    'line-height': this.$original_height * this.$store.getters.scale / 22 + 'px',
                    'font-size': this.$original_height * this.$store.getters.scale / 45 + 'px',
                }
            },
            iconFontSize(){
                return{
                    'font-size':this.$original_height*this.$store.getters.scale/25+'px',
                }
            },
        },
        methods:{
            drawAirDial(){
                this.option.series[0].data.value = this.aqi
                this.airDial.setOption(this.option, true);
            },
            // drawBar(){
            //     // this.option.series[0].data.value = this.aqi
            //     this.airBar.setOption(this.barOption, true);
            // }
        },
        created() {
            // console.log(moment().hours(moment().hours()-1).minutes(0).hours())
        },
        mounted() {
            this.airDial = this.$echarts.init( document.getElementById("airDial"));
            // this.airBar = this.$echarts.init(document.getElementById("air_bar"));

            this.drawAirDial()
            // this.drawBar()

        }
    }
</script>

<style scoped>
    .detal-line{
        margin-bottom: 1px;
    }
    .detal-left{
        color: #08C7F1;
        margin-left: 10%;
    }
    .detal-right{
        font-size: 16px;
        color: #08C7F1;
        float: right;
        margin-right: 10%;
    }
    .air-quality{
        height: 50%;
    }
    .air-level{
        margin-left: 18%;
        width:60%;
        height:15%;
        background:rgba(248,181,81,1);
        border-radius:4px;
        font-weight:400;
        color:rgba(255,255,255,1);
        text-align:center;
    }
    .first-code{
        width:100%;
        text-align:center;
        height:22px;
        font-size:22px;
        font-weight:400;
        color:rgba(8,199,241,1);
    }
    .air-dial{
        margin-top:10px;
        width: 100%;
        height: 60%;
    }
    .gauge-detail{
        float: left;
        height:60%;
        width: 50%;
    }
    .air_bar{
        float: right;
        width: 50%;
        height:60%;
    }
    .air-data-details{
        width: 100%;
        height: 30%;
        margin-top: 60%;
    }
    .air-data-column{
        background: url("../../assets/module/airbg01.png")no-repeat;
        background-size:cover;
        width: 32%;
        height: 40%;
        float: left;
        margin-left: 1%;
    }
    .air-data-column2{
        background: url("../../assets/module/airbg02.png")no-repeat;
        background-size:cover;
        width:48.5%;
        height: 40%;
        float: left;
        margin-left: 1%;
        margin-top: 2%;
    }
    .icon{
        color: #038fec;
        margin-left: 15%;
        opacity:1;
        float: left;
    }
    .normal-font{
       color: #08C7F1;
        margin-bottom: 0;
        margin-top: 0;
    }

    .air-info2{
        float: left;
        margin-left: 3%;
    }
</style>
