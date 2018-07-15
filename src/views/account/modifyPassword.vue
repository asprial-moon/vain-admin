<template>
  <div class="app-container">
        <el-form :rules="rules" ref="dataForm" :model="temp" label-position="left" label-width="90px" style='width:20%;margin-left:3%;'>
            <el-form-item label="新设密码" prop="password">
                <el-input v-model="temp.password" type="password"></el-input>
              </el-form-item>
            <el-form-item label="重复密码" prop="newpasswd">
                <el-input v-model="temp.newpasswd" type="password"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" style="margin-left:7%;">
            <el-button  type="primary" @click="saveModifyPassword()">确定</el-button>
        </div>
  </div>

</template>
<script>
import { validatePassword } from '@/utils/validate'
import { modifyPassword } from '@/api/account'

export default {
  name: 'modifyPassword',
  data() {
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
      roleList: ['admin', 'normal'],
      rules: {
        password: [
          { required: true, trigger: 'blur', validator: isValidatePassword }
        ],
        newpasswd: [
          { required: true, trigger: 'blur', validator: isSamePasssword }
        ]
      },
      temp: {
        passwd: undefined,
        newpasswd: undefined
      }
    }
  },
  created() {},
  methods: {
    saveModifyPassword() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          modifyPassword(this.temp).then(response => {
            this.$message({
              message: '修改成功',
              type: 'success'
            })
          })
        }
      })
    }
  }
}
</script>

