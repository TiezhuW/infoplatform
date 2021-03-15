<template>
  <div>
    <h1 align="center">二手房信息修改</h1>

    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="市" prop="city">
        <el-input v-model="ruleForm.city"></el-input>
      </el-form-item>
      <el-form-item label="区\县" prop="district">
        <el-input v-model="ruleForm.district"></el-input>
      </el-form-item>
      <el-form-item label="面积/㎡" prop="area">
        <el-input v-model.number="ruleForm.area" type="number"></el-input>
      </el-form-item>
      <el-form-item label="价格/元" prop="price">
        <el-input v-model.number="ruleForm.price" type="integer"></el-input>
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input v-model="ruleForm.address"></el-input>
      </el-form-item>
      <el-form-item label="楼层" prop="floor">
        <el-input v-model="ruleForm.floor"></el-input>
      </el-form-item>
      <el-form-item label="电梯" prop="lift">
        <el-input v-model="ruleForm.lift"></el-input>
      </el-form-item>
      <el-form-item label="户型" prop="houseType">
        <el-input v-model="ruleForm.houseType"></el-input>
      </el-form-item>
      <el-form-item label="朝向" prop="orientation">
        <el-input v-model="ruleForm.orientation"></el-input>
      </el-form-item>
      <el-form-item label="建造年代" prop="buildYear">
        <el-input v-model="ruleForm.buildYear"></el-input>
      </el-form-item>
      <el-form-item label="链接" prop="url">
        <el-input v-model="ruleForm.url"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="submitForm('ruleForm')" type="primary" icon="el-icon-edit">修改</el-button>
        <el-button @click="resetForm('ruleForm')" icon="el-icon-refresh">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'HouseUpdate',
  data() {
    return {
      ruleForm: {
        id: '',
        city: '',
        district: '',
        area: '',
        price: '',
        address: '',
        floor: '',
        lift: '',
        houseType: '',
        orientation: '',
        buildYear: '',
        url: ''
      },
      rules: {
        city: [
          { required: true, message: '请输入市名称', trigger: 'blur' }
        ],
        district: [
          { required: true, message: '请输入区或县名称', trigger: 'blur' }
        ],
        area: [
          { type: 'number', min: 0, required: true, message: '请输入面积（大于0的数）', trigger: 'blur' }
        ],
        price: [
          { type: 'integer', min: 0, required: true, message: '请输入价格（大于0的整数）', trigger: 'blur' }
        ],
        url: [
          { type: 'url', required: true, message: '请输入链接', trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    submitForm(formName) {
      const _this = this
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.put("/house/update", this.ruleForm)
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
  },
  created() {
    const _this = this
    axios.get('/house/findById/' + this.$route.query.id)
        .then(function (resp) {
          _this.ruleForm = resp.data
        })
  }
}
</script>

<style>
  .el-form-item {
    width: 80%
  }
</style>