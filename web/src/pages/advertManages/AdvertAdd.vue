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
        <el-form-item label="广告标题" prop="title">
          <el-input v-model="addInfo.title" autocomplete="off" width="10px" class="firPass"></el-input>
        </el-form-item>
        <el-form-item label="广告内容" prop="content">
          <el-input v-model="addInfo.content" autocomplete="off" class="secPass"></el-input>
        </el-form-item>
        <el-form-item label="奖励积分" prop="points">
          <el-input v-model="addInfo.points" autocomplete="off" class="secPass"></el-input>
        </el-form-item>
        <el-form-item label="图片地址" prop="imgUrl">
          <el-upload
            ref="uploads"
            :action="upLoadUrl"
            list-type="picture-card"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :file-list="fileList"
            :before-upload="beforeUpload"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="dialogImageUrl" alt />
          </el-dialog>
        </el-form-item>
        <el-form-item label="广告排序" prop="sortNo">
          <el-input type="number" v-model="addInfo.sortNo" autocomplete="off" class="secPass"></el-input>
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
      upLoadUrl: "prod-api/common/upload",
      dialogImageUrl: "",
      dialogVisible: false,
      dialogadd: false,
      fileList: [],
      dialogImg: "",
      addInfo: {
        title: "",
        content: "",
        sortNo: "",
        points: "",
        imgUrl: ""
      },
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
            // pattern: /<img.*?(?:>|\/>)/gi,
            message: "请上传正确的图片",
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
      // this.$refs.uploads.clearFiles();
    },
    openDialog(flag, val) {
      this.dialogadd = flag;
      this.$nextTick(() => {
        //修改数据后用此方法可立即获得更新后的数据
        this.$refs.addInfo.resetFields();
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
            setTimeout(() => this.$refs.uploads.clearFiles(), 3000);
          }
        });
      }
    },

    // 新增数据
    addData(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          console.log(this.dialogImageUrl);

          let pamars = {
            title: this.addInfo.title,
            content: this.addInfo.content,
            imgUrl: this.dialogImageUrl,
            points: this.addInfo.points,
            sortNo: this.addInfo.sortNo
          };
          this.$axios
            .post("prod-api/backend/advertisement/create", pamars)
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
          return false;
        }
      });
    },

    // 文件列表移出时的钩子
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    // 点击文件列表中已上传的文件时的钩子
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