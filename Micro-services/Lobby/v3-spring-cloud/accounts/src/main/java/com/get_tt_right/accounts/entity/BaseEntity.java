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
 * With the help of Spring Data JPA we can populate/update these metadata fields automatically. This means that we can give this responsibility to the Spring Data JPA Framework to populate and update these fields automatically.
 * Because anyway, the framework is going to take care of creating all the insert scripts, update scripts, delete scripts, and select scripts. So, why not give this responsibility to the Spring Data JPA Framework to populate and update these fields automatically.
 * All kind of the SQL scripts right now is in the control of the Spring Data JPA Framework as we have not written any single query inside our application.
 * Since Spring Data JPA is going to take care of all the SQL scripts/interactions with the database, we can give this responsibility to the Spring Data JPA Framework to populate and update these 4 metadata fields automatically.
 * As Spring data jpa also supports this auditing feature.
 * The annotation `@EntityListeners(AuditingEntityListener.class)` to the Spring Data JPA framework that the `BaseEntity` class will act as a listener for auditing events.
 * This means that the framework will automatically populate and update the auditing fields (such as `createdBy`, `createdDate`, `lastModifiedBy`, and `lastModifiedDate`) in the entities that extend `BaseEntity`.
 * The `AuditingEntityListener` class is responsible for handling these auditing events.
 * As a last step, we need to enable auditing in our application by adding the `@EnableJpaAuditing` annotation to the main class of our Spring Boot application.
 * Now, in the service impl, delete the manual code where we are trying to populate the createdBy and createdAt fields.
 * You can proceed to saving your code and running the application. You can now test the application by creating a new customer or updating an existing customer. You will see that the createdBy and createdAt fields are automatically populated by the Spring Data JPA framework. Yeey!
 * In this way we have given the responsibility of auditing to the Spring Data JPA Framework. This is great and one of the standards you can follow in your Rest APIs and Micro-service.
* */
@MappedSuperclass  // Indicates to the Spring Data Jpa Framework that this class is going to act as a super class for all my entities wherever I extend this class.
@EntityListeners(AuditingEntityListener.class) // It is going to tell the Spring Data Jpa Framework that this class is going to act as a listener for the auditing events.
@Getter @Setter @ToString
public class BaseEntity {

    @CreatedDate // Telling to the Spring Data Jpa Framework that for this field createdAt, you need to populate the current date and time whenever a new record is created.
    @Column(updatable = false) // The purpose of this updatable = false is whenever a record is being updated inside a DB table, I don't want this column to be considered by the Spring Data Jpa Framework to populate and to update the value. This means that this field will not be updated when the record is updated. I want to keep the value of this field as it is when the record was created.
    private LocalDateTime createdAt;

    @CreatedBy // Telling to the Spring data Jpa framework like who is trying to create this record. But here there is a problem, Spring data jpa framework can get the date/time from the local system/server. How about who is creating/updating the data? It does not have any clue. For the same we need to write some small logic. Check the package audit.
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(insertable = false) // This means that this field will not be inserted when the record is created. Whenever a new record is created, this field will not be considered by the Spring Data Jpa Framework to populate and to insert the value. This makes sense because while inserting a record very first time, why do we want to update the value of this field? We want to keep the value of this field as it is, null, when the record was created.
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(insertable = false)
    private String updatedBy;
}