<template>
<div>
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
    <a-radio-group  >
      <a-radio value="Y">
        通过
      </a-radio>
      <a-radio value="N">
        不通过
      </a-radio>
    </a-radio-group>
    </a-form-item>
      <a-form-item label="不通过原因：">
    <a-textarea placeholder="Basic usage" :rows="2" />
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


</div>
</template>

<script>
  import moment from 'moment'

  //审批的脚部组件
    export default {
        name: "AuditFooter",
      props:{
        dateFormat:{
          type: String,
          default: 'YYYY-MM-DD HH:mm:ss',
          required: false
        },
      },
      data(){
          return{
            applyer:'',
            applyTime:'',
            date:' '
          }

      },
      methods:{

      },
      watch:{
        applyTime(newVal){

          console.log(newVal)
          this.applyTime = moment(newVal).format(this.dateFormat);
        }
      },



      mounted() {
        let _this = this; // 声明一个变量指向Vue实例this，保证作用域一致
        this.timer = setInterval(() => {
          _this.date =moment().format(this.dateFormat); // 修改数据date
        }, 1000);

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