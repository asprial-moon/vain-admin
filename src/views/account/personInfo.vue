<template>
  <el-tabs tab-position="left" style="margin-top:2%">
    <el-tab-pane name="first" style="height:30px !important"  :model="information">
        <span slot="label" class="tableItem">基本信息</span>
        <span class="title">基本信息</span>
        <el-form label-width="80px" label-position="left" style="width:30%;max-width:400px">
          <el-form-item label=""></el-form-item>
          <el-form-item label="用户头像:">
                <el-upload class="upload-list" action="http://127.0.0.1/vain/api/upload/uploadPics" :show-file-list="false" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
                  <img v-if="information.avatar" :src="information.avatar" class="avatar">
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
          </el-form-item>
          <el-form-item label="登录名:">
              <span>{{information.userName}}</span>
          </el-form-item>
          <el-form-item label="昵称:">
                <el-input v-model="information.nickname" type="text" placeholder="" maxlength="15" size="small"></el-input>
          </el-form-item> 
          <el-form-item label="性别:">
                <el-radio v-model="information.sex" label=1>男</el-radio>
                <el-radio v-model="information.sex" label=2>女</el-radio>
          </el-form-item> 
          <el-form-item label="生日:">
                <el-date-picker
                  v-model="information.birthday"  format="yyyy 年 MM 月 dd 日"
                  type="date" value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期">
                </el-date-picker>
          </el-form-item> 
          <el-form-item label="注册时间:">
              <span>{{information.createTime}}</span>
          </el-form-item> 
          <el-form-item label="1">
                <el-button type="primary" @click="saveInfo()">保存</el-button>
          </el-form-item>
        </el-form>
    </el-tab-pane>
    <el-tab-pane name="second">
        <span slot="label" class="tableItem">安全设置</span>
         <span class="title">安全设置</span>
         <el-form label-width="80px" label-position="left" style="width:30%">
            <el-form-item label=""></el-form-item>
            <el-form-item label="绑定邮箱:">
              <span>{{information.email}}</span>
            </el-form-item>
            <el-form-item label="绑定手机:">
              <span>{{information.phone}}</span>
            </el-form-item>
         </el-form>
    </el-tab-pane>
  </el-tabs>
</template>
<script>
import { mapGetters } from 'vuex'
import { modifyPersonInfo } from '@/api/account'
export default {
  name: 'personInfo',
  computed: {
    ...mapGetters(['information'])
  },
  methods: {
    saveInfo() {
      modifyPersonInfo(this.information).then(response => {
        this.$message({
          message: '修改成功',
          type: 'success'
        })
      })
    },
    handleAvatarSuccess(res, file) {
      this.information.avatar = res.data.url
    },
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isLt2M
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.tableItem {
  padding-right: 120px;
  padding-left: 20px;
  font-size: 16px;
}
.title {
  font-size: 17px;
  padding-bottom: 50px;
}
.upload-list {
  -webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
  background: #fff;
  border: 1px solid transparent;
  border-radius: 4px;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
  display: inline-block;
  height: 100px;
  line-height: 60px;
  margin-right: 4px;
  overflow: hidden;
  position: relative;
  text-align: center;
  width: 100px;
  img {
    width: 100%;
    height: 100%;
  }
}
.upload-list-cover {
  background: rgba(0, 0, 0, 0.6);
  bottom: 0;
  left: 0;
  position: absolute;
  right: 0;
  top: 0;
  z-index: 100;
}
</style>




