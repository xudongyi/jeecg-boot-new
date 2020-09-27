<template>
  <div class="company-counts">
   <div class="count-item count-bg-pollution">
     <div class="count-body">
       <span class="count-text ">污染源企业数量</span><br/>
       <span class="count-num ">150</span>
       <span class="count-text ">家</span>
     </div>
   </div>
    <div class="count-item  count-bg-online">
      <div class="count-body">
        <span class="count-text ">在线企业数量</span><br/>
        <span class="count-num ">200</span>
        <span class="count-text ">家</span>
      </div>
    </div>
    <div class="count-item count-bg-realtime">
      <div class="count-body">
        <span class="count-text ">企业实时在线率</span><br/>
        <span class="count-num ">80</span>
        <span class="count-text ">%</span>
      </div>
    </div>
    <div class="count-item count-bg-outService">
      <div class="count-body">
        <span class="count-text ">停运企业数量</span><br/>
        <span class="count-num ">9</span>
        <span class="count-text ">家</span>
      </div>
    </div>
    <div class="count-item count-bg-maintain">
      <div class="count-body">
        <span class="count-text ">维修企业数量</span><br/>
        <span class="count-num ">3</span>
        <span class="count-text ">家</span>
      </div>
    </div>
  </div>
</template>

<script>
    export default {
        name: "companyCounts"
    }
</script>

<style scoped>
  .company-counts{
    display: flex;
    /* 均匀排列每个元素每个元素之间的间隔相等 */
    justify-content: space-evenly;
    padding: 10px 0 10px 0 ;
  }
  .count-item{
    width: 17%;
    padding-bottom: 8%;
    box-shadow: 0px 5px 10px 0px rgba(28, 81, 165, 0.15);
    border-radius: 6px;
    position: relative;
  }
  .count-bg-pollution{
    background: #00D65F;
  }
  .count-bg-online{
    background: #189FF0;
  }
  .count-bg-realtime{
    background: #00CDB7;
  }
  .count-bg-outService{
    background: #FF455B;
  }
  .count-bg-maintain{
    background: #FFB311;
  }
  .count-body{
    position: absolute;
    width: 100%;
    height: 100%;
    text-align: center;
    top:50%;
    margin-top: -25px;
  }
  .count-text{
    width: 100px;
    height: 14px;
    font-size: 14px;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: #FFFFFF;
    line-height: 23px;
  }
  .count-num{
    width: 66px;
    height: 20px;
    font-size: 24px;
    font-family: PingFang SC;
    font-weight: bold;
    color: #FFFFFF;
    line-height: 23px;
  }
</style>