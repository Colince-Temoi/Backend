CREATE DATABASE  IF NOT EXISTS `collage_admn` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `collage_admn`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: collage_admn
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
-- Table structure for table `admission_tbl`
--

DROP TABLE IF EXISTS `admission_tbl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admission_tbl` (
  `enrollment_id` int NOT NULL AUTO_INCREMENT,
  `applicany_name` varchar(45) NOT NULL,
  `fourth_optional` varchar(10) NOT NULL,
  `enrollment_date` date NOT NULL,
  `collage_id` int NOT NULL,
  PRIMARY KEY (`enrollment_id`),
  KEY `collage_id_fk_idx` (`collage_id`),
  CONSTRAINT `collage_id_fk` FOREIGN KEY (`collage_id`) REFERENCES `collage_master` (`collage_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admission_tbl`
--

LOCK TABLES `admission_tbl` WRITE;
/*!40000 ALTER TABLE `admission_tbl` DISABLE KEYS */;
/*!40000 ALTER TABLE `admission_tbl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collage_master`
--

DROP TABLE IF EXISTS `collage_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collage_master` (
  `collage_id` int NOT NULL AUTO_INCREMENT,
  `collage_name` varchar(45) NOT NULL,
  `total_seats` int NOT NULL,
  `course_fee` double NOT NULL,
  PRIMARY KEY (`collage_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collage_master`
--

LOCK TABLES `collage_master` WRITE;
/*!40000 ALTER TABLE `collage_master` DISABLE KEYS */;
INSERT INTO `collage_master` VALUES (1,'Multimedia',40,1000),(2,'UoN',30,2000),(3,'KU',20,3000),(4,'Jkuat',50,4000);
/*!40000 ALTER TABLE `collage_master` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'collage_admn'
--
/*!50003 DROP PROCEDURE IF EXISTS `p_collage_screen` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `p_collage_screen`(
 In p_status Varchar(50),
 Out o_msg Varchar(120)
)
BEGIN
  IF p_status = 'selectAllCollages' THEN
    SELECT c.collage_id, c.collage_name
    FROM collage_master c;
    SET o_msg = 'All Collages';
   END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-09 10:53:25
