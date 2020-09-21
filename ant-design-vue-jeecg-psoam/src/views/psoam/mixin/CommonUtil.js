import AreaHandler from "../component/AreaHandler";

const commonUtil = {
  data(){
    return {
      areaHandler: ''
    }
  },
  watch: {
    dataSource(val){
      this.rowspan(val)
    }
  },
  methods:{
    //分页 序号
    calcIndex: function (t,r,index) {
      return parseInt(index)+1+(this.ipagination.current-1)*this.ipagination.pageSize;
    },
    renderContentArea(value, row, index,key){
      let tmp =''
      if (value)
      {
      //初始化
      if (this.areaHandler === '') {
        this.initArea()
      }
      let arr = [];
      this.areaHandler.getAreaBycode(value, arr);
        tmp= arr[0] + arr[1] + arr[2]
      }
      return this.renderContent(tmp,row,index,key)
    },
    renderContent (value, row, index,key)  {
      const obj = {
        children: value,
        attrs: {}
      };
      const _row = this.spanArr[key.dataIndex][index];
      const _col = _row> 0 ? 1 : 0;
      obj.attrs = {
        rowSpan: _row,
        colSpan: _col
      };
      return obj;
    },
    rowspan(userData){


      let _this = this
      _this.spanArr = {};
      _this.position ={};
      for(let i=0 ; i < _this.mergeKeys.length ; i++) {
        _this.spanArr[_this.mergeKeys[i]] = []
        _this.position[_this.mergeKeys[i]]=0
      }
      if(userData===null)
        return
      //按照  mergeKeys  第一个值进行  合并相同列
      let keyIndex = _this.mergeKeys[0]
      let unMix = true;
      //end
      userData.forEach((item,index) => {
        for(let i=0 ; i < _this.mergeKeys.length ; i++){
        if(index === 0){
          _this.spanArr[_this.mergeKeys[i]].push(1);
          _this.position[_this.mergeKeys[i]] = 0;
        }else{

            if(userData[index][_this.mergeKeys[i]] === userData[index-1][_this.mergeKeys[i]]&&(unMix||keyIndex===_this.mergeKeys[i]) ){
              if(keyIndex===_this.mergeKeys[i]){
                unMix=true
              }
              _this.spanArr[_this.mergeKeys[i]][ _this.position[_this.mergeKeys[i]]] += 1;
              _this.spanArr[_this.mergeKeys[i]].push(0);
            }else{
              if(keyIndex===_this.mergeKeys[i]){
                unMix=false
              }
              _this.spanArr[_this.mergeKeys[i]].push(1);
              _this.position[_this.mergeKeys[i]] = index;
            }
          }
          //需要合并的地方判断

        }
      });
    },
    initArea() {
      this.areaHandler = new AreaHandler()
    },
    renderEmpty(val){
      if(val==null||val==='')
        return 'NA'
      return val
    },
  }
}

export  { commonUtil }