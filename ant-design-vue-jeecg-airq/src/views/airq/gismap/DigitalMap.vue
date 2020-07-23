<template >
  <div id="digital">
  <div ref="myMap" class="map"></div>
<!--  右上角的样式  -->
    <div class="pos_fixed">
      <div style="width: 100%">
        <img class="thumbnail" :src="iconurl.excellent"/>
        <img class="thumbnail" :src="iconurl.good "/>
        <img class="thumbnail" :src="iconurl.mild"/>
        <img class="thumbnail" :src="iconurl.moderate"/>
        <img class="thumbnail" :src="iconurl.serious"/>
        <img class="thumbnail" :src="iconurl.error"/>
        <img class="thumbnail" :src="iconurl.offline"/>
      </div>

      <div>
        <span class="description">优等</span>
        <span class="description">良好</span>
        <span class="description">轻度</span>
        <span class="description">中度</span>
        <span class="description">重度</span>
        <span class="description">故障</span>
        <span class="description">离线</span>
      </div>
    </div>
    <div class="detailArea" v-show="detailAble">
      <a-icon type="close" style="font-size: 20px" @click="closeDetail"/>
      <div style="margin: 5px 5px 5px 15px;">
        <span style="font-size: 16px;font-weight: bold;line-height: 1.5;">{{detail.siteName}}</span>
        <span style="margin-left: 100px">实时数据</span>
        <div style="border-top: 1px solid #F9F9F9">
          <div style="margin: 5px 0 0 5px; float:left;">
            <p>站点位置:</p>
            <p>发布时间:</p>
            <p>空气质量AQI:</p>
            <p>首要污染物:</p>
          </div>
          <div style="margin: 5px 0 0 100px; ">
            <p>{{getAreaBycode(detail.area)}}</p>
            <p>{{detail.dataTime}}</p>
            <p ><a-tag color="#f50">
              {{detail.aqi}}
            </a-tag></p>
            <p>111{{detail.meaning}}</p>
          </div>
        </div>
        <div style="border-top: 1px solid #F9F9F9">
          <div style="margin: 5px 0 0 5px; float:left;">
            <p>PM2.5(1h):</p>
            <p>PM10(1h):</p>
            <p>SO<sub>2</sub>:</p>
            <p>O<sub>3</sub>(1h):</p>
            <p>CO:</p>
            <p>湿度:</p>
            <p>风向:</p>
          </div>
          <div style="margin: 5px 0 0 5px; float:left;">
            <p>{{detail.a3400401Avg}}ug/m<sup>3</sup></p>
            <p>{{detail.a3400201Avg}}ug/m<sup>3</sup></p>
            <p>{{detail.a21026Avg}}ug/m<sup>3</sup></p>
            <p>{{detail.a0502401Avg}}ug/m<sup>3</sup></p>
            <p>{{detail.a21005Avg}}ug/m<sup>3</sup></p>
            <p>{{detail.a01002Avg}}mg/m<sup>3</sup></p>
            <p>{{detail.meaning}}</p>
          </div>
          <div style="margin: 5px 0 0 20px; float:left;">
            <p>PM2.5(24h):</p>
            <p>PM10(24h):</p>
            <p>NO<sub>2</sub>:</p>
            <p>O<sub>3</sub>(8h):</p>
            <p>温度:</p>
            <p>风速:</p>
            <p>气压:</p>
          </div>
          <div style="margin: 5px 0 0 5px; float:left;">
            <p>{{detail.a3400424Avg}}ug/m<sup>3</sup></p>
            <p>{{detail.a3400224Avg}}ug/m<sup>3</sup></p>
            <p>{{detail.a21004Avg}}ug/m<sup>3</sup></p>
            <p>{{detail.a0502408Avg}}ug/m<sup>3</sup></p>
            <p>{{detail.a01001Avg}}<sup>o</sup>C</p>
            <p>{{detail.a01007Avg}}m/s</p>
            <p>{{detail.a01006Avg}}kPa</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 左下角的按钮  -->
    <div class="two-button" :style="leftFixed">
      <div class="map-button" :style="mapStyle"  @click="toMap"><span class="button-span">地图</span></div>
      <div class="earth-button" :style="earthStyle"  @click="toEarth"><span class="button-span">地球</span></div>
    </div>


  </div>


</template>

