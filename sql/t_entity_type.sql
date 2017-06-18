-- phpMyAdmin SQL Dump
-- version 4.1.6
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 2017-06-18 05:33:28
-- 服务器版本： 5.6.16
-- PHP Version: 5.5.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `openie`
--

-- --------------------------------------------------------

--
-- 表的结构 `t_entity_type`
--

CREATE TABLE IF NOT EXISTS `t_entity_type` (
  `entity_type_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `entity_type_pid` int(10) unsigned DEFAULT NULL,
  `description` varchar(32) NOT NULL,
  PRIMARY KEY (`entity_type_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=14 ;

--
-- 转存表中的数据 `t_entity_type`
--

INSERT INTO `t_entity_type` (`entity_type_id`, `entity_type_pid`, `description`) VALUES
(1, NULL, 'MISC'),
(2, NULL, 'NUMBER'),
(3, NULL, 'TIME'),
(4, NULL, 'MONEY'),
(5, NULL, 'GPE'),
(6, NULL, 'DATE'),
(7, NULL, 'ORDINAL'),
(8, NULL, 'PERSON'),
(9, NULL, 'DEMONYM'),
(10, NULL, 'LOCATION'),
(11, NULL, 'PERCENT'),
(12, NULL, 'ORGANIZATION'),
(13, NULL, 'FACILITY');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
