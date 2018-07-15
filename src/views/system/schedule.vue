<template>
  <div class="app-container">
      <el-table :data="list" v-loading.body="listLoading" element-loading-text="Loading" border fit highlight-current-row>
      <el-table-column align="center" label="序号" width="80">
          <template slot-scope="scope">
              {{scope.$index+1}}
          </template>
      </el-table-column>
      <el-table-column label="任务描述"  align="center">
          <template slot-scope="scope">
            <template v-if="scope.row.edit">
              <el-input class="edit-input" size="small" v-model="scope.row.description"></el-input>
            </template>
            <span v-else> {{scope.row.description}}</span>
          </template>
      </el-table-column>
      <el-table-column label="cron表达式" width="300" align="center">
          <template slot-scope="scope">
            <template v-if="scope.row.edit">
              <el-input class="edit-input" size="small" v-model="scope.row.cronExpression"></el-input>
              <el-button class="cancel-btn" size="small" icon="el-icon-refresh" type="warning" @click="cancelEdit(scope.row)">取消</el-button>
            </template>
            <span v-else> {{scope.row.cronExpression}}</span>
          </template>
      </el-table-column>
      <el-table-column label="是否并发" width="100" align="center">
          <template slot-scope="scope">
             <el-switch v-model="scope.row.isConcurrent"  @change="modifyConcorrent(scope.row)" active-color="#13ce66"  inactive-color="#ff4949"></el-switch>
          </template>
      </el-table-column>
      <el-table-column label="状态" width="100" align="center" class-name="status-col">
          <template slot-scope="scope">
             <el-switch v-model="scope.row.jobStatus" @change="modifyStatus(scope.row)"  active-color="#13ce66"  inactive-color="#ff4949" ></el-switch>
          </template>
      </el-table-column>
      <el-table-column label="创建时间" width="200" prop="created_at" align="center">
          <template slot-scope="scope">
              <i class="el-icon-time"></i>
              <span>{{scope.row.createTime}}</span>
          </template>
      </el-table-column>
      <el-table-column label="修改时间" width="200" prop="created_at" align="center">
          <template slot-scope="scope">
              <i class="el-icon-time"></i>
              <span>{{scope.row.modifyTime}}</span>
          </template>
      </el-table-column>
      <el-table-column label="操作"  align="center">
          <template slot-scope="scope">
            <el-button v-if="scope.row.edit" type="success" @click="confirmEdit(scope.row)" size="small" icon="el-icon-circle-check-outline">提交</el-button>
            <el-button v-else type="primary" @click='scope.row.edit=!scope.row.edit' size="small" v-show="menus.indexOf('system.scheduleJob.modify')!==-1" >编辑</el-button>
            <el-button @click='trigger(scope.row)' size="small" v-show="menus.indexOf('system.scheduleJob.modify')!==-1" >触发</el-button>
            <el-button type="danger" @click='deleteDialog(scope.row)' size="small" v-show="menus.indexOf('system.scheduleJob.delete')!==-1" >删除</el-button>
          </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import {
  getScheduleList,
  modifySchedule,
  deleteSchedule,
  pauseSchedule,
  resumeSchedule,
  triggerSchedule
} from '@/api/system'
import { mapGetters } from 'vuex'

export default {
  name: 'config',
  data() {
    return {
      list: null,
      listLoading: true
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
      this.listLoading = false
      getScheduleList({}).then(reponse => {
        this.list = reponse.dataList.map(v => {
          this.$set(v, 'edit', false)
          v.originalValue = v.value
          v.isConcurrent = v.isConcurrent === 1
          v.jobStatus = v.jobStatus === 1
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
      modifySchedule(row).then(reponse => {
        this.$message({
          message: '修改成功',
          type: 'success'
        })
      })
    },
    deleteDialog(row) {
      this.$confirm('确定删除该配置, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error'
      })
        .then(() => {
          deleteSchedule(row).then(reponse => {
            this.$message({
              type: 'success',
              message: '删除成功!'
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
    },
    modifyConcorrent(row) {
      modifySchedule(row).then(reponse => {
        this.$message({
          message: row.isConcurrent ? '开启并发' : '禁止并发',
          type: 'success'
        })
      })
    },
    modifyStatus(row) {
      if (!row.jobStatus) {
        pauseSchedule(row).then(response => {
          this.$message({
            message: '暂停成功',
            type: 'success'
          })
        })
      } else {
        resumeSchedule(row).then(response => {
          this.$message({
            message: '开启成功',
            type: 'success'
          })
        })
      }
    },
    trigger(row) {
      triggerSchedule(row).then(response => {
        this.$message({
          message: '触发成功',
          type: 'success'
        })
      })
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