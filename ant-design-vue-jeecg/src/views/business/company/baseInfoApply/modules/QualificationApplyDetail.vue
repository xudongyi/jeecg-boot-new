<template>
<div>
  <a-button   @click="toApply" type="primary" icon="form">申报</a-button>
  <h3 :style="{ marginBottom: '16px' }">
    企业形象
  </h3>
  <j-upload ref="companyImage" fileType="image" bizPath="qualication" @change="fileListChange"
            @delete ="fileDelete"></j-upload>
  <h3 :style="{ marginBottom: '16px' }">
    营业执照照片
  </h3>
  <j-upload ref="businessLicense"  fileType="image" bizPath="qualication" @change="fileListChange"
            @delete ="fileDelete"></j-upload>
  <h3 :style="{ marginBottom: '16px' }">
    企业平面图
  </h3>
  <j-upload ref="floorPlan"  fileType="image" bizPath="qualication" @change="fileListChange"
            @delete ="fileDelete"></j-upload>
  <h3 :style="{ marginBottom: '16px' }">
    生产工艺图
  </h3>
  <j-upload ref="produceCrafts" fileType="image" bizPath="qualication" @change="fileListChange"
            @delete ="fileDelete"></j-upload>
  <h3 :style="{ marginBottom: '16px' }">
    生产工艺图
  </h3>
  <j-upload ref="controlCrafts"  fileType="image" bizPath="qualication" @change="fileListChange"
            @delete ="fileDelete"></j-upload>
</div>
</template>

<script>
  import JUpload from "@/components/jeecg/JUpload";
  import {loadQualifications} from "../../../requestAction/request";
    export default {
        name: "QualificationApplyDetail",
      components:{
        JUpload
      },
      props:{
        companyId:'',
      },
      date(){
        return{
          disable:false
        }
      },
      methods:{
        fileDelete(){},
        fileListChange(){

        },
        setDate(result,ref){
          let _this = this;
          let images= [];
            if (result[ref]) {
              for (let a = 0; a < result[ref].length; a++) {
                let url = result[ref][a].url;
                let filePath = url;
                let fileName = url.substring(url.lastIndexOf('/'), url.lastIndexOf('_')) + url.substring(url.lastIndexOf('.'));
                images[a] = {filePath:filePath,fileName:fileName};
              }
            }
            this.$nextTick(() => {

              _this.$refs[ref].initFileListArr(images);
            });

        },
        loadQualificationImgs(){
          let _this = this;
          loadQualifications({companyId:_this.companyId}).then((res)=>{

            if(res.success) {
               _this.setDate(res.result,'companyImage');
              _this.setDate(res.result,'businessLicense');
              _this.setDate(res.result,'floorPlan');
              _this.setDate(res.result,'produceCrafts');
              _this.setDate(res.result,'controlCrafts');
            } else{
              console.log(res.message);
            }
          });
        },

      },
      created() {
        this.loadQualificationImgs();
      }

    }
</script>

<style scoped>

</style>