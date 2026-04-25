package com.micheladas.chelas.entity;

import com.micheladas.chelas.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.Objects;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends BaseEntity {

    @NotBlank(message = "{customer.number.required}")
    @Column(name = "customer_number", nullable = false, unique = true)
    private String customerNumber;

    @NotBlank(message = "{customer.name.required}")
    @Column(name = "full_name")
    private String fullName;

    @NotBlank(message = "{customer.address.required}")
    @Column(name = "address")
    private String address;

    @NotBlank(message = "{customer.zip_code.required}")
    @Column(name = "zip_code")
    private String zipCode;

    /**
     * Updates the customer's information by comparing existing data with new input.
     * Checks for changes in identification number, name, address, and zip code.
     * param newData The source object containing the updated customer details.
     * return true if any field was modified; false if the data is identical.
     */

    public boolean updateFrom(Customer newData) {
        if (Objects.equals(this.customerNumber, newData.getCustomerNumber()) &&
                Objects.equals(this.fullName, newData.getFullName()) &&
                Objects.equals(this.address, newData.getAddress()) &&
                Objects.equals(this.zipCode, newData.getZipCode())) {
            return false;
        }

        this.customerNumber = newData.getCustomerNumber();
        this.fullName = newData.getFullName();
        this.address = newData.getAddress();
        this.zipCode = newData.getZipCode();
        return true;
    }
}