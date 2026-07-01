package com.get_tt_right.profile.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Profile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // This profile_id column is going to act as a PK. Everytime we insert a new record, we want the framework to take care of generating the sequence number and using the same during the insert operation. That's why we are trying to use the 2 property level annotations i.e., @Id and @GeneratedValue with the strategy as Identity.
    @Column(name = "profile_id", nullable = false)
    private long profileId; // As you can see here, 1st I have created a field "profileId" - with this field behind the scenes we are going to create a column "profile_id" in the DB under the table name "Profile".

    @Column(name = "name", length = 100, nullable = false)
    private String name; // Using the name column we are going to store the name data.

    @Column(name = "mobile_number", length = 20, nullable = false)
    private String mobileNumber; // Followed by mobile number.

    @Column(name = "active_sw", nullable = false)
    private boolean activeSw = false; // active switch with the default value as false

    @Column(name = "account_number")
    private long accountNumber; // This column is nullable, also the card_number and loan_number columns are nullable. So during the insert operation we are going to insert some null values or some default zero values since we are trying to store them as long values.

    @Column(name = "card_number")
    private long cardNumber;

    @Column(name = "loan_number")
    private long loanNumber;
}
