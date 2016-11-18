-- MySQL dump 10.13  Distrib 5.5.52, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: m2c_update_manager
-- ------------------------------------------------------
-- Server version	5.5.52-0+deb8u1

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
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'yo','123');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_gateway`
--

DROP TABLE IF EXISTS `customer_gateway`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_gateway` (
  `id_customer` int(11) NOT NULL,
  `id_gateway` int(11) NOT NULL,
  KEY `id_customer` (`id_customer`),
  KEY `id_gateway` (`id_gateway`),
  CONSTRAINT `customer_gateway_ibfk_1` FOREIGN KEY (`id_customer`) REFERENCES `customer` (`id`),
  CONSTRAINT `customer_gateway_ibfk_2` FOREIGN KEY (`id_gateway`) REFERENCES `gateway` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_gateway`
--

LOCK TABLES `customer_gateway` WRITE;
/*!40000 ALTER TABLE `customer_gateway` DISABLE KEYS */;
INSERT INTO `customer_gateway` VALUES (1,1);
/*!40000 ALTER TABLE `customer_gateway` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gateway`
--

DROP TABLE IF EXISTS `gateway`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gateway` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hostname` varchar(30) DEFAULT NULL,
  `Key_host` varchar(20) DEFAULT NULL,
  `installedPacket` int(11) NOT NULL,
  `notUpdate` tinyint(1) DEFAULT NULL,
  `forceUpdate` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `installedPacket` (`installedPacket`),
  CONSTRAINT `gateway_ibfk_1` FOREIGN KEY (`installedPacket`) REFERENCES `packetSw` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gateway`
--

LOCK TABLES `gateway` WRITE;
/*!40000 ALTER TABLE `gateway` DISABLE KEYS */;
INSERT INTO `gateway` VALUES (1,'Nimbus','12345',1,0,0);
/*!40000 ALTER TABLE `gateway` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `packetSw`
--

DROP TABLE IF EXISTS `packetSw`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `packetSw` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `packet` varchar(30) DEFAULT NULL,
  `version` varchar(10) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `packetSw`
--

LOCK TABLES `packetSw` WRITE;
/*!40000 ALTER TABLE `packetSw` DISABLE KEYS */;
INSERT INTO `packetSw` VALUES (1,'software','2.0','2010-01-01'),(2,'software','2.1','2011-01-01'),(3,'software','2.2','2016-04-05');
/*!40000 ALTER TABLE `packetSw` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status` (
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `id_gateway` int(11) NOT NULL,
  `uptime` int(11) DEFAULT NULL,
  `gsmRange` int(11) DEFAULT NULL,
  KEY `id_gateway` (`id_gateway`),
  CONSTRAINT `status_ibfk_1` FOREIGN KEY (`id_gateway`) REFERENCES `gateway` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES ('2016-11-15 12:07:09',1,15421548,3);
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-11-16 10:49:32
