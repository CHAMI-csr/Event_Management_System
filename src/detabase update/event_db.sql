-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.4.3 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for event_db
CREATE DATABASE IF NOT EXISTS `event_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `event_db`;

-- Dumping structure for table event_db.billing
CREATE TABLE IF NOT EXISTS `billing` (
  `bill_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `event_id` varchar(50) DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `advance_payment` decimal(10,2) DEFAULT '0.00',
  `balance_due` decimal(10,2) DEFAULT NULL,
  `payment_status` varchar(20) DEFAULT 'Pending',
  PRIMARY KEY (`bill_id`),
  KEY `event_id` (`event_id`),
  CONSTRAINT `billing_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.billing: ~0 rows (approximately)

-- Dumping structure for table event_db.clients
CREATE TABLE IF NOT EXISTS `clients` (
  `client_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `client_name` varchar(100) NOT NULL,
  `nic` varchar(20) DEFAULT NULL,
  `contact_number` varchar(15) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` text,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.clients: ~1 rows (approximately)
INSERT INTO `clients` (`client_id`, `client_name`, `nic`, `contact_number`, `email`, `address`) VALUES
	('C-1001', 'chamika', '200625103468', '071613367', 'infor.chamika@gmail.com', 'galle');

-- Dumping structure for table event_db.events
CREATE TABLE IF NOT EXISTS `events` (
  `event_id` varchar(50) NOT NULL,
  `client_id` varchar(100) DEFAULT NULL,
  `staff_id` varchar(50) NOT NULL,
  `event_type` varchar(50) DEFAULT NULL,
  `event_date` date DEFAULT NULL,
  `start_time` time DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  `venue` varchar(150) DEFAULT NULL,
  `guest_count` int DEFAULT NULL,
  `event_status` varchar(20) DEFAULT 'Upcoming',
  PRIMARY KEY (`event_id`),
  KEY `staff_id` (`staff_id`),
  KEY `client_ibfk_1` (`client_id`),
  CONSTRAINT `client_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`client_id`),
  CONSTRAINT `events_ibfk_2` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.events: ~0 rows (approximately)

-- Dumping structure for table event_db.event_resources
CREATE TABLE IF NOT EXISTS `event_resources` (
  `assignment_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `event_id` varchar(50) DEFAULT NULL,
  `resource_id` varchar(50) DEFAULT NULL,
  `quantity` int DEFAULT '1',
  `total_cost` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`assignment_id`),
  KEY `event_id` (`event_id`),
  KEY `event_resources_ibfk_2` (`resource_id`),
  CONSTRAINT `event_resources_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`),
  CONSTRAINT `event_resources_ibfk_2` FOREIGN KEY (`resource_id`) REFERENCES `resources` (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.event_resources: ~0 rows (approximately)

-- Dumping structure for table event_db.resources
CREATE TABLE IF NOT EXISTS `resources` (
  `resource_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `resource_name` varchar(100) NOT NULL,
  `resource_type` varchar(50) NOT NULL,
  `cost_per_item` decimal(10,2) NOT NULL,
  PRIMARY KEY (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.resources: ~0 rows (approximately)

-- Dumping structure for table event_db.staff
CREATE TABLE IF NOT EXISTS `staff` (
  `staff_id` varchar(50) NOT NULL,
  `staff_name` varchar(255) DEFAULT NULL,
  `contact_number` int NOT NULL DEFAULT (0),
  `staff_email` varchar(100) NOT NULL,
  `staff_address` text NOT NULL,
  `Id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `role` varchar(100) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `first_time_log` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.staff: ~2 rows (approximately)
INSERT INTO `staff` (`staff_id`, `staff_name`, `contact_number`, `staff_email`, `staff_address`, `Id`, `role`, `password`, `first_time_log`) VALUES
	('S-1000', 'chamika', 728880547, 'infor.chami1@gmail.com', 'galle', '200625103468', 'admin', '1HNeOiZeFu7gP1lxi5tdAwGcB9i2xR+Q2jpmbuwTqzU=', 0),
	('S-1001', 'sdf', 654, 'trt@gmmail.com', 'rter', '5646546.0', 'Event Planner', 'HtF789ToNT3KjKvwvhKs39c0Sz6renmG5IUEQberXo8=', 1);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
