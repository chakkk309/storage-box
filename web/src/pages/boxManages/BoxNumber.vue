<template>
  <div class="box">
    <!-- 查询 -->
    <el-card class="box-card">
      <el-form :model="boxform" ref="boxform">
        <el-form-item label="箱子规格" prop="boxSize" class="boxSize">
          <el-select v-model="boxform.boxSize" placeholder="请选择" class="selected">
            <el-option
              v-for="item in options"
              :key="item.id"
              :label="item.boxStandard"
              :value="item.boxStandard"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="isUsed" class="boxSize">
          <el-select v-model="boxform.isUsed" placeholder="请选择">
            <el-option label="未使用" value="0"></el-option>
            <el-option label="使用中" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="使用人" prop="usedByName" class="boxSize">
          <el-input v-model="boxform.usedByName" class="nameBtn"></el-input>
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="search()" class="addBtn">查 询</el-button>
      <el-button @click="resetForm('boxform')" class="resetBtn">重 置</el-button>
    </el-card>
    <!-- 客户列表 -->
    <el-card>
      <el-button type="primary" @click="addData()">新增</el-button>

      <el-button type="primary" @click="Delete()">删除</el-button>
      <siri-table
        :data="boxData"
        :page="page"
        @handleSizeChange="handleSizeChange"
        @handleSelectionChange="handleSelectionChange"
        @handleCurrentChange="handleCurrentChange"
        :tableHeight="tableHeight"
        class="box-table"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          v-for="item in columnList"
          :key="item.num"
          :label="item.lable"
          width="190"
          :prop="item.prop"
        ></el-table-column>
      </siri-table>
    </el-card>
    <boxnum-add ref="addData"></boxnum-add>
  </div>
</template>
<script>
import siriTable from "../../components/SiruiTable/index";
import boxnumAdd from "../boxManages/BoxNumberAdd";
export default {
  components: { siriTable, boxnumAdd },
  data() {
    return {
      tableHeight: 400,
      boxform: { boxSize: "", isUsed: "", usedByName: "" },

      page: {
        pages: 0,
        total: 0,
        pageSize: 10,
        pageNum: 1
      },
      options: [],

      columnList: [
        {
          prop: "boxStandard",
          lable: "箱子规格",
          width: 200,
          num: 1
        },
        { prop: "boxCode", lable: "箱子编号", num: 2 },
        { prop: "boxUnitPrice", lable: "所需积分", num: 3 },
        { prop: "isUsed", lable: "使用状态", num: 4 },
        { prop: "usedByName", lable: "使用人", num: 5 },
        { prop: "remark", lable: "备注", num: 6 }
      ],
      boxData: [],
      multipleSelection: []
    };
  },
  mounted() {
    this.getData();
    this.standradDown();
  },

  methods: {
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.handleCurrentChange(1);
      this.getData();
    },
    // 转换汉字
    Words() {
      for (let i = 0; i < this.boxData.length; i++) {
        if (this.boxData[i].isUsed == 0) {
          this.boxData[i].isUsed = "未使用";
        } else if (this.boxData[i].isUsed == 1) {
          this.boxData[i].isUsed = "使用中";
        }
      }
    },
    // 查询用户
    search() {
      this.handleCurrentChange(1);

      this.getData();
      this.standradDown();
    },
    //箱子規格下拉列表
    standradDown() {
      this.$axios.get("prod-api/backend/box/standard/select").then(res => {
        if (res.data.code == 200) {
          this.options = res.data.data;
          // console.log(this.options);
        } else {
          this.$message({
            showClose: true,
            message: res.data.msg,
            type: "error"
          });
        }
      });
    },

    //查询数据列表

    getData() {
      this.$axios
        .get(
          "prod-api/backend/box/info/list?" +
            "pageNum=" +
            this.page.pageNum +
            "&pageSize=" +
            this.page.pageSize +
            "&boxStandard=" +
            this.boxform.boxSize +
            "&isUsed=" +
            this.boxform.isUsed +
            "&usedByName=" +
            this.boxform.usedByName
        )
        .then(res => {
          if (200 == res.data.code) {
            this.boxData = res.data.rows;
            this.page.total = res.data.total;
            this.Words();
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
    // 每页条数
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getData();
    },
    // 选中触发事件
    handleSelectionChange(row) {
      this.multipleSelection = row;
      // alert(JSON.stringify(this.multipleSelection));
    },
    //第几页
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.getData();
    },
    //显示新增弹出框
    addData() {
      this.$refs.addData.openDialog(
        true,
        JSON.parse(JSON.stringify(this.multipleSelection))
      );
    },

    // 删除接口
    Delete() {
      if (this.multipleSelection.length >= 1) {
        this.$confirm("此操作将会将数据永久删除，是否继续？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            let params = {
              ids: []
            };

            for (var i = 0; i < this.multipleSelection.length; i++) {
              params.ids.push(this.multipleSelection[i].id);
            }
            params.ids = params.ids.join(",");
            this.$axios
              .delete("prod-api/backend/box/info/delete/" + params.ids)
              .then(res => {
                if (res.data.code == 200) {
                  this.$message({
                    type: "success",
                    message: res.data.msg
                  });
                  this.getData();
                } else {
                  this.$message({
                    type: "error",
                    message: res.data.msg
                  });
                }
              });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除"
            });
          });
      } else {
        this.$message({
          type: "info",
          message: "请选择数据进行操作"
        });
      }
    }
  }
};
</script>
<style lang="scss" scoped>
.box {
  margin: 10px;
  .box-card {
    height: 150px;
    margin-bottom: 10px;

    .nameBtn,
    .phoneBtn {
      width: 200px;
    }
  }
  .addBtn,
  .resetBtn {
    position: relative;
    left: 400px;
    top: 15px;
    margin-right: 50px;
  }
}
.boxSize {
  width: 400px;
  float: left;
  position: relative;
  top: 15px;
  left: 33px;
}
</style>