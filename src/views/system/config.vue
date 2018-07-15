<template>
  <div class="app-container">
      <el-table :data="list" v-loading.body="listLoading" element-loading-text="Loading" border fit highlight-current-row>
      <el-table-column align="center" label="序号" width="80">
          <template slot-scope="scope">
              {{scope.$index+1}}
          </template>
      </el-table-column>
      <el-table-column label="配置名称" width="300" align="center">
          <template slot-scope="scope">
              {{scope.row.name}}
          </template>
      </el-table-column>
      <el-table-column label="配置标识" width="300" align="center">
          <template slot-scope="scope">
             <span> {{scope.row.code}}</span>
          </template>
      </el-table-column>
      <el-table-column label="配置值" width="300" align="center">
          <template slot-scope="scope">
            <template v-if="scope.row.edit">
              <el-input class="edit-input" size="small" v-model="scope.row.value"></el-input>
              <el-button class="cancel-btn" size="small" icon="el-icon-refresh" type="warning" @click="cancelEdit(scope.row)">取消</el-button>
            </template>
            <span v-else> {{scope.row.value}}</span>
          </template>
      </el-table-column>
      <el-table-column label="配置描述"  align="center">
          <template slot-scope="scope">
              {{scope.row.description}}
          </template>
      </el-table-column>
      <el-table-column label="类型" width="100" align="center" class-name="status-col">
          <template slot-scope="scope">
             <el-tag :type="scope.row.valueType | statusFilter">{{scope.row.valueType | valueTypeFilter}}</el-tag>
          </template>
      </el-table-column>
      <el-table-column label="创建时间" width="200" prop="created_at" align="center">
          <template slot-scope="scope">
              <i class="el-icon-time"></i>
              <span>{{scope.row.createTime}}</span>
          </template>
      </el-table-column>
      <el-table-column label="操作" width="100" align="center">
          <template slot-scope="scope">
            <el-button v-if="scope.row.edit" type="success" @click="confirmEdit(scope.row)" size="small" icon="el-icon-circle-check-outline">提交</el-button>
            <el-button v-else type="primary" @click='scope.row.edit=!scope.row.edit' size="small" icon="el-icon-edit" v-show="menus.indexOf('system.config.modify')!==-1">编辑</el-button>
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
import { getConfigPageList, modifyConfig } from '@/api/system'
import { mapGetters } from 'vuex'

export default {
  name: 'config',
  data() {
    return {
      list: null,
      listLoading: true,
      currentPage: 1,
      pageSize: 20,
      totalPage: 0
    }
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        2: 'gray',
        3: 'danger'
      }
      return statusMap[status]
    },
    valueTypeFilter(status) {
      const valueTypeMap = {
        1: '文字',
        2: '数字'
      }
      return valueTypeMap[status]
    }
  },
  created() {
    this.fetchData()
  },
  computed: {
    ...mapGetters(['menus'])
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getConfigPageList(this.currentPage, this.pageSize).then(reponse => {
        const items = reponse.dataList
        this.currentPage = reponse.currentPage
        this.pageSize = reponse.pageSize
        this.totalPage = reponse.totalPage
        this.listLoading = false
        this.list = items.map(v => {
          this.$set(v, 'edit', false)
          v.originalValue = v.value
          return v
        })
      })
    },
    cancelEdit(row) {
      row.value = row.originalValue
      row.edit = false
      this.$message({
        message: '取消修改',
        type: 'warning'
      })
    },
    confirmEdit(row) {
      row.edit = false
      row.originalValue = row.value
      modifyConfig(row).then(reponse => {
        this.$message({
          message: '修改成功',
          type: 'success'
        })
      })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.fetchData()
    }
  }
}
</script>
<style scoped>
.edit-input {
  padding-right: 100px;
}
.cancel-btn {
  position: absolute;
  right: 15px;
  top: 10px;
}
</style>