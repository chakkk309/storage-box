<template>
  <div>
    <el-dialog
      :visible.sync="dialogadd"
      width="400px"
      :close-on-click-modal="false"
      @close="resetForm('addInfo')"
    >
      <el-form
        :model="addInfo"
        status-icon
        :rules="rules"
        ref="addInfo"
        label-width="200px"
        class="demo-ruleForm"
      >
        <el-form-item label="箱子规格" prop="boxStandard">
          <el-input v-model="addInfo.boxStandard" autocomplete="off" width="10px" class="firPass"></el-input>
        </el-form-item>
        <el-form-item label="数量" prop="inventoryNumber">
          <el-input
            v-model="addInfo.inventoryNumber"
            autocomplete="off"
            width="10px"
            class="firPass"
          ></el-input>
        </el-form-item>
        <el-form-item label="所需积分" prop="boxUnitPrice">
          <el-input v-model="addInfo.boxUnitPrice" autocomplete="off" class="secPass"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="addInfo.remark" autocomplete="off" class="secPass"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogadd = false;resetForm('addInfo')">取 消</el-button>
        <el-button type="primary" @click="addData('addInfo')">保 存</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  data() {
    return {
      dialogadd: false,
      options: [],
      addInfo: {
        boxStandard: "",
        inventoryNumber: "",
        boxUnitPrice: "",
        remark: ""
      },
      rules: {
        boxStandard: [
          {
            required: true,
            trigger: "blur",
            pattern: /\d+\*\d+\*\d+/,
            message: "箱子规格格式错误",
            trigger: "blur"
          }
        ],
        boxUnitPrice: [
          {
            required: true,
            trigger: "blur",
            // pattern: /^([\w\@.]{1,14})$/g,
            message: "所需积分不能为空",
            trigger: "blur"
          }
        ],

        inventoryNumber: [
          {
            required: true,
            pattern: /^([0-9]\d*(\.\d+)?)$/g,
            message: "数量不能小于0或为空",
            trigger: "blur"
          }
        ],
        remark: [
          {
            required: true,

            message: "备注不能为空",
            trigger: "blur"
          }
        ]
      }
    };
  },

  methods: {
    resetForm(formName) {
      if (this.$refs[formName] !== undefined) {
        this.$refs[formName].resetFields();
      }
    },
    // 弹出框
    openDialog(flag, val) {
      this.dialogadd = flag;
      this.$nextTick(() => {
        //修改数据后用此方法可立即获得更新后的数据
        this.$refs.addInfo.resetFields();
      });
    },

    // 新增接口
    addData(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          let pamars = {
            boxStandard: this.addInfo.boxStandard,
            boxUnitPrice: this.addInfo.boxUnitPrice,
            inventoryNumber: this.addInfo.inventoryNumber,
            remark: this.addInfo.remark
          };
          this.$axios
            .post("prod-api/backend/box/standard/create", pamars)
            .then(res => {
              if (200 == res.data.code) {
                this.$message({
                  showClose: true,
                  message: res.data.msg,
                  type: "success"
                });

                this.$parent.getData();
                this.$parent.standradDown();
                this.dialogadd = false;
                this.resetForm();
              } else {
                this.$message({
                  showClose: true,
                  message: res.data.msg,
                  type: "error"
                });
              }
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.demo-ruleForm {
  position: relative;
  left: -70px;
}
</style>