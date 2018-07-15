<template>
  <div class="app-container">
      <div class="operation-container">
          <el-input @keyup.enter.native="search" style="width:200px;" class="filter-item" placeholder="请输入用户名" v-model="listQuery.userName"></el-input>
          <el-select clearable style="width:150px" class="filter-item" v-model="listQuery.type" placeholder="选择用户类型">
              <el-option v-for="item in typeList" :key="item.name" :label="item.name" :value="item.type"></el-option>
          </el-select>
          <el-button class="filter-item" type='primary' icon="el-icon-search" @click="search">搜索</el-button>
          <el-button class="filter-item" type='primary' style="margin-left:10px" @click="createUser" icon="el-icon-edit" v-show="menus.indexOf('account.user.add')!==-1">添加</el-button>
      </div>

    <el-table :key='tableKey' :data='list' v-loading="listLoading" element-loading-text="加载中" border fit highlight-current-row style="width:100%;">
        <el-table-column align="center" width="65" label="序号">
            <template slot-scope="scope">
                <span>{{scope.$index+1}}</span>
            </template>
        </el-table-column>
        <el-table-column align="center" label="用户名" width="150px">
            <template slot-scope="scope">
                <span>{{scope.row.userName}}</span>
            </template>
        </el-table-column>
         <el-table-column align="center" label="类型" width="150px">
            <template slot-scope="scope">
                  <el-tag :type="scope.row.type | typeFilter">{{scope.row.type  | typeFilter}}</el-tag>
            </template>
        </el-table-column>
         <el-table-column align="center" label="角色" width="150px">
            <template slot-scope="scope">
                <el-tag v-for="role in scope.row.roles" :key="role.name" type="success"> {{role.name}}</el-tag>
            </template>
        </el-table-column>
        <el-table-column align="center" label="手机号" width="150px">
            <template slot-scope="scope">
                <span>{{scope.row.phone}}</span>
            </template>
        </el-table-column>
        <el-table-column align="center" label="邮箱" width="150px">
            <template slot-scope="scope">
                <span>{{scope.row.email}}</span>
            </template>
        </el-table-column>
         <el-table-column align="center" label="创建时间" width="200px" sortable prop="date">
            <template slot-scope="scope">
                 <i class="el-icon-time"></i>
                <span>{{scope.row.createTime}}</span>
            </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
            <template slot-scope="scope">
                <el-button type="primary" @click='modifyUser(scope.row)' size="small" v-show="menus.indexOf('account.user.modify')!==-1" >编辑</el-button>
                <el-button type="success" @click='resetPassword(scope.row)' size="small"  v-show="menus.indexOf('account.user.resetPwd')!==-1">重置密码</el-button>
                <el-button type="danger"  @click='grantPermission(scope.row)' size="small"  v-show="menus.indexOf('account.user.grant')!==-1">用户权限</el-button>
                <el-button type="publish" @click='lockUser(scope.row)' size="small"  v-show="menus.indexOf('account.user.lock')!==-1">{{scope.row.state==1?'解锁':'锁定'}}</el-button>
                <el-button type="danger"  @click='deleteUser(scope.row)' size="small" v-show="menus.indexOf('account.user.delete')!==-1" >删除</el-button>
            </template>
      </el-table-column>
    </el-table>
    <!-- 新建弹窗 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="createFormVisible" center width="25%">
        <el-form :rules="rules" ref="dataForm" :model="temp" label-position="left" label-width="90px" style='width:80%;margin-left:8%;'>
            <el-form-item label="用户名" prop="userName">
                <el-input v-model="temp.userName" type="text"></el-input>
              </el-form-item>
            <el-form-item label="用户密码" prop="password">
                <el-input v-model="temp.password" type="text"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="createFormVisible = false">取消</el-button>
            <el-button  type="primary" @click="saveCreate()" >添加</el-button>
        </div>
    </el-dialog>

    <!-- 修改弹窗 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="modifyFormVisible" center width="25%">
        <el-form :rules="rules" ref="dataForm" :model="temp" label-position="left" label-width="90px" style='width:80%;margin-left:8%;'>
            <el-form-item label="用户名" prop="userName">
                <el-input v-model="temp.userName" type="text"></el-input>
              </el-form-item>
            <el-form-item label="手机号" prop="phone">
                <el-input v-model="temp.phone" type="text"></el-input>
            </el-form-item>
            <el-form-item label="邮箱">
                <el-input v-model="temp.email" type="text"></el-input>
            </el-form-item>
            <el-form-item label="用户类型">
                <el-select clearable style="width:150px" class="filter-item" v-model="listQuery.type" placeholder="选择用户类型">
                <el-option v-for="item in typeList" :key="item.name" :label="item.name" :value="item.type"></el-option>
          </el-select>
          </el-form-item>
          <el-form-item label="用户角色">
                  <el-select v-model="temp.role" multiple placeholder="选择用户角色">
                  <el-option
                    v-for="item in roles"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="modifyFormVisible = false">取消</el-button>
            <el-button type="primary" @click="saveModify()">更新</el-button>
        </div>
    </el-dialog>

    <!-- 重置弹窗 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="resetPasswordFormVisible" center width="25%">
        <el-form :rules="rules" ref="dataForm" :model="temp" label-position="left" label-width="90px" style='width:80%;margin-left:8%;'>
            <el-form-item label="新设密码" prop="password">
                <el-input v-model="temp.password" type="text"></el-input>
              </el-form-item>
            <el-form-item label="重复密码" prop="newpasswd">
                <el-input v-model="temp.newpasswd" type="text"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="resetPasswordFormVisible = false">取消</el-button>
            <el-button  type="primary" @click="saveResetPassword()">重置</el-button>
        </div>
    </el-dialog>

    <!-- 分配权限 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="permissionFormVisible" center width="25%">
        <el-form :rules="rules" ref="dataForm" :model="temp" label-position="left" label-width="90px" style='width:80%;margin-left:5%;'>
            <el-tree
              :data="permissions"
              show-checkbox
              node-key="id"
              :default-expanded-keys="defaultExpanded"
              :default-checked-keys="hasPermissions"
              ref="tree"
              highlight-current
              :props="defaultProps">
            </el-tree>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="permissionFormVisible = false">取消</el-button>
            <el-button type="primary" @click="savePermission()">分配</el-button>
        </div>
    </el-dialog>

    <template>
    <div class="block page-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="1"
          layout="total, sizes, prev, pager, next, jumper"
          :total="20">
        </el-pagination>
      </div>
  </template>
  </div>

