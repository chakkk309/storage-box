package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.Address;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 地址Mapper接口
 *
 * @author yanjiaqi
 * @date 2020.05.09
 */
public interface AddressMapper {

    /**
     * 查询地址列表
     *
     * @param address address 地址
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
     * @param map ids：需要删除的地址id
     *            user：操作人
     *            time：操作时间
     *            userId 当前用户id
     * @return 修改条数
     */
    int deleteAddressByIds(Map<String, Object> map);

    /**
     * 设置默认地址
     *
     * @param id     被设置为默认地址的地址id
     * @param userId 当前用户id
     * @return 修改条数
     */
    int setDefaultAddress(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 设置默认地址为非默认
     *
     * @param userId 当前用户id
     * @return 修改条数
     */
    int setAddressIsNotDefault(Long userId);

    /**
     * 获取当前用户默认收货地址
     *
     * @param userId 当前用户id
     * @return 修改条数
     */
    Address getDefaultAddress(Long userId);

    /**
     * 获取当前用户默认地址的详细id
     *
     * @param userId 当前用户id
     * @return 地址id
     */
    Long selectDefaultAddress(Long userId);

    /**
     * 获取当前用户默认地址的详细信息
     *
     * @param userId 当前用户id
     * @return 详细地址
     */
    String selectDefaultAddressDetail(Long userId);

//    /**
//     * 校验联系人是否唯一（校验时可使用）
//     *
//     * @param contact 联系人
//     * @return 地址
//     */
//    Address checkContactsUnique(String contact);
//
//    /**
//     * 校验联系人电话是否唯一（校验时可使用）
//     *
//     * @param contactPhone 联系电话
//     * @return 地址
//     */
//    Address checkContactsPhoneUnique(String contactPhone);
//
//    /**
//     * 校验地址是否唯一（校验时可使用）
//     *
//     * @param address 地址
//     * @return 地址
//     */
//    Address checkAddressUnique(String address);

}


