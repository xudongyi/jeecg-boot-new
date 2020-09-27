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
    <site-monitor-point-modal ref="siteModal" :ftitle="title" @close="close" @ok="submitOk" isjmodal="true" :isDisable="isDisable" :addButton="addButton" :companyId="companyId"></site-monitor-point-modal>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel" >关闭</a-button>
      <a-button type="primary" @click="handleOk" v-if="!isDisable">保存</a-button>
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
            companyId:"",
            addButton:false,
            width:1200,
            title:"新增",
            siteType:'',
            isDisable:this.disable
          }

      },
      methods:{
        addClick(disable,addButton){
          this.$nextTick(() => {
            this.$refs.siteModal.add();
            this.$refs.siteModal.companyId = this.companyId;
            this.isDisable=disable;
            this.$data.addButton = addButton;
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
    }
  }
</script>

<style scoped>

</style>