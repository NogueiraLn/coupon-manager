package com.coupon.manager.repository;

import com.coupon.manager.domain.Coupon;
import com.coupon.manager.domain.enumeration.CouponStatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CouponRepositoryTest {

    @Autowired
    private CouponRepository repository;

    @Test
    void shouldSaveAndFindCouponById() {
        Coupon coupon = new Coupon(
                "ABC123",
                "Test Coupon",
                Instant.now().plusSeconds(1000).toString(),
                0.8,
                CouponStatusEnum.ACTIVE,
                true,
                false
        );

        Coupon saved = repository.save(coupon);

        Optional<Coupon> result = repository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("ABC123", result.get().getCode());
    }

    @Test
    void shouldNotAllowDuplicatedCode() {
        Coupon coupon1 = new Coupon(
                "ABC123",
                "Test Coupon 1",
                Instant.now().plusSeconds(1000).toString(),
                0.8,
                CouponStatusEnum.ACTIVE,
                true,
                false
        );

        Coupon coupon2 = new Coupon(
                "ABC123",
                "Test Coupon 2",
                Instant.now().plusSeconds(1000).toString(),
                0.9,
                CouponStatusEnum.ACTIVE,
                true,
                false
        );

        repository.save(coupon1);

        assertThrows(Exception.class, () -> {
            repository.saveAndFlush(coupon2);
        });
    }
}