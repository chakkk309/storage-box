<template>
  <div class="register">
    <!-- 导航栏 -->
    <van-nav-bar
      fixed
      title="注册"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
      @click-right="onClickRight"
    />
    <!-- 注册页海报 -->
    <div class="top">
      <img src="../assets/logo1.png" class="logo" alt />
      <div class="word">
        <div class="chu">CHU</div>
        <div class="liu">LIU</div>
        <div class="xiang">XIANG</div>
      </div>
    </div>

    <!-- 注册信息 表单 -->
    <div class="regiForm">
      <van-form @submit="onSubmit">
        <van-field
          v-model="userName"
          name="用户名"
          @blur="usernameCheck"
          left-icon="manager"
          placeholder="用户名"
          prop="userName"
        />
        <van-field
          v-model="password"
          type="password"
          prop="password"
          @click="pswd"
          @blur="passwordCheck"
          name="密码"
          placeholder="密码"
          left-icon="lock"
        />
        <div v-if="isShow">
          <div class="input_span">
            <span id="one"></span>
            <span id="two"></span>
            <span id="three"></span>
          </div>
          <div id="font">
            <span>弱</span>
            <span>中</span>
            <span>强</span>
          </div>
        </div>
        <van-field
          v-model="surePassword"
          type="password"
          name="确认密码"
          placeholder="确认密码"
          left-icon="lock"
          @blur="surepassword"
        />
        <van-field
          prop="phonenumber"
          v-model="phonenumber"
          @blur="phoneCheck"
          name="手机号"
          type="number"
          left-icon="phone-o"
          placeholder="手机"
        />
        <van-field
          prop="nickName"
          v-model="nickName"
          @blur="nameCheck"
          name="姓名"
          left-icon="contact"
          placeholder="姓名"
        />
        <van-field
          prop="email"
          v-model="email"
          @blur="emailCheck"
          name="邮箱"
          left-icon="free-postage"
          placeholder="邮箱"
        />

        <van-radio-group v-model="sex" direction="horizontal" style="color:gray;margin:20px">
          <div style="color:gray;padding-left:14px;margin-right:20px;font-size:14px">性别</div>
          <van-radio name="0">男</van-radio>
          <van-radio name="1">女</van-radio>
        </van-radio-group>

        <div style="margin: 16px;">
          <van-button round block type="info" native-type="submit" @click="checkForm">提交</van-button>
        </div>
      </van-form>
    </div>
  </div>
</template>
<script>
import $ from "jquery";

