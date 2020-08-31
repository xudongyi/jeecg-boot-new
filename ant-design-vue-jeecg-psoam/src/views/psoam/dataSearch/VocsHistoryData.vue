<!--<template>-->
<!--  <a-card :bordered="false">-->
<!--    &lt;!&ndash; 查询区域 &ndash;&gt;-->
<!--    <div class="table-page-search-wrapper">-->
<!--      <a-form layout="inline" @keyup.enter.native="searchQuery">-->
<!--        <a-row :gutter="24">-->
<!--          <a-col :xl="5" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="所属区域">-->
<!--              <area-link-select @change="areaChange" type="cascader" v-model="queryParam.area" show-search style="width: 100%" optionFilterProp="children"/>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :xl="5" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="监测点位名称">-->
<!--              <a-select v-model="queryParam.mn" show-search style="width: 100%" optionFilterProp="children">-->
<!--&lt;!&ndash;                                <a-select-option :value="companyIds">请选择</a-select-option>&ndash;&gt;-->
<!--                <a-select-option v-for="item in items" :key="item.value" :value="item.key">-->
<!--                  {{item.value}}-->
<!--                </a-select-option>-->
<!--              </a-select>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--&lt;!&ndash;          <a-col :xl="9" :lg="11" :md="12" :sm="24">&ndash;&gt;-->
<!--&lt;!&ndash;            <a-form-item label="发布时间">&ndash;&gt;-->
<!--&lt;!&ndash;              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间" class="query-group-cust" v-model="queryParam.dataTime_begin"></j-date>&ndash;&gt;-->
<!--&lt;!&ndash;              <span class="query-group-split-cust"></span>&ndash;&gt;-->
<!--&lt;!&ndash;              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间" class="query-group-cust" v-model="queryParam.dataTime_end" ></j-date>&ndash;&gt;-->
<!--&lt;!&ndash;            </a-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;          </a-col>&ndash;&gt;-->
<!--&lt;!&ndash;          <a-col :xl="5" :lg="7" :md="8" :sm="24">&ndash;&gt;-->
<!--&lt;!&ndash;            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">&ndash;&gt;-->
<!--&lt;!&ndash;              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>&ndash;&gt;-->
<!--&lt;!&ndash;              <a-button type="primary" @click="toSearchReset" icon="reload" style="margin-left: 8px">重置</a-button>&ndash;&gt;-->
<!--&lt;!&ndash;            </span>&ndash;&gt;-->
<!--&lt;!&ndash;          </a-col>&ndash;&gt;-->
<!--        </a-row>-->
<!--      </a-form>-->
<!--    </div>-->
<!--    &lt;!&ndash; 查询区域-END &ndash;&gt;-->

<!--    &lt;!&ndash; table区域-begin &ndash;&gt;-->
<!--&lt;!&ndash;    <div>&ndash;&gt;-->

<!--&lt;!&ndash;      <a-table&ndash;&gt;-->
<!--&lt;!&ndash;        ref="table"&ndash;&gt;-->
<!--&lt;!&ndash;        size="middle"&ndash;&gt;-->
<!--&lt;!&ndash;        bordered&ndash;&gt;-->
<!--&lt;!&ndash;        rowKey="id"&ndash;&gt;-->
<!--&lt;!&ndash;        :columns="columns"&ndash;&gt;-->
<!--&lt;!&ndash;        :dataSource="dataSource"&ndash;&gt;-->
<!--&lt;!&ndash;        :pagination="ipagination"&ndash;&gt;-->
<!--&lt;!&ndash;        :loading="loading"&ndash;&gt;-->
<!--&lt;!&ndash;        :scroll="{ x: 3100 }"&ndash;&gt;-->
<!--&lt;!&ndash;        class="j-table-force-nowrap"&ndash;&gt;-->
<!--&lt;!&ndash;        @change="handleTableChange">&ndash;&gt;-->

