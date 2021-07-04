<template>
  <div class="alterPw">
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="left-text" @click="ClickLeft()">
        <van-icon name="arrow-left" />
      </div>
      <div class="title">修改密码</div>
      <div class="more"></div>
    </div>

    <div class="pwBox">
      <div class="pwInp">
        <van-cell-group>
          <van-field
            v-model="beforeValue"
            @blur="checkbeforePw()"
            placeholder="请输入原密码"
            label="原密码"
          />
        </van-cell-group>
        <van-cell-group>
          <van-field v-model="newValue" @blur="cheknewPw()" placeholder="请输入新密码" label="新密码" />
        </van-cell-group>
        <van-cell-group>
          <van-field v-model="sureValue" @blur="checkPw()" placeholder="请确认密码" label="确认密码" />
        </van-cell-group>
      </div>
    </div>

    <button class="submit" @click="pwBtn()">提交</button>
  </div>
</template>
<script>
export default {
  data() {
    return {
      beforeValue: "",
      newValue: "",
      sureValue: ""
    };
  },
  methods: {
    pwBtn() {
      this.$axios
        .put(
          "prod-api/app/personal/updatePassword/" +
            this.beforeValue +
            "/" +
            this.sureValue
        )
        .then(res => {
          if (200 == res.data.code) {
            this.$toast.success(res.data.msg);
  
          } else {
            this.$toast.fail(res.data.msg);
          }
        });
    },
    ClickLeft() {
      this.$router.go(-1);
    },
    checkbeforePw() {
      var password = /^[A-Za-z0-9]{3,16}$/;
      if (!this.beforeValue) {
        this.$toast.fail("密码不能为空！");
      } else if (!password.test(this.beforeValue)) {
        this.$toast.fail("密码最大长度16，只能包含英文和数字 ");
        this.beforeValue = "";
      }
    },
    cheknewPw() {
      var password = /^[A-Za-z0-9]{3,16}$/;
      if (!this.newValue) {
        this.$toast.fail("密码不能为空！");
      } else if (!password.test(this.newValue)) {
        this.$toast.fail("密码最大长度16，只能包含英文和数字 ");
        this.newValue = "";
      } else if (this.newValue == this.beforeValue) {
        this.$toast.fail("新密码不能和原密码一样");
        this.newValue = "";
      }
    },
    checkPw() {
      if (!(this.newValue == this.sureValue)) {
        this.$toast.fail("两次密码不一样");
        (this.newValue = ""), (this.sureValue = "");
      }
    }
  }
};
</script>
<style scoped>
.navbar {
  width: 100%;
  height: 46px;
  background: url("../assets/bg2.png");
  position: fixed;
  display: flex;
  justify-content: space-between;
  color: white;
  z-index: 1;
  font-size: 15px;
  line-height: 46px;
  position: relative;
  top: 0;
}
.left-text {
  padding: 10px;
  position: relative;
  bottom: 8px;
}
.more {
  padding: 10px;
}
.pwBox {
  width: 95%;
  height: 150px;
  margin: 10px;
  border-radius: 10px;
  background: white;
}
.pwInp {
  width: 300px;
  height: 120px;
  position: relative;
  left: 10px;
  top: 1px;
}
.submit {
  position: relative;
  top: 200px;
  left: 50px;
  width: 250px;
  height: 40px;
  line-height: 40px;
  background: url("../assets/bg2.png");

  color: white;
  border: none;
  border-radius: 50px;
}
</style>