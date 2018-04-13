<template>
  <div class="components-container">
    <div style="margin-bottom: 20px">
      <el-button type="success" @click="dialogVisible = true">创建邀请码</el-button>
    </div>
    <el-table :data="list" v-loading="listLoading" element-loading-text="给我一点时间" border fit highlight-current-row
              style="width: 100%;">
      <el-table-column width="100" align="center" label="ID">
        <template slot-scope="scope">
          <span>{{scope.row.id}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="邀请码">
        <template slot-scope="scope">
          <span>{{scope.row.code}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态">
        <template slot-scope="scope">
          <span>{{scope.row.status == 'NORMAL' ? '正常' : '无效'}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间">
        <template slot-scope="scope">
          <span>{{conversionTime(scope.row.createdAt) |  parseTime('{y}-{m}-{d} {h}:{i}')}}</span>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-container page-list">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.number" :page-sizes="[10,12,14]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="listQuery.totalElements">
      </el-pagination>
    </div>

    <el-dialog
      title="创建邀请码"
      :visible.sync="dialogVisible"
      width="30%">
      <el-form :model="ruleForm" status-icon label-width="50px" class="demo-ruleForm">
        <el-form-item label="数量" prop="pass">
          <el-input type="number" v-model="ruleForm.number" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="submit()">确 定</el-button>
  </span>
    </el-dialog>
  </div>

</template>

<script>
  import axios from 'axios';
  import qs from 'qs';
  export default {
    name: 'user',
    data(){
      return{
        list:[],
        listLoading:true,
        listQuery:{
          limit:10,
          totalElements:null,
          number:null
        },
        ruleForm:{
          number:null
        },
        dialogVisible:false
      }
    },
    mounted: function () {
      this.pagelist(this.listQuery.limit,0)
    },
    methods:{
      //分页
      pagelist(size,page){
        axios.get('/admin/inviteCoed/list?sort=id,desc&size='+size+'&page='+page).then((response)=>{
          console.log(response)
          this.list = response.data.content
          this.listLoading=false
          this.listQuery.totalElements=response.data.totalElements
          this.listQuery.number=response.data.number + 1;

        }).catch((error)=>{
          this.$message({
            message: error.message,
            type: 'error',
            duration: 5 * 1000
          })
          this.loading = false
        })
      },
      handleSizeChange(val){
        this.listQuery.limit = val
        this.pagelist(this.listQuery.limit,0)
      },
      handleCurrentChange(val){
        console.log(val)
        this.pagelist(this.listQuery.limit,val-1)
      },
      conversionTime(val){
        //转化为时间搓，后台直接给的时间进过滤器有问题
        var date = new Date(val); //时间对象
        var str = date.getTime(); //转换成时间戳
        str = str / 1000;
        return str;
      },
      submit(){
        if(this.ruleForm.number <= 100){
          let data = qs.stringify({
            number: this.ruleForm.number,
          })
          axios.post('/admin/inviteCode/create',data, {headers:{ 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8' }}).then((response)=>{
            console.log(response)
            this.priceNow =this.ruleForm.price;
            this.dialogVisible = false;
            this.ruleForm.number=null;
            this.pagelist(this.listQuery.limit,0)
            this.$message({
              message: '邀请码生成成功',
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
        }else {
          this.$message({
            message: '创建邀请码不能一次超过100个',
            type: 'error',
            duration: 5 * 1000
          })
        }


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
  .page-list{
    position: absolute;
    bottom: 40px;
    right:15px;
  }
</style>
