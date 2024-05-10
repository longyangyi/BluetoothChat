/*
Navicat MySQL Data Transfer

Source Server         : aaaa
Source Server Version : 50527
Source Host           : localhost:3306
Source Database       : aaaa

Target Server Type    : MYSQL
Target Server Version : 50527
File Encoding         : 65001

Date: 2018-10-04 15:36:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `diagnosticdata`
-- ----------------------------
DROP TABLE IF EXISTS `diagnosticdata`;
CREATE TABLE `diagnosticdata` (
  `patientWalletDID` varchar(100) DEFAULT NULL,
  `doctorWalletDID` varchar(100) DEFAULT NULL,
  `fromTime` varchar(100) DEFAULT NULL,
  `toTime` varchar(100) DEFAULT NULL,
  `transactionID` varchar(100) DEFAULT NULL,
  `treatFee` int(11) DEFAULT NULL,
  `additionalInfomation` varchar(100) DEFAULT NULL,
  `diaTime` varchar(100) DEFAULT NULL,
  `diaResult` varchar(100) DEFAULT NULL,
  `whetherToConsult` varchar(100) DEFAULT NULL,
  `reservationResult` varchar(100) DEFAULT NULL,
  `checked` varchar(100) DEFAULT NULL,
  `reserved` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of diagnosticdata
-- ----------------------------
INSERT INTO `diagnosticdata` VALUES ('did:axn:88b8f1fd-3f3e-4a8c-87ac-335ad7fe01a5', 'did:axn:8b4541fc-38ee-4996-aa61-f57a22693b78', '', '', '', '5', '', '2018-10-01 01-05-16', '密密麻麻', 'true', '呵呵红红火火', 'true', 'true');
INSERT INTO `diagnosticdata` VALUES ('did:axn:88b8f1fd-3f3e-4a8c-87ac-335ad7fe01a5', 'did:axn:8b4541fc-38ee-4996-aa61-f57a22693b78', '2018-08-08 00-00-00', '2018-10-01 00-00-00', '', '5', '八月份日', '2018-10-01 08-27-56', '', '', '', 'true', 'false');
INSERT INTO `diagnosticdata` VALUES ('did:axn:dac47ccc-ed61-4edd-ae76-c8f72ce8df51', 'did:axn:8b4541fc-38ee-4996-aa61-f57a22693b78', '2018-08-08 00-00-00', '2018-10-01 09-00-00', '', '5', '这段时间内运动量较大，可能平均心率过快', '2018-10-01 08-36-27', '心率不稳，注意修养', 'true', '20181005这天在xx路xx号个人诊所', 'true', 'true');
INSERT INTO `diagnosticdata` VALUES ('did:axn:88b8f1fd-3f3e-4a8c-87ac-335ad7fe01a5', 'did:axn:8b4541fc-38ee-4996-aa61-f57a22693b78', '2018-09-30 00-00-00', '2018-10-03 00-00-00', '', '5', '10.2当天运动量过大，心率可能过快', '2018-10-02 15-31-29', '身体很不好', 'true', '10.20日下午，安徽合肥xx路xx号，前来会诊', 'true', 'true');

-- ----------------------------
-- Table structure for `heart`
-- ----------------------------
DROP TABLE IF EXISTS `heart`;
CREATE TABLE `heart` (
  `fileName` varchar(100) DEFAULT NULL,
  `access` varchar(100) DEFAULT NULL,
  `POEID` varchar(100) DEFAULT NULL,
  `hash` varchar(100) DEFAULT NULL,
  `filePath` varchar(100) DEFAULT NULL,
  `startTime` varchar(100) DEFAULT NULL,
  `endTime` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of heart
-- ----------------------------
INSERT INTO `heart` VALUES ('2018-09-30 19-24-39', 'yilongyang', 'did:axn:ccdefcdf-dd78-4902-a998-2ad3df1021c3', 'c374db420a28be420c3d3151fce4c90d4d96cd50457ff30b611c9ccb612362eb', 'C:\\digitalMedicalServerFolder\\usersAcquisitionData\\yilongyang\\2018-09-30 19-24-39', '2018-09-30 19-22-03', '2018-09-30 19-24-37');
INSERT INTO `heart` VALUES ('2018-09-30 21-50-40', 'yilongyang', 'did:axn:d917799c-8f8a-485d-81d9-42b00e92b6c8', '94cb9e62c81d4c97178e6694caa38e4a86a8eedbfcc7723a9e74c1386ccfc60d', 'C:\\digitalMedicalServerFolder\\usersAcquisitionData\\yilongyang\\2018-09-30 21-50-40', '2018-09-30 21-50-24', '2018-09-30 21-50-37');
INSERT INTO `heart` VALUES ('2018-10-01 08-34-46', 'yonghu1', 'did:axn:9e7419b4-f89f-42a6-ad94-eed9282cea28', '5552d54898533930cd7022532e558162f6e55a6915a7b37c16afcb29299413f7', 'C:\\digitalMedicalServerFolder\\usersAcquisitionData\\yonghu1\\2018-10-01 08-34-46', '2018-10-01 08-34-26', '2018-10-01 08-34-44');
INSERT INTO `heart` VALUES ('2018-10-02 15-27-04', 'yilongyang', 'did:axn:83265ec1-0249-4d53-8129-c260339de1c0', '5848e5f3b1a447e3451f9b345042414837b4be4e4995c42d365229017a9c8a6a', 'C:\\digitalMedicalServerFolder\\usersAcquisitionData\\yilongyang\\2018-10-02 15-27-04', '2018-10-02 15-25-59', '2018-10-02 15-27-02');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `access` varchar(100) NOT NULL,
  `password` varchar(100) DEFAULT NULL,
  `phoneNumber` varchar(100) DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `walletDID` varchar(100) DEFAULT NULL,
  `endpoint` varchar(100) DEFAULT NULL,
  `privateKey` varchar(100) DEFAULT NULL,
  `publicKey` varchar(100) DEFAULT NULL,
  `age` varchar(100) DEFAULT NULL,
  `doctorInfo` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`access`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('Doctor1', 'Ab123456', '13912341234', 'doctor', 'did:axn:8b4541fc-38ee-4996-aa61-f57a22693b78', '8d62157c8f04bbe9784104532d08c9acf807e83783872e7fa962f2047985492c', 'C5JmrK3ELM58jHDfMMskww3HWQjAfnQAK+MH8d6Gu5mUR3O7PuGqfyV8Ykbn+tb/SXdnGV2IpQh/4W32VR1qqw==', 'lEdzuz7hqn8lfGJG5/rW/0l3ZxldiKUIf+Ft9lUdaqs=', '35', '张医生，安医大二附属医院外科医生');
INSERT INTO `user` VALUES ('Doctor2', 'Ab123456', '13945674567', 'doctor', 'did:axn:200e6a67-7d47-4f65-b970-bee267be9d79', '0ba63db757b0d07b3fb3104257f05231c23203700709ca88b1309b06ae045309', 'RF1dQY931G0tAUAKcVFpTIgdWGvR9Zm42mXFxPS2Pz5bb0ELVGg2Nh0NJj9R8WHqQuw2lUBwHGRpK4H3HvmEbw==', 'W29BC1RoNjYdDSY/UfFh6kLsNpVAcBxkaSuB9x75hG8=', '40', '李医生，北京协和医院内科医生');
INSERT INTO `user` VALUES ('Doctor5', 'Ab123456', '', 'doctor', 'did:axn:49a10c79-ed3b-4d29-af96-42817d5cf99c', '4003e4164e80633b9ffcc98dc78f5c5ae343c4bc782ac731d794c84669afd463', 'GlOqW97fx8rBTSyKfyhE7Q7RG6gGguOSsIMkfR034+XsbvKjxQNXd2f9cTa+NmCBxos9Gk5KHUewIxGEZYEhCg==', '7G7yo8UDV3dn/XE2vjZggcaLPRpOSh1HsCMRhGWBIQo=', '', 'Hhhhhhhh');
INSERT INTO `user` VALUES ('yilongyang', 'Ab123456', '13739220456', 'patient', 'did:axn:88b8f1fd-3f3e-4a8c-87ac-335ad7fe01a5', 'ced35c0d6bd1a82f68abf5d555323b04220edf81d3d19b48d04cbfb66e1491b2', 'ZNVuPdY4Aq7ucWq9aO8Z7c4VgZJWE68G583MEcDiwCFYHG6O2s5a++gZ7zhnUIXtloFH3C7nqzh7u6Wg5yF+hQ==', 'WBxujtrOWvvoGe84Z1CF7ZaBR9wu56s4e7uloOchfoU=', '18', null);
INSERT INTO `user` VALUES ('yonghu1', 'Ab123456', '13912345678', 'patient', 'did:axn:dac47ccc-ed61-4edd-ae76-c8f72ce8df51', '2f444fea2824bc0636a087a548e78822a7344988e20275a7f5f9ac5b476985c8', 'DQhRYjYhqulD7uHe8qVaWjgaxRQgE6x4IcLp1VXEibgsplgbssYfK/VnhzOiHF8pqnQL60MBlBTfHWH7ubIu5Q==', 'LKZYG7LGHyv1Z4czohxfKap0C+tDAZQU3x1h+7myLuU=', '20', '');
INSERT INTO `user` VALUES ('yonghuming1', 'Ab123456', '13912341234', 'patient', 'did:axn:f3587b35-7f67-471f-9ae2-f41480e329f1', '357504d25bce887561765b88a989ac3fd247c9c026fb8eb37a3c3d684d8cd11f', 'Jt7hrBA8Fl7IxxThsQim8O1eiMkl2fqnY4XM7R8e2KMoUU0ZT2JMhg0lDqsF8C8ohMEj8WFdQkvf06Lz+q/Nrw==', 'KFFNGU9iTIYNJQ6rBfAvKITBI/FhXUJL39Oi8/qvza8=', '20', '');
