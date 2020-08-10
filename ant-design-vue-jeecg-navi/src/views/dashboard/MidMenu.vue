<template>
  <div>
  <div class="menu-middle">
    <div class="center-menu" :style="centerMenu">
        <div class="memu-item" v-for="item in memuData">
          <span @click="toNewPage(item.sysUrl)">{{item.sysName}}</span>
        </div>
    </div>

  </div>
  <div class="earth" id = "earthimg"></div>
  </div>
</template>

<script>
  import {debounce} from "lodash";

  export default {
        name: "MidMenu",
        data(){
          return{
            centerMenu:{
                width:'1500px',
                'margin-left':'210px',
                padding:'9rem 18rem 0 18rem'
            },
            memuData:[
              {
                sysName:'一企一档',
                sysUrl:'http://192.168.0.2'
              },
              {
                sysName:'一站一档',
                sysUrl:'http://192.168.0.2:8099'
              },
              {
                sysName:'大气环境质量',
                sysUrl:'http://192.168.0.2:8199'
              },
              {
                sysName:'生态环境大数据',
                sysUrl:'http://192.168.0.2:8220'
              },
            ]
          }
        },
        methods:{
          initScale() {
            //初始高度  970
            let height =970;
            //动态效果
            let containerWidth = document.body.clientWidth || document.documentElement.clientWidth;
            let containerHeight = document.body.clientHeight || document.documentElement.clientHeight;
            console.log(containerWidth,containerHeight)
            // sacle 缩放比例。  竖向缩放
            let scale = 1;
            if (containerHeight < height) {
              scale = containerHeight /height;
            }
            this.centerMenu.width = 1500*scale+'px';

            this.$emit('scaleChange',containerWidth / 1920);
            this.centerMenu['margin-left'] = (containerWidth - 1500*scale)/2+'px';
            // sacle 缩放比例。  横竖一起缩放
            // let scale1 = 1;
            // if (containerHeight < height) {
            //   scale1 = containerHeight / height;
            // }
            // let scale2 = 1;
            // if(containerWidth<width){
            //   scale2 = containerWidth / width;
            // }
            // let scale = Math.min(scale1,scale2);
            // this.centerMenu.width = 1500*scale+'px';
            // this.centerMenu.height = 533*scale+'px';
          },
          listenResize() {
            this.initScale();
            window.addEventListener('resize', debounce(() => {
              this.initScale();
            }, 300));
          },
          toNewPage(url){
            window.open(url)
          }
        },
        created() {
          this.listenResize();
          this.rotateVal=0
          let that = this
          this.InterVal = setInterval(function () {
            var img = document.getElementById('earthimg')
            that.rotateVal += 1
            // 设置旋转属性(顺时针)
            img.style.transform = 'rotate(' +  that.rotateVal + 'deg)'
            // 设置旋转属性(逆时针)
            //img.style.transform = 'rotate(-' + rotateVal + 'deg)'
            // 设置旋转时的动画  匀速0.1s
            img.style.transition = '0.1s linear'
          }, 100)
        },
        destroyed() {
          clearInterval(this.InterVal)
        }

  }
</script>

<style scoped>
.menu-middle{
  height: 55%;
  width: 100%;
  position: absolute;
  top :15%;
  z-index: 2;
}
.earth{
  background: url("~@/assets/earth3D.png")  center center no-repeat;
  height: 60%;
  width: 40%;
  top :15%;
  left: 30%;
  position: absolute;
}
.center-menu{
  background:url("~@assets/midMenuBg.png") no-repeat;
  background-size:cover;
  height: 100%;
  z-index: 20;

}
  .memu-item{
    width: 15rem;
    height: 4rem;
    text-align: center;
    font-size: 1.6rem;
    color: #ffffff;
    cursor:pointer;
    margin: 2.2rem 1.1rem 0 2rem;
    border:3px;
    float: left;
    line-height: 55px;
  }
.memu-item:hover{
  color: #ecdf51;
  border:2px solid #ecdf51;
}
</style>