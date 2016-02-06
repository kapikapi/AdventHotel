-- MySQL dump 10.13  Distrib 5.5.47, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: advent_hotel
-- ------------------------------------------------------
-- Server version	5.5.47-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `apartments`
--

DROP TABLE IF EXISTS `apartments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `apartments` (
  `apt_id` int(11) NOT NULL AUTO_INCREMENT,
  `number` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `places` int(11) NOT NULL,
  `class` tinyint(4) NOT NULL,
  `cost` int(11) NOT NULL,
  `description` int(11) DEFAULT NULL,
  PRIMARY KEY (`apt_id`),
  UNIQUE KEY `number` (`number`),
  KEY `description` (`description`),
  CONSTRAINT `apartments_ibfk_1` FOREIGN KEY (`description`) REFERENCES `apt_text` (`text_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apartments`
--

LOCK TABLES `apartments` WRITE;
/*!40000 ALTER TABLE `apartments` DISABLE KEYS */;
INSERT INTO `apartments` VALUES (1,'101',1,1,50,5),(2,'102',2,1,100,6),(3,'103',3,1,150,7),(4,'104',4,1,200,8),(5,'105',1,2,25,1),(6,'106',2,2,50,3),(7,'107',3,2,75,2),(8,'108',4,2,100,2),(9,'201',1,1,50,8),(10,'202',2,1,100,7),(11,'203',3,1,150,6),(12,'204',4,1,200,5),(13,'205',1,2,25,4),(14,'206',2,2,50,3),(15,'207',3,2,75,1),(16,'208',4,2,100,2),(17,'301',1,1,50,5),(18,'302',2,1,100,7),(19,'303',3,1,150,6),(20,'304',4,1,200,6),(21,'305',1,2,25,2),(22,'306',2,2,50,3),(23,'307',3,2,75,1),(24,'308',4,2,100,3),(25,'401',2,1,100,9),(26,'402',4,2,150,10);
/*!40000 ALTER TABLE `apartments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `apt_text`
--

DROP TABLE IF EXISTS `apt_text`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `apt_text` (
  `text_id` int(11) NOT NULL AUTO_INCREMENT,
  `locale` enum('en','ru') COLLATE utf8_unicode_ci NOT NULL,
  `apt_loc_text` text COLLATE utf8_unicode_ci,
  `text_number` int(11) NOT NULL,
  PRIMARY KEY (`text_id`),
  UNIQUE KEY `text_number` (`text_number`,`locale`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apt_text`
--

LOCK TABLES `apt_text` WRITE;
/*!40000 ALTER TABLE `apt_text` DISABLE KEYS */;
INSERT INTO `apt_text` VALUES (1,'en','Great room with TV and perfect design.',1),(2,'ru','Отличная комната с телевизором и прекрасным оформлением.',1),(3,'en','Comfortable room with a great view. Additional chair provided.',2),(4,'ru','Комфортабельная комната с прекрасным видом из окна. Предоставляется дополнительное креслою',2),(5,'en','Homely room with small balcony. ',3),(6,'ru','Уютная комната с небольшим балконом.',3),(7,'en','Old fashioned room with vintage chandelier.',4),(8,'ru','Комната в старинном стиле с знаменитым канделябром 19ого века.',4),(9,'en','Luxurious room in scythians treasures style design.',5),(10,'ru','Роскошная комната. Декор выполнен в стиле сюжетов со скифских украшений',5),(11,'en','Extra luxurious room. Pink carpet and white walls make it look futuristic.',6),(12,'ru','Роскошнейшая комната. Розовый ковер и белые стены переносят в мир будущего.',6),(13,'en','Lux room where you can take any of your pets you want.',7),(14,'ru','Прекрасная комната. Вы можете заселиться туда с любым домашним животным.',7),(15,'en','Nice room with magnificent view. Big balcony.',8),(16,'ru','Комната с приятным дизайном и великолепным видом из окна. Большой балкон.',8),(17,'en','Large lux room in penthouse. The best one we can offer. Awesome view from the terrace. Two nice cats can be provided.',9),(18,'ru','Просторная комната в пентхаусе, одна из лучших, которую может предложить отель. Бесподобный вид из окна, выход на терассу. Есть возможность предоставить двух пушистых котов.',9),(19,'en','Simple but big and well designed room. Perfect for people with pets and without.',10),(20,'ru','Простая, но очень большая и стильная комната. Идеальна для людей с домашними животными и без.',10);
/*!40000 ALTER TABLE `apt_text` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comments` (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `comment` text COLLATE utf8_unicode_ci,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `order_id` (`order_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (1,1,'I would like to have carpet in the room',3),(2,2,'Мне бы хотелось взять с собой собаку',3),(3,3,'',3),(4,4,'',3),(5,5,'Добрый день!',3),(6,6,'We would like to have breakfast in room every morning.',2),(7,7,'',2),(8,1,'What color do you prefer?',1),(9,1,'I like green with fur. Like in IKEA.',3),(10,1,'I offered you a room with a simple green carpet. Ypu can edit your order and set lux class, then I can offer you a room with pink fur carpet. But it will be more expensive.',1),(11,1,'Thank you for approving your order! Waiting for your payment',1),(12,8,'aaaa',3),(13,3,'',1),(14,3,'',1),(15,3,'Is this room dark?',3),(16,3,'No, it is really light and pretty',1),(17,3,'I wank a dark one',3),(18,3,'',3),(19,3,'',1),(20,3,'Okay, I offered you another room. It is darker',1),(21,3,'Ok',3);
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `places` int(11) NOT NULL,
  `class` tinyint(4) NOT NULL,
  `date_in` date NOT NULL,
  `date_out` date NOT NULL,
  `order_apt_id` int(11) DEFAULT NULL,
  `order_additional_info` text COLLATE utf8_unicode_ci,
  `status` enum('REQUESTED','IN_DISCUSSION','APPROVED','PAID','REJECTED') COLLATE utf8_unicode_ci NOT NULL,
  `cost` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `order_apt_id` (`order_apt_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`order_apt_id`) REFERENCES `apartments` (`apt_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,3,1,2,'2016-02-07','2016-02-09',5,NULL,'PAID',50),(2,3,3,1,'2016-02-18','2016-02-21',NULL,NULL,'REJECTED',NULL),(3,3,4,2,'2016-02-27','2016-04-01',16,'Wants room to be dark.','PAID',17400),(4,3,2,2,'2016-03-05','2016-03-12',6,NULL,'IN_DISCUSSION',NULL),(5,3,3,1,'2016-03-11','2016-03-18',11,NULL,'IN_DISCUSSION',NULL),(6,2,2,2,'2016-02-23','2016-02-28',NULL,NULL,'REQUESTED',NULL),(7,2,3,1,'2016-03-23','2016-03-26',NULL,NULL,'REQUESTED',NULL),(8,3,1,1,'2016-01-14','2016-01-21',NULL,NULL,'REQUESTED',NULL);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `login` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `passhash` varchar(355) COLLATE utf8_unicode_ci NOT NULL,
  `access_level` enum('admin','user') COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(355) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `login` (`login`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin@admin.adm','admin','21232f297a57a5a743894a0e4a801fc3','admin','Admin'),(2,'liss@liss.li','liss','6e25079982c372cc9fdb129c6b5cf311','user','Liss'),(3,'user@user.us','user','ee11cbb19052e40b07aac0ca060c23ee','user','UserName');
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

-- Dump completed on 2016-02-06 19:00:38
