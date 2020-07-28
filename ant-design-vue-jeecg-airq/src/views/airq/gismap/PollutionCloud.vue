<template>
  <div id="pollution_cloud">
    <div ref="heatmap" id="heatmap"></div>

    <div class="time_area" style="font-size: 14px;color: #FFFFFF">
      {{playerTime}}

    </div>

    <div class="play_area" :style="leftFixed">
      <div style="margin: 5px 0 0 5px; float:left;font-size: 14px">
        <p style="margin-top:5px;">播放速递：</p>
        <p>选择类型：</p>
        <p style="margin-top:55px;">时间跨度：</p>
      </div>
      <div style="margin: 5px 15px 0 75px; ">
        <a-slider  :max="10" :min="0" :default-value="1"  @afterChange="changeSpeed"/>

        <a-radio-group :options="plainOptions" v-model="queryParam.pollution" style="margin-top:5px;" @change="pollutionChange"/>
        <div style="margin-top:15px;">
          <a-date-picker placeholder="请选择数据时间"
                         @change="handleDateChange"  style="width: 116px"
                         format="YYYY-MM-DD"  :value="queryParam.datatime" valueFormat="YYYY-MM-DD" />
          <span class="query-group-split-cust"></span>
          <a-date-picker placeholder="请选择数据时间" :value="queryParam.datatime2"
                         @change="handleDateChange2"  style="width: 116px"
                         format="YYYY-MM-DD"  valueFormat="YYYY-MM-DD"  />
        </div>
      </div>
      <div style="width: 100%;height:50px;margin-top: 20px;float:left;">
            <a-button ghost  style="width: 100%;height:50px;float:left;"  v-show="playStatus==='ready'" @click="play" >播放</a-button>
            <a-button ghost style="width: 50%;height:50px; float:left;" v-show="playStatus==='playing'" @click="pause">暂停</a-button>
        <a-button ghost style="width: 50%;height:50px; float:left;" v-show="playStatus==='pause'" @click="goOn">继续播放</a-button>
            <a-button ghost style="width: 50%;height:50px; float:left;" v-show="playStatus!=='ready'" @click="end">结束</a-button>
      </div>
    </div>
    <div class="level_area">
      <div>
        <div style="width:66px;height:20px;float:left;text-align:center;"><span>优</span></div>
        <div style="width:66px;height:20px;float:left;text-align:center;"><span>良</span></div>
        <div style="width:66px;height:20px;float:left;text-align:center;"> <span>轻度污染</span></div>
        <div style="width:66px;height:20px;float:left;text-align:center;"> <span>中度污染</span></div>
        <div style="width:66px;height:20px;float:left;text-align:center;"><span>中度污染</span></div>
        <div style="width:66px;height:20px;float:left;text-align:center;"><span>严重污染</span></div>
      </div>
      <div style="width:66px;height:20px;float:left;background-color: rgba(0,228,0,1);"></div>
      <div style="width:66px;height:20px;float:left;background-color: rgba(250,241,0,1);"></div>
      <div style="width:66px;height:20px;float:left;background-color: rgba(255,126,0,1);"></div>
      <div style="width:66px;height:20px;float:left;background-color:rgba(255,0,0,1);"></div>
      <div style="width:66px;height:20px;float:left;background-color:rgba(153,0,76,1);"></div>
      <div style="width:66px;height:20px;float:left;background-color:rgba(126,0,35,1);"></div>


    </div>
  </div>
<!--  右下角-->
</template>

