<template>
  <!--flex  布局-->
  <a-card :bordered="false" >
    <div class="real-time-water">
      <a-card style="flex-basis:250px">
        <sites-tree :bg-imgs="bgImgs" @selectChange="siteChange" :site-type="'1'"></sites-tree>
      </a-card>
      <div class="right-window">
        <div class="right-title-window" >
          <span class="fixed-title">企业名称></span>
          <span class="dyn-title">{{siteInfo.companyName}}>{{siteInfo.siteName}}</span>
          <span class="update-time">更新时间：{{updateTime}}</span>
        </div>
        <!-- 提示标签 -->
        <div class="cue-mark">
          <div class="mark-pic" style="background: #00D65F;"></div><div class="mark-text">正常</div>
          <div class="mark-pic" style="background: #FF2323;"></div><div class="mark-text">超标</div>
          <div class="mark-pic" style="background: #FF9710;"></div><div class="mark-text">故障</div>
          <div class="mark-pic" style="background: #A1A1A1;"></div><div class="mark-text">离线</div>
          <div class="mark-pic" style="background: #1E1E1E;"></div><div class="mark-text">停运</div>
        </div>
      </div>
    </div>
  </a-card>
</template>

<script>
  import SitesTree from "./component/SitesTree";
  import moment from 'moment'
    export default {
        name: "RealTimeAir",
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
</style>