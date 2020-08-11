<template>
    <div class="left-module">
        <city-pollution-sources></city-pollution-sources>
        <major-pollutant-analysis></major-pollutant-analysis>
    </div>
</template>

<script>
    import  CityPollutionSources from '../ecosystem/CityPollutionSources'
    import MajorPollutantAnalysis from "../ecosystem/MajorPollutantAnalysis";
    export default {
        name: "LeftModile",
        components:{
            CityPollutionSources,
            MajorPollutantAnalysis
        }

    }
</script>

<style scoped>

    .left-module{
        position: absolute;
        top: 10%;
        width: 25%;
        height: 90%;

    }
</style>
