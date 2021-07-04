<template>
  <div class="personCenter">
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="left-text" @click="ClickLeft()">
        <van-icon name="arrow-left" style="position:relative;top:3px" />返回
      </div>
      <div class="title">个人中心</div>
      <div class="more"></div>
    </div>

    <!-- 个人信息 -->

    <div class="myImfo">
      <div class="head"></div>
      <div class="name">{{username}}</div>
      <div class="isVip">
        <van-icon name="vip-card" />
      </div>
      <div class="isVipText">{{isVip}}</div>
      <div class="idManage" @click="accountManage()">
        账户管理
        <van-icon name="arrow" />
      </div>
    </div>

    <!-- 操作 -->
    <div class="allUl">
      <van-list>
        <ul class="oper" @click="pointers()">
          <li>积分记录</li>
          <li>
            <van-icon name="arrow" />
          </li>
        </ul>
        <ul class="oper" @click="addrManage()">
          <li>地址管理</li>
          <li>
            <van-icon name="arrow" />
          </li>
        </ul>
        <ul class="oper" @click="alertPw()">
          <li>修改密码</li>
          <li>
            <van-icon name="arrow" />
          </li>
        </ul>

        <ul class="oper" @click="sugges()">
          <li>意见建议</li>
          <li>
            <van-icon name="arrow" />
          </li>
        </ul>
        <ul class="oper" @click="aboutMe()">
          <li>关于我们</li>
          <li>
            <van-icon name="arrow" />
          </li>
        </ul>
      </van-list>
      <van-button type="danger" @click="logout">退出登录</van-button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: "",
      isVip: "个人会员"
    };
  },
  mounted() {
    this.username = localStorage.username;
  },
  methods: {
    onClickLeft() {
      this.$router.push("/index");
    },
    onClickRight() {},
    ClickLeft() {
      this.$router.push("/index");
    },
    aboutMe() {
      this.$router.push("/aboutme");
    },
    sugges() {
      this.$router.push("/suggestion");
    },
    pointers() {
      this.$router.push("/integralrecord");
    },
    addrManage() {
      this.$router.push({
        name: "addrmanage",
        query: {
          from: "personcenter"
        }
      });
    },
    accountManage() {
      this.$router.push("/accountmanagement");
    },
    alertPw() {
      this.$router.push("/alterpassword");
    },
    logout() {
      this.$axios.post("prod-api/common/logout").then(res => {
        if (res.data.code == 200) {
          this.$toast.success(res.data.msg);
          this.$router.push("/");
        } else {
          this.$toast.fail(res.data.msg);
        }
      });
    }
  }
};
</script>
<style scoped>
.navbar {
  width: 100%;
  height: 200px;
  background: url("../assets/bg2.png");
  position: relative;
  display: flex;
  justify-content: space-between;
  color: white;
}
.left-text {
  padding: 10px;
}
.title {
  padding: 10px;
  padding-right: 30px;
}

.more {
  padding: 10px;
}
.myImfo {
  width: 95%;
  border-radius: 10px;
  height: 150px;
  position: relative;
  bottom: 100px;
  margin: 10px;
  background: white;
}
.idManage {
  color: rgb(255, 255, 255);
  border-radius: 10px;
  width: 100px;
  position: absolute;
  top: 20px;
  border-radius: 10px;
  z-index: 1;
  right: -10px;
  background: #0068c6;
  font-size: 14px;
  padding-left: 10px;
}
.head {
  background: url("../assets/head.png");
  background-repeat: no-repeat;
  background-size: 70px 70px;
  width: 70px;
  height: 70px;
  border-radius: 100px;
  position: absolute;
  top: 30px;
  left: 50px;
}
.name {
  position: absolute;
  left: 150px;
  top: 40px;
}
.isVip {
  position: absolute;
  left: 150px;
  top: 70px;
  font-size: 14px;
  color: red;
}
.isVipText {
  color: rgb(146, 138, 138);
  position: absolute;
  left: 170px;
  top: 72px;
  font-size: 8px;
}
.oper {
  margin: 20px;
  display: flex;
  justify-content: space-between;
}
.allUl {
  padding: 10px;
  width: 90%;
  height: 220px;
  border-radius: 10px;
  position: relative;
  bottom: 100px;
  margin: 10px;
  background: white;
}
.van-button--danger {
  position: relative;
  top: 50px;
  left: 10px;
  border-radius: 50px;
  width: 300px;
}
</style>