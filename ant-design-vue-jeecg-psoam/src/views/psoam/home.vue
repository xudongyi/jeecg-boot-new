<template>
  <a-card :bordered="false">
    <div class="home-body">
      <div class="body-top">
        <div class="body-top-left">
          <div class="body-module">
            <company-counts></company-counts>
            <site-status></site-status>
          </div>
        </div>
        <div class="body-top-right body-module">

        </div>
      </div>

      <div  class="body-buttom body-module">
        buttom
      </div>
    </div>
  </a-card>

</template>

<script>
  import companyCounts from "./component/home/companyCounts";
  import siteStatus from "./component/home/siteStatus";
    export default {
        name: "home",
        components:{companyCounts,siteStatus}
    }
</script>

<style scoped>
  .home-body{
    display: flex;
    flex-direction:column;
  }
  .body-top{
    width: 100%;
    display: flex;
  }
  .body-module{
    background: #FFFFFF;
    box-shadow: 0px 2px 6px 0px rgba(0, 0, 0, 0.2);
    border-radius: 8px;
  }
  .body-top-left{
    flex-grow:3;
    margin-right:15px ;
  }
  .body-top-right{
    flex-grow:2
  }
  .body-buttom{
    margin-top: 15px;
  }
</style>