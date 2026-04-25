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


@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Expense extends BaseEntity {

	@NotBlank(message = "{validation.required}")
	@Column(name = "item_name")
	private String itemName;

	@NotNull(message = "{product.quantity.required}")
	@Positive(message = "{validation.positive}")
	@Column(name = "quantity")
	private Integer quantity;

	@NotNull(message = "{product.price.required}")
	@Positive(message = "{validation.positive}")
	@Column(name = "unit_price", precision = 12, scale = 2)
	private BigDecimal unitPrice;

	@Column(name = "total_amount", precision = 12, scale = 2)
	private BigDecimal totalAmount;

	/**
	 * Compares the current expense with updated data to determine if a persistence
	 * update is necessary. Updates fields locally if changes are detected.
	 * * @param newData The source object with updated expense details.
	 * return true if changes were applied; false otherwise.
	 */

	public boolean updateFrom(Expense newData) {
		boolean samePrice = (this.unitPrice != null && newData.getUnitPrice() != null)
				? this.unitPrice.compareTo(newData.getUnitPrice()) == 0
				: Objects.equals(this.unitPrice, newData.getUnitPrice());

		if (Objects.equals(this.itemName, newData.getItemName()) &&
				samePrice &&
				Objects.equals(this.quantity, newData.getQuantity())) {
			return false;
		}

		this.itemName = newData.getItemName();
		this.unitPrice = newData.getUnitPrice();
		this.quantity = newData.getQuantity();
		return true;
	}

	@PreUpdate
	@PrePersist
	public void recalculateTotal() {
		if (this.unitPrice != null && this.quantity != null) {
			this.totalAmount = this.unitPrice.multiply(new BigDecimal(this.quantity))
					.setScale(2, RoundingMode.HALF_UP);
		} else {
			this.totalAmount = BigDecimal.ZERO;
		}
	}
}