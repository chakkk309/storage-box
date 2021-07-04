package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.common.util.ParameterUtil;
import com.ruoyi.project.storage.domain.Address;
import com.ruoyi.project.storage.mapper.AddressMapper;
import com.ruoyi.project.storage.service.IAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 地址Service业务层处理
 *
 * @author yanjiaqi
 * @date 2020.05.09
 */
@Service
@Slf4j
public class AddressServiceImpl implements IAddressService {

    /**
     * 地址Mapper
     */
    private final AddressMapper addressMapper;

    /**
     * 构造方法注入
     *
     * @param addressMapper 地址Mapper
     */
    @Autowired
    public AddressServiceImpl(AddressMapper addressMapper) {
        // 赋值
        this.addressMapper = addressMapper;
    }

    /**
     * 查询地址列表
     *
     * @param address 地址
     * @return 地址集合
     */
    @Override
    public List<Address> selectAddressList(Address address) {
        // 获取用户id
        address.setUserId(SecurityUtils.getUserId());
        // 返回地址列表数据
        return addressMapper.selectAddressList(address);
    }

    /**
     * 新增地址
     *
     * @param address 地址
     * @return 结果
     */
    @Override
    public int insertAddress(Address address) {
        // 判断收货人、手机号、地址，是否为空
        if (address.getContacts() != null && address.getContactsPhone() != null && address.getAddress() != null) {
            // 判断新增地址是否设置为默认地址
            if (address.getIsDefault() == 0) {
                // 修改当前用户的默认地址为非默认
                addressMapper.setAddressIsNotDefault(SecurityUtils.getUserId());
                // 打印当前用户的默认地址id
                log.info("当前默认地址id为" + addressMapper.selectDefaultAddress(SecurityUtils.getUserId()));
                // 打印当前默认标记
                log.info("is_default=" + address.getIsDefault());
            }
            // 设置用户userId
            address.setUserId(SecurityUtils.getUserId());
            // 设置创建基础字段
            ParameterUtil.setCreateEntity(address);
        } else {
            throw new CustomException("当前有信息还未填写，请填写完整");
        }
        // 返回插入条数
        return addressMapper.insertAddress(address);
    }

    /**
     * 修改地址
     *
     * @param address 地址
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAddress(Address address) {
        // 判断默认地址是否为空
        if (addressMapper.selectDefaultAddressDetail(SecurityUtils.getUserId()) != null) {
            // 如果地址要设置为默认地址，且修改的地址id和当前默认地址id不是同一个
            if (address.getIsDefault() == 0 && !address.getId().equals(addressMapper
                    .selectDefaultAddress(SecurityUtils.getUserId()))) {
                // 修改当前用户的默认地址为非默认
                addressMapper.setAddressIsNotDefault(SecurityUtils.getUserId());
                // 设置当前地址设置为默认地址
                addressMapper.setDefaultAddress(address.getId(), SecurityUtils.getUserId());
            }
        }
        // 设置用户userId
        address.setUserId(SecurityUtils.getUserId());
        // 设置更新基础字段
        ParameterUtil.setUpdateEntity(address);
        // 修改条数
        int count = addressMapper.updateAddress(address);
        // 乐观锁判断
        if (count == 0) {
            // 抛出异常标记乐观锁
            log.error("AddressServiceImpl.updateAddress failed：乐观锁");
            // 手动抛异常
            throw new CustomException("地址已被他人率先修改，请刷新后重试", HttpStatus.CONFLICT);
        }
        // 返回修改数据
        return addressMapper.updateAddress(address);
    }

    /**
     * 批量删除地址
     *
     * @param ids 需要删除的地址ID数组
     * @return 结果
     */
    @Override
    public int deleteAddressByIds(Long[] ids) {
        // 获取当前登录用户id
        Long userId = SecurityUtils.getUserId();
        // 返回修改条数
        return addressMapper.deleteAddressByIds(ParameterUtil.getBatchUpdateMapByIds(ids, userId));
    }

    /**
     * 设置默认地址
     *
     * @param id 需要设置为默认地址的地址id
     * @return 修改条数
     */
    @Override
    public int setDefaultAddress(Long id, Long userId) {
        // 修改当前用户的默认地址为非默认
        addressMapper.setAddressIsNotDefault(SecurityUtils.getUserId());
        // 当前用户的默认地址id
        log.info("当前用户的默认地址id是" + addressMapper.selectDefaultAddress(SecurityUtils.getUserId()));
        // 返回修改条数
        return addressMapper.setDefaultAddress(id, SecurityUtils.getUserId());
    }

    /**
     * 获取当前用户默认收货地址信息
     *
     * @param userId 当前用户的id
     * @return 修改条数
     */
    @Override
    public Address getDefaultAddress(Long userId) {
        // 返回修改条数
        return addressMapper.getDefaultAddress(userId);
    }

//    /**
//     * 校验联系人是否唯一
//     *
//     * @param address 地址
//     * @return 结果
//     */
//    @Override
//    public String checkContactsUnique(Address address) {
//        // 判断用户id是否为空
//        Long userId = StringUtils.isNull(address.getUserId()) ? -1L : address.getUserId();
//        // 获取联系人姓名
//        Address contact = addressMapper.checkContactsUnique(address.getContacts());
//        // 判断用联系人姓名是否为空且用户id是否已经存在
//        if (StringUtils.isNotNull(contact) && contact.getUserId().longValue() != userId.longValue()) {
//            // 返回不唯一标识
//            return UserConstants.NOT_UNIQUE;
//        }
//        // 返回唯一标识
//        return UserConstants.UNIQUE;
//    }
//
//    /**
//     * 判断手机号是否唯一
//     *
//     * @param address 地址
//     * @return 结果
//     */
//    @Override
//    public String checkContactsPhoneUnique(Address address) {
//        // 判断用户id是否为空
//        Long userId = StringUtils.isNull(address.getUserId()) ? -1L : address.getUserId();
//        // 获取联系人手机号码
//        Address phone = addressMapper.checkContactsPhoneUnique(address.getContactsPhone());
//        // 判断联系人手机号码是否为空且用户id是否已经存在
//        if (StringUtils.isNotNull(phone) && phone.getUserId().longValue() != userId.longValue()) {
//            // 返回不唯一标识
//            return UserConstants.NOT_UNIQUE;
//        }
//        // 返回唯一标识
//        return UserConstants.UNIQUE;
//    }
//
//    /**
//     * 判断地址是否唯一
//     *
//     * @param address 地址
//     * @return 结果
//     */
//    @Override
//    public String checkAddressUnique(Address address) {
//        // 判断用户id是否为空
//        Long userId = StringUtils.isNull(address.getUserId()) ? -1L : address.getUserId();
//        // 获取收货地址号码
//        Address detailAddress = addressMapper.checkAddressUnique(address.getAddress());
//        // 判断收货地址是否为空且用户id是否已经存在
//        if (StringUtils.isNotNull(detailAddress) && detailAddress.getUserId().longValue() != userId.longValue()) {
//            // 返回不唯一标识
//            return UserConstants.NOT_UNIQUE;
//        }
//        // 返回唯一标识
//        return UserConstants.UNIQUE;
//    }

}
