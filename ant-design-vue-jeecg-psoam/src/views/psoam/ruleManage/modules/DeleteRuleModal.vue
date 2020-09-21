<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <div style="width: 100%;height: 30px;">
        <span style="width: 15%;float: left;font-size: 16px;color: #000000;padding-left: 4px">策略执行对象:</span>
        <span style="width: 30%;float: left;font-size: 16px;color: #000000;padding-left: 4px">已选中 {{this.selectedKeys.length}} 个监测点</span>
      </div>
      <div style="width: 100%;height: 30px;margin-top: 5px">
        <span style="width: 25%;float: left;font-size: 16px;color: #000000;padding-left: 4px">待删除策略类型:</span>
        <span style="width: 10%;float: right;font-size: 16px;color: #000000;">
          <a-checkbox :indeterminate="indeterminate" :checked="checkAll" @change="onCheckAllChange">
            全选
          </a-checkbox>
        </span>
      </div>
      <div style="width:100%;height: 150px;border: 1px solid;margin-top: 10px;display: flex;flex-wrap: wrap;justify-content:space-around">
        <a-checkbox v-for="(item,index) in plainOptions" :key="item.value" :value="item.key" :checked="checkSingle[index]" style="width: 160px;margin-top: 20px" @change="onChange( index)">
          {{item.value}}</a-checkbox>
      </div>

    </a-spin>
    <template slot="footer">
      <a-button type="primary" @click="handleOk">确定</a-button>
      <a-button type="primary" @click="handleCancel">关闭</a-button>
    </template>
  </j-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import moment from 'moment'
  import {duplicateCheck } from '@/api/api'
  import {deleteAction} from "../../../../api/manage";

  export default {
    name: "DeleteRuleModal",
    components: {
      JDate,
      JDictSelectTag,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        selectedKeys:'',
        indeterminate: true,
        checkAll: false,
        checkSingle:{0:false,1:false,2:false,3:false,4:false,5:false,6:false,7:false},
        plainOptions:[{key:0,value:'超标报警(实时)'}, {key:1,value:'超标报警(小时)'},{key:2,value:'超标报警(日)'},{key:3,value:'离线报警'},
          {key:4,value:'设备报警'},{key:5,value: '量程报警'},{key:6,value: '定值报警'},{key:7,value:'数据异常报警'}],
        confirmLoading: false,
        url: {
          deleteBatch: "/wr/warnRule/deleteBatch",
        }
      }
    },
    created () {
    },
    methods: {
      onChange(val) {
        this.checkSingle[val] = !this.checkSingle[val];
        var count = 0;
        Object.keys(this.checkSingle).forEach(e=>{
          if (this.checkSingle[e]===true){
            count++;
          }
        });
        this.indeterminate = count !== 0;
      },
      onCheckAllChange(val) {
        Object.keys(this.checkSingle).forEach(e=>{
          this.checkSingle[e]=val.target.checked;
        });
        this.indeterminate = val.target.checked;
      },
      delete(selectedRowKeys){
        //console.log("idsssss",selectedRowKeys);
        this.visible = true;
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        var checkedList = [];
        Object.keys(this.checkSingle).forEach(e=>{
          if(this.checkSingle[e] === true){
            checkedList.push(e);
          }
        });
        if (checkedList.length <= 0) {
          this.$message.warning('请选择删除类型！');
          return;
        } else {
          var mnIds = "";
          for (var a = 0; a < this.selectedKeys.length; a++) {
            mnIds += this.selectedKeys[a] + ",";
          }
          var types = "";
          for (var a = 0; a < checkedList.length; a++) {
            types += checkedList[a] + ",";
          }
          console.log("llll",types)
          var that = this;
          this.$confirm({
            title: "确认删除",
            content: "是否删除选中数据?",
            onOk: function () {
              that.loading = true;
              deleteAction(that.url.deleteBatch, {ids: mnIds,types:types}).then((res) => {
                if (res.success) {
                  that.$message.success(res.message);
                  that.onClearSelected();
                  that.$emit('deleted')
                  that.close();
                } else {
                  that.$message.warning(res.message);
                }
              }).finally(() => {
                that.loading = false;
              });
            }
          });
        }
      },
      handleCancel () {
        this.close();
      },
      onClearSelected() {
        Object.keys(this.checkSingle).forEach(e=>{
          this.checkSingle[e]=false;
        });
      },
    },
    mounted(){

    },
  }
</script>
<style scoped>
</style>