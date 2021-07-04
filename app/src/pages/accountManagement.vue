<template>
  <div>
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="left-text" @click="ClickLeft()">
        <van-icon name="arrow-left" />
      </div>
      <div class="title">个人信息</div>
      <div class="more"></div>
    </div>
    <div class="accountManage">
      <ul class="list">
        <li class="tit">姓名</li>
        <li>
          <van-field :placeholder="List.nickName" readonly />
        </li>
      </ul>
      <ul class="list">
        <li class="tit">手机号</li>
        <li>
          <van-field :placeholder="List.phonenumber" readonly />
        </li>
      </ul>
      <ul class="list">
        <li class="tit">性别</li>
        <li>
          <van-field :placeholder="sex" readonly />
        </li>
      </ul>
      <ul class="list">
        <li class="tit">邮箱</li>

        <li>
          <van-field :placeholder="List.email" readonly />
        </li>
      </ul>
      <ul class="list">
        <li class="tit">修改密码</li>
        <li class="toPw" @click="alterPassword()">
          <van-icon name="arrow" />
        </li>
      </ul>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      sex: "",
      List: []
    };
  },
  mounted() {
    this.loginPerson();
  },
  methods: {
    // 调用登录人信息函数
    loginPerson() {
      this.$axios.get("prod-api/common/getLoginInfo").then(res => {
        if (res.data.code == 200) {
          this.$toast.success(res.data.msg);
          this.List = res.data.user;
          if (this.List.sex == 0) {
            this.sex = "男";
          } else if (this.List.sex == 1) {
            this.sex = "女";
          }
        } else {
          this.$toast.fail(res.data.msg);
        }
      });
    },
    ClickLeft() {
      this.$router.go(-1);
    },
    alterPassword() {
      this.$router.push("/alterpassword");
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
.accountManage {
  width: 95%;
  min-height: 200px;
  background: white;
  border-radius: 10px;
  margin: 10px;
}
.list {
  padding-left: 20px;

  display: flex;
  justify-content: space-between;
}
.tit {
  padding-top: 10px;
  padding-bottom: 10px;
  font-size: 14px;
}
.toPw {
  padding-top: 10px;
  padding-right: 20px;
  color: gray;
}
</style>