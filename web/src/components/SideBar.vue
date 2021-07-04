<template>
  <div class="sidebar">
    <el-row>
      <el-col :span="12">
        <el-menu
          :default-active="$route.path"
          class="el-menu-vertical-demo"
          @open="handleOpen"
          @close="handleClose"
          background-color="#04294b"
          text-color="rgba(182, 167, 167, 0.596)"
          active-text-color="white"
          router
        >
          <el-menu-item index="/usermanages">
            <i class="el-icon-s-custom"></i>
            <span slot="title">用户管理</span>
          </el-menu-item>

          <el-menu-item index="/customermanages">
            <i class="el-icon-s-home"></i>
            <span slot="title">客户管理</span>
          </el-menu-item>

          <el-submenu index="/boxmanages">
            <template slot="title">
              <i class="el-icon-box"></i>
              <span slot="title">箱子管理</span>
            </template>

            <el-menu-item-group>
              <el-menu-item index="/boxmanages">箱子规格</el-menu-item>
              <el-menu-item index="/boxnumber">箱子信息</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-menu-item index="/ordermanages">
            <i class="el-icon-shopping-cart-1"></i>
            <span slot="title">订单管理</span>
          </el-menu-item>

          <el-menu-item index="/advertmanages">
            <i class="el-icon-s-data"></i>
            <span slot="title">广告管理</span>
          </el-menu-item>

          <el-menu-item index="/viewmanages">
            <i class="el-icon-s-marketing"></i>
            <span slot="title">意见管理</span>
          </el-menu-item>
        </el-menu>
      </el-col>
    </el-row>
  </div>
</template>
<script>
export default {
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    }
  }
};
</script>
<style scoped>
html {
  background-color: #80ace6;
}
.sidebar {
  position: absolute;
  top: 60px;
  left: 0;
}
.el-menu {
  width: 250px;
  height: 760px;
}
.el-menu-item {
  background-color: #04294b;
  color: white;
}
</style>