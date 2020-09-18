<template>
    <div>
      <a-input-search style="margin-bottom: 8px" placeholder="搜索站点" @change="onChange" />
      <div class="a-tree-scroll">
        <a-tree
          :tree-data="treeData"
          :expanded-keys="expandedKeys"
          :auto-expand-parent="autoExpandParent"
          @expand="onExpand"
          @select = "onSelect"
          :selected-keys="selectedKeys"
        >
          <template slot="title" slot-scope="{ title,status }">
            <div v-if="status!=null" :style="{background:'url('+bgImgs[status]+') no-repeat center',width:'16px',height:'100%',float:'left'}">
            </div>
            <span v-if="title.indexOf(searchValue) > -1" style="float: left;margin-left: 4px">
              {{ title.substr(0, title.indexOf(searchValue)) }}
              <span style="color: #f50">{{ searchValue }}</span>
              {{ title.substr(title.indexOf(searchValue) + searchValue.length) }}
            </span>
            <span v-else  style="float: left;margin-left: 4px">{{ title}}</span>
          </template>
        </a-tree>
      </div>
    </div>
</template>

<script>
  import {getAction} from "@/api/manage"
  import {dataDictMixin} from "../../mixin/dataDictMixin";
    export default {
        name: "SitesTree",
        mixins:[dataDictMixin],
        props:{
          bgImgs:{},
          siteType:{
            type:String,
            default:'0'
          }
        },
        data(){
            return{
              treeDataSource:[],
              treeData:[],
              expandedKeys:[],
              autoExpandParent:true,
              searchValue:'',
              companyInfos:{},
              selectedKeys:[],
              siteInfos:[]
            }
        },
        methods:{
          init() {
            let _this = this;
            let param  = {
              companyIds :_this.$store.getters.userInfo.companyIds.join(','),
              siteType: this.siteType//废水
            };
            //先把所有的数据都查询出来--后台需要重写的
            getAction("/psoam/companyInfo/queryCompanySite",param).then((res) => {
              console.log(res)
              if (res.success) {
                _this.dealSiteData(res.result);
              }
            });
          },
          dealSiteData(siteInfos){
            let siteSource = [];
            let _this =this;
            _this.siteInfos = siteInfos;
            siteInfos.forEach((e)=>{
              if(siteSource[e.area])
                siteSource[e.area].push(e);
              else
                siteSource[e.area]=[{...e}]
            });
            let result = [];
            Object.keys(siteSource).map(key => {
              let siteInfo = [];
              _this.companyInfos={};
              siteSource[key].forEach(e=>{
                if(siteInfo[e.companyId])
                  siteInfo[e.companyId].push({key:e.mn,title:e.siteName,scopedSlots: { title: 'title' },status:e.status});
                else
                {
                  siteInfo[e.companyId]=[{key:e.mn,title:e.siteName,scopedSlots: { title: 'title' },status:e.status}];
                  _this.companyInfos[e.companyId] = e.companyName;
                }
              });
              let childrens = [];
              Object.keys(siteInfo).map(companyId=>{
                childrens.push({
                  key:companyId,
                  title: _this.companyInfos[companyId],
                  scopedSlots: { title: 'title' },
                  children:siteInfo[companyId],
                  selectable:false,
                })
              });
              result.push({
                key:key,
                title:_this.getAreaByCode(key,[2]),
                scopedSlots: { title: 'title' },
                selectable:false,
                children:childrens
              })
            });
            _this.treeData = result;
            console.log('默认全部打开');
            _this.onChange({target:{value:''}});//默认全部打开
            _this.selectedKeys = [result[0].children[0].children[0].key];
            console.log(_this.selectedKeys)
            this.selectChange();
          },
          onChange(e) {
            this.expandedKeys = [];
            const value = e.target.value;
            this.find(this.treeData,value);
            console.log(this.expandedKeys,value);

            this.searchValue= value;
            this.autoExpandParent= true

          },
          find(treeDate,val){

            let _this = this;
            treeDate.forEach(e=>{
              console.log(e);
              if(e.children){
                if(this.find(e.children,val)){
                  console.log(e.key);
                  _this.expandedKeys.push(e.key);
                  return true
                }

              }
              if (e.title.indexOf(val) > -1) {
                console.log(e.key);
                _this.expandedKeys.push(e.key);
                return true
              }
            })
          },
          onExpand(expandedKeys) {
            this.expandedKeys = expandedKeys;
            this.autoExpandParent = false;
          },

          onSelect(val){
            this.selectedKeys = val;
            this.selectChange();
          },
          selectChange(){
            let siteInfo;
            for(let i=0;i<this.siteInfos.length;i++){
              if(this.selectedKeys[0]===this.siteInfos[i].mn){
                siteInfo = this.siteInfos[i];
                break;
              }
            }
            this.$emit("selectChange",siteInfo);
          }
        },
        created() {
          this.init()
        }
    }
</script>

<style scoped>
  .a-tree-scroll{
    height: 100%;
    overflow: auto;
  }
</style>