<template>
  <div
    :style="{height:this.containerHeight,width:'100%', background:' rgba(255, 255, 255, 1)',boxSshadow: '1px 2px 6px 0px rgba(0, 0, 0, 0.2)', borderRadius: '4px'}">
    <div class="top">
      <div class="title">监测类型配置</div>
      <div class="save">
        <a-button style="background-color: #0098A1;color: #FEFEFE" @click="save">保存</a-button>
      </div>
    </div>
    <div class="content">
      <div class="detail">
        <div class="water">
          <div class="hover-mask" :style="waterHover"></div>
          <div class="mask" @click="waterCheck"></div>
          <a-icon v-show="waterChecked" type="check" class="check" @click="waterCheck"/>
        </div>
        <div class="label">废水监测</div>
      </div>
      <div class="detail">
        <div class="gas">
          <div class="hover-mask" :style="gasHover"></div>
          <div class="mask" @click="gasCheck"></div>
          <a-icon v-show="gasChecked" type="check" class="check" @click="gasCheck"/>
        </div>
        <div class="label">废气监测</div>
      </div>
      <div class="detail">
        <div class="vocs">
          <div class="hover-mask" :style="vocsHover"></div>
          <div class="mask" @click="vocsCheck"></div>
          <a-icon v-show="vocsChecked" type="check" class="check" @click="vocsCheck"/>
        </div>
        <div class="label">VOCs监测</div>
      </div>
    </div>
  </div>

</template>

<script>
  import {mixinDevice} from '@/utils/mixin'
  import {queryConfig,saveConfig} from '../../requestAction/request'

  export default {
    name: "MonitorTypeConf",
    mixins: [mixinDevice],
    data() {
      return {
        containerHeight: "",
        waterChecked: false,
        gasChecked: false,
        vocsChecked: false,
        waterHover: {},
        gasHover: {},
        vocsHover: {},
        model: {},
      }
    },
    methods: {
      waterCheck() {
        if (this.waterChecked) {
          this.waterChecked = false;
          this.waterHover.opacity = "0.5";
          this.waterHover.background = "#fff";
        } else {
          this.waterChecked = true;
          this.waterHover.opacity = "1";
          this.waterHover.background = "";
        }
      },
      gasCheck() {
        if (this.gasChecked) {
          this.gasChecked = false;
          this.gasHover.opacity = "0.5";
          this.gasHover.background = "#fff";
        } else {
          this.gasChecked = true;
          this.gasHover.opacity = "1";
          this.gasHover.background = "";
        }
      },
      vocsCheck() {
        if (this.vocsChecked) {
          this.vocsChecked = false;
          this.vocsHover.opacity = "0.5";
          this.vocsHover.background = "#fff";
        } else {
          this.vocsChecked = true;
          this.vocsHover.opacity = "1";
          this.vocsHover.background = "";
        }
      },
      save(){
        saveConfig({waterChecked:this.waterChecked,gasChecked:this.gasChecked,vocsChecked:this.vocsChecked}).then(res =>{
          if(res.result){
            this.$message.success('保存成功！');
          }else{
            this.$message.error('保存失败');
          }
        })
      }
    },
    created() {
      let containerHeight = document.body.clientHeight || document.documentElement.clientHeight;
      if (this.isMobile()) {
        containerHeight = containerHeight - 52 - 59 - 93;
      } else {
        containerHeight = containerHeight - 59 - 93;
      }
      this.containerHeight = containerHeight + "px";
      //查询类别
      let that = this;
      queryConfig().then(res => {
        that.waterChecked = res.result.waterChecked;
        that.gasChecked = res.result.gasChecked;
        that.vocsChecked = res.result.vocsChecked;
        if(that.waterChecked){
          that.waterHover.opacity = "1";
          that.waterHover.background = "";
        }else {
          that.waterHover.opacity = "0.5";
          that.waterHover.background = "#fff";
        }
        if(that.gasChecked){
          that.gasHover.opacity = "1";
          that.gasHover.background = "";
        }else{
          this.gasHover.opacity = "0.5";
          this.gasHover.background = "#fff";
        }
        if(that.vocsChecked){
          that.vocsHover.opacity = "1";
          that.vocsHover.background = "";
        }else{
          this.vocsHover.opacity = "0.5";
          this.vocsHover.background = "#fff";
        }
      })
    },
  }
</script>

<style scoped>
  .top {
    height: 8%;
    width: 100%;
    border-bottom: 1px solid #D9D9D9;
  }

  .title {
    height: 100%;
    float: left;
    font-size: 16px;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: #0098A1;
    display: flex;
    align-items: center;
    margin-left: 2%;
  }

  .save {
    height: 100%;
    float: right;
    display: flex;
    align-items: center;
    margin-right: 2%;
  }

  .label {
    text-align: center;
    font-size: 14px;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: #010101;
    opacity: 0.95;
    width: 347px;
    height: 20px;
    margin-top: 20px;
  }

  .content {
    height: 92%;
    width: 100%;
    display: flex;
    align-items: center;
  }

  .detail {
    float: left;
    margin: 0 auto;
    width: 348px;
    height: 340px;
  }

  .water {
    width: 347px;
    height: 300px;
    background: url(~@/assets/config/water.png) no-repeat;
    position: relative;
    border: 1px solid #0098A1;
    border-left: none;
    border-radius: 5px;
    display: flex;
    align-items: center;
  }

  .gas {
    width: 348px;
    height: 300px;
    background: url(~@/assets/config/gas.png) no-repeat;
    position: relative;
    border: 1px solid #0098A1;
    border-left: none;
    border-radius: 5px;
    display: flex;
    align-items: center;
  }

  .vocs {
    width: 347px;
    height: 300px;
    background: url(~@/assets/config/vocs.png) no-repeat;
    position: relative;
    border: 1px solid #0098A1;
    border-left: none;
    border-radius: 5px;
    display: flex;
    align-items: center;
  }

  .hover-mask {
    width: 350px;
    height: 300px;
    position: absolute;
  }

  .mask {
    position: absolute;
    right: 0px;
    top: 0px;
    border-style: solid;
    border-width: 30px 30px 30px 30px;
    border-color: white white transparent transparent;
    width: 0px;
    height: 0px;
    border-top-right-radius: 5px;
  }

  .check {
    position: absolute;
    right: 5px;
    top: 5px;
    font-size: 24px;
    color: #0098A1;
  }
</style>