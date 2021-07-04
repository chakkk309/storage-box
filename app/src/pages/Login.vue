<template>
  <div class="login">
    <div class="top">
      <!-- <img width="100" height="100" src="../assets/bg1.png" class="bg" /> -->
      <label for="储留箱" class="tit">储留箱</label>
    </div>
    <div class="inp">
      <van-form class="onSubmit">
        <van-field v-model="username" name="用户名" placeholder=" 用户名" left-icon="manager" />
        <van-field v-model="password" type="password" name="密码" placeholder=" 密码" left-icon="eye" />
        <div style="margin: 16px;position:relative;top:20px">
          <van-button round block type="info" native-type="submit" @click="toLogin()">登录</van-button>
        </div>
      </van-form>
    </div>
    <div class="bott">
      <div class="forgetPassword">忘记密码</div>
      <div class="register" @click="register()">注册</div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      password: "",
      username: "",
      userType: "02",
      role: ""
    };
  },
  mounted() {
    // 清除登录人信息

    localStorage.clear();
    // console.log(localStorage.config)
  },
  beforeDestroy() {
    console.log("upadated后" + this.role);
  },
  methods: {
    // 跳转注册页
    register() {
      this.$router.push("/register");
    },
    // 获取登录人信息接口
    loginPerson() {
      let PersonParms = {};

      this.$axios.get("prod-api/common/getLoginInfo", PersonParms).then(res => {
        if (200 == res.data.code) {
          this.$toast.success(res.data.msg);
          console.log(res.data.roles[0]);
          this.role = res.data.roles[0];

          console.log(this.role);
          if (this.role == "admin") {
            this.$router.push("/");
            this.$toast.fail("该用户为测试用户");
          } else if (this.role == "backend") {
            this.$router.push("/");
            this.$toast.fail("该用户为PC端用户");
          }
        } else {
          this.$toast.success(res.data.msg);
        }
      });
    },
    // 校验输入项
    toLogin() {
      var username = /^[a-zA-Z][a-zA-Z0-9]{3,15}$/;
      var idCard = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
      var phone = /^1[3456789]\d{9}$/;
      var password = /^[a-zA-Z0-9]{3,15}$/;
      if (!this.username) {
        this.$toast.fail("用户名不能为空");
      } else if (
        !username.test(this.username) &&
        !idCard.test(this.username) &&
        !phone.test(this.username) &&
        !password.test(this.password)
      ) {
        this.$toast.fail("用户名或密码错误");
        this.username = "";
        this.password = "";
      } else if (!this.password) {
        this.$toast.fail("密码不能为空");
      } else {
        let paramForm = {
          username: this.username,
          password: this.password,
          userType: this.userType
        };
        this.$axios.post("prod-api/common/login", paramForm).then(res => {
          if (res.data.code == 200) {
            // 调用登录人信息函数

            this.loginPerson();
            // setTimeout(function() {
            //   self.refreshBtn = false;
            // }, 2000);
            this.$toast.success(res.data.msg);

            localStorage.setItem("username", paramForm.username);

            localStorage.setItem("token", res.data.token); //存返回的token值

            this.$toast.success(res.data.msg);
            this.$router.push("/index");

            console.log("role" + this.role);
          } else {
            this.$toast.fail(res.data.msg);
          }
        });
      }
    }
  }
};
</script>
<style scoped>
.login {
  width: 100%;
  height: 100%;
  background: white;
  padding: 0;
  margin: 0;
}
.top {
  width: 100%;
  height: 200px;
  background-image: url("../assets/bg1.png");
  position: relative;
}

.tit {
  font-size: 50px;
  color: white;
  font-weight: bold;
  position: absolute;
  top: 30%;
  left: 30%;
}
.inp {
  width: 80%;
  height: 280px;
  position: relative;
  top: 90px;
  left: 35px;
}

.van-cell {
  height: 70px;
  color: #046ac6;
  padding-top: 30px;
}
.bott {
  background: white;
  width: 100%;
  height: 110px;
  position: relative;
  top: 80px;
}
.forgetPassword {
  color: rgb(80, 75, 75);
  float: left;
  margin-left: 70px;
}
.register {
  color: rgb(80, 75, 75);
  float: right;
  position: relative;
  right: 80px;
}
</style>