<template>
  <div class="allList1" id="allList">
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="left-text" @click="onClickLeft()">
        <van-icon name="arrow-left" style="position:relative;top:3px" />返回
      </div>
      <div class="title">订单详情</div>
      <div class="more"></div>
    </div>

    <!-- 订单列表 -->
    <van-pull-refresh v-model="isLoading" @refresh="onRefresh">
      <van-list :finished="finished" :offset="50">
        <div class="allList">
          <ul class="listTit1">
            <li>订单编号：{{rows.orderCode}}</li>
            <li v-if="emptyShow">空箱上门下单时间：{{rows.emptyBoxOrderTime}}</li>
            <li v-if="heavyShow">重箱上门下单时间：{{rows.heavyBoxOrderTime}}</li>
          </ul>
          <ul class="list">
            <li>订单名称：{{rows.orderName}}</li>
          </ul>

          <ul class="listTit1">
            <li>箱子信息</li>
          </ul>
          <ul class="list">
            <li>箱子编号：{{rows.boxCode}}</li>
            <li>箱子规格：{{rows.boxStandard}}</li>
          </ul>
          <ul class="list">
            <li>箱子单价积分：{{rows.boxUnitPrice}}</li>
            <li>租赁时长：{{rows.leaseDuration}} 个月</li>
          </ul>
          <ul class="list">
            <li>总共积分：{{totalPrice}}</li>
          </ul>
          <ul class="listTit1">
            <li>配送信息</li>
          </ul>

          <!--配送信息  -->
          <ul class="list">
            <li v-if="emptyShow">空箱上门姓名：{{rows.emptyBoxCallName}}</li>
            <li v-if="heavyShow">重箱上门姓名：{{rows.heavyBoxCallName}}</li>
          </ul>
          <ul class="list">
            <li v-if="emptyShow">空箱上门电话：{{rows.emptyBoxCallPhone}}</li>
            <li v-if="heavyShow">重箱上门电话：{{rows.heavyBoxCallPhone}}</li>
          </ul>
          <ul class="list">
            <li v-if="emptyShow">空箱上门地址：{{rows.emptyBoxCallAddress}}</li>
            <li v-if="heavyShow">重箱上门地址：{{rows.heavyBoxCallAddress}}</li>
          </ul>
          <ul class="list">
            <li v-if="emptyShow">空箱上门时间：{{rows.emptyBoxCallTime}}</li>
            <li v-if="heavyShow">重箱上门时间：{{rows.heavyBoxCallTime}}</li>
          </ul>
          <ul class="list">
            <li v-if="emptyShow">空箱上门时段：{{rows.emptyBoxCallInterval}}</li>
            <li v-if="heavyShow">重箱上门时段：{{rows.heavyBoxCallInterval}}</li>
          </ul>
        </div>
      </van-list>

      <div class="bottomInp">
        <ul class="statusBtn">
          <li>订单状态：{{btnValue}}</li>
          <li>
            <van-button
              type="info"
              @click="ThestatusBtn(rows)"
              class="subBtn"
              v-show="btnShow"
            >{{statusBtn}}</van-button>
          </li>
        </ul>
      </div>
    </van-pull-refresh>
    <!-- 弹出框 -->
    <van-popup v-model="show" position="bottom" :style="{ height: '50%' }">
      <!-- 上门时间 -->
      <van-datetime-picker
        @cancel="cancelPicker"
        @confirm="confirmPicker"
        v-model="timeValue"
        type="datetime"
        title="选择完整时间"
        :min-date="minDate"
        :max-date="maxDate"
      />
    </van-popup>
    <van-popup v-model="sdshow" position="bottom" :style="{ height: '50%' }">
      <!-- 上门时段 -->
      <van-datetime-picker
        @cancel="cancelPicker"
        @confirm="confirmIntervalStart"
        v-model="currentStartTime"
        type="time"
        title="选择开始时间"
        :min-hour="10"
        :max-hour="20"
      />
      <van-datetime-picker
        @cancel="cancelPicker"
        @confirm="confirmIntervalEnd"
        v-model="currentEndTime"
        type="time"
        title="选择结束时间"
        :min-hour="10"
        :max-hour="20"
      />
    </van-popup>
  </div>
