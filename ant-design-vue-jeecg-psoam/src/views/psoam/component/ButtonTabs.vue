<template>
  <div>
    <a-button class="double-icon"  icon="double-left" @click="left" v-show="firstIndex>0"></a-button>
    <a-button  v-for="(item,index) in dataList"
               :ghost="checkedRadio!==index" @click="check(index)" :style="{width:(item.site_name.length+1)*15+'px', color: checkedRadio!==index?'#0098A2':'white',background: '#0098A2'}"
               class="radio-column-point">{{item.site_name}}</a-button>
    <a-button  class="double-icon" icon="double-right"  @click="right" v-show="firstIndex+3<tabList.length"></a-button>
  </div>

</template>

<script>
    export default {
      name: "ButtonTabs",
      props:{
          tabList:{
            type:Array
          }
      },
      data(){
          return {
            firstIndex:0,
            checkedRadio:0,
          }
      },
      computed:{
        dataList(){
          if(this.tabList.length>3){
            let  result = [];
            result[0] = this.tabList[this.firstIndex];
            result[1] = this.tabList[this.firstIndex+1];
            result[2] = this.tabList[this.firstIndex+2];
            return result;
          }else{
            return this.tabList;
          }

        }
      },
      methods:{
        left(){
          this.firstIndex--;
            this.checkedRadio++
        },
        right(){
          this.firstIndex++;
            this.checkedRadio--
        },
        check(index){
          this.checkedRadio = index;
          this.$emit('change', index+this.firstIndex);
        }
      }
    }
</script>

<style scoped>
  .radio-column-point{
    font-size:13px;
    border-width: 1px ;
    width: 100px;
    height: 30px;
    border-radius: 4px;
    margin-left: 10px;
    padding: 0;
    text-align: center;
    color: #0098A2;
    border-color:#0098A2;
  }
  .double-icon{
    height: 30px;
    width: 30px;
    margin-left: 10px;
  }
</style>