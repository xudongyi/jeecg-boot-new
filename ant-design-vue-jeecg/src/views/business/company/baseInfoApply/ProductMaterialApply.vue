<template>
  <div>
  <company-apply-list :company-id="companyId"
                      :from-table="fromTable" @applyDetail = "applyDetail"  @toDetail="latestDetail" @toApply="apply"
                      v-show="!showDetail" ref = "applyList"></company-apply-list>
  <product-material-apply-list v-if="showDetail" :company-id="companyId" :isApply="isApply" @reloadApply="reload"> </product-material-apply-list>
  <companyApply-modal ref="applyInfoForm" ></companyApply-modal>
  </div>
</template>

<script>
  import CompanyApplyList from "./modules/CompanyApplyList"
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
          //查询详情数据
          this.$refs.applyInfoForm.detail(record);
          //单个表比较
          this.$refs.applyInfoForm.compareDetail(this.fromTable);
        },
        reload(){
          this.$refs.applyList.reInit();
          this.showDetail=false;
        }

      }
    }
</script>

<style scoped>

</style>