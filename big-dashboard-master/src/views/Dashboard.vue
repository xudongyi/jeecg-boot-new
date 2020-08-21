<template>
    <div class="wrapper">
        <div :style="marginStyle" >
            <div style="position: relative;">
                <div class="container" :style="transformStyle">
                    <top-title :title.sync="title" currView="home"></top-title>
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

        watch: {},

        methods: {

        },

        destroyed() {

        }

    };
</script>

<style lang="scss">








</style>
