<template>
    <div class="wrapper">
        <div :style="marginStyle" >
            <div style="position: relative;">
                <div class="container" :style="transformStyle">
                    <top-title :title.sync="title" currView="analysis"></top-title>
                    <analysis-left></analysis-left>
                    <analysis-center></analysis-center>
                    <right-module></right-module>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
    import TopTitle from '@/views/layouts/TopTitle';
    import AnalysisCenter from "./Analysis/AnalysisCenter";
    import AnalysisLeft from "./Analysis/AnalysisLeft";
    import RightModule from "@/views/layouts/RightModule";

    export default {
        name: "comprehensiveAnalysis",
        data() {
            return {
                title:'这是标题',
            };
        },
        components: {
            TopTitle,
            AnalysisLeft,
            AnalysisCenter,
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
                let containerWidth = document.body.clientWidth || document.documentElement.clientWidth;
                let marginWidth = this.$original_width * this.$store.getters.scale;
                let marginHorizontal = 0;
                if (containerWidth > marginWidth) {
                    marginWidth = (containerWidth - marginWidth) / 2;
                    marginHorizontal = marginWidth;
                }
                return {
                    margin: `0px ${marginHorizontal}px`
                };
            }
        },

        watch: {

        },
        methods: {
            init() {
                this.initScale();
            },
            initScale() {
                //动态效果
                let containerWidth = document.body.clientWidth || document.documentElement.clientWidth;
                //直接从store中取
                // let containerHeight = document.body.clientHeight || document.documentElement.clientHeight;
                // console.log(containerWidth,containerHeight)
                // // sacle 缩放比例。
                // let scale1 = 1;
                // if (containerHeight < this.$original_height) {
                //     scale1 = containerHeight / this.$original_height;
                // }
                // let scale2 = 1;
                // if(containerWidth<this.$original_width){
                //     scale2 = containerWidth / this.$original_width;
                // }
                //
                //
                // this.$store.commit('SET_SCALE',Math.min(scale1,scale2));



                let marginWidth = this.$original_width * this.$store.getters.scale;
                this.marginHorizontal = 0;
                if (containerWidth > marginWidth) {
                    marginWidth = (containerWidth - marginWidth) / 2;
                    this.marginHorizontal = marginWidth;
                }
            },
            //只需要有一个
            // listenResize() {
            //     this.initScale();
            //     window.addEventListener('resize', debounce(() => {
            //         console.log('resize')
            //         this.initScale();
            //     }, 300));
            // },
        },

        destroyed() {

        }

    };
</script>

<style lang="scss">

</style>
