<template>
  <div class="view">
    <!-- 查询用户 -->
    <el-card class="box-card">
      <el-form :model="imform" ref="imform" label-width="100px">
        <el-form-item prop="content" class="name">
          <el-input v-model="imform.content" class="nameBtn" placeholder="提交人/意见标题"></el-input>
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="search()" class="addBtn">查询</el-button>
      <el-button @click="resetForm('imform')" class="resetBtn">重置</el-button>
    </el-card>

    <!-- 用户列表 -->
    <el-card>
      <sirui-table
        :data="tableData"
        :page="page"
        :loading="load"
        @handleSelectionChange="handleSelectionChange"
        @handleSizeChange="handleSizeChange"
        :tableHeight="tableHeight"
        @handleCurrentChange="handleCurrentChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          v-for="item in columnList"
          :key="item.num"
          :label="item.lable"
          width="220"
          :prop="item.prop"
        ></el-table-column>
      </sirui-table>
    </el-card>
  </div>
</template>
<script>
import SiruiTable from "../../components/SiruiTable/index";

export default {
  components: {
    SiruiTable
  },
  data() {
    return {
      tableHeight: 400,
      load: false,
      imform: {
        content: ""
      },
      columnList: [
        {
          prop: "id",
          lable: "编号",
          width: 200,
          num: 1
        },
        { prop: "nickName", lable: "提交人", num: 2 },
        { prop: "title", lable: "意见标题", num: 3 },
        { prop: "content", lable: "意见内容", num: 4 },
        { prop: "createTime", lable: "提交时间", num: 5 }
      ],
      page: {
        pages: 0,
        total: 0,
        pageSize: 10,
        pageNum: 1
      },
      tableData: [],
      multipleSelection: [],
      addnew: []
    };
  },
  mounted() {
    this.getData();
  },
  methods: {
    resetForm(formName) {
      this.$refs[formName].resetFields();

      // console.log(formName);
      this.handleCurrentChange(1);
      this.getData();
    },
    // 查询用户
    search() {
      this.handleCurrentChange(1);

      this.getData();
    },

    //查询数据列表

    getData() {
      this.$axios
        .get(
          "prod-api/backend/advice/list?" +
            "pageNum=" +
            this.page.pageNum +
            "&pageSize=" +
            this.page.pageSize +
            "&searchValue=" +
            this.imform.content
        )
        .then(res => {
          if (200 == res.data.code) {
            this.tableData = res.data.rows;
            this.page.total = res.data.total;
            this.$message({
              showClose: true,
              message: "查询成功",
              type: "success"
            });
          } else {
            this.$message({
              showClose: true,
              message: res.data.msg,
              type: "error"
            });
          }
        });
    },
    // 选中触发事件
    handleSelectionChange(row) {
      this.multipleSelection = row;
      // alert(JSON.stringify(this.multipleSelection));
    },
    // 每页条数
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getData();
    },
    //第几页
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.getData();
    }
  }
};
</script>
<style lang="scss" scoped>
.view {
  margin: 10px;
  .box-card {
    height: 120px;
    margin-bottom: 10px;
    .name,
    .title {
      width: 200px;
      float: left;
      position: relative;
      top: 15px;
      left: -40px;
      margin-right: 80px;
      .nameBtn,
      .titleBtn {
        width: 200px;
      }
    }
    .addBtn,
    .resetBtn {
      position: relative;
      left: 50px;
      top: 15px;
      margin-right: 50px;
    }
  }
}
</style>