</template>
<script>
export default {
  data() {
    return {
      sdshow: false,
      show: false, //弹出层是否显示
      currentStartTime: "",
      timeValue: "",
      currentEndTime: "",
      minDate: new Date(),
      maxDate: new Date(2025, 10, 1),
      heavyBoxCallTime: "",
      heavyBoxCallPhone: "",
      heavyBoxCallAddress: "",
      heavyBoxCallInterval: "",
      heavyBoxCallName: "",
      heavyBoxCallInterval: "",
      heavyShow: false,
      emptyShow: false,
      btnValue: "-",
      rows: [],
      listData: [],
      statusBtn: "",
      btnShow: false,
      inputShow: false,
      imShow: false,
      currentDate: "",
      Interval: "",
      operate: "",
      isLoading: false,
      finished: false,
      totalPrice: ""
    };
  },
  mounted() {
    this.getData();

    let winHeight = document.documentElement.clientHeight; //视口大小
    document.getElementById("allList").style.height = winHeight - 46 + "px"; //调整上拉加载框高度
  },
  methods: {
    // 下拉刷新
    onRefresh() {
      setTimeout(() => {
        this.$toast("刷新成功");

        this.getData();
        this.isLoading = false;
      }, 1000);
    },
    // 检测手机号格式
    phoneCheck() {
      var phonenumber = /^1[3456789]\d{9}$/;
      if (!this.heavyBoxCallPhone) {
        this.$toast.fail("手机号不能为空！");
      } else if (!phonenumber.test(this.heavyBoxCallPhone)) {
        this.$toast.fail("请输入正确的手机号！");
        this.heavyBoxCallPhone = "";
      }
    },

    // 时段
    heavyBoxCallIntervalShow() {
      this.sdshow = true;
    },
    // 时间
    heavyBoxCallTimeShow() {
      this.show = true;
    },
    // 点击确认 开始时段格式

    confirmIntervalStart(value) {
      // console.log(value);
      this.currentStartTime = value;

      // console.log(this.Interval);
    },
    // 点击确认 结束时段格式

    confirmIntervalEnd(value) {
      // console.log(value);
      this.currentEndTime = value;
      this.Interval = this.currentStartTime + "-" + this.currentEndTime;
      this.sdshow = false;
      // console.log(this.lInterval);
    },
    // 点击确认 日期格式
    confirmPicker(value) {
      // console.log(value);
      let y = value.getFullYear();
      let m = value.getMonth() + 1;
      let d = value.getDate();
      let h = value.getHours();
      let min = value.getMinutes();
      let s = value.getSeconds();
      this.currentDate = y + "-" + m + "-" + d + " " + h + ":" + min + ":" + s;
      this.show = false;
    },
    // 点击取消
    cancelPicker() {
      this.show = false;
      this.sdshow = false;
    },
    getData() {
      this.$axios
        .get("prod-api/app/order/info/" + this.$route.params.id)
        .then(res => {
          if (res.data.code == 200) {
            this.$toast.success(res.data.msg);
            this.rows = res.data.data;
            // 总共所需积分
            this.totalPrice = this.rows.boxUnitPrice * this.rows.leaseDuration;
            // console.log(this.rows.status);
            // console.log(this.$route.params.id);
            // 将状态显示出来，信息显示：1-5显示空箱，6-10显示重箱
            if (this.rows.status == 1) {
              this.btnValue = "已上门下单";
              // this.statusBtn = "-";
              this.heavyShow = false;
              this.emptyShow = true;
              this.inputShow = false;
              this.imShow = true;
            } else if (this.rows.status == 2) {
              this.btnShow = true;
              this.btnValue = "待收空箱";
              this.statusBtn = "收取空箱";
              this.heavyShow = false;
              this.emptyShow = true;
              this.inputShow = false;
              this.imShow = true;
            } else if (this.rows.status == 3) {
              this.btnValue = "已收空箱";
              this.statusBtn = "发送重箱";
              this.heavyShow = false;
              this.btnShow = true;
              this.emptyShow = true;
              this.inputShow = false;
              this.imShow = true;
            } else if (this.rows.status == 4) {
              this.btnValue = "已发重箱";
              this.heavyShow = false;
              this.emptyShow = true;
              this.inputShow = false;
                 this.btnShow = false;
              this.imShow = true;
            } else if (this.rows.status == 5) {
              this.btnValue = "已存储";
              this.statusBtn = "预约提取";
              this.btnShow = true;
              this.inputShow = true;
              this.imShow = false;
              this.heavyShow = false;
              this.emptyShow = true;
            } else if (this.rows.status == 6) {
              this.btnValue = "已下提取单";
              this.inputShow = false;
              this.imShow = true;
              this.heavyShow = true;
              this.emptyShow = false;
            } else if (this.rows.status == 7) {
              this.btnValue = "待收重箱";
              this.statusBtn = "收取重箱";
              this.btnShow = true;
              this.inputShow = false;
              this.imShow = true;
              this.heavyShow = true;
              this.emptyShow = false;
            } else if (this.rows.status == 8) {
              this.btnValue = "已收重箱";
              this.statusBtn = "发送空箱";
              this.btnShow = true;
              this.inputShow = false;
              this.imShow = true;
              this.heavyShow = true;
              this.emptyShow = false;
            } else if (this.rows.status == 9) {
              this.btnValue = "已发空箱";
              this.heavyShow = true;
              this.emptyShow = false;
              this.inputShow = false;
              this.imShow = true;
               this.btnShow = false;
            } else if (this.rows.status == 10) {
              this.statusBtn = "删除订单";
              this.btnValue = "已完成";
              this.heavyShow = true;
              this.btnShow = true;
              this.emptyShow = false;
              this.inputShow = false;
              this.imShow = true;
            }
          } else {
            this.$toast.fail(res.data.msg);
          }
        });
    },

    ThestatusBtn(item) {
      //  将状态+1，赋给操作
      if (this.rows.status == 2) {
        this.operate = 3;
      } else if (this.rows.status == 3) {
        this.operate = 4;
      } else if (this.rows.status == 7) {
        this.operate = 8;
      } else if (this.rows.status == 8) {
        this.operate = 9;
      } else if (this.rows.status == 9) {
        this.operate = 10;
      } else if (this.rows.status == 10) {
        this.operate = 10;
      }
      // console.log(this.operate);
      // 状态为5 跳转操作页面
      if (this.rows.status == 5) {
        localStorage.setItem("from", "listdetail");
        this.$router.push({
          name: "operate",
          query: {
            id: this.rows.id,
            status: this.rows.status,
            version: this.rows.version
          }
        });
      } else {
        var allInfo = {};
        this.$axios
          .put(
            "prod-api/app/order/operate/" +
              this.rows.id +
              "/" +
              this.operate +
              "/" +
              this.rows.version,
            allInfo
          )
          .then(res => {
            if (res.data.code == 200) {
              if(this.operate==10){
      this.$router.push("/orderlist");
              }
        else{
     this.getData()
        }
              // this.$router.go(-1);
         

              this.$toast.success(res.data.msg);
            } else {
              this.$toast.fail(res.data.msg);
            }
          });
      }
    },

    // 订单操作

    onClickLeft() {
      this.$router.push("/orderlist");
    }
  }
};
</script>
<style scoped lang="scss">
.allList {
  font-size: 10px;
  height: auto !important;
  min-height: 700px;
}
.title {
  padding-right: 30px;
}
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
}
.left-text {
  padding: 10px;
  position: relative;
  bottom: 8px;
}
.more {
  padding: 10px;
}

.listTit1 {
  width: 95%;
  padding: 10px;
  display: flex;
  justify-content: space-between;
  height: 20px;
  line-height: 20px;
  position: relative;
  top: 46px;
  font-size: 10px;
  color: gray;
}
.list {
  width: 95%;
  padding: 10px;
  background: white;
  display: flex;
  justify-content: space-between;
  height: auto !important;
  min-height: 20px;
  line-height: 20px;
  border-bottom: 1px solid rgb(223, 221, 221);
  position: relative;
  font-size: 13px;
  top: 46px;
}
.bottomInp {
  background: rgb(255, 255, 255);
  width: 95%;
  height: 20px;
  line-height: 20px;
  padding: 10px;
  position: fixed;
  bottom: 0px;
}
.statusBtn {
  font-size: 12px;
  display: flex;
  justify-content: space-between;
}
.van-field {
  width: 350px;
}
.subBtn {
  line-height: 28px;
  font-size: 13px;
  width: 100px;
  height: 30px;
  line-height: 28px;
  position: relative;
  right: 10px;
  bottom: 5px;
}
</style>