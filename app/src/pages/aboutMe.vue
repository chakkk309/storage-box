<template>
  <div>
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="left-text" @click="ClickLeft()">
        <van-icon name="arrow-left" />
      </div>
      <div class="title">关于我们</div>
      <div class="more"></div>
    </div>
    <!-- 关于我们内容 -->
    <div class="content">
      为了满足个人用户对临时寄放物品等仓储业务的需求，储留箱有限公司出了以上海市微服务范围的个人物品想式寄存
      上门送箱、上门收箱/物品周转服务。储留箱个人仓储业务系统也是基于该业务开发、实施的软件项目。
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {};
  },
  methods: {
    ClickLeft() {
      this.$router.go(-1);
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
.content {
  width: 80%;
  min-height: 150px;
  height: auto !important;
  background: white;
  margin: 15px;
  border-radius: 10px;
  padding: 20px;
  text-indent: 25px;
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
  border-radius: 5px;
}
</style>