package com.coupon.manager.service;

import com.coupon.manager.domain.Coupon;
import com.coupon.manager.dto.CouponDTO;
import com.coupon.manager.exception.ResourceNotFoundException;
import com.coupon.manager.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.coupon.manager.domain.enumeration.CouponStatusEnum.ACTIVE;
import static com.coupon.manager.domain.enumeration.CouponStatusEnum.DELETED;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository repository;

    @InjectMocks
    private CouponService service;

    @Test
    void shouldReturnCouponDTOWhenFindById() {
        UUID id = UUID.randomUUID();

        Coupon coupon = new Coupon(
                "ABC123",
                "Test Coupon",
                Instant.now().plusSeconds(3600).toString(),
                10.0,
                ACTIVE,
                true,
                false
        );

        coupon.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(coupon));

        CouponDTO result = service.findById(id);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(id, result.getId()),
                () -> assertEquals("ABC123", result.getCode())
        );

        verify(repository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenCouponNotFound() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(id)
        );

        assertTrue(exception.getMessage().contains("Coupon not found"));

        verify(repository).findById(id);
    }

    @Test
    void shouldInsertCouponSuccessfully() {
        CouponDTO dto = new CouponDTO();
        dto.setCode("ABC123");
        dto.setDescription("Test Coupon");
        dto.setExpirationDate(Instant.now().plusSeconds(3600).toString());
        dto.setDiscountValue(10.0);
        dto.setPublished(true);

        when(repository.save(any(Coupon.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CouponDTO result = service.insert(dto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("ABC123", result.getCode()),
                () -> assertEquals(10.0, result.getDiscountValue())
        );

        verify(repository).save(any(Coupon.class));
    }

    @Test
    void shouldDeleteCouponSuccessfully() {
        UUID id = UUID.randomUUID();

        Coupon coupon = new Coupon(
                "ABC123",
                "Test Coupon",
                Instant.now().plusSeconds(3600).toString(),
                10.0,
                ACTIVE,
                true,
                false
        );

        coupon.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(coupon));
        when(repository.save(any(Coupon.class))).thenReturn(coupon);

        service.delete(id);

        assertEquals(DELETED, coupon.getStatus());

        verify(repository).findById(id);
        verify(repository).save(coupon);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingCoupon() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.delete(id)
        );

        assertTrue(exception.getMessage().contains("Coupon not found"));

        verify(repository).findById(id);
        verify(repository, never()).save(any());
    }
}