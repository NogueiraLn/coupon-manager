package com.coupon.manager.service;

import com.coupon.manager.domain.Coupon;
import com.coupon.manager.dto.CouponDTO;
import com.coupon.manager.exception.ResourceNotFoundException;
import com.coupon.manager.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class CouponService {

    @Autowired
    private CouponRepository repository;

    @Transactional(readOnly = true)
    public CouponDTO findById(UUID id) {
        Optional<Coupon> entity = repository.findById(id);
        Coupon coupon = entity.orElseThrow(() -> new ResourceNotFoundException("Coupon not found for id: " + id));
        return new CouponDTO(coupon);
    }

    @Transactional
    public CouponDTO insert(CouponDTO dto) {
        Coupon coupon =
                Coupon.create(dto.getCode(), dto.getDescription(), dto.getExpirationDate(), dto.getDiscountValue(), dto.isPublished());

        return new CouponDTO(repository.save(coupon));
    }

    @Transactional
    public void delete (UUID id) {
        Coupon coupon = repository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Coupon not found for id: " + id));

        coupon.delete();
        repository.save(coupon);

    }

}
