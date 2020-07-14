<template>
  <div v-if="!reloading" class="j-area-linkage">
    <area-cascader
      v-if="_type === enums.type[0]"
      :value="innerValue"
      :data="this.originalAreas"
      :level="1"
      :style="{width}"
      v-bind="$attrs"
      v-on="_listeners"
      @change="handleChange"
    />
    <area-select
      v-else-if="_type === enums.type[1]"
      :value="innerValue"
      :data="this.originalAreas"
      :level="2"
      v-bind="$attrs"
      v-on="_listeners"
      @change="handleChange"
    />
    <div v-else>
      <span style="color:red;"> Bad type value: {{_type}}</span>
    </div>
  </div>
</template>

<script>

  import AreaHandler from "./AreaHandler";
  import Vue from "vue";
  import { getAction } from '@/api/manage'
  import {loadAreaDate} from './areaUtil'


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
        originalAreas:""

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
      loadDataByCode(value) {
        let options = []
        let data = this.originalAreas[value]
        if (data) {
          for (let key in data) {
            if (data.hasOwnProperty(key)) {
              options.push({ value: key, label: data[key], })
            }
          }
          return options
        } else {
          return []
        }
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