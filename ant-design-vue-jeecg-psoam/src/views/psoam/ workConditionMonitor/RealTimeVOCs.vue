<template>
  <!--flex  布局-->
  <a-card :bordered="false">
    <div class="real-time-water">
      <a-card style="flex-basis:250px">
        <sites-tree :bg-imgs="bgImgs" @selectChange="siteChange" :site-type="'2'"></sites-tree>
      </a-card>
      <div class="right-window">
        <div class="right-title-window">
          <span class="fixed-title">企业名称></span>
          <span class="dyn-title">{{siteInfo.companyName}}>{{siteInfo.siteName}}</span>
          <span class="update-time">更新时间：{{updateTime}}</span>
        </div>
        <div class="process-body">
          <!-- 提示标签 -->
          <div class="cue-mark">
            <div class="mark-pic" style="background: #00D65F;"></div>
            <div class="mark-text">正常</div>
            <div class="mark-pic" style="background: #FF2323;"></div>
            <div class="mark-text">超标</div>
            <div class="mark-pic" style="background: #FF9710;"></div>
            <div class="mark-text">故障</div>
            <div class="mark-pic" style="background: #A1A1A1;"></div>
            <div class="mark-text">离线</div>
            <div class="mark-pic" style="background: #1E1E1E;"></div>
            <div class="mark-text">停运</div>
          </div>
          <div class="details">
            <div class="detail">
              <div style="width: 30%; display: flex;align-items: center;">
                <div class="mark-pic" :style="vocsStyle"></div>
                <div class="mark-text">苯 ：0.754 mg/m³</div>
              </div>
              <div style="width: 30%;display: flex;align-items: center;">
                <div class="mark-pic" :style="vocsStyle"></div>
                <div class="mark-text">非甲烷总烃：15.234 mg/m³</div>
              </div>
              <div style="width: 30%;display: flex;align-items: center;">
                <div class="mark-pic" :style="vocsStyle"></div>
                <div class="mark-text">烟气温度：-</div>
              </div>
            </div>
            <div class="cut-line"></div>
            <div class="detail">
              <div style="width: 30%;display: flex;align-items: center;">
                <div class="mark-pic" :style="vocsStyle"></div>
                <div class="mark-text">甲苯：0.123 mg/m³</div>
              </div>
              <div style="width: 30%;display: flex;align-items: center;">
                <div class="mark-pic" :style="vocsStyle"></div>
                <div class="mark-text">甲烷：-</div>
              </div>
              <div style="width: 30%;display: flex;align-items: center;">
                <div class="mark-pic" :style="vocsStyle"></div>
                <div class="mark-text">烟气动压：12.031 Pa</div>
              </div>
            </div>
            <div class="cut-line"></div>
            <div class="detail">
              <div style="width: 30%;display: flex;align-items: center;">
                <div class="mark-pic" :style="vocsStyle"></div>
                <div class="mark-text">二甲苯：0.354 mg/m³</div>
              </div>
              <div style="width: 30%;display: flex;align-items: center;">
                <div class="mark-pic" :style="vocsStyle"></div>
                <div class="mark-text">烟气流速：4.567 m/s</div>
              </div>
              <div style="width: 30%;display: flex;align-items: center;">

              </div>
            </div>
          </div>
          <div class="process-device" style="top:86%;left:19%;">
            VOCs废气处理装置
          </div>
          <div class="process-device" style="top:44%;left:47%;">
            温度、压力、流量计
          </div>
          <div class="process-device" style="top:62%;left:49%;">
            出口采样探头
          </div>
          <div class="process-device" style="top:86%;left:49%;">
            入口采样探头
          </div>
          <div class="process-device" style="top:68%;left:87%;">
            监测站房
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
    name: "RealTimeVOCs",
    components: {SitesTree},
    data() {
      return {
        vocsStyle: {background: "#00D65F", float: "left"},
        bgImgs: {
          outOfService: require('@/assets/workcondition/icon_water_stop.png'),
          offline: require('@/assets/workcondition/icon_water_offline.png'),
          error: require('@/assets/workcondition/icon_water_malfunction.png'),
          normal: require('@/assets/workcondition/icon_water_normal.png'),
          abnormal: require('@/assets/workcondition/icon_water_exceedstandard.png'),
        },

        updateTime: moment().format("YYYY-MM-DD HH:mm:ss"),
        valveStatus: true,
        siteInfo: {},
        deviceInfos: [1, 2]
      }
    },
    computed: {},
    methods: {
      siteChange(val) {
        console.log(val)
        this.siteInfo = val;
        this.siteName = val.siteName

        // site
      }
    },
  }
</script>

<style scoped>
  .real-time-water {
    display: -webkit-flex; /* Safari */
    display: flex;
  }

  .right-window {
    border: 1px solid #e8e8e8;
    flex-grow: 1;
    margin-left: 10px;
  }

  .right-title-window {
    width: 100%;
    height: 50px;
    border-bottom: 1px solid #e8e8e8;
    align-items: center;
    display: flex;
    padding: 0 10px 0 10px;

  }

  .fixed-title {
    font-size: 16px;
    color: #999999;
    font-weight: 400;

  }

  .dyn-title {
    font-size: 16px;
    color: #189FF0;
    font-weight: 400;
    flex-grow: 1
  }

  .process-body {
    background: url("~@/assets/workcondition/VOCs.gif") no-repeat 50%;
    background-size: cover;
    width: 100%;
    padding-bottom: 66.67%;
    position: relative;
  }
  .process-device {
    font-size: 14px;
    font-weight: 400;
    color: #000000;
    position: absolute;
  }
  .details {
    width: 50%;
    height: 90px;
    background: #FFFFFF;
    box-shadow: 3px 2px 10px 0px rgba(24, 24, 24, 0.32);
    border-radius: 4px;
    margin: 0 auto;
    margin-top: 1%;
  }

  .detail {
    height: 30px;
    display: flex;
    align-items: center;
    justify-content: space-around;
  }
  .cut-line {
    width: 100%;
    height: 1px;
    background: #F3F3F3;
  }
  .cue-mark {
    width: 100%;
    margin-top: 10px;
    display: flex;
    align-items: center;
  }

  .mark-pic {
    width: 18px;
    height: 18px;
    border-radius: 50%;
    margin-left: 15px;
  }
</style>