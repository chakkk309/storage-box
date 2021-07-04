<template>
  <div class="advert">
    <!-- 查询 -->
    <el-card class="box-card">
      <el-form :model="advertform" ref="advertform" label-width="100px">
        <el-form-item prop="title" class="title">
          <el-input v-model=" advertform.title" class="nameBtn" placeholder="广告标题/奖励积分/图片地址"></el-input>
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="search()" class="addBtn">查询</el-button>
      <el-button @click="resetForm('advertform')" class="resetBtn">重置</el-button>
    </el-card>
    <!-- 客户列表 -->
    <el-card>
      <el-button type="primary" @click="addData()">新增</el-button>
      <el-button type="primary" @click="editData()">编辑</el-button>
      <el-button type="primary" @click="start()">启用</el-button>
      <el-button type="primary" @click="forbid()">停用</el-button>
      <el-button type="primary" @click="Delete()">删除</el-button>
      <siri-table
        :data="advertData"
        :page="page"
        @handleSizeChange="handleSizeChange"
        @handleSelectionChange="handleSelectionChange"
        @handleCurrentChange="handleCurrentChange"
        :tableHeight="tableHeight"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          class="advert-table"
          label="广告编号"
          width="200"
          prop="id"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          class="advert-table"
          label="广告标题"
          width="200"
          prop="title"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          class="advert-table"
          label="广告内容"
          width="200"
          prop="content"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          class="advert-table"
          label="图片地址"
          width="150"
          prop="imgUrl"
          :show-overflow-tooltip="true"
        >
          <template slot-scope="scope">
            <el-image style="width: 100px; height: 100px" :src="scope.row.imgUrl"></el-image>
          </template>
        </el-table-column>

        <el-table-column
          class="advert-table"
          label="奖励积分"
          width="150"
          prop="points"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          class="advert-table"
          label="广告状态"
          width="100"
          prop="isEnable"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          class="advert-table"
          label="广告排序"
          width="100"
          prop="sortNo"
          :show-overflow-tooltip="true"
        ></el-table-column>
      </siri-table>
    </el-card>

    <advert-add ref="addData"></advert-add>
    <advert-edit ref="editData"></advert-edit>
  </div>
</template>
<script>
import siriTable from "../../components/SiruiTable/index";
import advertAdd from "../advertManages/AdvertAdd";
import advertEdit from "../advertManages/AdvertEdit";
export default {
  components: { siriTable, advertAdd, advertEdit },
  data() {
    return {
      tableHeight: 400,

      advertform: { title: "", phone: "" },
      page: {
        pages: 0,
        total: 0,
        pageSize: 10,
        pageNum: 1
      },

      fits: ["fill", "contain", "cover", "none", "scale-down"],
      advertData: [],
      multipleSelection: [],
      Ostatus: ""
    };
  },
  mounted() {
    this.getData();
  },
  methods: {
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.handleCurrentChange(1);

      this.getData();
    },
    // 转换汉字
    Words() {
      for (let i = 0; i < this.advertData.length; i++) {
        if (this.advertData[i].isEnable == 1) {
          this.advertData[i].isEnable = "停用";
        } else if (this.advertData[i].isEnable == 0) {
          this.advertData[i].isEnable = "启用";
        }
      }
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
          "prod-api/backend/advertisement/list?" +
            "pageNum=" +
            this.page.pageNum +
            "&pageSize=" +
            this.page.pageSize +
            "&searchValue=" +
            this.advertform.title
        )
        .then(res => {
          if (200 == res.data.code) {
            this.advertData = res.data.rows;
            this.page.total = res.data.total;
            this.Words();

            for (var i = 0; i < this.advertData.length; i++) {
              this.advertData[i].imgUrl =
                "prod-api" + this.advertData[i].imgUrl;
            }

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
      //   alert(JSON.stringify(this.multipleSelection));
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
    //显示修改弹出框
    editData() {
      if (1 == this.multipleSelection.length) {
        this.$refs.editData.openDialog(
          true,
          JSON.parse(JSON.stringify(this.multipleSelection))
        );
      } else if (this.multipleSelection.length > 1) {
        this.$message({
          type: "info",
          message: "请选择一条数据进行操作"
        });
      } else {
        this.$message({
          type: "info",
          message: "请选择数据进行操作"
        });
      }
    },

    //启用
    start() {
      if (this.multipleSelection.length >= 1) {
        this.$confirm("是否启用", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            let params = {
              ids: [],
              operate: "enable"
            };

            for (var i = 0; i < this.multipleSelection.length; i++) {
              console.log(this.multipleSelection[i]);
              params.ids.push(this.multipleSelection[i].id);
              this.Ostatus = this.multipleSelection[i].isEnable;
            }
            params.ids = params.ids.join(",");
            if (this.Ostatus == "启用") {
              this.$message({
                type: "error",
                message: "状态为启用的用户，不能启用"
              });
            } else {
              this.$axios
                .put(
                  "prod-api/backend/advertisement/" +
                    params.operate +
                    "/" +
                    params.ids
                )
                .then(res => {
                  if (res.data.code == 200) {
                    this.Words();
                    this.handleCurrentChange(1);
                    this.getData();
                    this.$message({
                      type: "success",
                      message: res.data.msg
                    });
                  } else {
                    this.$message({
                      type: "error",
                      message: res.data.msg
                    });
                  }
                });
            }
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消启用"
            });
          });
      } else {
        this.$message({
          type: "info",
          message: "请选择数据进行操作"
        });
      }
    },
    //禁用
    forbid() {
      if (this.multipleSelection.length >= 1) {
        this.$confirm("是否禁用", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            let params = {
              ids: [],
              operate: "disable"
            };

            for (var i = 0; i < this.multipleSelection.length; i++) {
              console.log(this.multipleSelection[i]);
              params.ids.push(this.multipleSelection[i].id);
            }
            params.ids = params.ids.join(",");

            this.$axios
              .put(
                "prod-api/backend/advertisement/" +
                  params.operate +
                  "/" +
                  params.ids
              )
              .then(res => {
                if (res.data.code == 200) {
                  this.Words();
                  this.handleCurrentChange(1);
                  this.getData();
                  this.$message({
                    type: "success",
                    message: res.data.msg
                  });
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
              message: "已取消禁用"
            });
          });
      } else {
        this.$message({
          type: "info",
          message: "请选择数据进行操作"
        });
      }
    },
    //删除
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
              .delete("prod-api/backend/advertisement/delete/" + params.ids)
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
.advert {
  margin: 10px;
  .box-card {
    height: 120px;
    margin-bottom: 10px;
    .title {
      width: 200px;
      float: left;
      position: relative;
      top: 15px;
      left: 0px;
      margin-right: 80px;
      .nameBtn,
      .phoneBtn {
        width: 200px;
      }
    }
    .addBtn,
    .resetBtn {
      position: relative;
      left: 80px;
      top: 15px;
      margin-right: 50px;
    }
  }
  .advert-table {
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
  }
}
</style>