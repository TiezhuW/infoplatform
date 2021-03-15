<template>
  <div>
    <h1 align="center">二手房信息爬取</h1>

    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="爬取平台" prop="platform">
        <el-select placeholder="请选择爬取平台" v-model="ruleForm.platform">
          <el-option label="安居客" value="安居客"></el-option>
          <el-option label="贝壳" value="贝壳"></el-option>
          <el-option label="房多多" value="房多多"></el-option>
          <el-option label="房天下" value="房天下"></el-option>
          <el-option label="我爱我家" value="我爱我家"></el-option>
          <el-option label="诸葛找房" value="诸葛找房"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="城市" prop="city">
        <el-input v-model="ruleForm.city"></el-input>
      </el-form-item>
      <el-form-item label="爬取页数" prop="pages">
        <el-input v-model.number="ruleForm.pages" type="integer"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="submitForm('ruleForm')" type="primary" icon="el-icon-attract" :loading="isLoading">爬取</el-button>
        <el-button @click="resetForm('ruleForm')" icon="el-icon-refresh">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "Crawler",

  data() {
    return {
      isLoading: false,
      ruleForm: {
        city: '',
        pages: '',
        platform: ''
      },
      rules: {
        city: [
          { required: true, message: '请输入市名称', trigger: 'blur' }
        ],
        pages: [
          { type: 'integer', min: 0, required: true, message: '请输入爬取页数（大于0的整数）', trigger: 'blur' }
        ],
        platform: [
          { required: true, message: '请选择爬取平台', trigger: 'change' }
        ]
      }
    };
  },

  methods: {
    submitForm(formName) {
      this.isLoading = true
      const _this = this
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.post(
              "/house/crawl/"
              + this.ruleForm.platform
              + '/'
              + this.ruleForm.city
              + '/'
              + this.ruleForm.pages
          ).then(function (resp) {
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
                _this.isLoading = false
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