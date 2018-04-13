<template>
  <div class="components-container">
    <div class="content_bg">
      <a>当日积分设置</a>
    </div>

    <div class="price"><span>当前价格:</span>¥ {{priceNow}} rmb/枚</div>
    <div style="width: 100px;margin: 50px auto 0">
      <el-button type="success" @click="dialogVisible = true">设置价格</el-button>
    </div>

    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="30%">
      <el-form :model="ruleForm" status-icon label-width="50px" class="demo-ruleForm">
        <el-form-item label="价格" prop="pass">
          <el-input type="number" v-model.number="ruleForm.price" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="setBp()">确 定</el-button>
  </span>
    </el-dialog>

  </div>
</template>

<script>
  import axios from 'axios'
  import qs from 'qs';
  export default {
    name: 'bpsetup',
    data(){
      return{
        priceNow:0,
        dialogVisible:false,
        ruleForm:{
          price:null
        },
      }
    },
    mounted: function () {
      this.getBp();
    },
    methods:{
        setBp(){
          if(this.ruleForm.price < 0){
            this.$message({
              message: '价格必须大于0',
              type: 'success',
              duration: 5 * 1000
            })
            return;
          }
          this.$confirm('是否更改积分价格?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            var data = qs.stringify({
              key: 'CURRENT_PRICE',
              value:this.ruleForm.price,
            });

            axios.post('/admin/config/createOrUpdate',data, {headers:{ 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8' }}).then((response)=>{
              console.log(response)
              this.priceNow =this.ruleForm.price;
              this.dialogVisible = false;
              this.ruleForm.price=null;
              this.$message({
                message: '积分价格设置成功',
                type: 'success',
                duration: 5 * 1000
              })
            }).catch((error)=>{
              this.$message({
                message: error.message,
                type: 'error',
                duration: 5 * 1000
              })
              this.loading = false
            })
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消'
            });
          });


        },
        getBp(){
          axios.get('/front/config/list').then((response)=>{
            console.log(response.data[1])
            this.priceNow=response.data[1].strvalue;
          }).catch((error)=>{

          })
        }
    }
  }
</script>

<style scoped>
  .components-container{
    position: relative;
    padding: 20px;
    min-height: calc(100vh - 84px);
  }
  .content_bg{
    background: #eef1f6;
    padding: 15px 16px;
    margin-bottom: 20px;
    display: block;
    line-height: 36px;
    font-size: 15px;
  }
  .price{
    color: #F6416C;
    display: block;
    margin: 10px 0;
    text-align: center;
    font-size: 60px;
    font-weight: 500;
  }
  .price > span{
    color: #1f2d3d;
    font-size: 30px;
    margin-right: 25px;
  }
</style>

