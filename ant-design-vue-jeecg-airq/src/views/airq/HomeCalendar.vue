<template>
  <div>
    <div class="titleRow">
      <div class="block"></div>
      <div class="smallTitle">蓝天日历</div>
      <a-month-picker placeholder="Select month" :value="queryParam.queryTime"  @change="monthChange" class="ant-calendar-picker"/>
    </div>
    <div class="border"></div>
    <div id="calendar" style="width:100%;height:500px;z-index:19;"></div>
  </div>

</template>

<script>
  import moment from 'moment'
  import {queryAirHomeCalendar} from "../requestAction/request";

  export default {
      name: "HomeCalendar",
      data(){
        return {
          queryParam:{queryTime:moment().format('YYYY-MM')},
          queryResult:{},
        }
      },
      methods:{
        monthChange(val){
          this.queryParam.queryTime = val;
        },
        drawCalendar(){
          let echarts = require('echarts');
          if(!this.myChart){
            let dom  = document.getElementById("calendar")
            option ={
              renderer: "Canvas",
              height:1500
            }
            //dom.style.height = 100+360*parseInt(this.queryResult.months.length/3 +(this.queryResult.months.length%3>0?1:0) )+'px'
            this.myChart = echarts.init( dom,null,option);
          }
        },
        searchQuery(){
          let that = this;
          queryAirHomeCalendar(this.queryParam).then(res=>{
            if(res.success){
              console.log(res);
              that.queryResult = res.result;
              console.log("!",that.queryResult);
              //that.drawCalendar()
            }else{
              that.$message.error(res.message);
            }
          })
        }
      },
      created(){

      }
    }
</script>

<style scoped>
  .titleRow {
    width: 100%;
    height: 50px;
  }
  .titleRow .block {
    width: 8px;
    height: 18px;
    background: rgba(1, 142, 237, 1);
    float: left;
    margin: 15px 0 0 10px;
  }
  .smallTitle {
    width: 30%;
    height: 18px;
    padding: 12px 0 0 27px;
    font-size: 16px;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: rgba(17, 17, 17, 1);
  }
  .border {
    width:100%;
    height:1px;
    background:rgba(0,0,0,0.09);
  }
  .ant-calendar-picker {
    float: right;
    margin: -8px 10px 0 0;
  }
</style>