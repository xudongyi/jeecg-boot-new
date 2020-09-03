<template>
  <div class="global-search-wrapper" style="width: 300px">
    <a-auto-complete
      class="global-search"
      size="large"
      style="width: 100%"
      :placeholder="placeholder"
      option-label-prop="title"
      @search="handleSearch"
      @select="onSelect"
      :value="value"
    >
      <template slot="dataSource">
        <a-select-option v-for="item in dataSource" :key="item.key" :title="item.key">
          <span className="global-search-item-count">{{item.value}}</span>
        </a-select-option>
      </template>
      <a-input>
        <a-button
          slot="suffix"
          style="margin-right: -12px"
          class="search-btn"
          size="large"
          type="primary"
        >
          <a-icon type="search" />
        </a-button>
      </a-input>
    </a-auto-complete>
  </div>
</template>
<script>
  export default {
    name: "AutoSelect",
    props:{
      placeholder:{
        type:String,
        default:'input here',
      },
      dataList:{
        type:Array,
      }
    },
    data() {
      return {
        dataSource: this.dataList,
        value :''
      };
    },
    methods: {
      onSelect(value) {
        this.$emit('select',value)
        //输入框 显示公司名称
        this.value = this.dataSource.find(item=>item.key===value).value;
      },

      handleSearch(value) {
          //筛选dataSource
        return this.dataSource.filter(item => item.value.indexOf(value)>=0)
      },



    }

  }
</script>

<style>
  .global-search-wrapper {
    padding-right: 50px;
  }

  .global-search {
    width: 100%;
  }

  .global-search.ant-select-auto-complete .ant-select-selection--single {
    margin-right: -46px;
  }

  .global-search.ant-select-auto-complete .ant-input-affix-wrapper .ant-input:not(:last-child) {
    padding-right: 62px;
  }

  .global-search.ant-select-auto-complete .ant-input-affix-wrapper .ant-input-suffix button {
    border-top-left-radius: 0;
    border-bottom-left-radius: 0;
  }

  .global-search-item {
    display: flex;
  }

  .global-search-item-desc {
    flex: auto;
    text-overflow: ellipsis;
    overflow: hidden;
  }

  .global-search-item-count {
    flex: none;
  }
</style>