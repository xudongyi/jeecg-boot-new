/**
 * 新增修改完成调用 modalFormOk方法 编辑弹框组件ref定义为modalForm
 * 高级查询按钮调用 superQuery方法  高级查询组件ref定义为superQueryModal
 * data中url定义 list为查询列表  delete为删除单条记录  deleteBatch为批量删除
 */
import Vue from 'vue'
import {queryCompanyName, querySiteNameAndMn} from "../../requestAction/request";
import {loadAreaDate} from "../component/areaUtil";
import AreaHandler from "../component/AreaHandler";

export const tableMixin = {
  data(){
    return {
      dataSource:[],
      /* 分页参数 */
      ipagination:{
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条"
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
      },
      /* table加载状态 */
      loading:false,
    }
  },
  computed:{


  },
  created() {
  },
  methods:{
    //列设置更改事件
    onColSettingsChange (checkedValues) {
      var key = this.$route.name+":colsettings";
      Vue.ls.set(key, checkedValues, 7 * 24 * 60 * 60 * 1000)
      this.settingColumns = checkedValues;
      const cols = this.defColumns.filter(item => {
        if(item.key =='rowIndex'|| item.dataIndex=='action'){
          return true
        }
        if (this.settingColumns.includes(item.dataIndex)) {
          return true
        }
        return false
      })
      this.columns =  cols;
    },
    handleTableChange(pagination, filters, sorter) {
      //分页、排序、筛选变化时触发
      //TODO 筛选
      this.ipagination = pagination;

    },
    initDictConfig(){
      loadAreaDate()
    },
    initArea(){
      this.areaHandler = new AreaHandler()
    },
    getAreaByCode(text){
      if(!text)
        return '';
      //初始化
      if(this.areaHandler==='')
      {
        this.initArea()
      }
      let arr = [];
      this.areaHandler.getAreaBycode(text,arr);
      return arr[0]+arr[1]+arr[2]
    },
    //地区企业站点级联
    areaChange(val){
      let _this = this;
      _this.items=[];
      //选择地区筛选公司名称
      _this.companyNames=[];
      if(this.queryParam.area != null){
        _this.companyNameOriginal.forEach(b=>{
          if(b.area === val){
            _this.companyNames.push(b);
          }
        })
      }else {
        _this.companyNames =_this.companyNameOriginal;
      }
    },
    //企业站点级联
    companyNameChange(val){
      console.log(this.queryParam.companyId)
      let _this = this;
      _this.items=[];
      _this.siteOriginal.forEach(e=>{
        if(e.companyId === val){
          _this.items.push(e)
        }
      });

      if(this.queryParam.companyId != null){
        _this.showDate = true;
      }else {
        _this.showDate = false;
      }
    },
    queryCompanyAndSite(){
      let that = this;
      querySiteNameAndMn({companyIds:this.$store.getters.userInfo.companyIds.join(','),siteType:this.siteType}).then((res)=>{
        if(res.success){
          //console.log("!!",res.result);
          that.siteOriginal = res.result;
          that.siteitems = res.result;
        }
      });
      if(this.queryParam.area != null){
        this.items = that.siteitems;
      }else {
        this.items = '';
      }
      //查询企业名称
      queryCompanyName({companyIds:this.$store.getters.userInfo.companyIds.join(',')}).then((res) => {
        if(res.success){
          that.companyNameOriginal = res.result.companyNames;
          that.companyNames = res.result.companyNames;
          console.log("!!",that.companyNames);
        }
      });
    },
    initColumns(){
      //权限过滤（列权限控制时打开，修改第二个参数为授权码前缀）
      //this.defColumns = colAuthFilter(this.defColumns,'testdemo:');

      var key = this.$route.name+":colsettings";
      let colSettings= Vue.ls.get(key);
      if(colSettings==null||colSettings==undefined){
        let allSettingColumns = [];
        this.defColumns.forEach(function (item,i,array ) {
          allSettingColumns.push(item.dataIndex);
        })
        this.settingColumns = allSettingColumns;
        this.columns = this.defColumns;
      }else{
        this.settingColumns = colSettings;
        const cols = this.defColumns.filter(item => {
          if(item.key =='rowIndex'|| item.dataIndex=='action'){
            return true;
          }
          if (colSettings.includes(item.dataIndex)) {
            return true;
          }
          return false;
        })
        this.columns =  cols;
      }
    },
  },




};