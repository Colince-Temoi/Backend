package com.get_tt_right.accounts.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

/** This Entity class represents accounts table inside the DB.
 * */
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Accounts extends BaseEntity {

    @Id
    private Long accountNumber;
    private String accountType;
    private String branchAddress;
    private String mobileNumber;
    private boolean activeSw;

}
