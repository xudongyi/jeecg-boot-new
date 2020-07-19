<template>
  <div ref="heatmap" id="heatmap"></div>
</template>

<script>
    export default {
        name: "PollutionCloud",
        data(){
          return {
            point: {longitude: 120.92787148669515, latitude: 32.00880776879839},//定位到南通
            ponits:[]
          }
        },
        mounted() {
          this.mapInit(this.point);
          this.hideBaiduLogo();
          this.heatmapPoint();
          this.openHeatmap();
        },
        methods:{
          mapInit(point){
            let vue = this;
            vue.map = new BMap.Map(vue.$refs.heatmap);
            let marker = new BMap.Point(vue.point.longitude, vue.point.latitude);
            // 设置中心点以及缩放级别
            vue.map.centerAndZoom(marker, this.zoom ? this.zoom : 15);
            vue.map.addControl(new BMap.NavigationControl());
            //滚轮控制缩放
            vue.map.enableScrollWheelZoom(true);
            //双击缩放控制
            vue.map.enableDoubleClickZoom(false);

            if(!vue.isSupportCanvas()){
              this.$message.error('热力图目前只支持有canvas支持的浏览器,您所使用的浏览器不能使用热力图功能~')
            }
          },
          //判断浏览区是否支持canvas
          isSupportCanvas(){
            let elem = document.createElement('canvas');
            return !!(elem.getContext && elem.getContext('2d'));
          },
          //热力点
          heatmapPoint(){
            this.points = [
              {"lng":120.918261,"lat":32.021984,"count":5000},
              {"lng":120.918261,"lat":32.021984,"count":5000},
           ];
            this.heatmapOverlay = new BMapLib.HeatmapOverlay({"radius":20});
            this.map.addOverlay(this.heatmapOverlay);
            this.heatmapOverlay.setDataSet({data:this.points,max:100});
          },
          //是否显示热力图
          openHeatmap(){
            this.heatmapOverlay.show();
          },
          closeHeatmap(){
            this.heatmapOverlay.hide();
          },
          //详细的参数,可以查看heatmap.js的文档 https://github.com/pa7/heatmap.js/blob/master/README.md
          //参数说明如下:
          /* visible 热力图是否显示,默认为true
             * opacity 热力的透明度,1-100
             * radius 势力图的每个点的半径大小
             * gradient  {JSON} 热力图的渐变区间 . gradient如下所示
             *	{
              .2:'rgb(0, 255, 255)',
              .5:'rgb(0, 110, 255)',
              .8:'rgb(100, 0, 255)'
            }
          其中 key 表示插值的位置, 0~1.
              value 为颜色值.
           */

          setGradient(){
            /*格式如下所示:
           {
               0:'rgb(102, 255, 0)',
               .5:'rgb(255, 170, 0)',
               1:'rgb(255, 0, 0)'
           }*/
            let gradient = {
              0:'rgb(102, 255, 0)',
              .5:'rgb(255, 170, 0)',
              1:'rgb(255, 0, 0)'
            };
            let colors = document.querySelectorAll("input[type='color']");
            colors = [].slice.call(colors,0);
            colors.forEach(function(ele){
              gradient[ele.getAttribute("data-key")] = ele.value;
            });
            this.heatmapOverlay.setOptions({"gradient":gradient});
          },
          //隐藏左下角的百度标识
          hideBaiduLogo(){
            let that = this;
            this.interval = setInterval(function(){
              console.log( document.getElementsByClassName("anchorBL"))
              let dom = document.getElementsByClassName("anchorBL");
              if(dom&&dom.length>1){

                dom[0].style.display = 'none';
                dom[1].style.display = 'none';
                clearInterval(that.interval);
              }
            },10)
          },

        }
    }









</script>

<style scoped>
  #heatmap{
    width: 100%;
    height: 750px;
  }
</style>