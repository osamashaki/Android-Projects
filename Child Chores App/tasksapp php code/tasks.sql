-- phpMyAdmin SQL Dump
-- version 2.10.2
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: Oct 09, 2015 at 11:55 AM
-- Server version: 5.0.45
-- PHP Version: 5.2.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- Database: `tasks`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `child`
-- 

CREATE TABLE `child` (
  `uid` int(11) NOT NULL auto_increment,
  `unique_id` varchar(25) NOT NULL,
  `firstname` varchar(25) NOT NULL,
  `lastname` varchar(25) NOT NULL,
  `email` varchar(25) NOT NULL,
  `encrypted_password` varchar(80) NOT NULL,
  `salt` varchar(10) NOT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

-- 
-- Dumping data for table `child`
-- 

INSERT INTO `child` VALUES (1, '5616260db118a3.56744648', 'Ahmad', 'test', 'ahmad@gmail.com', 'tSRB3VfUbr+7WE2PNJ2I0Pz7OE05YmVkMTIxYTgz', '9bed121a83', '2015-10-08 16:15:09');

-- --------------------------------------------------------

-- 
-- Table structure for table `chore`
-- 

CREATE TABLE `chore` (
  `id` int(11) NOT NULL auto_increment,
  `choretitle` varchar(80) NOT NULL,
  `choredesc` varchar(100) NOT NULL,
  `child_id` varchar(20) NOT NULL,
  `choredate` datetime NOT NULL,
  `status` varchar(25) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

-- 
-- Dumping data for table `chore`
-- 

INSERT INTO `chore` VALUES (1, 'new', 'clean bedroom', '1', '2015-10-05 21:02:07', 'finish');
INSERT INTO `chore` VALUES (2, 'new2', 'new2', '2', '2015-10-09 17:55:14', '');

-- --------------------------------------------------------

-- 
-- Table structure for table `parent`
-- 

CREATE TABLE `parent` (
  `uid` int(11) NOT NULL auto_increment,
  `unique_id` varchar(25) NOT NULL,
  `firstname` varchar(25) NOT NULL,
  `lastname` varchar(25) NOT NULL,
  `email` varchar(25) NOT NULL,
  `encrypted_password` varchar(80) NOT NULL,
  `salt` varchar(10) NOT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

-- 
-- Dumping data for table `parent`
-- 

INSERT INTO `parent` VALUES (1, '5616379e41d5f2.14085592', 'Yousof', 'test', 'yousof@gmail.com', 'xGFWIr8N/QOcLroe6mwSiv3X4+YwMWI2MWI5ZGRk', '01b61b9ddd', '2015-10-08 17:30:06');

-- --------------------------------------------------------

-- 
-- Table structure for table `points`
-- 

CREATE TABLE `points` (
  `id` int(11) NOT NULL auto_increment,
  `child_id` int(11) NOT NULL,
  `chpoints` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- 
-- Dumping data for table `points`
-- 

