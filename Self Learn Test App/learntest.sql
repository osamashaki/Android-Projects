-- phpMyAdmin SQL Dump
-- version 2.10.2
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: Sep 15, 2015 at 09:09 AM
-- Server version: 5.0.45
-- PHP Version: 5.2.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Database: `learntest`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `result`
-- 

CREATE TABLE `result` (
  `id` int(11) NOT NULL auto_increment,
  `stu_id` varchar(25) NOT NULL,
  `result1` varchar(25) NOT NULL,
  `result2` varchar(25) NOT NULL,
  `result3` varchar(25) NOT NULL,
  `testdate` varchar(25) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=10 ;

-- 
-- Dumping data for table `result`
-- 

INSERT INTO `result` VALUES (2, '4', '3', '0', '0', '06/09/2015');
INSERT INTO `result` VALUES (3, '4', '3', '0', '0', '06/09/2015');
INSERT INTO `result` VALUES (4, '4', '1', '0', '2', '06/09/2015');
INSERT INTO `result` VALUES (5, '4', '1', '0', '2', '06/09/2015');
INSERT INTO `result` VALUES (9, '4', '0', '0', '3', '06/09/2015');

-- --------------------------------------------------------

-- 
-- Table structure for table `student`
-- 

CREATE TABLE `student` (
  `uid` int(11) NOT NULL auto_increment,
  `unique_id` varchar(25) NOT NULL,
  `firstname` varchar(25) NOT NULL,
  `lastname` varchar(25) NOT NULL,
  `username` varchar(25) NOT NULL,
  `email` varchar(50) NOT NULL,
  `encrypted_password` varchar(80) NOT NULL,
  `salt` varchar(10) NOT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

-- 
-- Dumping data for table `student`
-- 

INSERT INTO `student` VALUES (1, '558c347c3b1327.30770549', 'waleed', 'ww', 'waleed', 'waleed@gmail.com', 'jMRzg33uUr/aD60Fuu4aQJycPys0MDgwYmI5Y2Nl', '4080bb9cce', '2015-06-26 01:03:56');
INSERT INTO `student` VALUES (4, '55c32337b1ba75.48310072', 'test2', 'test lastname', 'test ', 'test@gmail.com', 'v2xZvpP6JRls+rzHTy10Vf+ISoE3NTI4NTBkMjU3', '752850d257', '2015-08-06 17:04:55');
