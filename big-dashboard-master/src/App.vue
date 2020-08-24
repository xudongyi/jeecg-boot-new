<template>
    <div id="app">
        <router-view/>
    </div>
</template>
<script>

    import {debounce} from "lodash";

    export default {
        name: 'App',
        created(){
            this.listenResize();
        },
        methods:{
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
                console.log('scale',this.$store.getters.scale)
                this.$store.commit('SET_TITLEFONT',{
                    'line-height':this.$original_height*this.$store.getters.scale/22+'px',
                    'font-size':this.$original_height*this.$store.getters.scale/45+'px',
                });



            },
            listenResize() {
                this.initScale();
                window.addEventListener('resize', debounce(() => {
                    this.initScale();
                }, 300));
            },
        }
    }
</script>

<style lang="scss">
    #app {
        height: 100%;
        width: 100%;
        position: relative;
    }
</style>
