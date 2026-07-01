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

-- Dumping data for table event_db.billing: ~10 rows (approximately)
INSERT INTO `billing` (`bill_id`, `event_id`, `total_amount`, `advance_payment`, `balance_due`, `payment_status`) VALUES
	('B-0001', 'E-0001', 176500.00, 50000.00, 126500.00, 'Pending'),
	('B-0002', 'E-0002', 105000.00, 105000.00, 0.00, 'Paid'),
	('B-0003', 'E-0003', 407500.00, 100000.00, 307500.00, 'Pending'),
	('B-0004', 'E-0004', 160000.00, 160000.00, 0.00, 'Paid'),
	('B-0005', 'E-0005', 218000.00, 50000.00, 168000.00, 'Pending'),
	('B-0006', 'E-0006', 85000.00, 40000.00, 45000.00, 'Overdue'),
	('B-0007', 'E-0007', 107000.00, 107000.00, 0.00, 'Paid'),
	('B-0008', 'E-0008', 572500.00, 200000.00, 372500.00, 'Pending'),
	('B-0009', 'E-0009', 96000.00, 30000.00, 66000.00, 'Pending'),
	('B-0010', 'E-0010', 275000.00, 275000.00, 0.00, 'Paid');

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

-- Dumping data for table event_db.clients: ~10 rows (approximately)
INSERT INTO `clients` (`client_id`, `client_name`, `nic`, `contact_number`, `email`, `address`) VALUES
	('C-1001', 'Kasun Perera', '981234567V', '0711234567', 'kasun@gmail.com', 'No 45, Main St, Galle'),
	('C-1002', 'Amila Silva', '200112345678', '0777654321', 'amila@yahoo.com', '12/A, Temple Road, Matara'),
	('C-1003', 'Nadeesha Dissanayake', '955678123V', '0765544332', 'nadee@gmail.com', 'Baddegama Town, Galle'),
	('C-1004', 'Suresh Gamage', '881122334V', '0719988776', 'suresh@company.lk', 'Wakwella Road, Galle'),
	('C-1005', 'Tharindu Jayasinghe', '200056789123', '0751122334', 'tharindu@outlook.com', 'Hikkaduwa, Galle'),
	('C-1006', 'Dilani Kariyawasam', '926789456V', '0773344556', 'dilani@gmail.com', 'Koggala, Habaraduwa'),
	('C-1007', 'Mahesh Cooray', '851239876V', '0712233445', 'mahesh@cooray.com', 'Karapitiya, Galle'),
	('C-1008', 'Sanduni Gunawardena', '997894561V', '0768899001', 'sanduni@yahoo.com', 'Ambalangoda'),
	('C-1009', 'Lahiru Peiris', '931234567V', '0771122339', 'lahiru@peiris.lk', 'Tangalle Road, Matara'),
	('C-1010', 'Buddhika Pathirana', '891234567V', '0715566778', 'buddhika@gmail.com', 'Elpitiya, Galle');

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

-- Dumping data for table event_db.event_resources: ~10 rows (approximately)
INSERT INTO `event_resources` (`assignment_id`, `event_id`, `resource_id`, `package_id`, `quantity`, `total_cost`) VALUES
	('ASG-0001', 'E-0001', 'R-001', 'P-1001', 50, 7500.00),
	('ASG-0002', 'E-0001', 'R-002', 'P-1001', 3, 150000.00),
	('ASG-0003', 'E-0002', 'R-004', 'P-1002', 10, 1000.00),
	('ASG-0004', 'E-0003', 'R-006', 'P-1003', 3, 24000.00),
	('ASG-0005', 'E-0004', 'R-001', 'P-1004', 2, 50000.00),
	('ASG-0006', 'E-0005', 'R-003', 'P-1005', 1, 45000.00),
	('ASG-0007', 'E-0006', 'R-005', 'P-1006', 2, 30000.00),
	('ASG-0008', 'E-0007', 'R-007', 'P-1007', 4, 4000.00),
	('ASG-0009', 'E-0008', 'R-008', 'P-1008', 1, 10000.00),
	('ASG-0010', 'E-0009', 'R-009', 'P-1009', 2, 5000.00);

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
  `transport_cost` double DEFAULT NULL,
  `venue` varchar(150) DEFAULT NULL,
  `guest_count` int DEFAULT NULL,
  `event_status` varchar(20) DEFAULT 'Upcoming',
  `Event_Add_Date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`event_id`),
  KEY `staff_id` (`staff_id`),
  KEY `client_ibfk_1` (`client_id`),
  KEY `package_id` (`package_id`),
  KEY `FK_events_suppliers` (`sup_id`),
  CONSTRAINT `events_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`client_id`),
  CONSTRAINT `events_ibfk_2` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`),
  CONSTRAINT `events_ibfk_3` FOREIGN KEY (`package_id`) REFERENCES `package` (`package_id`),
  CONSTRAINT `FK_events_suppliers` FOREIGN KEY (`sup_id`) REFERENCES `suppliers` (`sup_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.events: ~10 rows (approximately)
