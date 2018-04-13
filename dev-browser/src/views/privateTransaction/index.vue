<template>
  <div class="components-container">

    <div style="display: flex;justify-content: space-between;">
      <p style="color: #606266;">手续费总和: {{totalMoney}}</p>
      <el-button type="success" round @click="exportExcel()">导出本页</el-button>
    </div>
    <el-table id="out-table" :data="list" v-loading="listLoading" element-loading-text="给我一点时间" border fit highlight-current-row
              style="width: 100%;margin-top: 20px">
      <el-table-column align="center" label="转让人">
        <template slot-scope="scope">
          <span>{{scope.row.fromUser.name}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="接收人">
        <template slot-scope="scope">
          <span>{{scope.row.receiveUser.name}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="转出积分个数">
        <template slot-scope="scope">
          <span>{{scope.row.number}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="手续费率">
        <template slot-scope="scope">
          <span>{{scope.row.rate}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="手续费">
        <template slot-scope="scope">
          <span>{{scope.row.poundage}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="转出时间">
        <template slot-scope="scope">
          <span>{{conversionTime(scope.row.createdAt) |  parseTime('{y}-{m}-{d} {h}:{i}')}}</span>
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
      title="凭证"
      :visible.sync="voucherShow"
      width="50%"
      center>
      <img style="width: 100%" :src="'uploads/'+imgsrc">
      <span slot="footer" class="dialog-footer">
  </span>
    </el-dialog>
  </div>
</template>

<script>
  import axios from 'axios'
  import XLSX from 'xlsx'
  import FileSaver from 'file-saver'

  export default {
    name: "index",
    data() {
      return {
        imgsrc: null,
        voucherShow: false,
        status: 'APPLYING',
        totalMoney: 0,
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
      this.pagelist(this.listQuery.limit, 0);
      this.getTotalMoney();
    },
    methods: {
      //分页
      pagelist(size, page) {
        let status = this.status;
        axios.get('/admin/transferlog/list?sort=id,desc&size=' + size + '&page=' + page + '&status=' + status).then((response) => {
          console.log(response);
          this.list = response.data.content;
          this.listLoading = false;
          this.listQuery.totalElements = response.data.totalElements;
          this.listQuery.number = response.data.number + 1;
        }).catch((error) => {
          this.$message({
            message: error.message,
            type: 'error',
            duration: 5 * 1000
          });
          this.loading = false
        })
      },
      handleSizeChange(val) {
        this.listQuery.limit = val;
        this.pagelist(this.listQuery.limit, 0)
      },
      handleCurrentChange(val) {
        this.pagelist(this.listQuery.limit, val - 1)
      },
      conversionTime(val) {
        //转化为时间搓，后台直接给的时间进过滤器有问题
        var date = new Date(val); //时间对象
        var str = date.getTime(); //转换成时间戳
        str = str / 1000;
        return str;
      },
      getTotalMoney(){
        axios.get('/admin/statisticAll').then((response) => {
          console.log(response);
          for(let i = 0;i < response.data.length;i++){
            if( response.data[i].key == 'TRANSFER_TOTAL_POUNDAGE'){
              this.totalMoney = parseFloat(response.data[i].value).toFixed(2);
            }
          }

        }).catch((error) => {
          this.$message({
            message: error.message,
            type: 'error',
            duration: 5 * 1000
          });
          this.loading = false
        })
      },
      exportExcel() {
        let wb = XLSX.utils.table_to_book(document.querySelector('#out-table'));
        let wbout = XLSX.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'});
        try {
          FileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), '私募积分交易记录.xlsx');
        } catch (e) {
          if (typeof console !== 'undefined') console.log(e, wbout)
        }
        return wbout;
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
