<template>
  <a-spin :spinning="confirmLoading">
    <a-button type="primary" @click="handleOk" v-if="addButton">保存</a-button>
    <a-form :form="form">

      <a-row>
        <a-col span='12'>
          <a-form-item label="站点名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['siteName', validatorRules.siteName]" :disabled="disable"
                     placeholder="请输入站点名称"></a-input>
          </a-form-item>
        </a-col>
        <a-col span='12'>
          <a-form-item label="站点编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['siteCode',validatorRules.siteCode]" placeholder="请输入站点编号"
                     :disabled="disable"></a-input>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col span='12'>
          <a-form-item label="站点类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-select v-decorator="['siteType', validatorRules.siteType]" style="width: 100%" placeholder="请选择站点类型" @change="handleChange">
              <a-select-option v-for="siteType in siteTypes" :key="siteType.value" :value="siteType.key">
                {{siteType.value}}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col span='12'>
          <a-form-item label="站点级别" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag type="list" v-decorator="['siteLevel', validatorRules.siteLevel]" :disabled="disable"
                               :trigger-change="true" dictCode="siteLevel" placeholder="请选择站点级别"/>
          </a-form-item>
        </a-col>
        <a-col span='12'>
          <a-form-item label="所属区域" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <area-link-select type="cascader" v-decorator="['area', validatorRules.area]" placeholder="请输入省市区"
                              :disabled="disable"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col span='24'>
          <a-form-item :label="locationLabel" :labelCol="labelCols" :wrapperCol="wrapperCols">
            <a-row>
              <a-col span='21'>
                <a-input v-decorator="['location', validatorRules.location]" :disabled="disable"
                         placeholder="请输入站点位置"></a-input>
              </a-col>
              <a-col span='3'>
                <a-button type="primary" :block="true" @click="searchMap" :disabled="disable">
                  查询坐标
                </a-button>
              </a-col>
            </a-row>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col span='12'>
          <a-form-item label="站点纬度" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['siteLatitude', validatorRules.siteLatitude]" :disabled="disable"
                     placeholder="请输入站点纬度"></a-input>
          </a-form-item>
        </a-col>
        <a-col span='12'>
          <a-form-item label="站点经度" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['siteLongitude', validatorRules.siteLongitude]" :disabled="disable"
                     placeholder="请输入站点经度"></a-input>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col span='12'>
          <a-form-item label="站点状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag type="list" v-decorator="['siteState', validatorRules.siteState]" :disabled="disable"
                               :trigger-change="true" dictCode="siteState" placeholder="请选择站点状态"/>
          </a-form-item>
        </a-col>
        <a-col span='12'>
          <a-form-item label="是否联网" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag type="list" v-decorator="['isNet']" :trigger-change="true" :disabled="disable"
                               dictCode="isNet"
                               placeholder="请选择是否联网"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row v-show="isjmodal">
        <a-col span="24">
          <a-form-item label="数采仪MN号" :labelCol="labelCols" :wrapperCol="wrapperCols">
            <a-input v-decorator="['mn', validatorRules.mn]" placeholder="请输入数采仪MN号" :disabled="disable"></a-input>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row v-if="isWater">
        <a-col span='12'>
          <a-form-item label="联系人" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['linkman']" placeholder="请输入联系人" :disabled="disable"></a-input>
          </a-form-item>
        </a-col>
        <a-col span='12'>
          <a-form-item label="联系电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['phone']" placeholder="请输入联系电话" :disabled="disable"></a-input>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row v-if="isWater">
        <a-col span='24'>
          <a-form-item label="水域功能区类别" :labelCol="labelCols" :wrapperCol="wrapperCols">
            <j-dict-select-tag type="list" v-decorator="['waterType']" :trigger-change="true" :disabled="disable"
                               dictCode="waterType" placeholder="请选择水域功能区类别"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row v-if="isWater">
        <a-col span='12'>
          <a-form-item label="排放去向" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag type="list" v-decorator="['direction']"
                               :trigger-change="true" dictCode="direction" placeholder="请输入排放去向" :disabled="disable"/>
          </a-form-item>
        </a-col>
        <a-col span='12'>
          <a-form-item label="排放规律" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag type="list" v-decorator="['letLaw']"
                               :trigger-change="true" dictCode="letLaw" placeholder="请选择排放规律" :disabled="disable"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row v-if="isWater">
        <a-col span='12'>
          <a-form-item label="标志牌安装方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag type="list" v-decorator="['signType']"
                               :trigger-change="true" dictCode="signType" placeholder="请输入标志牌安装方式" :disabled="disable"/>
          </a-form-item>
        </a-col>
        <a-col span='12'>
          <a-form-item label="进出口" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag type="list" v-decorator="['imorex', validatorRules.imorex]"
                               :trigger-change="true" dictCode="imorex" placeholder="请选择进出口" :disabled="disable"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row v-if="isAir">
        <a-col span='24'>
          <a-form-item label="废气排风口类型" :labelCol="labelCols" :wrapperCol="wrapperCols">
            <j-dict-select-tag type="list" v-decorator="['exitType']"
                               :trigger-change="true" dictCode="exitType" placeholder="请输入废气排风口类型" :disabled="disable"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row v-if="isAir">
        <a-col span='12'>
          <a-form-item label="出口内径" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['exportBore']" placeholder="请输入出口内径" :disabled="disable" addon-after="米"></a-input>
          </a-form-item>
        </a-col>
        <a-col span='12'>
          <a-form-item label="最大流量" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['maxFlow']" placeholder="请输入最大流量" :disabled="disable" addon-after="标立方米"></a-input>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row v-if="isAir">
        <a-col span='12'>
          <a-form-item label="出口截面积" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['exportCross']" placeholder="请输入出口截面积" :disabled="disable"
                     addon-after="平方米"></a-input>
          </a-form-item>
        </a-col>
        <a-col span='12' v-if="isAir">
          <a-form-item label="出口高度" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['exportHeight']" placeholder="请输入出口高度" :disabled="disable" addon-after="米"></a-input>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-spin>