INSERT INTO `events` (`event_id`, `client_id`, `staff_id`, `package_id`, `sup_id`, `event_type`, `event_date`, `start_time`, `end_time`, `transport_cost`, `venue`, `guest_count`, `event_status`, `Event_Add_Date`) VALUES
	('E-0001', 'C-1001', 'S-1000', 'P-1001', 'S-0001', 'Wedding', '2026-06-28', '09:00:00', '16:00:00', 15000, 'Jetwing Lighthouse, Galle', 150, 'Upcoming', '2026-06-26 11:02:58'),
	('E-0002', 'C-1002', 'S-1001', 'P-1002', 'S-0002', 'Birthday Party', '2026-06-30', '18:00:00', '23:00:00', 25000, 'Town Hall, Matara', 80, 'Upcoming', '2026-06-26 11:02:58'),
	('E-0003', 'C-1003', 'S-1002', 'P-1003', 'S-0003', 'Wedding', '2026-07-05', '08:30:00', '17:00:00', 18000, 'Amari Galle Resort', 300, 'Upcoming', '2026-06-26 11:02:58'),
	('E-0004', 'C-1004', 'S-1003', 'P-1004', 'S-0004', 'Corporate', '2026-07-10', '09:00:00', '13:00:00', 30000, 'Le Grand, Galle', 200, 'Upcoming', '2026-06-26 11:02:58'),
	('E-0005', 'C-1005', 'S-1004', 'P-1005', 'S-0005', 'Beach Party', '2026-07-12', '19:00:00', '02:00:00', 22000, 'Hikkaduwa Beach Club', 120, 'Upcoming', '2026-06-26 11:02:58'),
	('E-0006', 'C-1006', 'S-1005', 'P-1006', 'S-0006', 'Birthday Party', '2026-05-15', '15:00:00', '18:00:00', 20000, 'Fort Printers, Galle', 50, 'Completed', '2026-06-26 11:02:58'),
	('E-0007', 'C-1007', 'S-1006', 'P-1007', 'S-0007', 'Engagement', '2026-05-20', '10:00:00', '14:00:00', 12000, 'Galle Face Hotel', 75, 'Completed', '2026-06-26 11:02:58'),
	('E-0008', 'C-1008', 'S-1007', 'P-1008', 'S-0008', 'Concert', '2026-08-01', '18:00:00', '23:59:00', 35000, 'Samanala Grounds, Galle', 1500, 'Upcoming', '2026-06-26 11:02:58'),
	('E-0009', 'C-1009', 'S-1008', 'P-1009', 'S-0009', 'Get-together', '2026-08-15', '19:00:00', '23:00:00', 16000, 'Mirissa Beach Resort', 60, 'Upcoming', '2026-06-26 11:02:58'),
	('E-0010', 'C-1010', 'S-1009', 'P-1010', 'S-0010', 'Anniversary', '2026-08-25', '19:30:00', '23:30:00', 50000, 'Taj Samudra, Colombo', 100, 'Upcoming', '2026-06-26 11:02:58');

