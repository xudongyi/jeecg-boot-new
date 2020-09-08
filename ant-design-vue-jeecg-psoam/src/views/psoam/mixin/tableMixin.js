/**
 * 新增修改完成调用 modalFormOk方法 编辑弹框组件ref定义为modalForm
 * 高级查询按钮调用 superQuery方法  高级查询组件ref定义为superQueryModal
 * data中url定义 list为查询列表  delete为删除单条记录  deleteBatch为批量删除
 */
import Vue from 'vue'
import {queryCompanyName, querySiteNameAndMn} from "../../requestAction/request";
import {loadAreaDate} from "../component/areaUtil";
import AreaHandler from "../component/AreaHandler";
import { filterObj } from '@/utils/util';//过滤为空的对象
import { deleteAction, getAction,downFile,getFileAccessHttpUrl } from '@/api/manage'

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
      dataTypes:[{key:"realTime",value:"实时"},{key:"minute",value:"分钟"},{key:"hour",value:"小时"} ,{key:"day",value:"日"} ],

    }
  },
  computed:{


  },
  created() {
  },
  methods:{
    calcIndex: function (t,r,index) {
      return parseInt(index)+1+(this.ipagination.current-1)*this.ipagination.pageSize;
    },
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
      this.loadData();
    },
    getQueryField() {
      //TODO 字段权限控制
      var str = "id,";
      this.columns.forEach(function (value) {
        str += "," + value.dataIndex;
      });
      return str;
    },

    getQueryParams() {
      //获取查询条件
      var param = this.queryParam;
      // param.field = this.getQueryField();
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      return filterObj(param);
    },
    loadData(arg) {
      if(!this.url.list){
        this.$message.error("请设置url.list属性!")
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1;
      }
      var params = this.getQueryParams();//查询条件
      this.loading = true;
      getAction(this.url.list, params).then((res) => {
        if (res.success) {
          this.dealColumns(res.result.columns);
          this.dataSource = res.result.records;
          this.ipagination.total = res.result.total;
          console.log(this.dataSource)
        }
        if(res.code===510){
          this.$message.warning(res.message)
        }
        this.loading = false;
      })
    },
    creatData(arg) {
      if(!this.url.list){
        this.$message.error("请设置url.list属性!")
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1;
      }
      var params = this.getQueryParams();//查询条件
      this.loading = true;
      getAction(this.url.list, params).then((res) => {
        if (res.success) {
          this.dataSource = res.result.records;
          this.ipagination.total = res.result.total;
        }
        if(res.code===510){
          this.$message.warning(res.message)
        }
        this.loading = false;
      })
    },
    dealColumns(columns){
      //初始化
      this.defColumns=[];

      // this.scroll={x:250*columns.length};
      for(var i=0;i<this.fixedColumns.length;i++){
        this.defColumns.push(this.fixedColumns[i]);
      }
      for(var i=0;i<columns.length;i++){
        this.defColumns.push(columns[i]);
      }
      this.initColumns();
      if(this.dealScroll)
        this.dealScroll()
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
          // console.log("!!",that.companyNames);
        }
      });
    },
    initColumns(){
      //权限过滤（列权限控制时打开，修改第二个参数为授权码前缀）
      //this.defColumns = colAuthFilter(this.defColumns,'testdemo:');

      var key = this.$route.name+":colsettings";
      let colSettings=null;
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
      console.log(this.columns)
    },
    plainChange(val){
      // console.log(val)
      let list  = val;
      list.push('cou');
      console.log(this.checkedList)
      if(this.queryParam.dataType!=='realTime'){
        //筛选表格
        this.initColumns();
        console.log(this.columns)
        if(this.checkedList.length<4){
          let newColumns=[];
          for(let i = 0 ; i < this.columns.length ; i++)
          {
            //深度克隆
            let newColumn = {};
            Object.keys(this.columns[i]).forEach(e=>{
              newColumn[e] = this.columns[i][e];
            })
            newColumns.push(newColumn);

            if(this.columns[i].children){
              newColumns[i].children = this.columns[i].children.filter(e=>{
                for(let j=0 ; j < list.length ; j++){

                  console.log(e.dataIndex,list[j],e.dataIndex.indexOf(list[j]))
                  if(e.dataIndex.indexOf(list[j])>=0){
                    return true;
                  }
                }
                return false;
              })
            }
          }
          this.columns = newColumns
          this.dealScroll();
          console.log(this.defColumns)
        }
      }
      // queryParam
    },
    handleExportXls(fileName){
      if(!fileName || typeof fileName != "string"){
        fileName = "导出文件"
      }
      let param = {...this.queryParam};

      console.log("导出参数",param)
      downFile(this.url.exportXlsUrl,param).then((data)=>{
        if (!data) {
          this.$message.warning("文件下载失败")
          return
        }
        if (typeof window.navigator.msSaveBlob !== 'undefined') {
          window.navigator.msSaveBlob(new Blob([data],{type: 'application/vnd.ms-excel'}), fileName+'.xls')
        }else{
          let url = window.URL.createObjectURL(new Blob([data],{type: 'application/vnd.ms-excel'}))
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', fileName+'.xls')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link); //下载完成移除元素
          window.URL.revokeObjectURL(url); //释放掉blob对象
        }
      })
    },
  },




};