<script>
  import { mixinDevice } from '@/utils/mixin'
  import moment from "moment";
  import {getAction} from '@/api/manage'
  export default {
      name: "PollutionCloud",
      mixins: [mixinDevice],
      data(){
          return {
            point: {longitude: 120.92787148669515, latitude: 32.00880776879839},//定位到南通
            ponits:[],
            plainOptions : [
              { label: 'AQI', value: 'aqi' },
              { label: 'SO2', value: 'a21026Iaqi' },
              { label: 'NO2', value: 'a21004Iaqi' },
              { label: 'PM10', value: 'a3400201Iaqi' },
              { label: 'PM2.5', value: 'a3400401Iaqi' },
              { label: 'CO', value: 'a21005Iaqi' },
              { label: 'O3', value: 'a0502401Iaqi' },

            ],
            queryParam:{
              pollution:'aqi'
            },
            playSpeed:1000,
            playStatus:"ready",
            playerTime:"",
            dataSource:[]
          }
        },
        created() {
          this.localReset()
        },
        mounted() {
          if( this.mapvLayer)
            this.mapvLayer.destroy();
          this.mapInit(this.point);
          this.hideBaiduLogo();
          this.heatmapPoint(this.initHeatMap);
          // this.openHeatmap();
        },
        computed: {
          leftFixed() {
            return {
              left: this.isMobile() ? '30px' : '230px',
            }
          },
        },
        methods:{
          pollutionChange(){
            this.initHeatMap()
          },
          pause(){
            this.playStatus='pause'

          },
          end(){
            this.playStatus='ready'
            this.initHeatMap()
          },
          play(){
            this.playStatus='playing'
            if(this.dataSource!=={})
              this.playMap(Object.keys(this.dataSource)[0])
            else{
              this.$message.error("请选择合适的时间节点")
            }

          },
          goOn(){
            this.playStatus='playing'
            this.playMap()
          },
          playMap(orginalKey){
            console.log(this.playerTime,moment().format('HH:mm:ss'))
            if(this.playStatus==='playing'){
              if(orginalKey){
                this.setData(orginalKey)
                setTimeout(this.playMap,this.playSpeed);
              }else{

                let date = moment(this.playerTime,'YYYY年MM月DD日HH时')
                let dateStr = date.hours(date.hours()+1).format('YYYY年MM月DD日HH时')
                if(this.dataSource[dateStr]){
                  this.setData(dateStr)
                  setTimeout(this.playMap,this.playSpeed);
                }else{
                  //播放结束
                  this.playStatus='ready'
                }
              }

            }
          },
          localReset(){

            this.queryParam={
              datatime:moment().format('YYYY-MM-DD'),
              datatime2:moment().format('YYYY-MM-DD'),
              pollution:'aqi',
              companyIds:this.$store.getters.userInfo.companyIds.join(','),
            }
            this.playerTime =moment().hours(moment().hours()-1).format('YYYY年MM月DD日HH时')
          },
          handleDateChange(mom,dateStr){
            console.log(mom,dateStr)
            this.queryParam.datatime=dateStr
            this.heatmapPoint(this.initHeatMap);
          },
          handleDateChange2(mom,dateStr){
            this.queryParam.datatime2=dateStr
            this.heatmapPoint(this.initHeatMap);
          },
          mapInit(point){
            // 百度地图API功能
            let vue = this;
            vue.map = new BMap.Map("heatmap", {
              enableMapClick: false
            });    // 创建Map实例
            let marker = new BMap.Point(vue.point.longitude, vue.point.latitude);
            // 设置中心点以及缩放级别
            vue.map.centerAndZoom(marker, this.zoom ? this.zoom : 15);
            vue.map.enableScrollWheelZoom(true); // 开启鼠标滚轮缩放
            vue.map.setMapStyle({
              style: 'midnight'
            });
            vue.dataSet = new mapv.DataSet();
            var options = {
              size: 13,
              gradient: {
                0.25: "rgb(0,0,255)",
                0.55: "rgb(0,255,0)",
                0.85: "yellow",
                1.0: "rgb(255,0,0)"
              },
              max: 100,
              // range: [0, 100], // 过滤显示数据范围
              // minOpacity: 0.5, // 热力图透明度
              // maxOpacity: 1,
              draw: 'heatmap'
            }
            vue.mapvLayer = new mapv.baiduMapLayer(vue.map, vue.dataSet, options);

          },
          changeSpeed(val){
            this.playSpeed = 1000-val*100
            console.log( this.playSpeed)
          },
          //判断浏览区是否支持canvas
          isSupportCanvas(){
            let elem = document.createElement('canvas');
            return !!(elem.getContext && elem.getContext('2d'));
          },
          //热力点
          heatmapPoint(callback){
            let _this = this
            //热力点数据查询
            getAction('/hour/airqHour/queryPollutionCloud',this.queryParam).then((res)=>{

              if(res.success){
                _this.dataSource = res.result
                for(var i in res.result)
                  console.log(i)
                if(callback)
                  callback()
              }else{
                _this.$message.error('热力点数据查询失败')
              }
            })


          },
          initHeatMap(){
            let last = Object.keys(this.dataSource)
            this.setData(last[last.length-1])
          },
          //数据赋值
          setData(key){
            let orginalData = this.dataSource[key]
            this.playerTime = key
            let data = []
            if(orginalData){
              orginalData.forEach(e=>{
                data.push({
                  geometry: {
                    type: 'Point',
                    coordinates: [e.siteLongitude, e.siteLatitude]
                  },
                  count: e[this.queryParam.pollution]*30
                })
              })
            }
            this.dataSet.set(data)
          },
          //隐藏左下角的百度标识
          hideBaiduLogo(){
            let that = this;
            this.interval = setInterval(function(){
              let dom = document.getElementsByClassName("anchorBL");
              if(dom&&dom.length>1){

                dom[0].style.display = 'none';
                dom[1].style.display = 'none';
                clearInterval(that.interval);
              }
            },10)
          },

        },
        destroyed() {
          if( this.mapvLayer)
            this.mapvLayer.destroy();
        }
  }









</script>

<style>
  #heatmap{
    width: 100%;
    height: 750px;
    background:rgba(0,0,0,0.25);
    box-shadow:0px 3px 7px 0px rgba(0, 0, 0, 0.35);
    opacity:1;
    border-radius:8px;
    color: #FFFFFF;
  }
  .time_area{
    position:fixed;
    top:140px;
    right:20px;
  }
  .level_area{
    position:fixed;
    top:83%;
    right: 20px;
    width:396px;
    height:45px;
    background:rgba(0,0,0,0.25);
    box-shadow:0px 3px 7px 0px rgba(0, 0, 0, 0.35);
    opacity:1;
    border-radius:8px;
    color: #FFFFFF;
  }
  .play_area{
    position:fixed;
    top:65%;
    width:342px;
    height:225px;
    background:rgba(0,0,0,0.25);
    box-shadow:0px 3px 7px 0px rgba(0, 0, 0, 0.35);
    opacity:1;
    border-radius:8px;
    color: #FFFFFF;
  }
  .ant-radio-wrapper{
    width: 70px ;
    color: #FFFFFF;
  }
  .date-group-cust{width:116px}
</style>