<template>

   <company-dynamic-supervision-list ref="modalList" :company-id="companyid" role="monitor" listType=1></company-dynamic-supervision-list>
</template>

<script>
  import CompanyDynamicSupervisionList from '../oneCompayOneRecord/routeView/CompanyDynamicSupervisionList'

  export default {
      name: "dynamicSupervisionApply",
      components: {CompanyDynamicSupervisionList},
      computed: {},
      data(){
        return{
          companyid:this.$store.getters.userInfo.companyIds.join(',')
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
            customRender: this.$refs.modalList.calcIndex
            },
            {
              title: '材料名称',
              align: "center",
              dataIndex: 'documentName'
            },
            {
              title: '材料类型',
              align: "center",
              dataIndex: 'documentType_dictText'
            },
            {
              title: '企业名称',
              align: "center",
              dataIndex: 'companyName'
            },
            {
              title: '申报年份',
              align: "center",
              dataIndex: 'reportYear'
            },
            {
              title:'申报时间',
              align:"center",
              dataIndex: 'createTime',
            },
            {
              title:'申报人',
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
        console.log(this.$store.getters.userInfo)
        if(this.companyid==null) {

          this.companyid = this.$store.getters.userInfo.companyIds
        }
      }

    }
</script>

<style scoped>

</style>