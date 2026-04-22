package com.micheladas.chelas.entity;

import com.micheladas.chelas.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

	@NotBlank(message = "{supplier.phone.required}")
	@Column(name = "phone")
	private String phone;

	@NotBlank(message = "{supplier.location.required}")
	@Column(name = "location")
	private String location;

	/**
	 * Compare and update data
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