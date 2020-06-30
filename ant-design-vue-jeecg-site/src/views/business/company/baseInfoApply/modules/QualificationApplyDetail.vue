<template>
<div>
  <div style="margin-bottom:20px;">
  <a-button   @click="toApply" type="primary" icon="form" :loading="loading">申报</a-button>
  </div>

  <h3 :style="{ marginBottom: '16px' }">
    企业形象
  </h3>
  <j-upload ref="companyImage" fileType="image" bizPath="qualication" @add="companyImageAdd"
            @delete ="companyImageDelete"  @previewImage="preview"></j-upload>
  <h3 :style="{ marginBottom: '16px' }">
    营业执照照片
  </h3>
  <j-upload ref="businessLicense"  fileType="image" bizPath="qualication" @add="businessLicenseAdd"
            @delete ="businessLicenseDelete" @previewImage="preview"></j-upload>
  <h3 :style="{ marginBottom: '16px' }">
    企业平面图
  </h3>
  <j-upload ref="floorPlan"  fileType="image" bizPath="qualication" @add="floorPlanAdd"  @previewImage="preview"
            @delete ="floorPlanDelete"></j-upload>
  <h3 :style="{ marginBottom: '16px' }">
    生产工艺图
  </h3>
  <j-upload ref="produceCrafts" fileType="image" bizPath="qualication" @add="produceCraftsAdd"  @previewImage="preview"
            @delete ="produceCraftsDelete"></j-upload>
  <h3 :style="{ marginBottom: '16px' }">
    生产工艺图
  </h3>
  <j-upload ref="controlCrafts"  fileType="image" bizPath="qualication" @add="controlCraftsAdd"  @previewImage="preview"
            @delete ="controlCraftsDelete"></j-upload>


    <img  :id="previewImage" v-show = "false" :src="previewImage" preview />

</div>
</template>

<script>
  import JUpload from "@/components/jeecg/JUpload";
  import {loadQualifications,qualificationApply} from "../../../requestAction/request";

  export default {
        name: "QualificationApplyDetail",
      components:{
        JUpload
      },
      props:{
        companyId:'',
      },
      data(){
        return{
          disable:false,
          previewImage:'',
          dateSource:{},
          loading:false,
          changedDate:{
            //文件路径
            add:{
              companyImage:[],
              businessLicense:[],
              floorPlan:[],
              produceCrafts:[],
              controlCrafts:[]
            },
            //资质ID  不需要分类
            delete:[]
          }
        }
      },
      methods:{
        preview(url){
          this.previewImage = url;
          this.$nextTick(() => { document.getElementById(url).click();})

        },
        handleCancel(){
          this.disable = false;
        },
        companyImageAdd(file){
          this.fileAdd(file,'companyImage');
        },

        businessLicenseAdd(file){
          this.fileAdd(file,'businessLicense');
        },
        floorPlanAdd(file){
          this.fileAdd(file,'floorPlan');
        },
        produceCraftsAdd(file){
          this.fileAdd(file,'produceCrafts');
        },
        controlCraftsAdd(file){
          this.fileAdd(file,'controlCrafts');
        },
        companyImageDelete(file){
          this.fileDelete(file,'companyImage');
        },

        businessLicenseDelete(file){
          this.fileDelete(file,'businessLicense');
        },
        floorPlanDelete(file){
          this.fileDelete(file,'floorPlan');
        },
        produceCraftsDelete(file){
          this.fileDelete(file,'produceCrafts');
        },
        controlCraftsDelete(file){
          this.fileDelete(file,'controlCrafts');
        },
        fileDelete(file,type){
          console.log("delete",file)
          //老数据  history
          if(file.response.status)
            //删除的文件 name 就是id
            this.changedDate.delete.push(file.name);
          //新上传的文件附件表还没登记
          else{
            let a = this.changedDate.add[type].findIndex(e=>
                  e.url===this.imgsrc[index].url
            );
            //删除新增列表中的对象
            this.uploadImgs.splice(a,1);

          }
        },
        fileAdd(file,type){
          console.log("add",file)
          this.changedDate.add[type].push(file.response.message);
        },
        toApply(){
          let _this = this;
          this.loading=true;
          this.$confirm({
            title: "确认申报",
            content: "是否申报?",
            onOk: function () {
              _this.changedDate.companyId = _this.companyId;
              console.log("apply",_this.changedDate);
              //申报
              qualificationApply( _this.changedDate).then((res)=>{
                _this.loading=false;
                if (res.success) {
                  _this.$message.success("申请完成");
                  _this.$emit('success');
                }
                else{
                  _this.$message.error(res.message);
                }
              });
            }
          });
          this.loading=false;
        },
        //对upload赋值
        setDate(result,ref){
          let _this = this;
          let images= [];
            if (result[ref]) {
              for (let a = 0; a < result[ref].length; a++) {

                images[a] = {filePath:result[ref][a].url,fileName:result[ref][a].id};
              }
            }
            this.$nextTick(() => {

              _this.$refs[ref].initFileListArr(images);
            });

        },
        loadQualificationImgs(){
          let _this = this;
          loadQualifications({companyId:_this.companyId}).then((res)=>{
            this.dateSource = res.result;
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