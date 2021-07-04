<template>
  <div>
    <el-dialog
      :visible.sync="dialogDetail"
      width="400px"
      :close-on-click-modal="false"
      @close="resetForm('details')"
    >
      <el-form
        :model="details"
        status-icon
        ref="details"
        label-width="180px"
        class="demo-ruleForm"
        :inline="true"
      >
        <el-form-item label="订单编号" prop="orderCode">
          <el-input v-model="details.orderCode" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="订单名称" prop="orderName">
          <el-input v-model="details.orderName" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="订单状态" prop="status">
          <el-input v-model="details.status" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="details.customerName" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="箱子id" prop="boxId">
          <el-input v-model="details.boxId" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="箱子规格" prop="boxStandard">
          <el-input v-model="details.boxStandard" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="所需积分" prop="integration">
          <el-input v-model="details.integration" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="租赁时长" prop="leaseDuration">
          <el-input v-model="details.leaseDuration" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="实付积分" prop="totalIntegration">
          <el-input v-model="details.totalIntegration" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="空箱上门下单时间" prop="emptyBoxCallTime" v-if="emptyBoxShow">
          <el-input v-model="details.emptyBoxCallTime" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="重箱上门下单时间" prop="heavyBoxCallTime" v-if="heavyBoxShow">
          <el-input v-model="details.heavyBoxCallTime" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="空箱上门姓名" prop="emptyBoxName" v-if="emptyBoxNameShow">
          <el-input v-model="details.emptyBoxName" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="空箱上门电话" prop="emptyBoxPhone" v-if="emptyBoxPhoneShow">
          <el-input v-model="details.emptyBoxPhone" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="空箱上门地址" prop="emptyBoxAddr" v-if="emptyBoxAddrShow">
          <el-input v-model="details.emptyBoxAddr" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="空箱上门时间" prop="emptyBoxTime" v-if="emptyBoxTimeShow">
          <el-input v-model="details.emptyBoxTime" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="空箱上门时段" prop="emptyBoxTimeto" v-if="emptyBoxTimetoShow">
          <el-input v-model="details.emptyBoxTimeto" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="重箱提取姓名" prop="heavyBoxName" v-if="heavyBoxShow">
          <el-input v-model="details.heavyBoxName" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="重箱提取电话" prop="heavyBoxPhone" v-if="heavyBoxPhoneShow">
          <el-input v-model="details.heavyBoxPhone" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="重箱提取地址" prop="heavyBoxAddr" v-if="heavyBoxAddrShow">
          <el-input v-model="details.heavyBoxAddr" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="重箱提取时间" prop="heavyBoxTime" v-if="heavyBoxTimeShow">
          <el-input v-model="details.heavyBoxTime" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>

        <el-form-item label="重箱提取时段" prop="heavyBoxTimeto" v-if="heavyBoxTimetoShow">
          <el-input v-model="details.heavyBoxTimeto" autocomplete="off" class="secPass" readonly></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogDetail = false;resetForm('details')">关 闭</el-button>
        <!-- <el-button type="primary" @click="editData('details')">确 定</el-button> -->
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  data() {
    return {
      emptyBoxShow: false,
      emptyBoxNameShow: false,
      emptyBoxPhoneShow: false,
      emptyBoxAddrShow: false,
      emptyBoxTimetoShow: false,
      emptyBoxTimeShow: false,
      heavyBoxShow: false,
      heavyBoxNameShow: false,
      heavyBoxPhoneShow: false,
      heavyBoxAddrShow: false,
      heavyBoxTimetoShow: false,
      heavyBoxTimeShow: false,
      emptyBoxPhone: false,
      emptyBoxAddr: false,
      emptyBoxTimeto: false,
      heavyBoxName: false,
      heavyBoxPhone: false,
      heavyBoxAddr: false,
      heavyBoxTime: false,
      heavyBoxTimeTo: false,
      heavyBoxShow: false,
      dialogDetail: false,
      id: "",
      details: {
        boxStandard: "",
        integration: "",
        leaseDuration: "",
        orderName: "",
        orderCode: "",
        status: "",
        customerName: "",
        boxId: "",
        totalIntegration: "",
        emptyBoxCallTime: "",
        heavyBoxCallTime: "",
        emptyBoxName: ""
      },
      orderStatus: ""
    };
  },

  methods: {
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.dialogDetail = false;
    },
    openDialog(flag, val) {
      console.log(val);
      this.dialogDetail = flag;
      this.$nextTick(() => {
        //修改数据后用此方法可立即获得更新后的数据
        this.$refs.details.resetFields();
        // console.log(val);
        // 获取数据
        this.id = val[0].id;
        this.$axios.get("prod-api/backend/order/info/" + this.id).then(res => {
          if (200 == res.data.code) {
            this.details.orderCode = res.data.data.boxCode;
            this.details.orderName = res.data.data.orderName;
            this.details.status = res.data.data.status;
            this.details.customerName = res.data.data.nickName;
            this.details.boxId = res.data.data.boxId;
            this.details.boxStandard = res.data.data.boxStandard;
            this.details.integration = res.data.data.boxUnitPrice;
            this.details.leaseDuration = res.data.data.leaseDuration;
            this.details.totalIntegration = res.data.data.totalPrice;
            this.details.emptyBoxCallTime = res.data.data.emptyBoxCallTime;
            this.details.heavyBoxCallTime = res.data.data.heavyBoxCallTime;
            this.details.emptyBoxName = res.data.data.emptyBoxCallName;
            this.details.emptyBoxPhone = res.data.data.emptyBoxCallPhone;
            this.details.emptyBoxTime = res.data.data.emptyBoxCallTime;
            this.details.emptyBoxTimeto = res.data.data.emptyBoxCallInterval;
            this.details.heavyBoxName = res.data.data.heavyBoxCallName;
            this.details.heavyBoxPhone = res.data.data.heavyBoxCallPhone;
            this.details.heavyBoxAddr = res.data.data.heavyBoxCallAddress;
            this.details.heavyBoxTime = res.data.data.heavyBoxCallTime;
            this.details.heavyBoxTimeto = res.data.data.heavyBoxCallInterval;

            this.details.emptyBoxAddr = res.data.data.emptyBoxCallAddress;

            this.$message({
              showClose: true,
              message: res.data.msg,
              type: "success"
            });

            this.dialogadd = false;
          } else {
            this.$message({
              showClose: true,
              message: res.data.msg,
              type: "error"
            });
          }
        });
        if (val[0].status == "待发空箱") {
          this.orderStatus = 1;
        }
        if (val[0].status == "已发空箱") {
          this.orderStatus = 2;
        }
        if (val[0].status == "送达空箱") {
          this.orderStatus = 3;
        }
        if (val[0].status == "待收重箱") {
          this.orderStatus = 4;
        }
        if (val[0].status == "已存储") {
          this.orderStatus = 5;
        }
        if (val[0].status == "待发重箱") {
          this.orderStatus = 6;
        }
        if (val[0].status == "已发重箱") {
          this.orderStatus = 7;
        }
        if (val[0].status == "送达重箱") {
          this.orderStatus = 8;
        }
        if (val[0].status == "待收空箱") {
          this.orderStatus = 9;
        }
        if (val[0].status == "送达空箱") {
          this.orderStatus = 10;
        }

        // console.log(this.details.id);
        if (this.orderStatus >= 1 && this.orderStatus <= 5) {
          this.emptyBoxShow = true;
          this.emptyBoxNameShow = true;
          this.emptyBoxPhoneShow = true;
          this.emptyBoxAddrShow = true;
          this.emptyBoxTimetoShow = true;
          this.emptyBoxTimeShow = true;
          this.heavyBoxShow = false;
          this.heavyBoxNameShow = false;
          this.heavyBoxPhoneShow = false;
          this.heavyBoxAddrShow = false;
          this.heavyBoxTimetoShow = false;
          this.heavyBoxTimeShow = false;
        } else if (this.orderStatus > 5 && this.orderStatus <= 10) {
          this.emptyBoxShow = false;
          this.emptyBoxNameShow = false;
          this.emptyBoxPhoneShow = false;
          this.emptyBoxAddrShow = false;
          this.emptyBoxTimetoShow = false;
          this.emptyBoxTimeShow = false;
          this.heavyBoxShow = true;
          this.heavyBoxNameShow = true;
          this.heavyBoxPhoneShow = true;
          this.heavyBoxAddrShow = true;
          this.heavyBoxTimetoShow = true;
          this.heavyBoxTimeShow = true;
        }
        this.details.boxStandard = val[0].boxStandard;
        this.details.integration = val[0].integration;
        this.details.orderName = val[0].orderName;
        this.details.orderCode = val[0].orderCode;
        this.details.status = val[0].status;
        this.details.customerName = val[0].customerName;
        this.details.boxId = val[0].boxId;
        this.details.leaseDuration = val[0].leaseDuration;
        this.details.totalIntegration = val[0].totalIntegration;
        this.details.emptyBoxCallTime = val[0].emptyBoxCallTime;
        this.details.heavyBoxCallTime = val[0].heavyBoxCallTime;
      });
    },
    cancelDialog() {
      this.dialogEdit = false;
    }
  }
};
</script>
<style lang="scss" scoped>
.demo-ruleForm {
  position: relative;
  left: -70px;
  .secPass {
    width: 150px;
  }
}
</style>