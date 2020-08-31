<template>
  <div id="digital">
    <div ref="myMap" class="map"></div>
    <!-- 左下角的按钮  -->
    <div class="two-button" :style="leftFixed">
      <div class="map-button" :style="mapStyle"  @click="toMap"><span class="button-span">地图</span></div>
      <div class="earth-button" :style="earthStyle"  @click="toEarth"><span class="button-span">地球</span></div>
    </div>
  </div>
</template>

<script>
  import { mixinDevice } from '@/utils/mixin';
  import {mapMixin} from "../mixin/mapMixin";
  import {queryLastAirInfo} from "../../../../../ant-design-vue-jeecg-airq/src/views/requestAction/request";

  export default {
    name: "gisMap",
    mixins: [mixinDevice,mapMixin],

    data(){
          return {
            point: {longitude: 120.92787148669515, latitude: 32.00880776879839},//定位到南通
            cityName:'南通'//画的边框

          }

      },
      computed:{


      },
      methods: {

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

      },
      mounted() {
        this.mapInit();
        console.log('hideBaiduLogo')
        this.hideBaiduLogo();
        this.getBoundary();
      }


    }
</script>

<style scoped>
  .map{
    width: 100%;
    height: 750px;
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
  .button-span{
    color: #FFFFFF;
    text-align:center;
    display:block;
    position: relative;
    top:50%;
    transform:translateY(-50%);
    font-size: 22px;
  }
  .anchorBL{
    display: none !important;
    width: 0 !important;
    height:0 !important;
  }
</style>