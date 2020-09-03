<template>
  <div class="digital">
    <div ref="myMap" class="map"></div>
    <!--左上角搜索框-->
    <auto-select :dataList="dataList" placeholder="请输入关键词查询" class="select-company" @select="onSelect"></auto-select>
    <!--  右上角的样式  -->
    <div class="pos_fixed">
      <div class="radio-group">
        <a-button :ghost="checkedRadio!=='total'" @click="check('total')" :style="checkedRadio==='total'?radio_check:{}"
                  class="radio-column total">全部&#12288;{{companyCounts.total}}</a-button>
        <a-button :ghost="checkedRadio!=='normal'" @click="check('normal')" :style="checkedRadio==='normal'?radio_check:{}"
                  class="radio-column normal">正常&#12288;{{companyCounts.normal}}</a-button>
        <a-button :ghost="checkedRadio!=='standard'" @click="check('standard')" :style="checkedRadio==='standard'?radio_check:{}"
                  class="radio-column standard">超标&#12288;{{companyCounts.standard}}</a-button>
        <a-button :ghost="checkedRadio!=='abnormal'" @click="check('abnormal')" :style="checkedRadio==='abnormal'?radio_check:{}"
                  class="radio-column abnormal">异常&#12288;{{companyCounts.abnormal}}</a-button>
        <a-button :ghost="checkedRadio!=='part'" @click="check('part')" :style="checkedRadio==='part'?radio_check:{}"
                  class="radio-column part">部分离线&#32;{{companyCounts.part}}</a-button>
        <a-button :ghost="checkedRadio!=='offline'" @click="check('offline')" :style="checkedRadio==='offline'?radio_check:{}"
                  class="radio-column offline">全部离线&#32;{{companyCounts.offline}}</a-button>
      </div>
    </div>

    <!-- 左下角的按钮  -->
    <div class="two-button" >
      <div class="map-button" :style="mapStyle"  @click="toMap"><span class="button-span">地图</span></div>
      <div class="earth-button" :style="earthStyle"  @click="toEarth"><span class="button-span">地球</span></div>
    </div>
    <company-modal ref="company"></company-modal>
  </div>
</template>

