<template>
    <company-supervisory-monitor-list ref="modalList" :company-id="companyid" role="monitor" listType=1></company-supervisory-monitor-list>
</template>

<script>
  import CompanySupervisoryMonitorList from '../oneCompayOneRecord/CompanySupervisoryMonitorList'

    export default {
      name: "SupervisoryMonitorApply",
      components: {CompanySupervisoryMonitorList},
      computed: {},
      data(){
        return{
          companyid:this.$store.getters.userInfo.companyIds.join(",")
        }
      },
      methods:{

      },
      mounted()
      {
        this.$refs.modalList.columns = [{
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
            }
          },
          {
            title:'报告名称',
            align:"center",
            dataIndex: 'reportName'
          },
          {
            title:'报告类型',
            align:"center",
            dataIndex: 'reportType_dictText'
          },
          {
            title: '企业名称',
            align: "center",
            dataIndex: 'companyName'
          },
          {
            title:'报告日期',
            align:"center",
            dataIndex: 'reportDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'申报时间',
            align:"center",
            dataIndex: 'createTime',
          },
          {
            title:'填报人',
            align:"center",
            dataIndex: 'createName'
          },
          {
            title:'审核时间',
            align:"center",
            dataIndex: 'updateTime',
          },
          {
            title:'审核人',
            align:"center",
            dataIndex: 'updateName'
          },
          {
            title:'申报状态',
            align:"center",
            dataIndex: 'status_dictText'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            // fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ];
      },
      created(){
        if(this.companyid==null) {

          this.companyid = this.$store.getters.userInfo.companyIds
        }
      }
    }
</script>

<style scoped>

</style>