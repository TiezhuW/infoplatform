<template>
  <div>
    <h1 align="center">收藏</h1>

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
          <el-button @click="delCollection(scope.row)" type="danger" icon="el-icon-delete" circle size="small"></el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "Collection",

  data() {
    return {
      tableData: []
    }
  },

  created() {
    const _this = this
    axios.get('/collect/findByUserId/' + this.$route.query.id)
        .then(function (resp) {
      _this.tableData = resp.data
    })
  },

  methods: {
    delCollection(row) {
      const _this = this
      axios.delete('/collect/delete/' + row.id)
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
    }
  }
}
</script>

<style scoped>

</style>