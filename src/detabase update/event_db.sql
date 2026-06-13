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
  `quantity` int DEFAULT '1',
  `total_cost` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`assignment_id`),
  KEY `event_id` (`event_id`),
  KEY `event_resources_ibfk_2` (`resource_id`),
  CONSTRAINT `event_resources_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`),
  CONSTRAINT `event_resources_ibfk_2` FOREIGN KEY (`resource_id`) REFERENCES `resources` (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.event_resources: ~0 rows (approximately)

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
	('S-1003', 'gh', 'ghg', 6000);

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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.package_resources: ~8 rows (approximately)
INSERT INTO `package_resources` (`id`, `package_id`, `resource_id`, `quantity`) VALUES
	(1, 'P-1001', 'R-001', 1),
	(2, 'P-1001', 'R-002', 1),
	(3, 'P-1001', 'R-003', 1),
	(4, 'P-1001', 'R-004', 151),
	(5, 'P-1002', 'R-005', 1),
	(6, 'P-1002', 'R-006', 1),
	(8, 'P-1001', 'R-007', 14),
	(10, 'P-1001', 'R-006', 2);

-- Dumping structure for table event_db.resources
CREATE TABLE IF NOT EXISTS `resources` (
  `resource_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `resource_name` varchar(100) NOT NULL,
  `resource_type` varchar(50) NOT NULL,
  `cost_per_item` decimal(10,2) NOT NULL,
  `stock_qty` int DEFAULT '0',
  PRIMARY KEY (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.resources: ~7 rows (approximately)
INSERT INTO `resources` (`resource_id`, `resource_name`, `resource_type`, `cost_per_item`, `stock_qty`) VALUES
	('R-001', 'Professional DJ System', 'Music', 25000.00, 3),
	('R-002', 'Wedding Photography', 'Media', 50000.00, 0),
	('R-003', 'Floral Decorations', 'Decor', 45000.00, 10),
	('R-004', 'Banquet Chairs', 'Furniture', 100.00, 0),
	('R-005', 'Birthday Balloon Decor', 'Decor', 15000.00, 0),
	('R-006', 'Birthday Cake (2kg)', 'Food', 8000.00, 5),
	('R-007', 'jhg', 'Others', 4000.00, 3);

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
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.staff_log: ~145 rows (approximately)
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
	(13, 'S-1000', '2026-06-04 12:36:52', NULL),
	(14, 'S-1000', '2026-06-06 22:14:18', NULL),
	(15, 'S-1000', '2026-06-06 22:19:38', NULL),
	(16, 'S-1000', '2026-06-07 11:08:18', NULL),
	(17, 'S-1000', '2026-06-07 11:12:18', NULL),
	(18, 'S-1000', '2026-06-07 11:12:42', NULL),
	(19, 'S-1000', '2026-06-07 11:40:12', NULL),
	(20, 'S-1000', '2026-06-07 11:45:22', NULL),
	(21, 'S-1000', '2026-06-07 11:48:17', NULL),
	(22, 'S-1000', '2026-06-07 12:04:25', NULL),
	(23, 'S-1000', '2026-06-07 12:07:21', NULL),
	(24, 'S-1000', '2026-06-07 12:09:33', NULL),
	(25, 'S-1000', '2026-06-07 12:15:43', NULL),
	(26, 'S-1000', '2026-06-07 12:16:34', NULL),
	(27, 'S-1000', '2026-06-07 12:22:36', NULL),
	(28, 'S-1000', '2026-06-07 12:49:33', NULL),
	(29, 'S-1000', '2026-06-07 13:04:52', NULL),
	(30, 'S-1000', '2026-06-07 20:36:00', NULL),
	(31, 'S-1000', '2026-06-07 20:37:14', NULL),
	(32, 'S-1000', '2026-06-07 20:39:31', NULL),
	(33, 'S-1000', '2026-06-07 20:40:09', NULL),
	(34, 'S-1000', '2026-06-07 20:41:10', NULL),
	(35, 'S-1000', '2026-06-07 20:41:49', NULL),
	(36, 'S-1000', '2026-06-07 20:42:40', NULL),
	(37, 'S-1000', '2026-06-07 20:43:08', NULL),
	(38, 'S-1000', '2026-06-07 20:44:59', NULL),
	(39, 'S-1000', '2026-06-07 20:48:40', NULL),
	(40, 'S-1000', '2026-06-07 20:49:08', NULL),
	(41, 'S-1000', '2026-06-07 20:49:29', NULL),
	(42, 'S-1000', '2026-06-07 20:49:50', NULL),
	(43, 'S-1000', '2026-06-07 20:50:19', NULL),
	(44, 'S-1000', '2026-06-07 20:50:39', NULL),
	(45, 'S-1000', '2026-06-07 20:51:16', NULL),
	(46, 'S-1000', '2026-06-07 20:54:23', NULL),
	(47, 'S-1000', '2026-06-07 20:57:21', NULL),
	(48, 'S-1000', '2026-06-07 20:58:26', NULL),
	(49, 'S-1000', '2026-06-07 21:03:37', NULL),
	(50, 'S-1000', '2026-06-07 21:03:52', NULL),
	(51, 'S-1000', '2026-06-07 21:05:10', NULL),
	(52, 'S-1000', '2026-06-07 21:07:41', NULL),
	(53, 'S-1000', '2026-06-07 21:09:15', NULL),
	(54, 'S-1000', '2026-06-07 21:09:52', NULL),
	(55, 'S-1000', '2026-06-07 21:11:48', NULL),
	(56, 'S-1000', '2026-06-07 21:13:06', NULL),
	(57, 'S-1000', '2026-06-07 21:13:45', NULL),
	(58, 'S-1000', '2026-06-07 21:14:25', NULL),
	(59, 'S-1000', '2026-06-07 21:14:54', NULL),
	(60, 'S-1000', '2026-06-07 21:16:56', NULL),
	(61, 'S-1000', '2026-06-07 21:18:41', NULL),
	(62, 'S-1000', '2026-06-07 21:44:59', NULL),
	(63, 'S-1000', '2026-06-07 21:46:09', NULL),
	(64, 'S-1000', '2026-06-07 21:48:47', NULL),
	(65, 'S-1000', '2026-06-07 21:49:30', NULL),
	(66, 'S-1000', '2026-06-07 21:55:48', NULL),
	(67, 'S-1000', '2026-06-07 21:58:34', NULL),
	(68, 'S-1000', '2026-06-07 22:10:06', NULL),
	(69, 'S-1000', '2026-06-07 22:13:23', NULL),
	(70, 'S-1000', '2026-06-07 22:14:37', NULL),
	(71, 'S-1000', '2026-06-07 22:16:12', NULL),
	(72, 'S-1000', '2026-06-07 22:17:34', NULL),
	(73, 'S-1000', '2026-06-08 21:06:25', NULL),
	(74, 'S-1000', '2026-06-08 21:10:58', NULL),
	(75, 'S-1000', '2026-06-08 21:11:50', NULL),
	(76, 'S-1000', '2026-06-08 21:14:07', NULL),
	(77, 'S-1000', '2026-06-08 21:16:58', NULL),
	(78, 'S-1000', '2026-06-08 21:27:35', NULL),
	(79, 'S-1000', '2026-06-08 21:29:33', NULL),
	(80, 'S-1000', '2026-06-08 21:46:57', NULL),
	(81, 'S-1000', '2026-06-08 21:56:17', NULL),
	(82, 'S-1000', '2026-06-08 21:59:38', NULL),
	(83, 'S-1000', '2026-06-08 22:02:09', NULL),
	(84, 'S-1000', '2026-06-08 22:16:32', NULL),
	(85, 'S-1000', '2026-06-08 22:52:30', NULL),
	(86, 'S-1000', '2026-06-08 22:53:08', NULL),
	(87, 'S-1000', '2026-06-08 23:20:17', NULL),
	(88, 'S-1000', '2026-06-08 23:22:21', NULL),
	(89, 'S-1000', '2026-06-08 23:22:54', NULL),
	(90, 'S-1000', '2026-06-08 23:25:51', NULL),
	(91, 'S-1000', '2026-06-08 23:26:46', NULL),
	(92, 'S-1000', '2026-06-08 23:30:27', NULL),
	(93, 'S-1000', '2026-06-08 23:31:15', NULL),
	(94, 'S-1000', '2026-06-08 23:32:06', NULL),
	(95, 'S-1000', '2026-06-08 23:35:28', NULL),
	(96, 'S-1000', '2026-06-08 23:36:18', NULL),
	(97, 'S-1000', '2026-06-08 23:36:57', NULL),
	(98, 'S-1000', '2026-06-08 23:39:40', NULL),
	(99, 'S-1000', '2026-06-08 23:41:18', NULL),
	(100, 'S-1000', '2026-06-08 23:41:48', NULL),
	(101, 'S-1000', '2026-06-08 23:52:54', NULL),
	(102, 'S-1000', '2026-06-08 23:55:49', NULL),
	(103, 'S-1000', '2026-06-08 23:57:32', NULL),
	(104, 'S-1000', '2026-06-09 19:34:22', NULL),
	(105, 'S-1000', '2026-06-09 19:35:03', NULL),
	(106, 'S-1000', '2026-06-09 19:42:10', NULL),
	(107, 'S-1000', '2026-06-09 19:42:56', NULL),
	(108, 'S-1000', '2026-06-09 19:54:20', NULL),
	(109, 'S-1000', '2026-06-09 20:00:08', NULL),
	(110, 'S-1000', '2026-06-09 20:01:15', NULL),
	(111, 'S-1000', '2026-06-09 20:04:37', NULL),
	(112, 'S-1000', '2026-06-09 20:05:35', NULL),
	(113, 'S-1000', '2026-06-09 20:06:09', NULL),
	(114, 'S-1000', '2026-06-09 20:07:38', NULL),
	(115, 'S-1000', '2026-06-09 20:08:13', NULL),
	(116, 'S-1000', '2026-06-09 20:10:32', NULL),
	(117, 'S-1000', '2026-06-09 20:11:53', NULL),
	(118, 'S-1000', '2026-06-09 20:13:49', NULL),
	(119, 'S-1000', '2026-06-09 20:14:41', NULL),
	(120, 'S-1000', '2026-06-09 20:15:48', NULL),
	(121, 'S-1000', '2026-06-09 20:16:39', NULL),
	(122, 'S-1000', '2026-06-09 20:17:59', NULL),
	(123, 'S-1000', '2026-06-09 20:22:11', NULL),
	(124, 'S-1000', '2026-06-09 20:29:53', NULL),
	(125, 'S-1000', '2026-06-09 20:33:53', NULL),
	(126, 'S-1000', '2026-06-09 20:41:59', NULL),
	(127, 'S-1000', '2026-06-09 21:09:29', NULL),
	(128, 'S-1000', '2026-06-09 21:11:04', NULL),
	(129, 'S-1000', '2026-06-09 21:16:38', NULL),
	(130, 'S-1000', '2026-06-09 21:22:18', NULL),
	(131, 'S-1000', '2026-06-09 21:56:24', NULL),
	(132, 'S-1000', '2026-06-09 21:59:17', NULL),
	(133, 'S-1000', '2026-06-09 22:00:28', NULL),
	(134, 'S-1000', '2026-06-09 22:03:03', NULL),
	(135, 'S-1000', '2026-06-09 22:22:14', NULL),
	(136, 'S-1000', '2026-06-09 22:23:25', NULL),
	(137, 'S-1000', '2026-06-09 22:24:04', NULL),
	(138, 'S-1000', '2026-06-09 22:41:58', NULL),
	(139, 'S-1000', '2026-06-09 22:43:31', NULL),
	(140, 'S-1000', '2026-06-09 22:45:57', NULL),
	(141, 'S-1000', '2026-06-09 22:47:01', NULL),
	(142, 'S-1000', '2026-06-09 22:53:58', NULL),
	(143, 'S-1000', '2026-06-09 22:54:49', NULL),
	(144, 'S-1000', '2026-06-09 22:55:17', NULL),
	(145, 'S-1000', '2026-06-09 23:11:59', NULL),
	(146, 'S-1000', '2026-06-09 23:12:46', NULL),
	(147, 'S-1000', '2026-06-09 23:13:31', NULL),
	(148, 'S-1000', '2026-06-09 23:16:39', NULL),
	(149, 'S-1000', '2026-06-09 23:17:19', NULL),
	(150, 'S-1000', '2026-06-13 08:18:29', NULL),
	(151, 'S-1000', '2026-06-13 08:19:44', NULL),
	(152, 'S-1000', '2026-06-13 08:20:20', NULL),
	(153, 'S-1000', '2026-06-13 08:26:06', NULL);

-- Dumping structure for table event_db.suppliers
CREATE TABLE IF NOT EXISTS `suppliers` (
  `sup_id` int NOT NULL AUTO_INCREMENT,
  `sup_name` varchar(100) NOT NULL,
  `company_name` varchar(150) DEFAULT NULL,
  `contact_number` varchar(15) NOT NULL,
  `sup_email` varchar(100) DEFAULT NULL,
  `sup_address` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`sup_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.suppliers: ~0 rows (approximately)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
