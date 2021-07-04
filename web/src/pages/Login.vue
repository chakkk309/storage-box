<template>
  <div class="bg" id="login">
    <div class="head">
      <span class="logo">LOGO</span>
    </div>
    <div class="box">
      <div class="box-left">
        <span class="box-left-title">储留箱后台管理系统</span>
        <span class="box-left-text">STORAGE BOX BACKGROUND MANAGEMENT SYSTEM</span>
      </div>
      <div class="box-right">
        <div class="WEL">
          <div class="welcome">欢迎登陆</div>
          <div class="name">储留箱后台管理系统</div>
        </div>
        <el-form ref="form" :model="form" label-width="80px" :rules="rules">
          <el-form-item prop="username" class="username">
            <el-input v-model="form.username" placeholder="请输入用户名">
              <template slot="prepend">
                <img src="../assets/yonghu-2.png" alt />
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password" class="password">
            <el-input placeholder="请输入密码" v-model="form.password" show-password>
              <template slot="prepend">
                <img src="../assets/mima-2.png" alt />
              </template>
            </el-input>
          </el-form-item>
          <el-button type="primary" @click="submitForm('form')" class="login-btn">登录</el-button>
          <a href="#" class="help" @click="help">忘记密码？寻求帮助</a>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    var checkUserName = (rule, value, callback) => {
      var userName = /^[a-zA-Z][a-zA-Z0-9]{2,15}$/;
      var idCard = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
      var phone = /^1[3456789]\d{9}$/;
      if (!value) {
        callback(new Error("请输入用户名/手机号/身份证号"));
      }
      setTimeout(() => {
        if (
          !idCard.test(value) &&
          !userName.test(value) &&
          !phone.test(value)
        ) {
          callback(new Error("请输入正确的用户名/手机号/邮箱"));
        } else {
          callback();
        }
      }, 100);
    };
    return {
      userType: "01",
      form: {
        username: "",
        password: ""
      },
      role: "",
      rules: {
        username: [
          { required: true, validator: checkUserName, trigger: "blur" }
        ],
        password: [
          {
            required: true,
            // pattern: /^[a-zA-Z0-9]{3,15}$/,
            message: "请输入正确的密码",
            trigger: "blur"
          }
        ]
      }
    };
  },
  mounted() {
    // 清除登录人信息
    localStorage.clear();
  },
  methods: {
    // 获取登录人信息接口
    loginPerson() {
      let PersonParms = {};
      this.$axios.get("prod-api/common/getLoginInfo", PersonParms).then(res => {
        if (200 == res.data.code) {
          this.role = res.data.roles[0];
          if (this.role == "app") {
            this.$router.push("/");
            this.$message({
              showClose: true,
              message: "用户为手机端用户，无法登陆",
              type: "error"
            });
          } else if (this.role == "admin") {
            this.$router.push("/");
            this.$message({
              showClose: true,
              message: "用户为测试用户，无法登陆",
              type: "error"
            });
          }
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
    },
    // 忘记密码
    help() {
      this.$alert("请联系管理员13234567821", "忘记密码", {
        confirmButtonText: "确定",
        callback: action => {
          this.$message({
            type: "info",
            message: `action: ${action}`
          });
        }
      });
    },

    // 登录
    submitForm(formName) {
      console.log(this.$refs[formName].validate);
      this.$refs[formName].validate(valid => {
        if (valid) {
          // console.log(valid);
          let paramForm = {
            username: this.form.username,
            password: this.form.password,
            userType: this.userType
          };
          this.$axios.post("prod-api/common/login", paramForm).then(res => {
            if (200 == res.data.code) {
              this.loginPerson();
              console.log(res.data);
              localStorage.setItem("username", paramForm.username);
              localStorage.setItem("token", res.data.token); //存返回的token值
              this.$router.push("/ordermanages");
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

              this.form.username = "";
              this.form.password = "";
            }
          });
        }
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.bg {
  width: 1520px;
  height: 738px;
  background-image: url(../assets/bg2.png);
  position: relative;
  margin: 0;
  padding: 0 auto;
  .logo {
    font-size: 40px;
    color: white;
    font-weight: bold;
    position: absolute;
    left: 20px;
    top: 30px;
  }
  .box {
    width: 900px;
    height: 400px;
    position: relative;
    top: 150px;
    left: 20%;
    .box-right {
      width: 450px;
      height: 400px;
      position: absolute;
      right: 0;
      background: white;
      border-radius: 10px;
      .welcome {
        font: 25px bold;
        position: absolute;
        top: 30px;
        left: 42px;
      }
      .WEL {
        display: block;
      }

      .name {
        font-size: 18px;
        color: #878787;
        position: absolute;
        top: 85px;
        left: 42px;
      }

      .username {
        position: relative;
        left: 0px;
        top: 150px;
        width: 350px;
      }
      .password {
        position: relative;
        left: 0px;
        top: 160px;
        width: 350px;
      }
      .login-btn {
        width: 250px;
        position: relative;
        top: 180px;
        left: 100px;
        height: 40px;
        font-size: 15px;
      }
      .help {
        position: relative;
        font-size: 13px;
        top: 230px;
        left: -80px;
      }
    }
    .box-left {
      width: 450px;
      height: 400px;
      background: rgba(247, 244, 244, 0.1);
      border-radius: 8px;
      position: absolute;
      left: 0px;
      .box-left-title {
        font-size: 30px;
        color: white;
        position: absolute;
        top: 100px;
        left: 70px;
      }
      .box-left-text {
        font-size: 12px;
        color: white;
        position: absolute;
        top: 200px;
        left: 50px;
      }
    }
  }
}
</style>
