<template>
  <div class="components-container">

    <el-table :data="list" v-loading="listLoading" element-loading-text="给我一点时间" border fit highlight-current-row
              style="width: 100%;margin-top: 20px">

      <el-table-column align="center" label="修改前价格">
        <template slot-scope="scope">
          <span>{{JSON.parse(scope.row.diff).oldValue}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="修改后价格">
        <template slot-scope="scope">
          <span>{{JSON.parse(scope.row.diff).newValue}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="修改时间">
        <template slot-scope="scope">
          <span>{{conversionTime(scope.row.createdAt) |  parseTime('{y}-{m}-{d} {h}:{i}')}}</span>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-container page-list">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange"
                     :current-page="listQuery.number" :page-sizes="[10,20,30]" :page-size="listQuery.limit"
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

  export default {
    name: "index",
    data() {
      return {
        imgsrc: null,
        voucherShow: false,
        status: 'APPLYING',
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
        let status = this.status;
        axios.get('/admin/bpPriceLog/list?sort=id,desc&size=' + size + '&page=' + page + '&status=' + status).then((response) => {
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