</template>

<script>
import {
  getUserList,
  getMenuList,
  getRoleList,
  lockUser,
  modifyUser,
  addUser,
  deleteUser,
  resetPassword,
  getMenusByUserId,
  assignUserMenu
} from '@/api/account'
import { validateUsername, validatePassword } from '@/utils/validate'
import { mapGetters } from 'vuex'

export default {
  name: 'user',
  data() {
    const isValidateUsername = (rule, value, callback) => {
      if (!validateUsername(value)) {
        callback(new Error('用户名过短'))
      } else {
        callback()
      }
    }
    const isValidatePassword = (rule, value, callback) => {
      if (value === undefined || value.length < 5) {
        callback(new Error('密码不能小于5位数'))
      } else if (!validatePassword(value)) {
        callback(new Error('密码过于简单'))
      } else {
        callback()
      }
    }
    const isSamePasssword = (rule, value, callback) => {
      if (value !== this.temp.password) {
        callback(new Error('二次密码不相同'))
      } else {
        callback()
      }
    }
    return {
      listLoading: true,
      listQuery: {
        name: undefined
      },
      typeList: [
        {
          name: '管理员',
          type: 2
        },
        {
          name: '普通用户',
          type: 3
        }
      ],
      rules: {
        userName: [
          { required: true, trigger: 'blur', validator: isValidateUsername }
        ],
        password: [
          { required: true, trigger: 'blur', validator: isValidatePassword }
        ],
        newpasswd: [
          { required: true, trigger: 'blur', validator: isSamePasssword }
        ]
      },
      textMap: {
        create: '新建用户',
        modify: '修改信息',
        resetPassword: '重置密码',
        permission: '分配权限'
      },
      dialogStatus: '',
      createFormVisible: false,
      modifyFormVisible: false,
      resetPasswordFormVisible: false,
      permissionFormVisible: false,
      temp: {
        id: undefined,
        userName: undefined,
        passwd: undefined,
        email: undefined,
        newpasswd: undefined,
        role: undefined
      },
      tableKey: 0,
      list: null,
      permissions: undefined,
      hasPermissions: undefined,
      defaultProps: undefined,
      currentPage: 1,
      pageSize: 20,
      totalPage: 0,
      roles: undefined,
      defaultExpanded: [110000, 120000, 130000]
    }
  },
  filters: {
    typeFilter(type) {
      const typeMap = {
        1: '超级管理员',
        2: '管理员',
        3: '普通用户'
      }
      return typeMap[type]
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
      getUserList(this.listQuery, this.currentPage, this.pageSize).then(
        response => {
          this.list = response.dataList
          this.currentPage = response.currentPage
          this.pageSize = response.pageSize
          this.totalPage = response.totalPage
          this.listLoading = false
        }
      )
      getRoleList().then(response => {
        this.roles = response.dataList
      })
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        userName: undefined,
        passwd: undefined
      }
    },
    createUser() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.createFormVisible = true
    },
    saveCreate() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          this.temp.type = 3
          addUser(this.temp).then(response => {
            this.$message({
              message: '添加成功',
              type: 'success'
            })
            this.createFormVisible = false
            this.fetchData()
          })
        }
      })
    },
    modifyUser(row) {
      this.temp = {
        id: row.id,
        userName: row.userName,
        email: row.email,
        phone: row.phone,
        type: row.type,
        role: undefined
      }
      this.dialogStatus = 'modify'
      this.modifyFormVisible = true
    },
    saveModify() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          if (this.temp.role) {
            const roles = []
            this.temp.role.forEach(function(value, key) {
              roles.push({ id: value })
            })
            this.temp.roles = roles
          }
          modifyUser(this.temp).then(response => {
            this.$message({
              message: '修改成功',
              type: 'success'
            })
            this.createFormVisible = false
          })
        }
      })
    },
    lockUser(row) {
      row.state = row.state === 1 ? 0 : 1
      lockUser({ id: row.id, state: row.state }).then(response => {
        this.$message({
          message: row.state === 0 ? '解锁成功' : '锁定成功',
          type: 'success'
        })
      })
    },
    deleteUser(row) {
      this.$confirm('确定删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error'
      })
        .then(() => {
          deleteUser({ id: row.id }).then(response => {
            this.$message({
              message: '删除成功',
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
    },
    resetPassword(row) {
      this.temp = {
        id: row.id,
        password: undefined,
        newpasswd: undefined
      }
      this.dialogStatus = 'resetPassword'
      this.resetPasswordFormVisible = true
    },
    saveResetPassword() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          resetPassword(this.temp).then(response => {
            this.$message({
              message: '重置成功',
              type: 'success'
            })
            this.resetPasswordFormVisible = false
          })
        }
      })
    },
    grantPermission(row) {
      this.permissionFormVisible = true
      this.dialogStatus = 'permission'
      this.temp = row
      this.permissions = []
      this.hasPermissions = []
      getMenuList().then(reponse => {
        this.permissions = reponse.dataList
      })
      getMenusByUserId({ id: row.id }).then(response => {
        const userPermission = []
        Array.from(response.dataList).forEach(function(menu) {
          userPermission.push(menu.id)
        })
        this.hasPermissions = userPermission
      })
      console.log(this.hasPermissions)
      this.defaultProps = {
        children: 'children',
        label: 'name'
      }
    },
    savePermission() {
      const permissionArray = []
      Array.from(this.$refs.tree.getCheckedKeys()).forEach(function(record) {
        permissionArray.push({ id: record, hasPermission: true })
      })
      Array.from(this.$refs.tree.getHalfCheckedKeys()).forEach(function(
        record
      ) {
        permissionArray.push({ id: record, hasPermission: true })
      })
      this.temp.menus = permissionArray
      assignUserMenu(this.temp).then(response => {
        this.$message({
          message: '分配权限成功',
          type: 'success'
        })
      })
    },
    search() {
      getUserList(this.listQuery, this.currentPage, this.pageSize).then(
        response => {
          this.list = response.dataList
          this.currentPage = response.currentPage
          this.pageSize = response.pageSize
          this.listLoading = false
          this.$message({
            message: '查询成功',
            type: 'success'
          })
        }
      )
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
