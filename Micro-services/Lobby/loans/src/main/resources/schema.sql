-- Create the loans table with the following columns.
-- The reason I am not creating a Customer table is I already did that in the Accounts service.
-- What I'm going to do is, inside all the micro-services, I'm going to have a mobile number and using the same mobile number of the customer which is created in the Accounts micro-service we can create a loans record and we can also create a cards record inside the cards micro-service.
-- Below is the structure of the loans table with loan id as the primary key and mobile number as the foreign key.
-- We will generate the loan number using some business logic.
-- We also have the 4 metadata columns created_at, created_by, updated_at, and updated_by.
-- For this database table we need to create JPA entity class and a repository interface. Check respective files in the loans micro-service.
CREATE TABLE IF NOT EXISTS `loans` (
  `loan_id` int NOT NULL AUTO_INCREMENT,
  `mobile_number` varchar(15) NOT NULL,
  `loan_number` varchar(100) NOT NULL,
  `loan_type` varchar(100) NOT NULL,
  `total_loan` int NOT NULL,
  `amount_paid` int NOT NULL,
  `outstanding_amount` int NOT NULL,
  `created_at` date NOT NULL,
  `created_by` varchar(20) NOT NULL,
  `updated_at` date DEFAULT NULL,
  `updated_by` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`loan_id`)
);