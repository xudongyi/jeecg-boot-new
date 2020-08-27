<template>
    <div class="left-module">
        <major-emissions></major-emissions>
        <real-time-alarm></real-time-alarm>
    </div>
</template>

<script>
    import MajorEmissions from "./MajorEmissions";
    import RealTimeAlarm from "./RealTimeAlarm";
    export default {
        name: "AnalysisLeft",
        components:{
            MajorEmissions,RealTimeAlarm
        }
    }
</script>

<style scoped>

</style>
