<template>
  <div class="app-container">
      <div class="operation-container">
          <el-input @keyup.enter.native="search" style="width:200px;" class="filter-item" placeholder="请输入关键字" v-model="listQuery.operationData"></el-input>
          <el-select clearable style="width:150px" class="filter-item" v-model="listQuery.type" placeholder="选择操作类型">
              <el-option v-for="item in typeList" :key="item.name" :label="item.name" :value="item.type"></el-option>
          </el-select>
          <el-button class="filter-item" type='primary' icon="el-icon-search" @click="search">搜索</el-button>
          <el-button class="filter-item" type='danger' style="margin-left:10px" @click="deleteChooseLog" icon="el-icon-delete" v-show="menus.indexOf('system.log.delete')!==-1" >删除</el-button>
      </div>

      <el-table :data="list" v-loading.body="listLoading" element-loading-text="Loading" border fit highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column align="center"
      type="selection"
      width="55">
      </el-table-column>
      <el-table-column align="center" label="序号" width="80">
          <template slot-scope="scope">
              {{scope.$index+1}}
          </template>
      </el-table-column>
      <el-table-column label="操作人" width="100" align="center">
          <template slot-scope="scope">
              {{scope.row.userName}}
          </template>
      </el-table-column>
      <el-table-column label="操作信息" width="100" align="center">
          <template slot-scope="scope">
             <span> {{scope.row.info}}</span>
          </template>
      </el-table-column>
      <el-table-column label="操作数据" align="center">
          <template slot-scope="scope">
            <span v-html="scope.row.operationData"></span>
          </template>
      </el-table-column>
      <el-table-column label="操作时间" width="200" prop="created_at" align="center">
          <template slot-scope="scope">
              <i class="el-icon-time"></i>
              <span>{{scope.row.createTime}}</span>
          </template>
      </el-table-column>
      <el-table-column label="操作" width="100" align="center" v-if="menus.indexOf('system.log.delete')!==-1">
          <template slot-scope="scope">
            <el-button class="filter-item" type='danger' size="small"  @click="deleteChooseLog(scope.row.id)">删除</el-button>
          </template>
      </el-table-column>
    </el-table>
    <template>
    <div class="block page-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalPage">
        </el-pagination>
      </div>
  </template>
  </div>
</template>

<script>
import { getLogList, deleteLogs } from '@/api/system'
import { mapGetters } from 'vuex'

export default {
  name: 'log',
  data() {
    return {
      list: null,
      listLoading: true,
      currentPage: 1,
      pageSize: 20,
      totalPage: 0,
      listQuery: {
        operationData: undefined,
        type: undefined
      },
      typeList: [
        {
          name: '登录',
          type: [6]
        },
        {
          name: '数据操作',
          type: [1, 2, 3, 4, 5]
        }
      ],
      choonseLog: []
    }
  },
  computed: {
    ...mapGetters(['menus'])
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getLogList({
        currentPage: this.currentPage,
        pageSize: this.pageSize
      }).then(reponse => {
        this.list = reponse.dataList
        this.totalPage = reponse.totalSize
        this.currentPage = reponse.currentPage
        this.pageSize = reponse.pageSize
        this.listLoading = false
      })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.fetchData()
    },
    handleSelectionChange(val) {
      this.choonseLog = val
    },
    search() {
      this.listLoading = true
      getLogList({
        currentPage: this.currentPage,
        pageSize: this.pageSize,
        operationTypes: this.listQuery.type,
        operationData: this.listQuery.operationData
      }).then(reponse => {
        this.list = reponse.dataList
        this.totalPage = reponse.totalSize
        this.currentPage = reponse.currentPage
        this.pageSize = reponse.pageSize
        this.listLoading = false
      })
    },
    deleteChooseLog(val) {
      if (val > 0) {
        deleteLogs({ id: val }).then(response => {
          this.$message({
            message: '删除日志成功',
            type: 'success'
          })
          this.fetchData()
        })
      } else {
        this.$confirm('删除选中日志, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'error'
        })
          .then(() => {
            const deleteLog = []
            if (Array.isArray(this.choonseLog)) {
              Array.from(this.choonseLog).forEach(log => {
                deleteLog.push(log.id)
              })
            }
            deleteLogs({ ids: deleteLog }).then(response => {
              this.$message({
                message: '删除日志成功',
                type: 'success'
              })
              this.fetchData()
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            })
          })
      }
    }
  }
}
</script>