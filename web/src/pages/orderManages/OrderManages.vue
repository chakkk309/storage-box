<template>
  <div class="order">
    <!-- 查询 -->
    <el-card class="box-card">
      <el-form :model="orderform" ref="orderform" label-width="100px">
        <el-form-item label="客户名称" prop="nickName" class="name">
          <el-input v-model=" orderform.nickName" class="nameInp"></el-input>
        </el-form-item>
        <el-form-item label="订单状态" prop="status" class="status">
          <el-select v-model="orderform.status" placeholder="请选择" class="selected">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="空箱下单时间" prop="emptyDate" class="data">
          <el-date-picker
            v-model="orderform.emptyDate"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :clearable="false"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="重箱下单时间" prop="heavyDate" class="data">
          <el-date-picker
            v-model="orderform.heavyDate"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :clearable="false"
          ></el-date-picker>
        </el-form-item>
      </el-form>

      <el-button type="primary" @click="search()" class="addBtn">查询</el-button>
      <el-button @click="resetForm('orderform')" class="resetBtn">重置</el-button>
    </el-card>
    <!-- 客户列表 -->
    <el-card>
      <el-button type="primary" @click="detail()">详情</el-button>
      <el-button type="primary" @click="deleteOrder()">删除</el-button>

      <siri-table
        :data="orderData"
        :page="page"
        :tableHeight="tableHeight"
        @handleSizeChange="handleSizeChange"
        @handleSelectionChange="handleSelectionChange"
        @handleCurrentChange="handleCurrentChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>

        <el-table-column label="订单编号" prop="orderCode" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="订单名称" prop="orderName" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="订单状态" prop="status" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="客户名称" prop="nickName" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="箱子id" prop="boxId" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="箱子编号" prop="boxCode" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="箱子规格" prop="boxStandard" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="所需积分" prop="boxUnitPrice" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="租赁时长" prop="leaseDuration" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="实付积分" prop="totalPrice" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column
          label="空箱上门下单时间"
          v-if="emptyShow"
          prop="emptyBoxOrderTime"
          :show-overflow-tooltip="true"
        ></el-table-column>
        <el-table-column
          label="重箱上门下单时间"
          prop="heavyBoxOrderTime"
          v-if="heavyShow"
          :show-overflow-tooltip="true"
        ></el-table-column>

        <el-table-column fixed="right" label="操作" prop="operation" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="sendNull(scope.row)"
              v-if="scope.row.status=='待发空箱'"
              style="color:orange"
            >发送空箱</el-button>
            <el-button
              size="mini"
              @click="sendNull(scope.row)"
              v-else-if="scope.row.status=='待发重箱'"
              style="color:black"
            >发送重箱</el-button>
            <el-button
              size="mini"
              @click="sendNull(scope.row)"
              v-else-if="scope.row.status=='待收重箱'"
              style="color:purple"
            >收取重箱</el-button>
            <el-button
              size="mini"
              @click="sendNull(scope.row)"
              v-else-if="scope.row.status=='待收空箱'"
              style="color:green"
            >收取空箱</el-button>

            <el-button size="mini" v-else style="display:none">--</el-button>
          </template>
        </el-table-column>
      </siri-table>
      <order-detail ref="details"></order-detail>
    </el-card>
  </div>
</template>
<script>
import siriTable from "../../components/SiruiTable/index";
import orderDetail from "../orderManages/OrderDetail";

