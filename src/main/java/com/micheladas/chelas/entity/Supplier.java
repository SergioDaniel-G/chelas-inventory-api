package com.micheladas.chelas.entity;

import com.micheladas.chelas.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Supplier extends BaseEntity {

	@NotBlank(message = "{customer.name.required}")
	@Column(name = "name", nullable = false)
	private String name;

	@Pattern(regexp = "^\\d{10}$", message = "{validation.phone.invalid}")
	@Column(name = "phone", length = 10)
	private String phone;

	@NotBlank(message = "{supplier.location.required}")
	@Column(name = "location")
	private String location;

	/**
	 * UPDATES THE SUPPLIER'S DETAILS BY COMPARING EXISTING VALUES WITH NEW DATA.
	 * EVALUATES CHANGES IN NAME, PHONE, AND LOCATION TO DETERMINE IF PERSISTENCE IS NEEDED.
	 * PARAM NEWDATA THE SOURCE OBJECT CONTAINING UPDATED SUPPLIER INFORMATION.
	 * RETURN TRUE IF ANY FIELD WAS UPDATED; FALSE IF THE DATA REMAINS IDENTICAL.
	 */

	public boolean updateFrom(Supplier newData) {
		if (Objects.equals(this.name, newData.getName()) &&
				Objects.equals(this.phone, newData.getPhone()) &&
				Objects.equals(this.location, newData.getLocation())) {
			return false;
		}

		this.name = newData.getName();
		this.phone = newData.getPhone();
		this.location = newData.getLocation();
		return true;
	}
}