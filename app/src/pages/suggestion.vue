<template>
  <div>
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="left-text" @click="ClickLeft()">
        <van-icon name="arrow-left" />
      </div>
      <div class="title">意见建议</div>
      <div class="more"></div>
    </div>

    <!-- 意见栏 -->
    <div class="sugges">
      <!-- 标题 -->
      <div class="tit">
        <van-field v-model="title" placeholder="请输入标题" label="标题" />
      </div>
      <div class="content">
        <van-field
          v-model="content"
          rows="1"
          autosize
          type="textarea"
          placeholder="请在此输入建议内容"
          size="large"
        />
      </div>
    </div>
    <button class="submit" @click="suggesBtn()">提交</button>
  </div>
</template>
<script>
export default {
  data() {
    return {
      title: "",
      content: ""
    };
  },
  methods: {
    ClickLeft() {
      this.$router.go(-1);
    },
    suggesBtn() {
      let params = {
        title: this.title,
        content: this.content
      };
      this.$axios.post("prod-api/app/personal/advice", params).then(res => {
        if (200 == res.data.code) {
          this.$toast.success(res.data.msg);
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
.sugges {
  width: 95%;
  min-height: 240px;
  height: auto !important;
  margin: 10px;
  border-radius: 10px;
  background: white;
}
.tit {
  width: 90%;
  position: relative;
  top: 10px;
  left: 10px;
  border-bottom: 1px solid rgb(224, 220, 220);
}
.content {
  position: relative;
  min-height: 150px;
  height: auto !important;
  top: 10px;
  padding-left: 10px;
  text-indent: 25px;
  padding-bottom: 30px;
}
.submit {
  position: relative;
  top: 200px;
  left: 60px;
  width: 250px;
  height: 40px;
  line-height: 40px;
  background: url("../assets/bg2.png");

  color: white;
  border: none;
  border-radius: 10px;
}
</style>