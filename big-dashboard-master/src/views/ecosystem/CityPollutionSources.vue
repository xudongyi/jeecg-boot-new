<template>
    <div class="pollution-sources">
        <div class="subtitle-gradient sources-title">
            <span :style="titleLineHeight">全市重点污染源统计</span>
        </div>
        <div class="message-module message-module-left">
            <span class="icon font_family" :style="iconFontSize">&#xe601;</span>
            <div style="float:left;width: 60%;height: 100%;margin-left: 2%">
                <p class="main-info" ><span :style="infoFontSize">{{pollutionSize}}</span><span :style="LabelFontSize">家</span></p>
                <p class="annotations-message" :style="messageFontSize">重点污染源企业</p>
            </div>
        </div>
        <div class="message-module">
            <span class="icon font_family" :style="iconFontSize">&#xe602;</span>
            <div style="float:left;width: 60%;height: 100%;margin-left: 2%">
                <p class="main-info"><span :style="infoFontSize">{{totalSize}}</span><span :style="LabelFontSize">家</span></p>
                <p class="annotations-message" :style="messageFontSize">在线企业</p>
            </div>

        </div>
        <div class="message-module-longer">
            <span class="icon font_family" :style="iconFontSize">&#xe605;</span>
            <div style="float:left;width: 60%;height: 100%;margin-left: 2%">
                <p class="main-info" ><span :style="infoFontSize">{{keyPollutantSize}}</span><span :style="LabelFontSize">家</span></p>
                <p class="annotations-message" :style="messageFontSize">重点排污企业在线数量</p>
            </div>
        </div>
        <div class="message-module message-module-left">
            <span class="icon font_family" :style="iconFontSize">&#xe606;</span>
            <div style="float:left;width: 60%;height: 100%;margin-left: 2%">
                <p class="main-info" style="color:#EDCE46;"><span :style="infoFontSize">{{wasteWater}}</span><span :style="LabelFontSize">家</span></p>
                <p class="annotations-message" :style="messageFontSize" style="color:#EDCE46;">废水企业</p>
            </div>
        </div>
        <div class="message-module">
            <span class="icon font_family" :style="iconFontSize">&#xe609;</span>
            <div style="float:left;width: 60%;height: 100%;margin-left: 2%">
                <p class="main-info" style="color:#0DDE79;"><span :style="infoFontSize">{{waterMonitor}}</span><span :style="LabelFontSize">家</span></p>
                <p class="annotations-message"  :style="messageFontSize" style="color:#0DDE79;">废水监控点</p>
            </div>
        </div>
        <div class="message-module message-module-left">
            <span class="icon font_family" :style="iconFontSize">&#xe608;</span>
            <div style="float:left;width: 60%;height: 100%;margin-left: 2%">
                <p class="main-info" style="color:#08C7F1;"><span :style="infoFontSize">{{wasteGas}}</span><span :style="LabelFontSize">家</span></p>
                <p class="annotations-message" :style="messageFontSize" style="color:#08C7F1;">废气企业</p>
            </div>
        </div>
        <div class="message-module">
            <span class="icon font_family" :style="iconFontSize">&#xe60a;</span>
            <div style="float:left;width: 60%;height: 100%;margin-left: 2%">
                <p class="main-info" style="color:#EC903E;"><span :style="infoFontSize">{{gasMonitor}}</span><span :style="LabelFontSize">家</span></p>
                <p class="annotations-message" :style="messageFontSize" style="color:#EC903E;">废气监控点</p>
            </div>
        </div>
    </div>
</template>

<script>
    import '../../components/icon/iconfont.css'
    export default {
        name: "CityPollutionSources",
        data(){
            return {
                pollutionSize:21,
                totalSize:79,
                keyPollutantSize:10,
                wasteWater:58,
                waterMonitor:138,
                wasteGas:8,
                gasMonitor:13,
            }
        },
        computed:{
            titleLineHeight(){
                return{
                    'line-height':this.$original_height*this.$store.getters.scale/22+'px',
                    'font-size':this.$original_height*this.$store.getters.scale/45+'px',
                }
            },
            iconFontSize(){
                return{
                    'font-size':this.$original_height*this.$store.getters.scale/20+'px',
                }
            },
            infoFontSize(){
                return{
                    'font-size':this.$original_height*this.$store.getters.scale/30+'px',
                }
            },
            LabelFontSize(){
                return{
                    'font-size':this.$original_height*this.$store.getters.scale/40+'px',
                    'margin-left':this.$original_height*this.$store.getters.scale/80+'px',
                }
            },
            messageFontSize(){
                return{
                    'font-size':this.$original_height*this.$store.getters.scale/70+'px',
                }
            }
        }

    }
</script>

<style scoped>
    .pollution-sources{
        height: 50%;
        width: 100%;
    }
    .message-module{
        width:45%;
        height:18.5%;
        background: url("../../assets/module/border01.png") no-repeat;
        background-size:cover;
        margin-top: 3% ;
        margin-left: 3% ;
        float: left;
    }
    .message-module-left{
        margin-right: 3% ;
    }
    .message-module-longer{
        width:96%;
        height:18.5%;
        background: url("../../assets/module/border02.png")no-repeat;
        background-size:cover;
        margin:3% 0 0 3% ;
        float: left;
    }
    .icon{
        color: #01B4FF;
        margin:1% 0 0 9% ;
        float:left
    }
    .main-info{
        width:100%;
        height:60%;
        /*font-family:Microsoft YaHei;*/
        font-weight:400;
        color:rgba(255,255,255,1);
        margin: 2% 0 0 1%;
    }

    .annotations-message{
        width:100%;
        height:30%;
        font-family:Microsoft YaHei;
        font-weight:400;
        color:rgba(8,199,241,1);
        position:relative;
    }
</style>
