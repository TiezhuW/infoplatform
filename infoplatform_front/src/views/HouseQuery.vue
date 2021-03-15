<template xmlns="http://www.w3.org/1999/html">
  <div>
    <h1 align="center">二手房信息搜索引擎</h1>

    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="市" prop="city" style="width: 80%">
        <el-input v-model="ruleForm.city"></el-input>
      </el-form-item>
      <el-form-item label="区\县" prop="district" style="width: 80%">
        <el-input v-model="ruleForm.district"></el-input>
      </el-form-item>
      <el-row>
        <el-col :span="8">
          <el-form-item label="最小面积/㎡" prop="minArea">
            <el-input v-model.number="ruleForm.minArea" type="number"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8"></el-col>
        <el-col :span="8">
          <el-form-item label="最大面积/㎡" prop="maxArea">
             <el-input v-model.number="ruleForm.maxArea" type="number"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="最低价格/元" prop="minPrice">
            <el-input v-model.number="ruleForm.minPrice" type="integer"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="最高价格/元" prop="maxPrice">
            <el-input v-model.number="ruleForm.maxPrice" type="integer"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="排序" prop="order">
        <el-radio-group v-model="ruleForm.order">
          <el-radio label="默认排序"></el-radio>
          <el-radio label="面积升序"></el-radio>
          <el-radio label="面积降序"></el-radio>
          <el-radio label="价格升序"></el-radio>
          <el-radio label="价格降序"></el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button @click="submitForm('ruleForm')" type="primary" icon="el-icon-search" >搜索</el-button>
        <el-button @click="resetForm('ruleForm')" icon="el-icon-refresh">重置</el-button>
        <el-button @click="add()" icon="el-icon-plus" v-if="isAdmin">添加</el-button>
        <el-button @click="crawl()" icon="el-icon-attract" v-if="isAdmin">爬虫引擎</el-button>
      </el-form-item>
    </el-form>

    <el-table
        :data="tableData"
        border
        style="width: 100%">
      <el-table-column
          prop="city"
          label="市"
          align="center">
      </el-table-column>
      <el-table-column
          prop="district"
          label="区\县"
          align="center">
      </el-table-column>
      <el-table-column
          prop="area"
          label="面积/㎡"
          align="center">
      </el-table-column>
      <el-table-column
          prop="price"
          label="价格/元"
          align="center">
      </el-table-column>
      <el-table-column
          prop="address"
          label="地址"
          align="center">
      </el-table-column>
      <el-table-column
          prop="floor"
          label="楼层"
          align="center">
      </el-table-column>
      <el-table-column
          prop="lift"
          label="电梯"
          align="center">
      </el-table-column>
      <el-table-column
          prop="houseType"
          label="户型"
          align="center">
      </el-table-column>
      <el-table-column
          prop="orientation"
          label="朝向"
          align="center">
      </el-table-column>
      <el-table-column
          prop="buildYear"
          label="建造年代"
          align="center">
      </el-table-column>
      <el-table-column
          label="操作"
          align="center">
        <template slot-scope="scope">
          <el-button @click="moreHouseInfo(scope.row)" icon="el-icon-info" circle size="small"></el-button>
          <el-button @click="collect(scope.row)" type="warning" icon="el-icon-star-off" size="small" circle v-if="!isAdmin"></el-button>
          <el-button @click="update(scope.row)" type="primary" icon="el-icon-edit" circle size="small" v-if="isAdmin"></el-button>
          <el-button @click="delHouse(scope.row)" type="danger" icon="el-icon-delete" circle size="small" v-if="isAdmin"></el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        align="center"
        background layout="prev, pager, next"
        :page-size="pageSize"
        :total="total"
        @current-change="page"
        :current-page.sync="currentPage"
        v-if="tableData.length > 0">
    </el-pagination>
  </div>
</template>

<style scoped>
  .el-icon-user {
    text-align: right;
  }
</style>

