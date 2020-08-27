<template>
    <div class="analysis-center">
       <annual-discharge></annual-discharge>
        <main-pollutant></main-pollutant>
        <online-status-companies></online-status-companies>
    </div>
</template>

<script>
    import AnnualDischarge from "./AnnualDischarge";
    import MainPollutant from "./MainPollutant";
    import onlineStatusCompanies from "./onlineStatusCompanies";
    export default {
        name: "AnalysisCenter",
        components:{AnnualDischarge,MainPollutant,onlineStatusCompanies},

    }
</script>

<style scoped>
    .analysis-center{
        position: absolute;
        top: 10%;
        left: 25%;
        width: 50%;
        height: 90%;
    }

</style>
