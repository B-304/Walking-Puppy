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
-- Table structure for table `walk_spot`
--

DROP TABLE IF EXISTS `walk_spot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `walk_spot` (
  `walk_spot_id` bigint NOT NULL AUTO_INCREMENT,
  `spot_id` bigint DEFAULT NULL,
  `walk_id` bigint DEFAULT NULL,
  PRIMARY KEY (`walk_spot_id`),
  KEY `FKiilnknsy0gq1ypk3lfoecmwy4` (`spot_id`),
  KEY `FKdufqnb42g6h0sb3lojfo3ki4l` (`walk_id`),
  CONSTRAINT `FKdufqnb42g6h0sb3lojfo3ki4l` FOREIGN KEY (`walk_id`) REFERENCES `walk` (`walk_id`),
  CONSTRAINT `FKiilnknsy0gq1ypk3lfoecmwy4` FOREIGN KEY (`spot_id`) REFERENCES `spot` (`spot_id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `walk_spot`
--

LOCK TABLES `walk_spot` WRITE;
/*!40000 ALTER TABLE `walk_spot` DISABLE KEYS */;
INSERT INTO `walk_spot` VALUES (4,1,2),(5,2,2),(6,3,2),(7,1,3),(8,2,3),(9,3,3),(18,1,28),(33,1,81),(34,2,81),(35,3,81),(36,1,82),(37,2,82),(38,3,82);
/*!40000 ALTER TABLE `walk_spot` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-05 21:56:06
