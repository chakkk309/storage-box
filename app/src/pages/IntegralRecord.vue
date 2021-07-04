﻿<template>
  <div>
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="left-text" @click="ClickLeft()">
        <van-icon name="arrow-left" style="position:relative;top:3px" />返回
      </div>
      <div class="title">积分记录</div>
      <div class="more">
        <!-- <van-icon name="ellipsis" /> -->
      </div>
    </div>

    <!-- 积分 -->
    <div class="inte">
      <div class="inteNum">{{totalPoints}}</div>
      <div class="nowInte">当前积分</div>
    </div>

    <!-- 积分明细 -->

    <div class="inteDetail">
      <div class="intTitle">积分明细</div>
      <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
        <van-list
          v-model="loading"
          :finished="finished"
          finished-text="没有更多了"
          @load="onLoad"
          :offset="30"
        >
          <div v-for="(item,index) in rows" :key="index">
            <van-cell>
              <ul class="intelist">
                <li>{{item.way}}</li>
                <li class="time">{{item.createTime}}</li>
                <li class="addnum">{{item.points}}</li>
              </ul>
            </van-cell>
          </div>
        </van-list>
      </van-pull-refresh>
    </div>
  </div>
</template>
<script>
import $ from "jquery";
export default {
  data() {
    return {
      rows: [],
      totalPoints: 0,
      loading: false,
      finished: false,
      refreshing: false,
      pageNum: 0,
      pageSize: 4,
      isLoading: false,
      total: 0,
      temporary: []
    };
  },
  mounted() {
    this.getData();
    this.allPoints();
  },
  methods: {
    ClickLeft() {
      this.$router.push("/personcenter");
    },

    //获取现在的积分
    allPoints() {
      this.$axios.get("prod-api/app/point/select").then(res => {
        if (res.data.code == 200) {
          this.$toast.success(res.data.msg);
          this.totalPoints = res.data.data;
        } else {
          this.$toast.fail(res.data.msg);
        }
      });
    },

    // 获取数据
    getData() {
      var arr = {};
      $.ajax({
        url:
          "prod-api/app/point/list?" +
          "pageNum=" +
          this.pageNum +
          "&pageSize=" +
          this.pageSize, //url路径
        type: "GET", //GET
        // url:'',//url路径
        // type:'POST', //GET
        async: false, //或false,是否异步
        // data:{//参数

        // },
        headers: {
          Authorization: " Bearer " + localStorage.getItem("token")
        },

        timeout: 5000, //超时时间
        dataType: "json",
        beforeSend: function(xhr) {},
        success: function(data, textStatus, jqXHR) {
          if (200 == data.code) {
            console.log(data);
            arr.data = data.rows;
            arr.total = data.total;
            // this.total = data.total;
            console.log(arr);
          }
        },
        error: function(xhr, textStatus) {
          console.log("获取失败");
        },
        complete: function() {}
      });

      return arr;

      // this.$axios
      //   .get(
      //     "prod-api/app/point/list?" +
      // "pageNum=" +
      // this.pageNum +
      // "&pageSize=" +
      // this.pageSize
      //   )
      //   .then(res => {
      //     if (res.data.code == 200) {
      //       this.$toast.success(res.data.msg);
      //       this.rows = res.data.rows;
      //       for (var i = 0; i < this.rows.length; i++) {
      //         // this.totalPoints =
      //         //   parseInt(this.rows[i].points) + parseInt(this.totalPoints);
      // if (this.rows[i].points > 0) {
      //   this.rows[i].points = "+" + this.rows[i].points;
      // }
      // if (this.rows[i].way == 1) {
      //   this.rows[i].way = "注册赠送";
      // }
      // if (this.rows[i].way == 2) {
      //   this.rows[i].way = "活动获得";
      // }
      // if (this.rows[i].way == 3) {
      //   this.rows[i].way = "积分使用";
      // }
      //       }
      //     } else if (res.data.code == 500) {
      //       this.$toast.fail(res.data.msg);
      //     }
      //   });
    },
    onLoad() {
      setTimeout(() => {
        if (this.refreshing) {
          this.rows = [];
          this.pageNum = 0;
          this.refreshing = false;
        }
        // debugger;
        this.pageNum++;
        console.log(this.pageNum);
        this.temporary = this.getData().data;
        // console.log(this.temporary);
        this.total = this.getData().total;
        console.log(this.total);
        for (var i = 0; i < this.temporary.length; i++) {
          // this.totalPoints =
          //   parseInt(this.rows[i].points) + parseInt(this.totalPoints);
          this.rows.push(this.temporary[i]);

          if (this.temporary[i].way == 1) {
            this.temporary[i].way = "注册赠送";
          }
          if (this.temporary[i].way == 2) {
            this.temporary[i].way = "活动获得";
          }
          if (this.temporary[i].way == 3) {
            this.temporary[i].way = "积分使用";
          }
        }

        this.loading = false;

        if (this.rows.length >= this.total) {
          this.finished = true;
        }
      }, 1000);
    },
    onRefresh() {
      // 清空列表数据
      this.finished = false;

      // 重新加载数据
      // 将 loading 设置为 true，表示处于加载状态
      this.loading = true;
      this.onLoad();
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
.inte {
  bottom: 130px;
  position: relative;
  text-align: center;
}
.inteNum {
  color: white;
  font-size: 50px;
  position: relative;
}
.nowInte {
  font-size: 13px;
  color: white;
}

.time {
  font-size: 13px;
  color: gray;
}
.addnum {
  color: rgb(81, 130, 236);
  font-size: 20px;
  position: relative;
  left: 200px;
  bottom: 40px;
}

.inteDetail {
  width: 85%;
  background: white;
  position: relative;
  bottom: 80px;
  margin: 10px;
  padding: 10px;
  padding-right: 20px;
  border-radius: 10px;
  /* height: auto !important ; */
 height: 350px;
 overflow: auto;
}
.intTitle {
  font-size: 17px;
  font-weight: bold;
  position: relative;
}
</style>