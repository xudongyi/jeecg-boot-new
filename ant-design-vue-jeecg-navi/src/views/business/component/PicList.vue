<template >
  <a-list :grid="grid" :data-source="imgsrc">
    <a-list-item slot="renderItem" slot-scope="item, index">

      <div :style="style">
        <div
          :style="{
          width: '100%',
          height: '100%',
          position: 'relative',
          padding: '8px',
          border: (item.color!=='#d9d9d9'?'medium':'1px') +' solid '+item.color,
          'border-radius': '4px'
        }">
          <img style="width: 100%;height:100%;object-fit:cover;" alt="example" :src="item.url" preview="add">
        </div>
      </div>

    </a-list-item>
  </a-list>

</template>
<script>
  import {getFileAccessHttpUrl,uploadAction} from '@/api/manage';
  import Icons from "../../system/modules/icon/Icons";
  import {qualificationApply,queryQualification} from "../requestAction/request"

  export default {
    name:"PicList",
    components:{Icons},
    props:{

      size:{
        type:Number,
        default(){
          return 180;
        },
      },
      isApply:true,
      qualificttionType:'',
      companyId:''

    },
    data() {
      return {
        //列表展示 图片
        imgsrc:[],
        //删除图片
        deleteImgs:[],
        //上传图片
        uploadImgs:[],
        uploading:false,
        disable:false,
        style:{float: 'left',
          width:this.size+'px',
          height:this.size+'px',
          'margin-right': 10+'px',
          margin: '0 4px 4px 0',
          },
        grid:{ gutter: 16, xs: 1, sm: 2, md:this.size>110 ?2 : 4, lg: 4, xl: this.size>110 ?4 : 6, xxl: 6 }

      }
    },
    methods: {
      getPopupContainer(trigger) {
        return trigger.parentElement;
      },
      // previewImg(index){
      //   document.getElementById("img"+index).click();
      // },
      // deleteFile(index){
      //
      //   //是不是提交列表中的
      //   let a = this.uploadImgs.findIndex(e=>
      //     e.url===this.imgsrc[index].url
      //   );
      //   if(a>-1){
      //     //删除提交列表中的对象
      //     this.uploadImgs.splice(a,1);
      //   }else{
      //     //添加到删除对象中
      //     this.deleteImgs.push(this.imgsrc[index].id);
      //   }
      //   //删除列表中的元素
      //   this.imgsrc.splice(index,1);
      //   console.log(this.imgsrc);
      //   console.log(this.uploadImgs);
      //   console.log(this.deleteImgs);
      // },
      // beforeUpload(file) {
      //   console.log(file);
      //   //在本地预览
      //   const windowURL = window.URL || window.webkitURL;
      //   const dataURl = windowURL.createObjectURL(file);
      //   console.log(dataURl)
      //   this.imgsrc.push({id:0,url:dataURl});
      //   //添加到提交数组中
      //   this.uploadImgs.push({url:dataURl,value:file});
      //   //不提交文件
      //   return false;
      // },
      // //提交按钮
      // async handleUpload(){
      //   this.uploading = true;
      //   let addImgs = [];
      //   //需要提交的文件大于0
      //   if(this.uploadImgs.length>0){
      //     let formData = new FormData();
      //     this.uploadImgs.forEach(e=>{
      //       formData.append('file',e.value);
      //     });
      //
      //     formData.append('biz', "jeditor");
      //     // formData.append("jeditor","1");
      //
      //     await uploadAction(window._CONFIG['domianURL']+"/sys/common/uploadImages", formData).then((res) => {
      //       this.uploading = false;
      //       if (res.success) {
      //         this.$message.success("文件上传成功");
      //         //返回存储文件路径
      //         addImgs = res.result;
      //       }
      //       else{
      //         this.$message.error(res.message);
      //       }
      //     });
      //   }
        //发送申报请求
      //   await qualificationApply(
      //     {companyId:this.companyId,
      //       qualificttionType:this.qualificttionType,
      //       addImgs:addImgs,
      //       deleteImgs:this.deleteImgs})
      //     .then((res)=>{
      //       this.uploading = false;
      //       if (res.success) {
      //         this.$message.success("申请完成");
      //       }
      //       else{
      //         this.$message.error(res.message);
      //       }
      //     });
      //   //提交完成处理
      //   this.disable = true;
      // },
      initImages(images){

        let that =this;
        that.imgsrc = [];
        if(images) {
          images.forEach(
            element => {
              let color = '#d9d9d9';
              if(element.color){
                color = element.color;
              }
              that.imgsrc.push({id: element.id, url: getFileAccessHttpUrl(element.url),color:color});
            }
          );
        }
        // //查询申报
        // queryQualification({
        //   companyId: this.companyId,
        //   qualificttionType: this.qualificttionType
        // }).then((res) => {
        //
        //   if (res.success) {
        //     that.disable = res.result;
        //   }
        // });

      }
    },



  }
</script>
<style>

  /* item 用来放置全部的子元素 */
  .item {
    margin-top: 30px;
    width: 25vh;
    height: 20vh;
    background: green;
    object-fit:cover;
  }
  .img_div {
    position: relative;

  }
  .mask {
    position: absolute;
    margin-top: 30px;
    width: 25vh;
    height: 20vh;
    background: white;
    top: 0;
    left: 0;
    background: rgba(101, 101, 101, 0.6);
    color: #ffffff;
    opacity: 0;
  }
  .icon2 {
    position: absolute;
    top: 40%;
    left: 50%;
    height: 50%;
    width: 50%;
    margin: -5% 0 0 -15%;
  }
  .icon1 {
    position: absolute;
    top: 40%;
    left: 25%;
    height: 50%;
    width: 50%;
    margin: -5% 0 0 -15%;
  }
  .img_div:hover .mask {
    opacity: 1;
  }
</style>