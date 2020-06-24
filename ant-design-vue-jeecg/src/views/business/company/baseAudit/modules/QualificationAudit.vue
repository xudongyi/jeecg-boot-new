<template>

    <j-modal
      ref="modal"
      :title="title"
      :width="width"
      :visible="visible"
      :confirmLoading="confirmLoading"
      switchFullscreen
      @ok="handleOk"
      @cancel="handleCancel"
      cancelText="关闭"
    >
    <h3 :style="{ marginBottom: '16px' }">
      企业形象
    </h3>
          <pic-list ref="companyImage"  qualificttion-type="companyImage" :size="104" />
    <h3 :style="{ marginBottom: '16px' }">
      营业执照照片
    </h3>
        <pic-list ref="businessLicense"  qualificttion-type="businessLicense" :size="104" />

    <h3 :style="{ marginBottom: '16px' }">
      企业平面图
    </h3>
        <pic-list  ref="floorPlan"  qualificttion-type="floorPlan" :size="104"/>

    <h3 :style="{ marginBottom: '16px' }">
      生产工艺图
    </h3>
        <pic-list  ref="produceCrafts"  qualificttion-type="produceCrafts"  :size="104"/>

    <h3 :style="{ marginBottom: '16px' }">
      生产工艺图
    </h3>
        <pic-list  ref="controlCrafts"  qualificttion-type="controlCrafts"  :size="104"/>
      <h5 :style="{ marginBottom: '16px',color:'red' }">
        红色边框为本次申报删除资质信息，绿色边框为本次申报新增资质
      </h5>

      <a-descriptions >
        <a-descriptions-item label="申报人">
          {{applyer}}
        </a-descriptions-item>
        <a-descriptions-item label="申报时间">
          {{applyTime}}
        </a-descriptions-item>
      </a-descriptions>

      <a-form>
        <a-form-item label="审核结果：">
          <a-radio-group  v-model= "result" :disabled="applyInfo.isView">
            <a-radio value="2">
              通过
            </a-radio>
            <a-radio value="3">
              不通过
            </a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="不通过原因：">
          <a-textarea placeholder="备注信息" :rows="2"  v-model.trim = "message" :disabled="applyInfo.isView"/>
        </a-form-item>
      </a-form>
      <a-descriptions >
        <a-descriptions-item label="审核人">
          {{this.$store.getters.userInfo.realname}}
        </a-descriptions-item>
        <a-descriptions-item label="审核时间">
          {{date}}
        </a-descriptions-item>
      </a-descriptions>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel" >关闭</a-button>
      <a-button type="primary" @click="handleOk" v-show="!applyInfo.isView">确认</a-button>
    </template>
  </j-modal>
</template>

<script>
  import PicList from "../../../component/PicList";
  import {queryQualificationAudit, submitQualificationAudit} from "../../../requestAction/request";
  import AuditFooter from "./AuditFooter";
  import moment from "moment";
    export default {
      name: "QualificationAudit",
      components: {
        PicList, AuditFooter
      },
      props: {
        dateFormat: {
          type: String,
          default: 'YYYY-MM-DD HH:mm:ss',
          required: false,

        },
      },
      data() {
        return {
          applyInfo: {},
          confirmLoading: true,
          visible: false,
          width: 1000,
          title: "基本信息申报审核-资质信息",
          applyer:'',
          applyTime:'',
          date:' ',
          result:'2',
          message:'',
        }
      },
      methods: {

        loadQualificationImgs(recordId) {
          let that = this;

          queryQualificationAudit({applyId: recordId}).then((res) => {
            if (res.success) {
              console.log(res.result);
              that.$refs.companyImage.initImages(res.result.companyImage);
              that.$refs.businessLicense.initImages(res.result.businessLicense);
              that.$refs.floorPlan.initImages(res.result.floorPlan);
              that.$refs.produceCrafts.initImages(res.result.produceCrafts);
              that.$refs.controlCrafts.initImages(res.result.controlCrafts);

            } else {
              console.log(res.message);
            }
          });
        },
        auditModal(record) {
          let that = this;
          this.applyInfo = record;

          this.loadQualificationImgs(record.id);

          this.edit(record);

          if (this.applyInfo.isView) {
          } else {
            this.applyInfo.isView = false;
          }
          this.confirmLoading = false;
        },
        handleCancel() {
          this.visible = false;
        },
        handleOk() {
          this.confirmLoading = true;

          //提交数据，做数据处理
          //调用
          this.submit(this.applyInfo);

        },
        edit(record) {
          this.applyer = record.createBy;
          this.applyTime = moment(record.createTime).format(this.dateFormat);
          if(record.status==='2'||record.status==='3')
            this.result = record.status;
          this.message = record.content;
          if (record.updateTime == null) {
            this.date = moment().format(this.dateFormat);

            if (this.timer) {

            } else {
              let _this = this; // 声明一个变量指向Vue实例this，保证作用域一致
              this.timer = setInterval(() => {
                _this.date = moment().format(this.dateFormat); // 修改数据date
              }, 1000);
            }

          } else {
            if (this.timer) {
              clearInterval(this.timer);
            }
            this.date = moment(record.updateTime).format(this.dateFormat);

          }
          if (record.isView) {
            this.isView = this.applyInfo.isView;
          } else {
            this.isView = false;
          }
        },
        //数据提交
        submit(record) {
          let _this = this;

          submitQualificationAudit({applyId:record.id,result:this.result,userId:this.$store.getters.userInfo.id}).then((res) => {
            if (res.success) {
              this.visible = false;
              _this.$message.success("提交成功");
            } else {
              _this.$message.error(res.message);
            }

          });
        },

      },
      beforeDestroy() {
        if (this.timer) {
          clearInterval(this.timer); // 在Vue实例销毁前，清除我们的定时器
        }
      }
    }
</script>

<style scoped>

</style>