export default {
  components: { siriTable, orderDetail },
  data() {
    return {
      tableHeight: 400,
      emptyShow: true,
      heavyShow: true,
      orderform: {
        nickName: "",
        status: "",
        emptyDate: ["", ""],
        heavyDate: ["", ""]
      },
      page: {
        pages: 0,
        total: 0,
        pageSize: 10,
        pageNum: 1
      },
      options: [
        {
          label: "待发空箱",
          value: 1
        },
        {
          label: "已发空箱",
          value: 2
        },
        {
          label: "送达空箱",
          value: 3
        },
        {
          label: "待收重箱",
          value: 4
        },
        {
          label: "已存储",
          value: 5
        },
        {
          label: "待发重箱",
          value: 6
        },
        {
          label: "已发重箱",
          value: 7
        },
        {
          label: "送达重箱",
          value: 8
        },
        {
          label: "待收空箱",
          value: 9
        },
        {
          label: "已完成",
          value: 10
        }
      ],

      orderData: [],
      multipleSelection: [],
      operate: ""
    };
  },
  mounted() {
    this.getData();
    this.heavyBox();
    // window.setInterval(() => {
    //   setTimeout(this.getData(), 0);
    // }, 1000);

    // setTimeout(this.getData(), 1000); //指定1秒刷新一次
  },
  methods: {
    heavyBox(data) {
      console.log(data);
    },
    resetForm(formName) {
      if (this.$refs[formName] != undefined) {
        this.$refs[formName].resetFields();
        this.handleCurrentChange(1);

        this.getData();
      }
    },
    // 转换汉字
    Words() {
      for (let i = 0; i < this.orderData.length; i++) {
        if (this.orderData[i].status == 1) {
          this.orderData[i].status = "待发空箱";
        } else if (this.orderData[i].status == 2) {
          this.orderData[i].status = "已发空箱";
        } else if (this.orderData[i].status == 3) {
          this.orderData[i].status = "送达空箱";
        } else if (this.orderData[i].status == 4) {
          this.orderData[i].status = "待收重箱";
        } else if (this.orderData[i].status == 5) {
          this.orderData[i].status = "已存储";
        } else if (this.orderData[i].status == 6) {
          this.orderData[i].status = "待发重箱";
        } else if (this.orderData[i].status == 7) {
          this.orderData[i].status = "已发重箱";
        } else if (this.orderData[i].status == 8) {
          this.orderData[i].status = "送达重箱";
        } else if (this.orderData[i].status == 9) {
          this.orderData[i].status = "待收空箱";
        } else if (this.orderData[i].status == 10) {
          this.orderData[i].status = "已完成";
        }
      }
    },
    //显示详细信息
    // 查询用户
    search() {
      this.handleCurrentChange(1);
      this.getData();
      // console.log(this.orderform.emptyDate);
    },

    // 删除订单
    deleteOrder() {
      console.log(this.multipleSelection.length);
      if (this.multipleSelection.length >= 1) {
        this.$confirm("此操作将会将数据永久删除，是否继续？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            let params = {
              ids: []
            };

            for (var i = 0; i < this.multipleSelection.length; i++) {
              params.ids.push(this.multipleSelection[i].id);
            }
            params.ids = params.ids.join(",");
            console.log(params.ids);

            this.$axios
              .delete("prod-api/backend/order/delete/" + params.ids)
              .then(res => {
                if (res.data.code == 200) {
                  this.$message({
                    type: "success",
                    message: res.data.msg
                  });
                  this.handleCurrentChange(1);
                  this.getData();
                } else {
                  this.$message({
                    type: "error",
                    message: res.data.msg
                  });
                }
              });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除"
            });
          });
      } else {
        this.$message({
          type: "info",
          message: "请选择数据进行操作"
        });
      }
    },

    //查询数据列表

    getData() {
      console.log(this.orderform.emptyDate[0]);
      console.log(typeof this.orderform.emptyDate[0]);
      this.$axios
        .get(
          "prod-api/backend/order/list?" +
            "pageNum=" +
            this.page.pageNum +
            "&pageSize=" +
            this.page.pageSize +
            "&nickName=" +
            this.orderform.nickName +
            "&status=" +
            this.orderform.status +
            "&emptyBoxOrderTimeStart=" +
            this.orderform.emptyDate[0] +
            "&emptyBoxOrderTimeEnd=" +
            this.orderform.emptyDate[1] +
            "&heavyBoxOrderTimeStart=" +
            this.orderform.heavyDate[0] +
            "&heavyBoxOrderTimeEnd=" +
            this.orderform.heavyDate[1]
        )
        .then(res => {
          if (200 == res.data.code) {
            this.orderData = res.data.rows;
            // console.log(this.orderData);
            this.page.total = res.data.total;
            this.Words();

            this.$message({
              showClose: true,
              message: "查询成功",
              type: "success"
            });
          } else {
            this.$message({
              showClose: true,
              message: res.data.msg,
              type: "error"
            });
          }
        });
      // }
    },

    //订单详情
    detail() {
      if (1 == this.multipleSelection.length) {
        this.$refs.details.openDialog(
          true,
          JSON.parse(JSON.stringify(this.multipleSelection))
        );
      } else if (this.multipleSelection.length < 1) {
        this.$message({
          type: "info",
          message: "请选择一条数据进行操作"
        });
      } else {
        this.$message({
          type: "info",
          message: "请选择一条数据进行操作"
        });
      }
    },

    // 重箱信息填写
    ZX() {
      this.$refs.heavyBox.openDialog(true);
    },

    // 每页条数
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getData();
    },
    // 选中触发事件
    handleSelectionChange(row) {
      // debugger;
      this.multipleSelection = row;
      // alert(JSON.stringify(this.multipleSelection));
    },
    //第几页
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.getData();
    },

    //发送空箱
    sendNull(row) {
      if (row.status == "待发空箱") {
        this.operate = 2;
      } else if (row.status == "已发空箱") {
        this.operate = 3;
      } else if (row.status == "送达空箱") {
        this.operate = 4;
      } else if (row.status == "待收重箱") {
        this.operate = 5;
      } else if (row.status == "已存储") {
        this.operate = 6;
      } else if (row.status == "待发重箱") {
        this.operate = 7;
      } else if (row.status == "已发重箱") {
        this.operate = 8;
      } else if (row.status == "送达重箱") {
        this.operate = 9;
      } else if (row.status == "待收空箱") {
        this.operate = 10;
      } else if (row.status == "已完成") {
        this.operate = 10;
      }
      // console.log("肉" + row.status);
      // console.log("操作" + this.operate);

      let params = {};

      this.$axios
        .put(
          "prod-api/backend/order/operate/" +
            row.id +
            "/" +
            this.operate +
            "/" +
            row.version,
          params
        )
        .then(res => {
          if (res.data.code == 200) {
            this.$message({
              type: "success",
              message: res.data.msg
            });
            this.getData();
          } else {
            this.$message({
              type: "error",
              message: res.data.msg
            });
          }
        });
    }
  }
};
</script>
<style lang="scss" scoped>
.order {
  margin: 10px;
  .box-card {
    height: 180px;
    margin-bottom: 10px;
    .name {
      float: left;
      position: relative;
      top: 15px;
      left: 0px;
      margin-right: 80px;
      .nameInp {
        width: 220px;
      }
    }
    .status {
      width: 400px;
      float: left;
      position: relative;
      top: 15px;
      left: 50px;
    }

    .addBtn,
    .resetBtn {
      float: left;
      position: relative;

      top: -48px;
      margin-right: 20px;
    }
    .data {
      float: left;
      position: relative;
      top: 0px;
      left: 0px;
      top: 10px;
    }
  }
}
</style>