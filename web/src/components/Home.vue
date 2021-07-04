<template>
  <div class="wrapper">
    <v-sidebar></v-sidebar>
    <div class="content-box">
      <v-head></v-head>
      <div class="app-wrap">
        <div class="content">
          <router-view></router-view>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import vHead from "./Header.vue";
import vSidebar from "./SideBar.vue";
export default {
  name: "home",
  components: { vHead, vSidebar }
};
</script>
<style lang="scss" scoped>
.content {
  position: absolute;

  width: 1280px;
  height: 700px;
  left: 250px;
  top: 60px;
}
</style>