<script>
export default {
  name: 'HouseQuery',

  methods: {
    crawl() {
      window.location.href = '/crawler'
    },

    add() {
      window.location.href = '/houseAdd'
    },

    collect(row) {
      axios.post('/collect/save/' + row.id)
          .then(function (resp) {
            if (resp.data.success == true) {
              _this.$message({
                message: resp.data.msg,
                type: 'success',
                center: true
              })
              window.location.reload()
            } else {
              _this.$message({
                message: resp.data.msg,
                type: 'error',
                center: true
              })
            }
          })
    },

    moreHouseInfo(row) {
      window.open(row.url)
    },

    delHouse(row) {
      const _this = this
      axios.delete('/house/deleteById/' + row.id)
          .then(function (resp) {
            if (resp.data.success == true) {
              _this.$message({
                message: resp.data.msg,
                type: 'success',
                center: true
              })
              window.location.reload()
            } else {
              _this.$message({
                message: resp.data.msg,
                type: 'error',
                center: true
              })
            }
          })
    },

    update(row) {
      this.$router.push({
        path: '/houseUpdate',
        query: {
          id: row.id
        }
      })
    },

    page(currentPage) {
      const _ruleForm = this.ruleForm
      const _this = this

      axios.get(
          '/house/findByCityDistrict/'
          + _ruleForm.city
          + '/'
          + _ruleForm.district
          + '/'
          + currentPage
          +'/10?minArea='
          + _ruleForm.minArea
          + '&maxArea='
          + _ruleForm.maxArea
          + '&minPrice='
          + _ruleForm.minPrice
          + '&maxPrice='
          + _ruleForm.maxPrice
          + '&order='
          + _ruleForm.order
      ).then(function (resp) {
            _this.tableData = resp.data.list
            _this.total = resp.data.total
          })
    },

    submitForm(formName) {
      const _ruleForm = this.ruleForm
      const _this = this

      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.get(
              '/house/findByCityDistrict/'
              + _ruleForm.city
              + '/'
              + _ruleForm.district
              + '/1/'
              + _this.pageSize
              + '?minArea='
              + _ruleForm.minArea
              + '&maxArea='
              + _ruleForm.maxArea
              + '&minPrice='
              + _ruleForm.minPrice
              + '&maxPrice='
              + _ruleForm.maxPrice
              + '&order='
              + _ruleForm.order,
          )
              .then(function (resp) {
                _this.tableData = resp.data.list
                _this.total = resp.data.total
                _this.currentPage = 1
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
    axios.get('/user/isAdmin').then(function (resp) {
      _this.isAdmin = resp.data
    })
  },


  data() {
    let validateArea = (rule, value, callback) => {
      if (value < 0){
        return callback(new Error('请输入面积（大于0的数）'))
      } else {
        return callback()
      }
    }

    let validatePrice = (rule, value, callback) => {
      if (value < 0){
        return callback(new Error('请输入价格（大于0的数）'))
      } else {
        return callback()
      }
    }

    return {
      isAdmin: false,

      total: null,
      tableData: [],
      currentPage: null,
      pageSize: 10,

      ruleForm: {
        city: '',
        district: '',
        minArea: '',
        maxArea: '',
        minPrice: '',
        maxPrice: '',
        order: ''
      },

      rules: {
        city: [
          { required: true, message: '请输入市名称', trigger: 'blur' }
        ],
        district: [
          { required: true, message: '请输入区或县名称', trigger: 'blur' }
        ],
        minArea: [
          { validator: validateArea , trigger: 'blur' }
        ],
        maxArea: [
          { validator: validateArea, trigger: 'blur' }
        ],
        minPrice: [
          { validator: validatePrice, trigger: 'blur' }
        ],
        maxPrice: [
          { validator: validatePrice, trigger: 'blur' }
        ],
        order: [
          { required: true, message: '请选择排序方式', trigger: 'change' }
        ]
      }
    }
  }
}
</script>