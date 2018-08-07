package com.alx.weixin.wxsell.service.impl;

import com.alx.weixin.wxsell.dateobject.ProductInfo;
import com.alx.weixin.wxsell.dto.CartDto;
import com.alx.weixin.wxsell.exception.SellException;
import com.alx.weixin.wxsell.repository.ProductInfoRepository;
import com.alx.weixin.wxsell.service.ProductInfoService;
import com.alx.weixin.wxsell.util.ExceptionEnum;
import com.alx.weixin.wxsell.util.ProductStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @描述
 * @创建人 zhaoxny
 * @创建时间 2018/7/30
 */
@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    public ProductInfo getOne(String productInfoId) {
        return productInfoRepository.getOne(productInfoId);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoRepository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return productInfoRepository.save(productInfo);
    }

    @Override
    public void increaseStock(List<CartDto> cartDtoList) {
//        productInfoRepository
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDto> cartDtoList) {
        for(CartDto cartDto: cartDtoList){
            ProductInfo one = productInfoRepository.getOne(cartDto.getProductId());
            if(one == null){
                throw new SellException(ExceptionEnum.ERROR_PRODUCT_NOT_EXIST);
            }
            int num = one.getProductStock() - cartDto.getProductQuantity();
            if(num < 0){
                throw new SellException(ExceptionEnum.ERROR_PRODUCT_STOCK);
            }
            one.setProductStock(num);
            productInfoRepository.save(one);
        }
    }
}
