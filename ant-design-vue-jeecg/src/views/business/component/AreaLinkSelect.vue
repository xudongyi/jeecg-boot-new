<template>
  <a-cascader :options="options" :value="innerValue"  placeholder="请选择" @change="handleChange" :disabled="disabled"/>
</template>

<script>

  import AreaHandler from "./AreaHandler";
  import Vue from "vue";
  import { getAction } from '@/api/manage'
  import {loadAreaDate} from '../requestAction/areaUtil'

  export default {
        name: "AreaLinkSelect",
    props: {
      value: {
        type: String,
        required:false
      },
      // 组件的类型，可选值：
      // select 下拉样式
      // cascader 级联样式（默认）
      disabled:{
        type: Boolean,
        default: false
      },
      type: {
        type: String,
        default: 'cascader'
      },
      width: {
        type: String,
        default: '100%'
      }
    },
    data() {
      return {
        innerValue: [],
        usedListeners: ['change'],
        enums: {
          type: ['cascader', 'select']
        },
        reloading: false,
        //loadAreaDate
        areaData:'',
        //originalData
        originalAreas:'',
        options:[]

      }
    },
    computed: {
      _listeners() {
        let listeners = { ...this.$listeners }
        // 去掉已使用的事件，防止冲突
        this.usedListeners.forEach(key => {
          delete listeners[key]
        })
        return listeners
      },
      _type() {
        if (this.enums.type.includes(this.type)) {
          return this.type
        } else {
          console.error(`JAreaLinkage的type属性只能接收指定的值（${this.enums.type.join('|')}）`)
          return this.enums.type[0]
        }
      },
    },
    watch: {
      value: {
        immediate: true,
        handler() {
          this.loadDataByValue(this.value)
        }
      },
    },
    created() {
      this.loadAreaDate();
      this.loadData();
      this.initAreaData();
    },
    methods: {
      /** 通过 value 反推 options */
      loadDataByValue(value) {
        if(!value || value.length==0){
          this.innerValue = []
          this.reloading = true;
          setTimeout(()=>{
            this.reloading = false
          },100)
        }else{
          this.initAreaData();
          let arr = this.areaData.getRealCode(value);
          this.innerValue = arr
        }
      },
      /** 通过地区code获取子级 */
      loadData() {


        let pcaa = Vue.ls.get('sys_areas');
        let arr = []
        const province = pcaa['86']
        Object.keys(province).map(key=>{
          let arr1 = []
          const city = pcaa[key];
          Object.keys(city).map(key2=>{
            let arr2 = []
            const qu = pcaa[key2];
            Object.keys(qu).map(key3=>{
              arr2.push({value:key3, label:qu[key3]});
            })
            arr1.push({value:key2, label:city[key2], children:arr2});
          })
          arr.push({value:key, label:province[key],children:arr1});
        })
        this.options = arr
        console.log("options",arr)
      },
      /** 判断是否有子节点 */
      hasChildren(options) {
        options.forEach(option => {
          let data = this.loadDataByCode(option.value)
          option.isLeaf = data.length === 0
        })
      },
      handleChange(values) {
        let value = values[values.length - 1]
        this.$emit('change', value)
      },
      loadAreaDate:async  function(){

       this.originalAreas =   await  loadAreaDate();
          //再从数据库中获取
      },
      initAreaData(){
        if(!this.areaData){
          this.areaData = new AreaHandler();
        }
      },
    },
    model: { prop: 'value', event: 'change' },
  }
</script>

<style lang="less" scoped>
  .j-area-linkage {
    height:40px;
    /deep/ .area-cascader-wrap .area-select {
      width: 100%;
    }

    /deep/ .area-select .area-selected-trigger {
      line-height: 1.15;
    }
  }

</style>