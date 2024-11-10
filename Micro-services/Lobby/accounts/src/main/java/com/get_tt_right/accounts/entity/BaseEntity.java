package com.get_tt_right.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/** Entails metadata fields for all entities I will use in this project.
 *
* */
@MappedSuperclass  // Indicates to the Spring Data Jpa Framework that this class is going to act as a super class for all my entities wherever I extend this class.
@Getter @Setter @ToString
public class BaseEntity {

    @Column(updatable = false) // The purpose of this updatable = false is whenever a record is being updated inside a DB table, I don't want this column to be considered by the Spring Data Jpa Framework to populate and to update the value. This means that this field will not be updated when the record is updated. I want to keep the value of this field as it is when the record was created.
    private LocalDateTime createdAt;

    @Column(updatable = false)
    private String createdBy;

    @Column(insertable = false) // This means that this field will not be inserted when the record is created. Whenever a new record is created, this field will not be considered by the Spring Data Jpa Framework to populate and to insert the value. This makes sense because while inserting a record very first time, why do we want to update the value of this field? We want to keep the value of this field as it is, null, when the record was created.
    private LocalDateTime updatedAt;

    @Column(insertable = false)
    private String updatedBy;
}