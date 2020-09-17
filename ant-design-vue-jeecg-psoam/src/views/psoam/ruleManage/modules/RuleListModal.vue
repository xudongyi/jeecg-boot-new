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
      <div style="width: 100%;height: 30px">
        <span style="width: 8%;float: left;font-size: 16px;color: #000000;padding-left: 4px">企业名称:</span>
        <span style="width: 28%;float: left;font-size: 16px;color: #000000;padding-left: 4px">{{this.record.companyName}}</span>
        <span style="width: 8%;float: left;font-size: 16px;color: #000000;padding-left: 4px">监控点名称:</span>
        <span style="width: 28%;float: left;font-size: 16px;color: #000000;padding-left: 4px">{{this.record.siteName}}</span>
        <span style="width: 8%;float: right;font-size: 14px;color: #000000;padding-left: 4px">策略停用</span>
        <span  :style="{'background':'url('+bgImgs[1]+') no-repeat center',width: '14px',height:'14px',float: 'right',margin:'4px 0 0 0'}"></span>
        <span  style="width: 8%;float: right;font-size: 14px;color: #000000;padding-left: 4px">策略正常</span>
        <span  :style="{'background':'url('+bgImgs[0]+') no-repeat center',width: '14px',height:'14px',float: 'right',margin:'4px 0 0 0'}"></span>
      </div>
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <div slot="icons" slot-scope="text, record" :style="{'background':'url('+tableImg[text]+') no-repeat center',width: '100%',height:'20px'}"></div>

        <span slot="action" slot-scope="text, record">
            <a @click="handleDetail(record)">查看</a>
        </span>

      </a-table>

    </a-spin>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">关闭</a-button>
<!--      <a-button type="primary" @click="handleOk" v-if="!disableSubmit">确定</a-button>-->
    </template>
  </j-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import {tableMixin} from "../../mixin/tableMixin";
    export default {
      name: "RuleListModal",
      mixins:[tableMixin],
      data () {
        return {
          title:"详情",
          width:1200,
          visible: false,
          confirmLoading: false,
          record:'',
          bgImgs: {
            0: require('@/assets/icon_strategy_normal_b.png'),
            1: require('@/assets/icon_strategy_deactivate_b.png'),
            2: require('@/assets/icon_strategy_notset_b.png'),
          },
          tableImg: {
            0: require('@/assets/icon_strategy_normal_a.png'),
            1: require('@/assets/icon_strategy_deactivate_a.png'),
            2: require('@/assets/icon_strategy_notset_a.png'),
          },
          // 表头
          columns: [
            {
              title: '序号',
              dataIndex: '',
              key:'rowIndex',
              width:60,
              align:"center",
              customRender:this.calcIndex,
              scopedSlots: {
                filterDropdown: 'filterDropdown',
                filterIcon: 'filterIcon'},
            },
            {
              title:'策略类型',
              align:"center",
              dataIndex: 'ruleType',
            },
            {
              title:'污染因子',
              align:"center",
              dataIndex: 'code',
            },
            {
              title:'阈值上限',
              align:"center",
              dataIndex: 'warnValueUp',
            },
            {
              title:'阈值下限',
              align:"center",
              dataIndex: 'warnValueDown',
            },
            {
              title:'重复数据条数',
              align:"center",
              dataIndex: 'repeatDataCount',
            },
            {
              title:'策略级别',
              align:"center",
              dataIndex: 'ruleLevel',
            },
            {
              title:'是否发送短信',
              align:"center",
              dataIndex: 'isSendMsg',
            },
            {
              title:'发送频率(次/天)',
              align:"center",
              dataIndex: 'msgRate',
            },
            {
              title:'短信接收时段',
              align:"center",
              dataIndex: 'warnStartTime',
              customRender: function (text,record) {
                if(text)
                  return text.substring(0,5) + '~' + (record.warnEndTime).substring(0,5);
              }
            },
            {
              title:'策略状态',
              align:"center",
              dataIndex: 'isUsed',
              scopedSlots: { customRender: 'icons' }
            },
            {
              title: '操作',
              dataIndex: 'action',
              align:"center",
              // fixed:"right",
              width:147,
              scopedSlots: { customRender: 'action' }
            }
          ],
          url: {
            view: "/swr/sysWarnRule/add",
          }
        }
      },
      methods:{
        handleOk(){

        },
        handleCancel () {
          this.close()
        },
        close () {
          this.$emit('close');
          this.visible = false;
        },
        view(record){
          console.log("!!!!!",record);
          this.model = Object.assign({}, record);
          this.visible = true;
        }
      }
    }
</script>

<style scoped>

</style>