<template>
  <a-select :defaultValue="defaultValue" :allowClear="allowClear" :placeholder="placeholder"
            show-search style="width: 100%" optionFilterProp="children" @change="change">
    <!--                <a-select-option :value="companyIds">请选择</a-select-option>-->
    <a-select-option v-for="(item,index) in pollutions" :key="index" :value="item.code">
      {{item.meaning}}
    </a-select-option>
  </a-select>
</template>

<script>
  import {getAction} from "../../../api/manage";
  //主要污染物选择框
    export default {
        name: "mainPollutionSelect",
      props:{
        //默认选中的值
        defaultValue:{
          default:'',
          type:String
        },
        placeholder:{
          default:'请选择',
          type:String
        },
        allowClear:{
          default:true,
          type:Boolean
        },
        //污染类型
        pollutionType:{
          type:String
        }
      },
      data(){
        return {
          pollutions:[]
        }
      },
      methods:{
        change(val){
          this.$emit('change',val);
        }
      },
      created() {
        getAction("/spc/sysPollutionCode/queryMainByType",{type:this.pollutionType}).then(res=>{
            console.log(res);
            this.pollutions = res.result;
        })
      }
    }
</script>

<style scoped>

</style>