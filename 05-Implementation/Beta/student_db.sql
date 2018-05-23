-- phpMyAdmin SQL Dump
-- version 4.7.6
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 23, 2018 at 04:15 AM
-- Server version: 10.1.29-MariaDB
-- PHP Version: 7.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `student_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `events_info`
--

CREATE TABLE `events_info` (
  `item` int(11) NOT NULL,
  `date` text NOT NULL,
  `title` text NOT NULL,
  `prof` text NOT NULL,
  `subject` text NOT NULL,
  `time` text NOT NULL,
  `description` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `events_info`
--

INSERT INTO `events_info` (`item`, `date`, `title`, `prof`, `subject`, `time`, `description`) VALUES
(7, '1530061200000 6/27/2018', 'DCS Grade Consultation', 'Rose Ann Zuniga', '-', '1:00 pm', 'Prepare'),
(19, '1527296400000 5/26/2018', 'CS 145 Synthesis', 'Wilson Tan', 'CS 145', '1:00 pm', 'Prepare'),
(20, '1527296400000 5/26/2018', 'Stat 130 Final Exam', 'Sabrina Romasoc', 'Stat 130', '2:00 - 4:00 pm', 'Please Review'),
(29, '1527296400000 5/26/2018', 'CS 145 Assembly', 'Edgar Felizmenio', 'CS 145', '9:00 am', 'Please come'),
(35, '1527728400000 5/31/2018', 'CS 12 MP Deadline', 'Mario Carreon', 'CS 12', '11:00 pm', 'Pass on time');

-- --------------------------------------------------------

--
-- Table structure for table `login_info`
--

CREATE TABLE `login_info` (
  `item` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `surname` text NOT NULL,
  `password` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `login_info`
--

INSERT INTO `login_info` (`item`, `id`, `surname`, `password`) VALUES
(1, 201422070, 'test', 'test'),
(34, 201422071, 'Valencia', 'test123'),
(35, 201422072, 'Valencia', 'test123'),
(36, 201422073, 'Valencia', 'test123'),
(37, 201422074, 'Valencia', 'test123'),
(38, 201422075, 'Valencia', 'test123'),
(39, 201422076, 'Valencia', 'test123');

-- --------------------------------------------------------

--
-- Table structure for table `requests_info`
--

CREATE TABLE `requests_info` (
  `item` int(11) NOT NULL,
  `id` text NOT NULL,
  `date` text NOT NULL,
  `title` text NOT NULL,
  `prof` text NOT NULL,
  `subject` text NOT NULL,
  `time` text NOT NULL,
  `description` text NOT NULL,
  `status` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `requests_info`
--

INSERT INTO `requests_info` (`item`, `id`, `date`, `title`, `prof`, `subject`, `time`, `description`, `status`) VALUES
(4, '201422070', '5/26/2018', 'CS 150 Long Exam', 'Ma\'am Rae', 'CS 150', '4:00 pm - 6:00 pm', 'Answer Sheets will be provided', 'pending'),
(5, '201422070', '5/26/2018', 'CS 11 HOCE Review', 'Ma\'am Rose Zuniga', 'CS 11', '4:00 pm - 5:00 pm', 'Take this review session.', 'rejected'),
(7, '201422070', '5/27/2018', 'CS 135 Long Exam', 'Ma\'am Jhoi Clemente', 'CS 135', '3:00 pm - 5:00 pm', 'Be on time.', 'pending'),
(8, '201422070', '5/27/2018', 'CS 150 Long Exam', 'Ma\'am Rae', 'CS 150', '3:00 pm - 5:00 pm', 'Bring yellow pad.', 'rejected'),
(9, '201422070', '5/26/2018', 'CS 135 Long Exam', 'Ma\'am Jhoi Clemente', 'CS 135', '6:00 pm - 12:00 pm ', 'Bring pillows.', 'pending'),
(76, '201422070', '5/31/2018', 'CS 12 MP Deadline', 'Mario Carreon', 'CS 12', '23:00', 'Pass on time', 'accepted');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `events_info`
--
ALTER TABLE `events_info`
  ADD PRIMARY KEY (`item`);

--
-- Indexes for table `login_info`
--
ALTER TABLE `login_info`
  ADD PRIMARY KEY (`item`);

--
-- Indexes for table `requests_info`
--
ALTER TABLE `requests_info`
  ADD PRIMARY KEY (`item`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `events_info`
--
ALTER TABLE `events_info`
  MODIFY `item` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT for table `login_info`
--
ALTER TABLE `login_info`
  MODIFY `item` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT for table `requests_info`
--
ALTER TABLE `requests_info`
  MODIFY `item` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=77;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
