package com.micheladas.chelas.entity;

import com.micheladas.chelas.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "prices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Price extends BaseEntity {

	@NotBlank(message = "{price.product_id.required}")
	@Column(name = "product_id", nullable = false)
	private String productId;

	@NotBlank(message = "{price.description.required}")
	@Column(name = "description")
	private String description;

	@NotNull(message = "{validation.required}")
	@PositiveOrZero(message = "{validation.stock.min}")
	@Column(name = "stock")
	private Integer stock;

	@NotNull(message = "{validation.required}")
	@PositiveOrZero(message = "{validation.not_negative}")
	@Column(name = "product_price", precision = 12, scale = 2)
	private BigDecimal productPrice;

	/**
	 * Compare and update data.
	 */
	public boolean updateFrom(Price newData) {
		boolean samePrice = (this.productPrice != null && newData.getProductPrice() != null)
				? this.productPrice.compareTo(newData.getProductPrice()) == 0
				: Objects.equals(this.productPrice, newData.getProductPrice());

		if (Objects.equals(this.productId, newData.getProductId()) &&
				Objects.equals(this.description, newData.getDescription()) &&
				Objects.equals(this.stock, newData.getStock()) &&
				samePrice) {
			return false;
		}

		this.productId = newData.getProductId();
		this.description = newData.getDescription();
		this.stock = newData.getStock();
		this.productPrice = newData.getProductPrice();
		return true;
	}
}