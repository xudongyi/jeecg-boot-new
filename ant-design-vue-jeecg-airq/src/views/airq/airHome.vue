<template>
  <div :style="height">
    <div class="left">
      <div class="airQuality">

      </div>
      <div class="siteState">

      </div>
      <div class="alarm">

      </div>
    </div>
    <div class="right">
      <div class="fineDays">

      </div>
      <div class="calendar">

      </div>
    </div>
  </div>
</template>

<script>
  import {mixinDevice} from '@/utils/mixin'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {getAction} from '@/api/manage'
  import moment from 'moment'

  export default {
    name: "airHome",
    mixins: [mixinDevice],
    components: {},
    data() {
      return {
        height: {}
      }
    },
    created() {
      let containerHeight = document.body.clientHeight || document.documentElement.clientHeight;
      if (this.isMobile()) {
        let height = containerHeight - 52 - 59 - 93 + "px";
        this.height={height:height}
      } else {
        let height = containerHeight - 59 - 93 + "px";
        this.height={height:height}
      }


    }
  }


</script>

<style scoped>
  .left {
    height: 100%;
    width: 60%;
    float: left;
  }

  .right {
    height: 100%;
    width: 40%;
    float: right;
  }

  .left .airQuality {
    height: 40%;
    width: 98%;
    margin-right: 2%;
  }

  .left .siteState {
    height: 18%;
    width: 98%;
    margin-right: 2%;
    margin-top: 1.5%;
  }

  .left .alarm {
    height: 38%;
    width: 98%;
    margin-right: 2%;
    margin-top: 1.5%;
  }
   .fineDays,.calendar{
    height: 49%;
    width: 100%;
  }
  .calendar{
    margin-top: 2.25%;
  }

  .airQuality, .siteState, .alarm, .fineDays, .calendar {
    background: rgba(255, 255, 255, 1);
    box-shadow: 1px 2px 6px 0px rgba(0, 0, 0, 0.2);
    border-radius: 8px;
  }


</style>