package com.micheladas.chelas.entity;

import com.micheladas.chelas.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sale extends BaseEntity {

	@NotNull(message = "{validation.required}")
	@PositiveOrZero(message = "{validation.not_negative}")
	@Column(name = "cups_quantity")
	private Integer cupsQuantity;

	@NotNull(message = "{validation.required}")
	@PositiveOrZero(message = "{validation.not_negative}")
	@Column(name = "cigarettes_quantity")
	private Integer cigarettesQuantity;

	@NotNull(message = "{validation.required}")
	@PositiveOrZero(message = "{validation.not_negative}")
	@Column(name = "bigbottle_quantity")
	private Integer bigBottleQuantity;

	@NotNull(message = "{sale.total.required}")
	@Column(name = "total_amount", precision = 15, scale = 2)
	private BigDecimal totalAmount;

	@NotNull(message = "{sale.stock.required}")
	@Column(name = "bigbottle_stock")
	private Integer bigBottleStock;

	@NotNull(message = "{sale.stock.required}")
	@Column(name = "cigarettes_stock")
	private Integer cigarettesStock;

	/**
	 * Compares current sale data with new input to detect modifications.
	 * Checks all item quantities, total amount, and recorded stock levels.
	 * param newData The source object with updated sale details.
	 * return true if any field has changed; false otherwise.
	 */

	public boolean updateFrom(Sale newData) {
		boolean sameTotal = (this.totalAmount != null && newData.getTotalAmount() != null)
				? this.totalAmount.compareTo(newData.getTotalAmount()) == 0
				: Objects.equals(this.totalAmount, newData.getTotalAmount());

		if (Objects.equals(this.cupsQuantity, newData.getCupsQuantity()) &&
				Objects.equals(this.cigarettesQuantity, newData.getCigarettesQuantity()) &&
				Objects.equals(this.bigBottleQuantity, newData.getBigBottleQuantity()) &&
				sameTotal &&
				Objects.equals(this.bigBottleStock, newData.getBigBottleStock()) &&
				Objects.equals(this.cigarettesStock, newData.getCigarettesStock())) {
			return false;
		}

		this.cupsQuantity = newData.getCupsQuantity();
		this.cigarettesQuantity = newData.getCigarettesQuantity();
		this.bigBottleQuantity = newData.getBigBottleQuantity();
		this.totalAmount = newData.getTotalAmount();
		this.bigBottleStock = newData.getBigBottleStock();
		this.cigarettesStock = newData.getCigarettesStock();
		return true;
	}
}