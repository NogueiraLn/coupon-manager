package com.coupon.manager.dto;

import com.coupon.manager.domain.Coupon;
import com.coupon.manager.domain.enumeration.CouponStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CouponDTO {
    private UUID id;

    @Size(min = 6, message = "Field code must have at least 6 alphanumeric characters")
    @NotBlank(message = "Field code required")
    private String code;

    @NotBlank(message = "Field description required")
    private String description;

    @NotBlank(message = "Field expiration date required")
    private String expirationDate;

    @Positive(message = "Discount Value must be positive")
    @NotBlank(message = "Field discount value required")
    private double discountValue;

    private CouponStatusEnum status;

    private Boolean published;
    private Boolean redeemed;

    public CouponDTO (Coupon entity) {
        this.id = entity.getId();
        this.code = entity.getCode();
        this.description = entity.getExpirationDate();
        this.discountValue = entity.getDiscountValue();
        this.status = entity.getStatus();
        this.published = entity.isPublished();
        this.redeemed = entity.isRedeemed();
    }

}
