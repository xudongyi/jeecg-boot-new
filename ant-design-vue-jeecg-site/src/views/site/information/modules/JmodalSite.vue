<template>
  <j-modal
    ref="modal"
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @cancel="handleCancel"
    cancelText="关闭"
    >
    <site-monitor-point-modal ref="siteModal" :ftitle="title" :siteType="this.$data.siteType" @close="close" @ok="submitOk" isjmodal="true" :disable="disable"></site-monitor-point-modal>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel" >关闭</a-button>
      <a-button type="primary" @click="handleOk" v-if="!disable">保存</a-button>
    </template>
  </j-modal>
</template>

<script>
  import SiteMonitorPointModal from "./SiteMonitorPointModal";
  import pick from "lodash.pick";
  export default {
    name: "JmodalBaseInfo",
    components:{
      SiteMonitorPointModal
    },
      data(){
          return{
            confirmLoading:true,
            visible: false,
            width:1200,
            title:"新增",
            siteType:''
          }

      },
      methods:{
        addClick(siteType){
          this.$nextTick(() => {
            this.$data.siteType=siteType
            this.$refs.siteModal.add();
          });
        },
        viewClick(record){
          this.$nextTick(() => {
            this.$data.siteType=record.siteType;
            this.$refs.siteModal.edit(record);
          });
        },
        handleOk(){
          this.$refs.siteModal.handleOk();
        },
        submitOk(){
          this.$emit('ok');
        },
        handleCancel() {
          this.$refs.siteModal.handleCancel();
        },
        close() {
          this.visible = false;
        },
      },created() {

    },props:{
      disable:false
    }
  }
</script>

<style scoped>

</style>