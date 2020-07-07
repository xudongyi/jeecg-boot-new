<template>
  <div>
    <a-row type="flex" justify="space-around" align="top">
      <a-col :span="8">
        <a-card title="所有区域">
      <a-tree
          autoExpandParent
          :checkable="true"
          :checkStrictly = "false"
          :checkedKeys="checkedKeys"
          :treeData="treeData"
          @check="onCheck"
          @select="onSelect"
        />
        </a-card>
      </a-col>
      <a-col :span="4">
        <div style="height: 800px">
        <a-button-group style="top: 50%;">
          <a-button type="primary" @click="deleteArea" :disabled="disable"> <a-icon type="left" />删除 </a-button>
          <a-button type="primary" @click="addArea" :disabled="disable"> 新增<a-icon type="right" /> </a-button>
        </a-button-group>
        </div>
      </a-col>
      <a-col :span="8">
        <a-card title="已选区域" >
        <a-tree
          :defaultExpandAll="true"
          autoExpandParent
          :expanded-keys="expandedKeys"
          :checkable="true"
          :checkStrictly = "false"
          :checkedKeys="checkedKeys2"
          :treeData="treeData2"
          @check="onCheck2"
          @select="onSelect2"
        />
          </a-card>
      </a-col>
    </a-row>
  </div>
</template>
<script>
  import { getAction,postAction} from '@/api/manage'
  import Vue from "vue";

  export default {
    name :"AreaManager",
    data() {
      return {
        disable:true,
        treeDataSource:[],
        checkedKeys:[],
        treeData2:[],
        checkedKeys2:[],
        videoHight:'',
        targetKeys:[],
        expandedKeys:[],
        halfCheckedKeys:[],
        halfCheckedKeys2:[]
      };
    },
    computed: {
      treeData() {
        return this.handleTreeData(this.treeDataSource, this.targetKeys)
      }
    },
    created(){

      this.init();
      //监听高度
      // window.addEventListener('resize',this.getVideoHight)
    },
    mounted() {
      // this.getVideoHight();
    },
    methods: {
      init(){
        let _this = this;
        _this.checkedKeys = []
        _this.checkedKeys2 = []
        _this.expandedKeys = []
        _this.targetKeys=[]
        //先把所有的数据都查询出来
        getAction("/sys/sysArea/list",{}).then((res) => {
          console.log(res)
          if (res.success) {
            //预处理一下所有数据
            _this.treeDataSource =   this.dealAreaData(res);
          }
          //顺序问题
          getAction("/sys/sysArea/list",{active:'1'}).then((res) => {
            if (res.success) {
              //预处理一下所有数据
              _this.treeData2 =   this.dealAreaData(res);
              const province = res.result['86']
              Object.keys(province).map(key => {
                const city = res.result[key];
                _this.targetKeys.push(key);
                Object.keys(city).map(key2 => {
                  const qu = res.result[key2];
                  _this.targetKeys.push(key2);
                  Object.keys(qu).map(key3 => {
                    _this.targetKeys.push(key3);
                  })
                })
              })
              _this.checkedKeys = _this.targetKeys
              _this.checkedKeys2 = _this.targetKeys
              _this.expandedKeys = _this.targetKeys
              _this.disable=false
            }
          })

        })

      },
      // getVideoHight(){
      //
      //   this.videoHight =  this.$refs.video_area.Height+'px'
      //   console.log(this.$refs.video_area,this.videoHight)
      // },
      dealAreaData(res){
        let areaSource = []
        const province = res.result['86']
        Object.keys(province).map(key => {
          areaSource.push({key: key, title: province[key], children: []});
          const city = res.result[key];
          Object.keys(city).map(key2 => {

            areaSource[areaSource.length - 1].children.push({key: key2, title: city[key2], children: []});
            const qu = res.result[key2];
            Object.keys(qu).map(key3 => {
              let arrindex = areaSource[areaSource.length - 1].children.length - 1;
              areaSource[areaSource.length - 1].children[arrindex].children.push({key: key3, title: qu[key3]});
            })
          })
        })
        return areaSource
      },
      handleTreeData(data, targetKeys = []) {
        data.forEach(item => {
          item['disabled'] = targetKeys.includes(item.key)
          if(item.children) {
            this.handleTreeData(item.children, targetKeys)
          }
        })
        return data
      },
      onCheck(checkedKeys, info) {
        //修改选择数据
        this.checkedKeys = checkedKeys
        this.halfCheckedKeys= info.halfCheckedKeys

      },
      //同选择-暂时不做
      onSelect(selectedKeys) {
        // selectedKeys.forEach(e=>{
        //   //
        //   this.checkedKeys.push(e)
        // })
      },
      onCheck2(checkedKeys, info) {
        console.log(checkedKeys,info)

        this.checkedKeys2 = checkedKeys
        this.halfCheckedKeys2= info.halfCheckedKeys

      },
      //同选择-暂时不做
      onSelect2(selectedKeys) {
        // selectedKeys.forEach(e=>{
        //   //
        //   this.checkedKeys2.push(e)
        // })
      },
      addArea(){
        this.areaChange(this.checkedKeys,this.halfCheckedKeys)
      },
      deleteArea(){
        this.areaChange(this.checkedKeys2,this.halfCheckedKeys2)
      },
      areaChange(checkedKeys,halfCheckedKeys){
          this.disable=true;
          let _this = this;
          postAction("/sys/sysArea/change",{keys:[...checkedKeys ,...halfCheckedKeys]}).then((res)=>{
            if(res.success)
              this.$message.success(res.message)
            else
              this.$message.error(res.message)
          }).finally(() => {
            _this.init()
            Vue.ls.remove("sys_areas")
          })
      }
    },

  };
</script>
<style scoped>
  .tree-transfer .ant-transfer-list:first-child {
    width: 50%;
    flex: none;
  }
</style>