CREATE DATABASE  IF NOT EXISTS `student_schema` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `student_schema`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: student_schema
-- ------------------------------------------------------
-- Server version	8.0.34

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
  `lane` varchar(255) NOT NULL,
  `zip` varchar(255) NOT NULL,
  `state_id` int NOT NULL,
  `roll_no` int NOT NULL,
  PRIMARY KEY (`address_id`),
  KEY `state_id_fk_idx` (`state_id`),
  KEY `roll_number_fk` (`roll_no`),
  CONSTRAINT `roll_number_fk` FOREIGN KEY (`roll_no`) REFERENCES `t_student_master` (`roll_no`),
  CONSTRAINT `state_id_fk` FOREIGN KEY (`state_id`) REFERENCES `t_state` (`state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
  `branch_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_branch_master`
--

LOCK TABLES `t_branch_master` WRITE;
/*!40000 ALTER TABLE `t_branch_master` DISABLE KEYS */;
INSERT INTO `t_branch_master` VALUES (1,'Nairobi'),(2,'Mombasa'),(3,'Kisumu');
/*!40000 ALTER TABLE `t_branch_master` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_city`
--

DROP TABLE IF EXISTS `t_city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_city` (
  `city_id` int NOT NULL AUTO_INCREMENT,
  `city_name` varchar(45) NOT NULL,
  `state_id` int NOT NULL,
  PRIMARY KEY (`city_id`),
  KEY `state_id_fk_idx` (`state_id`),
  CONSTRAINT `state_id_fk9` FOREIGN KEY (`state_id`) REFERENCES `t_state` (`state_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_city`
--

LOCK TABLES `t_city` WRITE;
/*!40000 ALTER TABLE `t_city` DISABLE KEYS */;
INSERT INTO `t_city` VALUES (4,'Nairobi',1),(5,'Mombasa',2),(6,'Kisumu',3),(7,'Westy',1),(8,'Likoni',2),(9,'Kakamega',3);
/*!40000 ALTER TABLE `t_city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_course_master`
--

DROP TABLE IF EXISTS `t_course_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_course_master` (
  `course_id` int NOT NULL AUTO_INCREMENT,
  `course_title` varchar(255) DEFAULT NULL,
  `fees` double DEFAULT NULL,
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_course_master`
--

LOCK TABLES `t_course_master` WRITE;
/*!40000 ALTER TABLE `t_course_master` DISABLE KEYS */;
INSERT INTO `t_course_master` VALUES (1,'Cs',2000),(2,'IT',1500),(3,'Se',1500),(4,'Ct',1000);
/*!40000 ALTER TABLE `t_course_master` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_state`
--

DROP TABLE IF EXISTS `t_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_state` (
  `state_id` int NOT NULL AUTO_INCREMENT,
  `state_name` varchar(45) NOT NULL,
  PRIMARY KEY (`state_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_state`
--

LOCK TABLES `t_state` WRITE;
/*!40000 ALTER TABLE `t_state` DISABLE KEYS */;
INSERT INTO `t_state` VALUES (1,'Central'),(2,'Coast'),(3,'Western');
/*!40000 ALTER TABLE `t_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_student_course`
--

DROP TABLE IF EXISTS `t_student_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_student_course` (
  `roll_no` int NOT NULL,
  `course_id` int NOT NULL,
  KEY `FKqtdui1solsqhr1g5vblq7cc65` (`course_id`),
  KEY `FKfprrbym3w0jva16gxat64tflp` (`roll_no`),
  CONSTRAINT `FKfprrbym3w0jva16gxat64tflp` FOREIGN KEY (`roll_no`) REFERENCES `t_student_master` (`roll_no`),
  CONSTRAINT `FKqtdui1solsqhr1g5vblq7cc65` FOREIGN KEY (`course_id`) REFERENCES `t_course_master` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
  `cgpa` double DEFAULT NULL,
  `dob` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `yroa` int DEFAULT NULL,
  `branch_id` int DEFAULT NULL,
  PRIMARY KEY (`roll_no`),
  KEY `FKdf975hn1bm6t6heh2elnp72jp` (`branch_id`),
  CONSTRAINT `FKdf975hn1bm6t6heh2elnp72jp` FOREIGN KEY (`branch_id`) REFERENCES `t_branch_master` (`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_student_master`
--

LOCK TABLES `t_student_master` WRITE;
/*!40000 ALTER TABLE `t_student_master` DISABLE KEYS */;
INSERT INTO `t_student_master` VALUES (1,7.2,'2001-01-12 00:00:00.000000','amit@gmail.com','Amit Sahoo',2022,NULL);
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

-- Dump completed on 2024-02-15 15:09:46
