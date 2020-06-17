<template>
  <div>
  <company-apply-list :company-id="companyId"
                      :from-table="fromTable" @applyDetail = "applyDetail"  @toDetail="latestDetail" @toApply="apply"
                      v-if="!showDetail"></company-apply-list>
  <product-material-apply-list v-else :company-id="companyId" :isApply="isApply"> </product-material-apply-list>
  <companyApply-modal ref="applyInfoForm" ></companyApply-modal>
  </div>
</template>

<script>
  import CompanyApplyList from "./modules/CompanyApplyList"
  import {queryLatestArchivedData} from "../../requestAction/request";
  import ProductMaterialApplyList from "./modules/ProductMaterialApplyList";
  import CompanyApplyModal from "./modules/childModules/CompanyApplyModal";
    export default {
      name: "ProductMaterialApply",
      components: {
        CompanyApplyList,
        CompanyApplyModal,
        ProductMaterialApplyList
      },
      data() {
        return {
          fromTable: "company_product_material",
          companyId: this.$store.getters.userInfo.companyIds[0],
          showDetail: false,
          isApply:false
        }
      },
      methods: {
        //详情
        latestDetail() {
          //查询详情数据
          this.showDetail = true;
        },
        //新增申请
        apply() {

          //查询详情数据
          this.showDetail = true;
          this.isApply = true;

        },
        applyDetail(record) {
          console.log(record);
          //查询详情数据
          this.$refs.applyInfoForm.detail(record);

        }


      }
    }
</script>

<style scoped>

</style>