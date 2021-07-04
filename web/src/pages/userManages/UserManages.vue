<template>
  <div class="user">
    <!-- 查询用户 -->
    <el-card class="box-card">
      <el-form :model="imform" ref="imform" label-width="100px" :rules="rule1">
        <el-form-item label prop="name" class="name">
          <el-input v-model="imform.name" class="nameBtn" placeholder="姓名/用户名/手机号/邮箱"></el-input>
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="search()" class="addBtn">查询</el-button>
      <el-button @click="resetForm('imform')" class="resetBtn">重置</el-button>
    </el-card>

    <!-- 用户列表 -->
    <el-card>
      <el-button type="primary" @click="addData()">新增</el-button>
      <el-button type="primary" @click="editData()">编辑</el-button>
      <el-button type="primary" @click="start()">启用</el-button>
      <el-button type="primary" @click="forbid()">停用</el-button>
      <el-button type="primary" @click="Delete()">删除</el-button>
      <el-button type="primary" @click="resetPwd()">重置密码</el-button>
      <sirui-table
        :data="tableData"
        :page="page"
        :tableHeight="tableHeight"
        :loading="load"
        @handleSelectionChange="handleSelectionChange"
        @handleSizeChange="handleSizeChange"
        @handleCurrentChange="handleCurrentChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          v-for="item in columnList"
          :key="item.num"
          :label="item.lable"
          :width="item.width"
          :prop="item.prop"
        ></el-table-column>
      </sirui-table>
    </el-card>

    <!-- 新增弹出框 -->
    <User-add ref="addData"></User-add>
    <User-edit ref="editData"></User-edit>
  </div>
</template>
<script>
import SiruiTable from "../../components/SiruiTable/index";
import UserAdd from "../userManages/UserAdd";
import UserEdit from "../userManages/UserEdit";
export default {
  components: {
    SiruiTable,
    UserAdd,
    UserEdit
  },
  data() {
    var checkName = (rule, value, callback) => {
      var userName = /^[a-zA-Z][a-zA-Z0-9]{3,15}$/;
      var email = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
      var phone = /^1[3456789]\d{9}$/;
      var name = /([\*\"\'<>\/])+/;
      setTimeout(() => {
        if (
          !email.test(value) &&
          !userName.test(value) &&
          !phone.test(value) &&
          name.test(value)
        ) {
          callback(new Error("请输入正确的用户名/客户名/手机号/邮箱"));
        } else {
          callback();
        }
      }, 100);
    };

    return {
      tableHeight: 400,
      load: false,
      dialogVisible: false,
      pwdForm: {
        pass: "",
        checkPass: ""
      },

      imform: {
        name: ""
      },
      rule1: {
        name: {
          validator: checkName
        }
      },

      columnList: [
        {
          prop: "userName",
          lable: "用户名",
          width: 150,
          num: 1
        },
        { prop: "nickName", lable: "姓名", width: 200, num: 2 },
        { prop: "sex", lable: "性别", width: 200, num: 7 },
        { prop: "phonenumber", lable: "手机号", width: 200, num: 3 },
        { prop: "email", lable: "邮箱", width: 200, num: 4 },

        { prop: "status", lable: "状态", width: 150, num: 5 }
      ],
      page: {
        pages: 4,
        total: 0,
        pageSize: 10,
        pageNum: 1
      },
      tableData: [],
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
    resetPwd() {
      //  重置密码 调接口
      if (this.multipleSelection.length >= 1) {
        this.$confirm("是否重置密码", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            let params = {
              userIds: []
            };

            for (var i = 0; i < this.multipleSelection.length; i++) {
              params.userIds.push(this.multipleSelection[i].userId);
            }
            params.userIds = params.userIds.join(",");

            this.$axios
              .put("prod-api/backend/user/reset/" + params.userIds)
              .then(res => {
                if (res.data.code == 200) {
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
              message: "已取消重置密码"
            });
          });
      } else {
        this.$message({
          type: "info",
          message: "请选择数据进行操作"
        });
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
          "prod-api/backend/user/list?" +
            "pageNum=" +
            this.page.pageNum +
            "&pageSize=" +
            this.page.pageSize +
            "&searchValue=" +
            this.imform.name
        )
        .then(res => {
          if (200 == res.data.code) {
            this.tableData = res.data.rows;
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
    // 选中触发事件
    handleSelectionChange(row) {
      this.multipleSelection = row;
      //   alert(JSON.stringify(this.multipleSelection));
    },
    // 每页条数
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getData();
    },
    //第几页
    handleCurrentChange(val) {
      console.log(val);
      this.page.pageNum = val;
      this.getData();
    },
    // //下一页

    // nextClick() {},
    // // 上一页
    // prevClick() {},
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

    // 转换汉字
    Words() {
      for (let i = 0; i < this.tableData.length; i++) {
        if (this.tableData[i].status == 1) {
          this.tableData[i].status = "停用";
        } else if (this.tableData[i].status == 0) {
          this.tableData[i].status = "启用";
        }
        if (this.tableData[i].sex == 0) {
          this.tableData[i].sex = "男";
        } else if (this.tableData[i].sex == 1) {
          this.tableData[i].sex = "女";
        }
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
              userIds: [],
              operate: "enable"
            };

            for (var i = 0; i < this.multipleSelection.length; i++) {
              params.userIds.push(this.multipleSelection[i].userId);
              console.log(this.multipleSelection[i].status);
              this.Ostatus = this.multipleSelection[i].status;
            }
            params.userIds = params.userIds.join(",");
            if (this.Ostatus == "启用") {
              this.$message({
                type: "error",
                message: "状态为启用的用户，不能启用"
              });
            } else {
              this.$axios
                .put(
                  "prod-api/backend/user/" +
                    params.operate +
                    "/" +
                    params.userIds
                )
                .then(res => {
                  if (res.data.code == 200) {
                    this.getData();
                    // this.Words();

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
              userIds: [],
              operate: "disable"
            };

            for (var i = 0; i < this.multipleSelection.length; i++) {
              params.userIds.push(this.multipleSelection[i].userId);
              console.log("停用" + this.multipleSelection[i].status);
              this.multipleSelection[i].status = 1;
            }

            params.userIds = params.userIds.join(",");

            this.$axios
              .put(
                "prod-api/backend/user/" + params.operate + "/" + params.userIds
              )
              .then(res => {
                if (res.data.code == 200) {
                  this.getData();
                  // this.Words();

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
              message: "已取消停用"
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
              userIds: []
            };
            for (var i = 0; i < this.multipleSelection.length; i++) {
              params.userIds.push(this.multipleSelection[i].userId);
            }
            params.userIds = params.userIds.join(",");

            this.$axios
              .delete("prod-api/backend/user/delete/" + params.userIds)
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
.user {
  margin: 10px;
  .box-card {
    height: 120px;
    margin-bottom: 10px;
    .name,
    .phone {
      width: 200px;
      float: left;
      position: relative;
      top: 15px;
      left: -40px;
      margin-right: 80px;
      .nameBtn,
      .phoneBtn {
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
  .firPass,
  .secPass {
    width: 200px;
    position: relative;
  }
}
</style>