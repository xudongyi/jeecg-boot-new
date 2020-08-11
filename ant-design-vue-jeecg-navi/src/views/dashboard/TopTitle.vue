<template>
  <div class="TopTitle">
    <div class="container">
      <span class="title">{{title}}</span>
    </div>

  </div>
</template>

<script>
    export default {
        name: "TopTitle",
        props:{
          title:{
            type:String,
            default:'生态环境大数据及智慧环保监管平台',
          }
        }
    }
</script>

<style scoped>
  .TopTitle{
    height: 10%;
    width: 100%;
  }
  .container {
    flex-grow: 1;
    margin: 0 auto;
    position: relative;
    text-align: center;
    padding: 4.5rem 1.5rem;
  }
  .title {
    color: #ffffff;
    font-size: 2rem;
    font-weight: 600;
    letter-spacing:0.4rem;

  }
</style>