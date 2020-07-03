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
                    <a-col span='12'>
                        <a-form-item label="站点名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['siteName', validatorRules.siteName]"
                                     placeholder="请输入站点名称"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col span='12'>
                        <a-form-item label="站点编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['siteCode']" placeholder="请输入站点编号"></a-input>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col span='12'>
                        <a-form-item label="站点类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <j-dict-select-tag type="list" v-decorator="['siteType', validatorRules.siteType]"
                                               :trigger-change="true" dictCode="siteType" placeholder="请选择站点类型" disabled="true"/>
                        </a-form-item>
                    </a-col>
                    <a-col span='12'>
                      <a-form-item label="所属单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-select v-decorator="['companyId', validatorRules.companyId]" show-search style="width: 100%"
                                  placeholder="请输入企业名称" optionFilterProp="children">
                          <a-select-option v-for="item in items" :key="item.key" :value="item.key">
                            {{item.value}}
                          </a-select-option>
                        </a-select>
                      </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col span='12'>
                        <a-form-item label="站点级别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <j-dict-select-tag type="list" v-decorator="['siteLevel', validatorRules.siteLevel]"
                                               :trigger-change="true" dictCode="siteLevel" placeholder="请选择站点级别"/>
                        </a-form-item>
                    </a-col>
                    <a-col span='12'>
                        <a-form-item label="所属区域" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <j-area-linkage type="cascader" v-decorator="['area']" placeholder="请输入省市区"/>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col span='12'>
                        <a-form-item label="站点位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['location', validatorRules.location]"
                                     placeholder="请输入站点位置"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col span='12'>
                        <a-form-item label="站点经度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['siteLongitude', validatorRules.siteLongitude]"
                                     placeholder="请输入站点经度"></a-input>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col span='12'>
                        <a-form-item label="站点纬度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['siteLatitude', validatorRules.siteLatitude]"
                                     placeholder="请输入站点纬度"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col span='12'>
                        <a-form-item label="是否联网" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <j-dict-select-tag type="list" v-decorator="['isNet']" :trigger-change="true"
                                               dictCode="isNet"
                                               placeholder="请选择是否联网"/>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col span='12'>
                        <a-form-item label="站点状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <j-dict-select-tag type="list" v-decorator="['siteState', validatorRules.siteState]"
                                               :trigger-change="true" dictCode="siteState" placeholder="请选择站点状态"/>
                        </a-form-item>
                    </a-col>
                    <a-col span='12'>
                        <a-form-item label="进出口" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <j-dict-select-tag type="list" v-decorator="['imorex', validatorRules.imorex]"
                                               :trigger-change="true" dictCode="imorex" placeholder="请选择进出口"/>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col span='12'>
                        <a-form-item label="排放去向" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['direction', validatorRules.direction]"
                                     placeholder="请输入排放去向"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col span='12'>
                        <a-form-item label="排放规律" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['letLaw']" placeholder="请输入排放规律"></a-input>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col span='12'>
                        <a-form-item label="水域功能区类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <j-dict-select-tag type="list" v-decorator="['waterType']" :trigger-change="true"
                                               dictCode="waterType" placeholder="请选择水域功能区类别"/>
                        </a-form-item>
                    </a-col>
                    <a-col span='12'>
                        <a-form-item label="出口高度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['exportHeight']" placeholder="请输入出口高度"></a-input>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col span='12'>
                        <a-form-item label="出口内径" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['exportBore']" placeholder="请输入出口内径"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col span='12'>
                        <a-form-item label="最大流量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['maxFlow']" placeholder="请输入最大流量"></a-input>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col span='12'>
                        <a-form-item label="出口截面积" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['exportCross']" placeholder="请输入出口截面积"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col span='12'>
                        <a-form-item label="废气排风口类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['exitType', validatorRules.exitType]"
                                     placeholder="请输入废气排风口类型"></a-input>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col span='12'>
                        <a-form-item label="(地表水)地表水环境功能区类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <j-dict-select-tag type="list" v-decorator="['surfaceType', validatorRules.surfaceType]"
                                               :trigger-change="true" dictCode="waterType"
                                               placeholder="请选择(地表水)地表水环境功能区类别"/>
                        </a-form-item>
                    </a-col>
                    <a-col span='12'>
                        <a-form-item label="(噪音)声音环境功能区类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <j-dict-select-tag type="list" v-decorator="['noiseType', validatorRules.noiseType]"
                                               :trigger-change="true" dictCode="noiseType"
                                               placeholder="请选择(噪音)声音环境功能区类别"/>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col span='12'>
                        <a-form-item label="联系人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['linkman']" placeholder="请输入联系人"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col span='12'>
                        <a-form-item label="联系电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['phone']" placeholder="请输入联系电话"></a-input>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col span='12'>
                        <a-form-item label="标志牌安装方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['signType']" placeholder="请输入标志牌安装方式"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col span='12'>
                        <a-form-item label="数采仪MN号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-decorator="['mnCode', validatorRules.mnCode]" placeholder="请输入数采仪MN号"></a-input>
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form>
        </a-spin>
    </j-modal>
