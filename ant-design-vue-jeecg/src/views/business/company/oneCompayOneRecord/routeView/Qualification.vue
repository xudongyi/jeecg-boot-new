<template>
  <div>
    <a-tabs default-active-key="1"  @change="change">
      <a-tab-pane key="1" tab="企业形象">
          <pic-list ref="companyImage" :companyId="companyId" qualificttion-type="companyImage" :isApply="isApply"/>
      </a-tab-pane>
      <a-tab-pane key="2" tab="营业执照照片" >
        <pic-list ref="businessLicense" :companyId="companyId" qualificttion-type="businessLicense" :isApply="isApply" />
      </a-tab-pane>
      <a-tab-pane key="3" tab="企业平面图">
        <pic-list  ref="floorPlan" :companyId="companyId" qualificttion-type="floorPlan" :isApply="isApply" />
      </a-tab-pane>

      <a-tab-pane key="4" tab="生产工艺图">
        <pic-list  ref="produceCrafts" :companyId="companyId" qualificttion-type="produceCrafts" :isApply="isApply" />
      </a-tab-pane>
      <a-tab-pane key="5" tab="治理工艺图">
        <pic-list  ref="controlCrafts" :companyId="companyId" qualificttion-type="controlCrafts" :isApply="isApply" />
      </a-tab-pane>
    </a-tabs>

  </div>
</template>

<script>
    //一企一档 企业资质
    //企业形象照片
    import {loadQualifications} from "../../../requestAction/request"
    import PicList from "../../../component/PicList";
    import {getFileAccessHttpUrl} from '@/api/manage';


    export default {
      name: "Qualification",
      components:{
        PicList
      },
      props:{
        companyId:''
      },
      data() {
        return {
          qualificttionImgs:{},
          isApply:false
        };
      },
      methods:{
        change(val){
          let _this = this;
          this.$nextTick(() => {
            if (val === '2')
              _this.$refs.businessLicense.initImages(_this.qualificttionImgs.businessLicense);
            if (val === '3')
              _this.$refs.floorPlan.initImages(_this.qualificttionImgs.floorPlan);
            if (val === '4')
              _this.$refs.produceCrafts.initImages(_this.qualificttionImgs.produceCrafts);
            if (val === '5')
              _this.$refs.controlCrafts.initImages(_this.qualificttionImgs.controlCrafts);
          });
        },
        loadQualificationImgs(){
          let that = this;
          loadQualifications({companyId:that.companyId}).then((res)=>{
            if(res.success){
              that.qualificttionImgs = res.result;
              console.log(res.result);
              that.$refs.companyImage.initImages(res.result.companyImage);

            }else{
              console.log(res.message);
            }
          });
        }
      },
      mounted() {

        //查询
        this.loadQualificationImgs();

      }
    }
</script>

<style scoped>

</style>