<script>
  import {queryLastAirInfo} from '../../requestAction/request'
  import { mixinDevice } from '@/utils/mixin'
  import {loadAreaDate} from '../component/areaUtil'
  import AreaHandler from "../component/AreaHandler";
  import Vue from "vue";
  export default {
      name: "DigitalMap",
      mixins: [mixinDevice],
      data() {
        return {
          point: {longitude: 120.92787148669515, latitude: 32.00880776879839},//定位到南通
          active: false,
          iconurl: {
            error:require('@/assets/gisicon/gis_icon_error.png'),//故障
            excellent:require('@/assets/gisicon/gis_icon_excellent.png'),//优
            good:require('@/assets/gisicon/gis_icon_good.png'),//良
            mild:require('@/assets/gisicon/gis_icon_mild.png'),//轻度
            moderate:require('@/assets/gisicon/gis_icon_moderate.png'),//中度
            offline:require('@/assets/gisicon/gis_icon_offline.png'),//离线
            serious:require('@/assets/gisicon/gis_icon_serious.png'),//重度
          },
          mapTypes: ['BMAP_SATELLITE_MAP', 'BMAP_NORMAL_MAP'],  //BMAP_SATELLITE_MAP卫星地图,BMAP_NORMAL_MAP 普通街道地图,BMAP_HYBRID_MAP混合地图
          interval: null,
          originalSites:[],
          isEarthMap:false,
          detailAble:false,
          detail:{},
          areaHandler:''
        }
      },
      computed:{
        leftFixed(){
          return {
            left:this.isMobile()?'30px':'230px',
          }
        },
        mapStyle(){
          return {
            backgroundImage:'url(' + require('@/assets/map/map.png') + ')',
            border:!this.isEarthMap?'2px solid rgba(94,187,192,1)':'2px solid',
            'border-radius':'12px',
            width:!this.isEarthMap?'66px':'62px',
            height:!this.isEarthMap?'66px':'62px',
          }
        },
        earthStyle(){
          return {
            backgroundImage:'url(' + require('@/assets/map/earth.png') + ')',
            border:this.isEarthMap?'2px solid rgba(94,187,192,1)':'2px solid',
            'border-radius':'12px',
            width:this.isEarthMap?'66px':'62px',
            height:this.isEarthMap?'66px':'62px',
          }
        }
      },
    created() {
      loadAreaDate();
    },
    mounted() {
        this.mapInit(this.point);
        this.hideBaiduLogo();
        this.siteToMap();

      },
      methods: {
        initArea(){
          this.areaHandler = new AreaHandler()
        },
        getAreaBycode(text){
          if(!text)
            return ''
          //初始化
          if(this.areaHandler==='')
          {
            this.initArea()
          }
          let arr = [];
          this.areaHandler.getAreaBycode(text,arr);
          return arr.join('')
        },
        closeDetail(){
          this.detailAble =false
        },
        mapInit(point) {
          let vue = this;
          vue.map = new BMap.Map(vue.$refs.myMap);
          let marker = new BMap.Point(vue.point.longitude, vue.point.latitude);
          // 设置中心点以及缩放级别
          vue.map.centerAndZoom(marker, this.zoom ? this.zoom : 15);
          // if (this.controlShow) {
          //   vue.map.addControl(new BMap.NavigationControl());
          //   //添加缩放控件
          // }
          //滚轮控制缩放
          vue.map.enableScrollWheelZoom(true);
          //双击缩放控制
          vue.map.enableDoubleClickZoom(false);
          // //将地图显示范围设定在指定区域，地图拖出该区域后会重新弹回。  需要引入js
          // var b = new BMap.Bounds(new BMap.Point(120.02,33.01),new BMap.Point(122.02, 31.01));
          // try {
          //   vue.map.AreaRestriction.setBounds(map, b);
          // } catch (e) {
          //   alert(e);
          // }

          // 事件监听   获取点击点坐标 地图点击事件
          vue.map.addEventListener("click", (e) => {
            var center = vue.map.getCenter();
            if (vue.clickadd) {
              this.addOneOverlay(e.point);
            }
            vue.$emit("mapClick", center);
          });

          // 设置地图风格样式  可自定义
          vue.map.setMapStyleV2({
            styleId: '5e050d02262308dbfabc96e19ddc0482'
          });

          /**  tilesloaded 地图加载完成事件 */
          vue.map.addEventListener("tilesloaded", () => {
            let area = vue.map.getBounds();
            this.$emit("visibleArea", area);
            if (this.vevisibleFalg) {
              this.visibleAreaPonit();
            }
          });
          //对地图级别变化、移动结束后 监听获取可视区域
          vue.map.addEventListener("zoomend", () => {
            let area = vue.map.getBounds();
            let zoomSize = vue.map.getZoom();
            console.log(zoomSize);
            this.$emit("visibleArea", area);
            if (this.vevisibleFalg) {
              this.visibleAreaPonit();
            }
          });
          vue.map.addEventListener("moveend", () => {
            let area = vue.map.getBounds();
            this.$emit("visibleArea", area);
            if (this.vevisibleFalg) {
              this.visibleAreaPonit();
            }
          });


        },

        siteToMap(){
          let vue = this
          //查询 节点
          queryLastAirInfo({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res)=>{
            vue.originalSites =  res.result
            console.log(res)
            res.result.forEach(e=>{

              let pt = new BMap.Point(e.siteLongitude, e.siteLatitude);
              let iconUrl
              if(e.onlineStatus==='0'){
                iconUrl = this.iconurl.offline
              }else if(e.deviceStatus === '0'){
                iconUrl = this.iconurl.error
              }else if(e.level==='1'){
                iconUrl = this.iconurl.excellent
              }else if(e.level==='2'){
                iconUrl = this.iconurl.good
              }else if(e.level==='3'){
                iconUrl = this.iconurl.mild
              }else if(e.level==='4'){
                iconUrl = this.iconurl.moderate
              }else {
                iconUrl = this.iconurl.serious
              }
              let myIcon= new BMap.Icon(iconUrl, new BMap.Size(30,30));
              let label = new BMap.Label(e.siteName,{offset:new BMap.Size(-40,30)});
              let marker2 = new BMap.Marker(pt,{icon:myIcon});  // 创建标注
              marker2.addEventListener("click",()=>{
                vue.detail =  e
                vue.detailAble=true
              });
              label.addEventListener("click",()=>{
                vue.detail =  e
                vue.detailAble=true
              });
              marker2.setLabel(label)
              vue.map.addOverlay(marker2);
            })


          })


        },
        toEarth(){
          this.isEarthMap = true
          this.map.setMapType(BMAP_EARTH_MAP);

        },
        toMap(){
          this.isEarthMap = false
          this.map.setMapType(BMAP_EARTH_MAP);

        },

        //隐藏左下角的百度标识
        hideBaiduLogo(){
          let that = this;
          this.interval = setInterval(function(){
            console.log( document.getElementsByClassName("anchorBL"))
            let dom = document.getElementsByClassName("anchorBL");
            if(dom&&dom.length>1){

              dom[0].style.display = 'none';
              dom[1].style.display = 'none';
              clearInterval(that.interval);
            }
          },10)
        }
      }
    }
