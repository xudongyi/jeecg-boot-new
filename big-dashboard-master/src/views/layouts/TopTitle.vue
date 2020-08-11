<template>
    <div class="title" >
       <!-- 左边的菜单-->
        <div class="left-z">
            <span class="icon font_family" :style="adaptiveIcon" @click="home">&#xe610;</span>
            <a-button  ghost class="skew-button" :style="adaptiveButton" @click="toEnv"><span>生态环境</span></a-button>
            <a-button  ghost class="skew-button" :style="adaptiveButton" @click="toPollution"><span>污染源</span></a-button>
            <a-button  ghost class="skew-button" :style="adaptiveButton" @click="toEnvZ"><span>环境质量</span></a-button>
        </div>
        <!-- 右边的菜单-->
        <div class="right-z">
            <span class="icon-right font_family" :style="adaptiveIcon" @click="close">&#xe611;</span>
            <span class="icon-right font_family" :style="adaptiveIcon"  @click="fullScreen">&#xe612;</span>
            <a-button  ghost class="skew-button-right" :style="adaptiveButton" @click="risk"><span>风险监管</span></a-button>
            <a-button  ghost class="skew-button-right" :style="adaptiveButton" @click="waste"><span>危险废物</span></a-button>
            <a-button  ghost class="skew-button-right" :style="adaptiveButton" @click="analysis"><span>综合分析</span></a-button>

        </div>
    </div>
</template>

<script>

    import '../../components/icon/iconfont.css'
    import titleBg from '../../assets/title.png'

    export default {
        name: "TopTitle",
        //暂时title用不上  图片自动处理
        props: ['title'],
        data(){
          return{
              titleBg:titleBg
          }
        },
        computed:{
            adaptiveIcon(){
                return {
                    fontSize:this.$original_height*this.$store.getters.scale/36+'px',
                }
            },
            adaptiveButton(){
                return {
                    height:this.$original_height*this.$store.getters.scale/36+'px',
                    'font-size':this.$original_height*this.$store.getters.scale/78+'px',

                }
            }
        },
        methods:{
            home(){
                this.$message.success("home")
            },
            fullScreen(){
                var el = document.documentElement;
                var rfs = el.requestFullScreen || el.webkitRequestFullScreen || el.mozRequestFullScreen || el.msRequestFullscreen;
                if(typeof rfs != "undefined" && rfs) {
                    rfs.call(el);
                }
                return;
            },
            close(){
                this.$message.success("close")
            },
            toEnv(){
                this.$message.success("生态环境一张图")
            },
            toPollution(){
                this.$message.success("污染源一张图")
            },
            toEnvZ(){
                this.$message.success("环境质量一张图")
            },
            analysis(){
                this.$message.success("综合分析一张图")
            },
            waste(){
                this.$message.success("危险废物一张图")
            },
            risk(){
                this.$message.success("风险监管一张图")
            },
        }
    };
</script>

<style scoped lang="scss">

    .icon {
        color: #2AAEF2;
        margin-right: 1%;
        float:left;
        cursor:pointer;
    }
    .icon-right{
        color: #2AAEF2;
        margin-right: 1%;
        float:right;
        cursor:pointer;
    }
    .skew-button {
        transform: skewX(10deg);
        border-color:#2AAEF2;
        margin-right: 1%;
        margin-top:1% ;
        float:left;
        fontSize:'small' ;
        padding: 0  2%;
    }
    .skew-button > span { transform: skewX(-10deg); color: #2AAEF2}

    .skew-button-right {
        transform: skewX(-10deg);
        border-color:#2AAEF2;
        margin-right: 1%;
        margin-top:1% ;
        float:right;
        fontSize:'small' ;
        padding: 0  2%;
    }
    .skew-button-right > span { transform: skewX(10deg); color: #2AAEF2}
    .title {
        width: 100%;
        height: 9%;
        background: url("../../assets/title.png") no-repeat;
        background-size:cover
     }

    .left-z {
        position: absolute;
        width: 35%;
        height: 5%;
        /*background: url("../assets/left-z.png") no-repeat center;*/
        /*background-size: 100% 100%;*/
        top: 3%;
        left: 1%;
    }

    .right-z {
        position: absolute;
        width: 35%;
        height: 5%;

        /*background: url("../assets/right-z.png") no-repeat center;*/
        /*background-size: 100% 100%;*/
        top: 3%;
        right: 0.5%;
    }
</style>
