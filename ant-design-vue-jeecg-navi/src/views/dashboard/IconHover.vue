<template>
    <div :style="iconStyle" class="icon"  >

    </div>
</template>

<script>
    export default {
        name: "IconHover",
        props:{
          url:{
            type:String,
            required:true
          },
          overUrl:{
            type:String,
            required:true
          },
          width:{
            default:12
          },
          overWidth:{
            default:12
          },
          overHeight:{
            default:12
          },
          height:{
            default:12
          }
        },
        data(){
          console.log(this.url)
          return {
            iconStyle:{
              background: 'url('+require('@assets/'+this.url)+') no-repeat',
              width:this.width+'px',
              height:this.height+'px'
            }
          }
        },
        methods: {
          mouseOver: function(){
            this.iconStyle.background =  'url('+require('@assets/'+this.overUrl)+') no-repeat';
            this.iconStyle.width =  this.overWidth+'px';
            this.iconStyle.height = this.overHeight+'px';
          },
          mouseLeave: function () {
            this.iconStyle.background =  'url('+require('@assets/'+this.url)+') no-repeat';
            this.iconStyle.width =  this.width+'px';
            this.iconStyle.height = this.height+'px';
          },
        }
    }
</script>

<style scoped>
  .icon{
      background-size:cover;
  }
</style>