</script>




<style scoped>
  .description{
    width: 20px;
    margin:0px 7px 0px 7px;
    color: #FFFFFF;
  }
  .two-button{
    position:absolute;
    top:80%;
    z-index: 19;
  }
  .map-button{
    float:left;

    border-radius:10px;
    cursor:pointer;
  }
  .earth-button{
    float:left;
    border-radius:10px;
    margin-left: 20px;
    cursor:pointer;
  }
  .thumbnail
  {
    float:left;
    margin:8px;
  }
  .pos_fixed
  {
    position:fixed;
    top:130px;
    right:20px;
    z-index: 19;
    width:300px;
    height:59px;
    background:rgba(0,0,0,0.25);
    box-shadow:0px 3px 7px 0px rgba(0, 0, 0, 0.35);
    border-radius:8px 0px 0px 8px;
  }
  .detailArea
  {
    position:fixed;
    top:200px;
    right:20px;
    z-index: 19;
    width:340px;
    height:607px;
    background:rgba(0,0,0,0.25);
    box-shadow:0px 3px 7px 0px rgba(0, 0, 0, 0.35);
    border-radius:8px 0px 0px 8px;
  }
  .map{
    width: 100%;
    height: 750px;
  }
.anchorBL{
  display: none !important;
  width: 0 !important;
  height:0 !important;
}
.button-span{
  color: #FFFFFF;
  text-align:center;
  display:block;
  position: relative;
  top:50%;
  transform:translateY(-50%);
  font-size: 22px;
}


</style>