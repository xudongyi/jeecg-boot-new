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
    v-if="visible"
    >
    <base-info ref="baseModal" :companyId="companyId" :ftitle="title"  @OK="modalFormOk"></base-info>

    <template slot="footer">
      <a-button type="primary" @click="handleCancel">关闭</a-button>
      <a-button type="primary" @click="handleOk" v-if="disableSubmit">申报</a-button>

    </template>
  </j-modal>
</template>

<script>
  import BaseInfo from "../../../oneCompayOneRecord/routeView/BaseInfo";


  export default {
    name: "JmodalBaseInfo",
    components:{
      BaseInfo
    },
      data(){
          return{
            companyId:this.$store.getters.userInfo.companyIds[0],
            confirmLoading:true,
            visible: false,
            width:1200,
            title:"申请"
          }

      },
      methods:{
        handleCancel () {
          this.visible = false;
          // this.close()
        },
        handleOk(){
          console.log("OK")
          this.$refs.baseModal.handleOk();

          this.visible = false;
          this.$emit('submitOk');


        },
        modalFormOk(){

        }

      },created() {

    }
  }
</script>

<style scoped>

</style>