// 表单验证 传入输入的密码 判断其长度和内容
export function checkStrong(sValue) {
  var modes = 0;
  //正则表达式验证符合要求的
  if (sValue.length < 1) return modes;
  if (/\d/.test(sValue)) modes++; //数字
  if (/[a-z]/.test(sValue)) modes++; //小写
  if (/[A-Z]/.test(sValue)) modes++; //大写

  //逻辑处理  返回对应值
  switch (modes) {
    case 1:
      return 1;
      break;
    case 2:
      return 2;
      break;
    case 3:
    case 4:
      return sValue.length <= 16 ? 3 : 4;
      break;
  }
  return modes;
}
export default {
  data() {
    return {
      isShow: false,
      userName: "",
      password: "",
      surePassword: "",
      phonenumber: "",
      nickName: "",
      email: "",
      sex: "1",
      msgText: ""
    };
  },
  methods: {
    // 判断是否显示密码校验
    pswd() {
      this.isShow = true;
    },

    // 输入校验
    usernameCheck() {
      var userName = /^[A-Za-z0-9]{2,16}$/;
      if (!this.userName) {
        this.$toast.fail("用户名不能为空！");
      } else if (!userName.test(this.userName)) {
        this.$toast.fail("用户名最大长度16，只能包含英文和数字！");
        this.userName = "";
      }
      if (this.userName == this.email || this.userName == this.phonenumber) {
        this.$toast.fail("用户名、邮箱、手机号不能相同 ");
        this.userName = "";
      }
    },
    passwordCheck() {
      var password = /^[A-Za-z0-9]{3,16}$/;
      if (!this.password) {
        this.$toast.fail("密码不能为空！");
      } else if (!password.test(this.password)) {
        this.$toast.fail("密码最大长度16，只能包含英文和数字 ");
        this.password = "";
      } else {
        this.isShow = false;
      }
    },
    surepassword() {
      if (this.password != this.surePassword) {
        this.$toast.fail("两次密码输入不一致！");
        this.surePassword = "";
      }
    },
    nameCheck() {
      var nickName = /^([\u4e00-\u9fa5]){2,10}$/;
      if (!nickName.test(this.nickName)) {
        this.$toast.fail("请输入正确的姓名！");
        this.nickName = "";
      }
    },
    emailCheck() {
      var email = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
      if (!this.email) {
        this.$toast.fail("邮箱不能为空！");
      } else if (!email.test(this.email)) {
        this.$toast.fail("请输入正确的邮箱！");
        this.email = "";
      }
      if (this.userName == this.email || this.userName == this.phonenumber) {
        this.$toast.fail("用户名、邮箱、手机号不能相同 ");
        this.userName = "";
      }
    },
    phoneCheck() {
      var phonenumber = /^1[3456789]\d{9}$/;
      if (!this.phonenumber) {
        this.$toast.fail("手机号不能为空！");
      } else if (!phonenumber.test(this.phonenumber)) {
        this.$toast.fail("请输入正确的手机号！");
        this.phonenumber = "";
      }
      if (this.userName == this.email || this.userName == this.phonenumber) {
        this.$toast.fail("用户名、邮箱、手机号不能相同 ");
        this.userName = "";
      }
    },

    // 打印表单输入值
    onSubmit(values) {
      console.log("submit", values);
    },
    onClickLeft() {
      this.$router.push("/");
    },
    onClickRight() {
      this.$toast("按钮");
    },

    // 判断是否全部输入正确 是进入login页面
    checkForm() {
      if (
        this.userName != "" &&
        this.password != "" &&
        this.nickName != "" &&
        this.phonenumber != "" &&
        this.email != "" &&
        this.surePassword != ""
      ) {
        let params = {
          userName: this.userName,
          password: this.password,
          nickName: this.nickName,
          phonenumber: this.phonenumber,
          sex: this.sex,
          email: this.email
        };
        this.$axios.post("prod-api/app/regist", params).then(res => {
          if (res.data.code == 200) {
            this.$toast.success(res.data.msg);
            this.$router.push("/");
          } else {
            this.$toast.fail(res.data.msg);
          }
        });
      }
    }
  },

  watch: {
    // 监听输入的密码  根据返回值判断状态
    password(newValue, oldValue) {
      this.msgText = checkStrong(newValue);
      if (this.msgText > 1 || this.msgText == 1) {
        $("#one").css("background", "red");
      } else {
        $("#one").css("background", "#eee");
      }
      if (this.msgText > 2 || this.msgText == 2) {
        $("#two").css("background", "orange");
      } else {
        $("#two").css("background", "#eee");
      }
      if (this.msgText == 3) {
        $("#three").css("background", "#00D1B2");
      } else {
        $("#three").css("background", "#eee");
      }
    }
  }
};
</script>
<style scoped>
.register {
  background: white;
  width: 100%;
  height: 667px;
}
.top {
  position: relative;
  width: 100%;
  height: 200px;
}
.logo {
  position: absolute;
  top: 46px;
  width: 80%;
  height: 160px;
  left: 36px;
}
.word {
  width: 300px;
  height: 40px;
  position: relative;
  top: 176px;
  left: 50px;
  font-size: 25px;
  color: #046ac6;
}
.chu {
  position: absolute;
  left: 15px;
}
.liu {
  position: absolute;
  left: 110px;
}
.xiang {
  position: absolute;
  left: 175px;
}
.regiForm {
  width: 300px;
  position: relative;
  top: 46px;
  left: 20px;
  height: 350px;
}
.van-cell {
  color: #046ac6;
}
.sex {
  border: 0;
  width: 300px;
  height: 40px;
  border-bottom: 1px solid rgb(245, 242, 242);
  padding-left: 40px;
}
.input_span span {
  display: inline-block;
  width: 86px;
  height: 10px;
  background: #eee;
  line-height: 20px;
}

#one {
  border-top-left-radius: 5px;
  border-bottom-left-radius: 5px;
  border-right: 0px solid;
  margin-left: 20px;
  margin-right: 3px;
}

#two {
  border-left: 0px solid;
  border-right: 0px solid;
  margin-left: -5px;
  margin-right: 3px;
}

#three {
  border-top-right-radius: 5px;
  border-bottom-right-radius: 5px;
  border-left: 0px solid;
  margin-left: -5px;
}
#font span:nth-child(1) {
  color: red;
  margin-left: 55px;
}
#font span:nth-child(2) {
  color: orange;
  margin: 0 60px;
}
#font span:nth-child(3) {
  color: #00d1b2;
}
</style>

</style>
