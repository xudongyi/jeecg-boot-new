<template>
  <div>
    <site-monitor-point-list v-if="!isDetail" @detail="handleDetail" :siteType="siteType"></site-monitor-point-list>
    <site-detail ref="site" v-if="isDetail" :id="id"></site-detail>
  </div>
</template>

<script>
  import SiteMonitorPointList from "./SiteMonitorPointList";
  import SiteDetail from "./SiteDetail";
  import BusinessMenu from "../component/BusinessMenu";

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
        id:""
      }
    },
    methods:{
      handleDetail(record){

        this.isDetail=true;
        this.id=record.id;
        this.$nextTick(() => {
           this.$refs.site.editMonitor(record);
        })
      }
    }
  }
</script>

<style scoped>

</style>