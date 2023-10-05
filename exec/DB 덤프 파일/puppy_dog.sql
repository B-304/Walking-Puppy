-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: j9b304a.p.ssafy.io    Database: puppy
-- ------------------------------------------------------
-- Server version	8.0.34-0ubuntu0.20.04.1

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
-- Table structure for table `dog`
--

DROP TABLE IF EXISTS `dog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dog` (
  `dog_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `exp` int NOT NULL DEFAULT '0',
  `name` varchar(255) NOT NULL DEFAULT 'dog',
  `dog_level_id` bigint DEFAULT NULL,
  PRIMARY KEY (`dog_id`),
  KEY `FKb8vcsckcfb5wbhq258uer6wme` (`dog_level_id`),
  CONSTRAINT `FKb8vcsckcfb5wbhq258uer6wme` FOREIGN KEY (`dog_level_id`) REFERENCES `dog_level` (`dog_level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dog`
--

LOCK TABLES `dog` WRITE;
/*!40000 ALTER TABLE `dog` DISABLE KEYS */;
INSERT INTO `dog` VALUES (2,'2023-09-29 10:58:35',34,'몽실이',1),(4,'2023-10-03 01:40:20',0,'뽀삐',1),(5,'2023-10-03 01:43:04',0,'뽀삐',1),(6,'2023-10-03 01:45:04',0,'뽀삐',1),(7,'2023-10-03 01:45:45',0,'뽀삐',1),(8,'2023-10-03 01:56:54',12,'뽀삐',1),(9,'2023-10-04 00:04:42',0,'뽀삐',1),(10,'2023-10-04 23:15:25',0,'뽀삐',1),(11,'2023-10-04 23:18:38',0,'뽀삐',1);
/*!40000 ALTER TABLE `dog` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-05 21:56:03
