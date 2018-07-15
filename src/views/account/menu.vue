<template>
  <div class="app-container">
      <tree-table :data="menus" :columns="columns" :operateFunc="operateFunc" border></tree-table>

      <!-- 新建弹窗 -->
      <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" center width="25%">
          <el-form :rules="rules" ref="dataForm" :model="temp" label-position="left" label-width="90px" style='width:80%;margin-left:8%;'>
              <el-form-item label="菜单名" prop="name">
                  <el-input v-model="temp.name"></el-input>
              </el-form-item>
              <el-form-item label="菜单链接">
                  <el-input v-model="temp.url"></el-input>
              </el-form-item>
              <el-form-item label="菜单描述">
                  <el-input v-model="temp.description"></el-input>
              </el-form-item>
              <el-form-item label="菜单状态">
                 <el-switch v-model="temp.disable" active-text="启用" inactive-text="禁用"></el-switch>
              </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
              <el-button @click="dialogFormVisible = false">取消</el-button>
              <el-button v-if="dialogStatus=='create'" type="primary" @click="savaAddMenu()">添加</el-button>
              <el-button v-else type="primary" @click="saveModifyMenu()">更新</el-button>
          </div>
      </el-dialog>
  </div>
</template>
<script>
import treeTable from '@/components/TreeTable'
import { getMenuList, modifyMenu } from '@/api/account'
import { mapGetters } from 'vuex'

export default {
  name: 'treeTableDemo',
  components: { treeTable },
  data() {
    const isValidateName = (rule, value, callback) => {
      if (value === undefined || value.length < 0) {
        callback(new Error('菜单名不能为空'))
      } else {
        callback()
      }
    }
    const isValidateKey = (rule, value, callback) => {
      if (value === undefined || value.length < 0) {
        callback(new Error('菜单Key不能为空'))
      } else {
        callback()
      }
    }
    return {
      columns: [
        {
          text: '菜单名',
          value: 'name',
          width: 200
        },
        {
          text: '菜单类型',
          value: 'type'
        },
        {
          text: '菜单Key',
          value: 'menuKey'
        },
        {
          text: '菜单链接',
          value: 'url'
        },
        {
          text: '状态',
          value: 'status'
        },
        {
          text: '描述',
          value: 'description'
        },
        {
          text: '操作',
          value: ''
        }
      ],
      menus: [],
      operateFunc: [
        {
          name: '编辑',
          operate: this.modify
        }
      ],
      dialogStatus: '',
      dialogFormVisible: false,
      temp: {
        id: undefined,
        name: undefined,
        url: undefined,
        description: undefined,
        disable: true
      },
      textMap: {
        create: '新建',
        modify: '修改'
      },
      rules: {
        name: [{ required: true, trigger: 'blur', validator: isValidateName }],
        menuKey: [{ required: true, trigger: 'blur', validator: isValidateKey }]
      }
    }
  },
  created() {
    this.fetchData()
  },
  computed: {
    ...mapGetters({ userMenus: 'menus' })
  },
  methods: {
    fetchData() {
      getMenuList().then(reponse => {
        this.changeMenu(reponse.dataList)
        this.menus = reponse.dataList
        if (this.userMenus.indexOf('account.menu.modify') === -1) {
          // 移除权限操作
          this.operateFunc = []
        }
      })
    },
    modify(row) {
      this.temp.id = row.id
      this.temp.name = row.name
      this.temp.url = row.url
      this.temp.disable = row.deleted === 0
      this.temp.description = row.description
      this.dialogStatus = 'modify'
      this.dialogFormVisible = true
    },
    saveModifyMenu() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          this.temp.deleted = this.temp.disable === false
          modifyMenu(this.temp).then(response => {
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
    changeState(row) {},
    changeMenu(menus) {
      Array.from(menus).forEach(menu => {
        menu.type = menu.type === 3 ? '按钮' : '菜单'
        menu.status = menu.deleted === 0 ? '启用' : '禁用'
        if (Array.isArray(menu.children)) {
          this.changeMenu(menu.children)
        }
      })
    }
  }
}
</script>
