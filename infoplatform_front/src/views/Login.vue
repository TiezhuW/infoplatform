<template>
  <div>
    <h1 align="center">登录</h1>

    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="ruleForm.username"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="ruleForm.password" show-password></el-input>
      </el-form-item>
<!--      <el-form-item prop="isSaved">-->
<!--        <el-checkbox label="保持登录状态" v-model="ruleForm.isSaved"></el-checkbox>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
        <el-button @click="resetForm('ruleForm')" icon="el-icon-refresh">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "Login",

  data() {
    return {
      ruleForm: {
        username: '',
        password: '',
        // isSaved: false
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
          axios.post('/user/login', user)
              .then(function (resp) {
                if (resp.data.success == true) {
                  _this.$message({
                    message: resp.data.msg,
                    type: 'success',
                    center: true
                  })
                  localStorage.setItem('token', resp.data.obj)
                  window.location.href = '/'
                } else {
                  _this.$message({
                    message: resp.data.msg,
                    type: 'error',
                    dangerouslyUseHTMLString: true,
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