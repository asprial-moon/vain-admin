<template>
  <div class="app-container">
        <el-form :rules="rules" ref="dataForm" :model="temp" label-position="left" label-width="90px" style='width:30%;margin-left:3%;'>
            <el-form-item label="原密码" prop="password">
                <el-input v-model="temp.password" type="password" placeholder="请输入现在的密码"></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newpasswd">
                <el-input v-model="temp.newpasswd" type="password" placeholder="请输入新的密码 长度为5-20"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="newRepeatpasswd">
                <el-input v-model="temp.newRepeatpasswd" type="password" placeholder="请再次输入新密码"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="saveModifyPassword()">确定</el-button>
                <el-button plain @click="cancel()">取消</el-button>
            </el-form-item>
        </el-form>
  </div>

</template>
<script>
import { mapGetters } from 'vuex'
import { validatePassword } from '@/utils/validate'
import { modifyPersonPassword } from '@/api/account'

export default {
  computed: {
    ...mapGetters(['visitedViews'])
  },
  name: 'modifyPersonPassword',
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
      if (value !== this.temp.newpasswd) {
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
          { required: true, trigger: 'blur', validator: isValidatePassword }
        ],
        newRepeatpasswd: [
          { required: true, trigger: 'blur', validator: isSamePasssword }
        ]
      },
      temp: {
        passwd: undefined,
        newpasswd: undefined,
        newRepeatpasswd: undefined
      }
    }
  },
  created() {},
  methods: {
    saveModifyPassword() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          modifyPersonPassword(this.temp).then(response => {
            this.$message({
              message: '修改成功',
              type: 'success'
            })
          })
        }
      })
    },
    cancel() {
      this.$store
        .dispatch('delVisitedViews', {
          path: '/account/modifyPersonPassword',
          name: '修改密码',
          meta: { title: '修改密码' }
        })
        .then(() => {
          const latestView = this.visitedViews.slice(-1)[0]
          if (latestView) {
            this.$router.push(latestView.path)
          } else {
            this.$router.push('/')
          }
        })
    }
  }
}
</script>

