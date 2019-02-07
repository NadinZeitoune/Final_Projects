CREATE DATABASE  IF NOT EXISTS `ride_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `ride_db`;
-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: localhost    Database: ride_db
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `rides`
--

DROP TABLE IF EXISTS `rides`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `rides` (
  `ride_number` int(11) NOT NULL AUTO_INCREMENT,
  `origin` varchar(20) NOT NULL,
  `destination` varchar(20) NOT NULL,
  `departure` varchar(20) NOT NULL,
  `arrival` varchar(20) NOT NULL,
  `passenger_num` int(11) NOT NULL,
  `driver` varchar(20) NOT NULL,
  `passenger1` varchar(20) DEFAULT NULL,
  `passenger2` varchar(20) DEFAULT NULL,
  `passenger3` varchar(20) DEFAULT NULL,
  `passenger4` varchar(20) DEFAULT NULL,
  `passenger5` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ride_number`),
  KEY `driver` (`driver`),
  KEY `passenger1` (`passenger1`),
  KEY `passenger2` (`passenger2`),
  KEY `passenger3` (`passenger3`),
  KEY `passenger4` (`passenger4`),
  KEY `passenger5` (`passenger5`),
  CONSTRAINT `rides_ibfk_1` FOREIGN KEY (`driver`) REFERENCES `users` (`username`),
  CONSTRAINT `rides_ibfk_2` FOREIGN KEY (`passenger1`) REFERENCES `users` (`username`),
  CONSTRAINT `rides_ibfk_3` FOREIGN KEY (`passenger2`) REFERENCES `users` (`username`),
  CONSTRAINT `rides_ibfk_4` FOREIGN KEY (`passenger3`) REFERENCES `users` (`username`),
  CONSTRAINT `rides_ibfk_5` FOREIGN KEY (`passenger4`) REFERENCES `users` (`username`),
  CONSTRAINT `rides_ibfk_6` FOREIGN KEY (`passenger5`) REFERENCES `users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rides`
--

LOCK TABLES `rides` WRITE;
/*!40000 ALTER TABLE `rides` DISABLE KEYS */;
INSERT INTO `rides` VALUES (19,'Arim mall KS','Diamond borsa RG',' 7/2/2019, 07:30',' 7/2/2019, 08:15',1,'nadin','pierrozzz',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `rides` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `users` (
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `first_name` varchar(20) NOT NULL,
  `last_name` varchar(20) NOT NULL,
  `phone_number` varchar(14) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('nadin','8','nadin','zeitoune','0547007691'),('pierrozzz','HelloWorld','Pierre','Janineh','0526524288');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-02-07 18:25:38
