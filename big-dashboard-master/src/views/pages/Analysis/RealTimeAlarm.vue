<template>
    <div class="real-time-alarm">
        <div class="subtitle-gradient sources-title" style="height: 7.6923%">
            <span :style="titleLineHeight">实时报警信息</span>
        </div>
        <div class="alarm-infos" >
            <div v-for="item in alarmInfos" :key="item.id" class="alarmItem">
                <div class="alarm-sign">
                   <img :src="require('@/assets/analysis/icon_alarm.png')" style="height: 70%">
                    <div style="color:rgba(255,56,56,1);font-weight:300;" :style="fontsize">超标</div>
                </div>
                <div  :style="fontsize" style="color: white;padding-top:5px;font-weight:400; " >
                {{item.area}}[{{item.type}}]小时折算均值{{item.value}}于{{item.time}}超出超标上限({{item.standard}})
                </div>
            </div>
        </div>

    </div>
</template>

<script>
    //实时报警信息
    export default {
        name: "RealTimeAlarm",
        data(){
            return{
                alarmInfos:[
                    {   id:1,
                        area:'三闸纺织一号排气孔',
                        type:'烟尘',
                        value:'55.159',
                        time:'2020-05-22 15:00:46',
                        standard:'20.0'
                    },
                    {
                        id:2,
                        area:'三闸纺织一号排气孔',
                        type:'烟尘',
                        value:'55.159',
                        time:'2020-05-22 15:00:45',
                        standard:'20.0'
                    },
                    {
                        id:3,
                        area:'钧鼎电子进水口',
                        type:'总磷',
                        value:'6.003',
                        time:'2020-05-21 09:00:46',
                        standard:'4.0'
                    },
                    {
                        id:4,
                        area:' 工业园区进水口',
                        type:'总磷',
                        value:'10.11',
                        time:'2020-05-18 11:00:40',
                        standard:'4.0'
                    },
                    {
                        id:5,
                        area:' 工业园区进水口',
                        type:'总磷',
                        value:'10.11',
                        time:'2020-05-18 11:00:40',
                        standard:'4.0'
                    },
                    {
                        id:6,
                        area:' 工业园区进水口',
                        type:'总磷',
                        value:'10.11',
                        time:'2020-05-18 11:00:40',
                        standard:'4.0'
                    }


        ]
            }
        },
        computed:{
            titleLineHeight() {
                return this.$store.getters.titleFont
            },
            fontsize(){
               return {
                   'font-size':14*this.$store.getters.scale+'px',
               }
            },
        }
    }
</script>

<style scoped>
.real-time-alarm{
    height: 65%;
    width: 100%;
}
.alarm-infos{
    height: 90%;
    width: 100%;
    padding-top: 2%;
}

.alarmItem{
    height:14%;
    width:95%;
    margin:1% 5% 0 0 ;
    border-bottom: 2px solid #10163d;

}
.alarm-sign{
    height: 100%;
    width: 20%;
    float: left;
    text-align: center;
}
.alarmItem>span{
    color:white;
    float: left;
    width: 80%;
}


</style>