</template>

<script>

    import {httpAction} from '@/api/manage'
    import pick from 'lodash.pick'
    import {validateDuplicateValue} from '@/utils/util'
    import JDictSelectTag from "@/components/dict/JDictSelectTag"
    import JAreaLinkage from '@comp/jeecg/JAreaLinkage'
    import {queryCompanyName} from "../../../../../../ant-design-vue-jeecg/src/views/business/requestAction/request";


    export default {
        name: "SiteMonitorPointModal",
        components: {
            JDictSelectTag,
            JAreaLinkage,
        },
        data() {
            return {
                form: this.$form.createForm(this),
                title: "操作",
                width: 800,
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
                confirmLoading: false,
                validatorRules: {
                    siteName: {
                        rules: [
                            {required: true, message: '请输入站点名称!'},
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
                    exitType: {
                        rules: [
                            {required: true, message: '请输入废气排风口类型!'},
                        ]
                    },
                    surfaceType: {
                        rules: [
                            {required: true, message: '请输入(地表水)地表水环境功能区类别!'},
                        ]
                    },
                    noiseType: {
                        rules: [
                            {required: true, message: '请输入(噪音)声音环境功能区类别!'},
                        ]
                    },
                    mnCode: {
                        rules: [
                            {required: true, message: '请输入数采仪MN号!'},
                        ]
                    },
                },
                url: {
                    add: "/site/siteMonitorPoint/add",
                    edit: "/site/siteMonitorPoint/edit",
                }
            }
        },
        created() {
          let that = this;
          //查询企业名称
          queryCompanyName({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res) => {
            if(res.success){
              that.items = res.result;
            }
          })
        },
        methods: {
            add() {
                this.edit({});
            },
            edit(record) {
                this.form.resetFields();
                debugger
                this.model = Object.assign({}, record);
                this.visible = true;
                this.$nextTick(() => {
                    this.form.setFieldsValue(pick(this.model, 'siteName', 'siteCode', 'siteType', 'companyId', 'siteLevel', 'area', 'location', 'siteLongitude', 'siteLatitude', 'isNet', 'siteState', 'imorex', 'direction', 'letLaw', 'waterType', 'exportHeight', 'exportBore', 'maxFlow', 'exportCross', 'exitType', 'surfaceType', 'noiseType', 'linkman', 'phone', 'signType', 'mnCode'))
                })
              this.$nextTick(() => {
                this.form.setFieldsValue(pick({siteType:this.siteType}, 'siteType'))
              })
            },
            close() {
                this.$emit('close');
                this.visible = false;
            },
            handleOk() {
                const that = this;
                // 触发表单验证
                this.form.validateFields((err, values) => {
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
                        let formData = Object.assign(this.model, values);
                        console.log("表单提交数据", formData)
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

                })
            },
            handleCancel() {
                this.close()
            },
            popupCallback(row) {
                this.form.setFieldsValue(pick(row, 'siteName', 'siteCode', 'siteType', 'companyId', 'siteLevel', 'area', 'location', 'siteLongitude', 'siteLatitude', 'isNet', 'siteState', 'imorex', 'direction', 'letLaw', 'waterType', 'exportHeight', 'exportBore', 'maxFlow', 'exportCross', 'exitType', 'surfaceType', 'noiseType', 'linkman', 'phone', 'signType', 'mnCode'))
            },


        },
      props:{
        siteType:''
      }
    }
</script>