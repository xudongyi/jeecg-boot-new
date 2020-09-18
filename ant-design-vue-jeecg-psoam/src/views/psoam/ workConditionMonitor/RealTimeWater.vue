<template>
  <!--flex  布局-->
  <a-card :bordered="false" >
    <div class="real-time-water">
      <a-card style="flex-basis:250px">
        <sites-tree :bg-imgs="bgImgs" @selectChange="siteChange"></sites-tree>
      </a-card>
      <div class="right-window">
          <div class="right-title-window" >
            <span class="fixed-title">企业名称></span>
            <span class="dyn-title">{{siteInfo.companyName}}>{{siteInfo.siteName}}</span>
            <span class="update-time">更新时间：{{updateTime}}</span>
          </div>
        <div class="process-body">
          <!-- 提示标签 -->
          <div class="cue-mark">
            <div class="mark-pic" style="background: #00D65F;"></div><div class="mark-text">正常</div>
            <div class="mark-pic" style="background: #FF2323;"></div><div class="mark-text">超标</div>
            <div class="mark-pic" style="background: #FF9710;"></div><div class="mark-text">故障</div>
            <div class="mark-pic" style="background: #A1A1A1;"></div><div class="mark-text">离线</div>
            <div class="mark-pic" style="background: #1E1E1E;"></div><div class="mark-text">停运</div>
          </div>
          <div class="digital-Collector">
            <!--MN2176589-->{{siteInfo.mn}}
          </div>
          <div class="mark-device-min" :style="{background: '#FF9710',top:'15%',left:'37%'}"></div>
          <div class="process-text" style="top:31%;left:6.5%;">
            10.5<!-- PH-->
          </div>
          <div class="mark-device" :style="{background: '#FF9710',top:'36.3%',left:'6.7%'}"></div>
          <div class="process-device" style="top:40%;left:4%;">
            PH
          </div>
          <div class="process-text" style="top:31%;left:24.5%;">
            10.5<!-- 悬浮物-->
          </div>
          <div class="mark-device" :style="{background: '#FF9710',top:'36.6%',left:'24.8%'}"></div>

            <div class="process-device" style="top:40%;left:21%;">
            悬浮物
          </div>
          <div class="process-device" style="top:56%;left:2%;">
            污水入口
          </div>
          <!--自动采样button-->
          <div class="aoto-sampling-btn"></div>
          <div class="process-device" style="top:56%;left:46%;">
            自动采样仪
          </div>
          <div class="process-device" style="top:88%;left:22%;">
            污水处理池
          </div>
          <div class="valve-btn-open valve-btn"  v-show="valveStatus"></div>
          <div class="valve-btn-close valve-btn"  v-show="!valveStatus"></div>
          <div class="valve-close valve"  v-show="valveStatus"></div>
          <div class="valve-open valve"  v-show="!valveStatus"></div>
          <div class="process-device" style=" top:83%;left:61%;">
            阀门
          </div>
          <div class="process-text" style="top:64.8%;left:78%;">
            10.5L/S<!--流量计-->
          </div>
          <div class="mark-device-min" :style="{background: '#FF9710',top:'68.8%',left:'80%'}"></div>

          <div class="process-device" style=" top:83%;left:78.5%;">
            流量计
          </div>
          <div class="process-device" style=" top:83%;left:92%;">
            污水出口
          </div>
          <div v-for="(item,index) in deviceInfos"  class="device-img" :style="{left: 55.65+index*12.2+'%'}">
            <div style=" font-size: 12px;font-weight: 400;color: #FEFD00;margin-top: 72%;margin-left: 46%" >
              10.5L/S
            </div>
          </div>
        </div>
      </div>
    </div>
  </a-card>
</template>

