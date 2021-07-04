<template>
  <div class="header">
    <!-- header头部标题 -->
    <div class="systemTitle">储留箱后台管理系统</div>
    <div class="user_active">
      <img src="../assets/头像.png" alt />

      <!-- 用户名下拉菜单 -->
      <el-dropdown @command="handleCommand" trigger="click">
        <span class="el-dropdown-link">
          {{ userName }}
          <i class="el-icon-arrow-down"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="updatePw" @click="dialogVisible = true">修改密码</el-dropdown-item>
          <el-dropdown-item command="quiteLogin">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <!-- 修改密碼框 -->
    <el-dialog
      title="修改密码"
      :visible.sync="dialogVisible"
      width="400px"
      :close-on-click-modal="false"
      @close="resetForm('ruleForm')"
    >
      <el-form
        :model="ruleForm"
        status-icon
        :rules="rules"
        ref="ruleForm"
        label-width="150px"
        class="demo-ruleForm"
      >
        <el-form-item label="原密码" prop="oldPw">
          <el-input
            type="password"
            v-model="ruleForm.oldPw"
            autocomplete="off"
            width="10px"
            class="firPass"
          ></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPw">
          <el-input type="password" v-model="ruleForm.newPw" autocomplete="off" class="secPass"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="resetForm('ruleForm')">取 消</el-button>
        <el-button type="primary" @click="sureForm('ruleForm')">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  data() {
    var validatePass = (rule, value, callback) => {
      var oldPw = /^[a-zA-Z0-9]{3,15}$/;

      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (!oldPw.test(value)) {
          callback(new Error("请输入正确的密码"));
        }
        callback();
      }
    };

    return {
      userName: "",
      dialogVisible: false,
      ruleForm: {
        oldPw: "",
        newPw: "",
        age: ""
      },
      rules: {
        oldPw: [{ required: false, validator: validatePass, trigger: "blur" }],
        newPw: [{ required: true, validator: validatePass, trigger: "blur" }]
      }
    };
  },
  mounted() {
    // console.log(localStorage.getItem("username"));
    this.userName = localStorage.getItem("username");
  },
  methods: {
    handleCommand(command) {
      if (command == "updatePw") {
        this.dialogVisible = true;
      }
      if (command == "quiteLogin") {
        let PersonParms = {};
        this.$axios.post("prod-api/common/logout", PersonParms).then(res => {
          if (200 == res.data.code) {
            this.$message({
              showClose: true,
              message: res.data.msg,
              type: "success"
            });
            this.$router.push("/");
          }
        });
      }
    },

    resetForm(formName) {
      this.dialogVisible = false;
      this.$refs[formName].resetFields();
    },

    // 修改密码
    sureForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$axios
            .put(
              "prod-api/backend/home/updatePassword/" +
                this.ruleForm.oldPw +
                "/" +
                this.ruleForm.newPw
            )
            .then(res => {
              if (200 == res.data.code) {
                this.$message({
                  showClose: true,
                  message: res.data.msg,
                  type: "success"
                });
       

              } else if (500 == res.data.code) {
                this.$message({
                  showClose: true,
                  message: res.data.msg,
                  type: "error"
                });
              }
            });

          this.dialogVisible = false;
          this.$refs[formName].resetFields();
        }
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.header {
  position: absolute;
  box-sizing: border-box;
  top: 0px;
  width: 100%;
  height: 60px;
  background-color: rgb(255, 255, 255);

  box-sizing: border-box;
  padding-left: 20px;
  border-bottom: 1px solid #dadfe7;
  .systemTitle {
    color: #116dbf;
    font-size: 30px;
    line-height: 60px;
    font-weight: bold;
  }
  .user_active {
    display: block;
    position: absolute;
    right: 60px;
    top: 0;
    .el-dropdown-link {
      cursor: pointer;
      // background-color: rgb(179, 46, 46);
      position: relative;
      top: -25px;
      right: 0px;
    }
    .el-icon-arrow-down {
      font-size: 14px;
    }
  }
  .firPass,
  .secPass {
    width: 200px;
  }
  /deep/ .el-input__inner {
    width: 200px;
  }
}
</style>
