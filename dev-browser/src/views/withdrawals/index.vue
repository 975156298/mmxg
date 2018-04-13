<template>
  <div class="components-container">
    <div style="display: flex;justify-content: space-between;align-items: center">
      <div>
        <span style="font-size: 14px;margin-right: 10px">审核状态:</span>
        <el-radio-group v-model="status">
          <el-radio @change="radioChange()" label="">全部</el-radio>
          <el-radio @change="radioChange()" label="APPLYING">审核中</el-radio>
          <el-radio @change="radioChange()" label="SUCCESS">成功</el-radio>
          <el-radio @change="radioChange()" label="FAIL">失败</el-radio>
        </el-radio-group>
      </div>
      <el-button type="success" round @click="exportExcel()">导出本页</el-button>
    </div>

    <el-table id="out-table" :data="list" v-loading="listLoading" element-loading-text="给我一点时间" border fit highlight-current-row
              style="width: 100%;margin-top: 20px">
      <el-table-column align="center" label="用户名">
        <template slot-scope="scope">
          <span>{{scope.row.user.name}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="手机号">
        <template slot-scope="scope">
          <span>{{scope.row.user.mobile}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="账户当前余额(元)">
        <template slot-scope="scope">
          <span>{{scope.row.account.moneyBalance}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="提现金额(元)">
        <template slot-scope="scope">
          <span>{{scope.row.amount}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="手续费率">
        <template slot-scope="scope">
          <span>{{scope.row.poundage}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="到账金额">
        <template slot-scope="scope">
          <span>{{scope.row.amount * (1 - scope.row.poundage)}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="银行卡">
        <template slot-scope="scope">
          <span>{{scope.row.bankcard}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="开户行">
        <template slot-scope="scope">
          <span>{{scope.row.openBank}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间">
        <template slot-scope="scope">
          <span>{{conversionTime(scope.row.createdAt) |  parseTime('{y}-{m}-{d} {h}:{i}')}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" v-if=" status == 'APPLYING'">
        <template slot-scope="scope">
          <el-button @click="adopt(scope.row.id)" size="small" type="success" icon="el-icon-check">通过</el-button>
          <el-button @click="noAdopt(scope.row.id)" size="small" type="danger" icon="el-icon-close">未通过</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态" v-if="state">
        <template slot-scope="scope">
          <span>{{validStatus(scope.row.status)}}</span>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-container page-list">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.number" :page-sizes="[10,12,14]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="listQuery.totalElements">
      </el-pagination>
    </div>
  </div>
</template>

<script>
  import axios from 'axios'
  import qs from 'qs';
  import XLSX from 'xlsx'
  import FileSaver from 'file-saver'
  export default {
    name: 'withdrawals',
    data(){
      return{
        status:'APPLYING',
        state: false,
        list:[],
        listLoading:true,
        listQuery:{
          limit:10,
          totalElements:null,
          number:null
        }
      }
    },
    mounted: function () {
      this.pagelist(this.listQuery.limit,0)
    },
    methods:{
      //分页
      pagelist(size,page){
        let status = this.status;
        axios.get('/admin/withdrawMoney/list?sort=id,desc&size='+size+'&page='+page+'&status='+status).then((response)=>{
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
        this.listQuery.limit = val;
        this.pagelist(this.listQuery.limit,0)
      },
      handleCurrentChange(val){
        this.pagelist(this.listQuery.limit,val-1)
      },
      radioChange(){
        this.pagelist(this.listQuery.limit,0);
        if(this.status == ''){
          this.state = true;
        }else{
          this.state = false;
        }
      },
      conversionTime(val){
        //转化为时间搓，后台直接给的时间进过滤器有问题
        var date = new Date(val); //时间对象
        var str = date.getTime(); //转换成时间戳
        str = str / 1000;
        return str;
      },
      adopt(id) {
        //通过
        this.$confirm('是否给与该用户提现审核通过', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {

          let data = qs.stringify({
            id: id,
          })
          axios.post('/admin/withdrawMoney/success',data, {headers:{ 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8' }}).then((response)=>{
            console.log(response)
            this.pagelist(this.listQuery.limit,0)
            this.$message({
              type: 'success',
              message: '审核通过成功!'
            });

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
      noAdopt(id) {
        //未通过
        this.$confirm('是否给与该用户充值审核未通过', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {

          let data = qs.stringify({
            id: id,
          })
          axios.post('/admin/withdrawMoney/fail',data, {headers:{ 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8' }}).then((response)=>{
            console.log(response)
            this.pagelist(this.listQuery.limit,0)
            this.$message({
              type: 'success',
              message: '审核未通过成功!'
            });

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
      exportExcel() {
        let wb = XLSX.utils.table_to_book(document.querySelector('#out-table'));
        console.log(wb)
        wb.Sheets.Sheet1['!ref'] = "A1:I" + this.listQuery.totalElements + 1;
        let wbout = XLSX.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'});
        try {
          let status = [{key: '全部记录', val: ''}, {key: '审核中记录', val: 'APPLYING'}, {
            key: '成功记录',
            val: 'SUCCESS'
          }, {key: '失败记录', val: 'FAIL'}];
          let val = '全部记录';
          for (let i = 0; i < status.length; i++) {
            if (status[i].val == this.status) {
              val = status[i].key;
            }
          }
          FileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), '账户充值' + val + '.xlsx');
        } catch (e) {
          if (typeof console !== 'undefined') console.log(e, wbout)
        }
        return wbout;
      },
      validStatus(key) {
        let status = [{key: '审核中', val: 'APPLYING'}, {key: '成功', val: 'SUCCESS'}, {key: '失败', val: 'FAIL'}];
        let val = '';
        for (let i = 0; i < status.length; i++) {
          if (status[i].val == key) {
            val = status[i].key;
          }
        }
        return val;
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
