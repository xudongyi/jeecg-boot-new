<template>
  <div>
    <site-monitor-point-list v-if="!isDetail" @detail="handleDetail" :siteType="this.siteType"></site-monitor-point-list>
    <site-detail v-if="isDetail" :id="id"></site-detail>
  </div>
</template>

<script>
  import SiteMonitorPointList from "./SiteMonitorPointList";
  import SiteDetail from "./SiteDetail";
  import BusinessMenu from "../../business/component/BusinessMenu";

  export default {
    name: "Site",
    components: {
      BusinessMenu,
      SiteMonitorPointList,
      SiteDetail
    },
    data() {
      return {
        siteType: this.$route.params.siteType,
        isDetail:false,
        id:''
      }
    },
    methods:{
      handleDetail(id){
        this.isDetail=true;
        this.id=id;
      }
    }
  }
</script>

<style scoped>

</style>