<template>
    <company-complaint-letter-list ref="modalList" :company-id="companyid" role="monitor" listType=1></company-complaint-letter-list>
</template>

<script>
  import CompanyComplaintLetterList from '../oneCompayOneRecord/routeView/CompanyComplaintLetterList'

    export default {
        name: "ComplaintLetterApply",
      components: {CompanyComplaintLetterList},
      data(){
        return{
          companyid:this.$store.getters.userInfo.companyIds.join(",")
        }
      },
      methods:{

      },
      mounted(){
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
            title:'投诉标题',
            align:"center",
            dataIndex: 'complaintTitle'
          },
          {
            title:'污染类型',
            align:"center",
            dataIndex: 'pollutionType_dictText'
          },
          {
            title:'企业名称',
            align:"center",
            dataIndex: 'companyName'
          },
          {
            title:'投诉日期',
            align:"center",
            dataIndex: 'complaintDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          // {
          //   title:'内容',
          //   align:"center",
          //   dataIndex: 'content',
          //   scopedSlots: {customRender: 'fileSlot'}
          // },
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