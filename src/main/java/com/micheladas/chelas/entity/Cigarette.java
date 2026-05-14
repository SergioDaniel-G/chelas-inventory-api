package com.micheladas.chelas.entity;

import com.micheladas.chelas.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "cigarettes")
@NoArgsConstructor
@AllArgsConstructor
public class Cigarette extends BaseEntity {

	@NotBlank(message = "{product.brand.required}")
	@Column(name = "brand", nullable = false)
	private String brand;

	@NotNull(message = "{product.price.required}")
	@Positive(message = "{validation.positive}")
	@Column(name = "unit_price", precision = 12, scale = 2)
	private BigDecimal unitPrice;

	@NotNull(message = "{product.quantity.required}")
	@Positive(message = "{validation.positive}")
	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "total_amount", precision = 12, scale = 2)
	private BigDecimal totalAmount;

	/**
	 * COMPARES THE CURRENT ENTITY WITH NEW DATA TO DETECT CHANGES.
	 * IF CHANGES ARE FOUND, IT UPDATES THE LOCAL FIELDS AND RETURNS TRUE.
	 * PARAM NEWDATA THE OBJECT CONTAINING POTENTIALLY UPDATED INFORMATION.
	 * RETURN TRUE IF THE ENTITY WAS MODIFIED; FALSE IF ALL RELEVANT FIELDS REMAIN THE SAME.
	 */

	public boolean updateFrom(Cigarette newData) {
		boolean sameBrand = Objects.equals(this.brand, newData.getBrand());
		boolean samequantity = Objects.equals(this.quantity, newData.getQuantity());
		boolean samePrice = (this.unitPrice != null && newData.getUnitPrice() != null)
				? this.unitPrice.compareTo(newData.getUnitPrice()) == 0
				: Objects.equals(this.unitPrice, newData.getUnitPrice());

		if (sameBrand && samePrice && samequantity) {
			return false;
		}

		this.brand = newData.getBrand();
		this.unitPrice = newData.getUnitPrice();
		this.quantity = newData.getQuantity();

		return true;
	}

	/**
	 * LIFECYCLE CALLBACK TO AUTOMATICALLY CALCULATE THE TOTAL INVENTORY VALUE
	 * (PRICE * QUANTITY) BEFORE PERSISTING OR UPDATING THE RECORD IN THE DATABASE.
	 */

	@PrePersist
	@PreUpdate
	public void recalculateTotal() {
		if (this.unitPrice != null && this.quantity != null) {
			this.totalAmount = this.unitPrice.multiply(new BigDecimal(this.quantity))
					.setScale(2, RoundingMode.HALF_UP);
		}
	}
}