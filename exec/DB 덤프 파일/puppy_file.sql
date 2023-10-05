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
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file` (
  `file_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `original_name` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file`
--

LOCK TABLES `file` WRITE;
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
INSERT INTO `file` VALUES (1,'2023-10-04 09:40:33',NULL,'Duck.gltf','https://b304-bucket.s3.ap-northeast-2.amazonaws.com/9b8689cf-f396-4660-bdda-097497d02219.gltf'),(2,'2023-10-04 17:19:27',NULL,'DuckCM.png','https://b304-bucket.s3.ap-northeast-2.amazonaws.com/4864cf68-bc77-4599-87d5-33286a65d5a5.png'),(3,'2023-10-05 09:08:53',NULL,'지도2.png','https://b304-bucket.s3.ap-northeast-2.amazonaws.com/b5016673-3660-4f46-89cd-1c7f48d33482.png'),(4,'2023-10-05 09:09:22',NULL,'지도1.png','https://b304-bucket.s3.ap-northeast-2.amazonaws.com/c94a3400-dc80-43e8-a02e-f4612ef20ee9.png'),(5,'2023-10-05 09:09:37',NULL,'지도3.png','https://b304-bucket.s3.ap-northeast-2.amazonaws.com/5e0788eb-99b0-4f0c-a295-773b9333e385.png'),(6,'2023-10-05 16:23:36',NULL,'강아지.png','https://b304-bucket.s3.ap-northeast-2.amazonaws.com/37e392bb-7e1d-4f73-832a-6eb1e45731f8.png'),(7,'2023-10-05 17:38:15',NULL,'강아지.png','https://b304-bucket.s3.ap-northeast-2.amazonaws.com/50e88215-12b2-46f2-84fe-d9bb8434d58a.png'),(8,'2023-10-05 17:51:37',NULL,'중도종료지도.PNG','https://b304-bucket.s3.ap-northeast-2.amazonaws.com/58e4a8fc-e70c-4808-86ed-5e442b7a33ec.PNG'),(9,'2023-10-05 21:32:48',NULL,'지도4.PNG','https://b304-bucket.s3.ap-northeast-2.amazonaws.com/24f5c90e-2216-41ca-af9f-68d682aafdbc.PNG');
/*!40000 ALTER TABLE `file` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-05 21:56:05
