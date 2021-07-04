<template>
  <!-- 导航栏 -->
  <div class="index">
      <!-- 导航栏 -->
    <div class="navbar">
      <div class="left-text" >
      
      </div>
      <div class="title">首页</div>
      <div class="more"></div>
    </div>

    <!-- 首页海报 -->

    <!-- 轮播图-->
    <van-swipe class="my-swipe" :autoplay="3000" indicator-color="white">
      <van-swipe-item v-for="(image, index) in imgData" :key="index" @click="getPoints">
        <img v-lazy="image" class="img" />
      </van-swipe-item>
    </van-swipe> 

    <!-- 首页选项 -->
    <div class="box">
      <div class="person">
        <img src="../assets/person.png" @click="myCenter" />
        <p>个人中心</p>
      </div>
      <div class="nullBox">
        <img src="../assets/boxnull.png" @click="boxOrder" />
        <p>空箱预约</p>
      </div>
      <div class="list">
        <img src="../assets/list.png" @click="myOrder" />
        <p>我的订单</p>
      </div>
   
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      imgData: [],
      advertPoints: []
    };
  },
  mounted() {
    this.getData();
    // this.loginPerson();
  },
  methods: {
    loginPerson() {
      // console.log(localStorage.roles);
      // if (localStorage.roles == "app") {
      // } else if (localStorage.roles == "backend") {
      //   this.$router.push("/");
      //   this.$toast.fail("用户人为PC端！");
      // }
    },

    getData() {
      this.$axios.get("prod-api/app/advertisement/list").then(res => {
        if (res.data.code == 200) {
          for (var i = 0; i < res.data.data.length; i++) {
            this.imgData.push("prod-api" + res.data.data[i].imgUrl);
          }
          this.advertPoints = res.data.data;
          // console.log(this.advertPoints);
        }
      });
    },

    // 获取积分
    getPoints(row) {
      console.log(row.path[0].src)
      var imgSrc = row.path[0].src;
      if (imgSrc != undefined) {
        var imgSrc = row.path[0].src.split("prod-api");
        var id = "";
        var points = "";
        console.log(imgSrc);
        for (var i = 0; i < this.advertPoints.length; i++) {
          // console.log(this.advertPoints[i].imgUrl);

          if (imgSrc[1] == this.advertPoints[i].imgUrl) {
            id = this.advertPoints[i].id;
            points = this.advertPoints[i].points;
            // console.log("爱爱" + id);
            // console.log("婆婆" + points);
          }
        }
      }

      let params = {
        points: points,
        id: id
      };

      // console.log(points);
      this.$axios.put("prod-api/app/advertisement/points", params).then(res => {
        // console.log(res);
        if (200 == res.data.code) {
          this.$toast.success(res.data.msg);
          this.$router.push("/advertdetail");
          localStorage.setItem("advertId", id);
        } else {
          this.$toast.fail(res.data.msg);
        }
      });
    },
 
    myCenter() {
      this.$router.push("/personcenter");
    },
    boxOrder() {
      this.$router.push({
        name: "nullbox",
        query: {
          from: "index"
        }
      });
    },
    myOrder() {
      this.$router.push("/orderlist");
    }
  }
};
</script>
<style scoped>
.index{
 height: 100%;
 
}
.navbar {
  width: 100%;
  height: 200px;
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
.box {
  position: relative;
bottom: 140px;
  display: flex;
  justify-content: space-around;
  margin: 20px;
}
.my-swipe  {
  color: #fff;
  font-size: 20px;
  width: 335px;
  height: 230px;
  line-height: 220px;
  text-align: center;
  position: relative;
  left: 20px;
 bottom:160px;
 z-index:1;
   box-shadow: 2px 2px 10px #888888;
     border-radius: 8px;
}
.img{
  width: 335px;
  height: 230px;


}

</style>