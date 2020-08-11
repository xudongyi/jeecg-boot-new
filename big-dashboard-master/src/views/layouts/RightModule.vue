<template>
    <div class="right-module">
        <air-quality></air-quality>
        <counties-air-quality-times></counties-air-quality-times>
    </div>
</template>

<script>
    import AirQuality from "../ecosystem/AirQuality";
    import CountiesAirQualityTimes from "../ecosystem/CountiesAirQualityTimes";
    export default {
        name: "RightModule",
        components:{AirQuality,CountiesAirQualityTimes}
    }
</script>

<style scoped>
    .right-module{
        position: absolute;
        top:10%;
        right: 0;
        width: 25%;
        height: 90%;
    }
</style>
