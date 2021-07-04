<template>
  <div class="orderList" id="orderList">
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="left-text" @click="onClickLeft()">
        <van-icon name="arrow-left" style="position:relative;top:3px" />返回
      </div>
      <div class="title">订单列表</div>
      <div class="more"></div>
    </div>

    <!-- 订单列表 -->
    <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
      <van-list
        v-model="loading"
        :immediate-check="false"
        :finished="finished"
        finished-text="- 没有更多了 -"
        @load="onLoad"
        :offset="50"
      >
        <div
          class="allList"
          id="allList"
          v-for="(item,index) in list"
          :key="index"
          @click="Detail(item)"
        >
          <ul class="list">
            <li>订单编号：{{item.orderCode}}</li>
            <li>{{item.status}}</li>
          </ul>
          <ul class="list">
            <li>订单名称：{{item.orderName}}</li>
            <li>租赁时长：{{item.leaseDuration}} 个月</li>
          </ul>
          <ul class="list">
            <li>箱子编号：{{item.boxCode}}</li>
            <li>箱子规格：{{item.boxStandard}}</li>
          </ul>
          <ul class="list">
            <li>箱子单价积分：{{item.boxUnitPrice}}</li>
            <li>创建时间:{{item.createTime}}</li>
          </ul>
        </div>
      </van-list>
    </van-pull-refresh>
  </div>
</template>
<script>
import $ from "jquery";

export default {
  data() {
    return {
      list: [],
      listData: [],
      pageNum: 0,
      pageSize: 4,
      refreshing: false,
      total: 0,
      finished: false, // 数据是否请求结束，结束会先显示- 没有更多了 -
      isLoading: false,
      loading: false, //是否转圈圈,
      temporary:[]
    };
  },
  
  mounted() {
    let winHeight = document.documentElement.clientHeight; //视口大小
    document.getElementById("orderList").style.height = winHeight - 46 + "px"; //调整上拉加载框高度

    this.onLoad();
  },
  methods: {
    getData() {
      var arr = {};
      $.ajax({
        url:
          "prod-api/app/order/list?" +
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
            // console.log(data);
            arr.data = data.rows;
            arr.total = data.total;
            // this.total = data.total;
            // console.log(arr.data);
          }
        },
        error: function(xhr, textStatus) {
          console.log("获取失败");
        },
        complete: function() {}
      });

      return arr;
     
    },

    Detail(item) {
      console.log(item);
      this.$router.push({
        name: "listdetail",
        params: { id: item.id }
      });
    },

    onClickLeft() {
      this.$router.push("/index");
    },
    //列表加载

    onLoad() {
      setTimeout(() => {
        if (this.refreshing) {
          this.list = [];
          this.pageNum = 0;
          this.refreshing = false;
        }
        // debugger;
        this.pageNum++;
      
        // console.log(this.pageNum);
        // console.log("getDATA");
        // console.log(this.getData());
  
        // console.log(this.temporary);

        this.temporary = this.getData().data;
        this.total = this.getData().total;
        // console.log("temporary");

        console.log(this.temporary);

        // console.log(this.total);

        for (var i = 0; i < this.temporary.length; i++) {
          if (this.temporary[i].status == 1) {
            this.temporary[i].status = "已下上门单";
          } else if (this.temporary[i].status == 2) {
            this.temporary[i].status = "待收空箱";
          } else if (this.temporary[i].status == 3) {
            this.temporary[i].status = "已收空箱";
          } else if (this.temporary[i].status == 4) {
            this.temporary[i].status = "已发重箱";
          } else if (this.temporary[i].status == 5) {
            this.temporary[i].status = "已存储";
          } else if (this.temporary[i].status == 6) {
            this.temporary[i].status = "已下提取单";
          } else if (this.temporary[i].status == 7) {
            this.temporary[i].status = "待收重箱";
          } else if (this.temporary[i].status == 8) {
            this.temporary[i].status = "已收重箱";
          } else if (this.temporary[i].status == 9) {
            this.temporary[i].status = "已发空箱";
          } else if (this.temporary[i].status == 10) {
            this.temporary[i].status = "已完成";
          }
          this.list.push(this.temporary[i]);
        }
      
   if (this.list.length >= this.total) {
          this.finished = true;
        }
        this.loading = false;

       
      }, 1000);
    },
    onRefresh() {
      // 清空列表数据
      this.finished = false;
      this.refreshing = true;
      // 重新加载数据
      // 将 loading 设置为 true，表示处于加载状态
      this.loading = true;
      this.onLoad();
    }
  }
};
</script>
<style scoped lang="scss">
.navbar {
  width: 100%;
  height: 46px;
  background: url("../assets/bg2.png");
  position: fixed;
  display: flex;
  justify-content: space-between;
  color: white;
  z-index: 1;
}
.left-text {
  padding: 10px;
}
.more {
  padding: 10px;
}

.title {
  padding: 10px;
  padding-right: 60px;
}

.orderList {
  height: 100%;
}
.allList {
  font-size: 13px;
  height: 100%;
  margin-bottom: 10px;
  position: relative;
  top: 46px;
}
.list {
  width: 95%;
  padding: 10px;
  background: white;
  display: flex;
  justify-content: space-between;
  height: 20px;
  line-height: 20px;
  border-bottom: 1px solid rgb(223, 221, 221);
}
.navbar {
  background: url("../assets/bg2.png");
}
/deep/ .van-list__finished-text {
  margin: 40px;
}
</style>