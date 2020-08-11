<template>
  <div>
  <div class="menu-middle" v-show="!showEarth">
    <div class="center-menu" :style="centerMenu">
        <div class="memu-item" v-for="item in memuData">
          <span @click="toNewPage(item.sysUrl)">{{item.sysName}}</span>
        </div>
    </div>

  </div>
  <div class="earth Rotation" id = "earthimg"  :style = "earthStyle" v-show="showEarth"></div>
  </div>
</template>

<script>
  import {debounce} from "lodash";

  export default {
        name: "MidMenu",
        props:{
          memuKey:{
            type:String,
            default:'earth'
          }
        },
        data(){
          return{
            earthStyle:{},
            centerMenu:{
                width:'1500px',
                'margin-left':'210px',
                padding:'9rem 16% 0 16%',
                'font-size':'21px',
                  'line-height': '70px'
            },
            totalMenu:{
              earth:[],
              Integratedm:[


                {
                  sysName:'大气环境质量',
                  sysUrl:'8199'
                },
                {
                  sysName:'生态环境大数据中心',
                  sysUrl:'8220'
                },
                {
                  sysName:'企业基础档案',
                  sysUrl:'80'
                },
                {
                  sysName:'站点基础档案',
                  sysUrl:'8099'
                },
                {
                  sysName:'园区基础档案',
                  sysUrl:'8099'
                },
                {
                  sysName:'企业信用评价',
                  sysUrl:'8099'
                },
                {
                  sysName:'环保专项整治',
                  sysUrl:'8099'
                },
                {
                  sysName:'移动执法系统',
                  sysUrl:'8099'
                },
                {
                  sysName:'重污染天气应急',
                  sysUrl:'8099'
                },
              ],
              solidwaste:[
                {
                  sysName:'危险废物管理',
                  sysUrl:'8220'
                },
                {
                  sysName:'危险化学品管理',
                  sysUrl:'8220'
                },
                {
                  sysName:'特征污染物名录库',
                  sysUrl:'8220'
                },
              ],
              ecology:[
                {
                  sysName:'大气环境质量监控',
                  sysUrl:'8199'
                },
                {
                  sysName:'地表水环境监控',
                  sysUrl:'8220'
                },
                {
                  sysName:'大气网格化监控',
                  sysUrl:'8220'
                },
                {
                  sysName:'扬尘噪声监控',
                  sysUrl:'8220'
                },
                {
                  sysName:'河长制管理',
                  sysUrl:'8220'
                },
                {
                  sysName:'环境预警管理',
                  sysUrl:'8220'
                },
              ],
              pollutions:[
                {
                  sysName:'污染源排放在线监控',
                  sysUrl:'8220'
                },
                {
                  sysName:'污染源工况监控',
                  sysUrl:'8220'
                },
                {
                  sysName:'污染源能耗监控',
                  sysUrl:'8220'
                },
                {
                  sysName:'污染源能效监控',
                  sysUrl:'8220'
                },
                {
                  sysName:'园区LDAR泄漏检测与修复',
                  sysUrl:'8220'
                },
              ],
              emergency:[
                {
                  sysName:'应急指挥调度中心',
                  sysUrl:'8220'
                },
                {
                  sysName:'风险源申报',
                  sysUrl:'8220'
                },
                {
                  sysName:'风险应急信息',
                  sysUrl:'8220'
                },
                {
                  sysName:'应急综合业务',
                  sysUrl:'8220'
                },
                {
                  sysName:'应急事件评估',
                  sysUrl:'8220'
                },
                {
                  sysName:'风险评估预警',
                  sysUrl:'8220'
                },
              ],
              traceability:[
                {
                  sysName:'污染扩散分析',
                  sysUrl:'8220'
                },
                {
                  sysName:'污染溯源分析',
                  sysUrl:'8220'
                }
              ],
            }
          }
        },
        computed:{
          showEarth(){
            return 'earth'===this.memuKey
          },
          memuData(){
            return this.totalMenu[this.memuKey]
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
            this.centerMenu['font-size'] =  21*scale+'px';
            this.centerMenu['line-height'] = 70*scale+'px';
            this.earthStyle = {
                width:544*scale+'px',
                height:544*scale+'px',
                left: (containerWidth - 544*scale)/2+'px'
            }
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
            window.open("http://"+document.domain+':'+url)
          }
        },
        created() {
          this.listenResize();
          // this.rotateVal=0
          // let that = this
          // this.InterVal = setInterval(function () {
          //   var img = document.getElementById('earthimg')
          //   that.rotateVal += 1
          //   // 设置旋转属性(顺时针)
          //   img.style.transform = 'rotate(' +  that.rotateVal + 'deg)'
          //   // 设置旋转属性(逆时针)
          //   //img.style.transform = 'rotate(-' + rotateVal + 'deg)'
          //   // 设置旋转时的动画  匀速0.1s
          //   img.style.transition = '0.1s linear'
          // }, 100)
        },
        // destroyed() {
        //   if(this.InterVal)
        //     clearInterval(this.InterVal)
        // }

  }
</script>

<style scoped>

  .Rotation
  {
    background:blue;
    position:relative;
    animation:rotating 15s linear infinite;
    /*animation:rotating 5s infinite;*/
    -webkit-animation:rotating 15s linear infinite; /*Safari and Chrome*/
  }



  @keyframes rotating{
    from{transform:rotate(0)}
    to{transform:rotate(360deg)}
  }

  .menu-middle{
  height: 55%;
  width: 100%;
  position: absolute;
  top :12%;
  z-index: 2;
  text-align: center;
}
.earth{
  background: url("~@/assets/earth3D.png")  center center no-repeat;
  background-size:cover;
  top :18%;
  position: absolute;
  border-radius: 250px;
}
.center-menu{
  background:url("~@assets/midMenuBg.png") no-repeat;
  background-size:cover;
  height: 100%;
  z-index: 20;

}
  .memu-item{
    width: 32%;
    height: 20%;
    text-align: center;

    color: #ffffff;
    cursor:pointer;
    margin: auto;
    margin-top: 2%;
    border:2px;
    float: left;

  }
.memu-item:hover{
  color: #ecdf51;
  border:2px solid #ecdf51;
}
</style>