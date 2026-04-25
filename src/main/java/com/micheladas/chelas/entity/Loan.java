package com.micheladas.chelas.entity;

import com.micheladas.chelas.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Loan extends BaseEntity {

	@NotBlank(message = "{loan.customer.required}")
	@Column(name = "customer_name", nullable = false)
	private String customerName;

	@NotNull(message = "{product.quantity.required}")
	@PositiveOrZero(message = "{validation.not_negative}")
	@Column(name = "quantity")
	private Integer quantity;

	@NotNull(message = "{validation.required}")
	@PositiveOrZero(message = "{validation.not_negative}")
	@Column(name = "total_amount", precision = 12, scale = 2)
	private BigDecimal totalAmount;

	@NotBlank(message = "{loan.description.required}")
	@Column(name = "order_description")
	private String orderDescription;

	@NotEmpty(message = "{user.phone.required}")
	@Column(name = "customer_phone", length = 20)
	private String customerPhone;

	/**
	 * Compares the current loan record with updated data to identify changes.
	 * Updates all fields if at least one difference is found.
	 *
	 * param newData The source object with the updated loan information.
	 * return true if the entity was updated; false if data remains unchanged.
	 */

	public boolean updateFrom(Loan newData) {
		if (newData == null) return false;

		boolean sameAmount = (this.totalAmount != null && newData.getTotalAmount() != null)
				? this.totalAmount.compareTo(newData.getTotalAmount()) == 0
				: Objects.equals(this.totalAmount, newData.getTotalAmount());

		if (Objects.equals(this.customerName, newData.getCustomerName()) &&
				Objects.equals(this.quantity, newData.getQuantity()) &&
				sameAmount &&
				Objects.equals(this.orderDescription, newData.getOrderDescription()) &&
				Objects.equals(this.customerPhone, newData.getCustomerPhone())) {
			return false;
		}

		this.customerName = newData.getCustomerName();
		this.quantity = newData.getQuantity();
		this.totalAmount = newData.getTotalAmount();
		this.orderDescription = newData.getOrderDescription();
		this.customerPhone = newData.getCustomerPhone();
		return true;
	}
}