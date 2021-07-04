<template>
  <div>
    <!-- navbar -->
    <van-nav-bar fixed title="广告内容" left-text="返回" left-arrow @click-left="onClickLeft" />

    <div class="box">
      <!-- 广告地址 -->
      <div>
        <van-image width="100%" height="220" :src="imgUrl" />
      </div>

      <!-- 广告标题 -->
      <div class="title">
        <span class="titles">标题</span>
        {{title}}
      </div>
      <!-- 积分 -->
      <div class="title">
        <span class="titles">积分</span>
        <label for style="color:red">{{points}}</label>
      </div>
      <!-- 广告内容 -->
      <div class="content">
        <span class="titles">内容</span>
        {{content}}
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      id: "",
      imgUrl: "",
      content: "",
      title: "",
      points: ""
    };
  },
  mounted() {
    this.id = localStorage.advertId;
    // console.log(localStorage.advertId);
    this.getData();
  },
  methods: {
    onClickLeft() {
      this.$router.go(-1);
    },
    getData() {
      this.$axios.get("prod-api/app/advertisement/list").then(res => {
        if (res.data.code == 200) {
          for (var i = 0; i < res.data.data.length; i++) {
            if (res.data.data[i].id == this.id) {
              this.imgUrl = "prod-api" + res.data.data[i].imgUrl;
              this.title = res.data.data[i].title;
              this.content = res.data.data[i].content;
              this.points = res.data.data[i].points;
            }
          }
        }
      });
    }
  }
};
</script>
<style scoped>
.my-swipe .van-swipe-item {
  color: #fff;
  font-size: 20px;
  height: 220px;
  line-height: 220px;
  text-align: center;
  background-color: #39a9ed;
}
.box {
  position: relative;
  top: 46px;
}
.title {
  width: 90%;
  min-height: 10px;
  height: auto !important;
  background: white;
  margin: 10px;
  border-radius: 10px;
  padding: 10px;
  text-indent: 25px;
}
.titles {
  position: relative;
  left: -10px;
  font-weight: bold;
}
.content {
  width: 90%;
  height: 250px;

  background: white;
  margin: 10px;
  border-radius: 10px;
  padding: 10px;
  text-indent: 25px;
  overflow: auto;
}
</style>