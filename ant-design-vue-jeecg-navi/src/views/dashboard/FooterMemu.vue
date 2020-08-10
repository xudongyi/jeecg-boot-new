<template>
    <div class="footer">
      <div class="memu-button memu-shape1" :style="marginStyle.Integratedm" @mouseover="mouseOver('Integratedm')" @mouseleave="mouseLeave('Integratedm')">
          <icon-hover ref="Integratedm" url="memuIcon/icon_Integratedm_default.png"  :width="36" :height="42"
                      overUrl="memuIcon/icon_Integratedm_selected.png" :overWidth="42" :overHeight="47" class="icon-memu"></icon-hover>
        <span class="memu-title">综合业务管理</span>
      </div>
      <div class="memu-button memu-shape2" :style="marginStyle.solidwaste"  @mouseover="mouseOver('solidwaste')" @mouseleave="mouseLeave('solidwaste')">
        <icon-hover  ref="solidwaste" url="memuIcon/icon_solidwaste_default.png"  :width="36" :height="42"
                    overUrl="memuIcon/icon_solidwaste_selected.png" :overWidth="42" :overHeight="47" class="icon-memu"></icon-hover>
        <span class="memu-title">固（危）废管理</span>
      </div>
      <div class="memu-button memu-shape3" :style="marginStyle.ecology"  @mouseover="mouseOver('ecology')" @mouseleave="mouseLeave('ecology')">
        <icon-hover  ref="ecology" url="memuIcon/icon_ecology_default.png"  :width="36" :height="42"
                    overUrl="memuIcon/icon_ecology_selected.png" :overWidth="42" :overHeight="47" class="icon-memu"></icon-hover>
        <span class="memu-title">生态环境监控</span>
      </div>
      <div class="memu-button memu-shape4" :style="marginStyle.pollutions"  @mouseover="mouseOver('pollutions')" @mouseleave="mouseLeave('pollutions')">
        <icon-hover  ref="pollutions" url="memuIcon/icon_pollutions_default.png"  :width="36" :height="42"
                    overUrl="memuIcon/icon_pollutions_selected.png" :overWidth="42" :overHeight="47" class="icon-memu"></icon-hover>
        <span class="memu-title">污染源监控</span>
      </div>
      <div class="memu-button memu-shape5" :style="marginStyle.emergency"  @mouseover="mouseOver('emergency')" @mouseleave="mouseLeave('emergency')">
        <icon-hover  ref="emergency" url="memuIcon/icon_emergency_default.png"  :width="40" :height="42"
                    overUrl="memuIcon/icon_emergency_selected.png" :overWidth="45" :overHeight="47" class="icon-memu"></icon-hover>
        <span class="memu-title">环境应急管理</span>
      </div>
      <div class="memu-button memu-shape6" :style="marginStyle.traceability"  @mouseover="mouseOver('traceability')" @mouseleave="mouseLeave('traceability')">
        <icon-hover  ref="traceability" url="memuIcon/icon_traceability_default.png"  :width="36" :height="42"
                    overUrl="memuIcon/icon_traceability_selected.png" :overWidth="42" :overHeight="47" class="icon-memu"></icon-hover>
        <span class="memu-title">扩散溯源分析</span>
      </div >
      <div class="memu-button memu-shape7" :style="marginStyle.visualization"  @mouseover="mouseOver('visualization')" @mouseleave="mouseLeave('visualization')">
        <icon-hover  ref="visualization" url="memuIcon/icon_visualization_default.png"  :width="36" :height="42"
                    overUrl="memuIcon/icon_visualization_selected.png" :overWidth="42" :overHeight="47" class="icon-memu"></icon-hover>
        <span class="memu-title">数据可视化</span>
      </div>
    </div>
</template>

<script>
  import IconHover from "./IconHover";
    export default {
        name: "footerMemu",
      components: {IconHover},
        props:{
          scale:{
            default:1
          }
        },
      data(){
        return {
          marginLength:0,
          marginStyle:{
            Integratedm:{},
            solidwaste:{},
            ecology:{},
            pollutions:{},
            emergency:{},
            traceability:{},
            visualization:{},
          },
          topMargin:{
            Integratedm:'-10px',
            solidwaste:'30px',
            ecology:'54px',
            pollutions:'64px',
            emergency:'54px',
            traceability:'30px',
            visualization:'-10px',
          }
        }
      },
      //计算传过来的属性
      computed: {
        isShow() {
          return this.scale;
        }
      },
        watch:{
          isShow(){
            console.log('marginStyle',this.scale)
            let height =  1824*this.scale-1120;
            console.log('height',height)
            this.marginLength = height/14;
            this.marginStyle.default={
                'margin-left': this.marginLength+'px',
                'margin-right': this.marginLength+'px'
            }
            this.marginStyle.Integratedm =  this.marginStyle.default;
            this.marginStyle.solidwaste =  this.marginStyle.default;
            this.marginStyle.ecology =  this.marginStyle.default;
            this.marginStyle.pollutions =  this.marginStyle.default;
            this.marginStyle.emergency =  this.marginStyle.default;
            this.marginStyle.traceability =  this.marginStyle.default;
            this.marginStyle.visualization =  this.marginStyle.default;
          }
        },
      methods:{
        mouseOver: function(key){
          this.marginStyle[key]={
            width:'168px',
            height:'168px',
            'margin-left':  (this.marginLength-5)+'px',
            'margin-right': (this.marginLength-5)+'px',
            'margin-top':this.topMargin[key],
             color:'#ffe63d',
            border:'1px solid #ffe63d'
          };
          this.$refs[key].mouseOver();
        },
        mouseLeave: function (key) {
          this.marginStyle[key] =  this.marginStyle.default;
          this.$refs[key].mouseLeave();

        },
      }

    }
</script>

<style scoped>
  .footer{
    width: 95%;
    height: 30%;
    position: absolute;
    top:70%;
    left:2.5%;
    text-align: center;
  }
  .icon-memu{
    margin: auto;
    margin-top: 25px;
  }
  .memu-title{
    font-size: 16px;
    line-height: 80px;
  }
  .memu-button{
    background: linear-gradient(rgba(174, 228, 251,0.5), rgba(17, 149, 255, 0.5));
    float: left;
    width:158px;
    height:158px;
    cursor:pointer;
    border:1px solid #01b9f4;
    -webkit-box-reflect: below 10px -webkit-linear-gradient(transparent 20%,rgba(0,0,0,.3));
    color: #FFFFFF;
  }
  .memu-shape1{
    display: inline-block;
    transform: rotate(15deg) skew(15deg);
  }
  .memu-shape2{
    display: inline-block;
    transform: rotate(10deg) skew(10deg);
    margin-top: 2.5rem;
  }
  .memu-shape3{
    display: inline-block;
    margin-top: 4rem;
    transform: rotate(5deg) skew(5deg);
  }
  .memu-shape4{
    display: inline-block;
    margin-top: 4.6rem;

  }
  .memu-shape5{
    display: inline-block;
    margin-top: 4rem;
    transform: rotate(-5deg) skew(-5deg);
  }
  .memu-shape6{
    display: inline-block;
    margin-top: 2.5rem;
    transform: rotate(-10deg) skew(-10deg);
  }
  .memu-shape7{
    display: inline-block;
    transform: rotate(-15deg) skew(-15deg);
  }

</style>