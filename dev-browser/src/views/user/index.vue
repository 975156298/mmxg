<template>
  <div class="components-container">
    <div style="display: flex;justify-content: space-between;margin-bottom: 20px">
      <div class="demo-input-suffix" style="display: flex">
        <el-input
          placeholder="请输入手机号"
          prefix-icon="el-icon-search"
          v-model="input">
        </el-input>
        <el-button type="success" @click="searchPhone()" style="margin-left: 20px">查询</el-button>

      </div>
      <el-button type="success" round @click="exportExcel()">导出本页</el-button>
    </div>
    <el-table id="out-table" :data="list" v-loading="listLoading" element-loading-text="给我一点时间" border fit
              highlight-current-row
              style="width: 100%;">
      <!--<el-table-column width="100" align="center" label="ID">
        <template slot-scope="scope">
          <span>{{scope.row.id}}</span>
        </template>
      </el-table-column>-->
      <el-table-column align="center" label="用户名" width="140">
        <template slot-scope="scope">
          <span>{{scope.row.name}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="手机号">
        <template slot-scope="scope">
          <span>{{scope.row.mobile}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="角色" width="120">
        <template slot-scope="scope">
          <span>{{scope.row.type =='ADMIN' ?'管理员':'用户'}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="电子钱包地址">
        <template slot-scope="scope">
          <span>{{scope.row.walletAddress}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="私募积分" width="120">
        <template slot-scope="scope">
          <span>{{scope.row.bonusPoint.privatePlacement}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态" width="80">
        <template slot-scope="scope">
          <span>{{scope.row.status =="NORMAL"?"正常":"锁定"}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button size="small" type="success" icon="el-icon-edit-outline" @click="privateFn(scope.row.id)">新增私募积分
          </el-button>
          <el-button size="small" type="success" icon="el-icon-edit-outline" @click="resetPsw(scope.row.id)">重置密码
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-container page-list">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange"
                     :current-page="listQuery.number" :page-sizes="[10,20,30,500]" :page-size="listQuery.limit"
                     layout="total, sizes, prev, pager, next, jumper" :total="listQuery.totalElements">
      </el-pagination>
    </div>

    <el-dialog
      title="新增私募积分"
      :visible.sync="dialogVisible"
      width="30%">
      <el-form :model="ruleForm" status-icon label-width="100px" class="demo-ruleForm">
        <div style="text-align: left;font-size: 16px;margin-bottom: 10px;margin-left: 5px">当前私募积分:{{PrivateBP}}</div>
        <el-form-item label="新增私募积分" prop="pass">
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
  import axios from 'axios'
  import qs from 'qs';
  import XLSX from 'xlsx'
  import FileSaver from 'file-saver'

  export default {
    name: 'user',
    data() {
      return {
        userid: null,
        input: '',
        PrivateBP: 0,
        dialogVisible: false,
        ruleForm: {
          number: null
        },
        list: [],
        listLoading: true,
        listQuery: {
          limit: 10,
          totalElements: null,
          number: null
        }
      }
    },
    mounted: function () {
      this.pagelist(this.listQuery.limit, 0)
    },
    methods: {
      //分页
      pagelist(size, page) {
        let url = '/admin/user/list?sort=id,desc&size=' + size + '&page=' + page;
        if (this.input != '') {
          url = '/admin/user/list?sort=id,desc&size=' + size + '&page=' + page + '&mobile=' + this.input;
        }
        axios.get(url).then((response) => {
          console.log(response)
          this.list = response.data.content;
          this.listLoading = false
          this.listQuery.totalElements = response.data.totalElements
          this.listQuery.number = response.data.number + 1;

        }).catch((error) => {
          this.$message({
            message: error.message,
            type: 'error',
            duration: 5 * 1000
          })
          this.loading = false
        })
      },
      handleSizeChange(val) {
        this.listQuery.limit = val
        this.pagelist(this.listQuery.limit, 0)
      },
      handleCurrentChange(val) {
        console.log(val)
        this.pagelist(this.listQuery.limit, val - 1)
      },
      privateFn(userid) {
        this.userid = userid;
        this.dialogVisible = true;
        axios.get('/admin/bp/bpInfoByUser?userid=' + userid).then((response) => {
          console.log(response)
          this.PrivateBP = response.data.data.privatePlacement
        }).catch((error) => {
          this.$message({
            message: error.message,
            type: 'error',
            duration: 5 * 1000
          })
          this.loading = false
        })
      },
      resetPsw(userid) {
        this.userid = userid;
        axios.post('/admin/user/resetpwd', qs.stringify({user_id: userid})).then((response) => {

          if (response.data.statusCode == 0) {
            this.$alert('新密码为: 123456', '新密码', {
              confirmButtonText: '确定'
            });
          } else {
            this.$message({
              message: '重置密码失败。',
              type: 'error',
              duration: 5 * 1000
            })
          }


        }).catch((error) => {
          this.$message({
            message: error.message,
            type: 'error',
            duration: 5 * 1000
          })
          this.loading = false
        })
      },
      submit() {
        let data = qs.stringify({
          uid: this.userid,
          privateBPNumber: this.ruleForm.number
        })
        axios.post('/admin/bp/updatePrivateBP', data, {headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'}}).then((response) => {
          console.log(response)
          this.pagelist(this.listQuery.limit, 0)
          this.dialogVisible = false;
          this.ruleForm.number = null;
          this.$message({
            type: 'success',
            message: '私募积分新增成功!'
          });

        }).catch((error) => {
          this.$message({
            message: error.message,
            type: 'error',
            duration: 5 * 1000
          })
          this.loading = false
        })
      },
      exportExcel() {

        let wb = XLSX.utils.table_to_book(document.querySelector('#out-table'));

        console.log(wb)
        wb.Sheets.Sheet1['!rows'] = {w: 600}
        wb.Sheets.Sheet1['!ref'] = "A1:F" + (parseInt(this.listQuery.totalElements) + 1);
        let wbout = XLSX.write(wb, {bookType: 'xlsx', bookSST: false, type: 'array'});
        try {
          FileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), '用户管理.xlsx');
        } catch (e) {
          if (typeof console !== 'undefined') console.log(e, wbout)
        }
        return wbout;
      },
      searchPhone() {
        this.pagelist(this.listQuery.limit, 0);
      }
    }
  }
</script>

<style scoped>
  .components-container {
    position: relative;
    padding: 20px;
    min-height: calc(100vh - 84px);
  }

  .page-list {
    position: absolute;
    bottom: 40px;
    right: 15px;
  }
</style>