</template>

<script>

  import {httpAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import {queryCompanyName, loadBaiduMap} from '../../../../requestAction/request'
  import {duplicateCheck} from '@/api/api'
  import AreaLinkSelect from "../../../../component/AreaLinkSelect";

  export default {
    name: "SiteMonitorPointModal",
    components: {
      JDictSelectTag,
      AreaLinkSelect
    },
    data() {
      return {
        form: this.$form.createForm(this),
        locationLabel: "排污口位置",
        disable: this.isDisable,
        isWater: false,
        companyId:"",
        siteTypes:[{"key":"0","value":"废水"},{"key":"1","value":"废气"},{"key":"2","value":"VOCs"}],
        isNoise: false,
        isSiteType: true,
        isAir: false,
        title: "操作",
        width: 1200,
        visible: false,
        model: {},
        labelCol: {
          xs: {span: 24},
          sm: {span: 6},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        labelCols: {
          xs: {span: 24},
          sm: {span: 3},
        },
        wrapperCols: {
          xs: {
            span: 24
          },
          sm: {
            span: 20

          }
        },
        confirmLoading: false,
        validatorRules: {
          siteName: {
            rules: [
              {required: true, message: '请输入站点名称!'},
            ]
          },
          siteCode: {
            rules: [
              {required: false, message: '请输入站点编号!'},
            ]
          },
          siteType: {
            rules: [
              {required: true, message: '请输入站点类型!'},
            ]
          },
          companyId: {
            rules: [
              {required: true, message: '请输入所属单位!'},
            ]
          },
          area: {
            rules: [
              {required: true, message: '请输入所属区域!'},
            ]
          },
          siteLevel: {
            rules: [
              {required: true, message: '请输入站点级别!'},
            ]
          },
          location: {
            rules: [
              {required: true, message: '请输入站点位置!'},
            ]
          },
          siteLongitude: {
            rules: [
              {required: true, message: '请输入站点经度!'},
            ]
          },
          siteLatitude: {
            rules: [
              {required: true, message: '请输入站点纬度!'},
            ]
          },
          siteState: {
            rules: [
              {required: true, message: '请输入站点状态!'},
            ]
          },
          imorex: {
            rules: [
              {required: true, message: '请输入进出口!'},
            ]
          },
          direction: {
            rules: [
              {required: true, message: '请输入排放去向!'},
            ]
          },
          surfaceType: {
            rules: [
              {required: true, message: '请输入水环境功能区类别!'},
            ]
          },
          noiseType: {
            rules: [
              {required: true, message: '请输入(噪音)声音环境功能区类别!'},
            ]
          },
          mn: {
            rules: [
              {required: true, message: '请输入数采仪MN号!'},
              {validator: this.validateMn}
            ]
          },
        },
        url: {
          add: "/siteInfo/add",
        },
        items: []
      };
    },
    created() {
      let that = this;
      //查询企业名称
      queryCompanyName().then((res) => {
        if (res.success) {
          that.items = res.result;
        }
      })
    },
    mounted() {


    },
    methods: {
      validateMn(rule, value, callback) {
        debugger
        var params = {
          tableName: 'site_monitor_point',
          fieldName: 'mn',
          fieldVal: value,
          dataId: this.model.id
        };
        duplicateCheck(params).then((res) => {
          if (res.success) {
            callback()
          } else {
            callback("mn号已存在!")
          }
        })
      },
      add() {
        this.edit({siteState: '1', isNet: "1"});
      },
      edit(record) {
        this.$nextTick(() => {
          this.form.resetFields();
          record.companyId = this.companyId;
          this.model = Object.assign({}, record);
          this.visible = true;
          if (this.siteType === '0') {
            this.isWater = true
            this.isAir = false
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model, 'siteName', 'siteCode', 'siteType', 'siteLevel', 'area', 'location', 'siteLongitude', 'siteLatitude', 'isNet', 'siteState', 'imorex', 'direction', 'letLaw', 'waterType', 'linkman', 'phone', 'signType', 'mn'))
            })
          } else if (this.siteType === '1') {
            this.isWater = false
            this.isAir = true
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model, 'siteName', 'siteCode', 'siteType', 'siteLevel', 'area', 'location', 'siteLongitude', 'siteLatitude', 'isNet', 'siteState', 'exportHeight', 'exportBore', 'maxFlow', 'exportCross', 'exitType', 'linkman', 'phone', 'mn'))
            })
          } else {
            this.locationLabel = "站点位置";
            this.isWater = false
            this.isAir = false
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model, 'siteName', 'siteCode', 'siteType',  'siteLevel', 'area', 'location', 'siteLongitude', 'siteLatitude', 'isNet', 'siteState', 'linkman', 'phone', 'mn'))
            })
          }

          this.form.setFieldsValue(pick({siteType: this.siteType}, 'siteType'))
        })
      },
      handleOk() {
        const that = this;
        // 触发表单验证
        that.form.validateFields((err, values) => {
            if (!err) {
              that.confirmLoading = true;
              let httpurl = '';
              let method = '';
              if (!this.model.id) {
                httpurl += this.url.add;
                method = 'post';
              } else {
                httpurl += this.url.edit;
                method = 'put';
              }
              debugger
              let formData = Object.assign(that.model, values);
              httpAction(httpurl, formData, method).then((res) => {
                if (res.success) {
                  that.$message.success(res.message);
                  that.$emit('ok');
                } else {
                  that.$message.warning(res.message);
                }
              }).finally(() => {
                that.confirmLoading = false;
                that.close();
              })
            }

          }
        )
      },
      close() {
        this.$emit('close');
        this.visible = false;
      }
      ,
      handleCancel() {
        this.close()
      },
      handleChange(value) {
        if(value==="0"){
          this.isWater = true
          this.isAir = false
        }else if(value==="1"){
          this.isWater = false
          this.isAir = true
        }else if(value==="2"){
          this.isWater = false
          this.isAir = false
        }
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'siteName', 'siteCode', 'siteType', 'companyId', 'siteLevel', 'area', 'location', 'siteLongitude', 'siteLatitude', 'isNet', 'siteState', 'imorex', 'direction', 'letLaw', 'waterType', 'exportHeight', 'exportBore', 'maxFlow', 'exportCross', 'exitType', 'surfaceType', 'noiseType', 'linkman', 'phone', 'signType', 'mn'))
      },
      searchMap() {
        let that = this;
        that.form.validateFields((err, values) => {
          let param = {address: values.location}
          loadBaiduMap(param).then((res) => {
            if (res.success) {
              that.form.setFieldsValue({siteLongitude: res.result.lng, siteLatitude: res.result.lat});
            } else {
              that.$error({title: '查询坐标错误', content: '错误信息：' + res.message, maskClosable: true})
            }
          });
        });
      },
    },
    props: {
      siteType: '',
      isjmodal: false,
      isDisable: false,
      addButton: false

    }
  }
</script>