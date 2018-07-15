<template>
  <div class="app-container">
      <div class="operation-container">
          <el-button class="filter-item" type='primary' @click="addRole()" icon="el-icon-edit" v-show="menus.indexOf('account.role.add')!==-1">添加</el-button>
      </div>

    <el-table :key='tableKey' :data='list' v-loading="listLoading" element-loading-text="加载中" border fit highlight-current-row style="width:100%;">
        <el-table-column align="center" width="65" label="序号">
            <template slot-scope="scope">
                <span>{{scope.$index+1}}</span>
            </template>
        </el-table-column>
        <el-table-column align="center" label="角色名" width="150px">
            <template slot-scope="scope">
                <span>{{scope.row.name}}</span>
            </template>
        </el-table-column>
        <el-table-column align="center" label="角色key" width="150px">
            <template slot-scope="scope">
                <span>{{scope.row.roleKey}}</span>
            </template>
        </el-table-column>
        <el-table-column align="center" label="描述" width="150px">
            <template slot-scope="scope">
                <span>{{scope.row.description}}</span>
            </template>
        </el-table-column>
        <el-table-column align="center" label="创建时间" width="200px">
            <template slot-scope="scope">
                 <i class="el-icon-time"></i>
                <span>{{scope.row.createTime}}</span>
            </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
            <template slot-scope="scope">
                <el-button type="primary" @click='modifyRole(scope.row)' size="small" v-show="menus.indexOf('account.role.modify')!==-1">编辑</el-button>
                <el-button type="primary" @click='grantPermission(scope.row)' size="small" v-show="menus.indexOf('account.role.grant')!==-1">角色权限</el-button>
                <el-button type="danger" @click='deleteRole(scope.row)' size="mini" v-show="menus.indexOf('account.role.delete')!==-1">删除</el-button>
            </template>
      </el-table-column>
    </el-table>
    <!-- 新建弹窗 -->
  <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" center width="25%">
      <el-form :rules="rules" ref="dataForm" :model="temp" label-position="left" label-width="90px" style='width:80%;margin-left:8%;'>
          <el-form-item label="角色名" prop="name">
              <el-input v-model="temp.name"></el-input>
          </el-form-item>
          <el-form-item label="角色key" prop="roleKey">
              <el-input v-model="temp.roleKey"></el-input>
           </el-form-item>
          <el-form-item label="角色描述">
              <el-input v-model="temp.description"></el-input>
           </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button v-if="dialogStatus=='create'" type="primary" @click="saveAddRole()">添加</el-button>
          <el-button v-else type="primary" @click="saveModifyRole()">更新</el-button>
      </div>
  </el-dialog>

    <!-- 分配权限 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="permissionFormVisible" center width="25%">
        <el-form :rules="rules" ref="dataForm" :model="temp" label-position="left" label-width="90px" style='width:250px;margin-left:50px;'>
            <el-tree
              :data="permissions"
              show-checkbox
              node-key="id"
              :default-expanded-keys="[110000,120000,130000]"
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

  </div>



</template>
<script>
import {
  getRoleList,
  addRole,
  getRole,
  deteleRole,
  modifyRole,
  getMenuList,
  assignRoleMenu
} from '@/api/account'
import { mapGetters } from 'vuex'

export default {
  name: 'role',
  data() {
    const roleNameCheck = (rule, value, callback) => {
      if (value === undefined || value.length < 0) {
        callback(new Error('角色名不能为空'))
      } else {
        callback()
      }
    }
    const roleKeyCheck = (rule, value, callback) => {
      if (value === undefined || value.length < 0) {
        callback(new Error('角色Key不能为空'))
      } else {
        callback()
      }
    }
    return {
      listLoading: true,
      listQuery: {
        name: undefined
      },
      roleList: ['admin', 'normal'],
      rules: {
        name: [{ required: true, trigger: 'blur', validator: roleNameCheck }],
        roleKey: [{ required: true, trigger: 'blur', validator: roleKeyCheck }]
      },
      textMap: {
        create: '新建角色',
        modify: '修改角色',
        permission: '分配权限'
      },
      dialogStatus: '',
      dialogFormVisible: false,
      permissionFormVisible: false,
      temp: {
        id: undefined,
        username: undefined,
        password: undefined
      },
      tableKey: 0,
      list: null,
      permissions: [],
      hasPermissions: undefined,
      defaultProps: {
        children: 'children',
        label: 'name'
      }
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
      getRoleList(this.listQuery).then(response => {
        this.list = response.dataList
        this.listLoading = false
      })
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        username: undefined,
        password: undefined
      }
    },
    saveAddRole() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          this.temp.deleted = 0
          addRole(this.temp).then(reponse => {
            this.$message({
              message: '添加角色成功',
              type: 'success'
            })
            this.dialogFormVisible = false
            this.fetchData()
          })
        }
      })
    },
    addRole() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
    },
    modifyRole(row) {
      this.temp = row
      this.dialogStatus = 'modify'
      this.dialogFormVisible = true
    },
    saveModifyRole() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          this.temp.deleted = 0
          modifyRole(this.temp).then(reponse => {
            this.$message({
              message: '修改成功',
              type: 'success'
            })
            this.dialogFormVisible = false
            this.fetchData()
          })
        }
      })
    },
    grantPermission(row) {
      this.permissionFormVisible = true
      this.dialogStatus = 'permission'
      this.temp = row
      this.hasPermissions = []
      getMenuList().then(reponse => {
        this.permissions = reponse.dataList
      })
      getRole(row.id).then(response => {
        console.log(response.data.menus)
        const rolePermission = []
        Array.from(response.data.menus).forEach(function(menu) {
          rolePermission.push(menu.id)
        })
        this.hasPermissions = rolePermission
        this.$refs.tree.setCheckedNodes(rolePermission)
      })
      this.defaultProps = {
        children: 'children',
        label: 'name'
      }
    },
    savePermission() {
      const permissionArray = []
      Array.from(this.$refs.tree.getCheckedKeys()).forEach(function(id) {
        permissionArray.push({ id: id, hasPermission: true })
      })
      Array.from(this.$refs.tree.getHalfCheckedKeys()).forEach(function(id) {
        permissionArray.push({ id: id, hasPermission: true })
      })
      this.temp.menus = permissionArray
      assignRoleMenu(this.temp).then(response => {
        this.$message({
          message: '分配权限成功',
          type: 'success'
        })
        this.permissionFormVisible = false
      })
    },
    deleteRole(row) {
      this.$confirm('删除该角色影响对应用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'error'
      })
        .then(() => {
          deteleRole({ id: row.id }).then(response => {
            this.$message({
              message: '删除角色成功',
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
</script>