<!--&lt;!&ndash;        <div slot="filterDropdown" >&ndash;&gt;-->
<!--&lt;!&ndash;          <a-card title="自定义列">&ndash;&gt;-->
<!--&lt;!&ndash;            <a-checkbox-group @change="onColSettingsChange" v-model="settingColumns" :defaultValue="settingColumns">&ndash;&gt;-->
<!--&lt;!&ndash;              <a-row>&ndash;&gt;-->
<!--&lt;!&ndash;                <template v-for="(item,index) in defColumns">&ndash;&gt;-->
<!--&lt;!&ndash;                  <template v-if="item.key!='rowIndex'&& item.dataIndex!='action'">&ndash;&gt;-->
<!--&lt;!&ndash;                    <a-col :span="12"><a-checkbox :value="item.dataIndex">{{ item.title }}</a-checkbox></a-col>&ndash;&gt;-->
<!--&lt;!&ndash;                  </template>&ndash;&gt;-->
<!--&lt;!&ndash;                </template>&ndash;&gt;-->
<!--&lt;!&ndash;              </a-row>&ndash;&gt;-->
<!--&lt;!&ndash;            </a-checkbox-group>&ndash;&gt;-->
<!--&lt;!&ndash;          </a-card>&ndash;&gt;-->
<!--&lt;!&ndash;        </div>&ndash;&gt;-->
<!--&lt;!&ndash;        <a-icon slot="filterIcon" type='setting' :style="{ fontSize:'16px',color:  '#108ee9',width:'100%' }" />&ndash;&gt;-->

<!--&lt;!&ndash;        <span slot="airLevel" slot-scope="text,record">&ndash;&gt;-->
<!--&lt;!&ndash;          <a-tag :color="tagColors[record.level]" style="width: 90%">&ndash;&gt;-->
<!--&lt;!&ndash;            {{ text}}&ndash;&gt;-->
<!--&lt;!&ndash;          </a-tag>&ndash;&gt;-->
<!--&lt;!&ndash;         </span>&ndash;&gt;-->
<!--&lt;!&ndash;      </a-table>&ndash;&gt;-->
<!--&lt;!&ndash;    </div>&ndash;&gt;-->

<!--  </a-card>-->
<!--</template>-->

<!--<script>-->
<!--  import {loadAreaDate} from '../component/areaUtil'-->
<!--  import AreaHandler from "../component/AreaHandler"-->
<!--  import {querySiteNameAndMn} from "../../requestAction/request";-->
<!--  import Vue from 'vue'-->
<!--  import AreaLinkSelect from '../component/AreaLinkSelect'-->

<!--    export default {-->
<!--      name: "VocsHistoryData",-->
<!--      components: {-->

<!--        AreaLinkSelect-->
<!--      },-->
<!--      data(){-->
<!--        return {-->
<!--          queryParam: {-->
<!--            companyIds:this.$store.getters.userInfo.companyIds.join(',')-->
<!--          },-->
<!--          items:[],-->
<!--          siteOriginal:[],-->
<!--          areaHandler:''-->
<!--        }-->
<!--      },-->
<!--      methods:{-->
<!--        initArea(){-->
<!--          this.areaHandler = new AreaHandler()-->
<!--        },-->
<!--        getAreaByCode(text){-->
<!--          if(!text)-->
<!--            return ''-->
<!--          //初始化-->
<!--          if(this.areaHandler==='')-->
<!--          {-->
<!--            this.initArea()-->
<!--          }-->
<!--          let arr = [];-->
<!--          this.areaHandler.getAreaBycode(text,arr);-->
<!--          return arr[0]+arr[1]+arr[2]-->
<!--        },-->
<!--        areaChange(val){-->
<!--          let _this = this;-->
<!--          _this.items=[];-->
<!--          _this.siteOriginal.forEach(e=>{-->
<!--            if(e.area === val){-->
<!--              _this.items.push(e)-->
<!--            }-->
<!--          })-->
<!--        },-->
<!--      },-->
<!--      mounted(){-->
<!--        //this.initColumns();-->
<!--        let that = this;-->
<!--        querySiteNameAndMn({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res)=>{-->
<!--          if(res.success){-->
<!--            console.log("!!",res.result);-->
<!--            that.siteOriginal = res.result;-->
<!--            that.items = res.result;-->
<!--          }-->
<!--        })-->
<!--      }-->
<!--    }-->
<!--</script>-->

<!--<style scoped>-->

<!--</style>-->