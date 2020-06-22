<template>
<div>
  <a-row :gutter="24">
    <a-col :span="3" ></a-col>
    <a-col :span="20">
    <a-descriptions >
    <a-descriptions-item label="申报人">
     {{applyer}}
    </a-descriptions-item>
    <a-descriptions-item label="申报时间">
      {{applyTime}}
    </a-descriptions-item>
  </a-descriptions>

  <a-form>
    <a-form-item label="审核结果：">
    <a-radio-group  v-model= "result" :disabled="disable">
      <a-radio value="2">
        通过
      </a-radio>
      <a-radio value="3">
        不通过
      </a-radio>
    </a-radio-group>
    </a-form-item>
      <a-form-item label="不通过原因：">
    <a-textarea placeholder="备注信息" :rows="2"  v-model.trim = "message" :disabled="disable"/>
    </a-form-item>
  </a-form>
    <a-descriptions >
      <a-descriptions-item label="审核人">
        {{this.$store.getters.userInfo.realname}}
      </a-descriptions-item>
      <a-descriptions-item label="审核时间">
        {{date}}
      </a-descriptions-item>
    </a-descriptions>
    </a-col>
  </a-row>


</div>
</template>

<script>
  import moment from 'moment'
  import {submitAudit} from '../../../requestAction/request'
  //审批的脚部组件
    export default {
        name: "AuditFooter",
      props:{
        dateFormat:{
          type: String,
          default: 'YYYY-MM-DD HH:mm:ss',
          required: false,

        },
        disable:false
      },
      data(){
          return{
            applyInfo:{},
            applyer:'',
            applyTime:'',
            date:' ',
            result:'2',
            message:''
          }

      },
      methods:{
        //数据提交
        submit(record){
          let _this = this;
          submitAudit({userId:this.$store.getters.userInfo.id,applyId:record.id,
            result:this.result,message:this.message,fromTable:record.fromTable}).then((res)=>{
            if(res.success){
              _this.$emit("success");
              _this.$message.success("提交成功");
            }else{
              _this.$emit("false");
              _this.$message.error(res.message);
            }

          });


        },
        edit(record){
          this.applyer = record.createBy;
          this.applyTime =  moment(record.createTime).format(this.dateFormat);
          this.result = record.status;
          this.message = record.content;
          if(record.updateTime==null){
            this.date = moment().format(this.dateFormat);

            if (this.timer){

            }else{
              let _this = this; // 声明一个变量指向Vue实例this，保证作用域一致
              this.timer = setInterval(() => {
                _this.date =moment().format(this.dateFormat); // 修改数据date
              }, 1000);
            }

          }else{
            if (this.timer) {
              clearInterval(this.timer);
            }
            this.date = moment(record.updateTime).format(this.dateFormat);

          }
          if(record.isView) {
            this.isView = this.applyInfo.isView;
          }
          else {
            this.isView = false;
          }
        }
      },


      beforeDestroy() {
        if (this.timer) {
          clearInterval(this.timer); // 在Vue实例销毁前，清除我们的定时器
        }
      }
    }
</script>

<style scoped>

</style>