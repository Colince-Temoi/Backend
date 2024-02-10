-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: student_schema
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_address_master`
--

DROP TABLE IF EXISTS `t_address_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_address_master` (
  `address_id` int NOT NULL AUTO_INCREMENT,
  `lane` varchar(45) NOT NULL,
  `city` varchar(45) NOT NULL,
  `state` varchar(45) NOT NULL,
  `zip` varchar(8) NOT NULL,
  `roll_no` int NOT NULL,
  PRIMARY KEY (`address_id`),
  KEY `roll_no_fk_idx` (`roll_no`),
  CONSTRAINT `roll_no_fk1` FOREIGN KEY (`roll_no`) REFERENCES `t_student_master` (`roll_no`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_address_master`
--

LOCK TABLES `t_address_master` WRITE;
/*!40000 ALTER TABLE `t_address_master` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_address_master` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_branch_master`
--

DROP TABLE IF EXISTS `t_branch_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_branch_master` (
  `branch_id` int NOT NULL AUTO_INCREMENT,
  `branch_name` varchar(32) NOT NULL,
  PRIMARY KEY (`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_branch_master`
--

LOCK TABLES `t_branch_master` WRITE;
/*!40000 ALTER TABLE `t_branch_master` DISABLE KEYS */;
INSERT INTO `t_branch_master` VALUES (1,'CSE'),(2,'IT'),(3,'EE');
/*!40000 ALTER TABLE `t_branch_master` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_course_master`
--

DROP TABLE IF EXISTS `t_course_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_course_master` (
  `course_id` int NOT NULL AUTO_INCREMENT,
  `course_title` varchar(45) NOT NULL,
  `fees` double NOT NULL,
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_course_master`
--

LOCK TABLES `t_course_master` WRITE;
/*!40000 ALTER TABLE `t_course_master` DISABLE KEYS */;
INSERT INTO `t_course_master` VALUES (1,'C++',3000),(2,'Java',4000),(3,'Python',5000);
/*!40000 ALTER TABLE `t_course_master` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_student_course`
--

DROP TABLE IF EXISTS `t_student_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_student_course` (
  `sc_id` int NOT NULL AUTO_INCREMENT,
  `roll_no` int NOT NULL,
  `course_id` int NOT NULL,
  PRIMARY KEY (`sc_id`),
  KEY `roll_no_fk_idx` (`roll_no`),
  KEY `course_id_fk_idx` (`course_id`),
  CONSTRAINT `course_id_fk` FOREIGN KEY (`course_id`) REFERENCES `t_course_master` (`course_id`),
  CONSTRAINT `roll_no_fk` FOREIGN KEY (`roll_no`) REFERENCES `t_student_master` (`roll_no`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_student_course`
--

LOCK TABLES `t_student_course` WRITE;
/*!40000 ALTER TABLE `t_student_course` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_student_course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_student_master`
--

DROP TABLE IF EXISTS `t_student_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_student_master` (
  `roll_no` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `dob` date NOT NULL,
  `cgpa` double NOT NULL,
  `yroa` int NOT NULL,
  `branch_id` int NOT NULL,
  PRIMARY KEY (`roll_no`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `branch_id_fk_idx` (`branch_id`),
  CONSTRAINT `branch_id_fk` FOREIGN KEY (`branch_id`) REFERENCES `t_branch_master` (`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_student_master`
--

LOCK TABLES `t_student_master` WRITE;
/*!40000 ALTER TABLE `t_student_master` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_student_master` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-22 11:35:15
