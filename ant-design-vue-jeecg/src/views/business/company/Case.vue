<template>
  <div>
    <a-descriptions >
      <a-descriptions-item label="申报人">
        张三
      </a-descriptions-item>
      <a-descriptions-item label="申报时间">
        1810000000
      </a-descriptions-item>
    </a-descriptions>



      <a-form-item label="不通过原因：">
        <a-textarea placeholder="Basic usage" :rows="2" />
      </a-form-item>
      <a-descriptions >
        <a-descriptions-item label="审核人">
          张三
        </a-descriptions-item>
        <a-descriptions-item label="审核时间">
          1810000000
        </a-descriptions-item>
      </a-descriptions>

  </div>
</template>

<script>
  //审批的脚部组件
  export default {
    name: "AuditFooter"
  }
</script>

<style scoped>

</style>