package com.micheladas.chelas.entity;

import com.micheladas.chelas.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Entity
@Table(name = "bigBottle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BigBottle extends BaseEntity {

    @NotEmpty(message = "{product.brand.required}")
    @Column(name = "brand", nullable = false)
    private String brand;

    @NotNull(message = "{product.price.required}")
    @Positive(message = "{validation.positive}")
    @Column(name = "unit_price", precision = 12, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @NotNull(message = "{product.quantity.required}")
    @Positive(message = "{validation.positive}")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    /**
     * UPDATES THE CURRENT ENTITY'S FIELDS WITH DATA FROM ANOTHER BIGBOTTLE INSTANCE.
     * USED BY THE GENERIC CONTROLLER TO DETECT CHANGES BEFORE PERSISTING.
     * * @PARAM NEWDATA THE SOURCE OBJECT CONTAINING UPDATED VALUES.
     * RETURN TRUE IF AT LEAST ONE FIELD WAS MODIFIED, FALSE OTHERWISE.
     */

    public boolean updateFrom(BigBottle newData) {
        if (newData == null) return false;

        boolean hasChanges = false;

        if (!Objects.equals(this.brand, newData.getBrand())) {
            this.brand = newData.getBrand();
            hasChanges = true;
        }

        if (newData.getUnitPrice() != null &&
                (this.unitPrice == null || this.unitPrice.compareTo(newData.getUnitPrice()) != 0)) {
            this.unitPrice = newData.getUnitPrice();
            hasChanges = true;
        }

        if (newData.getQuantity() != null && !Objects.equals(this.quantity, newData.getQuantity())) {
            this.quantity = newData.getQuantity();
            hasChanges = true;
        }

        return hasChanges;
    }

    /**
     * AUTOMATICALLY CALCULATES THE TOTAL_AMOUNT BEFORE SAVING OR UPDATING THE RECORD.
     * CALCULATION: UNITPRICE * QUANTITY (ROUNDED TO 2 DECIMAL PLACES).
     */

    @PrePersist
    @PreUpdate
    private void recalculateTotal() {
        if (this.unitPrice != null && this.quantity != null) {
            this.totalAmount = this.unitPrice
                    .multiply(BigDecimal.valueOf(this.quantity))
                    .setScale(2, RoundingMode.HALF_UP);
        }
    }

}