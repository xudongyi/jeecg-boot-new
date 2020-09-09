/**
 *  报表  混入
 *
 */


export const reportMixin = {
  data(){
    return {
      tabList:[
        {
          key: 'date',
          tab: '日报表',
        },
        {
          key: 'month',
          tab: '月报表',
        },
        {
          key: 'quarterly',
          tab: '季度报表',
        },
        {
          key: 'year',
          tab: '年报表',
        },
      ],
      //默认是第一个
      key:'date',
      dateFormat:{
        month:{value:"YYYY-MM",mode:'month'},
        year:{value:"YYYY",mode:'year'},
        date:{value:"YYYY-MM-DD",mode:'date'},
        quarterly:{value:"YYYY",mode:'year'}
      },
    }
  },
  computed:{

  },
  created() {
  },
  methods:{


  },



};