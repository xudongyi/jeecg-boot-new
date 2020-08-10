<template>
  <div class="analysis" >
    <top-title :title="title"></top-title>
    <mid-menu @scaleChange="reScale"></mid-menu>
    <footer-memu :scale="scale"></footer-memu>
<!--    <lottie :options="defaultOptions" :height="400" :width="400" />-->
  </div>
</template>

<script>
  import  animationData from '@/assets/gif/29128-van-morphing-animation.json';
  import Lottie from 'vue-lottie';
  import TopTitle from './TopTitle';
  import MidMenu from './MidMenu';
  import FooterMemu from "./FooterMemu";
  export default {
    name: "Analysis",
    components: {
      Lottie,
      TopTitle,
      MidMenu,
      FooterMemu
    },
    data() {
      return {
        title:'生态环境大数据及智慧环保监管平台',
        defaultOptions: {animationData: animationData},
        scale:1
      }
    },
    created() {
    },
    methods: {
      reScale(val){
        this.scale = val;
      }
    },

  }
</script>
<style scoped>
  .analysis{
    background:url('~@/assets/analysisBg.png') no-repeat 50%;
    background-size:cover;
    width:100%;
    height:100%  ;

  }
</style>