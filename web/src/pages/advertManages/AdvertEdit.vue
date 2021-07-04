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
        <el-form-item label="广告标题" prop="title">
          <el-input v-model="editInfo.title" autocomplete="off" width="10px" class="firPass"></el-input>
        </el-form-item>
        <el-form-item label="广告内容" prop="content">
          <el-input v-model="editInfo.content" autocomplete="off" class="secPass"></el-input>
        </el-form-item>
        <el-form-item label="奖励积分" prop="points">
          <el-input v-model="editInfo.points" autocomplete="off" class="secPass"></el-input>
        </el-form-item>
        <el-form-item label="原地址">
          <el-image style="width: 100px; height: 100px" :src="beforeImgUrl"></el-image>
        </el-form-item>
        <el-form-item label="图片地址" prop="imgUrl">
          <el-upload
            ref="uploads"
            :action="upLoadUrl"
            list-type="picture-card"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :before-upload="beforeUpload"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="dialogImageUrl" alt />
          </el-dialog>
        </el-form-item>
        <el-form-item label="排序" prop="sortNo">
          <el-input type="number" v-model="editInfo.sortNo" autocomplete="off" class="secPass"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogEdit = false;resetForm('editInfo')">取 消</el-button>
        <el-button type="primary" @click="addData('editInfo')">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  data() {
    return {
      upLoadUrl: "prod-api/common/upload",
      uploads: "",
      dialogImageUrl: "",
      dialogVisible: false,
      dialogEdit: false,
      editInfo: {
        title: "",
        content: "",
        sortNo: "",
        points: "",
        imgUrl: "",
        id: "",
        version: ""
      },
      beforeImgUrl: "",
      rules: {
        title: [
          {
            required: true,
            trigger: "blur",
            message: "请正确的标题",
            trigger: "blur"
          }
        ],
        content: [
          {
            required: true,
            message: "请输入广告内容",
            trigger: "blur"
          }
        ],
        points: [
          {
            required: true,
            message: "请输入奖励积分",
            trigger: "blur"
          }
        ],
        imgUrl: [
          {
            message: "请上传图片",
            trigger: "blur"
          }
        ],
        sortNo: [
          {
            required: true,

            message: "请输入排序",
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
      this.beforeImgUrl = val[0].imgUrl;
      console.log(this.beforeImgUrl);
      this.$nextTick(() => {
        //修改数据后用此方法可立即获得更新后的数据
        console.log(val);

        this.$refs.editInfo.resetFields();
        (this.editInfo.id = val[0].id),
          (this.editInfo.title = val[0].title),
          (this.editInfo.content = val[0].content),
          (this.editInfo.points = val[0].points),
          (this.editInfo.imgUrl = val[0].imgUrl),
          (this.editInfo.sortNo = val[0].sortNo),
          (this.editInfo.version = val[0].version);
      });
    },
    // 上传之前校验图片
    beforeUpload(file) {
      // console.log(file);
      var testmsg = file.name.substring(file.name.lastIndexOf(".") + 1);
      var imgSize = file.size / 1024 / 1024;
      console.log(testmsg);
      if (testmsg != "png" && testmsg != "jpg") {
        this.$message({
          message: "只能上传jpg、png格式",
          type: "error"
        });

        // 延时器，清空
        setTimeout(() => this.$refs.uploads.clearFiles(), 1000);
      } else if (testmsg < 10) {
        this.$message({
          message: "上传文件不能超过10M",
          type: "error"
        });
        setTimeout(() => this.$refs.uploads.clearFiles(), 1000);
      } else {
        let fd = new FormData();
        fd.append("file", file); //传文件
        console.log(fd);
        this.$axios({
          method: "post",
          url: this.upLoadUrl,
          data: fd
        }).then(res => {
          console.log(res);
          if (res.data.code == 200) {
            this.dialogImageUrl = res.data.fileName;
            this.$message({
              showClose: true,
              message: res.data.msg,
              type: "success"
            });
          }
        });
      }
    },
    addData(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          let params = {
            id: this.editInfo.id,
            title: this.editInfo.title,
            content: this.editInfo.content,
            points: this.editInfo.points,
            imgUrl: this.dialogImageUrl,
            sortNo: this.editInfo.sortNo,
            version: this.editInfo.version
          };
          this.$axios
            .put("prod-api/backend/advertisement/update", params)
            .then(res => {
              if (200 == res.data.code) {
                this.dialogEdit = false;
                this.$parent.getData();
                this.$message({
                  showClose: true,
                  message: res.data.msg,
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
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    }
  }
};
</script>
<style lang="scss" scoped>
.demo-ruleForm {
  // width: 500px;
  position: relative;
  left: -70px;
}
/deep/ .el-input__inner {
  width: 200px;
}
</style>