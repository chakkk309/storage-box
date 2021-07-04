<template>
  <div>
    <el-dialog
      :visible.sync="dialogEdit"
      width="400px"
      :close-on-click-modal="false"
      @close="resetForm('editInfo')"
    >
      <el-form
        :model="editInfo"
        status-icon
        :rules="rules"
        ref="editInfo"
        label-width="200px"
        class="demo-ruleForm"
      >
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="editInfo.userName" autocomplete="off" readonly class="secPass"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="editInfo.name" autocomplete="off" width="10px" class="firPass"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editInfo.email" autocomplete="off" class="secPass"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="editInfo.phone" autocomplete="off" class="secPass"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="editInfo.sex" placeholder="请选择" class="selected">
            <el-option label="男" value="0"></el-option>
            <el-option label="女" value="1"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogEdit = false;resetForm('editInfo')">取 消</el-button>
        <el-button type="primary" @click="editData('editInfo')">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  data() {
    return {
      dialogEdit: false,
      editInfo: {
        userName: "",
        email: "",
        phone: "",
        name: "",
        sex: "",
        userId: "",
        version: ""
      },
      rules: {
        email: [
          {
            required: true,
            pattern: /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/,
            message: "请输入正确的邮箱",
            trigger: "blur"
          }
        ],
        sex: [
          {
            required: true,
            pattern: /^(0|1|2)$/,
            message: "请输入性别",
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
        ],
        sex: [
          {
            required: true,
            pattern: /^(男|女)$/,
            message: "请选择性别",
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
      this.dialogEdit = flag;
      this.$nextTick(() => {
        //修改数据后用此方法可立即获得更新后的数据
        this.$refs.editInfo.resetFields();
        console.log(val);
        this.editInfo.userId = val[0].userId;
        this.editInfo.email = val[0].email;
        this.editInfo.name = val[0].nickName;
        this.editInfo.userName = val[0].userName;
        this.editInfo.phone = val[0].phonenumber;
        this.editInfo.sex = val[0].sex;
        console.log( this.editInfo.sex)
        this.editInfo.version = val[0].version;
      });
    },
    cancelDialog() {
      this.dialogEdit = false;
    },
    editData(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.dialogEdit = false;
          let params = {
            userId: this.editInfo.userId,
            nickName: this.editInfo.name,
            phonenumber: this.editInfo.phone,
            email: this.editInfo.email,
            sex: this.editInfo.sex,
            version: this.editInfo.version
          };

          this.$axios.put("prod-api/backend/user/update", params).then(res => {
            if (200 == res.data.code) {
              this.$message({
                showClose: true,
                message: res.data.msg,
                type: "success"
              });
              this.$parent.getData();
              this.dialogEdit = false;
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