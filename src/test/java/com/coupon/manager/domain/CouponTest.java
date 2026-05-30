package com.coupon.manager.domain;

import com.coupon.manager.domain.enumeration.CouponStatusEnum;
import com.coupon.manager.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.coupon.manager.domain.enumeration.CouponStatusEnum.ACTIVE;
import static com.coupon.manager.domain.enumeration.CouponStatusEnum.DELETED;
import static com.coupon.manager.domain.enumeration.CouponStatusEnum.INACTIVE;
import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    private String futureExpirationDate;
    private String pastExpirationDate;

    @BeforeEach
    void setUp() throws Exception {
        futureExpirationDate = Instant.now().plusSeconds(1000).toString();
        pastExpirationDate = Instant.now().minusSeconds(100).toString();
    }

    @Test
    void shouldCreateCouponSuccessfully() {

        Coupon coupon = Coupon.create(
                "abc123",
                "Coupon Test",
                futureExpirationDate,
                10.0,
                true
        );

        assertAll(
                () -> assertNotNull(coupon.getId()),
                () -> assertEquals("ABC123", coupon.getCode()),
                () -> assertEquals("Coupon Test", coupon.getDescription()),
                () -> assertEquals(futureExpirationDate, coupon.getExpirationDate()),
                () -> assertEquals(10.0, coupon.getDiscountValue()),
                () -> assertEquals(ACTIVE, coupon.getStatus()),
                () -> assertTrue(coupon.isPublished()),
                () -> assertFalse(coupon.isRedeemed())
        );
    }

    @Test
    void shouldThrowExceptionWhenExpirationDateIsInPast() {

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> Coupon.create(
                        "ABC123",
                        "Coupon Test",
                        pastExpirationDate,
                        10.0,
                        true
                )
        );

        assertEquals("Expiration date cannot be in the past", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDiscountValueIsLessThanMinimum() {
        String expirationDate = Instant.now().plusSeconds(3600).toString();

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> Coupon.create(
                        "ABC123",
                        "Coupon Test",
                        expirationDate,
                        0.4,
                        true
                )
        );

        assertEquals("Discount Value cannot be less then 5%", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCouponCodeHasLessThanSixAlphanumericCharacters() {

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> Coupon.create(
                        "ABC12",
                        "Coupon Test",
                        futureExpirationDate,
                        0.8,
                        true
                )
        );

        assertEquals("Coupon Code must have at least 6 alphanumeric characters", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCouponCodeContainsSpecialCharactersAndResultHasLessThanSixAlphanumericCharacters() {

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> Coupon.create(
                        "AB@#12",
                        "Coupon Test",
                        futureExpirationDate,
                        0.8,
                        true
                )
        );

        assertEquals("Coupon Code must have at least 6 alphanumeric characters", exception.getMessage());
    }

    @Test
    void shouldSanitizeAndUppercaseCouponCode() {

        Coupon coupon = Coupon.create(
                "ab-c1@2!.3",
                "Coupon Test",
                futureExpirationDate,
                0.8,
                true
        );

        assertEquals("ABC123", coupon.getCode());
    }

    @Test
    void shouldDeleteCouponSuccessfully() {
        Coupon coupon = new Coupon(
                "ABC123",
                "Coupon Test",
                futureExpirationDate,
                0.8,
                ACTIVE,
                true,
                false
        );

        coupon.delete();

        assertEquals(DELETED, coupon.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenDeletingAlreadyDeletedCoupon() {
        Coupon coupon = new Coupon(
                "ABC123",
                "Coupon Test",
                futureExpirationDate,
                0.8,
                DELETED,
                true,
                false
        );

        BusinessException exception = assertThrows(BusinessException.class, coupon::delete);

        assertEquals("Coupon already deleted", exception.getMessage());
    }

    @Test
    void shouldReturnInactiveStatusWhenCouponIsExpired() {
        Coupon coupon = new Coupon(
                "ABC123",
                "Coupon Test",
                pastExpirationDate,
                10.0,
                ACTIVE,
                true,
                false
        );

        CouponStatusEnum status = coupon.getStatus();

        assertEquals(INACTIVE, status);
    }

    @Test
    void shouldKeepDeletedStatusWhenCouponIsExpired() {
        Coupon coupon = new Coupon(
                "ABC123",
                "Coupon Test",
                pastExpirationDate,
                10.0,
                DELETED,
                true,
                false
        );

        assertEquals(DELETED, coupon.getStatus());
    }

}