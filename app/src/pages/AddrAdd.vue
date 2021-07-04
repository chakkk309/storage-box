<template>
  <div class="addrmanage">
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="left-text" @click="onClickLeft()">
        <van-icon name="arrow-left" style="position:relative;top:3px" />返回
      </div>
      <div class="title">地址新增</div>
      <div class="more"></div>
    </div>
    <div class="addrDetail">
      <van-address-edit
        :area-list="areaList"
        show-postal
        show-set-default
        show-search-result
        :search-result="searchResult"
        :area-columns-placeholder="['请选择', '请选择', '请选择']"
        @save="onSave"
      />
    </div>
  </div>
</template>
<script>
import areaList from "../assets/js/area";

export default {
  data() {
    return {
      areaList,
      searchResult: [],
      provinceNumber: "",
      cityNumber: "",
      countyNumber: ""
    };
  },
  mounted() {
    this.areaNum();
  },
  methods: {
    // 地区编号
    areaNum() {
      console.log(areaList.province_list);
      // console.log(
      //   Object.entries(areaList.city_list)
      //     .filter(e => e[1] === "巴中市")
      //     .map(e => e[0])
      // );
    },

    onClickLeft() {
      this.$router.go(-1);
    },
    onSave(row) {
      // 地址对应编号
      // 城市对应编号

      for (var key in areaList.city_list) {
        if (areaList.city_list[key] == row.city) {
          this.cityNumber = Object.entries(areaList.city_list)
            .filter(e => e[1] === areaList.city_list[key])
            .map(e => e[0])[0];
        }
      }

      // 省份对应编号
      for (var c in areaList.province_list) {
        if (areaList.province_list[c] == row.province) {
          this.provinceNumber = Object.entries(areaList.province_list)
            .filter(e => e[1] === areaList.province_list[c])
            .map(e => e[0])[0];
        }
      }

      //区县对应编号
      for (var b in areaList.county_list) {
        if (areaList.county_list[b] == row.county) {
          this.countyNumber = Object.entries(areaList.county_list)
            .filter(e => e[1] === areaList.county_list[b])
            .map(e => e[0])[0];
        }
      }

      console.log(row);
      if (row.isDefault == true) {
        row.isDefault = 0;
      } else if (row.isDefault == false) {
        row.isDefault = 1;
      }
      let params = {
        contacts: row.name,
        contactsPhone: row.tel,
        provinceName: row.province,
        cityName: row.city,
        areaName: row.county,
        province: this.provinceNumber,
        city: this.cityNumber,
        area: this.countyNumber,
        address: row.addressDetail,
        isDefault: row.isDefault
      };
      this.$axios.post("prod-api/app/address/create", params).then(res => {
        if (200 == res.data.code) {
          this.$toast.success(res.data.msg);
          this.$router.go(-1);
        } else {
          this.$toast.error(res.data.msg);
        }
      });
    },
    onChangeDetail(val) {
      if (val) {
        this.searchResult = [
          {
            // address: "某小区"
          }
        ];
      } else {
        this.searchResult = [];
      }
    }
  }
};
</script>
<style scoped lang="scss">
.navbar {
  width: 100%;
  height: 46px;
  background: url("../assets/bg2.png");
  position: fixed;
  display: flex;
  justify-content: space-between;
  color: white;
  z-index: 1;
  font-size: 15px;
  line-height: 46px;
  position: relative;
  top: 0;
}
.left-text {
  padding: 10px;
  position: relative;
  bottom: 8px;
}
.title {
  // padding: 10px;
  padding-right: 30px;
}
.more {
  padding: 10px;
}
/deep/ .van-button--danger {
  color: #fff;
  background-color: #1989fa;
  border: 1px solid #1989fa;
}
</style>
