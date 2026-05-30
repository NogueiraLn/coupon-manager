package com.coupon.manager.domain;

import com.coupon.manager.domain.enumeration.CouponStatusEnum;
import com.coupon.manager.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

import static com.coupon.manager.domain.enumeration.CouponStatusEnum.DELETED;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_coupon")
public class Coupon {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String code;

    private String description;

    private String expirationDate;
    private double discountValue;

    @Enumerated(EnumType.STRING)
    private CouponStatusEnum status;

    private boolean published;
    private boolean redeemed;

    public Coupon(String code, String description, String expirationDate, double discountValue, CouponStatusEnum status, boolean published, boolean redeemed) {
        this.id = UUID.randomUUID();
        this.code = code;
        this.description = description;
        this.expirationDate = expirationDate;
        this.discountValue = discountValue;
        this.status = status;
        this.published = published;
        this.redeemed = redeemed;
    }

    public static Coupon create(String code,
                                String description,
                                String expirationDate,
                                double discountValue,
                                boolean published) {
        validateExpirationDate(expirationDate);
        validateDiscountValue(discountValue);
        code = sanitizeCode(code);
        return new Coupon(code, description, expirationDate, discountValue, CouponStatusEnum.ACTIVE, published, false);
    }

    public void delete() {
        if (this.status.equals(DELETED)) {
            throw new BusinessException("Coupon already deleted");
        }
        this.status = DELETED;
    }

    private static void validateExpirationDate(String expirationDate) {
        if (Instant.parse(expirationDate).isBefore(Instant.now()))
            throw new BusinessException("Expiration date cannot be in the past");
    }

    private static void validateDiscountValue(double discountValue) {
        if (discountValue < 0.5)
            throw new BusinessException("Discount Value cannot be less then 5%");
    }

    private static String sanitizeCode(String code) {
        String sanitizedCode = code.replaceAll("[^A-Za-z0-9]", "");

        if (sanitizedCode.length() != 6)
            throw new BusinessException("Coupon Code must have at least 6 alphanumeric characters");

        return sanitizedCode.toUpperCase();
    }


    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Coupon coupon)) return false;
        return id.equals(coupon.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
