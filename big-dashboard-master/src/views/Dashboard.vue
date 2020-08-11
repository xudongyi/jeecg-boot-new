<template>
    <div class="wrapper">
        <div :style="marginStyle" >
            <div style="position: relative;">
                <div class="container" :style="transformStyle">
                    <top-title :title.sync="title"></top-title>
                    <left-module></left-module>
                    <content-module></content-module>
                    <right-module></right-module>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
    import TopTitle from './layouts/TopTitle';
    import ContentModule from "./layouts/ContentModule";
    import LeftModule from "./layouts/LeftModule";
    import RightModule from "./layouts/RightModule";
    import {debounce} from "lodash";

    export default {
        name: "Dashboard",
        data() {
            return {
                marginHorizontal: 0,
                title:'这是标题',

            };
        },
        components: {
            TopTitle,
            LeftModule,
            ContentModule,
            RightModule,


        },
        created() {

        },
        mounted() {
            this.init();
        },
        computed: {
            transformStyle(){
                return {
                    height:this.$original_height*this.$store.getters.scale+'px',
                    width:this.$original_width*this.$store.getters.scale+'px',
                }
            },
            marginStyle: function () {
                return {
                    margin: `0px ${this.marginHorizontal}px`
                };
            }
        },

        watch: {},

        methods: {
            init() {
                this.listenResize();
            },
            initScale() {
                //动态效果
                let containerWidth = document.body.clientWidth || document.documentElement.clientWidth;
                let containerHeight = document.body.clientHeight || document.documentElement.clientHeight;
                console.log(containerWidth,containerHeight)
                // sacle 缩放比例。
                let scale1 = 1;
                if (containerHeight < this.$original_height) {
                    scale1 = containerHeight / this.$original_height;
                }
                let scale2 = 1;
                if(containerWidth<this.$original_width){
                    scale2 = containerWidth / this.$original_width;
                }


                this.$store.commit('SET_SCALE',Math.min(scale1,scale2));



                let marginWidth = this.$original_width * this.$store.getters.scale;

                this.marginHorizontal = 0;
                console.log(this.transformStyle)
                console.log(containerWidth,marginWidth)
                if (containerWidth > marginWidth) {
                    marginWidth = (containerWidth - marginWidth) / 2;
                    this.marginHorizontal = marginWidth;
                    console.log("marginHorizonta",this.marginHorizontal)
                }
            },
            listenResize() {
                this.initScale();
                window.addEventListener('resize', debounce(() => {
                    this.initScale();
                }, 300));
            },
        },

        destroyed() {

        }

    };
</script>

<style lang="scss">

    .wrapper {
        height: 100%;
        width: 100%;
        position: absolute;
    }

    .container {
        /*background-color: rgb(3, 12, 59);*/
        overflow: hidden;
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: url('../assets/bg.png')   no-repeat 50%;

    }


    .dashboard-bg-image {
        width: 100%;
        height: 100%;
        position: absolute;
        /*background-color: white;*/

    }

    .dashboard-container {
        position: relative;
        user-select: none;
        width: 100%;
        height: 100%;
        transform-origin: 0 0;
        box-shadow: 0 0 10px 0 rgba(0, 0, 0, .5);
        transition: all .3s linear;
        overflow: hidden;
    }

    .dashboard-error {
        position: absolute;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.7);
        z-index: 0;
        display: flex;
        align-items: center;
        justify-content: center;

        .dashboard-error-inner {
            color: #fff;
            font-size: 32px;
        }
    }


</style>
