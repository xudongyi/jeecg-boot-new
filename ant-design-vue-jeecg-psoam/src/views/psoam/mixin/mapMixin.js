/**
 * 新增修改完成调用 modalFormOk方法 编辑弹框组件ref定义为modalForm
 * 高级查询按钮调用 superQuery方法  高级查询组件ref定义为superQueryModal
 * data中url定义 list为查询列表  delete为删除单条记录  deleteBatch为批量删除
 */
import { filterObj } from '@/utils/util';
import { deleteAction, getAction,downFile,getFileAccessHttpUrl } from '@/api/manage'
import Vue from 'vue'
import { ACCESS_TOKEN } from "@/store/mutation-types"
import {queryLastAirInfo} from "../../../../../ant-design-vue-jeecg-airq/src/views/requestAction/request";

export const mapMixin = {
  data(){
    return {
      isEarthMap:false
    }
  },
  computed:{
    leftFixed(){
      return {
        left:this.isMobile()?'30px':'230px',
      }
    },
    mapStyle(){
      return {
        backgroundImage:'url(' + require('@/assets/map/map.png') + ')',
        border:!this.isEarthMap?'2px solid rgba(94,187,192,1)':'2px solid',
        'border-radius':'12px',
        width:!this.isEarthMap?'66px':'62px',
        height:!this.isEarthMap?'66px':'62px',
      }
    },
    earthStyle(){
      return {
        backgroundImage:'url(' + require('@/assets/map/earth.png') + ')',
        border:this.isEarthMap?'2px solid rgba(94,187,192,1)':'2px solid',
        'border-radius':'12px',
        width:this.isEarthMap?'66px':'62px',
        height:this.isEarthMap?'66px':'62px',
      }
    }
  },
  created() {
  },
  methods:{
    toEarth(){
      this.isEarthMap = true
      console.log(BMAP_SATELLITE_MAP);
      this.map.setMapType(BMAP_SATELLITE_MAP);

    },
    toMap(){
      this.isEarthMap = false
      this.map.setMapType(BMAP_NORMAL_MAP);
    },
    mapInit() {
      let vue = this;
      vue.map = new BMap.Map(vue.$refs.myMap, {
        enableMapClick: false
      });
      let marker = new BMap.Point(vue.point.longitude, vue.point.latitude);
      // 设置中心点以及缩放级别
      vue.map.centerAndZoom(marker, this.zoom ? this.zoom : 15);

      //滚轮控制缩放
      vue.map.enableScrollWheelZoom(true);
      //双击缩放控制
      vue.map.enableDoubleClickZoom(false);


      // 事件监听   获取点击点坐标 地图点击事件
      vue.map.addEventListener("click", (e) => {
        var center = vue.map.getCenter();
        if (vue.clickadd) {
          this.addOneOverlay(e.point);
        }
        vue.$emit("mapClick", center);
      });
      // 设置地图风格样式  可自定义
      vue.map.setMapStyleV2({
        styleId: '5e050d02262308dbfabc96e19ddc0482'
      });
      /**  tilesloaded 地图加载完成事件 */
      vue.map.addEventListener("tilesloaded", () => {
        let area = vue.map.getBounds();
        this.$emit("visibleArea", area);
        if (this.vevisibleFalg) {
          this.visibleAreaPonit();
        }
      });
    },
    getBoundary(){
      var bdary = new BMap.Boundary();
      let vue  = this
      bdary.get(this.cityName, function(rs){       //获取行政区域
        //vue.map.clearOverlays();        //清除地图覆盖物
        var count = rs.boundaries.length; //行政区域的点有多少个
        if (count === 0) {
          alert('未能获取当前输入行政区域');
          return ;
        }
        var pointArray = [];
        for (var i = 0; i < count; i++) {
          var ply = new BMap.Polygon(rs.boundaries[i], {strokeWeight: 2, strokeColor: "#ff0000",fillColor:""}); //建立多边形覆盖物
          vue.map.addOverlay(ply);  //添加覆盖物
          pointArray = pointArray.concat(ply.getPath());
        }
        vue.map.setViewport(pointArray);    //调整视野
      });
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
          if( dom[0].style.display === 'none')
            clearInterval(that.interval);
        }
      },10)
    }

  },



};