-- phpMyAdmin SQL Dump
-- version 2.10.2
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: May 29, 2016 at 01:55 PM
-- Server version: 5.0.45
-- PHP Version: 5.2.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Database: `ukmproject`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `msg`
-- 

CREATE TABLE `msg` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `phone` varchar(50) NOT NULL,
  `textmsg` varchar(250) NOT NULL,
  `date_` varchar(25) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

-- 
-- Dumping data for table `msg`
-- 

INSERT INTO `msg` VALUES (1, 'test', 'test@gmail.com', '0123456789', 'test msg', '2016-05-21 03:09:34');
INSERT INTO `msg` VALUES (2, 'test', 'test@gmail.com', '0123456789', 'test msg', '2016-05-21 03:10:49');
INSERT INTO `msg` VALUES (3, 'new name', 'new@gmail.com', '01122334455', 'new msg test', '2016-05-21 03:16:04');
INSERT INTO `msg` VALUES (4, 'test from mobile', 'osamashaki@gmail.com', '0111112233', 'testtt\n', '2016-05-29 03:48:31');

-- --------------------------------------------------------

-- 
-- Table structure for table `restaurant`
-- 

CREATE TABLE `restaurant` (
  `id` int(11) NOT NULL auto_increment,
  `resname` varchar(50) NOT NULL,
  `resmobile` varchar(50) NOT NULL,
  `openinghours` varchar(50) NOT NULL,
  `dishes` varchar(200) NOT NULL,
  `address` varchar(200) NOT NULL,
  `location` varchar(200) NOT NULL,
  `imagepath` varchar(200) NOT NULL,
  `category` varchar(50) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

-- 
-- Dumping data for table `restaurant`
-- 

INSERT INTO `restaurant` VALUES (1, 'Hadramout Restaurant', '0123456789', '12PM-2AM', 'Pizza, Nasi Arab', 'KL, Bukit Bintang', '3.146817, 101.710719', 'arabic.jpg', 'arabic');
INSERT INTO `restaurant` VALUES (2, 'AB Malay Restaurant', '0133658974', '24 hours', 'All Malay food', 'Serdang Perdana', '3.036942, 101.707667', 'malay.jpg', 'malay');
INSERT INTO `restaurant` VALUES (3, 'Chinese Restaurant', '014563287', '12PM-12AM', 'All Chinese food and drinks', 'Serdang Perdana', '3.036942, 101.707667', 'chinese.jpg', 'chinese');
INSERT INTO `restaurant` VALUES (4, 'Sea Food Restaurant', '012356987', '12PM-2AM', 'All Sea Food', 'Serdang Perdana', '3.036942, 101.707667', 'seafood.jpg', 'seafood');
