/**
 *  数据字典  混入
 *
 */
import {ajaxGetDictItems, getDictItemsFromCache} from '@/api/api'
import AreaHandler from "../component/AreaHandler";

export const dataDictMixin = {
  data(){
    return {
      dictOptions:{

      },
      areaHandler:''
    }
  },
  computed:{

  },
  created() {
  },
  methods:{

    initDictData(dictCode) {

      //优先从缓存中读取字典配置
      if(getDictItemsFromCache(dictCode)){
        this.dictOptions[dictCode] = getDictItemsFromCache(dictCode);
        return
      }
      //根据字典Code, 初始化字典数组
      ajaxGetDictItems(dictCode, null).then((res) => {
        if (res.success) {
          this.dictOptions[dictCode] = res.result;
        }
      })
    },
    //查询字典
    dictVal(key,text){
      if(text===null)
        return ''
      //普通字典数据
      this.initDictData(key)
      let result = this.dictOptions[key].find(e=>{
          return  e.value===text;
      });
      if(result==null)
        return text;
      return result.text;
    },
    //查询地址--行政区域
    getAreaByCode(text,keys){
      if(!text)
        return ''
      //初始化
      if(this.areaHandler==='')
      {
        this.areaHandler = new AreaHandler()
      }
      let arr = [];
      this.areaHandler.getAreaBycode(text,arr);
      if(arr.length===3){
        if(!keys){
          return arr[0]+arr[1]+arr[2]
        }else{
          let result  = "";
          for(let i=0 ; i < keys.length;i++)
            result+=arr[keys[i]];
          return result
        }
      }
      else
        return text
    },
  },



};