<script>

    import SitesTree from "./component/SitesTree";
    import moment from 'moment'
    export default {
        name: "RealTimeWater",
        components:{SitesTree},
        data(){
          return{

            bgImgs: {
              outOfService: require('@/assets/workcondition/icon_water_stop.png'),
              offline: require('@/assets/workcondition/icon_water_offline.png'),
              error: require('@/assets/workcondition/icon_water_malfunction.png'),
              normal:require('@/assets/workcondition/icon_water_normal.png'),
              abnormal:require('@/assets/workcondition/icon_water_exceedstandard.png'),
            },

            updateTime:moment().format("YYYY-MM-DD HH:mm:ss"),
            valveStatus:true,
            siteInfo:{},
            deviceInfos:[1,2]
          }
        },
      computed: {

      },
      methods:{
        siteChange(val){
          console.log(val)
          this.siteInfo = val;
          this.siteName = val.siteName

          // site
        }
      },

    }
</script>

<style scoped>
  .real-time-water{
    display: -webkit-flex; /* Safari */
    display: flex;
  }
  .right-window{
    border: 1px solid #e8e8e8;
    flex-grow:1;
    margin-left: 10px;
  }
  .right-title-window{
    width:100%;
    height: 50px;
    border-bottom: 1px solid #e8e8e8;
    align-items: center;
    display: flex;
    padding:0 10px 0 10px ;

  }
  .fixed-title{
    font-size:16px ;
    color:#999999;
    font-weight: 400;

  }
  .dyn-title{
    font-size:16px ;
    color: #189FF0;
    font-weight: 400;
    flex-grow:1
  }
  .process-body{
    background: url("~@/assets/workcondition/water01.png") no-repeat 50%;
    background-size:cover;
    width: 100%;
    padding-bottom: 66.67%;
    position:relative;
  }
  .cue-mark{
      width: 100%;
      margin-top: 10px;
      display: flex;
      align-items: center;
  }
  .mark-pic{
    width: 18px;
    height: 18px;
    border-radius: 50%;
    margin-left: 15px;
  }
  .mark-device{
    width: 16px;
    height: 16px;
    border-radius: 50%;
    position: absolute;
  }
  .mark-device-min{
    width: 12px;
    height: 12px;
    border-radius: 50%;
    position: absolute;
  }
  .mark-text{
    font-weight: 400;
    color: #000000;
    font-size: 14px;
    margin-left: 5px;
  }
  .digital-Collector{
      position: absolute;
      top:11.5%;
      left:34.5%;
      font-size: 12px;
      font-weight: 400;
      color: #FEFD00;
  }
  .process-text{
    font-size: 12px;
    font-weight: 400;
    /*color: #FFFFFF;*/
    color: #FEFD00;
    position: absolute;
  }
  .process-device{
    font-size: 14px;
    font-weight: 400;
    color: #000000;
    position: absolute;
  }
  .aoto-sampling-btn{
    background: url("~@/assets/workcondition/btn.png") no-repeat 50%;
    background-size:cover;
    width: 8%;
    height: 3.5%;
    position: absolute;
    top:23.5%;
    left:44%;
  }
  .valve-close{
    width: 8.03%;
    height: 11.62%;
    background:url(~@/assets/workcondition/valve_close.png) no-repeat 50%;
    background-size:cover;
    position: absolute;
    top:71%;
    left:58.4%;
  }
  .valve{
    width: 8.03%;
    height: 11.62%;
    position: absolute;
    top:71%;
    left:58.4%;
  }
  .valve-open{
    background:url(~@/assets/workcondition/valve_open.png) no-repeat 50%;
    background-size:cover;
  }
  .valve-close{
    background:url(~@/assets/workcondition/valve_close.png) no-repeat 50%;
    background-size:cover;
  }
  .valve-btn{
    width: 8%;
    height: 4.5%;
    position: absolute;
    top:66%;
    left:58.4%;
  }
  .valve-btn-open{
    background:url(~@/assets/workcondition/open_btn.png) no-repeat 50%;
    background-size:cover;
  }
  .valve-btn-close{
    background:url(~@/assets/workcondition/close_btn.png) no-repeat 50%;
    background-size:cover;
  }
  .device-img{
    background:url(~@/assets/workcondition/water02.png) no-repeat 50%;
    width: 12.16%;
    height: 44.55%;
    background-size:cover;
    position: absolute;
    top:19.15%;

  }
</style>