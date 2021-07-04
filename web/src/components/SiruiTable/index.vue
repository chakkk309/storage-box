<template>
  <div class="sirui-table">
    <el-table
      :data="data"
      ref="mainTable"
      v-loading="loading"
      highlight-current-row
      @selection-change="handleSelectionChange"
      stripe
      :maxHeight="tableHeight"
      @row-click="clickRow"
      :default-sort="defaultSort"
    >
      <slot></slot>
    </el-table>
    <div class="block">
      <el-pagination
        ref="page"
        @size-change="handleSizeChange"
        @current-change="handlePagination"
        :page-count="page.pages"
        :current-page="page.pageNum || 1"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="page.pageSize"
        :total="page.total"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>
    </div>
  </div>
</template>
<script>
export default {
  name: "SiruiTable",
  props: {
    defaultSort: {
      type: Object,
      default: () => {}
    },
    //table数据
    data: {
      type: Array,
      default: () => []
    },
    //固定表头的高度
    height: {
      type: String,
      default: null
    },
    //初始化table的高度
    tableHeight: {
      type: Number,
      default: document.body.scrollHeight - 260
    },

    page: {
      type: Object,
      default: {},
      required: true
    },
    //是否添加loading效果
    loading: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    //抛出选择每页条数事件
    handleSizeChange(val) {
      this.$emit("handleSizeChange", val);
    },
    //抛出表格选中事件
    handleSelectionChange(val) {
      this.$emit("handleSelectionChange", val);
    },
    //抛出当前页数跳转事件
    handlePagination(val) {
      this.$emit("handleCurrentChange", val);
    },
    //选中一行事件
    clickRow(val) {
      this.$refs.mainTable.toggleRowSelection(val);
    }
    // //点击下一页
    // nextClick(val) {
    //   this.$emit("nextClick", val);
    // },
    // //点击上一页
    // prevClick(val) {
    //   this.$emit("prevClick", val);
    // }
  }
};
</script>
<style lang="scss">
.el-pagination {
  text-align: right;
  padding-top: 10px;
  margin-bottom: 60px;
}
</style>
