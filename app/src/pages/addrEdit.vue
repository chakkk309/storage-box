<template>
  <div class="addrmanage">
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="left-text" @click="onClickLeft()">
        <van-icon name="arrow-left" style="position:relative;top:3px" />返回
      </div>
      <div class="title">地址修改</div>
      <div class="more"></div>
    </div>
    <div class="addrDetail">
      <van-address-edit
        :area-list="areaList"
        show-set-default
        show-search-result
        :search-result="searchResult"
        :area-columns-placeholder="['请选择', '请选择', '请选择']"
        @save="onSave"
        @change-detail="onChangeDetail"
        :address-info="addressInfo"
      />
    </div>
  </div>
</template>
<script>
import areaList from "../assets/js/area";
export default {
  data() {
    return {
      addressInfo: {},
      areaList,
      searchResult: [],
      list: [],
      provinceNumber: "",
      cityNumber: "",
      countyNumber: ""
    };
  },
  mounted() {
    this.index();
    this.imfo();
  },
  methods: {
    index() {
      // console.log(this.$route.params.id);
    },
    imfo() {
      this.$axios
        .get(
          "prod-api/app/address/list?" +
            "pageNum=" +
            this.count +
            "&pageSize=10"
        )
        .then(res => {
          if (200 == res.data.code) {
            this.finished = false;
            this.list = res.data.rows;
            for (var i = 0; i < res.data.rows.length; i++) {
              this.list[i].name = res.data.rows[i].contacts;
              this.list[i].tel = res.data.rows[i].contactsPhone;
              this.list[i].id = res.data.rows[i].id;
              this.list[i].areaCode = res.data.rows[i].area;
              if (res.data.rows[i].isDefault == 0) {
                this.list[i].isDefault = true;
              } else if (res.data.rows[i].isDefault == 1) {
                this.list[i].isDefault = false;
              }
            }

            // console.log(res.data.rows.contacts);
          } else {
            this.$toast.error(res.data.msg);
          }
          console.log(this.list);
          for (var i = 0; i < this.list.length; i++) {
            if (this.$route.query.id == this.list[i].id) {
              this.addressInfo = {
                name: this.list[i].name,
                id: this.$route.query.id,
                tel: this.list[i].tel,
                addressDetail: this.list[i].address,
                isDefault: this.list[i].isDefault,
                areaCode: this.list[i].areaCode
              };
            }
          }
        });
    },

    onClickLeft() {
      this.$router.go(-1);
    },
    onSave(row) {
      console.log(row);

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
      if (row.isDefault == true) {
        row.isDefault = 0;
      } else if (row.isDefault == false) {
        row.isDefault = 1;
      }

      let params = {
        id: this.$route.query.id,
        contacts: row.name,
        contactsPhone: row.tel,
        address: row.addressDetail,
        isDefault: row.isDefault,
        provinceName: row.province,
        cityName: row.city,
        areaName: row.county,
        province: this.provinceNumber,
        city: this.cityNumber,
        area: this.countyNumber
      };
      this.$axios.put("prod-api/app/address/update", params).then(res => {
        if (200 == res.data.code) {
          this.$toast.success(res.data.msg);
          this.$router.go(-1);
        } else {
          this.$toast.fail(res.data.msg);
        }
      });
    },
    onChangeDetail(val) {
      if (val) {
        this.searchResult = [{}];
      } else {
        this.searchResult = [];
      }
    }
  }
};
</script>
<style lang="scss" scoped>
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
