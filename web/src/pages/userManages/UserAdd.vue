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
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="addInfo.userName" autocomplete="off" width="10px" class="firPass"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="addInfo.name" autocomplete="off" width="10px" class="firPass"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="addInfo.email" autocomplete="off" class="secPass"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="addInfo.phone" autocomplete="off" class="secPass"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="addInfo.sex" placeholder="请选择" class="selected">
            <el-option label="男" value="0"></el-option>
            <el-option label="女" value="1"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogadd = false;resetForm('addInfo')">取 消</el-button>
        <el-button type="primary" @click="addData('addInfo')">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  data() {
    return {
      dialogadd: false,
      addInfo: { userName: "", name: "", email: "", sex: "", phone: "" },
      rules: {
        userName: [
          {
            required: true,
            trigger: "blur",
            pattern: /^([\w\@.]{1,16})$/g,
            message: "请输入正确的用户名",
            trigger: "blur"
          }
        ],
        name: [
          {
            required: true,
            trigger: "blur",
            pattern: /^([\u4e00-\u9fa5]){2,7}$/,
            message: "请输入正确的名字",
            trigger: "blur"
          }
        ],
        sex: [
          {
            required: true,

            message: "性别不能为空",
            trigger: "blur"
          }
        ],
        email: [
          {
            required: true,
            pattern: /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/,
            message: "请输入正确的邮箱",
            trigger: "blur"
          }
        ],
        phone: [
          {
            required: true,
            min: 11,
            max: 11,
            pattern: /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/,
            message: "请输入正确的手机号",
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
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
    //新增接口
    addData(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          let pamars = {
            userName: this.addInfo.userName,
            nickName: this.addInfo.name,
            phonenumber: this.addInfo.phone,
            email: this.addInfo.email,
            sex: this.addInfo.sex
          };
          this.$axios.post("prod-api/backend/user/create", pamars).then(res => {
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