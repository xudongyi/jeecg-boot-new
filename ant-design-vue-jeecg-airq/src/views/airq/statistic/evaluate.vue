<template>
  <a-layout>
    <a-layout-sider theme="light" width="260">
      <a-form :form="form">
        <a-form-item label="所属区域" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <area-link-select type="cascader" v-decorator="['area']"  @change="selectChangeArea" :disabled="disableSubmit" placeholder="请选择省市区"/>
        </a-form-item>
        <a-form-item label="站点名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-search type="list" v-decorator="['siteName']" placeholder="查找站点" :disabled="disableSubmit" @change="onChange">
          </a-input-search>
        </a-form-item>

      </a-form>
      <div>
        <a-tree
          :checkable="true"
          :expanded-keys="expandedKeys"
          :auto-expand-parent="autoExpandParent"
          :treeData="treeData"
          @check="onCheck"
          @select="onSelect"
          @expand="onExpand"
        >
          <template slot="title" slot-scope="{ title }">
                  <span v-if="title.indexOf(searchValue) > -1">
                    {{ title.substr(0, title.indexOf(searchValue)) }}
                    <span style="color: #f50">{{ searchValue }}</span>
                    {{ title.substr(title.indexOf(searchValue) + searchValue.length) }}
                  </span>
            <span v-else>{{ title }}</span>
          </template>
        </a-tree>
      </div>
    </a-layout-sider>
    <a-layout-content>
    </a-layout-content>
  </a-layout>
</template>

<script>
  import {querySiteName} from "../../requestAction/request";

    export default {
        name: "evaluate",
      methods:{
        selectChangeArea(val){
          this.queryParam.area = val;
          this.selectChange()
        },
        selectChange(){
          let that = this;
          querySiteName({siteType: this.queryParam.siteType,area: this.queryParam.area}).then((res)=>{
            this.siteData =  res.result;
            let data = [];
            //省
            that.data.forEach((a)=>{
              //市
              let city = [];
              a.children.forEach((b)=>{
                let qu = [];
                //区
                b.children.forEach((c)=>{
                  let site = [];
                  res.result.forEach((e)=>{
                    if(c.key === e.area)
                      site.push({key:e.key,title:e.siteName})
                  });
                  if(site.length>0){
                    c.children = site;
                    qu.push(c)
                  }
                });
                if(qu.length>0){
                  b.children = qu;
                  city.push(b)
                }
              });
              if(city.length>0){
                a.children = city;
                data.push(a)
              }
            });
            that.treeData=data;
            console.log("!!!!!!",res);
            if(res.success)
              that.items = res.result;
          });


        },
      }
    }
</script>

<style scoped>

</style>