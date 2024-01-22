CREATE DATABASE  IF NOT EXISTS `empcrud_schema` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `empcrud_schema`;
-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: empcrud_schema
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
-- Table structure for table `t_dept`
--

DROP TABLE IF EXISTS `t_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_dept` (
  `dept_id` int NOT NULL AUTO_INCREMENT,
  `dept_name` varchar(45) NOT NULL,
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_dept`
--

LOCK TABLES `t_dept` WRITE;
/*!40000 ALTER TABLE `t_dept` DISABLE KEYS */;
INSERT INTO `t_dept` VALUES (10,'Sales'),(20,'IT'),(30,'Marketing');
/*!40000 ALTER TABLE `t_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_emp`
--

DROP TABLE IF EXISTS `t_emp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_emp` (
  `emp_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `salary` double NOT NULL,
  `hire_date` date NOT NULL,
  `dept_id` int NOT NULL,
  `is_deleted` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`emp_id`),
  KEY `dept_id_fk_idx` (`dept_id`),
  CONSTRAINT `dept_id_fk` FOREIGN KEY (`dept_id`) REFERENCES `t_dept` (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_emp`
--

LOCK TABLES `t_emp` WRITE;
/*!40000 ALTER TABLE `t_emp` DISABLE KEYS */;
INSERT INTO `t_emp` VALUES (1,'Colince',201923,'2023-10-03',20,'YES'),(2,'Senior',123456,'2021-01-12',20,'NO'),(3,'Max',2000,'2023-02-02',20,'NO'),(4,'Ryan',30000,'2023-12-01',10,'NO'),(5,'James',50000,'2004-04-20',10,'NO'),(6,'Raymond',82000,'2011-06-23',30,'NO'),(7,'Iano',23980,'2013-09-23',30,'NO'),(8,'Jefferson',110000,'2007-08-14',10,'NO'),(9,'Elizabeth',98600,'2010-05-14',30,'NO'),(10,'Angela',90500,'2014-04-04',20,'NO'),(11,'Mitchelle',162900,'2004-05-29',30,'NO'),(12,'Yovitta',68050,'2022-01-12',20,'NO'),(13,'Evabrows',62000,'2021-09-27',20,'YES'),(14,'Samson',78500,'2012-04-17',30,'NO'),(15,'Bradly',52000,'2013-06-24',30,'NO'),(16,'Mose',21000,'2016-03-01',10,'NO'),(17,'Augustin',52000,'2016-07-23',20,'NO'),(18,'Michael',102500,'2018-11-22',30,'NO'),(19,'Faith',98600,'2020-10-14',20,'NO'),(20,'Gloria',112600,'2021-07-14',30,'NO'),(21,'Mildah',92800,'2017-08-14',10,'YES'),(22,'Munai',40300,'2017-07-13',10,'NO');
/*!40000 ALTER TABLE `t_emp` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-22 14:17:56