-- Dumping structure for table event_db.package
CREATE TABLE IF NOT EXISTS `package` (
  `package_id` varchar(20) NOT NULL,
  `package_name` varchar(100) NOT NULL,
  `description` text,
  `price` double NOT NULL,
  PRIMARY KEY (`package_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.package: ~10 rows (approximately)
INSERT INTO `package` (`package_id`, `package_name`, `description`, `price`) VALUES
	('P-1001', 'Wedding Premium', 'Full Wedding Package with all services', 210000),
	('P-1002', 'Basic Birthday', 'Simple Birthday Package', 40000),
	('P-1003', 'Customize Pack', 'No Need anything', 50000),
	('P-1004', 'Corporate Seminar Setup', 'Projector, PA sound system, podium, 200 chairs', 120000),
	('P-1005', 'Beach Party Fiesta', 'Outdoor DJ setup, tiki torches, LED lighting', 180000),
	('P-1006', 'Kids Birthday Special', 'Bouncy castle, magic show setup, candy bar', 65000),
	('P-1007', 'Engagement Simple Package', 'Mini sofa set, backdrop flower arch, 50 chairs', 95000),
	('P-1008', 'Musical Concert Mega', 'Line array mega sound, stage trussing, moving head lights', 500000),
	('P-1009', 'Get-together Evening', 'BBQ night lighting setup, acoustic sound system', 75000),
	('P-1010', 'VIP Anniversary Gala', 'Premium table centerpieces, champagne fountain setup', 220000);

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
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.package_resources: ~10 rows (approximately)
INSERT INTO `package_resources` (`id`, `package_id`, `resource_id`, `quantity`) VALUES
	(14, 'P-1001', 'R-001', 1),
	(15, 'P-1001', 'R-002', 2),
	(16, 'P-1001', 'R-004', 140),
	(17, 'P-1002', 'R-005', 1),
	(18, 'P-1002', 'R-006', 5),
	(19, 'P-1003', 'R-007', 10),
	(20, 'P-1004', 'R-008', 1),
	(21, 'P-1005', 'R-003', 2),
	(22, 'P-1006', 'R-005', 3),
	(23, 'P-1007', 'R-010', 1);

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

-- Dumping data for table event_db.resources: ~10 rows (approximately)
INSERT INTO `resources` (`resource_id`, `resource_name`, `resource_type`, `cost_per_item`, `stock_qty`, `sup_id`) VALUES
	('R-001', 'Professional DJ System', 'Music', 25000.00, 3, NULL),
	('R-002', 'Wedding Photography', 'Media', 50000.00, 5, NULL),
	('R-003', 'Floral Decorations', 'Decor', 45000.00, 10, NULL),
	('R-004', 'Banquet Chairs', 'Furniture', 100.00, 200, NULL),
	('R-005', 'Birthday Balloon Decor', 'Decor', 15000.00, 8, NULL),
	('R-006', 'Birthday Cake (2kg)', 'Food', 8000.00, 5, NULL),
	('R-007', 'LED Par Light 54W', 'Lighting', 1000.00, 40, NULL),
	('R-008', 'Sony 4K Projector & Screen', 'Others', 10000.00, 3, NULL),
	('R-009', 'Cordless Shure Mic Set', 'Music', 2500.00, 10, NULL),
	('R-010', 'Red Carpet (50 Meters)', 'Decor', 5000.00, 5, NULL);

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

-- Dumping data for table event_db.staff: ~10 rows (approximately)
INSERT INTO `staff` (`staff_id`, `staff_name`, `contact_number`, `staff_email`, `staff_address`, `Id`, `role`, `password`, `first_time_log`, `make_time`) VALUES
	('S-1000', 'Chamika Sandeepa', '0728880547', 'infor.chami1@gmail.com', 'galle', '200625103468', 'admin', 'a4ayc/80/OGda4BO/1o/V0etpOqiLx1JwB5S3beHW0s=', 1, '2026-06-26 11:02:58'),
	('S-1001', 'Ruwan Fernando', '0710000654', 'trt@gmmail.com', 'Galle Town', '5646546', 'Event Planner', 'a4ayc/80/OGda4BO/1o/V0etpOqiLx1JwB5S3beHW0s=', 1, '2026-06-26 11:02:58'),
	('S-1002', 'Danushka Senanayake', '0771122334', 'danushka@company.com', 'Karapitiya', 'EMP03', 'Event Planner', 'a4ayc/80/OGda4BO/1o/V0etpOqiLx1JwB5S3beHW0s=', 0, '2026-06-26 11:02:58'),
	('S-1003', 'Kavindi de Silva', '0712233445', 'kavindi@company.com', 'Hikkaduwa', 'EMP04', 'Event Planner', 'a4ayc/80/OGda4BO/1o/V0etpOqiLx1JwB5S3beHW0s=', 0, '2026-06-26 11:02:58'),
	('S-1004', 'Pradeep Kumara', '0763344556', 'pradeep@company.com', 'Ambalangoda', 'EMP05', 'Event Planner', 'a4ayc/80/OGda4BO/1o/V0etpOqiLx1JwB5S3beHW0s=', 0, '2026-06-26 11:02:58'),
	('S-1005', 'Nuwan Pradeep', '0774455667', 'nuwan@company.com', 'Matara', 'EMP06', 'Event Planner', 'a4ayc/80/OGda4BO/1o/V0etpOqiLx1JwB5S3beHW0s=', 0, '2026-06-26 11:02:58'),
	('S-1006', 'Chathura Mendis', '0715566778', 'chathura@company.com', 'Elpitiya', 'EMP07', 'admin', 'a4ayc/80/OGda4BO/1o/V0etpOqiLx1JwB5S3beHW0s=', 0, '2026-06-26 11:02:58'),
	('S-1007', 'Sachini Anuradha', '0756677889', 'sachini@company.com', 'Baddegama', 'EMP08', 'Event Planner', 'a4ayc/80/OGda4BO/1o/V0etpOqiLx1JwB5S3beHW0s=', 0, '2026-06-26 11:02:58'),
	('S-1008', 'Isuru Udana', '0767788990', 'isuru@company.com', 'Galle', 'EMP09', 'Event Planner', 'a4ayc/80/OGda4BO/1o/V0etpOqiLx1JwB5S3beHW0s=', 0, '2026-06-26 11:02:58'),
	('S-1009', 'Malith Shanaka', '0778899001', 'malith@company.com', 'Weligama', 'EMP10', 'Event Planner', 'a4ayc/80/OGda4BO/1o/V0etpOqiLx1JwB5S3beHW0s=', 0, '2026-06-26 11:02:58');

-- Dumping structure for table event_db.staff_log
CREATE TABLE IF NOT EXISTS `staff_log` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `staff_id` varchar(50) NOT NULL,
  `login_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `logout_time` datetime DEFAULT NULL,
  PRIMARY KEY (`log_id`),
  KEY `FK_staff_log_staff` (`staff_id`),
  CONSTRAINT `FK_staff_log_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB AUTO_INCREMENT=261 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table event_db.staff_log: ~12 rows (approximately)
INSERT INTO `staff_log` (`log_id`, `staff_id`, `login_time`, `logout_time`) VALUES
	(246, 'S-1000', '2026-06-26 11:04:53', '2026-06-26 11:06:54'),
	(247, 'S-1000', '2026-06-26 11:08:17', '2026-06-26 11:08:35'),
	(248, 'S-1001', '2026-06-26 11:09:54', '2026-06-26 11:10:11'),
	(249, 'S-1000', '2026-06-26 11:12:38', '2026-06-26 11:12:41'),
	(250, 'S-1001', '2026-06-26 11:12:52', '2026-06-26 11:13:04'),
	(251, 'S-1000', '2026-06-26 11:16:06', '2026-06-26 11:16:11'),
	(252, 'S-1000', '2026-06-26 11:16:50', '2026-06-26 11:17:04'),
	(253, 'S-1001', '2026-06-26 11:17:15', '2026-06-26 11:17:58'),
	(254, 'S-1000', '2026-06-26 11:19:43', '2026-06-26 11:19:49'),
	(255, 'S-1001', '2026-06-26 11:19:59', '2026-06-26 11:20:02'),
	(256, 'S-1001', '2026-06-26 11:20:19', '2026-06-26 11:20:22'),
	(257, 'S-1001', '2026-06-26 11:20:41', '2026-06-26 11:21:01'),
	(258, 'S-1000', '2026-06-26 11:21:10', '2026-06-26 11:22:15'),
	(259, 'S-1000', '2026-07-01 09:42:32', '2026-07-01 09:43:48'),
	(260, 'S-1000', '2026-07-01 11:12:45', '2026-07-01 11:12:53');

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

-- Dumping data for table event_db.suppliers: ~10 rows (approximately)
INSERT INTO `suppliers` (`sup_id`, `sup_name`, `contact_number`, `nic`, `sup_address`, `vehicle_modal`, `vehicle_no`, `vehicle_Price`, `Status`) VALUES
	('S-0001', 'Saman Transport', '0714455667', '851234567V', 'Galle Town', 'Toyota HiAce', 'WP-SP-1234', '15000.0', 'Active'),
	('S-0002', 'Kumara Lorries', '0778899001', '791122334V', 'Mapalagama', 'Isuzu Elf Lorry', 'SP-LL-9876', '25000.0', 'Active'),
	('S-0003', 'Perera Cabs & Vans', '0761122334', '881234567V', 'Karapitiya', 'Nissan Caravan', 'SP-PD-4567', '18000.0', 'Active'),
	('S-0004', 'Southern Logistics', '0719988771', '901234567V', 'Matara Town', 'Mitsubishi Canter', 'WP-LC-3344', '30000.0', 'Active'),
	('S-0005', 'Jagath Lorry Service', '0752233445', '821234567V', 'Baddegama', 'Tata Lorry 1615', 'SP-LX-1122', '22000.0', 'Active'),
	('S-0006', 'Ruwan Express Transport', '0773344556', '931234567V', 'Hikkaduwa', 'Toyota Dyna', 'SP-D-5566', '20000.0', 'Active'),
	('S-0007', 'Ajith Movers', '0764455667', '751234567V', 'Ambalangoda', 'Mahindra Bolero', 'SP-PI-8899', '12000.0', 'Active'),
	('S-0008', 'Silva Heavy Movers', '0715566778', '811234567V', 'Galle', 'Isuzu Forward', 'WP-LM-4455', '35000.0', 'Active'),
	('S-0009', 'Nihal Van Hire', '0776677889', '891234567V', 'Elpitiya', 'Toyota KDH', 'SP-PH-7788', '16000.0', 'Active'),
	('S-0010', 'Lanka Event Logistics', '0757788990', '911234567V', 'Colombo 03', 'Container Truck', 'WP-LY-9900', '50000.0', 'Active');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
