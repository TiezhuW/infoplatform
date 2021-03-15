<template>
  <div>
    <h1 align="center">注册</h1>

    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="ruleForm.username"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="ruleForm.password" show-password></el-input>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="ruleForm.phone"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="ruleForm.email"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">注册</el-button>
        <el-button @click="resetForm('ruleForm')" icon="el-icon-refresh">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "Register",

  data() {
    return {
      ruleForm: {
        username: '',
        password: '',
        phone: '',
        email: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 14, message: '长度在 3 到 14 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 14, message: '长度在 6 到 14 个字符', trigger: 'blur' }
        ]
      }
    };
  },

  methods: {
    submitForm(formName) {
      const _this = this
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let user = {
            username: this.ruleForm.username,
            password: this.ruleForm.password
          }
          if (this.ruleForm.phone != '') {
            user.phone = this.ruleForm.phone
          }
          if (this.ruleForm.email != '') {
            user.email = this.ruleForm.email
          }
          axios.post('/user/register', user)
              .then(function (resp) {
                if (resp.data.success == true) {
                  _this.$message({
                    message: resp.data.msg,
                    type: 'success',
                    center: true
                  })
                } else {
                  _this.$message({
                    message: resp.data.msg,
                    type: 'error',
                    center: true
                  })
                }
              })
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>

<style scoped>
  .el-form-item {
    width: 80%
  }
</style>