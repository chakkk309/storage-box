package com.ruoyi.project.storage.service;

import com.ruoyi.project.storage.domain.Address;

import java.util.List;

/**
 * 地址Service接口
 *
 * @author yanjiaqi
 * @date 2020.05.09
 */
public interface IAddressService {

    /**
     * 查询地址列表
     *
     * @param address 地址
     * @return 地址集合
     */
    List<Address> selectAddressList(Address address);

    /**
     * 新增地址
     *
     * @param address 地址
     * @return 修改条数
     */
    int insertAddress(Address address);

    /**
     * 修改地址
     *
     * @param address 地址
     * @return 修改条数
     */
    int updateAddress(Address address);

    /**
     * 批量删除地址
     *
     * @param ids 需要删除的地址ID数组
     * @return 修改条数
     */
    int deleteAddressByIds(Long[] ids);

    /**
     * 设置默认地址
     *
     * @param id     需要设置为默认地址的地址id
     * @param userId 当前用户id
     * @return 修改条数
     */
    int setDefaultAddress(Long id, Long userId);

    /**
     * 获取当前用户默认收货地址
     *
     * @param userId 当前用户的id
     * @return 修改条数
     */
    Address getDefaultAddress(Long userId);

//    /**
//     * 判断收货人是否唯一（校验时可使用）
//     *
//     * @param address 地址
//     * @return 修改条数
//     */
//    String checkContactsUnique(Address address);
//
//    /**
//     * 判断手机号是否唯一（校验时可使用）
//     *
//     * @param address 地址
//     * @return 修改条数
//     */
//    String checkContactsPhoneUnique(Address address);
//
//    /**
//     * 判断地址是否唯一（校验时可使用）
//     *
//     * @param address 地址
//     * @return 修改条数
//     */
//    String checkAddressUnique(Address address);

}
