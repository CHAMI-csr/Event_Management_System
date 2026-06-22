-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.4.2 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.17.0.7270
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
INSERT INTO `billing` (`bill_id`, `event_id`, `total_amount`, `advance_payment`, `balance_due`, `payment_status`) VALUES
	('B-0001', 'E-0001', 113000.00, 70000.00, 43000.00, 'Pending');

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

-- Dumping data for table event_db.clients: ~0 rows (approximately)
INSERT INTO `clients` (`client_id`, `client_name`, `nic`, `contact_number`, `email`, `address`) VALUES
	('C-1001', 'chamika', '200625103468', '071613367', 'infor.chamika@gmail.com', 'galle');

-- Dumping structure for table event_db.event_resources
CREATE TABLE IF NOT EXISTS `event_resources` (
  `assignment_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `event_id` varchar(50) DEFAULT NULL,
  `resource_id` varchar(50) DEFAULT NULL,
  `package_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `quantity` int DEFAULT '1',
  `total_cost` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`assignment_id`),
  KEY `event_id` (`event_id`),
  KEY `event_resources_ibfk_2` (`resource_id`),
  CONSTRAINT `event_resources_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`),
  CONSTRAINT `event_resources_ibfk_2` FOREIGN KEY (`resource_id`) REFERENCES `resources` (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.event_resources: ~4 rows (approximately)
INSERT INTO `event_resources` (`assignment_id`, `event_id`, `resource_id`, `package_id`, `quantity`, `total_cost`) VALUES
	('ASG-0002', 'E-0001', 'R-002', 'P-1001', 3, 150000.00),
	('ASG-0003', 'E-0001', 'R-004', 'P-1001', 140, 14000.00),
	('ASG-0004', 'E-0001', 'R-006', 'P-1001', 3, 24000.00),
	('ASG-0005', 'E-0001', 'R-001', NULL, 2, 50000.00);

-- Dumping structure for table event_db.events
CREATE TABLE IF NOT EXISTS `events` (
  `event_id` varchar(50) NOT NULL,
  `client_id` varchar(100) DEFAULT NULL,
  `staff_id` varchar(50) NOT NULL,
  `package_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `sup_id` varchar(50) DEFAULT NULL,
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
INSERT INTO `events` (`event_id`, `client_id`, `staff_id`, `package_id`, `sup_id`, `event_type`, `event_date`, `start_time`, `end_time`, `venue`, `guest_count`, `event_status`, `Event_Add_Date`) VALUES
	('E-0001', 'C-1001', 'S-1000', 'P-1001', NULL, NULL, '2026-06-18', '08:00:00', NULL, 'Aradana', 28, 'Upcoming', '2026-06-19 20:45:39');

-- Dumping structure for table event_db.package
CREATE TABLE IF NOT EXISTS `package` (
  `package_id` varchar(20) NOT NULL,
  `package_name` varchar(100) NOT NULL,
  `description` text,
  `price` double NOT NULL,
  PRIMARY KEY (`package_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.package: ~3 rows (approximately)
INSERT INTO `package` (`package_id`, `package_name`, `description`, `price`) VALUES
	('P-1001', 'Wedding Premium', 'Full Wedding Package with all services', 210005),
	('P-1002', 'Basic Birthday', 'Simple Birthday Package', 40000),
	('P-1003', 'Custamize Pack', 'No Need any think', 0);

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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.package_resources: ~6 rows (approximately)
INSERT INTO `package_resources` (`id`, `package_id`, `resource_id`, `quantity`) VALUES
	(1, 'P-1001', 'R-001', 1),
	(2, 'P-1001', 'R-002', 1),
	(4, 'P-1001', 'R-004', 140),
	(5, 'P-1002', 'R-005', 1),
	(6, 'P-1002', 'R-006', 5),
	(10, 'P-1001', 'R-006', 3);

-- Dumping structure for table event_db.resources
CREATE TABLE IF NOT EXISTS `resources` (
  `resource_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `resource_name` varchar(100) NOT NULL,
  `resource_type` varchar(50) NOT NULL,
  `cost_per_item` decimal(10,2) NOT NULL,
  `stock_qty` int DEFAULT '0',
  `sup_id` int DEFAULT NULL,
  PRIMARY KEY (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.resources: ~7 rows (approximately)
INSERT INTO `resources` (`resource_id`, `resource_name`, `resource_type`, `cost_per_item`, `stock_qty`, `sup_id`) VALUES
	('R-001', 'Professional DJ System', 'Music', 25000.00, 3, NULL),
	('R-002', 'Wedding Photography', 'Media', 50000.00, 0, NULL),
	('R-003', 'Floral Decorations', 'Decor', 45000.00, 10, NULL),
	('R-004', 'Banquet Chairs', 'Furniture', 100.00, 1, NULL),
	('R-005', 'Birthday Balloon Decor', 'Decor', 15000.00, 0, NULL),
	('R-006', 'Birthday Cake (2kg)', 'Food', 8000.00, 5, NULL),
	('R-007', 'jhg', 'Others', 4000.00, 2, NULL);

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
	('S-1001', 'sdf', '0710000654', 'trt@gmmail.com', 'rter', '5646546', 'Event Planner', 'HtF789ToNT3KjKvwvhKs39c0Sz6renmG5IUEQberXo8=', 0, '2026-06-03 10:19:14');

-- Dumping structure for table event_db.staff_log
CREATE TABLE IF NOT EXISTS `staff_log` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `staff_id` varchar(50) NOT NULL,
  `login_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `logout_time` datetime DEFAULT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=242 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.staff_log: ~3 rows (approximately)
INSERT INTO `staff_log` (`log_id`, `staff_id`, `login_time`, `logout_time`) VALUES
	(239, 'S-1000', '2026-06-22 20:05:17', '2026-06-22 20:05:44'),
	(240, 'S-1000', '2026-06-22 20:19:24', '2026-06-22 20:19:32'),
	(241, 'S-1000', '2026-06-22 20:21:48', '2026-06-22 20:21:55');

-- Dumping structure for table event_db.suppliers
CREATE TABLE IF NOT EXISTS `suppliers` (
  `sup_id` varchar(50) NOT NULL DEFAULT 'AUTO_INCREMENT',
  `sup_name` varchar(100) NOT NULL,
  `contact_number` varchar(15) NOT NULL,
  `nic` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `sup_address` varchar(200) DEFAULT NULL,
  `vehicle_modal` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `vehicle_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `vehicle_Price` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `Status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`sup_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.suppliers: ~2 rows (approximately)
INSERT INTO `suppliers` (`sup_id`, `sup_name`, `contact_number`, `nic`, `sup_address`, `vehicle_modal`, `vehicle_no`, `vehicle_Price`, `Status`) VALUES
	('S-0001', 'asdsad', '243323123', '123123123', 'fdsfas', 'dsfs', 'hgj-1211', '230000.0', 'On Goin'),
	('S-0002', 'gfreagfrefr', '324324', '3432432', 'fdgf', 'bda-1991', '3243242343', '3423424.0', 'Active');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
