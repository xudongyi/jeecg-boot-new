<template>
    <div class="counties-air-quality-times">
        <div class="subtitle-gradient sources-title">
            <span :style="titleLineHeight">各区县空气质量实时报</span>
        </div>
        <dv-scroll-board :config="config" style="width:100%;height:90%" />
    </div>

</template>

<script>
    export default {
        name: "CountiesAirQualityTimes",
        data(){
            return {
                config:{
                    rowNum:7,
                    headerBGC:'#000127',
                    oddRowBGC:'#01153c',
                    evenRowBGC:'#000127',
                    header:['区县名称', 'AQI', '污染等级','PM2.5浓度'],
                    data:[
                        ['<span style="color:#08C7F1;">海安市</span>',56, '良', '32μg/m³'],
                        ['<span style="color:#08C7F1;">如东县</span>',45, '<span style="color:#00E400;">优</span>', '32μg/m³'],
                        ['<span style="color:#08C7F1;">如皋市</span>',67, '<span style="color:#FFFF00">良</span>', '32μg/m³'],
                        ['<span style="color:#08C7F1;">通州区</span>',21, '<span style="color:#00E400;">优</span>', '32μg/m³'],
                        ['<span style="color:#08C7F1;">港闸区</span>',77, '<span style="color:#FFFF00;">良</span>', '32μg/m³'],
                        ['<span style="color:#08C7F1;">崇川区</span>',34, '<span style="color:#00E400;">优</span>', '32μg/m³'],
                        ['<span style="color:#08C7F1;">海门区</span>',52, '<span style="color:#FFFF00;">良</span>', '32μg/m³'],
                        ['<span style="color:#08C7F1;">启东市</span>',32, '<span style="color:#00E400;">优</span>', '32μg/m³'],
                    ],
                    align: ['center','center','center','center']
                }

            }
        },
        computed:{
            titleLineHeight(){
                return this.$store.getters.titleFont
            },
        }
    }
</script>

<style scoped>
    .counties-air-quality-times{
        height: 50%;
    }
</style>
