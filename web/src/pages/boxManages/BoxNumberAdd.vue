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
          <el-select v-model="addInfo.boxStandard" @change="jifen()" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.id"
              :label="item.boxStandard"
              :value="item.boxStandard"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="所需积分" prop="boxUnitPrice">
          <el-input readonly v-model="addInfo.boxUnitPrice" autocomplete="off" class="secPass"></el-input>
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
    var checkUser = (rule, value, callback) => {
      var user = /([\*\"\'<>\/])+/;

      if (!value) {
        callback(new Error("使用人不能为空"));
      }
      setTimeout(() => {
        if (user.test(value)) {
          callback(new Error("使用人不能有特殊符号"));
        } else {
          callback();
        }
      }, 100);
    };
    return {
      options: [],
      dialogadd: false,
      addInfo: {
        boxStandard: "",
        boxUnitPrice: "",
        remark: ""
      },
      rules: {
        boxStandard: [
          {
            required: true,
            trigger: "blur",
            // pattern: /^([\w\@.]{1,14})$/g,
            message: "请输入正确的箱子规格",
            trigger: "blur"
          }
        ],
        boxUnitPrice: [
          {
            required: true,
            message: "请输入正确的所需积分",
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
  mounted() {
    this.standradDown();
  },
  methods: {
    //箱子規格下拉列表
    standradDown() {
      this.$axios.get("prod-api/backend/box/standard/select").then(res => {
        if (res.data.code == 200) {
          this.options = res.data.data;
        } else {
          this.$message({
            showClose: true,
            message: res.data.msg,
            type: "error"
          });
        }
      });
    },
    // 积分
    jifen() {
      for (var i in this.options) {
        if (this.options[i].boxStandard == this.addInfo.boxStandard) {
          this.addInfo.boxUnitPrice = this.options[i].boxUnitPrice;
        }
      }
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    openDialog(flag, val) {
      this.dialogadd = flag;
      this.$nextTick(() => {
        //修改数据后用此方法可立即获得更新后的数据
        this.$refs.addInfo.resetFields();
      });
    },
    // 新增数据接口
    addData(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          let pamars = {
            boxStandard: this.addInfo.boxStandard,
            boxUnitPrice: this.addInfo.boxUnitPrice,
            remark: this.addInfo.remark
          };
          this.$axios
            .post("prod-api/backend/box/info/create", pamars)
            .then(res => {
              if (200 == res.data.code) {
                this.$message({
                  showClose: true,
                  message: res.data.msg,
                  type: "success"
                });
                this.$parent.getData();
                this.dialogadd = false;
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