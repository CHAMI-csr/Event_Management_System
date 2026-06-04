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
  `package_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `event_type` varchar(50) DEFAULT NULL,
  `event_date` date DEFAULT NULL,
  `start_time` time DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  `venue` varchar(150) DEFAULT NULL,
  `guest_count` int DEFAULT NULL,
  `event_status` varchar(20) DEFAULT 'Upcoming',
  `Event_Add_Date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`event_id`),
  KEY `staff_id` (`staff_id`),
  KEY `client_ibfk_1` (`client_id`),
  KEY `package_id` (`package_id`),
  CONSTRAINT `events_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`client_id`),
  CONSTRAINT `events_ibfk_2` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`),
  CONSTRAINT `events_ibfk_3` FOREIGN KEY (`package_id`) REFERENCES `package` (`package_id`)
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

-- Dumping structure for table event_db.package
CREATE TABLE IF NOT EXISTS `package` (
  `package_id` varchar(20) NOT NULL,
  `package_name` varchar(100) NOT NULL,
  `description` text,
  `price` double NOT NULL,
  PRIMARY KEY (`package_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.package: ~2 rows (approximately)
INSERT INTO `package` (`package_id`, `package_name`, `description`, `price`) VALUES
	('P-1001', 'Wedding Premium', 'Full Wedding Package with all services', 150000),
	('P-1002', 'Basic Birthday', 'Simple Birthday Package', 40000);

-- Dumping structure for table event_db.package_resources
CREATE TABLE IF NOT EXISTS `package_resources` (
  `id` int NOT NULL AUTO_INCREMENT,
  `package_id` varchar(20) NOT NULL,
  `resource_id` varchar(50) NOT NULL,
  `quantity` int DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `package_id` (`package_id`),
  KEY `resource_id` (`resource_id`),
  CONSTRAINT `package_resources_ibfk_1` FOREIGN KEY (`package_id`) REFERENCES `package` (`package_id`),
  CONSTRAINT `package_resources_ibfk_2` FOREIGN KEY (`resource_id`) REFERENCES `resources` (`resource_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.package_resources: ~7 rows (approximately)
INSERT INTO `package_resources` (`id`, `package_id`, `resource_id`, `quantity`) VALUES
	(1, 'P-1001', 'R-001', 1),
	(2, 'P-1001', 'R-002', 1),
	(3, 'P-1001', 'R-003', 1),
	(4, 'P-1001', 'R-004', 150),
	(5, 'P-1002', 'R-005', 1),
	(6, 'P-1002', 'R-006', 1),
	(7, 'P-1002', 'R-001', 1);

-- Dumping structure for table event_db.resources
CREATE TABLE IF NOT EXISTS `resources` (
  `resource_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `resource_name` varchar(100) NOT NULL,
  `resource_type` varchar(50) NOT NULL,
  `cost_per_item` decimal(10,2) NOT NULL,
  PRIMARY KEY (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.resources: ~8 rows (approximately)
INSERT INTO `resources` (`resource_id`, `resource_name`, `resource_type`, `cost_per_item`) VALUES
	('R-001', 'Professional DJ System', 'Music', 25000.00),
	('R-002', 'Wedding Photography', 'Media', 50000.00),
	('R-003', 'Floral Decorations', 'Decor', 45000.00),
	('R-004', 'Banquet Chairs', 'Furniture', 100.00),
	('R-005', 'Birthday Balloon Decor', 'Decor', 15000.00),
	('R-006', 'Birthday Cake (2kg)', 'Food', 8000.00),
	('R-007', 'Flowers', 'Decor', 1000.00),
	('R-008', 'Canabis', 'Food', 500.00);

-- Dumping structure for table event_db.staff
CREATE TABLE IF NOT EXISTS `staff` (
  `staff_id` varchar(50) NOT NULL,
  `staff_name` varchar(255) DEFAULT NULL,
  `contact_number` varchar(15) NOT NULL,
  `staff_email` varchar(100) NOT NULL,
  `staff_address` text NOT NULL,
  `Id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `role` varchar(100) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `first_time_log` tinyint(1) NOT NULL DEFAULT '0',
  `make_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.staff: ~2 rows (approximately)
INSERT INTO `staff` (`staff_id`, `staff_name`, `contact_number`, `staff_email`, `staff_address`, `Id`, `role`, `password`, `first_time_log`, `make_time`) VALUES
	('S-1000', 'chamika', '0728880547', 'infor.chami1@gmail.com', 'galle', '200625103468', 'admin', '1HNeOiZeFu7gP1lxi5tdAwGcB9i2xR+Q2jpmbuwTqzU=', 0, '2026-06-03 10:19:14'),
	('S-1001', 'sdf', '0710000654', 'trt@gmmail.com', 'rter', '5646546', 'Event Planner', 'HtF789ToNT3KjKvwvhKs39c0Sz6renmG5IUEQberXo8=', 1, '2026-06-03 10:19:14');

-- Dumping structure for table event_db.staff_log
CREATE TABLE IF NOT EXISTS `staff_log` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `staff_id` varchar(50) NOT NULL,
  `login_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `logout_time` datetime DEFAULT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.staff_log: ~13 rows (approximately)
INSERT INTO `staff_log` (`log_id`, `staff_id`, `login_time`, `logout_time`) VALUES
	(1, 'S-1000', '2026-06-03 14:24:07', NULL),
	(2, 'S-1000', '2026-06-03 14:37:46', NULL),
	(3, 'S-1000', '2026-06-03 14:38:37', NULL),
	(4, 'S-1000', '2026-06-04 11:30:27', NULL),
	(5, 'S-1000', '2026-06-04 11:38:14', NULL),
	(6, 'S-1000', '2026-06-04 11:55:56', NULL),
	(7, 'S-1000', '2026-06-04 11:56:40', NULL),
	(8, 'S-1000', '2026-06-04 11:58:28', NULL),
	(9, 'S-1000', '2026-06-04 12:02:44', NULL),
	(10, 'S-1000', '2026-06-04 12:03:14', NULL),
	(11, 'S-1000', '2026-06-04 12:03:59', NULL),
	(12, 'S-1000', '2026-06-04 12:30:38', NULL),
	(13, 'S-1000', '2026-06-04 12:36:52', NULL);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
