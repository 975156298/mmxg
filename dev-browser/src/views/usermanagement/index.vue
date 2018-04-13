<template>
  <div class="components-container">
    <el-row>
      <!--<el-card class="box-card">-->
        <!--<div slot="header" class="clearfix">-->
          <!--<span>后台用户列表</span>-->
        <!--</div>-->
        <div style="margin-bottom:50px;">
          <el-form :inline="true" :model="formInline" class="demo-form-inline">
            <el-col :span="4" class="text-center">
              <el-form-item label="账号">
                <el-input v-model="formInline.user" placeholder="单行输入"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4" class="text-center">
              <el-form-item label="用户名">
                <el-input v-model="formInline.user" placeholder="单行输入"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5" class="text-center">
              <el-form-item label="用户状态">
                <el-select v-model="value" placeholder="请选择">
                  <el-option
                    v-for="item in formInline.state"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5" class="text-center">
              <el-row>
                <el-col :span="7" class="text-center">
                  <el-button>查询</el-button>
                </el-col>
                <el-col :span="7" class="text-center">
                  <el-button type="primary">新增</el-button>
                </el-col>
              </el-row>
            </el-col>
          </el-form>
        </div>
      <!--</el-card>-->
    </el-row>

      <el-table :key='tableKey' :data="list" v-loading="listLoading" element-loading-text="给我一点时间" border fit highlight-current-row
                style="width: 100%;">
        <el-table-column width="100" align="center" label="ID">
          <template slot-scope="scope">
            <span>{{scope.$index}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="账号">
          <template slot-scope="scope">
            <span>{{scope.row.username}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="用户名">
          <template slot-scope="scope">
            <span>{{scope.row.nickname}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="角色">
          <template slot-scope="scope">
            <span>{{scope.row.role}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="部门">
          <template slot-scope="scope">
            <span>{{scope.row.department}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="状态">
          <template slot-scope="scope">
            <span>{{scope.row.state}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="创建时间">
          <template slot-scope="scope">
            <span>{{scope.row.createtime}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="最后登录时间">
          <template slot-scope="scope">
            <span>{{scope.row.lasttime}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <el-button size="mini" icon="el-icon-edit-outline"></el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete"></el-button>
            <el-button size="mini" type="info" icon="fa fa-lock"></el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="listQuery.total">
        </el-pagination>
      </div>
  </div>
</template>

<script>
  export default {
    name: 'backgroundUser',
    data(){
      return{
        formInline:{
          user:'',
          state:[
            {value:'2',label:'全部'},
            {value:'1',label:'正常'},
            {value:'0',label:'锁定'}
          ]
        },
        value:'2',
        tableKey:0,
        listLoading:false,
        list:[
          {id:1,username:'13456376682',nickname:'皮皮',role:'客服',department:'海南门店',state:'锁定',createtime:'2017-8-12',lasttime:'2017-12-12'},
          {id:1,username:'13456376686',nickname:'皮皮aADAS',role:'客服',department:'海南门店',state:'正常',createtime:'2017-8-12',lasttime:'2017-12-12'},
          {id:1,username:'13456376111',nickname:'皮皮aADAS',role:'客服',department:'海南门店',state:'锁定',createtime:'2017-8-12',lasttime:'2017-12-12'},
          {id:1,username:'13456376333',nickname:'皮皮aADAS',role:'客服',department:'海南门店',state:'锁定',createtime:'2017-8-12',lasttime:'2017-12-12'},
          {id:1,username:'13456376682',nickname:'皮皮aADAS',role:'客服',department:'海南门店',state:'锁定',createtime:'2017-8-12',lasttime:'2017-12-12'},
          {id:1,username:'13456376682',nickname:'皮皮aADAS',role:'客服',department:'海南门店',state:'锁定',createtime:'2017-8-12',lasttime:'2017-12-12'},
          {id:1,username:'13456376682',nickname:'皮皮aADAS',role:'客服',department:'海南门店',state:'锁定',createtime:'2017-8-12',lasttime:'2017-12-12'},
          {id:1,username:'13456376682',nickname:'皮皮aADAS',role:'客服',department:'海南门店',state:'锁定',createtime:'2017-8-12',lasttime:'2017-12-12'},
          {id:1,username:'13456376682',nickname:'皮皮aADAS',role:'客服',department:'海南门店',state:'锁定',createtime:'2017-8-12',lasttime:'2017-12-12'},
          {id:1,username:'13456376682',nickname:'皮皮aADAS',role:'客服',department:'海南门店',state:'锁定',createtime:'2017-8-12',lasttime:'2017-12-12'},
          {id:1,username:'13456376682',nickname:'皮皮aADAS',role:'客服',department:'海南门店',state:'锁定',createtime:'2017-8-12',lasttime:'2017-12-12'},
          {id:1,username:'13456376682',nickname:'皮皮aADAS',role:'客服',department:'海南门店',state:'锁定',createtime:'2017-8-12',lasttime:'2017-12-12'}
        ],
        listQuery:{
          limit:10,
          page:1,
          total:2000
        }
      }
    },
    methods:{
      handleSizeChange(val){
        console.log(val)
      },
      handleCurrentChange(val){
        console.log(val)
      }
    }
  }
</script>

<style scoped>
  .components-container{
    padding: 20px;
    min-height: calc(100vh - 84px);
  }
</style>
