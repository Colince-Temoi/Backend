package com.get_tt_right.accounts.entity;

import jakarta.persistence.*;
import lombok.*;

/* The reason why I have not used @Data in our entity class(s) is because it will generate the equals() and hashCode() methods for us which we don't want.
* Because sometimes it is going to create issues with the Spring Data Jpa Framework. So, I am using @Getter, @Setter, @ToString, @AllArgsConstructor, @NoArgsConstructor annotations.
* */
@Entity // This tells Hibernate to make a table out of this class.
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Accounts extends BaseEntity {

    @Column(name="customer_id")
    private Long customerId;    // This is the foreign key to the customer table.

    @Column(name="account_number")
    @Id
    private Long accountNumber; // We will generate this by ourselves. It's usually a 10-digit number.

    @Column(name="account_type") // This is completely optional, ignoring the underscore and the case. No need to define this annotation.
    private String accountType;

    @Column(name="branch_address")
    private String branchAddress;

}