<script>
  import { mixinDevice } from '@/utils/mixin';
  import {mapMixin} from "../mixin/mapMixin";
  import {mapButton} from "../constants/colorConstant";
  import AutoSelect from "../component/AutoSelect";
  import {queryGisInfos} from "../../requestAction/request";
  import {mapIcons} from "../constants/picConstant";
  import companyModal from "./modal/companyModal";
  export default {
    name: "gisMap",
    mixins: [mixinDevice,mapMixin],
    components:{
      AutoSelect,companyModal
    },
    data(){
          return {
            point: {longitude: 120.92787148669515, latitude: 32.00880776879839},//定位到南通
            cityName:'南通',//画的边框
            checkedRadio:'total',
            mapButtonColors:mapButton,
            dataList:[],
            orginalData:{
              normal:[],
              standard:[],
              abnormal:[],
              part:[],
              offline:[],
            },
            queryResult:[],
            companyCounts:{
              total:0,
              normal:0,
              standard:0,
              abnormal:0,
              part:0,
              offline:0,
            },
          }
      },
      computed:{
        radio_check(){
          return {
              color:'white',
              background: mapButton[this.checkedRadio],
              'border-color': mapButton[this.checkedRadio],
          }
        },
      },
      methods: {
        check(val){
          let _this = this;
          this.checkedRadio = val;
          //其他事件  只显示这部分数据
          // this.map.clearOverlays();
          if(val==='total'){
             Object.keys(this.orginalData).forEach(v=>{
               _this.orginalData[v].forEach(e=>{
                  e.show()
               })
             })
          }else{
            Object.keys(this.orginalData).forEach(v=>{

              if(v===val){
                _this.orginalData[v].forEach(e=>{
                  e.show();
                })
              }else{
                _this.orginalData[v].forEach(e=>{
                  e.hide();
                })
              }

            })
          }
        },
        onSelect(companyId){
          let company = this.queryResult[companyId];
          this.map.setZoom(13)
          this.map.panTo(new BMap.Point(company.longitude, company.dimension));
          // this.$refs.company.show(company);
        },
        siteToMap(){
          let vue = this
          //查询 节点
          queryGisInfos({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res)=> {
            vue.queryResult = res.result;
            // let markers = [];
            let keys = Object.keys(res.result);
            for(let i=0;i<keys.length;i++){
              let item = res.result[keys[i]];
              vue.dataList.push({key: keys[i], value: item.company_name});
              let status = this.status(item.points);

              let pt = new BMap.Point(item.longitude, item.dimension);
              let iconUrl = mapIcons[status]
              let myIcon = new BMap.Icon(iconUrl, new BMap.Size(30, 30));
              let label = new BMap.Label("<span class='my-maptip'>" + item.short_name + "</span>",
                {offset: new BMap.Size(-item.short_name.length * 6 + 8, 25)});
              let marker = new BMap.Marker(pt, {icon: myIcon, title: item.company_name});  // 创建标注
              marker.addEventListener("click", () => {
                vue.$refs.company.show(item);
              });
              label.addEventListener("click", () => {
                vue.$refs.company.show(item);
              });
              label.setStyle({//给label设置样式，任意的CSS都是可以的
                "color": "red",//颜色
                "fontSize": "14px",
                "border": "0",
                "height": "auto",
                "width": "auto",
                "textAlign": "center",
                "lineHeight": "30px",
                "background": "lightgray",
                "cursor": "pointer"
              });
              marker.setLabel(label);
              vue.map.addOverlay(marker);
              vue.orginalData[status].push(marker);
            }


          })
        },

        //label的显示与隐藏
        labelDisplay(){
          var currentZoom = this.map.getZoom();
          let dom = document.getElementsByClassName('my-maptip');
          // console.log( dom)
          for(let i=0;i<dom.length;i++) {
            // console.log( dom[i].parentElement)
            //2公里才会显示
            dom[i].parentElement.style.display = currentZoom <= 12 ? 'none' : 'block';
          }
        },

        //公司状态判定
        status(points){
          //根据  item的points  的属性获取状态  并统计数据
          let offlinecounts = 0;

          this.companyCounts.total++;
          for(let i=0;i<points.length;i++){
            let  point  = points[i];

            if(point.data_status>=6){
              this.companyCounts.abnormal++;
              return 'abnormal';
            }
            if(point.data_status>1){
              this.companyCounts.standard++;
              return 'standard';
            }
            if(point.online_status!==1){
              offlinecounts++;
            }
          }
          if(offlinecounts>0){
            if(offlinecounts===points.length){
              this.companyCounts.offline++;
              return 'offline';
            }
            this.companyCounts.part++;
            return 'part';
          }
          this.companyCounts.normal++;
          return 'normal';
        },
        //添加控件
        addControl(){
          this.map.addControl(new BMap.ScaleControl());

        }
      },
      mounted() {
        this.mapInit();
        this.hideBaiduLogo();
        this.getBoundary();
        this.addControl();
        this.siteToMap();
        //是否显示label
        this.map.addEventListener("zoomend", this.labelDisplay);
        this.labelDisplay();
      }


    }
</script>

<style>
  .digital{
    position:relative
  }
  .select-company{
    position:absolute;
    top:20px;
    left:20px;
    z-index: 19;
  }
  .radio-column{
    font-size:13px;
    border-width: 1px ;
    width: 80px;
    height: 30px;
    border-radius: 4px;
    margin-left: 10px;
    padding: 0;
    text-align: center;
  }
  .total{
    color: #00BDC8;
    background: #00BDC8;
    border-color:#00BDC8;
  }
  .normal{
    color: #00D660;;
    background: #00D660;
    border-color:#00D660;

  }
  .standard{
    color: #FF1212;
    background: #FF1212;
    border-color:#FF1212;
  }
  .abnormal{
    color: #FF40FF;
    background: #FF40FF;
    border-color:#FF40FF;

  }
  .part{
    color: #0098A2;
    background: #0098A2;
    border-color:#0098A2;

  }
  .offline{
    color: #D2D2D2;
    background: #D2D2D2;
    border-color:#D2D2D2;

  }
  .map{
    width: 100%;
    height: 750px;
  }
  .two-button{
    position:absolute;
    left:20px;
    top:80%;
    z-index: 19;

  }
  .map-button{
    float:left;

    border-radius:10px;
    cursor:pointer;
  }
  .earth-button{
    float:left;
    border-radius:10px;
    margin-left: 20px;
    cursor:pointer;
  }
  .button-span{
    color: #FFFFFF;
    text-align:center;
    display:block;
    position: relative;
    top:50%;
    transform:translateY(-50%);
    font-size: 22px;
  }
  .anchorBL{
    display: none !important;
    height:0 !important;
  }
  .BMap_scaleCtrl{
    display: block !important;
    height:23px !important;
  }
  .pos_fixed
  {
    position:absolute;
    top:20px;
    right:20px;
    z-index: 19;
    width:560px;
    height:59px;

  }
</style>