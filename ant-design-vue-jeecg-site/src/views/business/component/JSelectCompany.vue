<template>
  <j-select-biz-component :width="1000" v-bind="configs" v-on="$listeners"/>
</template>


<!-- 选择框组件，value 是逗号分隔拼接字符串  -->

<script>
  import JSelectBizComponent from '@/components/jeecgbiz/JSelectBizComponent'

  export default {
    name: 'JSelectCompany',
    components: { JSelectBizComponent },
    props: ['value'],
    data() {
      return {
        settings: {
          name: '公司',
          displayKey: 'companyName',
          returnKeys: [ 'id','companyName'],
          listUrl: '/cb/companyBase/baseInfolist',
          queryParamCode: 'companyName',
          queryParamText: '公司名称',
          valueKey:'id',
          columns: [
            { title: '公司名称', dataIndex: 'companyName', align: 'center', width: '30%', widthRight: '70%' },
            { title: '统一社会信用代码', dataIndex: 'socialCreditCode', align: 'center', width: '35%' },
            { title: '企业法人', dataIndex: 'corporate', align: 'center', width: '25%' }
          ]
        }
      }
    },
    computed: {
      configs() {
        return Object.assign({ value: this.value }, this.settings, this.$attrs)
      }
    }
  }
</script>

<style lang="less" scoped></style>