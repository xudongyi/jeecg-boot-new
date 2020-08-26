<template>
  <div style="width: 100%;height: 100%">
    <div class="top">
      <span class="mark"></span>
      <span class="caption">辖区站点实时统计</span>
    </div>
    <div style="width: 100%;height: 50%;margin-top: 0.5%">
      <div class="normal">
        <div class="backImg"></div>
        <div class="siteRight"><div class="stateName">正常</div><div class="siteNum">{{siteStates[0]}}</div></div>
      </div>
      <div class="early">
        <div class="backImg"></div>
        <div class="siteRight"><div class="stateName">超标预警</div><div class="siteNum">{{siteStates[1]}}</div></div>
      </div>
      <div class="warn">
        <div class="backImg"></div>
        <div class="siteRight"><div class="stateName">超标报警</div><div class="siteNum">{{siteStates[2]}}</div></div>
      </div>
      <div class="offLine">
        <div class="backImg"></div>
        <div class="siteRight"><div class="stateName">离线</div><div class="siteNum">{{siteStates[3]}}</div></div>
      </div>
      <div class="trouble">
        <div class="backImg"></div>
        <div class="siteRight"><div class="stateName">故障</div><div class="siteNum">{{siteStates[4]}}</div></div>
      </div>
    </div>
  </div>
</template>

<script>
  import {querySiteState} from "../../requestAction/request";
  import moment from "moment";
  export default {
    name: "siteState",
    data() {
      return {
        startTime:moment().subtract(1, "hours").format('YYYY-MM-DD HH'),
        endTime:moment().format('YYYY-MM-DD HH'),
        siteStates:[]
      }
    },
    created() {
      this.selectSiteState();
    },
    methods:{
      selectSiteState(){
        querySiteState({companyIds:this.$store.getters.userInfo.companyIds.join(','),startTime:this.startTime,endTime:this.endTime}).then(res=>{
          this.siteStates = res.result;
        })
      }

    }
  }
</script>

<style scoped>
  .top {
    height: 35px;
    width: 100%;
    display: flex;
    align-items: center;
    text-align: center;
    position: relative;
  }

  .mark {
    width: 8px;
    height: 20px;
    background: rgba(1, 142, 237, 1);
    float: left;
    line-height: 35px;
    margin-left: 1%;
  }

  .caption {
    width: 130px;
    height: 20px;
    font-size: 16px;
    float: left;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: rgba(17, 17, 17, 1);
    line-height: 20px;
    margin-left: 5px
  }

  .normal {
    height: 100%;
    width: 15%;
    float: left;
    background-color: #00D65F;
    box-shadow: 0px 5px 10px 0px rgba(28, 81, 165, 0.15);
    border-radius:10px;
    margin-left: 4%;
    display: flex;
    align-items: center;
  }

  .early {
    height: 100%;
    width: 15%;
    float: left;
    background-color: #FF9710;
    box-shadow: 0px 5px 10px 0px rgba(28, 81, 165, 0.15);
    border-radius:10px;
    margin-left: 4%;
    display: flex;
    align-items: center;
  }

  .warn {
    height: 100%;
    width: 15%;
    float: left;
    background-color: #FF2323;
    box-shadow: 0px 5px 10px 0px rgba(28, 81, 165, 0.15);
    border-radius:10px;
    margin-left: 4%;
    display: flex;
    align-items: center;
  }

  .offLine {
    height: 100%;
    width: 15%;
    float: left;
    background-color: #A1A1A1;
    box-shadow: 0px 5px 10px 0px rgba(28, 81, 165, 0.15);
    border-radius:10px;
    margin-left: 4%;
    display: flex;
    align-items: center;
  }

  .trouble {
    height: 100%;
    width: 15%;
    float: left;
    background-color: #CD66C2;
    box-shadow: 0px 5px 10px 0px rgba(28, 81, 165, 0.15);
    border-radius:10px;
    margin-left: 4%;
    display: flex;
    align-items: center;
  }
  .stateName{
    font-size:14px;
    font-family:Microsoft YaHei;
    font-weight:bold;
    color:rgba(255,255,255,1);
    text-align: right;
  }
  .siteNum{
    font-size:26px;
    font-family:Microsoft YaHei;
    font-weight:bold;
    color:rgba(255,255,255,1);
    text-align: right;
  }
  .backImg{
    width: 20%;
    height: 68%;
    background: url("../../../assets/home/home_alarmbell.png") no-repeat center;
    background-size: cover;
    margin-left: 10%;
  }
  .siteRight{
    width:65%;
    height: 75%;
  }
</style>