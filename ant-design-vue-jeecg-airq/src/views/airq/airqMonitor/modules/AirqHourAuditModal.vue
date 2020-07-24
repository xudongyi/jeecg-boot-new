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
      <a-form :form="form">

        <a-row>
          <a-col span="12">
            <a-form-item label="行政区域" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <area-link-select @change="areaChange" type="cascader" v-decorator="['area']" placeholder="请选择所属区域" show-search style="width: 100%" optionFilterProp="children" :disabled="true"/>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="监测点位名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select v-decorator="['mn']" show-search style="width: 100%" optionFilterProp="children" placeholder="请选择监测点位名称" :disabled="true">
                <a-select-option v-for="item in items" :key="item.value" :value="item.key">
                  {{item.value}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col span="12">
            <a-form-item label="采集时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请输入采集时间" v-decorator="['dataTime']" :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" :disabled="true"/>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="SO2(1h)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a21026Avg']" placeholder="请输入SO2" style="width: 100%" :disabled="true"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="NO2(1h)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a21004Avg']" placeholder="请输入NO2" style="width: 100%" :disabled="true"/>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="PM10(1h)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a3400201Avg']" placeholder="请输入PM10(1h)" style="width: 100%" :disabled="true"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="PM10(24h)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a3400224Avg']" placeholder="请输入PM10(24h)" style="width: 100%" :disabled="true"/>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="CO(1h)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a21005Avg']" placeholder="请输入CO" style="width: 100%" :disabled="true"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="O3(1h)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a0502401Avg']" placeholder="请输入O3(1h)" style="width: 100%" :disabled="true"/>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="O3(8h)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a0502408Avg']" placeholder="请输入O3(8h)" style="width: 100%" :disabled="true"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="PM2.5(1h)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a3400401Avg']" placeholder="请输入PM2.5(1h)" style="width: 100%" :disabled="true"/>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="PM2.5(24h)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a3400424Avg']" placeholder="请输入PM2.5(24h)" style="width: 100%" :disabled="true"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="温度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a01001Avg']" placeholder="请输入温度" style="width: 100%" :disabled="true"/>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="湿度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a01002Avg']" placeholder="请输入湿度" style="width: 100%" :disabled="true"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="风速" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['a01007Avg']" placeholder="请输入风速" style="width: 100%" :disabled="true"/>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="风向" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['a01008Avg']" :trigger-change="true" dictCode="wind" placeholder="请输入风向" :disabled="true"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="录入人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['createName']" placeholder="请输入录入人" :disabled="true"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="录入时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date :showTime="true" :dateFormat="dateFormat"
                      placeholder="请选择录入时间" v-decorator="['createTime']" :trigger-change="true" style="width: 100%" :disabled="true"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="审核人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['updateName']" placeholder="请输入审核人" :disabled="true"></a-input>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="审核时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date :showTime="true" :dateFormat="dateFormat"
                      placeholder="请选择审核时间" v-decorator="['updateTime']" :trigger-change="true" style="width: 100%" :disabled="true"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="审核结果：" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-radio-group v-model="result" :disabled="disableSubmit">
                <a-radio value = 1>
                  审核通过
                </a-radio>
                <a-radio value = 4>
                  审核不通过
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col span="12">
            <a-form-item label="审核说明:" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="['content']" placeholder="审核说明" :rows="2" :disabled="disableSubmit"/>
            </a-form-item>
          </a-col>
        </a-row>

      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button type="primary" @click="handleOk" v-if="monitorTag === 'edit'">确定</a-button>
      <a-button type="primary" @click="handleCancel">关闭</a-button>
    </template>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import AreaLinkSelect from '../../component/AreaLinkSelect'
  import {querySiteNameAndMn} from "../../../requestAction/request";
  import moment from 'moment'

  export default {
    name: "AirqHourAuditModal",
    components: {
      JDate,
      JDictSelectTag,
      AreaLinkSelect
    },

    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        dateFormat:"YYYY-MM-DD HH:mm:ss",
        items:[],
        siteOriginal:[],
        monitorTag:'',
        result:1,
        disableSubmit:false,
        width:1000,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          //add: "/hour/airqHour/add",
          edit: "/hour/airqHour/audit"
        }
      }
    },
    created () {
      let that = this;
      querySiteNameAndMn({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res)=>{
        if(res.success){
          console.log("!!",res.result);
          that.siteOriginal = res.result;
          that.items = res.result;

        }
      })
    },
    methods: {
      //获取系统时间
      getTime() {
        let  _this =this;
        this.timer =  setInterval(()=>{
          _this.model.updateTime = moment().format(this.dateFormat);
          console.log(_this.model.updateTime );
          this.$nextTick(() => { this.form.setFieldsValue(pick(this.model,'updateTime'))})

        },1000)
      },
      areaChange(val){
        let _this = this;
        _this.items=[];
        _this.siteOriginal.forEach(e=>{
          if(e.area === val){
            _this.items.push(e)
          }
        })
      },
      add () {
        this.edit({});
        this.getTime();
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        if(record.state===1||record.state===4)
          this.result = record.state;
        if(record.updateTime){
          if (this.timer) {
            clearInterval(this.timer); // 在Vue实例销毁前，清除我们的定时器
          }
          this.model.updateTime = moment(record.updateTime).format(this.dateFormat);
        }else {
          this.model.updateTime = moment().format(this.dateFormat);
          //如果没有定时器开启一个定时器
          //获取时间
          this.getTime();
        }
        this.model.updateName = this.$store.getters.userInfo.realname;

        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'area','mn','dataTime','content','createTime','createName','updateName','updateTime','state','a01008Avg','a01006Avg','a21005Avg','a3400201Avg','a01007Avg','a21004Avg','a3400424Avg','a01001Avg','a21002Avg','a0502408Avg','a3400401Avg','a0502401Avg','a01002Avg','a21026Avg','a3400224Avg','a21003Avg'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
        if (this.timer) {
          clearInterval(this.timer); // 在Vue实例销毁前，清除我们的定时器
        }
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
              method = 'put';
            }
            let formData = Object.assign(this.model, values);
            formData.state = this.result;
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }

        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'dataTime','createTime','mn','state','level','firstCode','aqi','a01006Avg','a01006Iaqi','a21005Avg','a21005Iaqi','a3400201Avg','a3400201Iaqi','a01007Avg','a01007Iaqi','a21004Avg','a21004Iaqi','a3400424Avg','a3400424Iaqi','a01001Avg','a01001Iaqi','a21002Avg','a21002Iaqi','a0502408Avg','a0502408Iaqi','a3400401Avg','a3400401Iaqi','a0502401Avg','a0502401Iaqi','a01002Avg','a01002Iaqi','a21026Avg','a21026Iaqi','a3400224Avg','a3400224Iaqi','a21003Avg','a21003Iaqi'))
      },


    }
  }
</script>